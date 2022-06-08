package me.braun.spacex;

import me.braun.spacex.dao.sql.SQLTest;
import me.braun.spacex.connection.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class Main {
    public static Connection connection;
    public static Statement statement;
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(
                () -> {
                    try {
                        onDisable();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    log.info("Stopped SpaceX");
                }, "Shutdown Thread"));
        onEnable();
    }

    private static void connect() {
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            statement = connection.createStatement();
            log.info("Successful SQL connection!");
        } catch (SQLException e) {
            log.error("Connection failed!", e);
        }
    }

    private static void onEnable() {
        log.info("Launching SpaceX application");
        connect();

        SQLTest test = new SQLTest();

        test.executeMission();
        test.executeAccount();
        test.executeSpacecraft();
    }

    private static void onDisable() throws SQLException {
        ConnectionPool.getInstance().shutdown();
        log.info("SQL connection is closed!");
    }
}
