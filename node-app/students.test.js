const { getClient, addStudent, getStudent, averageGrade } = require('./students');

let client;

beforeAll(async () => {
  client = getClient();
  await client.connect();
});

afterAll(async () => {
  await client.end();
});

beforeEach(async () => {
  await client.query('DELETE FROM students');
});

test('add and get', async () => {
  await addStudent(client, 'Alice', 15.5);
  const res = await client.query("SELECT id FROM students WHERE name = 'Alice'");
  const student = await getStudent(client, res.rows[0].id);
  expect(student).not.toBeNull();
  expect(student.name).toBe('Alice');
  expect(parseFloat(student.grade)).toBe(15.5);
});

test('get unknown', async () => {
  const student = await getStudent(client, 99999);
  expect(student).toBeNull();
});

test('moyenne', async () => {
  await addStudent(client, 'Amel', 10);
  await addStudent(client, 'Karim', 14);
  expect(await averageGrade(client)).toBe(12.0);
});
