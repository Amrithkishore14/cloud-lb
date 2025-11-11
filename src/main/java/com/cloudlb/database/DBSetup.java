package com.cloudlb.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class DBSetup {
    public static void initDatabase() {
        try (Connection conn = DBConnect.getConnection();
             Statement stmt = conn.createStatement()) {

            String createTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    role TEXT DEFAULT 'standard'
                );
            """;
            stmt.execute(createTable);

            // create default admin
            String adminHash = hashPassword("admin");
            PreparedStatement ps = conn.prepareStatement(
                "INSERT OR IGNORE INTO users (username, password, role) VALUES ('admin', ?, 'admin')"
            );
            ps.setString(1, adminHash);
            ps.executeUpdate();

            System.out.println("✅ DB and Admin user ready!");
        } catch (Exception e) {
            System.out.println("❌ DB setup error: " + e.getMessage());
        }
    }

    private static String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return password;
        }
    }
}

