# Getting Started


## Prerequisites

- Preferably JDK 1.8
- Maven
- Git


## Get the Application Running

Pull the project using the following command in your desired directory
```bash
git pull "https://github.com/prateeknima/LogRecordsProcessor"

Note: You need to first initialize your repository using 'git init'
```
Go into the application directory and use the following commands to run the application

```python
- mvn spring-boot:run -Dspring-boot.run.arguments="'Path to the input file'"
 
The path to the file which needs to analysed must be entered between the single quotes 
```
You can run the test cases using
```python
- mvn clean test

 The test cases are written which considers an valid input to the program as well as invalid input i.e. when there is no file present at the path specified
```

## Additional Details
- The program uses executor service for multithreading. The thread pool size has been set to maximum based on the processor being used. This can be adjusted based on scenarios such as if you have a database with 100 threads in its thread pool there is no point in giving additional threads to the application since it would anyways be a waste.
- Logs have been provided at appropriate places to understand the application flow and to convey events such as when exception occurs.
- The data is stored in the HSQLDB database and the properties of which are set in the application.properties file.
- Test cases are appropriately written with a test coverage of 100 percent.
