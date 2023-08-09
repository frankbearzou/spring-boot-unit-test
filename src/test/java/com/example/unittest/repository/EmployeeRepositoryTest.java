package com.example.unittest.repository;

import java.util.List;
import java.util.Optional;

import com.example.unittest.entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

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

        Employee saved = employeeRepository.save(employee);

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

    @Test
    public void givenEmployee_whenFindById_thenReturnEmployee() {
        Employee emp = new Employee();
        emp.setFirstName("Adam");
        emp.setLastName("Smith");
        emp.setEmail("adam@abc.com");

        Employee saved = employeeRepository.save(emp);

        Employee employee = employeeRepository.findById(saved.getId()).get();

        assertThat(employee.getId()).isGreaterThan(0);
    }

    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployee() {
        Employee emp = new Employee();
        emp.setFirstName("Adam");
        emp.setLastName("Smith");
        emp.setEmail("adam@abc.com");
        employeeRepository.save(emp);

        Employee employee = employeeRepository.findByEmail(emp.getEmail()).get();

        assertThat(employee).isNotNull();
    }

    @Test
    public void givenEmployee_whenUpdate_thenReturnUpdatedEmployee() {
        Employee emp = new Employee();
        emp.setFirstName("Adam");
        emp.setLastName("Smith");
        emp.setEmail("adam@abc.com");
        employeeRepository.save(emp);

        Employee employee = employeeRepository.findByEmail(emp.getEmail()).get();
        employee.setLastName("Doe");
        employee.setEmail("adam.doe@gmail.com");
        Employee saved = employeeRepository.save(employee);

        assertThat(saved.getLastName()).isEqualTo("Doe");
        assertThat(saved.getEmail()).isEqualTo("adam.doe@gmail.com");
    }

    @Test
    public void givenEmployee_whenDelete_thenRemoveEmployee() {
        Employee emp = new Employee();
        emp.setFirstName("Adam");
        emp.setLastName("Smith");
        emp.setEmail("adam@abc.com");
        employeeRepository.save(emp);

        employeeRepository.delete(emp);

        Optional<Employee> employee = employeeRepository.findById(emp.getId());
        assertThat(employee).isEmpty();
    }

    @Test
    public void givenFirstNameAndLastName_whenFindByName_thenReturnEmployee() {
        Employee employee = getEmployee();
        employeeRepository.save(employee);

        Employee emp = employeeRepository.findByName("Adam", "Smith");

        assertThat(emp).isNotNull();
    }

    Employee getEmployee() {
        Employee emp = new Employee();
        emp.setFirstName("Adam");
        emp.setLastName("Smith");
        emp.setEmail("adam@abc.com");

        return emp;
    }
}
