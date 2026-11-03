package com.subdivision.gateentry.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** Handles the SQLite connection and first-time database setup. */
public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:gateentry.db";

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    /** Creates the users table (if missing) and seeds a default admin. */
    public static void initialize() {
        String createUsers = """
                CREATE TABLE IF NOT EXISTS users (
                    user_id       INTEGER PRIMARY KEY AUTOINCREMENT,
                    username      TEXT NOT NULL UNIQUE,
                    password_hash TEXT NOT NULL,
                    full_name     TEXT NOT NULL,
                    role          TEXT NOT NULL CHECK (role IN ('GUARD','ADMIN','RESIDENT')),
                    status        TEXT NOT NULL DEFAULT 'PENDING'
                                  CHECK (status IN ('PENDING','ACTIVE','INACTIVE'))
                )
                """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createUsers);
        } catch (SQLException e) {
            System.out.println("Database setup failed: " + e.getMessage());
            return;
        }

        // Seed a default ACTIVE admin so pending accounts can be approved.
        UserDao dao = new UserDao();
        if (!dao.usernameExists("admin")) {
            dao.register(new com.subdivision.gateentry.model.User(
                    0, "admin", UserDao.hash("admin123"),
                    "System Administrator", "ADMIN", "ACTIVE"));
        }
    }
}
