package ru.vez;

import org.junit.jupiter.api.Test;
import org.openmetadata.client.gateway.OpenMetadata;

import static org.junit.jupiter.api.Assertions.*;

class OpenMetadataConfigurationTest {

    @Test
    void testConstructorWithValidParameters() {
        // Given
        String serverUrl = "http://localhost:8585";
        String jwtToken = "test-jwt-token";

        // When
        OpenMetadataConfiguration config = new OpenMetadataConfiguration(serverUrl, jwtToken);

        // Then
        assertNotNull(config);
        assertEquals(serverUrl, config.getServerUrl());
        assertEquals(jwtToken, config.getJwtToken());
    }

    @Test
    void testConstructorWithNullServerUrl() {
        // Given
        String jwtToken = "test-jwt-token";

        // When & Then
        assertThrows(IllegalArgumentException.class, 
            () -> new OpenMetadataConfiguration(null, jwtToken));
    }

    @Test
    void testConstructorWithNullJwtToken() {
        // Given
        String serverUrl = "http://localhost:8585";

        // When & Then
        assertThrows(IllegalArgumentException.class, 
            () -> new OpenMetadataConfiguration(serverUrl, null));
    }

    @Test
    void testCreateOpenMetadataGateway() {
        // Given
        String serverUrl = "http://localhost:8585";
        String jwtToken = "test-jwt-token";
        OpenMetadataConfiguration config = new OpenMetadataConfiguration(serverUrl, jwtToken);

        // When
        OpenMetadata gateway = config.createOpenMetadataGateway();

        // Then
        assertNotNull(gateway);
    }

    @Test
    void testConfigurationWithDifferentUrls() {
        // Test with HTTP URL
        OpenMetadataConfiguration config1 = new OpenMetadataConfiguration("http://localhost:8585", "token");
        assertNotNull(config1);

        // Test with HTTPS URL
        OpenMetadataConfiguration config2 = new OpenMetadataConfiguration("https://openmetadata.example.com", "token");
        assertNotNull(config2);

        // Test with IP address
        OpenMetadataConfiguration config3 = new OpenMetadataConfiguration("http://192.168.1.100:8585", "token");
        assertNotNull(config3);
    }
}
