package todo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Students {

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/tp",
            "postgres",
            "postgres"
        );
    }

    public static void addStudent(Connection conn, String name, double grade) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO students (name, grade) VALUES (?, ?)")) {
            ps.setString(1, name);
            ps.setDouble(2, grade);
            ps.executeUpdate();
        }
    }

    public static Optional<String[]> getStudent(Connection conn, int id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT name, grade FROM students WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(new String[]{
                    rs.getString("name"),
                    rs.getString("grade")
                });
            }
        }
    }

    public static Double averageGrade(Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT AVG(grade) AS avg FROM students");
             ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) return null;
            double v = rs.getDouble("avg");
            if (rs.wasNull()) return null;
            return v;
        }
    }
}
