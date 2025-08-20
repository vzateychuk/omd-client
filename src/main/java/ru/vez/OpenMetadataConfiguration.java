package ru.vez;

import org.openmetadata.client.gateway.OpenMetadata;
import org.openmetadata.schema.services.connections.metadata.AuthProvider;
import org.openmetadata.schema.services.connections.metadata.OpenMetadataConnection;
import org.openmetadata.schema.security.client.OpenMetadataJWTClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration class for OpenMetadata client setup.
 * Handles the creation and configuration of OpenMetadata gateway instances.
 */
public class OpenMetadataConfiguration {

    private static final Logger log = LoggerFactory.getLogger(OpenMetadataConfiguration.class);

    private final String serverUrl;
    private final String jwtToken;

    public OpenMetadataConfiguration(String serverUrl, String jwtToken) {
        if (serverUrl == null) {
            throw new IllegalArgumentException("Server URL cannot be null");
        }
        if (jwtToken == null) {
            throw new IllegalArgumentException("JWT token cannot be null");
        }
        
        this.serverUrl = serverUrl;
        this.jwtToken = jwtToken;
    }

    /**
     * Creates and configures an OpenMetadata gateway instance.
     * 
     * @return configured OpenMetadata gateway
     */
    public OpenMetadata createOpenMetadataGateway() {
        log.debug("Creating OpenMetadata gateway for server: {}", serverUrl);
        
        OpenMetadataConnection server = new OpenMetadataConnection();
        OpenMetadataJWTClientConfig jwtClientConfig = new OpenMetadataJWTClientConfig();
        jwtClientConfig.setJwtToken(jwtToken);

        server.setAuthProvider(AuthProvider.OPENMETADATA);
        server.setHostPort(serverUrl);
        server.setSecurityConfig(jwtClientConfig);

        // Instantiate the client with the connection config
        return new OpenMetadata(server);
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getJwtToken() {
        return jwtToken;
    }
}
