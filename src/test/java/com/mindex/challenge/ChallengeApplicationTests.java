package com.mindex.challenge;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChallengeApplicationTests {


	@Autowired
	EmployeeService employeeService;

	@Autowired
	WebApplicationContext webApplicationContext;

	@Test
	public void contextLoads() {
		assertNotNull(employeeService);
		Employee emp1 = new Employee();
		emp1.setEmployeeId("6");
		employeeService.create(emp1);
		assertNotNull(employeeService.read("6"));
	}

	@Test
	public void createAnotherEmployeeAndDirectReports() {
		assertNotNull(employeeService);
		Employee emp1 = new Employee();
		emp1.setEmployeeId("13");
		emp1.setFirstName("Frank");
		emp1.setLastName("Gore");
		emp1.setPosition("Solution Lead");
		emp1.setDepartment("Product Services");
		emp1.setDirectReports(getEmployeeWithSeveralDirectReports());
		employeeService.create(emp1);
		assertEquals("13", employeeService.read("13").getEmployeeId());
	}

	@Test
	public void restCallForOrgChart() throws Exception {
		createAnEmployeeAndDirectReports();
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"http://localhost:8080/orgChart/7").accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		ReportingStructure tt = mapFromJson(result.getResponse().getContentAsString(), ReportingStructure.class);
		assertEquals(4, tt.getNumberOfReports());
	}

	@Test
	public void employeeCompensation() {
		assertNotNull(employeeService);
		Date eff = new Date();
		Compensation comp1 = new Compensation(getEmployee(),"100,00", eff);
		Compensation result = employeeService.createCompensation(comp1);
		assertEquals("Steve", result.getEmployee().getFirstName());

	}

	private Employee getEmployee() {
		Employee emp8 = new Employee();
		emp8.setEmployeeId("27");
		emp8.setFirstName("Steve");
		emp8.setLastName("Smith");
		emp8.setPosition("Software Developer");
		emp8.setDepartment("Product Services");
		employeeService.create(emp8);
		return emp8;
	}
	private List<Employee> getEmployeeWithDirectReport() {
		List<Employee> employeeList = new ArrayList<>();
		Employee emp3 = new Employee();
		emp3.setEmployeeId("97");
		emp3.setFirstName("David");
		emp3.setLastName("Hughes");
		emp3.setPosition("Software Developer");
		emp3.setDepartment("Product Services");
		//emp3.setDirectReports();
		employeeService.create(emp3);
		employeeList.add(emp3);
		return employeeList;
	}

	private void createAnEmployeeAndDirectReports() {
		assertNotNull(employeeService);
		Employee emp1 = new Employee();
		emp1.setEmployeeId("7");
		emp1.setFirstName("Bill");
		emp1.setLastName("Harris");
		emp1.setPosition("Solution Lead");
		emp1.setDepartment("Product Services");
		emp1.setDirectReports(getEmployeeWithSeveralDirectReports());
		employeeService.create(emp1);

	}


	private List<Employee> getEmployeeWithSeveralDirectReports(){
		List<Employee> employeeList = new ArrayList<>();
		Employee emp3 = new Employee();
		emp3.setEmployeeId("75");
		emp3.setFirstName("Dan");
		emp3.setLastName("Jones");
		emp3.setPosition("Solutions Lead");
		emp3.setDepartment("Team Services");
		//emp3.setDirectReports();
		employeeService.create(emp3);
		employeeList.add(emp3);

		Employee emp4 = new Employee();
		emp4.setEmployeeId("45");
		emp4.setFirstName("Harry");
		emp4.setLastName("Dirty");
		emp4.setPosition("Senior Dev");
		emp4.setDepartment("Team Services");
		//emp4.setDirectReports();
		employeeService.create(emp4);
		employeeList.add(emp4);

		Employee emp5 = new Employee();
		emp5.setEmployeeId("33");
		emp5.setFirstName("Ed");
		emp5.setLastName("Gunther");
		emp5.setPosition("Software Developer");
		emp5.setDepartment("Team Services");
		emp5.setDirectReports(getEmployeeWithDirectReport());
		employeeService.create(emp5);
		employeeList.add(emp5);
		return employeeList;

	}

	private <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}

}
