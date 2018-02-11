package com.corevalue.main;

import com.corevalue.main.model.Employee;
import com.corevalue.main.service.EmployeeService;
import com.corevalue.main.service.EmployeeServiceImpl;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        EmployeeService employeeService = new EmployeeServiceImpl();
        List<Employee> employeeList = employeeService.prepareData();

        employeeService.printCompanyEmployees();
        employeeService.printSubordinates(employeeList.get(1).getId());
        employeeService.printCeo(employeeList.get(employeeList.size() - 1).getId());
    }
}
