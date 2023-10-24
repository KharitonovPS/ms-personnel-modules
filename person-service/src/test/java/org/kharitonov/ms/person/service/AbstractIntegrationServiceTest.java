package org.kharitonov.ms.person.service;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = AbstractIntegrationServiceTest.Initializer.class)
public class AbstractIntegrationServiceTest {


    public static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer<>()
            .withInitScript("init.sql")
            .withExposedPorts(5432);


    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        private static final Logger logger = LoggerFactory.getLogger(Initializer.class);

        @SneakyThrows
        public void initialize(ConfigurableApplicationContext applicationContext) {
            POSTGRES.start();

            String[] variables = {"spring.jpa.hibernate.ddl-auto=update",

                    "spring.datasource.url=" + POSTGRES.getJdbcUrl(),
                    "spring.datasource.username=" + POSTGRES.getUsername(),
                    "spring.datasource.password=" + POSTGRES.getPassword(),

                    "clients.dictionary-client.host=" + "http://localhost:8080"
            };

            logger.info("initialize: переопределяем проперти={}", Arrays.toString(variables));

            var testPropertyValues = TestPropertyValues.of(variables);

            testPropertyValues.applyTo(applicationContext.getEnvironment());
        }
    }
}
