<?php

use PHPUnit\Framework\TestCase;

require_once __DIR__ . '/../src/Students.php';

class StudentsTest extends TestCase
{
    private ?PDO $conn = null;

    protected function setUp(): void
    {
        $this->conn = Students::getConnection();
        $this->conn->exec('DELETE FROM students');
    }

    protected function tearDown(): void
    {
        $this->conn = null;
    }

    public function testAddAndGet(): void
    {
        Students::addStudent($this->conn, 'Alice', 15.5);
        $stmt = $this->conn->query("SELECT id FROM students WHERE name = 'Alice'");
        $id = (int) $stmt->fetchColumn();
        $student = Students::getStudent($this->conn, $id);
        $this->assertNotNull($student);
        $this->assertSame('Alice', $student['name']);
        $this->assertEquals(15.5, (float) $student['grade']);
    }

    public function testGetUnknown(): void
    {
        $this->assertNull(Students::getStudent($this->conn, 99999));
    }

    public function testMoyenne(): void
    {
        Students::addStudent($this->conn, 'Amel', 10);
        Students::addStudent($this->conn, 'Karim', 14);
        $this->assertEquals(12.0, Students::averageGrade($this->conn));
    }
}
