package com.corevalue.test.service;

import com.corevalue.main.service.EmployeeService;
import com.corevalue.main.service.EmployeeServiceImpl;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.testng.AssertJUnit.assertEquals;

public class EmployeeServiceImplTest {
    private static final String EOL = System.getProperty("line.separator");
    private static final int NON_EXISTING_ID = 100;

    private PrintStream console;

    private ByteArrayOutputStream bytes;

    private EmployeeService employeeService;

    @BeforeMethod
    public void setUp() {
        console = System.out;
        bytes = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bytes));

        employeeService = new EmployeeServiceImpl();
        employeeService.prepareData();
    }

    @AfterMethod
    public void tearDown() {
        System.setOut(console);
    }

    @Test
    public void test_setManager_success() {
        employeeService.setManager(4, 3);
        employeeService.printSubordinates(3);
        assertEquals(bytes.toString(), "John Snow's subordinates: Arya Stark" + EOL);
    }

    @Test
    public void test_setManager_errorCannotBecomeManagerForSelf() {
        employeeService.setManager(3, 3);
        assertEquals(bytes.toString(), "Employee cannot be manager for himself." + EOL);
    }

    @Test
    public void test_setManager_errorRelationsCircularityForbidden() {
        employeeService.setManager(1, 2);
        assertEquals(bytes.toString(), "Employee cannot become a manager for his manager." + EOL);
    }

    @Test
    public void test_printSubordinates_successWithSubordinates() {
        employeeService.printSubordinates(2);
        assertEquals(bytes.toString(), "Ned Stark's subordinates: John Snow, Arya Stark" + EOL);
    }

    @Test
    public void test_printSubordinates_successWithoutSubordinates() {
        employeeService.printSubordinates(4);
        assertEquals(bytes.toString(), "Arya Stark has no subordinates." + EOL);
    }

    @Test
    public void test_printSubordinates_errorIdCannotBeNull() {
        employeeService.printSubordinates(null);
        assertEquals(bytes.toString(), "Manager ID cannot be null." + EOL);
    }

    @Test
    public void test_printSubordinates_errorEmployeeNotFound() {
        employeeService.printSubordinates(NON_EXISTING_ID);
        assertEquals(bytes.toString(), "Manager with id: " + NON_EXISTING_ID + " not found." + EOL);
    }

    @Test
    public void test_printCompanyEmployees_successWithEmployees() {
        employeeService.printCompanyEmployees();
        assertEquals(bytes.toString(), "Company employees: Rickard Stark, Ned Stark, John Snow, Arya Stark" + EOL);
    }

    @Test
    public void test_printCompanyEmployees_successWithoutEmployees() {
        employeeService.setEmployeeList(new ArrayList<>());
        employeeService.printCompanyEmployees();
        assertEquals(bytes.toString(), "Company has no employees." + EOL);
    }

    @Test
    public void test_printCeo_success() {
        employeeService.printCeo(3);
        assertEquals(bytes.toString(), "CEO for John Snow is Rickard Stark" + EOL);
    }

    @Test
    public void test_printCeo_successHimself() {
        employeeService.printCeo(1);
        assertEquals(bytes.toString(), "Rickard Stark is CEO himself." + EOL);
    }

    @Test
    public void test_printCeo_errorEmployeeNotFound() {
        employeeService.printCeo(NON_EXISTING_ID);
        assertEquals(bytes.toString(), "Employee with id: " + NON_EXISTING_ID + " not found." + EOL);
    }


}
