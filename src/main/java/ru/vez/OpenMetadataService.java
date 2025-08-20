package ru.vez;
import org.openmetadata.client.api.DatabaseServicesApi;
import org.openmetadata.client.api.DatabasesApi;
import org.openmetadata.client.api.TablesApi;
import org.openmetadata.client.gateway.OpenMetadata;
import org.openmetadata.client.model.*;
import org.openmetadata.schema.services.connections.metadata.AuthProvider;
import org.openmetadata.schema.services.connections.metadata.OpenMetadataConnection;

import org.openmetadata.schema.security.client.OpenMetadataJWTClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class OpenMetadataService {

    private static final Logger log = LoggerFactory.getLogger(OpenMetadataService.class);

    // OpenMetadata Gateway
    private final OpenMetadata openMetadataGateway;
    private final String serverUrl;

    public OpenMetadataService(String serverUrl, String jwtToken) {
        this.serverUrl = serverUrl;
        OpenMetadataConnection server = new OpenMetadataConnection();
        OpenMetadataJWTClientConfig jwtClientConfig = new OpenMetadataJWTClientConfig();
        jwtClientConfig.setJwtToken(jwtToken);

        server.setAuthProvider(AuthProvider.OPENMETADATA);
        server.setHostPort(serverUrl);
        server.setSecurityConfig(jwtClientConfig);

        // Instantiate the client with the connection config
        this.openMetadataGateway = new OpenMetadata(server);
    }

    public List<DatabaseService> getDatabaseServices() {
            log.debug("Fetching database services from OpenMetadata at {}", this.serverUrl);
            DatabaseServicesApi databaseServicesApi = openMetadataGateway.buildClient(DatabaseServicesApi.class);
            // You can set additional query parameters if needed, e.g., pagination, filters, etc
            DatabaseServicesApi.ListDatabaseServicesQueryParams queryParams = new DatabaseServicesApi.ListDatabaseServicesQueryParams();
/*            DatabaseServiceList serviceList = databaseServicesApi.listDatabaseServices(queryParams);
            return serviceList.getData();
            */
        return Collections.emptyList(); // TODO: Placeholder for actual database fetching logic
    }

    public List<Database> getDatabases() {
        log.debug("Fetching databases from OpenMetadata at {}", this.serverUrl);
            DatabasesApi databasesApi = openMetadataGateway.buildClient(DatabasesApi.class);
            // You can set additional query parameters if needed, e.g., pagination, filters, etc
            DatabasesApi.ListDatabasesQueryParams queryParams = new DatabasesApi.ListDatabasesQueryParams();
            queryParams.limit(100); // Example limit, adjust as needed
/*
            DatabaseList listDatabases = databasesApi.listDatabases(queryParams);
            return listDatabases.getData();
*/
        return Collections.emptyList(); // TODO: Placeholder for actual database fetching logic
    }

    public List<Table> getTables(String databaseFqn) {
        log.debug("Fetching tables from OpenMetadata at {}", this.serverUrl);
            TablesApi tablesApi = openMetadataGateway.buildClient(TablesApi.class);
            TablesApi.ListTablesQueryParams queryParams = new TablesApi.ListTablesQueryParams();
/*            queryParams.databaseFqn(databaseFqn);
            tablesApi.listTables(queryParams);*/
            return Collections.emptyList(); //  TODO: Placeholder for actual database fetching logic
    }

/*    public List<Lineage> getLineages() {
        log.debug("Fetching lineage from OpenMetadata at {}", openMetadataGateway.getServerConfig().getHostPort());
            LineageApi lineageApi = openMetadataGateway.buildClient(LineageApi.class);
            LineageApi.ListTablesQueryParams queryParams = new LineageApi.ListTablesQueryParams();
        // lineageApi.listTables(queryParams);
            return Collections.emptyList(); // TODO: Placeholder for actual database fetching logic
    }*/
}