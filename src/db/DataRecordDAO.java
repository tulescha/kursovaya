//package db;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class DataRecordDAO {
//
//    private static final String URL = "jdbc:sqlite:metallurgist.db";
//
//    public static void add(double value) throws SQLException {
//        try (Connection c = DriverManager.getConnection(URL)) {
//            String sql = "INSERT INTO records(value, created_at) VALUES (?, datetime('now'))";
//            try (PreparedStatement p = c.prepareStatement(sql)) {
//                p.setDouble(1, value);
//                p.executeUpdate();
//            }
//        }
//    }
//
//    public static void delete(int id) throws SQLException {
//        try (Connection c = DriverManager.getConnection(URL)) {
//            String sql = "DELETE FROM records WHERE id = ?";
//            try (PreparedStatement p = c.prepareStatement(sql)) {
//                p.setInt(1, id);
//                p.executeUpdate();
//            }
//        }
//    }
//
//    public static List<Double> getAll() throws SQLException {
//        List<Double> list = new ArrayList<>();
//        try (Connection c = DriverManager.getConnection(URL)) {
//            String sql = "SELECT value FROM records";
//            try (Statement s = c.createStatement();
//                 ResultSet rs = s.executeQuery(sql)) {
//                while (rs.next()) list.add(rs.getDouble("value"));
//            }
//        }
//        return list;
//    }
//
//    public static List<Double> search(double min, double max) throws SQLException {
//        List<Double> list = new ArrayList<>();
//        try (Connection c = DriverManager.getConnection(URL)) {
//            String sql = "SELECT value FROM records WHERE value BETWEEN ? AND ?";
//            try (PreparedStatement p = c.prepareStatement(sql)) {
//                p.setDouble(1, min);
//                p.setDouble(2, max);
//                ResultSet rs = p.executeQuery();
//                while (rs.next()) list.add(rs.getDouble(1));
//            }
//        }
//        return list;
//    }
//
//    public static List<Double> sorted(boolean asc) throws SQLException {
//        List<Double> list = new ArrayList<>();
//        String order = asc ? "ASC" : "DESC";
//        try (Connection c = DriverManager.getConnection(URL)) {
//            String sql = "SELECT value FROM records ORDER BY value " + order;
//            try (Statement s = c.createStatement();
//                 ResultSet rs = s.executeQuery(sql)) {
//                while (rs.next()) list.add(rs.getDouble(1));
//            }
//        }
//        return list;
//    }
//}
