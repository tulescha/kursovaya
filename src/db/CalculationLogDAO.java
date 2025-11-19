package db;

import model.CalculationLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CalculationLogDAO {

    private static final String URL = "jdbc:sqlite:metallurgist.db";

    public static void add(CalculationLog log) throws SQLException {
        String sql = """
            INSERT INTO calculation_logs(user_id, calc_type, input_data, result_data, created_at)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection c = DriverManager.getConnection(URL);
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setInt(1, log.getUserId());
            p.setString(2, log.getCalcType());
            p.setString(3, log.getInputData());
            p.setString(4, log.getResultData());
            p.setString(5, log.getCreatedAt().toString());

            p.executeUpdate();
        }
    }

    public static List<CalculationLog> getAll() throws SQLException {
        List<CalculationLog> list = new ArrayList<>();

        String sql = "SELECT * FROM calculation_logs ORDER BY created_at DESC";

        try (Connection c = DriverManager.getConnection(URL);
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new CalculationLog(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("calc_type"),
                        rs.getString("input_data"),
                        rs.getString("result_data"),
                        rs.getString("created_at")
                ));
            }
        }
        return list;
    }

    public static void delete(int id) throws SQLException {
        try (Connection c = DriverManager.getConnection(URL)) {
            String sql = "DELETE FROM calculation_logs WHERE id = ?";
            try (PreparedStatement p = c.prepareStatement(sql)) {
                p.setInt(1, id);
                p.executeUpdate();
            }
        }
    }
}
