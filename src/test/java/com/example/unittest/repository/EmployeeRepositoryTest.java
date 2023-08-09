package com.example.unittest.repository;

import java.util.List;
import java.util.Optional;

import com.example.unittest.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setupEmployee() {
        employee = new Employee();
        employee.setFirstName("Adam");
        employee.setLastName("Smith");
        employee.setEmail("adam@abc.com");
    }

    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        Employee saved = employeeRepository.save(employee);

        assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void givenTwoEmployees_whenFindAll_thenReturnEmployeeList() {
        // given
        Employee emp2 = new Employee();
        emp2.setFirstName("Bob");
        emp2.setLastName("Doe");
        emp2.setEmail("bob@abc.com");

        // when
        employeeRepository.save(employee);
        employeeRepository.save(emp2);

        // then
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(2);
    }

    @Test
    public void givenEmployee_whenFindById_thenReturnEmployee() {
        Employee saved = employeeRepository.save(employee);

        Employee emp = employeeRepository.findById(saved.getId()).get();

        assertThat(emp.getId()).isGreaterThan(0);
    }

    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployee() {
        employeeRepository.save(employee);

        Employee emp = employeeRepository.findByEmail("adam@abc.com").get();

        assertThat(emp).isNotNull();
    }

    @Test
    public void givenEmployee_whenUpdate_thenReturnUpdatedEmployee() {
        employeeRepository.save(employee);

        Employee emp = employeeRepository.findByEmail("adam@abc.com").get();
        emp.setLastName("Doe");
        emp.setEmail("adam.doe@gmail.com");
        Employee saved = employeeRepository.save(emp);

        assertThat(saved.getLastName()).isEqualTo("Doe");
        assertThat(saved.getEmail()).isEqualTo("adam.doe@gmail.com");
    }

    @Test
    public void givenEmployee_whenDelete_thenRemoveEmployee() {
        employeeRepository.save(employee);
        int id = employee.getId();

        employeeRepository.delete(employee);

        Optional<Employee> employee = employeeRepository.findById(id);
        assertThat(employee).isEmpty();
    }

    @Test
    public void givenFirstNameAndLastName_whenFindByName_thenReturnEmployee() {
        employeeRepository.save(employee);

        Employee emp = employeeRepository.findByName("Adam", "Smith");

        assertThat(emp).isNotNull();
    }
}
