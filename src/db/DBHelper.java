package db;

import model.User;
import util.CryptoUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBHelper {
    private static final String URL = "jdbc:sqlite:metallurgist.db";

    public static void init() throws SQLException {
        try (Connection c = DriverManager.getConnection(URL)) {
            try (Statement s = c.createStatement()) {
                s.execute("PRAGMA foreign_keys = ON");
                s.execute("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE NOT NULL, password_hash TEXT NOT NULL, role TEXT NOT NULL)");
                s.execute("CREATE TABLE IF NOT EXISTS calculations (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, input_data TEXT, result_data TEXT, created_at TEXT)");
                s.execute("""
    CREATE TABLE IF NOT EXISTS records(
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        value REAL NOT NULL,
        created_at TEXT
    )
""");           s.execute("""
CREATE TABLE IF NOT EXISTS calculation_logs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    calc_type TEXT NOT NULL,
    input_data TEXT NOT NULL,
    result_data TEXT NOT NULL,
    created_at TEXT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
)
""");


                // Добавляем тестового пользователя
                try {
                    addUser("admin", "admin", "admin");
                } catch (SQLException e) {
                    // Пользователь уже существует
                }
            }
        }
    }

    public static boolean addUser(String username, String password, String role) throws SQLException {
        String sql = "INSERT INTO users(username, password_hash, role) VALUES (?, ?, ?)";
        try (Connection c = DriverManager.getConnection(URL);
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, username);
            p.setString(2, CryptoUtils.hashPassword(password));
            p.setString(3, role);
            return p.executeUpdate() == 1;
        }
    }

    public static User authenticateUser(String username, String password) throws SQLException {
        String sql = "SELECT id, username, password_hash, role FROM users WHERE username = ?";
        try (Connection c = DriverManager.getConnection(URL);
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, username);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                if (CryptoUtils.verifyPassword(password, storedHash)) {
                    return new User(rs.getInt("id"), rs.getString("username"), storedHash, rs.getString("role"));
                }
            }
            return null;
        }
    }

    public static List<User> listUsers() throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT id, username, password_hash, role FROM users";
        try (Connection c = DriverManager.getConnection(URL);
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("password_hash"), rs.getString("role")));
            }
        }
        return list;
    }
}