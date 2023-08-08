package com.example.unittest.repository;

import com.example.unittest.entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

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
