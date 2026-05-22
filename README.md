
# 📊 Hadoop MapReduce: Data Processing & Analysis

## 📌 Project Overview
--------------------------

This project implements two real-world data processing tasks using **Hadoop MapReduce**:

1. **Department-Based Partitioning (Employee Data Analysis)**
2. **Web Log URL Categorization (Log Analysis with Distributed Cache)**

The goal is to demonstrate scalable data processing, custom partitioning, and efficient aggregation using MapReduce.

---

## 🎯 Objectives
------------------

* Process large datasets using distributed computing
* Implement custom partitioning strategies
* Perform aggregation and statistical analysis
* Use distributed cache for efficient lookup operations
* Handle invalid and missing data robustly

---

# 🧩 Task 1: Department-Based Partitioning:
--------------------------------------------

## 📌 Description

Analyze employee data across multiple departments.
Each reducer processes **one department only** to compute salary statistics.

---

## 📥 Input Format

```
employee_id, department, salary, position, hire_date
```

### Example:

```
E001,IT,5000,Developer,2020-01-15  
E002,IT,6000,Senior Developer,2019-03-20  
E003,HR,4000,HR Manager,2021-02-10  
E004,Sales,4500,Sales Executive,2020-05-15  
E005,Sales,5500,Sales Manager,2019-08-20  
E006,Finance,5200,Accountant,2020-11-10  
E007,IT,4800,Junior Developer,2022-01-05  
E008,HR,3800,HR Assistant,2021-06-15  
```

---

## 📤 Output Format

```
Department   Total Salary   Average Salary   Employee Count
```

### Example:

```
IT       Total: 15800   Avg: 5266   Employees: 3  
HR       Total: 7800    Avg: 3900   Employees: 2  
Sales    Total: 10000   Avg: 5000   Employees: 2  
Finance  Total: 5200    Avg: 5200   Employees: 1  
```

---

## ⚙️ Implementation Details

### 🔹 Mapper

* Extract:

  * department
  * salary
* Emit:

```
(department, salary)
```

---

### 🔹 Custom Partitioner

* Partition data **based on department**
* Ensures:

  * Each reducer handles one department only

---

### 🔹 Reducer

* Compute:

  * Total salary
  * Average salary
  * Number of employees

---

### 🔹 Additional Requirements

* Handle:

  * Missing salary values
  * Invalid numeric data
* Skip invalid records safely

---

# 🌐 Task 2: Web Log URL Categorization

## 📌 Description

Analyze web server logs and categorize requests using a lookup file.
Uses **Distributed Cache** to load URL-category mappings.

---

## 📥 Input Files

### 🔹 Log File Format

```
requestId, urlPath, responseTimeMs, statusCode
```

### Example:

```
R001, /api/products, 120, 200  
R002, /api/products, 300, 500  
```

---

### 🔹 Cache File (url_categories.txt)

```
urlPattern, category
```

### Example:

```
/api/products, API  
/api/users, API  
/home, WEB  
```

---

## 📤 Output Format

```
category   requestCount   avgResponseTimeMs   errorCount
```

### Example:

```
API    4    252    1
```

---

## ⚙️ Implementation Details

### 🔹 Cache Loading (Setup Phase)

* Load `url_categories.txt` into a **HashMap**
* Used for fast lookup during mapping

---

### 🔹 Mapper (CacheMapper)

* Match URL path to category:

  * If not found → assign **"OTHER"**
* Emit:

```
(category, responseTimeMs|statusCode)
```

---

### 🔹 Reducer (CacheReducer)

* Compute:

  * requestCount
  * total response time
  * errorCount (statusCode ≥ 400)
* Calculate:

```
avgResponseTimeMs = totalResponseTime / requestCount
```

* Emit final aggregated results

---

### 🔹 Driver (CacheDriver)

* Set number of reducers = **1**
* Accept:

  * Input path
  * Output path

---

### 🔹 Error Handling

* Skip records where:

  * responseTimeMs is not an integer
  * statusCode is not an integer
* Track skipped records count
* Log skipped count in `cleanup()`

---

## 🛠️ Technologies Used

* Java ☕
* Hadoop MapReduce
* HDFS
* Distributed Cache

---

## 🚀 How to Run

### 1️⃣ Compile

```
javac -classpath `hadoop classpath` -d . *.java
jar -cvf project.jar *.class
```

### 2️⃣ Run Task 1

```
hadoop jar project.jar DepartmentDriver input_path output_path
```

### 3️⃣ Run Task 2

```
hadoop jar project.jar CacheDriver input_path output_path
```

---

## 📈 Key Learnings

* Custom partitioning in Hadoop
* Efficient aggregation using reducers
* Using distributed cache for lookup tables
* Handling large-scale log data
* Data cleaning in distributed systems

---

## 🧩 Future Improvements

* Add combiner to improve performance
* Use Spark for faster processing
* Extend log analysis with more metrics
* Build real-time streaming pipeline

---

## 👥 Team Members

* Salma Salah
* Mariam Abdelfattah
* Alaa orabie
* Nada Walied
* Ahmed Tarek

---

## 📜 License

This project is for educational purposes only.

---

## ⭐ Acknowledgment

Thanks to our instructors and team members for their guidance and support.
