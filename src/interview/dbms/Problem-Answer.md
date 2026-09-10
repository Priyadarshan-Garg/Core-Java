## 1. What is an index in a database? Why does an index make a query faster, and what are the disadvantages of using indexes?

An index is a separate data structure maintained by the database to speed up data retrieval based on one or more
columns. For example, without an index, searching for a particular ID may require a sequential scan of the table, which
can take O(n). With a suitable B-tree index, the database can locate the indexed value in approximately O(log n) time
and then access the corresponding row. The trade-off is that indexes consume additional storage and make write
operations more expensive because the indexes also need to be maintained

---

## 2. What is the difference between a PRIMARY KEY and a UNIQUE constraint? Can a table have multiple UNIQUE constraints? Can it have multiple primary keys?

A primary key uniquely identifies each row in a table. It must be unique and cannot contain NULL values, and a table can
have only one primary key constraint, although that primary key can consist of multiple columns.

A UNIQUE constraint also enforces uniqueness, but unlike a primary key, it can allow NULL values under PostgreSQL's
default behaviour. A table can have multiple UNIQUE constraints.

## Follow-up : Can a primary key contain multiple columns?

Yes. That's called a composite primary key

```sqlite-psql
PRIMARY KEY (student_id, course_id)
```

---

## 3. What is database normalization? Why do we normalize databases? Explain 1NF, 2NF and 3NF with an example.

1NF = atomic values, no repeating groups.

```textmate
Bad :
    student_id | courses
    1          | DBMS, OS, CN

Good :
    student_id | course
    1          | DBMS
    1          | OS
    1          | CN
```

Normalization is the process of organising data into related tables to reduce unnecessary redundancy and prevent insert,
update and delete anomalies. 1NF requires atomic values and eliminates repeating groups. 2NF builds on 1NF and removes
partial dependencies on part of a composite key. 3NF builds on 2NF and removes transitive dependencies, so non-key
attributes depend only on the key.

---

## 4. What are the ACID properties of a database transaction? Explain each one with a simple example.

ACID describes the key properties of reliable database transactions. Atomicity means a transaction is all-or-nothing.
Consistency means a transaction takes the database from one valid state to another while respecting constraints.
Isolation controls how concurrent transactions interact so that intermediate states aren't improperly visible.
Durability means that once a transaction is committed, its changes survive failures such as a crash.

```textmate
A → All or nothing
C → Valid state → valid state
I → Concurrent transactions are isolated
D → Committed data survives
```

---

## 5. What is a transaction in a database? Give an example. What happens if a transaction fails halfway through?

A transaction is a sequence of one or more database operations that are treated as a single logical unit of work. The
transaction either completes successfully and is committed, or if it fails, its changes can be rolled back. Transactions
provide properties such as atomicity, consistency, isolation and durability.

---

## 6. What is the difference between COMMIT and ROLLBACK? What happens to the changes made during a transaction when each one is executed?

COMMIT permanently makes the changes performed by the current transaction visible and durable. ROLLBACK aborts the
transaction and undoes the changes made by that transaction, returning the database to its previous state.


---

## 7. Suppose two transactions execute concurrently. Transaction T1 reads a row, and before T1 reads it again, T2 modifies and commits that row. When T1 reads the row again, it gets a different value. What is this phenomenon called?

```textmate
T1                         T2
 |                          |
 | READ balance = 100       |
 |                          |
 |                     UPDATE balance = 200
 |                     COMMIT
 |                          |
 | READ balance             |
 | → 200                    |
```

This is a non-repeatable read. It occurs when a transaction reads the same row twice and gets different values because
another concurrent transaction modified and committed that row between the reads. This behaviour is controlled by
transaction isolation, not consistency

1. Dirty Read  : T1 reads data written by T2 before T2 commits.

```textmate
T1: READ 100
T2: UPDATE → 200
T1: READ 200  ← but T2 hasn't committed!
T2: ROLLBACK
```

2. Non-repeatable Read T1 reads a row → T2 changes and commits it → T1 reads same row again → different value.
3. Phantom Read : T2 inserts a new employee satisfying that condition and commits.T1 runs the same query again and gets
   an additional row. That new row is the "phantom".

---

## 9. What are the four standard SQL transaction isolation levels, from weakest to strongest? what anomalies each level allows/prevents

```textmate
Levels :
   READ UNCOMMITTED
         ↓
   READ COMMITTED
         ↓
   REPEATABLE READ
         ↓
   SERIALIZABLE
 
Anomalies :
   Dirty Read
   Non-repeatable Read
   Phantom Read
```

