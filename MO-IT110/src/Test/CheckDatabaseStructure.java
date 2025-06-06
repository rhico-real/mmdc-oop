package Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Database.DatabaseConnection;

public class CheckDatabaseStructure {
    
    public static void main(String[] args) {
        System.out.println("Checking Database Structure");
        System.out.println("==========================");
        
        try {
            checkRolesTable();
            checkUsersTable();
            checkUserRolesTable();
            
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void checkRolesTable() {
        System.out.println("\n1. Checking 'roles' table structure:");
        try (Connection conn = DatabaseConnection.getConnection()) {
            
            // Check if roles table exists
            String checkTableSQL = """
                SELECT EXISTS (
                    SELECT FROM information_schema.tables 
                    WHERE table_name = 'roles'
                )
            """;
            
            try (PreparedStatement pstmt = conn.prepareStatement(checkTableSQL);
                 ResultSet rs = pstmt.executeQuery()) {
                
                if (rs.next() && rs.getBoolean(1)) {
                    System.out.println("  ✓ 'roles' table exists");
                    
                    // Get column structure
                    String columnsSQL = """
                        SELECT column_name, data_type, is_nullable
                        FROM information_schema.columns 
                        WHERE table_name = 'roles'
                        ORDER BY ordinal_position
                    """;
                    
                    try (PreparedStatement colStmt = conn.prepareStatement(columnsSQL);
                         ResultSet colRs = colStmt.executeQuery()) {
                        
                        System.out.println("  Columns:");
                        while (colRs.next()) {
                            System.out.println("    - " + colRs.getString("column_name") + 
                                             " (" + colRs.getString("data_type") + 
                                             ", nullable: " + colRs.getString("is_nullable") + ")");
                        }
                    }
                    
                    // Show existing roles
                    String rolesSQL = "SELECT * FROM roles";
                    try (PreparedStatement roleStmt = conn.prepareStatement(rolesSQL);
                         ResultSet roleRs = roleStmt.executeQuery()) {
                        
                        System.out.println("  Existing roles:");
                        while (roleRs.next()) {
                            System.out.println("    - Role ID: " + roleRs.getInt("role_id") + 
                                             ", Name: " + roleRs.getString("role_name"));
                        }
                    }
                    
                } else {
                    System.out.println("  ❌ 'roles' table does not exist");
                }
            }
            
        } catch (SQLException e) {
            System.out.println("  ❌ Error checking roles table: " + e.getMessage());
        }
    }
    
    private static void checkUsersTable() {
        System.out.println("\n2. Checking 'users' table structure:");
        try (Connection conn = DatabaseConnection.getConnection()) {
            
            // Check if users table exists
            String checkTableSQL = """
                SELECT EXISTS (
                    SELECT FROM information_schema.tables 
                    WHERE table_name = 'users'
                )
            """;
            
            try (PreparedStatement pstmt = conn.prepareStatement(checkTableSQL);
                 ResultSet rs = pstmt.executeQuery()) {
                
                if (rs.next() && rs.getBoolean(1)) {
                    System.out.println("  ✓ 'users' table exists");
                    
                    // Get column structure
                    String columnsSQL = """
                        SELECT column_name, data_type, is_nullable
                        FROM information_schema.columns 
                        WHERE table_name = 'users'
                        ORDER BY ordinal_position
                    """;
                    
                    try (PreparedStatement colStmt = conn.prepareStatement(columnsSQL);
                         ResultSet colRs = colStmt.executeQuery()) {
                        
                        System.out.println("  Columns:");
                        while (colRs.next()) {
                            System.out.println("    - " + colRs.getString("column_name") + 
                                             " (" + colRs.getString("data_type") + 
                                             ", nullable: " + colRs.getString("is_nullable") + ")");
                        }
                    }
                    
                } else {
                    System.out.println("  ❌ 'users' table does not exist");
                }
            }
            
        } catch (SQLException e) {
            System.out.println("  ❌ Error checking users table: " + e.getMessage());
        }
    }
    
    private static void checkUserRolesTable() {
        System.out.println("\n3. Checking 'user_roles' table structure:");
        try (Connection conn = DatabaseConnection.getConnection()) {
            
            // Check if user_roles table exists
            String checkTableSQL = """
                SELECT EXISTS (
                    SELECT FROM information_schema.tables 
                    WHERE table_name = 'user_roles'
                )
            """;
            
            try (PreparedStatement pstmt = conn.prepareStatement(checkTableSQL);
                 ResultSet rs = pstmt.executeQuery()) {
                
                if (rs.next() && rs.getBoolean(1)) {
                    System.out.println("  ✓ 'user_roles' table exists");
                } else {
                    System.out.println("  ❌ 'user_roles' table does not exist");
                }
            }
            
        } catch (SQLException e) {
            System.out.println("  ❌ Error checking user_roles table: " + e.getMessage());
        }
    }
}
