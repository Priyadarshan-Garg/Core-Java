## 1. Write a SQL query to find the names of all employees who work in the "Engineering" department.

```textmate
    employees
    ---------
    id
    name
    department_id
    salary
    
    departments
    -----------
    id
    name
```

```sql
SELECT e.name
FROM employees e
JOIN departments d
    ON e.department_id = d.id
WHERE d.name = 'Engineering';
```

I join employees with departments using the foreign-key relationship employees.department_id = departments.id, then
filter the joined rows where the department name is Engineering.

```textmate
What do I need?
        ↓
employee name
        ↓
Which employees?
        ↓
Engineering department
        ↓
Where is "Engineering"?
        ↓
departments.name
        ↓
How do I reach departments?
        ↓
employees.department_id = departments.id
```

----

## 2. Write a query to find the names and salaries of employees whose salary is greater than ₹80,000 and who work in the Engineering department.

```textmate
    employees
    ---------
    id
    name
    department_id
    salary
    
    departments
    -----------
    id
    name
```

```sql 
SELECT e.name, e.salary
FROM employees AS e
JOIN departments AS d
    ON e.department_id = d.id
WHERE e.salary > 80000
  AND d.name = 'Engineering';
```

----

## 3. Write a query to find the average salary of each department. Expected output: department_id | average_salary
```textmate
employees
---------
id
name
department_id
salary
```
I group employees by department_id and then calculate the average salary within each group
```sql
SELECT department_id, AVG(salary) AS average_salary
FROM employees
GROUP BY department_id;
```

---

## 4. Write a query to find the departments whose average employee salary is greater than ₹80,000. Expected output: department_id | average_salary
```textmate
    FROM
     ↓
    WHERE
     ↓
    GROUP BY
     ↓
    HAVING
     ↓
    SELECT
```
WHERE happens before the grouping/aggregation.

```sql
SELECT department_id, AVG(salary) AS average_salary
FROM employees
GROUP BY department_id
HAVING AVG(salary) > 80000;
```
WHERE → filters rows
HAVING → filters groups

---

## 5. Write a query to return: department_name | employee_count for only those departments that have at least 5 employees.
```textmate
employees
---------
id
name
department_id
salary

departments
-----------
id
name
```

```sql
SELECT d.name AS department_name,
       COUNT(e.id) AS employee_count
FROM employees AS e
JOIN departments AS d
    ON e.department_id = d.id
GROUP BY d.id, d.name
HAVING COUNT(e.id) >= 5;
```
For each X, calculate aggregate Y, only where aggregate satisfies condition Z.
```sql
Think : 
SELECT X, AGG(Y)
FROM ...
GROUP BY X
HAVING AGG(Y) condition

If  X lives in another table :
FROM A
JOIN B
ON relationship
GROUP BY X
HAVING ...
```

WHERE comes before GROUP BY
```textmate
FROM
  ↓
JOIN / ON
  ↓
WHERE       ← filter individual rows
  ↓
GROUP BY    ← create groups
  ↓
HAVING      ← filter groups
  ↓
SELECT
```

```sql
SELECT d.name AS department_name,
       COUNT(e.id) AS employee_count
FROM employees AS e
JOIN departments AS d
    ON e.department_id = d.id
WHERE e.salary > 70000
GROUP BY d.id, d.name
HAVING COUNT(e.id) >= 3;
```
---

## 7. Find the department with the highest average salary. department_name | average_salary
```textmate
employees
---------
id
name
department_id
salary

departments
-----------
id
name
```
HAVING needs a condition. Like HAVING COUNT(*) >= 5;
inner query  → calculate average for every department
                    ↓
outer query  → find the department having the maximum average
I first group employees by department and calculate the average salary. I then sort those averages in descending order and take the first department.
```sql
SELECT d.name AS department_name,
       AVG(e.salary) AS average_salary
FROM employees AS e
JOIN departments AS d
    ON e.department_id = d.id
GROUP BY d.id, d.name
ORDER BY AVG(e.salary) DESC
LIMIT 1;
```

--- 

## 8. Find the second-highest distinct salary in the company.
``` textmate
employees
---------
id
name
department_id
salary
```

LIMIT 2 OFFSET 1 means: Skip one row, then return two rows.
```textmate
DISTINCT
   ↓
ORDER BY DESC
   ↓
OFFSET 1
   ↓
LIMIT 1
```
```sql
SELECT DISTINCT salary
FROM employees
ORDER BY salary DESC
LIMIT 1 OFFSET 1;
```

---
 ## 9. Find the employee(s) who have the second-highest distinct salary.

```textmate
For : 
    1 | Alice   | 100000
    2 | Bob      | 90000
    3 | Charlie  | 90000
    4 | David    | 80000
Expected :
    Bob     | 90000
    Charlie | 90000
```
Step 1 → Find the second-highest distinct salary
Step 2 → Find employees whose salary = that value

Where needs a boolean so its not correct
```textmate
WHERE (
    SELECT DISTINCT salary
    FROM employees
    ORDER BY salary DESC
    LIMIT 1 OFFSET 1
);
```
Correct way :
```sql 
SELECT e.name AS employee_name,
       e.salary
FROM employees AS e
WHERE e.salary = (
    SELECT DISTINCT salary
    FROM employees
    ORDER BY salary DESC
    LIMIT 1 OFFSET 1
);
```
Mental Model :
```textmate
Outer query
    ↓
employees
    ↓
WHERE e.salary =
    ↓
Inner query
    ↓
second-highest distinct salary
```

---

## 10. Write a query to find all employees who do not have a manager. May have NULL
```textmate
employees
---------
id
name
manager_id
```
```sql 
SELECT *
FROM employees
WHERE manager_id IS NULL;
```

---

## 11. Write Query which return manager name of the employee
```textmate
For :
    id | name    | manager_id
    ---+---------+-----------
    1  | Alice   | NULL
    2  | Bob     | 1
    3  | Charlie | 1
    4  | David   | 2
    
Expected :
    Bob     | Alice
    Charlie | Alice
    David   | Bob
```
because a normal JOIN (INNER JOIN) won't find a matching m.id for NULL, so those employees naturally disappear.

```sql
SELECT
    e.name AS employee_name,
    m.name AS manager_name
FROM employees AS e
JOIN employees AS m
    ON e.manager_id = m.id;
```

---

## 12. Write a query that returns every employee, including employees who don't have a manager.
Expected :
```textmate
employee_name | manager_name
--------------+-------------
Alice         | NULL
Bob           | Alice
Charlie       | Alice
David         | Bob
```
With Join Alice disappears because she has no manager but on left join she appears : Keep every row from the left table (e), even if there is no match in m.

```sql
SELECT
    e.name AS employee_name,
    m.name AS manager_name
FROM employees AS e
LEFT JOIN employees AS m
    ON e.manager_id = m.id;
```

```textmate
INNER JOIN
→ only rows having a match

LEFT JOIN
→ ALL rows from left table
→ matching right row if available
→ otherwise NULL
```

---

## 13. Write a query to find managers who earn less than at least one of the employees they manage. Expected : manager_name | manager_salary
```sql
SELECT DISTINCT
    m.name AS manager_name,
    m.salary AS manager_salary
FROM employees AS m
JOIN employees AS e
    ON m.id = e.manager_id
WHERE m.salary < e.salary;

```

---

 ## 14. Write a query to find all employees whose salary is greater than the average salary of the entire company.
```sql
SELECT e.name
FROM employees e
WHERE e.salary > (
    SELECT AVG(salary)
    FROM employees
);
```