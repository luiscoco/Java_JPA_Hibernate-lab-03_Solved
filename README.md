#

## Exercise

Entities relations definition

Goal

Learn how to describe entities relations in terms of Java Persistence API.

Subject

There are the following entities: Company, Department and Employee. The entities relates to each other as follows:

o	Company relates to Department as one-to-many

o	Department relates to Company as many-to-one

o	Department relates to Employee as one-to-many

o	Employee relates to Department as many-to-one

o	Relations Company-Department and Department-Employee are bidirectional.

o	Employee contains collection of Project objects.

Project is not an entity. It means that it does not have its own identifier and cannot be persisted separately (without owning entity).

<img width="408" height="155" alt="image" src="https://github.com/user-attachments/assets/545dc407-868b-4097-aa5e-b98ad9efe9de" />

You need to describe relations between the entities and objects using annotations @OneToMany, @ManyToOne and @ElementCollection.

Description
1.	Open module jpa-lab-03

2.	Open class edu.jpa.entity.Company:

3.	Look on the field departments of type List<Department>. This field contains all departments that belong to company. Specify field-level annotation @OneToMany for the field departments. Annotation argument “mappedBy” should be specified to let JPA runtime know that this is bidirectional relation, and relation is defined by metadata specified for field defined by “mappedBy”: @OneToMany(mappedBy = "company")

4.	Open class edu.jpa.entity.Department:

5.	Look on the field company of type Company. This field contains the reference to Company entity this department belongs to. Specify field-level annotation @ManyToOne for the field company.

6.	Look on the field employees of type List<Employee>. This field contains all employees that belong to the department. Specify field-level annotation @OneToMany for the field employees. Annotation argument “mappedBy” should be specified to let JPA runtime know that this is bidirectional relation, and relation is defined by metadata specified for field defined by “mappedBy”: @OneToMany(mappedBy = "department")

7.	Open class edu.jpa.entity.Employee:

8.	Look on the field department of type Department. This field contains the reference to Department entity this employee belongs to. Specify field-level annotation @ManyToOne for the field department.

9.	Look on the field projects of type List<Project>. This field contains all the projects the employee is involved in. Specify field-level annotation @ElementCollection for the field projects.

10.	Open the class edu.jpa.entity.embeddables.Project and marl this as embeddable one. Specify the class-level annotation @Embeddable and make this class implementing java.io.Serializable.

11.	Open the class edu.jpa.Launcher and analyze its content with help of trainer (since EntityManager is not known for you yet).

12.	Run the class edu.jpa.Launcher. There should be no errors if entity is defined correctly. 

13.	Analyze queries JPA runtime sends to database for data extraction (see STDOUT).

14.	Open database DB_LAB_03 using MySQL Workbench and look on the created database objects.

## Solution

Here are SQL snippets you can run in MySQL Workbench to verify the app data and relationships.

Use the schema

```
USE jpa_db_03;
```

Inspect schema objects

```
SHOW TABLES;

SHOW CREATE TABLE Company;

SHOW CREATE TABLE Department;

SHOW CREATE TABLE Employee;

SHOW CREATE TABLE employee_project;
```
Verify inserted data

```
SELECT * FROM Company;

SELECT * FROM Department ORDER BY id;

SELECT * FROM Employee ORDER BY id;

SELECT * FROM employee_project ORDER BY employee_id, name;
```

Check expected counts/values

```
SELECT COUNT(*) AS company_count FROM Company; -- expect 1

SELECT COUNT(*) AS dept_count FROM Department; -- expect 2

SELECT COUNT(*) AS emp_count FROM Employee; -- expect 1

SELECT COUNT(*) AS proj_count FROM employee_project; -- expect 2

SELECT name FROM Company WHERE id = 1; -- “Company #1”

SELECT name FROM Department WHERE id IN (1,2) ORDER BY id;-- “Department #1”, “Department #2”

SELECT name FROM Employee WHERE id = 1; -- “Employee #1”
```

Verify relationships with joins

```
SELECT c.id, c.name, d.id AS dept_id, d.name AS dept_name
FROM Company c JOIN Department d ON d.company_id = c.id
WHERE c.id = 1 ORDER BY d.id;

SELECT d.id, d.name, e.id AS emp_id, e.name AS emp_name
FROM Department d LEFT JOIN Employee e ON e.department_id = d.id
WHERE d.id IN (1,2) ORDER BY d.id, e.id;

SELECT e.id AS emp_id, e.name AS emp_name, p.name AS project_name
FROM Employee e JOIN employee_project p ON p.employee_id = e.id
WHERE e.id = 1 ORDER BY project_name; -- expect Project #1, Project #2
```

Inspect FKs (optional)

```
SELECT constraint_name, table_name, column_name, referenced_table_name, referenced_column_name
FROM information_schema.KEY_COLUMN_USAGE
WHERE table_schema = 'jpa_db_03' AND referenced_table_name IS NOT NULL
ORDER BY table_name, column_name;
```
