package com.example.unittest.service;

import java.util.List;
import java.util.Optional;

import com.example.unittest.entity.Employee;
import com.example.unittest.exception.ResourceNotFoundException;
import com.example.unittest.repository.EmployeeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private Employee employee1;

    @BeforeEach
    public void setupEmployee() {
        employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@abc.com");
    }

    @BeforeEach
    public void setupEmployee1() {
        employee1 = new Employee();
        employee1.setFirstName("Joe");
        employee1.setLastName("Dow");
        employee1.setEmail("joe.dow@abc.com");
    }

    @Test
    public void givenEmployee_whenSaveEmployee_thenReturnSavedEmployee() {
        given(employeeRepository.findByEmail(anyString())).willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);

        Employee savedEmployee = employeeService.saveEmployee(employee);

        then(employeeRepository).should().findByEmail(anyString());
        then(employeeRepository).should().save(employee);

        assertThat(savedEmployee).isNotNull();
    }

    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowException() {
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        assertThrows(ResourceNotFoundException.class, () -> employeeService.saveEmployee(employee));

        then(employeeRepository).should(never()).save(any(Employee.class));
    }

    @Test
    public void givenEmployeeList_whenFindAllEmployees_thenReturnEmployeeList() {
        given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));

        List<Employee> employeeList = employeeService.FindAllEmployees();

        then(employeeRepository).should().findAll();

        assertThat(employeeList).isEqualTo(List.of(employee, employee1));
    }

    @Test
    public void givenEmptyEmployeeList_whenFindAllEmployees_thenReturnEmptyEmployeeList() {
        given(employeeRepository.findAll()).willReturn(List.of());

        List<Employee> employeeList = employeeService.FindAllEmployees();

        then(employeeRepository).should().findAll();

        assertThat(employeeList).isEmpty();
    }
}
