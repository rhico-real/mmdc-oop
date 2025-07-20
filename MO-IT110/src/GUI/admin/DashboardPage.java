package GUI.admin;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import javax.swing.*;

import DAO.EmployeeDAO;
import DAO.LeaveRequestDAO;

import Classes.Compensation;
import Classes.EmployeeInformation;
import Classes.GovernmentIdentification;
import GUI.LoginPage;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Image;



@SuppressWarnings("serial")
public class DashboardPage extends JFrame {

	private JLabel employeeId = new JLabel("Search ID");

	// Last Name
	private JLabel lastName = new JLabel("Last Name:");
	private JLabel lastNameValue = new JLabel("");

	// First Name
	private JLabel firstName = new JLabel("First Name:");
	private JLabel firstNameValue = new JLabel("");

	// Birthday
	private JLabel birthday = new JLabel("Birthday:");
	private JLabel birthdayValue = new JLabel("");

	// Address
	private JLabel address = new JLabel("Address:");
	private JLabel addressValue = new JLabel("");

	// Phone Number
	private JLabel phoneNumber = new JLabel("Phone Number:");
	private JLabel phoneNumberValue = new JLabel("");

	// SSS Number
	private JLabel sssNumber = new JLabel("SSS Number:");
	private JLabel sssNumberValue = new JLabel("");

	// PhilHealth Number
	private JLabel philhealthNumber = new JLabel("PhilHealth Number:");
	private JLabel philhealthNumberValue = new JLabel("");

	// TIN Number
	private JLabel tinNumber = new JLabel("TIN Number:");
	private JLabel tinNumberValue = new JLabel("");

	// Pag-IBIG Number
	private JLabel pagibigNumber = new JLabel("Pag-IBIG Number:");
	private JLabel pagibigNumberValue = new JLabel("");

	// Status
	private JLabel status = new JLabel("Status:");
	private JLabel statusValue = new JLabel("");

	// Position
	private JLabel position = new JLabel("Position:");
	private JLabel positionValue = new JLabel("");

	// Immediate Supervisor
	private JLabel immediateSupervisor = new JLabel("Immediate Supervisor:");
	private JLabel immediateSupervisorValue = new JLabel("");

	// Hourly Rate
	private JLabel hourlyRate = new JLabel("Hourly Rate:");
	private JLabel hourlyRateValue = new JLabel("");
	
	private String navyblueColor = "#153969";
	private String lightgrayColor = "#eeeeee";
	private JLabel fullName = new JLabel("Name"); ;

	// Interactibles
	private JTextField employeeIdField = new JTextField(30);
	private JButton searchButton = new JButton("Search");
	private JButton computeButton = new JButton("Compute Salary");
	private JButton leaveRequestButton = new JButton("Leave Requests");
	private JButton updateRequestsButton = new JButton("Update Requests");
	private JButton employeeListButton = new JButton("Employee List");
	private JButton logoutButton = new JButton("Log Out");;
	private JLabel[] labels = { lastNameValue, firstNameValue, birthdayValue, addressValue, phoneNumberValue,
			sssNumberValue, philhealthNumberValue, tinNumberValue, pagibigNumberValue, statusValue, positionValue,
			immediateSupervisorValue, hourlyRateValue };
	

	// Instantiate two of the user's important information
	GovernmentIdentification employeeGI = new GovernmentIdentification(employeeIdField.getText());
	Compensation employeeComp = new Compensation(employeeIdField.getText());

	// Panels
	private javax.swing.JPanel mainPanelLayout;
	private javax.swing.JPanel menubarPanel;
	private javax.swing.JPanel searchPanel;
	private javax.swing.JPanel contentPanel;
	private javax.swing.JPanel sidebarPanel;
	private javax.swing.JPanel sidebarButtons;
    private javax.swing.JPanel dashboardPanel;
	
	// Make corners of panels rounded
	private RoundedPanel fullNamePanel = new RoundedPanel(30);
	private RoundedPanel profilePicPanel = new RoundedPanel(30);
	private RoundedPanel employmentPanel = new RoundedPanel(30);
	private RoundedPanel positionPanel = new RoundedPanel(30);
	private RoundedPanel governmentInfoPanel = new RoundedPanel(30);
	private RoundedPanel addressPanel = new RoundedPanel(30);
	private RoundedPanel allowancesPanel = new RoundedPanel(30);

	/**
	 * Creates new form NewJFrame
	 */
	public DashboardPage() {
		initComponents();
	}
	
