package com.minecraft.mobbattle;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * PlayerService Class
 * Provides business logic operations for Player authentication, registration,
 * and battlefield statistics updates.
 * 
 * Secure practices: Uses PreparedStatement to avoid SQL injection,
 * and hashes passwords using SHA-256.
 */
public class PlayerService {

    /**
     * Hashes password using SHA-256 algorithm.
     * 
     * @param password Cleartext password
     * @return Hexadecimal representation of hashed password
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Hashing algorithm SHA-256 not found. Storing plain password instead as fallback.");
            return password;
        }
    }

    /**
     * STUDENT TASK 2: PlayerService.login()
     * COMPLETED: Complete SQL query and return the correct Player object.
     * Checks if the username exists, and validates the password.
     * 
     * @param username User input username
     * @param password User input password
     * @return Player object if authentication is successful, null otherwise
     * @throws SQLException if a database connection or query error occurs
     */
    public Player login(String username, String password) throws SQLException {
        String query = "SELECT * FROM players WHERE username = ? AND password = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseManager.getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, hashPassword(password));
            rs = stmt.executeQuery();

            if (rs.next()) {
                return new Player(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getInt("wins"),
                    rs.getInt("losses"),
                    rs.getInt("draws"),
                    rs.getInt("score")
                );
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        return null;
    }

    /**
     * Registers a new player in the database.
     * 
     * @param username Choice of username
     * @param password Choice of password
     * @return true if registration was successful, false if database error occurred
     * @throws SQLException if a database connection error occurs
     */
    public boolean register(String username, String password) throws SQLException {
        String insertSQL = "INSERT INTO players (username, password, wins, losses, draws, score) VALUES (?, ?, 0, 0, 0, 0)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DatabaseManager.getConnection();
            stmt = conn.prepareStatement(insertSQL);
            stmt.setString(1, username);
            stmt.setString(2, hashPassword(password));
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    /**
     * STUDENT TASK 3: PlayerService.updateStatistics()
     * COMPLETED: Update wins, losses, draws, and score after game result.
     * Updates statistical results (wins, losses, draws, score) of a player after a battle.
     * 
     * @param playerId ID of the player
     * @param winsDelta Value to add to wins (e.g. 1 or 0)
     * @param lossesDelta Value to add to losses (e.g. 1 or 0)
     * @param drawsDelta Value to add to draws (e.g. 1 or 0)
     * @param scoreDelta Value to add to score (e.g. 10, 3, or 0)
     * @return true if successfully updated, false otherwise
     */
    public boolean updateStatistics(int playerId, int winsDelta, int lossesDelta, int drawsDelta, int scoreDelta) {
        String updateSQL = "UPDATE players SET wins = wins + ?, losses = losses + ?, draws = draws + ?, score = score + ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DatabaseManager.getConnection();
            stmt = conn.prepareStatement(updateSQL);
            stmt.setInt(1, winsDelta);
            stmt.setInt(2, lossesDelta);
            stmt.setInt(3, drawsDelta);
            stmt.setInt(4, scoreDelta);
            stmt.setInt(5, playerId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Failed to update statistics: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    /**
     * Retrieves a Player from the database by their unique ID.
     * Useful for refreshing screen statistics.
     * 
     * @param playerId player's database ID
     * @return Player object if found, null otherwise
     */
    public Player getPlayerById(int playerId) {
        String query = "SELECT * FROM players WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseManager.getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, playerId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return new Player(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getInt("wins"),
                    rs.getInt("losses"),
                    rs.getInt("draws"),
                    rs.getInt("score")
                );
            }
        } catch (SQLException e) {
            System.err.println("Retrieving player failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        return null;
    }

    /**
     * STUDENT TASK 4: PlayerService.getTopFiveScorers()
     * COMPLETED: Retrieve Top 5 players from database.
     * Gets top 5 scorers sorted by score desc, then wins desc.
     * 
     * @return List of Player objects
     */
    public List<Player> getTopFiveScorers() {
        List<Player> topPlayers = new ArrayList<>();
        String query = "SELECT id, username, password, wins, losses, draws, score " +
                       "FROM players ORDER BY score DESC, wins DESC LIMIT 5";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseManager.getConnection();
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Player p = new Player(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getInt("wins"),
                    rs.getInt("losses"),
                    rs.getInt("draws"),
                    rs.getInt("score")
                );
                topPlayers.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Retrieving leaderboard failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        return topPlayers;
    }

    /**
     * Helper method to safely close open database connections, statements, and result sets.
     */
    private void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
