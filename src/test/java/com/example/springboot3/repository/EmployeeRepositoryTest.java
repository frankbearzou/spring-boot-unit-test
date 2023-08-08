package com.example.springboot3.repository;

import com.example.springboot3.entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        Employee employee = new Employee();
        employee.setFirstName("Adam");
        employee.setLastName("Smith");
        employee.setEmail("adam@abc.com");

        Employee saved = employeeRepository.saveAndFlush(employee);

        assertThat(saved.getId()).isGreaterThan(0);
    }
}
