package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        String sql = """
            SELECT u.user_id, u.username, u.password, u.email, u.is_active,
                   e.employee_id, e.employee_number,
                   STRING_AGG(r.role_name, ',') as roles
            FROM users u
            LEFT JOIN employees e ON u.user_id = e.user_id
            LEFT JOIN user_roles ur ON u.user_id = ur.user_id AND ur.is_active = TRUE
            LEFT JOIN roles r ON ur.role_id = r.role_id
            WHERE u.username = ? AND u.password = ? AND u.is_active = TRUE
            GROUP BY u.user_id, u.username, u.password, u.email, u.is_active, 
                     e.employee_id, e.employee_number
        """;
        
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
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Create user
            String insertUserSql = """
                INSERT INTO users (username, password, email, is_active) 
                VALUES (?, ?, ?, TRUE)
            """;
            
            int userId;
            try (PreparedStatement pstmt = conn.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.setString(3, email);
                pstmt.executeUpdate();
                
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        userId = rs.getInt(1);
                    } else {
                        throw new SQLException("Failed to get generated user ID");
                    }
                }
            }
            
            // Assign role
            String assignRoleSql = """
                INSERT INTO user_roles (user_id, role_id) 
                SELECT ?, role_id FROM roles WHERE role_name = ?
            """;
            try (PreparedStatement pstmt = conn.prepareStatement(assignRoleSql)) {
                pstmt.setInt(1, userId);
                pstmt.setString(2, roleName);
                pstmt.executeUpdate();
            }
            
            // Create specific role record if needed
            if ("ADMIN".equals(roleName)) {
            createAdminRecord(conn, userId);
            } else if ("HR".equals(roleName)) {
            createHRRecord(conn, userId);
            } else if ("FINANCE".equals(roleName)) {
				createFinanceRecord(conn, userId);
			}
            
            conn.commit();
            return userId;
            
        } catch (SQLException e) {
            System.err.println("Error creating user with role: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return -1;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Get user by employee number (for compatibility with existing code)
     * @param employeeNum Employee number
     * @return User object if found, null otherwise
     */
    public static User getUserByEmployeeNumber(String employeeNum) {
        String sql = """
            SELECT u.user_id, u.username, u.password, u.email, u.is_active,
                   e.employee_number,
                   STRING_AGG(r.role_name, ',') as roles
            FROM users u
            JOIN employees e ON u.user_id = e.user_id
            LEFT JOIN user_roles ur ON u.user_id = ur.user_id AND ur.is_active = TRUE
            LEFT JOIN roles r ON ur.role_id = r.role_id
            WHERE e.employee_number = ? AND u.is_active = TRUE
            GROUP BY u.user_id, u.username, u.password, u.email, u.is_active, e.employee_number
        """;
        
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
        String sql = """
            SELECT u.user_id, u.username, u.password, u.email, u.is_active,
                   e.employee_number,
                   STRING_AGG(r.role_name, ',') as roles
            FROM users u
            LEFT JOIN employees e ON u.user_id = e.user_id
            LEFT JOIN user_roles ur ON u.user_id = ur.user_id AND ur.is_active = TRUE
            LEFT JOIN roles r ON ur.role_id = r.role_id
            WHERE u.username = ? AND u.is_active = TRUE
            GROUP BY u.user_id, u.username, u.password, u.email, u.is_active, e.employee_number
        """;
        
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
        // For compatibility, we need to get the user_id first
        User existingUser = getUserByEmployeeNumber(user.getEmployeeNumber());
        if (existingUser == null) {
            return false;
        }
        
        String sql = """
            UPDATE users SET 
                username = ?, password = ?, updated_at = CURRENT_TIMESTAMP 
            WHERE username = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, existingUser.getUserId()); // Use existing username as identifier
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
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
        String sql = """
            INSERT INTO user_roles (user_id, role_id) 
            SELECT u.user_id, r.role_id 
            FROM users u, roles r 
            WHERE u.username = ? AND r.role_name = ?
            ON CONFLICT (user_id, role_id) DO UPDATE SET is_active = TRUE
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, roleName);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
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
        String sql = """
            UPDATE user_roles SET is_active = FALSE 
            WHERE user_id = (SELECT user_id FROM users WHERE username = ?) 
            AND role_id = (SELECT role_id FROM roles WHERE role_name = ?)
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, roleName);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
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
        String sql = """
            SELECT u.user_id, u.username, u.password, u.email, u.is_active,
                   e.employee_number,
                   STRING_AGG(r.role_name, ',') as roles
            FROM users u
            LEFT JOIN employees e ON u.user_id = e.user_id
            LEFT JOIN user_roles ur ON u.user_id = ur.user_id AND ur.is_active = TRUE
            LEFT JOIN roles r ON ur.role_id = r.role_id
            WHERE u.is_active = TRUE
            GROUP BY u.user_id, u.username, u.password, u.email, u.is_active, e.employee_number
            ORDER BY u.username
        """;
        
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
        String sql = """
            UPDATE users SET is_active = FALSE, updated_at = CURRENT_TIMESTAMP 
            WHERE user_id = (SELECT user_id FROM employees WHERE employee_number = ?)
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, employeeNum);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
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
        String sql = """
            UPDATE users SET 
                username = ?, updated_at = CURRENT_TIMESTAMP 
            WHERE user_id = (
                SELECT user_id FROM employees WHERE employee_number = ?
            )
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newUsername);
            pstmt.setString(2, employeeNumber);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating username: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Helper method to create admin record
     */
    private static void createAdminRecord(Connection conn, int userId) throws SQLException {
        String sql = "INSERT INTO admins (user_id, admin_level, permissions) VALUES (?, 1, 'BASIC')";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Helper method to create HR record
     */
    private static void createHRRecord(Connection conn, int userId) throws SQLException {
        String sql = "INSERT INTO hr_personnel (user_id, hr_level) VALUES (?, 'Junior')";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Helper method to create Finance record
     */
    private static void createFinanceRecord(Connection conn, int userId) throws SQLException {
        String sql = "INSERT INTO finance_personnel (user_id, finance_level) VALUES (?, 'Junior')";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        }
    }
}
