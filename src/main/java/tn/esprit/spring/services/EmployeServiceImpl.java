package tn.esprit.spring.services;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Timesheet;
import tn.esprit.spring.repository.ContratRepository;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.TimesheetRepository;

@Service
public class EmployeServiceImpl implements IEmployeService {

	@Autowired
	EmployeRepository employeRepository;
	@Autowired
	DepartementRepository deptRepoistory;
	@Autowired
	ContratRepository contratRepoistory;
	@Autowired
	TimesheetRepository timesheetRepository;

	private static final Logger l = LogManager.getLogger(EmployeServiceImpl.class);

	@Override
	public Employe authenticate(String login, String password) {
		l.info("In  authenticate : "); 
		return employeRepository.getEmployeByEmailAndPassword(login, password);

	}

	@Override
	public int addOrUpdateEmploye(Employe employe) {
		l.info("In addOrUpdateEmploye: " );
		employeRepository.save(employe);
		l.info("Out addOrUpdateEmploye: " );
		return employe.getId();
	}


	public void mettreAjourEmailByEmployeId(String email, int employeId) {
		l.info("In mettreAjourEmailByEmployeId: " );

		Employe employe = employeRepository.findById(employeId).get();
		employe.setEmail(email);
		employeRepository.save(employe);
		l.info("Out mettreAjourEmailByEmployeId: " );


	}

	@Transactional	
	public void affecterEmployeADepartement(int employeId, int depId) {
		l.info("In affecterEmployeADepartement: " );

		Departement depManagedEntity = deptRepoistory.findById(depId).get();
		Employe employeManagedEntity = employeRepository.findById(employeId).get();

		if(depManagedEntity.getEmployes() == null){

			List<Employe> employes = new ArrayList<>();
			employes.add(employeManagedEntity);
			depManagedEntity.setEmployes(employes);
		}else{

			depManagedEntity.getEmployes().add(employeManagedEntity);
		}

		// à ajouter? 
		deptRepoistory.save(depManagedEntity); 
		l.info("Out affecterEmployeADepartement: " );


	}
	@Transactional
	public void desaffecterEmployeDuDepartement(int employeId, int depId)
	{		
		l.info("In desaffecterEmployeDuDepartement: " );

		Departement dep = deptRepoistory.findById(depId).get();

		int employeNb = dep.getEmployes().size();
		for(int index = 0; index < employeNb; index++){
			if(dep.getEmployes().get(index).getId() == employeId){
				dep.getEmployes().remove(index);
				break;//a revoir
			}
		}
		l.info("Out desaffecterEmployeDuDepartement: " );

	} 
	
	// Tablesapce (espace disque) 

	public int ajouterContrat(Contrat contrat) {
		contratRepoistory.save(contrat);
		return contrat.getReference();
	}

	public void affecterContratAEmploye(int contratId, int employeId) {
		l.info("In affecterContratAEmploye: " );

		Contrat contratManagedEntity = contratRepoistory.findById(contratId).get();
		Employe employeManagedEntity = employeRepository.findById(employeId).get();

		contratManagedEntity.setEmploye(employeManagedEntity);
		contratRepoistory.save(contratManagedEntity);
		l.info("Out affecterContratAEmploye: " );

		

	}

	public String getEmployePrenomById(int employeId) {
		l.info("Int getEmployePrenomById: " );

		Employe employeManagedEntity = employeRepository.findById(employeId).get();
		l.debug("getEmployePrenomById return: "+employeManagedEntity.getPrenom());
		return employeManagedEntity.getPrenom();
	}
	 
	public void deleteEmployeById(int employeId)
	{
		l.info("Int deleteEmployeById: " );

		Employe employe = employeRepository.findById(employeId).get();

		//Desaffecter l'employe de tous les departements
		//c'est le bout master qui permet de mettre a jour
		//la table d'association
		for(Departement dep : employe.getDepartements()){
			dep.getEmployes().remove(employe);
		}

		employeRepository.delete(employe);
		l.info("Out deleteEmployeById: " );

	}

	public void deleteContratById(int contratId) {
		l.info("In deleteContratById: " );

		Contrat contratManagedEntity = contratRepoistory.findById(contratId).get();
		contratRepoistory.delete(contratManagedEntity);
		l.info("Out deleteContratById: " );


	}

	public int getNombreEmployeJPQL() {
		l.info("In getNombreEmployeJPQL: " );
		l.debug("getNombreEmployeJPQL reutn: "+employeRepository.countemp());
		return employeRepository.countemp();
	}

	public List<String> getAllEmployeNamesJPQL() {
		l.info("In getAllEmployeNamesJPQL ");
		l.debug("getAllEmployeNamesJPQL reutn: "+employeRepository.employeNames());

		return employeRepository.employeNames();

	}

	public List<Employe> getAllEmployeByEntreprise(Entreprise entreprise) {
		l.info("In getAllEmployeByEntreprise ");
		l.debug("getAllEmployeByEntreprise reutn: "+employeRepository.getAllEmployeByEntreprisec(entreprise));
		return employeRepository.getAllEmployeByEntreprisec(entreprise);
	}

	public void mettreAjourEmailByEmployeIdJPQL(String email, int employeId) {
		l.info("In mettreAjourEmailByEmployeIdJPQL ");
		employeRepository.mettreAjourEmailByEmployeIdJPQL(email, employeId);
		l.info("Out mettreAjourEmailByEmployeIdJPQL");

	}
	public void deleteAllContratJPQL() {
		l.info("In deleteAllContratJPQL ");

		employeRepository.deleteAllContratJPQL();
		l.info("Out deleteAllContratJPQL");

	}

	public float getSalaireByEmployeIdJPQL(int employeId) {
		l.info("In getSalaireByEmployeIdJPQL ");
l.debug("getSalaireByEmployeIdJPQL retun: "+employeRepository.getSalaireByEmployeIdJPQL(employeId));
		return employeRepository.getSalaireByEmployeIdJPQL(employeId);
	}

	public Double getSalaireMoyenByDepartementId(int departementId) {
		l.info("In getSalaireMoyenByDepartementId ");
		l.debug("getSalaireMoyenByDepartementId retun: "+employeRepository.getSalaireMoyenByDepartementId(departementId));
		return employeRepository.getSalaireMoyenByDepartementId(departementId);
	}

	public List<Timesheet> getTimesheetsByMissionAndDate(Employe employe, Mission mission, Date dateDebut,
			Date dateFin) {
		l.info("In getTimesheetsByMissionAndDate ");
		l.debug("getTimesheetsByMissionAndDate retun: "+timesheetRepository.getTimesheetsByMissionAndDate(employe, mission, dateDebut, dateFin));
		return timesheetRepository.getTimesheetsByMissionAndDate(employe, mission, dateDebut, dateFin);
	}

	public List<Employe> getAllEmployes() {
		l.info("In getAllEmployes ");
		l.debug("getAllEmployes retun: "+employeRepository.findAll());
		return (List<Employe>) employeRepository.findAll();
	}

}
