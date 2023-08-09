package com.example.unittest.repository;

import java.util.Optional;

import com.example.unittest.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findByEmail(String email);

    @Query("select e from Employee e where e.firstName=:firstName and e.lastName=:lastName")
    Employee findByName(String firstName, String lastName);
}
