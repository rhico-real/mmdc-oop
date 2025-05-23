import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import GUI.LoginPage;
import UtilityClasses.JsonToDatabaseImporter;

public class Main {

    public static void main(String[] args) {
        // Create and show the loading dialog
        JDialog loadingDialog = createLoadingDialog();
        loadingDialog.setVisible(true);
        
        // Import data in a background thread
        Thread importThread = new Thread(() -> {
            try {
                // Import JSON data to database
                JsonToDatabaseImporter.importAllData();
                
                // Wait until import is complete
                while (JsonToDatabaseImporter.isImportInProgress()) {
                    Thread.sleep(500);
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
                    new LoginPage().setVisible(true);
                });
            }
        });
        
        importThread.start();
    }
    
    /**
     * Create a loading dialog with progress bar
     * @return JDialog loading dialog
     */
    private static JDialog createLoadingDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("CamuLite HR System");
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setModal(false);
        dialog.setResizable(false);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Motorph Payroll System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel loadingLabel = new JLabel("Initializing database and importing data...");
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
