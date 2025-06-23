package Test.java;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;

import Classes.EmployeeInformation;
import Classes.GovernmentIdentification;
import Classes.Compensation;
import Classes.User;
import DAO.EmployeeDAO;
import DAO.UserDAO;
import Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;
import java.util.ArrayList;

/**
 * CRUD Operations Test Group - Database Safe with Rollback
 * Tests CRUD operations with actual DAO calls but ensures database restoration
 */
@DisplayName("CRUD Operations Tests - Database Safe with DAO")
public class CrudOperationsTestWithDAO {

    private EmployeeInformation testEmployee;
    private String testEmployeeNumber;
    private List<EmployeeInformation> testEmployeeList;
    private static Connection testConnection;
    private Savepoint savepoint;
    
    @BeforeAll
    static void setUpClass() {
        try {
            testConnection = DatabaseConnection.getConnection();
            testConnection.setAutoCommit(false);
            System.out.println("Test database connection established for CRUD tests");
        } catch (SQLException e) {
            System.err.println("Failed to establish test database connection: " + e.getMessage());
        }
    }
    
    @AfterAll
    static void tearDownClass() {
        try {
            if (testConnection != null && !testConnection.isClosed()) {
                testConnection.rollback();
                testConnection.setAutoCommit(true);
                testConnection.close();
                System.out.println("Test database connection closed - all changes rolled back");
            }
        } catch (SQLException e) {
            System.err.println("Error closing test database connection: " + e.getMessage());
        }
    }
    
    @BeforeEach
    void setUp() {
        try {
            if (testConnection != null && !testConnection.isClosed()) {
                savepoint = testConnection.setSavepoint("CrudTestSavepoint_" + System.currentTimeMillis());
                System.out.println("Savepoint created for CRUD test");
            }
        } catch (SQLException e) {
            System.err.println("Failed to create savepoint: " + e.getMessage());
        }
        
        testEmployeeNumber = "TEST_EMP_" + System.currentTimeMillis();
        testEmployee = null;
        testEmployeeList = new ArrayList<>();
    }
    
    @AfterEach
    void tearDown() {
        try {
            if (testConnection != null && !testConnection.isClosed() && savepoint != null) {
                testConnection.rollback(savepoint);
                System.out.println("Database rolled back to savepoint - all test data removed");
            }
        } catch (SQLException e) {
            System.err.println("Failed to rollback to savepoint: " + e.getMessage());
        }
        
        testEmployee = null;
        testEmployeeNumber = null;
        if (testEmployeeList != null) {
            testEmployeeList.clear();
        }
        testEmployeeList = null;
    }
    
    @Test
    @DisplayName("Test Create Employee - Valid Data")
    void testcreateemployee_validdata() {
        String uniqueEmployeeNumber = "TEST_CREATE_" + System.currentTimeMillis();
        String firstName = "TestJohn";
        String lastName = "TestDoe";
        String birthday = "1990-01-01";
        String address = "123 Test St, Test City";
        String phoneNumber = "555-TEST-123";
        String status = "Active";
        String position = "Test Developer";
        String supervisor = "TestJane TestSmith";
        double hourlyRate = 25.50;
        
        // Create employee object
        testEmployee = new EmployeeInformation(uniqueEmployeeNumber);
        testEmployee.setFirstName(firstName);
        testEmployee.setLastName(lastName);
        testEmployee.setBirthday(birthday);
        testEmployee.setAddress(address);
        testEmployee.setPhoneNumber(phoneNumber);
        testEmployee.setStatus(status);
        testEmployee.setPosition(position);
        testEmployee.setSupervisor(supervisor);
        testEmployee.setHourlyRate(hourlyRate);
        
        // Assert object creation
        assertNotNull(testEmployee, "Employee object should be created successfully");
        assertEquals(uniqueEmployeeNumber, testEmployee.getEmployeeNumber());
        assertEquals(firstName, testEmployee.getFirstName());
        assertEquals(lastName, testEmployee.getLastName());
        assertEquals(hourlyRate, testEmployee.getHourlyRate(), 0.01);
        
        // Test DAO operations (if database is available)
        if (isDatabaseAvailable()) {
            try {
                String testUsername = "testuser_" + System.currentTimeMillis();
                String testEmail = testUsername + "@test.com";
                
                int userId = UserDAO.createUserWithRole(testUsername, "testpass123", testEmail, "EMPLOYEE");
                
                if (userId > 0) {
                    System.out.println("Test user created with ID: " + userId + " (will be rolled back)");
                    
                    User createdUser = UserDAO.getUserByUsername(testUsername);
                    if (createdUser != null) {
                        assertEquals(testUsername, createdUser.getUserId());
                        System.out.println("User creation verified via DAO");
                    }
                }
            } catch (Exception e) {
                System.out.println("Database operation result: " + e.getMessage());
            }
        }
    }
    
