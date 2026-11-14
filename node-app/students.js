const { Client } = require('pg');

function getClient() {
  return new Client({
    host: 'localhost',
    port: 5432,
    user: 'postgres',
    password: 'postgres',
    database: 'tp',
  });
}

async function addStudent(client, name, grade) {
  await client.query(
    'INSERT INTO students (name, grade) VALUES ($1, $2)',
    [name, grade]
  );
}

async function getStudent(client, id) {
  const res = await client.query(
    'SELECT id, name, grade FROM students WHERE id = $1',
    [id]
  );
  return res.rows[0] || null;
}

async function averageGrade(client) {
  const res = await client.query('SELECT AVG(grade) AS avg FROM students');
  const avg = res.rows[0].avg;
  return avg === null ? null : parseFloat(avg);
}

module.exports = { getClient, addStudent, getStudent, averageGrade };
