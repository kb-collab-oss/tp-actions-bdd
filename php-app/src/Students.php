<?php

class Students
{
    public static function getConnection(): PDO
    {
        return new PDO(
            'pgsql:host=localhost;port=5432;dbname=tp',
            'postgres',
            'postgres',
            [PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION]
        );
    }

    public static function addStudent(PDO $conn, string $name, float $grade): void
    {
        $stmt = $conn->prepare('INSERT INTO students (name, grade) VALUES (:name, :grade)');
        $stmt->execute(['name' => $name, 'grade' => $grade]);
    }

    public static function getStudent(PDO $conn, int $id): ?array
    {
        $stmt = $conn->prepare('SELECT id, name, grade FROM students WHERE id = :id');
        $stmt->execute(['id' => $id]);
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
        return $row === false ? null : $row;
    }

    public static function averageGrade(PDO $conn): ?float
    {
        $stmt = $conn->query('SELECT AVG(grade) AS avg FROM students');
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
        return $row['avg'] === null ? null : (float) $row['avg'];
    }
}
