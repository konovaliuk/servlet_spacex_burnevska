package me.braun.spacex.connection;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

@Slf4j
@AllArgsConstructor
public class ConnectionPool {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("dao");
    private static final String URL = RESOURCE_BUNDLE.getString("db.url");
    private static final String USERNAME = RESOURCE_BUNDLE.getString("db.login");
    private static final String PASSWORD = RESOURCE_BUNDLE.getString("db.password");
    private static final List<Connection> connectionPool = new LinkedList<>();
    private static final List<Connection> usedConnections = new LinkedList<>();
    private static final int MAX_POOL_SIZE = 20;
    private static ConnectionPool instance = null;


    public static ConnectionPool getInstance() {
        if (instance == null)
            instance = new ConnectionPool();
        return instance;
    }

    public void releaseConnection(Connection connection) {
        connectionPool.add(connection);
        usedConnections.remove(connection);
    }

    private static Connection createConnection(String URL, String USERNAME, String PASSWORD)
            throws SQLException {
        log.info(URL);
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);

    }

    public Connection getConnection() throws SQLException {
        if (connectionPool.isEmpty()) {
            if (usedConnections.size() < MAX_POOL_SIZE) {
                connectionPool.add(createConnection(URL, USERNAME, PASSWORD));
            } else {
                throw new RuntimeException(
                        "Maximum pool size reached, no available connections!");
            }
        }
        Connection connection = connectionPool.remove(connectionPool.size() - 1);
        usedConnections.add(connection);
        return connection;
    }

    public void shutdown() throws SQLException {
        usedConnections.forEach(this::releaseConnection);
        for (Connection connection : connectionPool) {
            connection.close();
        }
        connectionPool.clear();
    }
}



//    public ConnectionPool(String url, String username, String password, List<Connection> pool) {
//    }
//
//
//    public static ConnectionPool create(String URL, String USERNAME, String PASSWORD)
//            throws SQLException {
//        List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
//        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
//            pool.add(createConnection(URL, USERNAME, PASSWORD));
//        }
//        return new ConnectionPool(URL, USERNAME, PASSWORD, pool);
//    }

