## Introduction
<br>
The aim of this project is to build a tool that enables interactive visual exploration of Query Execution Plans (QEP) for SQL queries. We will design and implement an algorithm that takes as input a SQL query, and its execution plan, and correlate it using colour codes and visual exploration. There will be a Graphical User Interface (GUI) for the entire application.

## Tech Stack 
1.psycopg2<br>
2.Tkinter <br>
3. PyQt <br>
4. PostgreSQL <br>

## Algorithm 
To generate the QEP for the specified query, we will make use of the ‘EXPLAIN’ function in PostgreSQL. The ‘EXPLAIN’ function will return the query plan which the planner creates for a query.


## Query Interface

![PyQt User Interface](https://github.com/programmer1997/Query-Execution-Plan-Visualizer/blob/master/Screenshot%202019-05-04%20at%204.16.27%20PM.png)





## Example
![Tkinter Query Execution Plan](https://github.com/programmer1997/Query-Execution-Plan-Visualizer/blob/master/Screenshot%202019-05-04%20at%204.17.43%20PM.png)
