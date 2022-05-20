package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CompensationRepository compensationRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);
        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);
        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Retrieving employee information associated with employee ID [{}]", id);
        Employee employee = employeeRepository.findByEmployeeId(id);
        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("updating employee information for employee ID ", employee.getEmployeeId());
        return employeeRepository.save(employee);
    }

    @Override
    public ReportingStructure orgChart(String id) {
        LOG.debug("Developing org Chart for employee id [{}]", id);
        ReportingStructure org = null;
        Employee employee = employeeRepository.findByEmployeeId(id);
        if (employee != null) {
            List<Employee> reportsList = employee.getDirectReports();
            org = new ReportingStructure();
            org.setManager(employee.getFirstName(), employee.getLastName());
            setEmployees(org.getDirectReports(), reportsList);
            org.setNumberOfReports(getNumberOfDirectReports(reportsList));
        }
        return org;
    }

    // Using recursion to get a given employee direct reports
    private void setEmployees(List<Employee> employees, List<Employee> reports) {
        if (reports != null)for (Employee report : reports) {
            Employee employee = employeeRepository.findByEmployeeId(report.getEmployeeId());
            employees.add(employee);
            setEmployees(employees, employee.getDirectReports());
        }
    }

    // Using recursion to get a given employee's number of direct reports
    private int getNumberOfDirectReports(List<Employee> reports) {
        int count = 0;
        if (reports != null)for (Employee report : reports) {
            Employee employee = employeeRepository.findByEmployeeId(report.getEmployeeId());
            count += 1 + getNumberOfDirectReports(employee.getDirectReports());
        }
        return count;
    }

    @Override
    public Compensation readCompensation(String id) {
        LOG.debug("Retrieve Compensation for employee ID [{}] ", id);
        return compensationRepository.findCompensationByEmployeeEmployeeId(id);
    }

    @Override
    public Compensation createCompensation (Employee employee, String salary, String effectiveDate) {
        LOG.debug("Creation of Compensation for employee ID [{}] ", employee.getEmployeeId());
        return compensationRepository.save(new Compensation(employee, salary, effectiveDate));
    }


}
