package com.example.unittest.integration;

import java.sql.Connection;
import java.util.Map;

import com.example.unittest.entity.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeIT extends AbstractContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@123.com");
    }

    @Test
    void test() throws Exception {
        assertTrue(MY_SQL_CONTAINER.isRunning());
        System.out.println("url: " + MY_SQL_CONTAINER.getJdbcUrl());
        Map<String, Object> properties = entityManager.getEntityManagerFactory().getProperties();
        HikariDataSource dataSource = (HikariDataSource)properties.get("javax.persistence.nonJtaDataSource");
        Connection connection = dataSource.getConnection();
        System.out.println("connection url: " + connection.getMetaData().getURL());
        mockMvc.perform(
                post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(employee))
        ).andExpect(status().isCreated());

        mockMvc.perform(get("/api/employee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }
}
