import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import Database.DatabaseConnection;
import Database.DatabaseObjectsInstaller;
import Database.DirectDatabaseInstaller;
import GUI.LoginPage;
import UtilityClasses.JsonToDatabaseImporter;

public class Main {

    public static void main(String[] args) {
        // Create and show the loading dialog
        JDialog loadingDialog = createLoadingDialog();
        loadingDialog.setVisible(true);
        
        // Database initialization in a background thread
        Thread initThread = new Thread(() -> {
            try {
                // Initialize the database
                JsonToDatabaseImporter.importAllData();
                
                // Wait until initialization is complete
                while (JsonToDatabaseImporter.isImportInProgress()) {
                   Thread.sleep(500);
                }
                
                // Test database connection
                if (!DatabaseConnection.testConnection()) {
                    SwingUtilities.invokeLater(() -> {
                        loadingDialog.dispose();
                        JOptionPane.showMessageDialog(
                            null,
                            "Database connection failed. Please make sure PostgreSQL is running and properly configured.",
                            "Database Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                        System.exit(1);
                    });
                    return;
                }
                
                // Install database views and stored procedures directly
                System.out.println("Using direct installer for database objects...");
                if (!DirectDatabaseInstaller.installDatabaseObjects()) {
                    SwingUtilities.invokeLater(() -> {
                        loadingDialog.dispose();
                        JOptionPane.showMessageDialog(
                            null,
                            "Failed to install database objects. The application may not function correctly.",
                            "Database Warning",
                            JOptionPane.WARNING_MESSAGE
                        );
                        // Continue anyway, as the application might still work with direct SQL queries
                        new LoginPage().setVisible(true);
                    });
                    return;
                }
                
                // Close the loading dialog and start the application
                SwingUtilities.invokeLater(() -> {
                    loadingDialog.dispose();
                    new LoginPage().setVisible(true);
                });
            } catch (Exception e) {
                System.err.println("Error initializing database: " + e.getMessage());
                e.printStackTrace();
                
                // Close the loading dialog and show error
                SwingUtilities.invokeLater(() -> {
                    loadingDialog.dispose();
                    
                    // Show error message
                    JOptionPane.showMessageDialog(
                        null,
                        "Error initializing database: " + e.getMessage() + 
                        "\nThe application will exit.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    
                    System.exit(1);
                });
            }
        });
        
        initThread.start();
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
        
        JLabel loadingLabel = new JLabel("Initializing database...");
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