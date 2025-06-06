package GUI.admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import Classes.UpdateRequest;
import DAO.UpdateRequestDAO;
import DAO.EmployeeDAO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;

@SuppressWarnings("serial")
public class UpdateRequestsPage extends JFrame {

    private JTable requestsTable;
    private DefaultTableModel tableModel;
    private JButton approveButton;
    private JButton rejectButton;
    private JButton backButton;
    private JTextField notesField;
    private List<UpdateRequest> requests;
    private int selectedRequestId = -1;

    public UpdateRequestsPage() {
        initComponents();
        loadRequests();
    }

    private void initComponents() {
        setTitle("MotorPH Payroll System | Employee Update Requests");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Create table model with columns
        String[] columns = {
            "Request ID", "Employee Number", "Name", "Date", "Status", "Details"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create table
        requestsTable = new JTable(tableModel);
        requestsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        requestsTable.setRowHeight(30);
        requestsTable.getTableHeader().setReorderingAllowed(false);
        
        // Add mouse listener to the table for row selection
        requestsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = requestsTable.getSelectedRow();
                if (row != -1) {
                    selectedRequestId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    String status = tableModel.getValueAt(row, 4).toString();
                    
                    // Only enable approve/reject buttons for pending requests
                    boolean isPending = "PENDING".equals(status);
                    approveButton.setEnabled(isPending);
                    rejectButton.setEnabled(isPending);
                    
                    // Show request details
                    showRequestDetails(selectedRequestId);
                }
            }
        });

        // Create scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(requestsTable);
        scrollPane.setPreferredSize(new Dimension(800, 300));

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonsPanel.setBackground(Color.WHITE);
        
        approveButton = new JButton("Approve");
        approveButton.setEnabled(false);
        approveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                approveSelectedRequest();
            }
        });
        
        rejectButton = new JButton("Reject");
        rejectButton.setEnabled(false);
        rejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rejectSelectedRequest();
            }
        });
        
        backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new DashboardPage().setVisible(true);
            }
        });
        
        JPanel notesPanel = new JPanel(new BorderLayout(5, 5));
        notesPanel.setBackground(Color.WHITE);
        notesPanel.add(new JLabel("Admin Notes:"), BorderLayout.NORTH);
        
        notesField = new JTextField(30);
        notesPanel.add(notesField, BorderLayout.CENTER);
        
        buttonsPanel.add(approveButton);
        buttonsPanel.add(rejectButton);
        buttonsPanel.add(notesPanel);
        buttonsPanel.add(backButton);
        
        // Details panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Request Details"));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setPreferredSize(new Dimension(800, 200));
        
        // Add components to the main panel
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        mainPanel.add(detailsPanel, BorderLayout.NORTH);
        
        // Set the main panel as the content pane
        setContentPane(mainPanel);
    }
    
    private void loadRequests() {
        // Clear the table
        tableModel.setRowCount(0);
        
        // Load all requests
        requests = UpdateRequestDAO.getUpdateRequests(null);
        
        // Format date for display
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        
        // Add requests to the table
        for (UpdateRequest request : requests) {
            String name = request.getFirstName() + " " + request.getLastName();
            String date = dateFormat.format(request.getRequestDate());
            
            Object[] row = {
                request.getRequestId(),
                request.getEmployeeNumber(),
                name,
                date,
                request.getStatus(),
                "View Details"
            };
            tableModel.addRow(row);
        }
        
        // Add row sorter
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        requestsTable.setRowSorter(sorter);
    }
    
    private void showRequestDetails(int requestId) {
        // Find the request
        UpdateRequest request = null;
        for (UpdateRequest r : requests) {
            if (r.getRequestId() == requestId) {
                request = r;
                break;
            }
        }
        
        if (request == null) {
            return;
        }
        
        // Create details panel
        JPanel detailsPanel = new JPanel(new GridLayout(5, 4, 10, 10));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Request Details"));
        detailsPanel.setBackground(Color.WHITE);
        
        // Add request details
        addDetailField(detailsPanel, "First Name:", request.getFirstName());
        addDetailField(detailsPanel, "Last Name:", request.getLastName());
        addDetailField(detailsPanel, "Birthday:", request.getBirthday());
        addDetailField(detailsPanel, "Address:", request.getAddress());
        addDetailField(detailsPanel, "Phone Number:", request.getPhoneNumber());
        addDetailField(detailsPanel, "SSS Number:", request.getSssNumber());
        addDetailField(detailsPanel, "PhilHealth Number:", request.getPhilhealthNumber());
        addDetailField(detailsPanel, "TIN Number:", request.getTinNumber());
        addDetailField(detailsPanel, "Pag-IBIG Number:", request.getPagibigNumber());
        
        // Admin notes
        if (request.getAdminNotes() != null && !request.getAdminNotes().isEmpty()) {
            addDetailField(detailsPanel, "Admin Notes:", request.getAdminNotes());
        }
        
        // Replace the details panel
        Container contentPane = getContentPane();
        if (contentPane instanceof JPanel) {
            JPanel mainPanel = (JPanel) contentPane;
            
            // Remove the old details panel
            Component[] components = mainPanel.getComponents();
            for (Component component : components) {
                if (component instanceof JPanel && ((JPanel) component).getBorder() instanceof javax.swing.border.TitledBorder) {
                    mainPanel.remove(component);
                    break;
                }
            }
            
            // Add the new details panel
            mainPanel.add(detailsPanel, BorderLayout.NORTH);
            mainPanel.revalidate();
            mainPanel.repaint();
        }
    }
    
    private void addDetailField(JPanel panel, String label, String value) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Arial", Font.BOLD, 12));
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Arial", Font.PLAIN, 12));
        
        panel.add(labelComponent);
        panel.add(valueComponent);
    }
    
    private void approveSelectedRequest() {
        if (selectedRequestId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a request to approve.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Confirm approval
        int option = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to approve this request? This will update the employee's information.", 
                "Confirm Approval", 
                JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            String notes = notesField.getText();
            
            // Approve the request
            boolean approved = UpdateRequestDAO.approveUpdateRequest(selectedRequestId, notes);
            
            if (approved) {
                JOptionPane.showMessageDialog(this, "Request approved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Reload requests
                loadRequests();
                
                // Reset selection
                selectedRequestId = -1;
                approveButton.setEnabled(false);
                rejectButton.setEnabled(false);
                notesField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to approve request. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void rejectSelectedRequest() {
        if (selectedRequestId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a request to reject.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Confirm rejection
        int option = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to reject this request?", 
                "Confirm Rejection", 
                JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            String notes = notesField.getText();
            
            // Reject the request
            boolean rejected = UpdateRequestDAO.rejectUpdateRequest(selectedRequestId, notes);
            
            if (rejected) {
                JOptionPane.showMessageDialog(this, "Request rejected successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Reload requests
                loadRequests();
                
                // Reset selection
                selectedRequestId = -1;
                approveButton.setEnabled(false);
                rejectButton.setEnabled(false);
                notesField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to reject request. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UpdateRequestsPage().setVisible(true);
            }
        });
    }
}
