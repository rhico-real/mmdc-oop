package UtilityClasses;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import Classes.LeaveRequest;
import DAO.AttendanceDAO;
import DAO.AttendanceDAO.AttendanceRecord;
import Database.DatabaseConnection;
import Database.DatabaseInitializer;

/**
 * Utility class to import JSON data into the PostgreSQL database
 */
public class JsonToDatabaseImporter {
    
    private static boolean importInProgress = false;
    
    private JsonToDatabaseImporter() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Import all JSON data into the database with progress updates in the console
     */
    public static void importAllData() {
        if (importInProgress) {
            System.out.println("Import already in progress, please wait...");
            return;
        }
        
        importInProgress = true;
        
        // Create a SwingWorker to run the import in the background
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                try {
                    publish("Initializing database schema...");
                    // First initialize the database schema
                    DatabaseInitializer.initializeDatabase();
                    
                    // Import each type of data
                    publish("Importing login credentials...");
                    importLoginCredentials();
                    
                    publish("Importing employee data...");
                    importEmployees();
                    
                    publish("Importing attendance records...");
                    importAttendance();
                    
                    publish("Importing leave requests...");
                    importLeaveRequests();
                    
                    publish("All data imported successfully!");
                    return true;
                } catch (Exception e) {
                    publish("Error importing data: " + e.getMessage());
                    e.printStackTrace();
                    return false;
                } finally {
                    importInProgress = false;
                }
            }
            
            @Override
            protected void process(List<String> chunks) {
                // Print progress messages to console
                for (String message : chunks) {
                    System.out.println(message);
                }
            }
            