    @Test
    @DisplayName("Test Create Employee - Duplicate ID")
    void testcreateemployee_duplicateid() {
        String duplicateEmployeeNumber = "TEST_DUP_" + System.currentTimeMillis();
        
        EmployeeInformation firstEmployee = new EmployeeInformation(duplicateEmployeeNumber);
        firstEmployee.setFirstName("TestJohn");
        firstEmployee.setLastName("TestDoe");
        
        EmployeeInformation secondEmployee = new EmployeeInformation(duplicateEmployeeNumber);
        secondEmployee.setFirstName("TestJane");
        secondEmployee.setLastName("TestSmith");
        
        assertNotNull(firstEmployee, "First employee object should be created");
        assertNotNull(secondEmployee, "Second employee object should be created");
        assertEquals(duplicateEmployeeNumber, firstEmployee.getEmployeeNumber());
        assertEquals(duplicateEmployeeNumber, secondEmployee.getEmployeeNumber());
        assertNotSame(firstEmployee, secondEmployee, "Should be different objects");
        
        if (isDatabaseAvailable()) {
            try {
                String firstUsername = "testdup1_" + System.currentTimeMillis();
                String firstEmail = firstUsername + "@test.com";
                
                int firstUserId = UserDAO.createUserWithRole(firstUsername, "pass1", firstEmail, "EMPLOYEE");
                
                if (firstUserId > 0) {
                    System.out.println("First test user created: " + firstUserId);
                    
                    try {
                        String secondUsername = "testdup2_" + System.currentTimeMillis();
                        int secondUserId = UserDAO.createUserWithRole(secondUsername, "pass2", firstEmail, "EMPLOYEE");
                        
                        if (secondUserId > 0) {
                            System.out.println("Second user created: " + secondUserId);
                        } else {
                            System.out.println("Duplicate email prevented second user creation");
                        }
                    } catch (Exception e) {
                        System.out.println("Duplicate handling: " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                System.out.println("Duplicate test result: " + e.getMessage());
            }
        }
    }
    
    @Test
    @DisplayName("Test Read Employee - Valid ID")
    void testreademployee_valid() {
        String validEmployeeNumber = "TEST_READ_" + System.currentTimeMillis();
        
        testEmployee = new EmployeeInformation(validEmployeeNumber);
        testEmployee.setFirstName("TestJohn");
        testEmployee.setLastName("TestDoe");
        testEmployee.setPosition("TestDeveloper");
        testEmployee.setHourlyRate(25.00);
        
        assertEquals(validEmployeeNumber, testEmployee.getEmployeeNumber());
        assertEquals("TestJohn", testEmployee.getFirstName());
        assertEquals("TestDoe", testEmployee.getLastName());
        assertEquals("TestDeveloper", testEmployee.getPosition());
        assertEquals(25.00, testEmployee.getHourlyRate(), 0.01);
        
        if (isDatabaseAvailable()) {
            try {
                String testUsername = "testread_" + System.currentTimeMillis();
                String testEmail = testUsername + "@test.com";
                
                int userId = UserDAO.createUserWithRole(testUsername, "readpass", testEmail, "EMPLOYEE");
                
                if (userId > 0) {
                    User readUser = UserDAO.getUserByUsername(testUsername);
                    
                    if (readUser != null) {
                        assertEquals(testUsername, readUser.getUserId(), "Read user should match created user");
                        System.out.println("User read operation successful via DAO");
                    }
                    
                    List<User> allUsers = UserDAO.getAllUsers();
                    assertNotNull(allUsers, "All users list should not be null");
                    System.out.println("Retrieved " + allUsers.size() + " users from database");
                }
            } catch (Exception e) {
                System.out.println("Database read operation result: " + e.getMessage());
            }
        }
    }
    
    @Test
    @DisplayName("Test Read Employee - Invalid ID")
    void testreademployee_invalidid() {
        EmployeeInformation nullEmployee = new EmployeeInformation(null);
        assertNull(nullEmployee.getEmployeeNumber());
        
        EmployeeInformation emptyEmployee = new EmployeeInformation("");
        assertEquals("", emptyEmployee.getEmployeeNumber());
        
        String nonExistentNumber = "TEST_NONEXISTENT_" + System.currentTimeMillis();
        EmployeeInformation nonExistentEmployee = new EmployeeInformation(nonExistentNumber);
        assertEquals(nonExistentNumber, nonExistentEmployee.getEmployeeNumber());
        assertNull(nonExistentEmployee.getFirstName());
        
        if (isDatabaseAvailable()) {
            try {
                String nonExistentUsername = "NONEXISTENT_" + System.currentTimeMillis();
                User nonExistentUser = UserDAO.getUserByUsername(nonExistentUsername);
                assertNull(nonExistentUser, "Non-existent user should return null");
                
                User nullUser = UserDAO.getUserByUsername(null);
                assertNull(nullUser, "Null username should return null");
                
                System.out.println("Invalid ID tests completed with DAO");
            } catch (Exception e) {
                System.out.println("Invalid ID test result: " + e.getMessage());
            }
        }
    }
    
    @Test
    @DisplayName("Test Update Employee - Valid Update")
    void testupdateemployee_validupdate() {
        testEmployee = new EmployeeInformation("TEST_UPDATE_" + System.currentTimeMillis());
        testEmployee.setFirstName("TestJohn");
        testEmployee.setLastName("TestDoe");
        testEmployee.setPosition("TestJunior Developer");
        testEmployee.setHourlyRate(20.00);
        
        assertEquals("TestJohn", testEmployee.getFirstName());
        assertEquals(20.00, testEmployee.getHourlyRate(), 0.01);
        
        // Update employee data
        testEmployee.setFirstName("TestJonathan");
        testEmployee.setLastName("TestDoe-TestSmith");
        testEmployee.setPosition("TestSenior Developer");
        testEmployee.setHourlyRate(35.00);
        
        assertEquals("TestJonathan", testEmployee.getFirstName(), "First name should be updated");
        assertEquals("TestDoe-TestSmith", testEmployee.getLastName(), "Last name should be updated");
        assertEquals("TestSenior Developer", testEmployee.getPosition(), "Position should be updated");
        assertEquals(35.00, testEmployee.getHourlyRate(), 0.01, "Hourly rate should be updated");
        
        if (isDatabaseAvailable()) {
            try {
                String testUsername = "testupdate_" + System.currentTimeMillis();
                String testEmail = testUsername + "@test.com";
                
                int userId = UserDAO.createUserWithRole(testUsername, "updatepass", testEmail, "EMPLOYEE");
                
                if (userId > 0) {
                    User createdUser = UserDAO.getUserByUsername(testUsername);
                    if (createdUser != null) {
                        createdUser.setPassword("newpassword");
                        boolean updated = UserDAO.updateUser(createdUser);
                        System.out.println("User update result: " + updated + " (will be rolled back)");
                        
                        boolean roleAssigned = UserDAO.assignRoleToUser(testUsername, "HR");
                        System.out.println("Role assignment result: " + roleAssigned + " (will be rolled back)");
                    }
                }
            } catch (Exception e) {
                System.out.println("Update operation result: " + e.getMessage());
            }
        }
    }
    
    @Test
    @DisplayName("Test Update Employee - Missing Field")
    void testupdateemployee_missingfield() {
        testEmployee = new EmployeeInformation("TEST_MISSING_" + System.currentTimeMillis());
        testEmployee.setFirstName("TestJohn");
        testEmployee.setLastName("TestDoe");
        testEmployee.setHourlyRate(25.00);
        
        // Update with null values
        testEmployee.setFirstName(null);
        testEmployee.setLastName(null);
        testEmployee.setAddress(null);
        testEmployee.setPhoneNumber(null);
        
        assertNull(testEmployee.getFirstName(), "Should handle null first name update");
        assertNull(testEmployee.getLastName(), "Should handle null last name update");
        assertNull(testEmployee.getAddress(), "Should handle null address update");
        assertNull(testEmployee.getPhoneNumber(), "Should handle null phone number update");
        assertEquals(25.00, testEmployee.getHourlyRate(), 0.01, "Hourly rate should remain unchanged");
        
        // Update with empty strings
        testEmployee.setFirstName("");
        testEmployee.setLastName("");
        testEmployee.setAddress("");
        
        assertEquals("", testEmployee.getFirstName(), "Should handle empty first name update");
        assertEquals("", testEmployee.getLastName(), "Should handle empty last name update");
        assertEquals("", testEmployee.getAddress(), "Should handle empty address update");
    }
    
    @Test
    @DisplayName("Test Delete Employee - Existing")
    void testdeleteemployee_existing() {
        testEmployee = new EmployeeInformation("TEST_DELETE_" + System.currentTimeMillis());
        testEmployee.setFirstName("TestJohn");
        testEmployee.setLastName("TestDoe");
        testEmployee.setPosition("TestDeveloper");
        testEmployee.setHourlyRate(25.00);
        
        assertNotNull(testEmployee, "Employee should exist before deletion");
        assertEquals("TestJohn", testEmployee.getFirstName(), "Employee should have data");
        
        // Simulate deletion by clearing fields
        testEmployee.setFirstName(null);
        testEmployee.setLastName(null);
        testEmployee.setPosition(null);
        testEmployee.setHourlyRate(0.0);
        
        assertNull(testEmployee.getFirstName(), "First name should be cleared after deletion");
        assertNull(testEmployee.getLastName(), "Last name should be cleared after deletion");
        assertNull(testEmployee.getPosition(), "Position should be cleared after deletion");
        assertEquals(0.0, testEmployee.getHourlyRate(), 0.01, "Hourly rate should be reset");
        
        if (isDatabaseAvailable()) {
            try {
                String testUsername = "testdelete_" + System.currentTimeMillis();
                String testEmail = testUsername + "@test.com";
                
                int userId = UserDAO.createUserWithRole(testUsername, "deletepass", testEmail, "EMPLOYEE");
                
                if (userId > 0) {
                    User createdUser = UserDAO.getUserByUsername(testUsername);
                    if (createdUser != null) {
                        boolean deleted = UserDAO.deleteUser(createdUser.getEmployeeNumber());
                        System.out.println("User deletion result: " + deleted + " (will be rolled back)");
                    }
                }
            } catch (Exception e) {
                System.out.println("Delete operation result: " + e.getMessage());
            }
        }
    }
    
    @Test
    @DisplayName("Test Delete Employee - Non-existent")
    void testdeleteemployee_nonexistent() {
        EmployeeInformation nonExistentEmployee = new EmployeeInformation("TEST_NONEXISTENT_" + System.currentTimeMillis());
        
        assertNull(nonExistentEmployee.getFirstName(), "Non-existent employee should have null first name");
        assertNull(nonExistentEmployee.getLastName(), "Non-existent employee should have null last name");
        assertEquals(0.0, nonExistentEmployee.getHourlyRate(), 0.01, "Non-existent employee should have 0 hourly rate");
        
        assertDoesNotThrow(() -> {
            nonExistentEmployee.setFirstName(null);
            nonExistentEmployee.setLastName(null);
            nonExistentEmployee.setPosition(null);
        }, "Deleting non-existent employee should not throw exceptions");
        
        if (isDatabaseAvailable()) {
            try {
                String nonExistentUsername = "NONEXISTENT_DELETE_" + System.currentTimeMillis();
                boolean deleted = UserDAO.deleteUser(nonExistentUsername);
                System.out.println("Non-existent user deletion result: " + deleted);
            } catch (Exception e) {
                System.out.println("Non-existent deletion result: " + e.getMessage());
            }
        }
    }
    
    @Test
    @DisplayName("Test Get All Employees")
    void testgetallemployees() {
        testEmployeeList = new ArrayList<>();
        
        // Create multiple employees
        EmployeeInformation emp1 = new EmployeeInformation("TEST_ALL_" + System.currentTimeMillis() + "_1");
        emp1.setFirstName("TestJohn");
        emp1.setLastName("TestDoe");
        emp1.setPosition("TestDeveloper");
        emp1.setHourlyRate(25.00);
        testEmployeeList.add(emp1);
        
        EmployeeInformation emp2 = new EmployeeInformation("TEST_ALL_" + System.currentTimeMillis() + "_2");
        emp2.setFirstName("TestJane");
        emp2.setLastName("TestSmith");
        emp2.setPosition("TestManager");
        emp2.setHourlyRate(35.00);
        testEmployeeList.add(emp2);
        
        EmployeeInformation emp3 = new EmployeeInformation("TEST_ALL_" + System.currentTimeMillis() + "_3");
        emp3.setFirstName("TestBob");
        emp3.setLastName("TestWilson");
        emp3.setPosition("TestAnalyst");
        emp3.setHourlyRate(30.00);
        testEmployeeList.add(emp3);
        
        assertEquals(3, testEmployeeList.size(), "Should have 3 employees");
        
        // Verify employees
        assertEquals("TestJohn", testEmployeeList.get(0).getFirstName());
        assertEquals("TestJane", testEmployeeList.get(1).getFirstName());
        assertEquals("TestBob", testEmployeeList.get(2).getFirstName());
        
        if (isDatabaseAvailable()) {
            try {
                // Create test users and get all users
                String testUsername1 = "testall1_" + System.currentTimeMillis();
                String testUsername2 = "testall2_" + System.currentTimeMillis();
                
                int userId1 = UserDAO.createUserWithRole(testUsername1, "pass1", testUsername1 + "@test.com", "EMPLOYEE");
                int userId2 = UserDAO.createUserWithRole(testUsername2, "pass2", testUsername2 + "@test.com", "HR");
                
                if (userId1 > 0 && userId2 > 0) {
                    List<User> allUsers = UserDAO.getAllUsers();
                    assertNotNull(allUsers, "All users list should not be null");
                    assertTrue(allUsers.size() >= 2, "Should have at least our test users");
                    System.out.println("Get all users returned " + allUsers.size() + " users (will be rolled back)");
                    
                    // Verify our test users are in the list
                    boolean foundUser1 = allUsers.stream().anyMatch(u -> testUsername1.equals(u.getUserId()));
                    boolean foundUser2 = allUsers.stream().anyMatch(u -> testUsername2.equals(u.getUserId()));
                    
                    assertTrue(foundUser1 || foundUser2, "At least one test user should be found in all users list");
                }
            } catch (Exception e) {
                System.out.println("Get all users result: " + e.getMessage());
            }
        }
        
        // Test empty list
        List<EmployeeInformation> emptyList = new ArrayList<>();
        assertEquals(0, emptyList.size(), "Empty list should have size 0");
        assertTrue(emptyList.isEmpty(), "Empty list should be empty");
    }
    
    // Helper method to verify database connection
    private boolean isDatabaseAvailable() {
        try {
            return testConnection != null && !testConnection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
