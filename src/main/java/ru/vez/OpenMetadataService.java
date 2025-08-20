package ru.vez;

import org.openmetadata.client.api.DatabaseServicesApi;
import org.openmetadata.client.api.DatabasesApi;
import org.openmetadata.client.api.TablesApi;
import org.openmetadata.client.gateway.OpenMetadata;
import org.openmetadata.client.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * Service class for interacting with OpenMetadata API.
 * Uses dependency injection for better testability and separation of concerns.
 */
public class OpenMetadataService {

    private static final Logger log = LoggerFactory.getLogger(OpenMetadataService.class);

    private final OpenMetadata openMetadataGateway;

    /**
     * Constructor with dependency injection.
     * 
     * @param openMetadataGateway the configured OpenMetadata gateway instance
     */
    public OpenMetadataService(OpenMetadata openMetadataGateway) {
        if (openMetadataGateway == null) {
            throw new IllegalArgumentException("OpenMetadata gateway cannot be null");
        }
        this.openMetadataGateway = openMetadataGateway;
    }

    public List<DatabaseService> getDatabaseServices() {
        log.debug("Fetching database services from OpenMetadata");
        try {
            DatabaseServicesApi databaseServicesApi = openMetadataGateway.buildClient(DatabaseServicesApi.class);
            // You can set additional query parameters if needed, e.g., pagination, filters, etc
            DatabaseServicesApi.ListDatabaseServicesQueryParams queryParams = new DatabaseServicesApi.ListDatabaseServicesQueryParams();
            DatabaseServiceList serviceList = databaseServicesApi.listDatabaseServices(queryParams);
            return serviceList.getData();
        } catch (Exception e) {
            log.error("Error fetching database services: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<Database> getDatabases() {
        log.debug("Fetching databases from OpenMetadata");
        try {
            DatabasesApi databasesApi = openMetadataGateway.buildClient(DatabasesApi.class);
            // You can set additional query parameters if needed, e.g., pagination, filters, etc
            DatabasesApi.ListDatabasesQueryParams queryParams = new DatabasesApi.ListDatabasesQueryParams();
            queryParams.limit(100); // Example limit, adjust as needed
            DatabaseList listDatabases = databasesApi.listDatabases(queryParams);
            return listDatabases.getData();
        } catch (Exception e) {
            log.error("Error fetching databases: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<Table> getTables(String databaseFqn) {
        log.debug("Fetching tables from OpenMetadata for database: {}", databaseFqn);
        try {
            TablesApi tablesApi = openMetadataGateway.buildClient(TablesApi.class);
            TablesApi.ListTablesQueryParams queryParams = new TablesApi.ListTablesQueryParams();
            // Filter tables by database FQN
            queryParams.database(databaseFqn);
            TableList tableList = tablesApi.listTables(queryParams);
            return tableList.getData();
        } catch (Exception e) {
            log.error("Error fetching tables for database {}: {}", databaseFqn, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

/*    public List<Lineage> getLineages() {
        log.debug("Fetching lineage from OpenMetadata at {}", openMetadataGateway.getServerConfig().getHostPort());
            LineageApi lineageApi = openMetadataGateway.buildClient(LineageApi.class);
            LineageApi.ListTablesQueryParams queryParams = new LineageApi.ListTablesQueryParams();
        // lineageApi.listTables(queryParams);
            return Collections.emptyList(); // TODO: Placeholder for actual database fetching logic
    }*/
}