            @Override
            protected void done() {
                try {
                    if (get()) {
                        JOptionPane.showMessageDialog(
                            null,
                            "Data import completed successfully!",
                            "Import Complete",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                            null,
                            "Error during data import. Check console for details.",
                            "Import Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        
        worker.execute();
    }
    
    /**
     * Import login credentials from JSON to database
     */
    private static void importLoginCredentials() throws IOException, SQLException {
        JsonArray loginCredentials = JsonFileHandler.getLoginCredentialsJSON();
        
        String sql = "INSERT INTO users (employee_num, username, password, is_admin) VALUES (?, ?, ?, ?) " +
                     "ON CONFLICT (employee_num) DO UPDATE SET " +
                     "username = EXCLUDED.username, password = EXCLUDED.password";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            int count = 0;
            for (JsonElement element : loginCredentials) {
                JsonObject user = element.getAsJsonObject();
                
                pstmt.setInt(1, user.get("employeeNum").getAsInt());
                pstmt.setString(2, user.get("username").getAsString());
                pstmt.setString(3, user.get("password").getAsString());
                pstmt.setBoolean(4, false); // Regular users, not admin
                
                pstmt.addBatch();
                count++;
                
                // Execute in batches of 50
                if (count % 50 == 0) {
                    pstmt.executeBatch();
                }
            }
            
            // Add admin user
            pstmt.setInt(1, 99999);  // Special employee number for admin
            pstmt.setString(2, "admin");
            pstmt.setString(3, "123");
            pstmt.setBoolean(4, true);
            pstmt.addBatch();
            
            int[] results = pstmt.executeBatch();
            System.out.println("Imported " + (count + 1) + " login credentials");
        }
    }
    
    /**
     * Import employees from JSON to database
     */
    private static void importEmployees() throws IOException, SQLException {
        JsonArray employees = JsonFileHandler.getEmployeesJSON();
        
        String sql = """
            INSERT INTO employees (
                employee_num, last_name, first_name, birthday, address, phone_number, 
                sss, philhealth, tin, pagibig, status, position, immediate_supervisor,
                basic_salary, rice_subsidy, phone_allowance, clothing_allowance,
                gross_semi_monthly_rate, hourly_rate
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT (employee_num) DO UPDATE SET
                last_name = EXCLUDED.last_name,
                first_name = EXCLUDED.first_name,
                birthday = EXCLUDED.birthday,
                address = EXCLUDED.address,
                phone_number = EXCLUDED.phone_number,
                sss = EXCLUDED.sss,
                philhealth = EXCLUDED.philhealth,
                tin = EXCLUDED.tin,
                pagibig = EXCLUDED.pagibig,
                status = EXCLUDED.status,
                position = EXCLUDED.position,
                immediate_supervisor = EXCLUDED.immediate_supervisor,
                basic_salary = EXCLUDED.basic_salary,
                rice_subsidy = EXCLUDED.rice_subsidy,
                phone_allowance = EXCLUDED.phone_allowance,
                clothing_allowance = EXCLUDED.clothing_allowance,
                gross_semi_monthly_rate = EXCLUDED.gross_semi_monthly_rate,
                hourly_rate = EXCLUDED.hourly_rate
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            int count = 0;
            for (JsonElement element : employees) {
                JsonObject employee = element.getAsJsonObject();
                
                pstmt.setInt(1, employee.get("employeeNum").getAsInt());
                pstmt.setString(2, employee.get("last_name").getAsString());
                pstmt.setString(3, employee.get("first_name").getAsString());
                pstmt.setString(4, employee.get("birthday").getAsString());
                pstmt.setString(5, employee.get("address").getAsString());
                pstmt.setString(6, employee.get("phone_number").getAsString());
                pstmt.setString(7, employee.get("SSS").getAsString());
                pstmt.setLong(8, employee.get("Philhealth").getAsLong());
                pstmt.setString(9, employee.get("TIN").getAsString());
                pstmt.setLong(10, employee.get("Pag-ibig").getAsLong());
                pstmt.setString(11, employee.get("Status").getAsString());
                pstmt.setString(12, employee.get("Position").getAsString());
                pstmt.setString(13, employee.has("immediate_supervisor") ? 
                                  employee.get("immediate_supervisor").getAsString() : "N/A");
                pstmt.setDouble(14, employee.get("basic_salary").getAsDouble());
                pstmt.setDouble(15, employee.get("rice_subsidy").getAsDouble());
                pstmt.setDouble(16, employee.get("phone_allowance").getAsDouble());
                pstmt.setDouble(17, employee.get("clothing_allowance").getAsDouble());
                pstmt.setDouble(18, employee.get("gross_semi-monthly_rate").getAsDouble());
                pstmt.setDouble(19, employee.get("hourly_rate").getAsDouble());
                
                pstmt.addBatch();
                count++;
                
                // Execute in batches of 50
                if (count % 50 == 0) {
                    pstmt.executeBatch();
                }
            }
            
            int[] results = pstmt.executeBatch();
            System.out.println("Imported " + count + " employee records");
        }
    }
    
    /**
     * Import attendance from JSON to database
     */
    private static void importAttendance() throws IOException {
        JsonArray attendance = JsonFileHandler.getAttendanceJSON();
        
        int importedCount = 0;
        int totalCount = attendance.size();
        
        for (JsonElement element : attendance) {
            JsonObject record = element.getAsJsonObject();
            
            AttendanceRecord attendanceRecord = new AttendanceRecord(
                record.get("employeeNum").getAsInt(),
                record.get("last_name").getAsString(),
                record.get("first_name").getAsString(),
                record.get("date").getAsString(),
                record.get("time_in").getAsString(),
                record.get("time_out").getAsString()
            );
            
            if (AttendanceDAO.saveAttendanceRecord(attendanceRecord)) {
                importedCount++;
            }
            
            // Print progress every 100 records
            if (importedCount % 100 == 0 || importedCount == totalCount) {
                System.out.println("Imported " + importedCount + " of " + totalCount + " attendance records");
            }
        }
        
        System.out.println("Completed importing " + importedCount + " attendance records");
    }
    
    /**
     * Import leave requests from JSON to database
     */
    private static void importLeaveRequests() throws IOException {
        List<LeaveRequest> leaveRequests = JsonFileHandler.readLeaveRequestsFromFile(
            JsonFileHandler.getLeaveRequestJsonPath());
        
        int importedCount = 0;
        for (LeaveRequest request : leaveRequests) {
            if (DAO.LeaveRequestDAO.createLeaveRequest(request)) {
                importedCount++;
            }
        }
        
        System.out.println("Imported " + importedCount + " leave request records");
    }
    
    /**
     * Check if import is currently in progress
     * @return true if import is in progress, false otherwise
     */
    public static boolean isImportInProgress() {
        return importInProgress;
    }
}
