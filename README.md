# simple-mfs-ussd-java

This is a simple implementation of a USSD Web API for an MFS using Java SE. I tried to use as few frameworks I could to improve my understanding of the language. 

## Data Storage
All menus, response messages, request parameters and customer data for the financial service are stored in a database on the server-side. DDL files for a dummy database are provided in the repository.

## Using the API
App URL: `tomcat_server/WAR_context/ussd?initiator=customer_id&input=user_input` 

`input=` (no input) indicates login. The application must start with login.

Examples:
1. Login: `http://localhost:6810/myapi/ussd?initiator=111&input=`
2. Next input: `http://localhost:6810/myapi/ussd?initiator=111&input=2`

To reload cache while the server is running, </br>
Cache reload URL: `tomcat_server/WAR_context/reload`

## Adding and maintaining Services
### 1. Create a service class
Every feature of the application is treated as a service. To add a new service, simply create a class for that service and inherit members from the `ServiceController` class. The new class must implement the following methods:
- void initialiseFromLog()
  - fetches data required to run the service from the session log table in the database.
- boolean isAllowed(PrintWriter out)
  - contains data validation checks
- void execute()
  - main process for the service
- void sendSuccessMessage(HttpServletResponse resp, PrintWriter out)
  - sends a response for the service, like 'Transaction Successful', a mini-statement for the account, etc.
 
### 2. Add to the Database
The menus leading to the service must be added in the `MENU_ROUTES` and `RESPONSES` tables. The regular expressions for the expected input at every step has to be added to the `MENU_REGEX` table. Finally, to dynamically load the class, add the class name (including its package) to the `SERVICE_CLASSES` table.

## Caching
In-memory caching is used. On servlet initialisation, the `CacheLoader` class fetches all static and rarely updated tables from the database and stores them as `HashMap` objects in memory. `CacheLoader` is a singleton class so whenever a cached information is required, the same objects are called from its running instance. 

To update caches while the server is running, hit the URL for the `CacheReloader` servlet. 

## Features
Services for personal accounts have been implemented. The application provides:
1. Cash Out
2. Send Money
3. Mobile Recharge
   - integrated Telco APIs that send dummy responses
   - needs [dummy-telco-server-for-simple-ussd](https://github.com/sadpotat/dummy-telco-server-for-simple-ussd.git) to be deployed on another port to function
4. Payment
5. Bill Pay
6. EMI payment
7. Account Balance Check
8. Mini Statement
9. Change PIN

Services for other account types will be implemented later.

# Dependencies:
- JDK 11 or later versions
- Oracle Database
- Oracle JDBC - [Ojdbc 19.3.0.0](https://mvnrepository.com/artifact/com.oracle.database.jdbc/ojdbc8/19.3.0.0)
- [Java Servlet API 4.0.1](https://mvnrepository.com/artifact/javax.servlet/javax.servlet-api/4.0.1)
- Apache Maven 3.9.2 or later versions
- Apache Tomcat 9