Important PostgreSQL nuance: PostgreSQL's REPEATABLE READ is stronger than the minimum standard definition and prevents
phantom reads under its MVCC implementation. PostgreSQL's READ UNCOMMITTED behaves like READ COMMITTED.

| Isolation level  | Dirty read | Non-repeatable read | Phantom read |
|------------------|------------|---------------------|--------------|
| READ UNCOMMITTED | Possible   | Possible            | Possible     |
| READ COMMITTED   | ❌          | Possible            | Possible     |
| REPEATABLE READ  | ❌          | ❌                   | Possible*    |
| SERIALIZABLE     | ❌          | ❌                   | ❌            |

Serializable : Make concurrent transactions behave as though they executed one after another. Strongest Isolation .


----

## 9. Why don't we simply use SERIALIZABLE isolation level for every transaction?

We don't use SERIALIZABLE for every transaction because it provides the strongest isolation but can reduce concurrency
and throughput. It can introduce more contention and may cause transactions to wait or be aborted and retried when
conflicts occur. Therefore, we usually choose an isolation level that provides sufficient correctness without
unnecessarily sacrificing performance.

---

## 10. PostgreSQL uses MVCC. What is MVCC, and why does PostgreSQL use it?

MVCC = Multi-Version Concurrency Control. PostgreSQL maintains multiple row versions so concurrent transactions can work
with appropriate snapshots instead of relying solely on blocking locks.

---

## 11. What is a deadlock in a database? Give an example of how two transactions can cause a deadlock, and how can a database handle it?

```textmate
T1                         T2
 |                          |
 | LOCK row A               |
 |                          | LOCK row B
 |                          |
 | wants row B ─────────→  waiting
 |                          |
 |             wants row A ─┘
 |                    waiting
```

A deadlock occurs when two or more transactions are waiting for locks held by each other, creating a circular
dependency. For example, T1 locks row A and waits for row B, while T2 locks row B and waits for row A. The database
detects the deadlock and typically aborts one of the transactions, releasing its locks so the other transaction can
proceed.

```textmate
T1 locks A
T2 locks B
   ↓
T1 waits for B
T2 waits for A
   ↓
Database detects cycle
   ↓
Abort T2
   ↓
T2 releases B
   ↓
T1 gets B
   ↓
T1 continues
```

how to prevent it :

```textmate
Do : Always Lock A before B
   T1: A → B
   T2: A → B

Don't :
   T1: A → B
   T2: B → A
```

---

## 12 . If PostgreSQL uses MVCC, why do we need locks at all?

MVCC and locks complement each other. MVCC allows PostgreSQL to provide concurrency by maintaining multiple row
versions, particularly allowing readers and writers to operate concurrently without unnecessary blocking. Locks are
still necessary to coordinate conflicting operations, especially writes and explicit locking requirements. Deadlocks are
a possible consequence of incompatible lock acquisition, not the purpose of locks.

---

## 13. What is a foreign key? Why do we use it? What happens if I try to insert a foreign-key value that doesn't exist in the referenced table?

A foreign key is a column or set of columns in one table that references a primary key or unique key in another table.
It is used to enforce referential integrity between related tables. If we try to insert or update a foreign-key value
that has no corresponding value in the referenced table, the database rejects the operation unless the constraint allows
that situation.

---

## 14. What are ON DELETE CASCADE, ON DELETE SET NULL, and ON DELETE RESTRICT used for?

```textmate
departments
-----------
id
1 | Engineering

employees
---------
id | name | department_id
10 | Bob  | 1

You Execute :
   DELETE FROM departments
   WHERE id = 1;
```

A foreign key can define what should happen to referencing rows when the referenced row is deleted. RESTRICT prevents
the parent from being deleted while dependent rows exist. CASCADE automatically deletes the dependent rows. SET NULL
keeps the dependent rows but sets their foreign-key column to NULL, provided the column allows NULL.

```textmate
RESTRICT
→ "Don't delete parent if children reference it."

CASCADE
→ "Delete the referencing children too."

SET NULL
→ "Keep the children, but remove their reference."
```

---

## 15. Can a foreign key contain duplicate values? Can a foreign key contain NULL?

Yes, a foreign key can contain duplicate values because multiple rows can reference the same parent row. A foreign key
can also contain NULL by default. The foreign-key constraint validates non-NULL values against the referenced key;
whether NULL is allowed is controlled separately by the NOT NULL constraint.

