package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Classes.User;
import Database.DatabaseConnection;

public class UserDAO {
    
    /**
     * Authenticate user login
     * @param username Username to check
     * @param password Password to verify
     * @return User object if authentication successful, null otherwise
     */
    public static User authenticateUser(String username, String password) {
        String sql = "SELECT employee_num, username, password, is_admin FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(String.valueOf(rs.getInt("employee_num")));
                    user.setUserId(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setIsAdmin(rs.getBoolean("is_admin"));
                    user.setLoginStatus(true);
                    user.setIsVerified(true);
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
     * Get user by employee number
     * @param employeeNum Employee number
     * @return User object if found, null otherwise
     */
    public static User getUserByEmployeeNumber(String employeeNum) {
        String sql = "SELECT employee_num, username, password, is_admin FROM users WHERE employee_num = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, Integer.parseInt(employeeNum));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(String.valueOf(rs.getInt("employee_num")));
                    user.setUserId(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setIsAdmin(rs.getBoolean("is_admin"));
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
        String sql = "SELECT employee_num, username, password, is_admin FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(String.valueOf(rs.getInt("employee_num")));
                    user.setUserId(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setIsAdmin(rs.getBoolean("is_admin"));
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
     * Create a new user
     * @param user User object to create
     * @return true if creation successful, false otherwise
     */
    public static boolean createUser(User user) {
        String sql = "INSERT INTO users (employee_num, username, password, is_admin) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, Integer.parseInt(user.getEmployeeNumber()));
            pstmt.setString(2, user.getUserId());
            pstmt.setString(3, user.getPassword());
            pstmt.setBoolean(4, user.getIsAdmin());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Update user information
     * @param user User object with updated information
     * @return true if update successful, false otherwise
     */
    public static boolean updateUser(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, is_admin = ?, updated_at = CURRENT_TIMESTAMP WHERE employee_num = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setBoolean(3, user.getIsAdmin());
            pstmt.setInt(4, Integer.parseInt(user.getEmployeeNumber()));
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete user by employee number
     * @param employeeNum Employee number
     * @return true if deletion successful, false otherwise
     */
    public static boolean deleteUser(String employeeNum) {
        String sql = "DELETE FROM users WHERE employee_num = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, Integer.parseInt(employeeNum));
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get all users
     * @return List of all users
     */
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT employee_num, username, password, is_admin FROM users ORDER BY employee_num";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                User user = new User(String.valueOf(rs.getInt("employee_num")));
                user.setUserId(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setIsAdmin(rs.getBoolean("is_admin"));
                users.add(user);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all users: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }
}
