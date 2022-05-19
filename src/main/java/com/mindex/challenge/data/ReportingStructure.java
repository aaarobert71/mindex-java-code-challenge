package com.mindex.challenge.data;

import java.util.ArrayList;
import java.util.List;

public class ReportingStructure {
    private List<Employee> employees = new ArrayList<>();
    private int numberOfReports;

    private String manager;

    public String getManager() {
        return manager;
    }

    public void setManager(String firstName, String lastName) {
        this.manager = firstName + " " + lastName;
    }

    public int getNumberOfReports() {
        return numberOfReports;
    }
    public void setNumberOfReports(int numberOfReports) {
        this.numberOfReports = numberOfReports;
    }

    public List<Employee> getDirectReports() {return employees;}
    public void setDirectReport(Employee employee) {
        employees.add(employee);
    }
}
