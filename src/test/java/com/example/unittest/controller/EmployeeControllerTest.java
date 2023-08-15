package com.example.unittest.controller;

import java.util.List;
import java.util.Optional;

import com.example.unittest.entity.Employee;
import com.example.unittest.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.*;

@WebMvcTest
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    private ObjectMapper objectMapper;

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
        employee1.setEmail("jone.doe@abc.net");
    }


    @BeforeEach
    public void setupObjectMapper() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void givenEmployeeList_whenGetAllEmployee_thenReturnAllEmployee() throws Exception {
        given(employeeService.findAllEmployees()).willReturn(List.of(employee, employee1));

        ResultActions response = mockMvc.perform(get("/api/employee"));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$.[0].firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.[0].lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.[0].email", is(employee.getEmail())))
                .andExpect(jsonPath("$.[1].firstName", is(employee1.getFirstName())))
                .andExpect(jsonPath("$.[1].lastName", is(employee1.getLastName())))
                .andExpect(jsonPath("$.[1].email", is(employee1.getEmail())));
    }

    @Test
    public void givenExistingEmployeeId_whenFindEmployById_thenReturnEmployee() throws Exception {
        int employeeId = 1;
        employee.setId(employeeId);
        given(employeeService.findById(employeeId)).willReturn(Optional.of(employee));

        ResultActions response = mockMvc.perform(get("/api/employee/{id}", employeeId));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(employeeId)))
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @Test
    public void givenNonExistingEmployeeId_whenFindEmployeeById_thenReturnEmpty() throws Exception {
        int employeeId = 1;
        given(employeeService.findById(employeeId)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/employee/{id}", employeeId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenEmployee_whenCreateEmployee_thenReturnCreatedEmployee() throws Exception {
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer(invocation -> {
                    Employee emp = invocation.getArgument(0, Employee.class);
                    emp.setId(1);
                    return emp;
                });

        ResultActions response = mockMvc.perform(post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @Test
    public void givenEmployee_whenUpdate_whenReturnUpdatedEmployee() throws Exception {
        int employeeId = 1;

        given(employeeService.findById(employeeId)).willReturn(Optional.of(employee));
        given(employeeService.updateEmployee(any(Employee.class))).willAnswer(invocation -> invocation.getArgument(0, Employee.class));

        mockMvc.perform(
                put("/api/employee/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee1))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee1.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee1.getLastName())))
                .andExpect(jsonPath("$.email", is(employee1.getEmail())));
    }
}
