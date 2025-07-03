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
 * This test requires a running Vault server since the composite profile includes Vault.
 * To run this test, start the Vault server using docker-compose:
 * docker-compose up -d vault
 * <p>
 * Then set the environment variable VAULT_AVAILABLE=true
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.profiles.active=composite"
        })
public class CompositeProfileConfigServerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void configClientPropertiesAreAvailableFromCompositeBackend() {
        // Test should be able to retrieve properties from any of the configured backends
        ResponseEntity<String> entity = this.restTemplate.getForEntity(
                "http://localhost:" + this.port + "/clientA/default", String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        // The actual content will depend on the precedence of backends in the composite configuration
        // We're just checking that we get a valid response with some content
        assertThat(entity.getBody()).isNotEmpty();
        assertThat(entity.getBody()).contains("\"message\":");
    }
}