package tn.esprit.spring;


import static org.junit.Assert.assertEquals;
import java.text.ParseException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import tn.esprit.spring.entities.Role;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.services.IEmployeService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeServiceImplTest {
	
	@Autowired
	IEmployeService emp;
	@Test
	public void testaddOrUpdateEmploye() throws ParseException {
		
		Employe ep = new Employe(6,"Amine", "Trabelsi", "amine.trabelsi@gmail.com","ahmed", true, Role.INGENIEUR);
		int employeAddedID = emp.addOrUpdateEmploye(ep);
		assertEquals(ep.getId(), employeAddedID);
		
	}
	@Test
	public void testgetAllEmployes() throws ParseException {
		List<Employe> listEmployes = emp.getAllEmployes();
		assertEquals(4, listEmployes.size());
	}
	
	
	@Test
	public void testgetEmployePrenomById() throws ParseException {
		String prenomEmploye = emp.getEmployePrenomById(1);
		assertEquals("jaber", prenomEmploye);
		
	}
	
	
	
	

	
	
	

}
