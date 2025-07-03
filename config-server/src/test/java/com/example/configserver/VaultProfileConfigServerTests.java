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
 * This test requires a running Vault server.
 * To run this test, start the Vault server using docker-compose:
 * docker-compose up -d vault
 * <p>
 * Then set the environment variable VAULT_AVAILABLE=true
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.profiles.active=vault"
        })
public class VaultProfileConfigServerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void clientBPropertiesAreNotAvailableFromVaultBackend() {
        // This test assumes that the Vault server has been initialized with the config-client.yaml file
        // containing the message property
        ResponseEntity<String> entity = this.restTemplate.getForEntity(
                "http://localhost:" + this.port + "/clientB/default", String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).doesNotContain("message");
    }

    @Test
    void clientAPropertiesAreAvailableFromVaultBackend() {
        // This test assumes that the Vault server has been initialized with the config-client.yaml file
        // containing the message property
        ResponseEntity<String> entity = this.restTemplate.getForEntity(
                "http://localhost:" + this.port + "/clientA/default", String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).contains("\"message\":\"ClientA - Hello from the Vault backend!\"");
    }
}
