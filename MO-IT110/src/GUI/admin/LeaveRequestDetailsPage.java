package GUI.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import Classes.Compensation;
import Classes.GovernmentIdentification;
import Classes.LeaveRequest;
import DAO.LeaveRequestDAO;

@SuppressWarnings("serial")
public class LeaveRequestDetailsPage extends JFrame {
	private javax.swing.JLabel endDateField;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextArea notesField;
	private javax.swing.JLabel leaveRequestLabel;
	private javax.swing.JLabel notesLabel;
	private javax.swing.JLabel startDateField;
	private javax.swing.JButton submitButton;
	private javax.swing.JButton cancelButton;
	private javax.swing.JButton viewRequestsButton;
	private javax.swing.JLabel typeOfLeaveDropdown;
	private javax.swing.JLabel typeOfLeaveLabel;
	private GovernmentIdentification employeeGI;
	private Compensation employeeComp;
	private LeaveRequest leaveRequest;

	public LeaveRequestDetailsPage(GovernmentIdentification employeeGI, Compensation employeeComp,
			LeaveRequest leaveRequest) {
		this.employeeGI = employeeGI;
		this.employeeComp = employeeComp;
		this.leaveRequest = leaveRequest;

		initComponents();
	}

	// @SuppressWarnings("unchecked")
	private void initComponents() {

		leaveRequestLabel = new javax.swing.JLabel();
		typeOfLeaveLabel = new javax.swing.JLabel();
		typeOfLeaveDropdown = new javax.swing.JLabel();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		startDateField = new javax.swing.JLabel();
		endDateField = new javax.swing.JLabel();
		notesLabel = new javax.swing.JLabel();
		jScrollPane1 = new javax.swing.JScrollPane();
		notesField = new javax.swing.JTextArea();
		submitButton = new javax.swing.JButton();
		cancelButton = new javax.swing.JButton();
		viewRequestsButton = new javax.swing.JButton();

		setTitle("MotorPH Payroll System | Leave Request Details");
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);

		leaveRequestLabel.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
		leaveRequestLabel.setText("Leave Request Details");

		typeOfLeaveLabel.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
		typeOfLeaveLabel.setText("Type of Leave");

		typeOfLeaveDropdown.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
		typeOfLeaveDropdown.setText(leaveRequest.getLeaveType());

		jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
		jLabel1.setText("Start Date");

		jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
		jLabel2.setText("End Date");

		startDateField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
		startDateField.setPreferredSize(new java.awt.Dimension(126, 22));

		try {
			startDateField.setText(new SimpleDateFormat("EEE MMM dd, yyyy")
					.format(new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(leaveRequest.getStartDate())));
		} catch (ParseException e) {
			
			e.printStackTrace();
		}

		endDateField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
		endDateField.setPreferredSize(new java.awt.Dimension(126, 22));

		try {
			endDateField.setText(new SimpleDateFormat("EEE MMM dd, yyyy")
					.format(new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(leaveRequest.getEndDate())));
		} catch (ParseException e) {
			
			e.printStackTrace();
		}

		notesLabel.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
		notesLabel.setText("Notes");

		notesField.setColumns(20);
		notesField.setRows(5);
		notesField.setText(leaveRequest.getNotes());
		notesField.setEditable(false);

		jScrollPane1.setViewportView(notesField);

