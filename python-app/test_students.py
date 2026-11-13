import pytest

from students import add_student, get_student, average_grade, get_connection


@pytest.fixture
def conn():
    """Fournit une connexion et vide la table avant chaque test."""
    connection = get_connection()
    with connection.cursor() as cur:
        cur.execute("DELETE FROM students;")
    connection.commit()
    yield connection
    connection.close()


def test_add_and_get(conn):
    """Test 1 : ajouter un étudiant puis le relire."""
    add_student(conn, "Alice", 15.5)

    with conn.cursor() as cur:
        cur.execute("SELECT id FROM students WHERE name = 'Alice';")
        student_id = cur.fetchone()[0]

    student = get_student(conn, student_id)
    assert student is not None
    assert student[1] == "Alice"
    assert float(student[2]) == 15.5


def test_get_unknown(conn):
    """Test 2 : lire un étudiant inexistant renvoie None."""
    assert get_student(conn, 99999) is None


def test_moyenne(conn):
    """Test 3 : moyenne de 10 et 14 = 12."""
    add_student(conn, "Amel", 10)
    add_student(conn, "Karim", 14)
    assert average_grade(conn) == 12.0