---

## 16. What is a composite primary key? Why would we need one? Give me a practical example.

A composite primary key is a primary key made up of multiple columns. We use it when no single column uniquely
identifies a row, but the combination of multiple columns does. For example, in an enrollment table, (student_id,
course_id) can uniquely identify a student's enrollment in a particular course.

```textmate
Enrollment
---------------------------
student_id | course_id
1          | 101
1          | 102
2          | 101
```

Primary Key : (student_id, course_id)/

---

## 17. What is a candidate key? How is it different from a primary key?

Candidate keys = all minimal keys that could uniquely identify a row.
Primary key = the candidate key we actually choose as the table's primary identifier.

A candidate key is a minimal set of attributes that can uniquely identify a row. A table can have multiple candidate
keys, but we choose one of them as the primary key. The remaining candidate keys are often called alternate keys.

---

## 18. What is a super key? How is it different from a candidate key?

```textmate
Employee
----------------
employee_id
email
name

Then these are super keys:: 
   {employee_id}             ✅
   {email}                   ✅
   {employee_id, email}      ✅
   {employee_id, name}       ✅
   {employee_id, email,name} ✅
```

Because if employee_id alone identifies the row, adding more columns doesn't stop it from uniquely identifying the row.
A candidate key is a minimal super key.

Super key = uniquely identifies.
Candidate key = uniquely identifies + nothing unnecessary.

A super key is any set of attributes that can uniquely identify a row. A candidate key is a minimal super key, meaning
none of its attributes can be removed while maintaining uniqueness. Therefore, every candidate key is a super key, but a
super key may contain unnecessary attributes


---

## 19. What is a functional dependency? Explain student_id → student_name in simple terms

A functional dependency means that one attribute or set of attributes uniquely determines another attribute. For
example, student_id → student_name means that for a given student ID, there can be only one corresponding student name.
The dependency is directional; the reverse is not necessarily true.


--- 

## 20. Is this table in 2NF? If not, why not?

```textmate
Enrollment
------------------------------------------------
student_id | course_id | student_name | course_name


Primary key:
(student_id, course_id)

student_id → student_name
course_id → course_name
```

Agar primary key composite hai, toh non-key column ko poori composite key par depend karna chahiye.
2NF = if the primary key is composite, every non-key attribute must depend on the whole key, not just part of it.

```textmate
Fix :
   Student
   ----------------
   student_id | student_name
   
   Course
   ----------------
   course_id | course_name
   
   Enrollment
   ----------------
   student_id | course_id -> both makes composite key
```

Partial dependency → 2NF violation

```textmate
                Composite Primary Key
              ┌───────────────────────┐
              │ student_id + course_id│
              └───────────────────────┘
                    ↓          ↓
                    ↓          ↓
              student_name  course_name
                    ↑             ↑
                    │             │
              only student_id  only course_id
```

---

## 21. Is this table in 3NF ?

```textmate
Employee
------------------------------------------
employee_id | department_id | department_name

Primary key:
   employee_id

Dependencies:
   employee_id → department_id
   department_id → department_name
```

Transitive dependency.

```textmate
employee_id
     ↓
department_id
     ↓
department_name
```

Satisfy 2NF first  : Non-key attributes should depend on the key, the whole key, and nothing but the key.
Fix :

```textmate
Employee:
employee_id → department_id

Department:
department_id → department_name
```

No non-key → non-key dependency within the same table.

Mental Model :
**1NF**
Cell ke andar ek value.

`❌ DBMS, OS
✅ DBMS
✅ OS`

**2NF**
Composite PK ho toh non-key attribute key ke sirf ek part par depend nahi karna chahiye.
`PK = (A,B)
A → X ❌`

**3NF**
Non-key attribute doosre non-key attribute par depend nahi karna chahiye.
`PK → A → B
A = non-key
B = non-key
❌`


----

## 21. Is this table 1NF, 2NF , 3NF or not give reasons.

```textmate
Student
----------------------------------
student_id | student_name | email

Primary key:
   student_id

And assume:
   student_id → student_name
   student_id → email
```

No repeating group / multi valued cells : 1NF

```textmate
101 | Rahul | rahul@gmail.com
102 | Aman  | aman@gmail.com
```

Single Primary key → 2NF
No non key dependency → 3NF

