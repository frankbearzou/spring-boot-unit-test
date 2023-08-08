package com.example.unittest.repository;

import java.util.List;

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

    @Test
    public void givenTwoEmployees_whenFindAll_thenReturnEmployeeList() {
        // given
        Employee emp1 = new Employee();
        emp1.setFirstName("Adam");
        emp1.setLastName("Smith");
        emp1.setEmail("adam@abc.com");

        Employee emp2 = new Employee();
        emp2.setFirstName("Bob");
        emp2.setLastName("Doe");
        emp2.setEmail("bob@abc.com");

        // when
        employeeRepository.save(emp1);
        employeeRepository.save(emp2);

        // then
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(2);
    }
}
