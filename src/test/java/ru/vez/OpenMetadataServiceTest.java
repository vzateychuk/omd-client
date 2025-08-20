package ru.vez;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openmetadata.client.api.DatabaseServicesApi;
import org.openmetadata.client.api.DatabasesApi;
import org.openmetadata.client.api.TablesApi;
import org.openmetadata.client.gateway.OpenMetadata;
import org.openmetadata.client.model.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpenMetadataServiceTest {

    @Mock
    private OpenMetadata openMetadataGateway;

    @Mock
    private DatabaseServicesApi databaseServicesApi;

    @Mock
    private DatabasesApi databasesApi;

    @Mock
    private TablesApi tablesApi;

    private OpenMetadataService openMetadataService;

    @BeforeEach
    void setUp() {
        openMetadataService = new OpenMetadataService(openMetadataGateway);
    }

    @Test
    void testConstructor() {
        // Test that the service is created with correct parameters
        OpenMetadata mockGateway = mock(OpenMetadata.class);
        OpenMetadataService service = new OpenMetadataService(mockGateway);
        assertNotNull(service);
    }

    @Test
    void testConstructorWithNullGateway() {
        // Test that the service handles null gateway gracefully
        assertThrows(IllegalArgumentException.class, () -> new OpenMetadataService(null));
    }

    @Test
    void testGetDatabaseServices() {
        // Given
        DatabaseService service1 = new DatabaseService();
        service1.setName("MySQL Service");
        service1.setId("service-1");

        DatabaseService service2 = new DatabaseService();
        service2.setName("PostgreSQL Service");
        service2.setId("service-2");

        DatabaseServiceList serviceList = new DatabaseServiceList();
        serviceList.setData(Arrays.asList(service1, service2));

        when(openMetadataGateway.buildClient(DatabaseServicesApi.class)).thenReturn(databaseServicesApi);
        when(databaseServicesApi.listDatabaseServices(any())).thenReturn(serviceList);

        // When
        List<DatabaseService> result = openMetadataService.getDatabaseServices();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("MySQL Service", result.get(0).getName());
        assertEquals("PostgreSQL Service", result.get(1).getName());
    }

    @Test
    void testGetDatabaseServicesWithException() {
        // Given
        when(openMetadataGateway.buildClient(DatabaseServicesApi.class)).thenReturn(databaseServicesApi);
        when(databaseServicesApi.listDatabaseServices(any())).thenThrow(new RuntimeException("Connection failed"));

        // When
        List<DatabaseService> result = openMetadataService.getDatabaseServices();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetDatabases() {
        // Given
        Database db1 = new Database();
        db1.setName("test_db_1");
        db1.setId("db-1");

        Database db2 = new Database();
        db2.setName("test_db_2");
        db2.setId("db-2");

        DatabaseList databaseList = new DatabaseList();
        databaseList.setData(Arrays.asList(db1, db2));

        when(openMetadataGateway.buildClient(DatabasesApi.class)).thenReturn(databasesApi);
        when(databasesApi.listDatabases(any())).thenReturn(databaseList);

        // When
        List<Database> result = openMetadataService.getDatabases();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("test_db_1", result.get(0).getName());
        assertEquals("test_db_2", result.get(1).getName());
    }

    @Test
    void testGetDatabasesWithException() {
        // Given
        when(openMetadataGateway.buildClient(DatabasesApi.class)).thenReturn(databasesApi);
        when(databasesApi.listDatabases(any())).thenThrow(new RuntimeException("Connection failed"));

        // When
        List<Database> result = openMetadataService.getDatabases();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetTables() {
        // Given
        String databaseFqn = "test.database.schema";
        
        Table table1 = new Table();
        table1.setName("users");
        table1.setId("table-1");

        Table table2 = new Table();
        table2.setName("orders");
        table2.setId("table-2");

        TableList tableList = new TableList();
        tableList.setData(Arrays.asList(table1, table2));

        when(openMetadataGateway.buildClient(TablesApi.class)).thenReturn(tablesApi);
        when(tablesApi.listTables(any())).thenReturn(tableList);

        // When
        List<Table> result = openMetadataService.getTables(databaseFqn);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("users", result.get(0).getName());
        assertEquals("orders", result.get(1).getName());
    }

    @Test
    void testGetTablesWithException() {
        // Given
        when(openMetadataGateway.buildClient(TablesApi.class)).thenReturn(tablesApi);
        when(tablesApi.listTables(any())).thenThrow(new RuntimeException("Connection failed"));

        // When
        List<Table> result = openMetadataService.getTables("test.database");

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetTablesWithNullDatabaseFqn() {
        // Test that the method handles null database FQN
        assertThrows(NullPointerException.class, () -> openMetadataService.getTables(null));
    }

}
