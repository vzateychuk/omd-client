package ru.vez;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command(name = "omd-client", mixinStandardHelpOptions = true, version = "omd-client 1.0",
        description = "OpenMetadata Data Dictionary Client",
        subcommands = {
                DatabaseServicesCommand.class,
                DatabasesCommand.class,
                TablesCommand.class
        })
public class Main implements Callable<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Option(names = {"-s", "--server-url"}, description = "OpenMetadata server URL", required = true)
    private String serverUrl;

    @Option(names = {"-t", "--jwt-token"}, description = "JWT token for authentication", required = true)
    private String jwtToken;

    @Override
    public Integer call() throws Exception {
        logger.info("Welcome to OpenMetadata Data Dictionary Client!");
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    public OpenMetadataService createService() {
        OpenMetadataConfiguration config = new OpenMetadataConfiguration(serverUrl, jwtToken);
        return new OpenMetadataService(config.createOpenMetadataGateway());
    }
}

@Command(name = "db-services", description = "List of Database Services")
class DatabaseServicesCommand implements Callable<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseServicesCommand.class);

    @CommandLine.ParentCommand
    private Main parent;

    @Override
    public Integer call() throws Exception {
        OpenMetadataService service = parent.createService();
        service.getDatabaseServices().forEach(srv -> logger.info(srv.getName()));
        return 0;
    }
}

@Command(name = "databases", description = "List databases")
class DatabasesCommand implements Callable<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(DatabasesCommand.class);

    @CommandLine.ParentCommand
    private Main parent;

    @Override
    public Integer call() throws Exception {
        OpenMetadataService service = parent.createService();
        service.getDatabases().forEach(db -> logger.info(db.getName()));
        return 0;
    }
}

@Command(name = "tables", description = "List tables in a database")
class TablesCommand implements Callable<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(TablesCommand.class);

    @CommandLine.ParentCommand
    private Main parent;

    @Option(names = {"-d", "--database"}, description = "Fully qualified database name", required = true)
    private String databaseFqn;

    @Override
    public Integer call() throws Exception {
        OpenMetadataService service = parent.createService();
        service.getTables(databaseFqn).forEach(table -> logger.info(table.getName()));
        return 0;
    }
}