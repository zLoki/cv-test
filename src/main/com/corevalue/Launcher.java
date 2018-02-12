package com.corevalue;

import com.corevalue.model.Employee;
import com.corevalue.service.EmployeeService;
import com.corevalue.service.EmployeeServiceImpl;

import java.util.List;

public class Launcher {

    public static void main(String[] args) {
        EmployeeService employeeService = new EmployeeServiceImpl();
        List<Employee> employeeList = employeeService.prepareData();

        employeeService.printCompanyEmployees();
        employeeService.printSubordinates(employeeList.get(1).getId());
        employeeService.printCeo(employeeList.get(employeeList.size() - 1).getId());
    }
}
