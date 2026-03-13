package com.example.soildprincipalsdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the "java-soild-principals-demo" project.
 *
 * This class is intentionally simple because its single responsibility is to
 * bootstrap the Spring Boot application context.
 */
@SpringBootApplication
public class JavaSoildPrincipalsDemoApplication {

    /**
     * Application startup method.
     *
     * @param args command-line arguments passed while starting the application
     */
    public static void main(String[] args) {
        SpringApplication.run(JavaSoildPrincipalsDemoApplication.class, args);
    }
}
