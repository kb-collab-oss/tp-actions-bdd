import psycopg2


def get_connection():
    """Retourne une connexion à la base tp sur localhost."""
    return psycopg2.connect(
        host="localhost",
        port=5432,
        user="postgres",
        password="postgres",
        dbname="tp",
    )


def add_student(conn, name, grade):
    """Insère un étudiant dans la table students."""
    with conn.cursor() as cur:
        cur.execute(
            "INSERT INTO students (name, grade) VALUES (%s, %s)",
            (name, grade),
        )
    conn.commit()


def get_student(conn, student_id):
    """Retourne (id, name, grade) ou None si l'étudiant n'existe pas."""
    with conn.cursor() as cur:
        cur.execute(
            "SELECT id, name, grade FROM students WHERE id = %s",
            (student_id,),
        )
        row = cur.fetchone()
    return row if row else None


def average_grade(conn):
    """Calcule la moyenne des notes."""
    raise NotImplementedError("average_grade n'est pas encore implémentée")
