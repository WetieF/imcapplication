package com.wetie.imcapplication.testcontainer;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

// Singleton containers
public abstract class AbstractContainerBaseTest {

    // definition of MySQL container
    static final MySQLContainer MY_SQL_CONTAINER;

    static {  // will be shared between test methods
        MY_SQL_CONTAINER = new MySQLContainer("mysql:latest")
                .withUsername("username")
                .withPassword("password")
                .withDatabaseName("ems");
        MY_SQL_CONTAINER.start();
    }

    // tell your test case to connect to my MY_SQL_CONTAINER
    @DynamicPropertySource
    public static void dynamicPropertySource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
    }
}
