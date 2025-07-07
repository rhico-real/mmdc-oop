package Database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Utility class to install SQL views and stored procedures
 */
public class DatabaseObjectsInstaller {
    
    private static final String SQL_DIR = "/Users/systech/Documents/mmdc/aoop/mmdc-oop/MO-IT110/src/Database/SQL";
    
    /**
     * Install all database objects (views and stored procedures)
     * @return true if installation successful, false otherwise
     */
    public static boolean installDatabaseObjects() {
        System.out.println("Installing database objects...");
        
        // First install views
        if (!installViews()) {
            return false;
        }
        
        // Then install stored procedures
        if (!installProcedures()) {
            return false;
        }
        
        System.out.println("All database objects installed successfully!");
        return true;
    }
    
    /**
     * Install all database views
     * @return true if installation successful, false otherwise
     */
    private static boolean installViews() {
        System.out.println("Installing database views...");
        
        String[] viewFiles = {
            "EmployeeViews.sql",
            "AttendanceViews.sql",
            "LeaveRequestViews.sql",
            "UserViews.sql",
            "UpdateRequestViews.sql"
        };
        
        for (String file : viewFiles) {
            if (!executeSqlFile(file)) {
                // Try with fallback method
                if (!executeEntireSqlFile(file)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Install all stored procedures
     * @return true if installation successful, false otherwise
     */
    private static boolean installProcedures() {
        System.out.println("Installing stored procedures...");
        
        String[] procedureFiles = {
            "EmployeeProcedures.sql",
            "AttendanceProcedures.sql",
            "LeaveRequestProcedures.sql",
            "UserProcedures.sql",
            "UpdateRequestProcedures.sql"
        };
        
        for (String file : procedureFiles) {
            // For stored procedures, it's safer to use the executeStatementByStatement method
            if (!executeStatementByStatement(file)) {
                // Try with fallback method for the entire file
                if (!executeEntireSqlFile(file)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Execute SQL statements from a file one at a time
     * This method executes each CREATE PROCEDURE/FUNCTION statement as a separate unit
     * @param fileName Name of the SQL file
     * @return true if execution successful, false otherwise
     */
    private static boolean executeStatementByStatement(String fileName) {
        System.out.println("Executing SQL file statement by statement: " + fileName);
        
        File file = new File(SQL_DIR, fileName);
        String fileContent;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            fileContent = content.toString();
        } catch (IOException e) {
            System.err.println("Error reading SQL file: " + fileName);
            e.printStackTrace();
            return false;
        }
        
        // Extract each CREATE FUNCTION or CREATE PROCEDURE statement
        List<String> statements = extractCreateStatements(fileContent);
        boolean success = true;
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            for (String statement : statements) {
                if (statement.trim().isEmpty()) {
                    continue;
                }
                
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(statement);
                } catch (SQLException e) {
                    System.err.println("Error executing SQL statement: " + e.getMessage());
                    System.err.println("Statement (first 100 chars): " + statement.substring(0, Math.min(100, statement.length())) + "...");
                    e.printStackTrace();
                    success = false;
                    // Continue with other statements
                }
            }
        } catch (SQLException e) {
            System.err.println("Error with database connection: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        
        if (success) {
            System.out.println("SQL file executed successfully: " + fileName);
            return true;
        } else {
            System.out.println("Some statements failed in file: " + fileName);
            return false;
        }
    }
    
    /**
     * Extract CREATE FUNCTION/PROCEDURE statements from SQL content
     * @param content SQL content
     * @return List of CREATE statements
     */
    private static List<String> extractCreateStatements(String content) {
        List<String> statements = new ArrayList<>();
        
        // Remove comments
        content = removeComments(content);
        
        // Pattern to match CREATE [OR REPLACE] FUNCTION/PROCEDURE
        Pattern pattern = Pattern.compile("(?i)\\s*CREATE\\s+(OR\\s+REPLACE\\s+)?((FUNCTION)|(PROCEDURE))\\s+", 
                                         Pattern.CASE_INSENSITIVE);
        
        // Split by the pattern
        String[] parts = pattern.split(content);
        
        // The first part is before any CREATE statement
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            
            // Find the end of the function/procedure definition (usually marked by '$$;')
            int endPos = part.indexOf("$$;");
            if (endPos >= 0) {
                // Reconstruct the complete statement
                String statement = "CREATE OR REPLACE " + 
                                  (part.trim().toUpperCase().startsWith("FUNCTION") ? "FUNCTION " : "PROCEDURE ") +
                                  part.substring(0, endPos + 3); // +3 to include '$$;'
                statements.add(statement);
            }
        }
        
        return statements;
    }
    
    /**
     * Execute SQL file directly as a single statement
     * This is a fallback method used when parsing the file fails
     * @param fileName Name of the SQL file
     * @return true if execution successful, false otherwise
     */
    private static boolean executeEntireSqlFile(String fileName) {
        System.out.println("Executing entire SQL file as single statement: " + fileName);
        
        File file = new File(SQL_DIR, fileName);
        StringBuilder sql = new StringBuilder();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error reading SQL file: " + fileName);
            e.printStackTrace();
            return false;
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(sql.toString());
            System.out.println("SQL file executed successfully: " + fileName);
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error executing SQL file: " + fileName);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Execute SQL statements from a file
     * @param fileName Name of the SQL file
     * @return true if execution successful, false otherwise
     */
    private static boolean executeSqlFile(String fileName) {
        System.out.println("Executing SQL file: " + fileName);
        
        File file = new File(SQL_DIR, fileName);
        String sqlContent;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            sqlContent = content.toString();
        } catch (IOException e) {
            System.err.println("Error reading SQL file: " + fileName);
            e.printStackTrace();
            return false;
        }
        
        // Split the SQL content into individual statements
        List<String> statements = splitSqlStatements(sqlContent);
        boolean success = true;
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Execute each statement
            for (String statement : statements) {
                if (statement.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    stmt.execute(statement);
                } catch (SQLException e) {
                    System.err.println("Error executing SQL statement: " + e.getMessage());
                    System.err.println("Statement (first 100 chars): " + statement.substring(0, Math.min(100, statement.length())) + "...");
                    e.printStackTrace();
                    success = false;
                    // Continue with other statements instead of failing completely
                }
            }
            
            if (success) {
                System.out.println("SQL file executed successfully: " + fileName);
                return true;
            } else {
                System.out.println("Errors occurred while executing SQL file: " + fileName);
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("Error with database connection: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Split SQL content into individual statements
     * @param sqlContent SQL content
     * @return List of SQL statements
     */
    private static List<String> splitSqlStatements(String sqlContent) {
        List<String> statements = new ArrayList<>();
        StringBuilder currentStatement = new StringBuilder();
        
        // Split by semicolons, but ignore semicolons inside quotes or comments
        boolean inQuote = false;
        boolean inComment = false;
        
        for (int i = 0; i < sqlContent.length(); i++) {
            char c = sqlContent.charAt(i);
            char next = (i < sqlContent.length() - 1) ? sqlContent.charAt(i + 1) : '\0';
            
            // Check for start/end of comments
            if (!inQuote && c == '-' && next == '-') {
                inComment = true;
            }
            
            // Check for end of line (end of comment)
            if (inComment && (c == '\n' || c == '\r')) {
                inComment = false;
            }
            
            // Check for quotes (but ignore if in a comment)
            if (!inComment && c == '\'') {
                inQuote = !inQuote;
            }
            
            // Add the character to the current statement
            currentStatement.append(c);
            
            // Check for semicolon (end of statement)
            if (c == ';' && !inQuote && !inComment) {
                statements.add(currentStatement.toString());
                currentStatement = new StringBuilder();
            }
        }
        
        // Add any remaining statement
        if (currentStatement.length() > 0) {
            statements.add(currentStatement.toString());
        }
        
        return statements;
    }
    
    /**
     * Remove SQL comments from the content
     * @param content SQL content
     * @return SQL content without comments
     */
    private static String removeComments(String content) {
        // Remove multi-line comments (/* ... */)
        content = Pattern.compile("/\\*[\\s\\S]*?\\*/").matcher(content).replaceAll("");
        
        // Remove single line comments (-- ...)
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n");
        for (String line : lines) {
            int commentPos = line.indexOf("--");
            if (commentPos >= 0) {
                result.append(line.substring(0, commentPos));
            } else {
                result.append(line);
            }
            result.append("\n");
        }
        
        return result.toString();
    }
}
