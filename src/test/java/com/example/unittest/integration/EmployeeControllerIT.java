package com.example.unittest.integration;

import com.example.unittest.entity.Employee;
import com.example.unittest.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    private Employee employee1;

    @BeforeEach
    public void setupEmployeeEntity() {
        employeeRepository.deleteAll();
    }

    @BeforeEach
    public void setupEmployee() {
        employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@123.com");
    }

    @BeforeEach
    public void setupEmployee1() {
        employee1 = new Employee();
        employee1.setFirstName("Jane");
        employee1.setLastName("Dow");
        employee1.setEmail("jane.dow@456.net");
    }

    @Test
    public void givenEmployee_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        mockMvc.perform(
                post("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @Test
    public void givenEmployee_whenFindAllEmployees_thenReturnEmployees() throws Exception {
        List<Employee> employeeList = List.of(employee, employee1);
        employeeRepository.saveAll(employeeList);

        mockMvc.perform(get("/api/employee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(employeeList.size())));
    }

    @Test
    public void givenEmployee_whenFindById_thenReturnFoundEmployee() throws Exception {
        employeeRepository.save(employee);

        mockMvc.perform(get("/api/employee/{id}", employee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @Test
    public void givenNonExistingEmployee_whenFindById_thenReturnNotFound404() throws Exception {
        employeeRepository.save(employee);

        mockMvc.perform(get("/api/employee/{id}", employee.getId() + 10))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenEmployee_whenUpdate_thenReturnUpdatedEmployee() throws Exception {
        employeeRepository.save(employee);

        mockMvc.perform(
                put("/api/employee/{id}", employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee1))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee1.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee1.getLastName())))
                .andExpect(jsonPath("$.email", is(employee1.getEmail())));
    }

    @Test
    public void givenNonExistingEmployeeId_whenUpdateEmployee_thenReturnNotFound404() throws Exception {
        int nonExistingEmployeeId = 404;
        mockMvc.perform(put("/api/employee/{id}", nonExistingEmployeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee1))
        ).andExpect(status().isNotFound());
    }
}
