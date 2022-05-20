package com.mindex.challenge;

import com.fasterxml.jackson.core.JsonProcessingException;
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

	private String baseUrl = "http://localhost:8080/";


	private MvcResult setupForGETRestCall( String uri) throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		return mockMvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
	}

	private MvcResult setupForPOSTRestCall( String uri, String inputJson) throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		return mockMvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
	}

	private MvcResult setupForPUTRestCall( String uri, String inputJson) throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		return mockMvc.perform(MockMvcRequestBuilders.put(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
	}

	@Test
	public void readEmployee() throws Exception {
		MvcResult result = setupForGETRestCall(baseUrl + "employee/c0c2293d-16bd-4603-8e08-638a9d18b22c");
		Employee newEmployee = mapFromJson(result.getResponse().getContentAsString(), Employee.class);
		assertEquals("Harrison", newEmployee.getLastName());
	}

	@Test
	public void updateEmployee() throws Exception {
		Employee employee = employeeService.read("c0c2293d-16bd-4603-8e08-638a9d18b22c");
		employee.setLastName("Baker");
		String employeeStr = mapToJson(employee);
		MvcResult result = setupForPUTRestCall(baseUrl + "employee/c0c2293d-16bd-4603-8e08-638a9d18b22c", employeeStr);
		Employee newEmployee = mapFromJson(result.getResponse().getContentAsString(), Employee.class);
		assertEquals("Baker", newEmployee.getLastName());
	}

	@Test
	public void readReportingStructure() throws Exception {
		MvcResult result = setupForGETRestCall(baseUrl + "orgChart/16a596ae-edd3-4847-99fe-c4518e82c86f");
		ReportingStructure reportingStructure = mapFromJson(result.getResponse().getContentAsString(), ReportingStructure.class);
		assertEquals(4, reportingStructure.getNumberOfReports());
	}

	@Test
	public void createEmployeeCompensation() throws Exception {
		Employee employee = employeeService.read("b7839309-3348-463b-a7e3-5de1c168beb3");
		String employeeStr = mapToJson(employee);
		MvcResult result = setupForPOSTRestCall(baseUrl + "compensation?salary=44445&effectiveDate=6/6/20", employeeStr);
		Compensation compensation = mapFromJson(result.getResponse().getContentAsString(), Compensation.class);
		assertEquals("44445", compensation.getSalary());
	}

	@Test
	public void readEmployeeCompensation() throws Exception {
		MvcResult result = setupForGETRestCall(baseUrl + "compensation/b7839309-3348-463b-a7e3-5de1c168beb3");
		Compensation compensation = mapFromJson(result.getResponse().getContentAsString(), Compensation.class);
		assertEquals("55", compensation.getSalary());
	}

	private <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}

	private String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}


}