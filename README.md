# Java_JPA_Hibernate-lab-03_Solved

## 1. Exercise

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

## 2. Solution

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

## 3. Application folders and files structure

<img width="433" height="699" alt="image" src="https://github.com/user-attachments/assets/e22f6fcc-2ac4-44d0-b5b3-5d7be72030ba" />

## 4. Application persistence.xml file 

This is your JPA configuration file (persistence.xml), which defines how your application connects to the database and what JPA provider it uses. 

Let’s go line by line so you clearly understand what’s happening:

```xml
<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd"
             version="3.0">
    <persistence-unit name="persistenceUnits.lab03">
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
        <properties>
            <property name="jakarta.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>
            <property name="jakarta.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/jpa_db_03?createDatabaseIfNotExist=true&amp;useSSL=false&amp;allowPublicKeyRetrieval=true&amp;serverTimezone=UTC"/>
            <property name="jakarta.persistence.jdbc.user" value="root"/>
            <property name="jakarta.persistence.jdbc.password" value="root"/>
            <property name="hibernate.dialect" value="org.hibernate.dialect.MySQL57Dialect"/>
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
            <property name="hibernate.cache.use.query_cache" value="false"/>
            <property name="hibernate.cache.use_second_level_cache" value="false"/>
            <property name="hibernate.hbm2ddl.auto" value="create"/>
        </properties>
    </persistence-unit>
</persistence>
```

**File purpose**

persistence.xml is a mandatory configuration file for JPA applications.

It lives inside META-INF/ and tells JPA:

What persistence units exist (usually one per database).

Which provider (e.g., Hibernate) will implement JPA.

How to connect to the database (driver, URL, username, password).

How schema creation and logging should behave.

**🧩 Structure explained**

```
<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd"
             version="3.0">
```

This declares:

The XML schema for Jakarta Persistence 3.0 (the version used since Jakarta EE 9+).

The namespace and validation info for the XML file.

**🧰 Persistence Unit**

```
<persistence-unit name="persistenceUnits.lab03">
```

Defines one persistence unit (a logical database configuration).

Your Java code refers to it in:

```
Persistence.createEntityManagerFactory("persistenceUnits.lab03");
```

So this connects that call to these settings.

**⚙️ Provider**

```
<provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
```

Specifies that Hibernate will be the JPA implementation (provider).

Hibernate translates your JPA entity operations into SQL statements.

**🗄️ Database connection properties**

```
<property name="jakarta.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>
```

→ JDBC driver for MySQL.

```
<property name="jakarta.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/jpa_db_03?..."/>
```

→ URL for connecting to the MySQL database jpa_db_03.
The flags:

createDatabaseIfNotExist=true → creates DB if missing.

useSSL=false → disables SSL.

allowPublicKeyRetrieval=true → allows password authentication in dev setups.

serverTimezone=UTC → ensures consistent timestamp handling.

```
<property name="jakarta.persistence.jdbc.user" value="root"/>
<property name="jakarta.persistence.jdbc.password" value="root"/>
```

→ Credentials for the connection.

**💬 Hibernate behavior settings**

```
<property name="hibernate.dialect" value="org.hibernate.dialect.MySQL57Dialect"/>
```

Tells Hibernate what SQL dialect to use (MySQL 5.7 syntax and types).

```
<property name="hibernate.show_sql" value="true"/>
<property name="hibernate.format_sql" value="true"/>
```

→ Prints SQL statements to the console, formatted for readability.

```
<property name="hibernate.cache.use.query_cache" value="false"/>
<property name="hibernate.cache.use_second_level_cache" value="false"/>
```

→ Disables caching for simplicity (useful in demos or development).

**🧩 Schema generation**

```
<property name="hibernate.hbm2ddl.auto" value="create"/>
```

Tells Hibernate what to do with the database schema on startup:

create → drops existing tables and recreates them each run.

Common alternatives:

update → updates schema if needed.

validate → only checks schema.

none → does nothing.

In your example, every time you run the program, the **schema is recreated** from the entity mappings.

<img width="790" height="472" alt="image" src="https://github.com/user-attachments/assets/9409917f-5b5b-4c41-862e-c4116ab11400" />

🧩 This persistence.xml tells Hibernate how to connect to MySQL, log SQL statements, and automatically create the database schema each time, 

using the persistence unit named persistenceUnits.lab03 — the one used in your Launcher class.

## 5. Application Output

<img width="513" height="328" alt="image" src="https://github.com/user-attachments/assets/8a4725d5-46cd-4342-aceb-60bf002f9e5e" />

Search for "[==CONSOLE==]" in the Terminaal:

<img width="1919" height="1026" alt="image" src="https://github.com/user-attachments/assets/6b080121-6056-47da-af4f-133ef513792b" />

## 6. How to Run the Application

Build only: 

```
mvn -q -DskipTests=true package
```

Clean + build: 

```
mvn -q -DskipTests=true clean package
```

Run the app: 

```
mvn -q exec:java
```
