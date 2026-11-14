package todo;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.*;

class StudentsTest {

    static Connection conn;

    @BeforeAll
    static void connect() throws SQLException {
        conn = Students.getConnection();
    }

    @AfterAll
    static void close() throws SQLException {
        if (conn != null) conn.close();
    }

    @BeforeEach
    void clean() throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM students");
        }
    }

    @Test
    void addAndGet() throws SQLException {
        Students.addStudent(conn, "Alice", 15.5);
        int id;
        try (Statement st = conn.createStatement();
             var rs = st.executeQuery("SELECT id FROM students WHERE name = 'Alice'")) {
            rs.next();
            id = rs.getInt("id");
        }
        var student = Students.getStudent(conn, id);
        assertTrue(student.isPresent());
        assertEquals("Alice", student.get()[0]);
        assertEquals(15.5, Double.parseDouble(student.get()[1]), 0.01);
    }

    @Test
    void getUnknown() throws SQLException {
        assertTrue(Students.getStudent(conn, 99999).isEmpty());
    }

    @Test
    void moyenne() throws SQLException {
        Students.addStudent(conn, "Amel", 10);
        Students.addStudent(conn, "Karim", 14);
        assertEquals(12.0, Students.averageGrade(conn), 0.01);
    }
}
