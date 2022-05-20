package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employee")
    public Employee create(@RequestBody Employee employee) {
        LOG.debug("Received request for employee creation [{}]", employee);
        return employeeService.create(employee);
    }

    @GetMapping("/employee/{id}")
    public Employee read(@PathVariable String id) {
        LOG.debug("Received request for information with employee ID [{}]", id);
        return employeeService.read(id);
    }

    @PutMapping("/employee/{id}")
    public Employee update(@PathVariable String id, @RequestBody Employee employee) {
        LOG.debug("Received request to update employee information associated with employee ID [{}] and employee [{}]", id, employee);
        Employee temp = employeeService.read(id);
        temp.setEmployeeId(id);
        return employeeService.update(temp);
    }

    @GetMapping("/orgChart/{id}")
    public ReportingStructure orgChart(@PathVariable String id) {
        LOG.debug("Received request for orgChart associated with employee ID [{}]", id);
        return employeeService.orgChart(id);
    }

    @PostMapping("/compensation")
    public Compensation createCompensation(@RequestBody Employee employee, @RequestParam String salary, @RequestParam String effectiveDate) {
        LOG.debug("Received request for employee compensation creation [{}]", employee);
        return employeeService.createCompensation(employee, salary, effectiveDate);
    }
    @GetMapping("/compensation/{id}")
    public Compensation readCompensation(@PathVariable String id) {
        LOG.debug("Received request for employee compensation associated with employeeID [{}]", id);
        return employeeService.readCompensation(id);
    }

}
