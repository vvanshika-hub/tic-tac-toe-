package com.minecraft.mobbattle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DatabaseManager Class
 * Manages JDBC connection setup to the MySQL database.
 * Also handles automatic schema initialization so the application
 * can start on an empty database without manual table imports.
 * 
 * Safe JDBC practices: Handles ClassNotFoundException and SQLException.
 */
public class DatabaseManager {
    // STUDENT TASK 1: Complete database URL, username, and password.
    // COMPLETED: Defined the host, port, database name, server and database connection URLs,
    // and standard MySQL credentials (root / empty password).
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DATABASE = "minecraft_mob_battle";
    
    // Connection string to MySQL server without database first (to create database if not exists)
    private static final String SERVER_URL = "jdbc:mysql://" + HOST + ":" + PORT + "/?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DB_URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    
    private static final String USER = "root";
    private static final String PASSWORD = ""; // standard empty password for root

    // Load JDBC Driver class
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found in classpath! Make sure you include the mysql-connector-j jar.");
            e.printStackTrace();
        }
    }

    /**
     * Obtains a connection to the specific minecraft_mob_battle database.
     * 
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    /**
     * Initializes the database.
     * 1. Connects to MySQL server.
     * 2. Creates 'minecraft_mob_battle' database if it doesn't exist.
     * 3. Creates the 'players' table if it doesn't exist.
     */
    public static void initializeDatabase() {
        Connection conn = null;
        Statement stmt = null;
        try {
            // 1. Connect to server (without DB name) to create DB
            conn = DriverManager.getConnection(SERVER_URL, USER, PASSWORD);
            stmt = conn.createStatement();
            
            // Create database
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DATABASE);
            System.out.println("Database check/creation completed successfully.");
            
            // Close statement and connection to reopen with DB context
            stmt.close();
            conn.close();

            // 2. Connect to the specific DB and create tables
            conn = getConnection();
            stmt = conn.createStatement();

            // Create players table
            String createTableSQL = "CREATE TABLE IF NOT EXISTS players (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(50) UNIQUE NOT NULL, " +
                    "password VARCHAR(100) NOT NULL, " +
                    "wins INT DEFAULT 0, " +
                    "losses INT DEFAULT 0, " +
                    "draws INT DEFAULT 0, " +
                    "score INT DEFAULT 0" +
                    ")";
            stmt.executeUpdate(createTableSQL);
            System.out.println("Database tables initialized successfully.");

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Safe cleanup of resources
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
