package com.example.configserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test simulates the Git profile by using a mock configuration.
 * In a real environment, you would use an actual Git repository.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.profiles.active=git"
        })
public class GitProfileConfigServerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void configClientPropertiesAreAvailableFromGitBackend() {
        ResponseEntity<String> entity = this.restTemplate.getForEntity(
                "http://localhost:" + this.port + "/clientA/default", String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).contains("\"message\":\"ClientA - Hello from the Git backend!\"");
    }
}
