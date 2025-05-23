import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import DAO.UserDAO;
import Database.DatabaseConnection;
import Database.DatabaseInitializer;
import GUI.LoginPage;
import UtilityClasses.JsonToDatabaseImporter;

public class Main {

    public static void main(String[] args) {
        // Create and show the loading dialog
        JDialog loadingDialog = createLoadingDialog();
        loadingDialog.setVisible(true);
        
        // Database initialization and data import in a background thread
        Thread importThread = new Thread(() -> {
            try {
                // First initialize the database schema
                DatabaseInitializer.initializeDatabase();
                
                // Check if data already exists in the database
                if (isDataAlreadyImported()) {
                    System.out.println("Data already exists in the database. Skipping import.");
                    
                    // Update loading dialog message
                    SwingUtilities.invokeLater(() -> {
                        for (java.awt.Component comp : ((JPanel)loadingDialog.getContentPane().getComponent(0)).getComponents()) {
                            if (comp instanceof JPanel) {
                                for (java.awt.Component inner : ((JPanel)comp).getComponents()) {
                                    if (inner instanceof JLabel && ((JLabel)inner).getText().contains("Initializing")) {
                                        ((JLabel)inner).setText("Database already initialized. Starting application...");
                                    }
                                }
                            }
                        }
                    });
                    
                    // Small delay to show the message
                    Thread.sleep(1500);
                } else {
                    // Import JSON data to database
                    JsonToDatabaseImporter.importAllData();
                    
                    // Wait until import is complete
                    while (JsonToDatabaseImporter.isImportInProgress()) {
                        Thread.sleep(500);
                    }
                }
                
                // Close the loading dialog and start the application
                SwingUtilities.invokeLater(() -> {
                    loadingDialog.dispose();
                    new LoginPage().setVisible(true);
                });
            } catch (Exception e) {
                System.err.println("Error initializing database: " + e.getMessage());
                e.printStackTrace();
                
                // Close the loading dialog even if there's an error
                SwingUtilities.invokeLater(() -> {
                    loadingDialog.dispose();
                    
                    // Show error message
                    JOptionPane.showMessageDialog(
                        null,
                        "Error initializing database: " + e.getMessage() + 
                        "\nThe application will start, but some features might not work correctly.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    
                    new LoginPage().setVisible(true);
                });
            }
        });
        
        importThread.start();
    }
    
    /**
     * Check if data already exists in the database
     * @return true if data exists, false otherwise
     */
    private static boolean isDataAlreadyImported() {
        try {
            // Check if there are users in the database
            Connection conn = DatabaseConnection.getConnection();
            
            // Check users table
            String sqlUsers = "SELECT COUNT(*) FROM users";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUsers);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int userCount = rs.getInt(1);
                    // If we have users (more than just the admin user), data has been imported
                    if (userCount > 1) {
                        return true;
                    }
                }
            }
            
            // Additional check: verify if employee data exists
            String sqlEmployees = "SELECT COUNT(*) FROM employees";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlEmployees);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int employeeCount = rs.getInt(1);
                    // If we have employees, data has been imported
                    if (employeeCount > 0) {
                        return true;
                    }
                }
            }
            
            // If we get here, no data has been imported yet
            return false;
        } catch (Exception e) {
            // If an error occurs (like tables don't exist yet), assume no data has been imported
            System.err.println("Error checking for existing data: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Create a loading dialog with progress bar
     * @return JDialog loading dialog
     */
    private static JDialog createLoadingDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("MotorPH Payroll System");
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setModal(false);
        dialog.setResizable(false);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("MotorPH Payroll System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel loadingLabel = new JLabel("Initializing database and checking for existing data...");
        loadingLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(350, 20));
        progressBar.setBorder(BorderFactory.createEmptyBorder());
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(loadingLabel, BorderLayout.NORTH);
        centerPanel.add(progressBar, BorderLayout.CENTER);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        
        dialog.getContentPane().add(panel);
        
        return dialog;
    }
}