```
1NF
↓
Atomic values?

2NF
↓
Composite PK?
↓
Partial dependency?

3NF
↓
Non-key → non-key dependency?
```

--- 

## 22. What kind of index would you consider creating, and why? Also: does the order of columns in a composite index matter?

```textmate
SELECT *
FROM employees
WHERE department_id = 10
  AND salary > 80000;
```

Since this query frequently filters by department_id using equality and then by salary using a range condition, I would
consider creating a composite B-tree index on (department_id, salary). The order matters because B-tree multicolumn
indexes are most effective when the leading columns are constrained first; here department_id is the equality condition
and salary is the range condition. However, I would still verify the actual workload and query plan using EXPLAIN
ANALYZE before creating indexes in production.

---

## 23. What is the difference between a clustered index and a non-clustered index? And does PostgreSQL actually have a traditional clustered index?

A clustered index means the table's physical row organisation is aligned with the index ordering, which can be
beneficial for locality and range scans. A non-clustered or secondary index is a separate structure that points to the
table rows without determining their permanent physical order. PostgreSQL does not have a traditional permanently
maintained clustered index like some other databases. It has the CLUSTER command, which physically reorganises a table
according to an index, but that ordering is not automatically maintained after subsequent modifications.

---

## 24. But PostgreSQL chooses a Sequential Scan instead of using the index. Is PostgreSQL doing something wrong? Why might a sequential scan actually be faster?

```textmate
You have an index on employees(salary). You run
   SELECT *
   FROM employees
   WHERE salary > 1000;
```

No, PostgreSQL is not necessarily doing something wrong. The optimizer estimates the cost of different execution plans.
If the predicate matches a large portion of the table, an index scan may require fetching many table rows, potentially
causing expensive random I/O. A sequential scan can be cheaper because it reads the table sequentially. PostgreSQL
therefore chooses the plan with the lower estimated cost

----

## 25. Why might PostgreSQL still choose a Sequential Scan even though there is an index exactly on salary?

```textmate
CREATE INDEX idx_salary ON employees(salary);
SELECT *
FROM employees
WHERE salary = 80000;
```

Having an index does not guarantee that PostgreSQL will use it. The optimizer estimates the cost of different plans
using table statistics. If salary = 80000 matches a large percentage of rows, fetching those rows through the index may
be more expensive than a sequential scan. For a highly selective condition, however, the index is usually beneficial.

salary > 1000 → naturally may match many rows.
salary = 80000 → could be highly selective or could match many rows depending on the data distribution.
---

## 26. What does EXPLAIN do in PostgreSQL? And what is the difference between:

```textmate
EXPLAIN SELECT ...
     &
EXPLAIN ANALYZE SELECT ...
```

EXPLAIN shows PostgreSQL's estimated execution plan without executing the query. EXPLAIN ANALYZE actually executes the
query and reports the real execution statistics, such as actual time and actual rows, allowing us to compare estimates
with reality.

EXPLAIN ANALYZE modifies data if the query itself is an INSERT, UPDATE, or DELETE, because it actually runs the query.

---

## 27. You run EXPLAIN and PostgreSQL estimates 100 rows, but EXPLAIN ANALYZE shows that actually 100,000 rows were returned.

Why is this mismatch a problem, and what could cause it?

```textmate
SELECT *
FROM employees
WHERE department_id = 10;
```

A large difference between estimated and actual rows means the optimizer has inaccurate cardinality estimates. Since
PostgreSQL uses these estimates to choose an execution plan, a bad estimate can lead to a poor plan, such as choosing an
index scan when a sequential scan would be faster. Stale statistics are a common cause, so running ANALYZE can help.

```textmate
Planner thinks:
department_id = 10 → only 100 rows
                    ↓
              Index Scan looks cheap

Reality:
department_id = 10 → 100,000 rows
                    ↓
        Sequential Scan may have been better
```

---

## 28. What is a database view?

A view is a virtual table defined by a SQL query. A normal view does not store a separate copy of the query result; it
stores the query definition and presents its result as a table-like interface. A materialized view, on the other hand,
stores the query result physically and needs to be refreshed

```textmate
Normal View
→ stores definition
→ result generated when queried

Materialized View
→ stores result
→ must be refreshed to reflect changes
```

---

## 29. Will this update the original employees table? Why or why not?

```textmate
CREATE VIEW engineering_employees AS
SELECT id, name, salary
FROM employees
WHERE department_id = 10;

Now someone runs:

UPDATE engineering_employees
SET salary = salary * 1.10;
```

