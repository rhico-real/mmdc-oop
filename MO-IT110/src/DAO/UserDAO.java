package DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import Classes.User;
import Database.DatabaseConnection;

public class UserDAO {
    
    /**
     * Enhanced user authentication with role checking
     * @param username Username to check
     * @param password Password to verify
     * @return User object with roles if authentication successful, null otherwise
     */
    public static User authenticateUser(String username, String password) {
        // Using the stored procedure sp_authenticate_user
        String sql = "SELECT * FROM sp_authenticate_user(?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(rs.getString("employee_number"));
                    user.setUserId(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setLoginStatus(true);
                    user.setIsVerified(true);
                    
                    // Set roles based on new structure
                    String roles = rs.getString("roles");
                    if (roles != null) {
                        user.setIsAdmin(roles.contains("ADMIN"));
                        user.setIsHR(roles.contains("HR"));
                        user.setIsFinance(roles.contains("FINANCE"));
                    }
                    
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Create a new user with role assignment
     * @param username Username
     * @param password Password
     * @param email Email address
     * @param roleName Role name (ADMIN, HR, EMPLOYEE)
     * @return User ID if creation successful, -1 otherwise
     */
    public static int createUserWithRole(String username, String password, String email, String roleName) {
        // Using the stored procedure sp_create_user_with_role
        String sql = "{CALL sp_create_user_with_role(?, ?, ?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            // Set input parameters
            cstmt.setString(1, username);
            cstmt.setString(2, password);
            cstmt.setString(3, email);
            cstmt.setString(4, roleName);
            
            // Register output parameters
            cstmt.registerOutParameter(5, Types.INTEGER); // user_id
            cstmt.registerOutParameter(6, Types.BOOLEAN); // success
            cstmt.registerOutParameter(7, Types.VARCHAR); // error_message
            
            cstmt.execute();
            
            boolean success = cstmt.getBoolean(6);
            if (success) {
                return cstmt.getInt(5); // Return the user ID
            } else {
                String errorMessage = cstmt.getString(7);
                System.err.println("Error creating user with role: " + errorMessage);
                return -1;
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating user with role: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }
    
    /**
     * Get user by employee number (for compatibility with existing code)
     * @param employeeNum Employee number
     * @return User object if found, null otherwise
     */
    public static User getUserByEmployeeNumber(String employeeNum) {
        // Using the stored procedure sp_get_user_by_employee_number
        String sql = "SELECT * FROM sp_get_user_by_employee_number(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, employeeNum);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(rs.getString("employee_number"));
                    user.setUserId(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    
                    // Set roles
                    String roles = rs.getString("roles");
                    if (roles != null) {
                        user.setIsAdmin(roles.contains("ADMIN"));
                        user.setIsHR(roles.contains("HR"));
                        user.setIsFinance(roles.contains("FINANCE"));
                    }
                    
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by employee number: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get user by username
     * @param username Username to search for
     * @return User object if found, null otherwise
     */
    public static User getUserByUsername(String username) {
        // Using the stored procedure sp_get_user_by_username
        String sql = "SELECT * FROM sp_get_user_by_username(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String empNum = rs.getString("employee_number");
                    User user = new User(empNum != null ? empNum : "");
                    user.setUserId(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    
                    // Set roles
                    String roles = rs.getString("roles");
                    if (roles != null) {
                        user.setIsAdmin(roles.contains("ADMIN"));
                        user.setIsHR(roles.contains("HR"));
                        user.setIsFinance(roles.contains("FINANCE"));
                    }
                    
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by username: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Update user information
     * @param user User object with updated information
     * @return true if update successful, false otherwise
     */
    public static boolean updateUser(User user) {
        // Using the stored procedure sp_update_user
        String sql = "{CALL sp_update_user(?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            // Set input parameters
            cstmt.setString(1, user.getUserId()); // old username
            cstmt.setString(2, user.getUserId()); // new username (same for updating password only)
            cstmt.setString(3, user.getPassword());
            
            // Register output parameters
            cstmt.registerOutParameter(4, Types.BOOLEAN); // success
            cstmt.registerOutParameter(5, Types.VARCHAR); // error_message
            
            cstmt.execute();
            
            boolean success = cstmt.getBoolean(4);
            if (!success) {
                String errorMessage = cstmt.getString(5);
                System.err.println("Error updating user: " + errorMessage);
            }
            
            return success;
            
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Assign role to user
     * @param username Username
     * @param roleName Role name to assign
     * @return true if assignment successful, false otherwise
     */
    public static boolean assignRoleToUser(String username, String roleName) {
        // Using the stored procedure sp_assign_role_to_user
        String sql = "{CALL sp_assign_role_to_user(?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            // Set input parameters
            cstmt.setString(1, username);
            cstmt.setString(2, roleName);
            
            // Register output parameters
            cstmt.registerOutParameter(3, Types.BOOLEAN); // success
            cstmt.registerOutParameter(4, Types.VARCHAR); // error_message
            
            cstmt.execute();
            
            boolean success = cstmt.getBoolean(3);
            if (!success) {
                String errorMessage = cstmt.getString(4);
                System.err.println("Error assigning role to user: " + errorMessage);
            }
            
            return success;
            
        } catch (SQLException e) {
            System.err.println("Error assigning role to user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Remove role from user
     * @param username Username
     * @param roleName Role name to remove
     * @return true if removal successful, false otherwise
     */
    public static boolean removeRoleFromUser(String username, String roleName) {
        // Using the stored procedure sp_remove_role_from_user
        String sql = "{CALL sp_remove_role_from_user(?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            // Set input parameters
            cstmt.setString(1, username);
            cstmt.setString(2, roleName);
            
            // Register output parameters
            cstmt.registerOutParameter(3, Types.BOOLEAN); // success
            cstmt.registerOutParameter(4, Types.VARCHAR); // error_message
            
            cstmt.execute();
            
            boolean success = cstmt.getBoolean(3);
            if (!success) {
                String errorMessage = cstmt.getString(4);
                System.err.println("Error removing role from user: " + errorMessage);
            }
            
            return success;
            
        } catch (SQLException e) {
            System.err.println("Error removing role from user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get all users with their roles
     * @return List of all users
     */
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        
        // Using the stored procedure sp_get_all_users
        String sql = "SELECT * FROM sp_get_all_users()";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                String empNum = rs.getString("employee_number");
                User user = new User(empNum != null ? empNum : "");
                user.setUserId(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                
                // Set roles
                String roles = rs.getString("roles");
                if (roles != null) {
                    user.setIsAdmin(roles.contains("ADMIN"));
                    user.setIsHR(roles.contains("HR"));
                    user.setIsFinance(roles.contains("FINANCE"));
                }
                
                users.add(user);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all users: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }
    
    /**
     * Delete user by employee number (for compatibility)
     * @param employeeNum Employee number
     * @return true if deletion successful, false otherwise
     */
    public static boolean deleteUser(String employeeNum) {
        // Using the stored procedure sp_delete_user
        String sql = "{CALL sp_delete_user(?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            // Set input parameters
            cstmt.setString(1, employeeNum);
            
            // Register output parameters
            cstmt.registerOutParameter(2, Types.BOOLEAN); // success
            cstmt.registerOutParameter(3, Types.VARCHAR); // error_message
            
            cstmt.execute();
            
            boolean success = cstmt.getBoolean(2);
            if (!success) {
                String errorMessage = cstmt.getString(3);
                System.err.println("Error deleting user: " + errorMessage);
            }
            
            return success;
            
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Update username for a user based on employee number
     * @param employeeNumber Employee number to identify the user
     * @param newUsername New username to set
     * @return true if update successful, false otherwise
     */
    public static boolean updateUsername(String employeeNumber, String newUsername) {
        // Using the stored procedure sp_update_username
        String sql = "{CALL sp_update_username(?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            // Set input parameters
            cstmt.setString(1, employeeNumber);
            cstmt.setString(2, newUsername);
            
            // Register output parameters
            cstmt.registerOutParameter(3, Types.BOOLEAN); // success
            cstmt.registerOutParameter(4, Types.VARCHAR); // error_message
            
            cstmt.execute();
            
            boolean success = cstmt.getBoolean(3);
            if (!success) {
                String errorMessage = cstmt.getString(4);
                System.err.println("Error updating username: " + errorMessage);
            }
            
            return success;
            
        } catch (SQLException e) {
            System.err.println("Error updating username: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
