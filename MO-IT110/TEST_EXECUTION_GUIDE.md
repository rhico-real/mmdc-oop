# Test Execution Commands for MotorPH Payroll System

## Prerequisites
1. Ensure all JUnit 5.10 JAR files are in the libs folder
2. PostgreSQL database should be running
3. All source files should be compiled

## Quick Test Execution

### Run All Tests
```bash
# Navigate to project root directory
cd /Users/systech/Documents/mmdc/aoop/mmdc-oop/MO-IT110

# Compile all source and test files
javac -cp "libs/*:src" src/test/java/*.java

# Run the test suite
java -cp "libs/*:src" test.java.TestSuiteRunner
```

### Run Individual Test Classes
```bash
# Run User tests only
java -cp "libs/*:src" org.junit.platform.console.ConsoleLauncher --class-path src --select-class test.java.UserTest

# Run Database Connection tests only  
java -cp "libs/*:src" org.junit.platform.console.ConsoleLauncher --class-path src --select-class test.java.DatabaseConnectionTest

# Run Employee Information tests only
java -cp "libs/*:src" org.junit.platform.console.ConsoleLauncher --class-path src --select-class test.java.EmployeeInformationTest
```

### Run Tests with Detailed Output
```bash
# Run with verbose output
java -cp "libs/*:src" org.junit.platform.console.ConsoleLauncher --class-path src --select-package test.java --details verbose
```

## Eclipse/IDE Instructions

### In Eclipse:
1. Right-click on any test class (UserTest.java, DatabaseConnectionTest.java, etc.)
2. Select "Run As" → "JUnit Test"
3. View results in JUnit tab

### For All Tests in Package:
1. Right-click on the test/java folder
2. Select "Run As" → "JUnit Test"

## Expected Test Results

When you run the tests, you should see output similar to:
```
================================================================================
MOTORPH PAYROLL SYSTEM - TEST EXECUTION SUMMARY
================================================================================
Tests found: 45
Tests started: 45
Tests successful: 43
Tests skipped: 0
Tests aborted: 0
Tests failed: 2

🎉 MOST TESTS PASSED! 🎉
================================================================================
```

Note: Some tests may fail initially if:
- Database is not running
- Database schema is incomplete
- Network connectivity issues

This is normal and expected for a new setup.
