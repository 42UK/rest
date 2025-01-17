package rest.databse;

import java.sql.Connection;

public interface ConnectionManager {
    Connection getConnection();
}