A normal view does not contain a separate copy of the data. It is a table-like interface over an underlying query. If
the view is updatable, an UPDATE issued against the view can modify the underlying base table, and subsequent queries on
the view reflect those changes.


---

## 30. What's the difference between DELETE, TRUNCATE, and DROP in SQL?

DELETE removes rows and can selectively remove them using a WHERE clause. TRUNCATE removes all rows while preserving the
table structure. DROP removes the table itself, including its definition and data

```textmate
DELETE    → rows
TRUNCATE  → all rows
DROP      → table/object itself
```

---

## 31. What's the difference between WHERE and HAVING? And specifically, why can't we normally use an aggregate condition like COUNT(*) > 5 in WHERE?

Working :

```textmate
employees
   ↓
WHERE salary > 70000     ← filter individual rows
   ↓
GROUP BY department_id   ← make groups
   ↓
COUNT(*)                 ← aggregate each group
   ↓
HAVING COUNT(*) > 5      ← filter groups
```

WHERE filters individual rows before grouping and aggregation. HAVING filters groups after GROUP BY and therefore can
use aggregate functions such as COUNT and AVG. COUNT(*) cannot normally be used in WHERE because aggregation has not
happened yet at the WHERE stage.

```textmate
COUNT(*)       -- counts every row
COUNT(salary)  -- counts only rows where salary IS NOT NULL
```

---

## 32. In what logical order does SQL process these clauses? Tell me the order of: SELECT, FROM, WHERE, GROUP BY, HAVING.

```textmate
SELECT department_id, COUNT(*)
FROM employees
WHERE salary > 50000
GROUP BY department_id
HAVING COUNT(*) > 5;
```

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

1. FROM
   Get rows from employees.

2. WHERE
   Remove employees whose salary isn't > 50000.

3. GROUP BY
   Put the remaining employees into department groups.

4. HAVING
   Keep only departments whose group has more than 5 employees.

5. SELECT
   Finally produce department_id and COUNT(*).

That's also why: WHERE COUNT(*) > 5 doesn't work: COUNT hasn't been calculated when WHERE is evaluated.

---

## 33. Why this query fails ?

```textmate
SELECT department_id, COUNT(*)
FROM employees
GROUP BY department_id
WHERE salary > 50000;
```

Correct : Where comes before grouping.

```textmate
SELECT department_id, COUNT(*)
FROM employees
WHERE salary > 50000
GROUP BY department_id;
```

---

## 35. What's the difference between these two?

```textmate
COUNT(*)

and

COUNT(salary)
```

Answer :

```textmate
COUNT(*)       → counts every row
COUNT(salary)  → counts only rows where salary IS NOT NULL
```

---

## 36. And if 100 employees belong to only 5 different departments, what will each return?

```textmate
COUNT(*) 
    &
COUNT(DISTINCT department_id)
```

COUNT(*) → 100 because there are 100 rows/employees.
COUNT(DISTINCT department_id) → 5 because only 5 unique department IDs exist.

---

## 37. What's the difference between UNION and UNION ALL?

UNION combines the result sets of two queries and removes duplicate rows, whereas UNION ALL combines them while
preserving duplicates. UNION ALL is generally faster because it doesn't need duplicate elimination.

---

## 38. What are the requirements for two queries to be combined using UNION?

```textmate
SELECT id, name FROM employees
UNION
SELECT id FROM departments;
```

No it cannot..

```textmate
UNION
  ↓
same number of columns
  +
compatible data types
  +
results vertically combine
```

---

# Concept :

**Inner Join**

```textmate
employees              departments
---------              -----------
A → IT                 IT
B → HR                 HR
C → NULL

SELECT *
FROM employees e
INNER JOIN departments d
    ON e.department_id = d.id;
```

No C because only gives matching rows.

```
INNER JOIN
     ↓
Only common/matching rows
```

INNER JOIN returns only rows where the join condition matches in both tables. LEFT JOIN returns all rows from the left
table and matching rows from the right table; if there is no match, the right-side columns contain NULL
```
Result :
   A → IT
   B → HR
   C → NULL


LEFT JOIN
    ↓
Left table ki EVERY row
    +
Matching right-table data
    +
No match → NULL
```
```textmate
Important :
A LEFT JOIN B

→ Keep ALL rows of A
→ Bring matching rows from B
→ No match in B = NULL
```
```textmate
A RIGHT JOIN B
       ≡
B LEFT JOIN A
```