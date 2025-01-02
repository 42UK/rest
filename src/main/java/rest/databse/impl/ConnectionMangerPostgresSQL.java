package rest.databse.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import rest.databse.ConnectionManager;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public class ConnectionMangerPostgresSQL implements ConnectionManager {

    private String url;
    private String user;
    private String password;
    private HikariDataSource dataSource;
    private final Logger log = Logger.getLogger(String.valueOf(ConnectionMangerPostgresSQL.class));


    public ConnectionMangerPostgresSQL() {
        loadDatabaseConfig();
        initializeDataSource();
    }

    private void loadDatabaseConfig() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                log.warning("Не удалось найти файл application.properties");
            }
            properties.load(input);
            Class.forName(properties.getProperty("database.driver"));
            url = properties.getProperty("url.database");
            user = properties.getProperty("user.database");
            password = properties.getProperty("pswd.database");
            log.info("Соединение с БД успешно");
        } catch (Exception e) {
            log.warning("Не верные данные из файла application.property");
        }
    }

    // Инициализация пула соединений HikariCP
    private void initializeDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(20);  // Максимальное количество соединений в пуле
        config.setMinimumIdle(5);       // Минимальное количество неактивных соединений
        config.setIdleTimeout(30000);   // Время простоя соединения (в миллисекундах)
        config.setConnectionTimeout(30000); // Максимальное время ожидания соединения (в миллисекундах)

        dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void close() {
        if (dataSource != null) {
            dataSource.close();
            log.info("Все соединения с бд успешно закрыты");
        }
    }
}
