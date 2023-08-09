package com.example.unittest.service;

import com.example.unittest.entity.Employee;
import com.example.unittest.exception.ResourceNotFoundException;
import com.example.unittest.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {
    private EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    Employee saveEmployee(Employee employee) {
        Optional<Employee> employeeOptional = employeeRepository.findByEmail(employee.getEmail());
        if (employeeOptional.isPresent()) {
            throw new ResourceNotFoundException("email " + employee.getEmail() + " exist");
        }
        return employeeRepository.save(employee);
    }
}
