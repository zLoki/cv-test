package com.corevalue.service;

import com.corevalue.model.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> prepareData();

    void setEmployeeList(List<Employee> employeeList);

    void printSubordinates(Integer managerId);

    void printCompanyEmployees();

    void setManager(Integer employeeId, Integer managerId);

    void printCeo(Integer employeeId);
}
