# omd-client

Simple Java CLI application for calling the OpenMetadata service.

## Requirements

- Java 21 or newer
- Maven 3.8+

## Build

Build the project using Maven:

```sh
mvn clean package
```

## Run

Run the application from the command line with required parameters:  
`--server-url (-s)` — OpenMetadata server URL
`--jwt-token (-t)` — JWT token for authentication

## Examples

### List databases:

```sh
java -jar target/omd-client-1.0-SNAPSHOT.jar -s http://localhost:8585 -t <your_jwt_token> databases
```

### List tables in a database:

```sh
java -jar target/omd-client-1.0-SNAPSHOT.jar -s http://localhost:8585 -t <your_jwt_token> tables -d <database_fqn>
```

### Show help:

```sh
java -jar target/omd-client-1.0-SNAPSHOT.jar --help
```

## Logging

This application uses SLF4J Simple for logging.

### Configuring log level

Set the log level using JVM system properties when starting the application.

#### Global log level:
```sh
java -Dorg.slf4j.simpleLogger.defaultLogLevel=info -jar target/omd-client-1.0-SNAPSHOT.jar ...
```

#### Specific log level for a package:
```sh
java -Dorg.slf4j.simpleLogger.log.<package_name>=debug -jar target/omd-client-1.0-SNAPSHOT.jar ...
``` 