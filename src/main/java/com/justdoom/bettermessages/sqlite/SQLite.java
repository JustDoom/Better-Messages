package com.justdoom.bettermessages.sqlite;

import com.justdoom.bettermessages.BetterMessages;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.UUID;

public class SQLite {

    private final BetterMessages plugin;

    public SQLite(BetterMessages plugin) {
        this.plugin = plugin;
    }

    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:" + plugin.getDataFolder() + "/database.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**public void connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:" + plugin.getDataFolder();
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }**/

    public static void createNewDatabase(String fileName, JavaPlugin plugin) {

        String url = "jdbc:sqlite:" + plugin.getDataFolder() + "/" + fileName;

        String sql = "CREATE TABLE IF NOT EXISTS data (\n"
                + "	uuid VARCHAR(100) PRIMARY KEY,\n"
                + "	joins integer NOT NULL\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean getUuid(UUID uuid) {
        String sql = "SELECT * FROM data WHERE uuid='"+uuid+"'";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                if(rs.getString("uuid").equals(uuid.toString())){
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public int getJoins(UUID uuid) {
        String sql = "SELECT uuid,joins FROM data";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                if(rs.getString("uuid").equals(uuid.toString())){
                    return rs.getInt("joins");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return 1;
    }

    public void insert(UUID uuid) {
        String sql = "INSERT INTO data(uuid,joins) VALUES(?,?)";

        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            pstmt.setDouble(2, 1);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(UUID uuid) {
        String sql = "UPDATE data SET joins = ? "
                + "WHERE uuid = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, getJoins(uuid) + 1);
            pstmt.setString(2, uuid.toString());
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