		submitButton.setText("Approve");
		submitButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				approveButtonActionPerformed(evt);
			}
		});

		cancelButton.setText("Cancel");
		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelButtonActionPerformed(evt);
			}
		});

		viewRequestsButton.setText("Reject");
		viewRequestsButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				rejectButtonActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(29, 29, 29)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
								.addGroup(layout.createSequentialGroup().addComponent(cancelButton)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(viewRequestsButton)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(submitButton))
								.addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(notesLabel, javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(leaveRequestLabel, javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(typeOfLeaveLabel).addComponent(jLabel1)
												.addComponent(jLabel2))
										.addGap(99, 99, 99)
										.addGroup(layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
												.addComponent(typeOfLeaveDropdown, 0, 170, Short.MAX_VALUE)
												.addComponent(startDateField, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(endDateField, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
						.addContainerGap(29, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(32, 32, 32).addComponent(leaveRequestLabel)
						.addGap(39, 39, 39)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(typeOfLeaveLabel)
								.addComponent(typeOfLeaveDropdown, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1)
								.addComponent(startDateField, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jLabel2).addComponent(endDateField,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18).addComponent(notesLabel)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 95,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(31, 31, 31)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(submitButton).addComponent(cancelButton).addComponent(viewRequestsButton))
						.addContainerGap(32, Short.MAX_VALUE)));

		pack();

		// Put the window in the middle
		setLocationRelativeTo(null);
	}

	private void rejectButtonActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			// Add debugging information
			System.out.println("Attempting to reject leave request with ID: " + leaveRequest.getId());
			System.out.println("Leave request details: Employee=" + leaveRequest.getEmployeeNum() + 
							   ", Name=" + leaveRequest.getFirstName() + " " + leaveRequest.getLastName());
			
			// Validate leave request data
			if (leaveRequest == null) {
				JOptionPane.showMessageDialog(this, 
					"Error: Leave request data is not available.", 
					"Data Error", 
					JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if (leaveRequest.getId() == null || leaveRequest.getId().trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, 
					"Error: Leave request ID is missing.", 
					"Data Error", 
					JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// Check if the leave request is already processed
			String currentStatus = leaveRequest.isApproved();
			if ("Approved".equals(currentStatus) || "Rejected".equals(currentStatus)) {
				int choice = JOptionPane.showConfirmDialog(this, 
					"This leave request is already " + currentStatus.toLowerCase() + 
					". Do you want to change it to Rejected?", 
					"Status Change Confirmation", 
					JOptionPane.YES_NO_OPTION);
				
				if (choice != JOptionPane.YES_OPTION) {
					return;
				}
			}
			
			// Update leave request status to "Rejected" in the database
			boolean success = LeaveRequestDAO.updateLeaveRequestStatus(leaveRequest.getId(), "Rejected");
			
			if (success) {
				// Update the local object as well
				leaveRequest.setApproved("Rejected");
				
				JOptionPane.showMessageDialog(this, 
					"Leave request has been rejected successfully!", 
					"Success", 
					JOptionPane.INFORMATION_MESSAGE);
				
				// Go back to the leave request list page
				java.awt.EventQueue.invokeLater(new Runnable() {
					public void run() {
						dispose();
						try {
							new LeaveRequestListPage(employeeGI, employeeComp).setVisible(true);
						} catch (ParseException e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(null, 
								"Error navigating back to list: " + e.getMessage(), 
								"Navigation Error", 
								JOptionPane.ERROR_MESSAGE);
						}
					}
				});
			} else {
				// More detailed error message
				String errorMsg = "Failed to reject leave request. Possible causes:\n" +
								"- Database connection issue\n" +
								"- Leave request not found in database\n" +
								"- Database constraint violation\n\n" +
								"Please check the console for detailed error messages.";
				
				JOptionPane.showMessageDialog(this, 
					errorMsg, 
					"Database Error", 
					JOptionPane.ERROR_MESSAGE);
				
				System.err.println("Failed to update leave request status for ID: " + leaveRequest.getId());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, 
				"An unexpected error occurred: " + e.getMessage() + 
				"\n\nPlease check the console for detailed error information.", 
				"System Error", 
				JOptionPane.ERROR_MESSAGE);
		}
	}

	private void approveButtonActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			// Add debugging information
			System.out.println("Attempting to approve leave request with ID: " + leaveRequest.getId());
			System.out.println("Leave request details: Employee=" + leaveRequest.getEmployeeNum() + 
							   ", Name=" + leaveRequest.getFirstName() + " " + leaveRequest.getLastName());
			
			// Validate leave request data
			if (leaveRequest == null) {
				JOptionPane.showMessageDialog(this, 
					"Error: Leave request data is not available.", 
					"Data Error", 
					JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if (leaveRequest.getId() == null || leaveRequest.getId().trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, 
					"Error: Leave request ID is missing.", 
					"Data Error", 
					JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// Check if the leave request is already processed
			String currentStatus = leaveRequest.isApproved();
			if ("Approved".equals(currentStatus) || "Rejected".equals(currentStatus)) {
				int choice = JOptionPane.showConfirmDialog(this, 
					"This leave request is already " + currentStatus.toLowerCase() + 
					". Do you want to change it to Approved?", 
					"Status Change Confirmation", 
					JOptionPane.YES_NO_OPTION);
				
				if (choice != JOptionPane.YES_OPTION) {
					return;
				}
			}
			
			// Update leave request status to "Approved" in the database
			boolean success = LeaveRequestDAO.updateLeaveRequestStatus(leaveRequest.getId(), "Approved");
			
			if (success) {
				// Update the local object as well
				leaveRequest.setApproved("Approved");
				
				JOptionPane.showMessageDialog(this, 
					"Leave request has been approved successfully!", 
					"Success", 
					JOptionPane.INFORMATION_MESSAGE);
				
				// Go back to the leave request list page
				java.awt.EventQueue.invokeLater(new Runnable() {
					public void run() {
						dispose();
						try {
							new LeaveRequestListPage(employeeGI, employeeComp).setVisible(true);
						} catch (ParseException e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(null, 
								"Error navigating back to list: " + e.getMessage(), 
								"Navigation Error", 
								JOptionPane.ERROR_MESSAGE);
						}
					}
				});
			} else {
				// More detailed error message
				String errorMsg = "Failed to approve leave request. Possible causes:\n" +
								"- Database connection issue\n" +
								"- Leave request not found in database\n" +
								"- Database constraint violation\n\n" +
								"Please check the console for detailed error messages.";
				
				JOptionPane.showMessageDialog(this, 
					errorMsg, 
					"Database Error", 
					JOptionPane.ERROR_MESSAGE);
				
				System.err.println("Failed to update leave request status for ID: " + leaveRequest.getId());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, 
				"An unexpected error occurred: " + e.getMessage() + 
				"\n\nPlease check the console for detailed error information.", 
				"System Error", 
				JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				dispose();
				try {
					new LeaveRequestListPage(employeeGI, employeeComp).setVisible(true);
				} catch (ParseException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, 
						"Error navigating back to list: " + e.getMessage(), 
						"Navigation Error", 
						JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}
