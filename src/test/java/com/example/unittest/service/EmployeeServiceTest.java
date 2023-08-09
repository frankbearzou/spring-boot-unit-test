package com.example.unittest.service;

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

    @BeforeEach
    public void setupEmployee() {
        employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@abc.com");
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
}
