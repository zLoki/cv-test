package com.corevalue.service;

import com.corevalue.model.Employee;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class EmployeeServiceImpl implements EmployeeService {

    private List<Employee> employeeList;

    /* Basic hierarchy:
    * CEO: Rickard Stark
    * Manger: Ned Stark
    * Employees: John Snow, Arya Stark
    */
    @Override
    public List<Employee> prepareData() {
        employeeList = new ArrayList<>();

        Employee employee = new Employee(1, "Rickard", "Stark");
        employeeList.add(employee);

        employee = new Employee(2, "Ned", "Stark", 1);
        employeeList.add(employee);

        employee = new Employee(3, "John", "Snow", 2);
        employeeList.add(employee);

        employee = new Employee(4, "Arya", "Stark", 2);
        employeeList.add(employee);

        return employeeList;
    }

    @Override
    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    @Override
    public void setManager(Integer employeeId, Integer managerId) {
        if (employeeId.equals(managerId)) {
            print("Employee cannot be manager for himself.");
            return;
        }

        if (assertRelationCircularity(employeeId, managerId)) {
            print("Employee cannot become a manager for his manager.");
            return;
        }

        if (nonNull(managerId) && isNull(getEmployee(managerId))) {
            print("Manager with id: " + managerId + " not found.");
            return;
        }

        Employee employee = getEmployee(employeeId);

        if (isNull(employee)) {
            print("Employee with id: " + employeeId + " not found.");
            return;
        }

        employee.setManagerId(managerId);
        employeeList.set(employeeList.indexOf(employee), employee);
    }

    @Override
    public void printSubordinates(Integer managerId) {
        if (isNull(managerId)) {
            print("Manager ID cannot be null.");
            return;
        }

        Employee manager = getEmployee(managerId);

        if (isNull(manager)) {
            print("Manager with id: " + managerId + " not found.");
            return;
        }

        List<String> subordinates = employeeList.stream()
                .filter(employee -> nonNull(employee.getManagerId()) && employee.getManagerId().equals(manager.getId()))
                .map(Employee::getFullName)
                .collect(Collectors.toList());

        String result = subordinates.isEmpty() ?
                manager.getFullName() + " has no subordinates." :
                manager.getFullName() + "'s subordinates: " + String.join(", ", subordinates);

        print(result);
    }

    @Override
    public void printCompanyEmployees() {
        List<String> employees = employeeList.stream()
                .map(Employee::getFullName)
                .collect(Collectors.toList());

        String result = employees.isEmpty() ?
                "Company has no employees." :
                "Company employees: " + String.join(", ", employees);

        print(result);
    }

    @Override
    public void printCeo(Integer employeeId) {
        Employee employee = getEmployee(employeeId);

        if (isNull(employee)) {
            print("Employee with id: " + employeeId + " not found.");
            return;
        }

        if (isNull(employee.getManagerId())) {
            print(employee.getFullName() + " is CEO himself.");
            return;
        }

        print("CEO for " + employee.getFullName() + " is " + findCeo(employee).getFullName());
    }

    /*** Private functions ***/

    private Employee getEmployee(Integer employeeId) {
        OptionalInt optIndex = IntStream.range(0, employeeList.size())
                .filter(i -> employeeList.get(i).getId().equals(employeeId))
                .findFirst();

        //ideally EntityNotFound exception should be thrown, but since it's a console app, null will be returned instead
        return optIndex.isPresent() ? employeeList.get(optIndex.getAsInt()) : null;
    }

    private Employee findCeo(Employee employee) {
        if (isNull(employee.getManagerId())) {
            return employee;
        }

        Optional<Employee> manager = employeeList.stream()
                .filter(e -> e.getId().equals(employee.getManagerId()))
                .findFirst();

        return manager.isPresent() ? findCeo(manager.get()) : employee;
    }

    private boolean assertRelationCircularity(Integer employeeId, Integer managerId) {
        Employee employee = getEmployee(employeeId);
        Employee manager = getEmployee(managerId);

        return nonNull(employee) && nonNull(manager) && employee.getId().equals(manager.getManagerId());
    }

    private void print(String text) {
        System.out.println(text);
    }
}