	// import poppins font
	private static Font loadCustomFont(String fontPath, float size) {
	    try {
	        Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath)).deriveFont(size);
	        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	        ge.registerFont(font);
	        return font;
	    } catch (FontFormatException | IOException e) {
	        System.err.println("Error loading font: " + e.getMessage());
	        return null;
	    }
	}
	
	
	// Rounded Panel Function Class
	static class RoundedPanel extends JPanel {
        private int cornerRadius;

        public RoundedPanel(int radius) {
            this.cornerRadius = radius;
            setOpaque(false); // Make background transparent
        }

        @Override
        protected void paintComponent(Graphics g) {
            // Enable anti-aliasing for smoother curves
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw filled rounded rectangle with background color
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

            g2.dispose();
            super.paintComponent(g);
        }
    }

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// @SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {
		// custom color
		String navyBlue = "#153969";
        String lightGray = "#f5f5f5";
        
        // main panel layout -----------------------------------------------------------------------------------------
		
		mainPanelLayout = new JPanel(new BorderLayout());
		mainPanelLayout.setBackground(Color.decode(lightGray));
		
		// menubar panel ---------------------------------------------------------------------------------------------

		menubarPanel = new JPanel(new BorderLayout());
		menubarPanel.setBackground(Color.decode(navyBlue));
		menubarPanel.setBorder(new EmptyBorder(-10, 0, -10, 0));
		mainPanelLayout.add(menubarPanel, BorderLayout.NORTH);
		
			// motorph logo
			ImageIcon motorphlogoAdmin = new ImageIcon("resources/images/motorph-logo-white.png");
	        JLabel motorPHLogo = new JLabel(motorphlogoAdmin);
	        motorPHLogo.setBorder(new EmptyBorder(-7, -10, 0, 0));
	        menubarPanel.add(motorPHLogo, BorderLayout.WEST);
		
			// search panel
			searchPanel = new JPanel(new GridBagLayout());
			searchPanel.setBackground(Color.decode(navyBlue));
			menubarPanel.add(searchPanel, BorderLayout.CENTER);
				
				// search label
				employeeId.setFont(FontLoader.poppinsSearchLabel);
				employeeId.setForeground(Color.GRAY);
				GridBagConstraints gbc_employeeId = new GridBagConstraints();
				gbc_employeeId.gridx = 1;
				gbc_employeeId.gridy = 0;
				gbc_employeeId.anchor = GridBagConstraints.WEST;
				gbc_employeeId.insets = new Insets(0, 18, 0, 100);
				searchPanel.add(employeeId, gbc_employeeId);

				// search button
				ImageIcon searchButtonImage = new ImageIcon("resources/images/search-button-small.png");
				JButton searchButton = new JButton(searchButtonImage);
				searchButton.setBorder(null);
		        searchButton.setContentAreaFilled(false); 
		        searchButton.setFocusPainted(false);
				GridBagConstraints gbc_searchButton = new GridBagConstraints();
				gbc_searchButton.gridx = 1;
				gbc_searchButton.gridy = 0;
				gbc_searchButton.insets = new Insets (0,0,5,0);
				gbc_searchButton.anchor = GridBagConstraints.EAST;
				searchPanel.add(searchButton, gbc_searchButton);
				
				// search field
				Border outerBorder = new LineBorder(Color.decode(navyBlue), 0, true); // outer border
				Border innerPadding = new EmptyBorder(5, 9, 5, 10); // inner border
				employeeIdField.setBorder(new CompoundBorder(outerBorder, innerPadding));
				employeeIdField.setFont(FontLoader.poppinsSearchLabel);
				employeeIdField.setBackground(Color.WHITE);
				GridBagConstraints gbc_employeeIdField = new GridBagConstraints();
				gbc_employeeIdField.gridx = 1;
				gbc_employeeIdField.gridy = 0;
				searchPanel.add(employeeIdField, gbc_employeeIdField);
			
			
			// admin logo
	        ImageIcon admindisplayLogo = new ImageIcon("resources/images/Admin-Logo.png");
	        JLabel adminLogo = new JLabel(admindisplayLogo);
	        adminLogo.setBorder(new EmptyBorder(-3, 0, 0, 0));
			menubarPanel.add(adminLogo, BorderLayout.EAST);
		
		// content panel ---------------------------------------------------------------------------------------------        
			
		contentPanel = new JPanel(new BorderLayout());	
		mainPanelLayout.add(contentPanel, BorderLayout.CENTER);
				
				// sidebar panel
				sidebarPanel = new JPanel(new BorderLayout());
				sidebarPanel.setBorder(new EmptyBorder(0, 12, 0, 12));
				sidebarPanel.setBackground(Color.WHITE);
		        contentPanel.add(sidebarPanel, BorderLayout.WEST);
		        	
		        	// side bar button panel
		        	sidebarButtons = new JPanel (new GridBagLayout());
		        	sidebarButtons.setBackground(Color.WHITE);
		        	sidebarPanel.add(sidebarButtons, BorderLayout.NORTH);
		        		
		        		// compute button
			        	ImageIcon computeButtonImage = new ImageIcon("resources/images/admin/admin-compute-button.png");
			        	JButton computeButton = new JButton(computeButtonImage);
			        	computeButton.setBorder(null);
			        	computeButton.setContentAreaFilled(false); 
			        	computeButton.setFocusPainted(false);
			        	GridBagConstraints gbc_computeButton = new GridBagConstraints();
			        	gbc_computeButton.gridx = 0;
			        	gbc_computeButton.gridy = 0;
			        	gbc_computeButton.insets = new Insets(20,0,0,0);
			        	sidebarButtons.add(computeButton, gbc_computeButton);
			        	
			        	computeButton.addActionListener(new java.awt.event.ActionListener() {
							public void actionPerformed(java.awt.event.ActionEvent evt) {
								computeButtonActionPerformed(evt);
							}
						});
		        	
			        	// leave request button
			        	ImageIcon leaveRequestButtonImage = new ImageIcon("resources/images/admin/admin-leave-request-button.png");
			        	JButton leaveRequestButton = new JButton(leaveRequestButtonImage);
			        	leaveRequestButton.setBorder(null);
			        	leaveRequestButton.setContentAreaFilled(false); 
			        	leaveRequestButton.setFocusPainted(false);
			        	GridBagConstraints gbc_leaveRequestButton = new GridBagConstraints();
						gbc_leaveRequestButton.gridx = 0;
						gbc_leaveRequestButton.gridy = 1;
						gbc_leaveRequestButton.insets = new Insets(30,0,0,0);
						sidebarButtons.add(leaveRequestButton, gbc_leaveRequestButton);
						
						leaveRequestButton.addActionListener(new java.awt.event.ActionListener() {
							public void actionPerformed(java.awt.event.ActionEvent evt) {
								leaveRequestButtonActionPerformed(evt);
							}
						});
			        	
			        	// update request button
			        	ImageIcon updateRequestsButtonImage = new ImageIcon("resources/images/admin/admin-update-request-button.png");
			        	JButton updateRequestsButton = new JButton(updateRequestsButtonImage);
			        	updateRequestsButton.setBorder(null);
			        	updateRequestsButton.setContentAreaFilled(false); 
			        	updateRequestsButton.setFocusPainted(false);
			        	GridBagConstraints gbc_updateRequestsButton = new GridBagConstraints();
						gbc_updateRequestsButton.gridx = 0;
						gbc_updateRequestsButton.gridy = 2;
						gbc_updateRequestsButton.insets = new Insets(30,0,0,0);
						sidebarButtons.add(updateRequestsButton, gbc_updateRequestsButton);
			        	
						updateRequestsButton.addActionListener(new java.awt.event.ActionListener() {
				            public void actionPerformed(java.awt.event.ActionEvent evt) {
				                updateRequestsButtonActionPerformed(evt);
				            }
				        });
						
						// employee list button
						ImageIcon employeeListButtonImage = new ImageIcon("resources/images/admin/admin-employee-list-button.png");
						JButton employeeListButton = new JButton(employeeListButtonImage);
						employeeListButton.setBorder(null);
						employeeListButton.setContentAreaFilled(false); 
						employeeListButton.setFocusPainted(false);
						GridBagConstraints gbc_employeeListButton = new GridBagConstraints();
						gbc_employeeListButton.gridx = 0;
						gbc_employeeListButton.gridy = 3;
						gbc_employeeListButton.insets = new Insets(30,0,0,0);
						sidebarButtons.add(employeeListButton, gbc_employeeListButton);
						
						employeeListButton.addActionListener(new java.awt.event.ActionListener() {
							public void actionPerformed(java.awt.event.ActionEvent evt) {
								employeeListButtonActionPerformed(evt);
							}
						});
			        	
					// logout button
					ImageIcon logoutButtonImage = new ImageIcon("resources/images/admin/admin-logout-button.png");
					JButton logoutButton = new JButton(logoutButtonImage);
					logoutButton.setBorder(null);
					logoutButton.setContentAreaFilled(false); 
					logoutButton.setFocusPainted(false);
					logoutButton.setBorder(new EmptyBorder(0, -25, 5, 0));
					sidebarPanel.add(logoutButton, BorderLayout.SOUTH);
					
					logoutButton.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent evt) {
							logoutButtonActionPerformed(evt);
						}
					});
					
		// dashboard card layout panel
		CardLayout dashboardCardLayout = new CardLayout();
		JPanel dashboardCardLayoutPanel = new JPanel(dashboardCardLayout);
		contentPanel.add(dashboardCardLayoutPanel, BorderLayout.CENTER);
		
		// instruction screen for dashboard
		JPanel instructionScreenPanel = createInstructionScreen();
		JPanel dashboardResultScreen = createDashboardResultScreen();
		
		// assign panels for card layout panel
		dashboardCardLayoutPanel.add(instructionScreenPanel, "instruction");
		dashboardCardLayoutPanel.add(dashboardResultScreen, "result");
		
		// employee field trigger listener
		employeeIdField.addActionListener(e -> {
		    employeeIdFieldActionPerformed(e); // event 1
		    dashboardCardLayout.show(dashboardCardLayoutPanel, "result"); // event 2
		});
		
		// search button field trigger listener
		searchButton.addActionListener(e -> {
			searchButtonActionPerformed(e); // event 1
		    dashboardCardLayout.show(dashboardCardLayoutPanel, "result"); // event 2
		});
		
		
		// taskbar icon
		ImageIcon taskbarImage = new ImageIcon("resources/images/motorph-taskbar-image.png");
		setIconImage(taskbarImage.getImage());
		
		// JFrame setup
   		setTitle("MotorPH Payroll System | Dashboard");
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);   
		add(mainPanelLayout);
		setVisible(true);
		pack();
		setSize(1366,788);
		setLocationRelativeTo(null);

	}
	
	private JPanel createDashboardResultScreen() {
		JPanel dashboardPanel = new JPanel(new GridBagLayout());
		dashboardPanel.setBackground(Color.decode("#f5f5f5"));
			
			//full name panel
			fullNamePanel.setBackground(Color.WHITE);
			GridBagConstraints gbc_fullNamePanel = new GridBagConstraints();
			gbc_fullNamePanel.gridx = 0;
			gbc_fullNamePanel.gridy = 0;
			gbc_fullNamePanel.gridwidth= 2;
			gbc_fullNamePanel.gridheight= 1;
			gbc_fullNamePanel.fill = GridBagConstraints.BOTH;
			gbc_fullNamePanel.insets = new Insets (0,0,10,10);
			dashboardPanel.add(fullNamePanel, gbc_fullNamePanel);
			
			// profile picture panel
			profilePicPanel.setBackground(Color.WHITE);
			GridBagConstraints gbc_profilePicPanel = new GridBagConstraints();
			gbc_profilePicPanel.gridx = 2;
			gbc_profilePicPanel.gridy = 0;
			gbc_profilePicPanel.gridwidth= 1;
			gbc_profilePicPanel.gridheight= 2;
			gbc_profilePicPanel.fill = GridBagConstraints.BOTH;
			gbc_profilePicPanel.insets = new Insets (0,10,10,0);
			dashboardPanel.add(profilePicPanel, gbc_profilePicPanel);
			
			// employment panel
			employmentPanel.setBackground(Color.WHITE);
			GridBagConstraints gbc_employmentPanel = new GridBagConstraints();
			gbc_employmentPanel.gridx = 0;
			gbc_employmentPanel.gridy = 1;
			gbc_employmentPanel.gridwidth= 2;
			gbc_employmentPanel.gridheight= 1;
			gbc_employmentPanel.fill = GridBagConstraints.BOTH;
			gbc_employmentPanel.insets = new Insets (10,0,10,10);
			dashboardPanel.add(employmentPanel, gbc_employmentPanel);
			
			// position panel
			positionPanel.setBackground(Color.WHITE);
			GridBagConstraints gbc_positionPanel = new GridBagConstraints();
			gbc_positionPanel.gridx = 0;
			gbc_positionPanel.gridy = 2;
			gbc_positionPanel.gridwidth= 1;
			gbc_positionPanel.gridheight= 1;
			gbc_positionPanel.fill = GridBagConstraints.BOTH;
			gbc_positionPanel.insets = new Insets (10,0,10,10);
			dashboardPanel.add(positionPanel, gbc_positionPanel);		
			
			// government information panel
			governmentInfoPanel.setBackground(Color.WHITE);
			GridBagConstraints gbc_governmentInfoPanel = new GridBagConstraints();
			gbc_governmentInfoPanel.gridx = 1;
			gbc_governmentInfoPanel.gridy = 2;
			gbc_governmentInfoPanel.gridwidth= 1;
			gbc_governmentInfoPanel.gridheight= 1;
			gbc_governmentInfoPanel.fill = GridBagConstraints.BOTH;
			gbc_governmentInfoPanel.insets = new Insets (10,10,10,10);
			dashboardPanel.add(governmentInfoPanel, gbc_governmentInfoPanel);	
			
			// address panel
			addressPanel.setBackground(Color.WHITE);
			GridBagConstraints gbc_addressPanel = new GridBagConstraints();
			gbc_addressPanel.gridx = 0;
			gbc_addressPanel.gridy = 3;
			gbc_addressPanel.gridwidth= 2;
			gbc_addressPanel.gridheight= 1;
			gbc_addressPanel.fill = GridBagConstraints.BOTH;
			gbc_addressPanel.insets = new Insets (10,0,0,10);
			dashboardPanel.add(addressPanel, gbc_addressPanel);	
			
			// allowances panel
			allowancesPanel.setBackground(Color.WHITE);
			GridBagConstraints gbc_allowancesPanel = new GridBagConstraints();
			gbc_allowancesPanel.gridx = 2;
			gbc_allowancesPanel.gridy = 2;
			gbc_allowancesPanel.gridwidth= 1;
			gbc_allowancesPanel.gridheight= 2;
			gbc_allowancesPanel.fill = GridBagConstraints.BOTH;
			gbc_allowancesPanel.insets = new Insets (10,10,0,0);
			dashboardPanel.add(allowancesPanel, gbc_allowancesPanel);	
			
			// Group Layout --------------------------------------------------------------------------			
			
			// fullNamePanel group layout
			firstNameValue.setFont(FontLoader.poppinsRegular45f); 
			firstNameValue.setText(" ");
			
			lastNameValue.setFont(FontLoader.poppinsRegular45f); 
			lastNameValue.setText(" ");
			
			positionValue.setFont(FontLoader.poppinsRegular15f);  
			positionValue.setText(" ");
			
			GroupLayout fullNamePanelGL = new GroupLayout(fullNamePanel);
			fullNamePanel.setLayout(fullNamePanelGL);
			
			fullNamePanelGL.setHorizontalGroup(
					fullNamePanelGL.createSequentialGroup()
					.addGap(50)
			        .addGroup(fullNamePanelGL.createParallelGroup(GroupLayout.Alignment.LEADING)
			            .addComponent(firstNameValue)
			        		)
			        .addGap(15)
			        .addGroup(fullNamePanelGL.createParallelGroup(GroupLayout.Alignment.LEADING)
				            .addComponent(lastNameValue)
			        		)
			);
			
			fullNamePanelGL.setVerticalGroup(
				    fullNamePanelGL.createSequentialGroup()
				        .addGap(50)
				        .addGroup(fullNamePanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
				            .addComponent(firstNameValue)
				            .addComponent(lastNameValue)
				        )
				        .addGap(50)
				);	
			
			// profilePicPanel group layout
			ImageIcon empprofilePhoto = new ImageIcon("resources/images/profile-pic-emp.png");
			JLabel empPhoto = new JLabel(empprofilePhoto);
			
			GroupLayout empPhotoGL = new GroupLayout(profilePicPanel);
			profilePicPanel.setLayout(empPhotoGL);
			
			empPhotoGL.setHorizontalGroup(
					empPhotoGL.createSequentialGroup()
			        .addGroup(empPhotoGL.createParallelGroup(GroupLayout.Alignment.LEADING)
			            .addComponent(empPhoto)
			            	)
			);
			
			empPhotoGL.setVerticalGroup(
					empPhotoGL.createSequentialGroup()
			        .addGroup(empPhotoGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
			            .addComponent(empPhoto)
			        		)
			);	
			
			//employmentPanel group layout
			status.setFont(FontLoader.poppinsSemiBold20f); 
			status.setForeground(Color.decode(navyblueColor));
			status.setText("Status");
			statusValue.setFont(FontLoader.poppinsRegular15f);; 
			statusValue.setText(" ");
			
			hourlyRate.setFont(FontLoader.poppinsSemiBold20f);  
			hourlyRate.setForeground(Color.decode(navyblueColor));
			hourlyRate.setText("Hourly Rate");
			hourlyRateValue.setFont(FontLoader.poppinsRegular15f);  
			hourlyRateValue.setText(" ");
			
			phoneNumber.setFont(FontLoader.poppinsSemiBold20f);  
			phoneNumber.setForeground(Color.decode(navyblueColor));
			phoneNumber.setText("Phone Number");
			phoneNumberValue.setFont(FontLoader.poppinsRegular15f); 
			phoneNumberValue.setText(" ");		
			
			birthday.setFont(FontLoader.poppinsSemiBold20f); 
			birthday.setForeground(Color.decode(navyblueColor));
			birthday.setText("Birthday");
			birthdayValue.setFont(FontLoader.poppinsRegular15f);
			birthdayValue.setText(" ");
				
			GroupLayout employmentPanelGL = new GroupLayout(employmentPanel);
			employmentPanel.setLayout(employmentPanelGL);
			
			employmentPanelGL.setHorizontalGroup(
					employmentPanelGL.createSequentialGroup()
					.addGap(20)
			        .addGroup(employmentPanelGL.createParallelGroup(GroupLayout.Alignment.LEADING)
			            .addComponent(status)
			            .addComponent(statusValue)
			        		)
			        .addGap(75)
			        .addGroup(employmentPanelGL.createParallelGroup(GroupLayout.Alignment.LEADING)
				            .addComponent(hourlyRate)
				            .addComponent(hourlyRateValue)
			        		)
			        .addGap(75)
			        .addGroup(employmentPanelGL.createParallelGroup(GroupLayout.Alignment.LEADING)
			        		.addComponent(phoneNumber)
				            .addComponent(phoneNumberValue)
			        		)
			        .addGap(75)
			        .addGroup(employmentPanelGL.createParallelGroup(GroupLayout.Alignment.LEADING)
			        		.addComponent(birthday)
				            .addComponent(birthdayValue)
			        		)
			);
			
			employmentPanelGL.setVerticalGroup(
					employmentPanelGL.createSequentialGroup()
					.addGap(20)
			        .addGroup(employmentPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
			            .addComponent(status)
			            .addComponent(hourlyRate)
			            .addComponent(phoneNumber)
			            .addComponent(birthday)
			        		)
			        .addGroup(employmentPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
			    		.addComponent(statusValue)
			            .addComponent(hourlyRateValue)
			            .addComponent(phoneNumberValue)
			            .addComponent(birthdayValue)
			        		)
			        .addGap(20)
			);	
			
			// positionPanel group layout
			position.setFont(FontLoader.poppinsSemiBold20f); 
			position.setForeground(Color.decode(navyblueColor));
			position.setText("Position");
			positionValue.setFont(FontLoader.poppinsRegular15f);  
			positionValue.setText(" ");
			
			immediateSupervisor.setFont(FontLoader.poppinsSemiBold20f);  
			immediateSupervisor.setForeground(Color.decode(navyblueColor));
			immediateSupervisor.setText("Immediate Supervisor");
			immediateSupervisorValue.setFont(FontLoader.poppinsRegular15f);  
			immediateSupervisorValue.setText(" ");		
						
			GroupLayout positionPanelGL = new GroupLayout(positionPanel);
			positionPanel.setLayout(positionPanelGL);
			
			positionPanelGL.setHorizontalGroup(
					positionPanelGL.createSequentialGroup()
					.addGap(20)
			        .addGroup(positionPanelGL.createParallelGroup(GroupLayout.Alignment.LEADING)
			            .addComponent(position)
			            .addComponent(positionValue)
			            .addComponent(immediateSupervisor)
			            .addComponent(immediateSupervisorValue)
			            	)
			        .addGap(20)
			);
			
			positionPanelGL.setVerticalGroup(
					positionPanelGL.createSequentialGroup()
					.addGap(20)
			        .addGroup(positionPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
			    		.addComponent(position)
			            	)
			        .addGroup(positionPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
			        		.addComponent(positionValue)
			        		)
			        .addGap(20)
			        .addGroup(positionPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
			        		.addComponent(immediateSupervisor)
			        		)
			        .addGroup(positionPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
			        		.addComponent(immediateSupervisorValue)
			    			)   
			        .addGap(20)
			);
			
			// governmentInfoPanel group layout		
			sssNumber.setFont(FontLoader.poppinsSemiBold20f); 
			sssNumber.setForeground(Color.decode(navyblueColor));
			sssNumber.setText("SSS Number");
			sssNumberValue.setFont(FontLoader.poppinsRegular15f); 
			sssNumberValue.setText(" ");
			
			philhealthNumber.setFont(FontLoader.poppinsSemiBold20f);  
			philhealthNumber.setForeground(Color.decode(navyblueColor));
			philhealthNumber.setText("PhilHealth Number");
			philhealthNumberValue.setFont(FontLoader.poppinsRegular15f);
			philhealthNumberValue.setText(" ");
			
			pagibigNumber.setFont(FontLoader.poppinsSemiBold20f);  
			pagibigNumber.setForeground(Color.decode(navyblueColor));
			pagibigNumber.setText("Pag-ibig Number");
			pagibigNumberValue.setFont(FontLoader.poppinsRegular15f); 
			pagibigNumberValue.setText(" ");
			
			tinNumber.setFont(FontLoader.poppinsSemiBold20f); 
			tinNumber.setForeground(Color.decode(navyblueColor));
			tinNumber.setText("TIN Number");
			tinNumberValue.setFont(FontLoader.poppinsRegular15f);
			tinNumberValue.setText(" ");
			
			// set layout for government account numbers panel
			GroupLayout governmentInfoPanelGL = new GroupLayout(governmentInfoPanel);
			governmentInfoPanel.setLayout(governmentInfoPanelGL);
			
			governmentInfoPanelGL.setHorizontalGroup(
					governmentInfoPanelGL.createSequentialGroup()
					.addGap(20)
			        .addGroup(governmentInfoPanelGL.createParallelGroup(GroupLayout.Alignment.LEADING)
			            .addComponent(sssNumber)
			            .addComponent(sssNumberValue)
			            .addComponent(philhealthNumber)
			            .addComponent(philhealthNumberValue)
			            	)
			        .addGap(50)
			        .addGroup(governmentInfoPanelGL.createParallelGroup(GroupLayout.Alignment.LEADING)
			            .addComponent(tinNumber)
			            .addComponent(tinNumberValue)
			            .addComponent(pagibigNumber)
			            .addComponent(pagibigNumberValue)
				            )
			        .addGap(20)
			);
			
			governmentInfoPanelGL.setVerticalGroup(
					governmentInfoPanelGL.createSequentialGroup()
					.addGap(20)
			        .addGroup(governmentInfoPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
			    		.addComponent(sssNumber)
			            .addComponent(tinNumber)
			            	)
			        .addGroup(governmentInfoPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
			        		.addComponent(sssNumberValue)
				            .addComponent(tinNumberValue)
			    			) 
			        .addGap(20)
			        .addGroup(governmentInfoPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
			        		.addComponent(philhealthNumber)
				            .addComponent(pagibigNumber)
			    			) 
			        .addGroup(governmentInfoPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
			        		.addComponent(philhealthNumberValue)
				            .addComponent(pagibigNumberValue)
			    			) 
			        .addGap(20)
			);
			
			// address group layout
			
			// set font and colors for government account numbers panel
			address.setFont(FontLoader.poppinsSemiBold20f); 
			address.setForeground(Color.decode(navyblueColor));
			address.setText("Address");
			addressValue.setFont(FontLoader.poppinsRegular15f); 
			addressValue.setText(" ");
			
			// set layout for address panel
			GroupLayout addressPanelGL = new GroupLayout(addressPanel);
			addressPanel.setLayout(addressPanelGL);
			
			addressPanelGL.setHorizontalGroup(
					addressPanelGL.createSequentialGroup()
					.addGap(20)
			        .addGroup(addressPanelGL.createParallelGroup(GroupLayout.Alignment.LEADING)
			            .addComponent(address)
			            .addComponent(addressValue)
			        )
			        .addGap(20)
			);
			
			addressPanelGL.setVerticalGroup(
					addressPanelGL.createSequentialGroup()
					.addGap(20)
			        .addGroup(addressPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
			            .addComponent(address)
			        )
			        .addGroup(addressPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
			            .addComponent(addressValue)
			        )
			        .addGap(20)
			);
		
		return dashboardPanel;
	}
	
	private JPanel createInstructionScreen() {
	    JPanel panel = new JPanel(new BorderLayout());

	    // Load the image
	    ImageIcon originalIcon = new ImageIcon("resources/images/instruction-screen-image.png");
	    Image originalImage = originalIcon.getImage();

	    // Define target size (adjust as needed)
	    int targetWidth = 350;
	    int targetHeight = 350;

	    // Scale it with high quality
	    Image scaledImage = getScaledImage(originalImage, targetWidth, targetHeight);
	    ImageIcon scaledIcon = new ImageIcon(scaledImage);

	    // Set it to the label
	    JLabel searchInstructionLabel = new JLabel(scaledIcon);
	    searchInstructionLabel.setHorizontalAlignment(SwingConstants.CENTER);

	    panel.add(searchInstructionLabel, BorderLayout.CENTER);
	    return panel;
	}
	
	// render images smoothly and not pixelated
	private Image getScaledImage(Image srcImg, int w, int h) {
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();
	    return resizedImg;
	}
	

	private void employeeIdFieldActionPerformed(java.awt.event.ActionEvent evt) {
		// Get employee information from the database
		EmployeeInformation employee = EmployeeDAO.getEmployeeByNumber(employeeIdField.getText());
		
		// Set the label values with the employee data
		setLabelValues(employee);
	}

	private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// Get employee information from the database
		EmployeeInformation employee = EmployeeDAO.getEmployeeByNumber(employeeIdField.getText());
		
		// Set the label values with the employee data
		setLabelValues(employee);
	}

	private void computeButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
		openCalculator(employeeComp);
	}

	private void employeeListButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Remove the DashboardPage Window
				dispose();

				// Go to the employees list page
				new EmployeeListPage().setVisible(true);
			}
		});
	}

	private void leaveRequestButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// Check if there are any leave requests in the database
		if (LeaveRequestDAO.getAllLeaveRequests().isEmpty()) {
			// Display a message to the user
			JOptionPane.showMessageDialog(this, "No leave requests found.", "Empty Data",
					JOptionPane.INFORMATION_MESSAGE);

			return;
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Close the dashboard page
				dispose();

				// Log out
				try {
					new LeaveRequestListPage(employeeGI, employeeComp).setVisible(true);
				} catch (java.text.ParseException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error loading leave requests: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Close the dashboard page
				dispose();

				// Log out
				new LoginPage().setVisible(true);
			}
		});
	}

	private void updateRequestsButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// Navigate to the update requests page
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Close the dashboard page
				dispose();

				// Open the update requests page
				new UpdateRequestsPage().setVisible(true);
			}
		});
	}

	public Boolean checkForEmployee(EmployeeInformation employee) {
		// Return if employeeIdField is empty
		if (employeeIdField.getText().equals("")) {
			JOptionPane.showMessageDialog(new JFrame(""), "Please provide an employee number.", "No input detected",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		// Check if employee data is null
		if (employee == null) {
			JOptionPane.showMessageDialog(new JFrame(""), "No user found.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	public void setLabelValues(EmployeeInformation employee) {
		// Check if user exists
		if (!checkForEmployee(employee)) {
			return;
		}

		// Set the values to labels
		lastNameValue.setText(employee.getLastName());
		firstNameValue.setText(employee.getFirstName());
		birthdayValue.setText(employee.getBirthday());
		addressValue.setText(employee.getAddress());
		phoneNumberValue.setText(employee.getPhoneNumber());
		
		// Get government ID information
		GovernmentIdentification govId = EmployeeDAO.getEmployeeGovId(employee.getEmployeeNumber());
		sssNumberValue.setText(govId.getSSSNumber());
		philhealthNumberValue.setText(govId.getPhilHealthNumber());
		tinNumberValue.setText(govId.getTinNumber());
		pagibigNumberValue.setText(govId.getPagibigNumber());
		
		// Set employee status and position
		statusValue.setText(employee.getStatus());
		positionValue.setText(employee.getPosition());
		immediateSupervisorValue.setText(employee.getSupervisor());
		
		// Set hourly rate
		hourlyRateValue.setText(String.valueOf(employee.getHourlyRate()));

		// Set the employeeData to the employeeComp and employeeGI objects
		EmployeeInformation.setEmployeeInformationObject(employeeIdField.getText(), employeeGI, employeeComp);

		// Let user click the compute button if user exists
		computeButton.setEnabled(true);
	}

	public void openCalculator(Compensation employeeComp) {
		// Open the calculator
		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new CalculatorPage(employeeComp).setVisible(true);
			}
		});
	}

	public void setEmployeeInformationObject(String employeeNumber) {
		// Get employee data from database
		GovernmentIdentification employeeGovInfo = EmployeeDAO.getEmployeeGovId(employeeNumber);
		Compensation employeeCompInfo = EmployeeDAO.getEmployeeCompensation(employeeNumber);

		// Set Government Identification data of Employee
		employeeGI.setSSSNumber(employeeGovInfo.getSSSNumber());
		employeeGI.setPhilHealthNumber(employeeGovInfo.getPhilHealthNumber());
		employeeGI.setPagibigNumber(employeeGovInfo.getPagibigNumber());
		employeeGI.setTinNumber(employeeGovInfo.getTinNumber());

		// Set Compensation data of Employee
		employeeComp.setBasicSalary(employeeCompInfo.getBasicSalary());
		employeeComp.setClothingAllowance(employeeCompInfo.getClothingAllowance());
		employeeComp.setGrossSemiMonthlyRate(employeeCompInfo.getGrossSemiMonthlyRate());
		employeeComp.setPhoneAllowance(employeeCompInfo.getPhoneAllowance());
		employeeComp.setRiceSubsidy(employeeCompInfo.getRiceSubsidy());
		employeeComp.setHourlyRate(employeeCompInfo.getHourlyRate());
	}
	
	private class FontLoader {

        // Public static font variable (accessible from anywhere)
        public static final Font poppinsRegular45f = loadCustomFont("resources/fonts/Poppins-Regular.ttf", 50f);
        public static final Font poppinsSearchLabel = loadCustomFont("resources/fonts/Poppins-Regular.ttf", 15f);
        public static final Font poppinsRegular15f = loadCustomFont("resources/fonts/Poppins-Regular.ttf", 20f);
        public static final Font poppinsSemiBold20f = loadCustomFont("resources/fonts/Poppins-SemiBold.ttf", 20f);

        // Font loading utility
        private static Font loadCustomFont(String fontPath, float size) {
            try {
                Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath)).deriveFont(size);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(font);
                return font;
            } catch (FontFormatException | IOException e) {
                System.err.println("Error loading font: " + e.getMessage());
                return new Font("SansSerif", Font.PLAIN, (int) size); // fallback font
            }
        }
    }

}
