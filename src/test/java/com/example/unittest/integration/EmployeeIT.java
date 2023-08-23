package com.example.unittest.integration;

import java.sql.Connection;
import java.util.Map;

import com.example.unittest.entity.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class EmployeeIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    private static final MySQLContainer MY_SQL_CONTAINER = new MySQLContainer("mysql:8.1.0");

    @DynamicPropertySource
    static void configureTestProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

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
