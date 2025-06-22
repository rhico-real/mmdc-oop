package GUI.admin;

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


@SuppressWarnings("serial")
public class DashboardPage extends JFrame {

	private JLabel employeeId = new JLabel("Employee ID:");

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
	private JButton employeeListButton = new JButton("Employee List");
	private JButton leaveRequestButton = new JButton("Leave Requests");
	private JButton updateRequestsButton = new JButton("Update Requests");
	private JButton logoutButton = new JButton("Log Out");;
	private JLabel[] labels = { lastNameValue, firstNameValue, birthdayValue, addressValue, phoneNumberValue,
			sssNumberValue, philhealthNumberValue, tinNumberValue, pagibigNumberValue, statusValue, positionValue,
			immediateSupervisorValue, hourlyRateValue };
	

	// Instantiate two of the user's important information
	GovernmentIdentification employeeGI = new GovernmentIdentification(employeeIdField.getText());
	Compensation employeeComp = new Compensation(employeeIdField.getText());

	// Panels
	private javax.swing.JPanel mainPanel;
	private javax.swing.JPanel menubarPanel;
	private javax.swing.JPanel sidebarPanel;
    private javax.swing.JPanel dashboardPanel;
    
    private javax.swing.JPanel leftDashboardPanel;
    private javax.swing.JPanel profilepicturePanel;
    
    private javax.swing.JPanel rightDashboardPanel;
	private javax.swing.JPanel primaryinfoPanel;
	private javax.swing.JPanel fullnamePanel;
	private javax.swing.JPanel otherprimaryinfoPanel;
	
	// Make corners of panels rounded
	private RoundedPanel positioninfoPanel = new RoundedPanel(30);
	private RoundedPanel govtnumbersPanel = new RoundedPanel(30);
	private RoundedPanel addressPanel = new RoundedPanel(30);

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
		// assign fonts
		
		Font poppinsFullNameLabelFont = loadCustomFont("resources/fonts/Poppins-SemiBold.ttf", 30f);
		Font poppinsFullNameOutputFont = loadCustomFont("resources/fonts/Poppins-Regular.ttf", 50f);
		Font poppinsText = loadCustomFont("resources/fonts/Poppins-Regular.ttf", 14f);
		Font poppinsButtonsFont = loadCustomFont("resources/fonts/Poppins-Bold.ttf", 16f);
		Font poppinsFieldNameFont = loadCustomFont("resources/fonts/Poppins-SemiBold.ttf", 18f);
		Font poppinsFieldOutputFont = loadCustomFont("resources/fonts/Poppins-Regular.ttf", 18f);
		
		// Grid Layout for the entire window
		GridBagConstraints windowGBC = new GridBagConstraints();
		// MAIN PANEL
		JPanel mainPanelLayout = new JPanel(new GridBagLayout());

		// MENUBAR PANEL 
		menubarPanel = new JPanel(new GridBagLayout());
		menubarPanel.setBackground(Color.decode("#153969"));
		GridBagConstraints menubarGBC = new GridBagConstraints();
		menubarGBC.gridx = 0;
		menubarGBC.gridy = 0;
		menubarGBC.gridwidth = GridBagConstraints.REMAINDER;
		menubarGBC.gridheight = 1;
		menubarGBC.fill = GridBagConstraints.BOTH;
		menubarGBC.weightx = 1;
		menubarGBC.weighty = 0;
		mainPanelLayout.add(menubarPanel, menubarGBC);

		// SIDEBAR PANEL 
		sidebarPanel = new JPanel(new GridBagLayout());
		sidebarPanel.setBackground(Color.decode("#a4a4a4"));
		GridBagConstraints sidebarGBC = new GridBagConstraints();
		sidebarGBC.gridx = 0;
		sidebarGBC.gridy = 1;
		sidebarGBC.gridwidth = 1;
		sidebarGBC.fill = GridBagConstraints.BOTH;
		sidebarGBC.weightx = 0.2;
		sidebarGBC.weighty = 1;
		mainPanelLayout.add(sidebarPanel, sidebarGBC);

		// DASHBOARD PANEL 
		dashboardPanel = new JPanel(new GridBagLayout());
		GridBagConstraints dashGBC = new GridBagConstraints();
		dashGBC.gridy = 1;
		dashGBC.gridx = 1;
		dashGBC.weightx = 1;
		dashGBC.weighty = 1;
		dashGBC.fill = GridBagConstraints.BOTH;
		mainPanelLayout.add(dashboardPanel, dashGBC);
		
		// LEFTSIDE OF DASHBOARD
		leftDashboardPanel = new JPanel(new GridBagLayout());
		GridBagConstraints leftGBC = new GridBagConstraints();
		leftGBC.gridx = 0;
		leftGBC.gridy = 0;
		leftGBC.weightx = 0.2;
		leftGBC.fill = GridBagConstraints.BOTH;
		leftGBC.weighty = 1;
		dashboardPanel.add(leftDashboardPanel, leftGBC);
				
				// PROFILE PICTURE PANEL
				profilepicturePanel = new JPanel();
				GridBagConstraints profilepictureGBC = new GridBagConstraints();
				profilepictureGBC.gridx = 0;
				profilepictureGBC.gridy = 0;
				profilepictureGBC.weightx = 1;
				profilepictureGBC.weighty = 0.15;
				profilepictureGBC.fill = GridBagConstraints.BOTH;
				profilepictureGBC.insets = new Insets(0,0,0,0);
				leftDashboardPanel.add(profilepicturePanel, profilepictureGBC);
				
				// POSITION INFO PANEL
				positioninfoPanel.setBackground(Color.WHITE);
				GridBagConstraints positioninfoGBC = new GridBagConstraints();
				positioninfoGBC.gridx = 0;
				positioninfoGBC.gridy = 1;
				positioninfoGBC.weightx = 1;
				positioninfoGBC.weighty = 0.85;
				positioninfoGBC.fill = GridBagConstraints.BOTH;
				positioninfoGBC.insets = new Insets(10,30,30,0);
				leftDashboardPanel.add(positioninfoPanel, positioninfoGBC);
		
		
		// RIGTH SIDE OF DASHBOARD
		rightDashboardPanel = new JPanel(new GridBagLayout());
		GridBagConstraints rightGBC = new GridBagConstraints();
		rightGBC.gridx = 1;
		rightGBC.gridy = 0;
		rightGBC.weightx = 0.8;
		rightGBC.fill = GridBagConstraints.BOTH;
		rightGBC.weighty = 1;
		dashboardPanel.add(rightDashboardPanel, rightGBC);
			
			//PRIMARY INFO PANEL
			primaryinfoPanel = new JPanel(new GridBagLayout());
			GridBagConstraints primaryinfoGBC = new GridBagConstraints();
			primaryinfoGBC.gridx = 0;
			primaryinfoGBC.gridy = 0;
			primaryinfoGBC.weightx = 1;
			primaryinfoGBC.weighty = 0.4;
			primaryinfoGBC.fill = GridBagConstraints.BOTH;
			primaryinfoGBC.insets = new Insets(10,0,0,220);
			rightDashboardPanel.add(primaryinfoPanel, primaryinfoGBC);
				
				fullnamePanel = new JPanel(new GridBagLayout());
				GridBagConstraints fullnameGBC = new GridBagConstraints();
				fullnameGBC.gridx = 0;
				fullnameGBC.gridy = 0;
				fullnameGBC.weightx = 1;
				fullnameGBC.weighty = 0.2;
				fullnameGBC.fill = GridBagConstraints.BOTH;
				fullnameGBC.insets = new Insets(30,0,0,0);
				primaryinfoPanel.add(fullnamePanel, fullnameGBC);
				
				otherprimaryinfoPanel = new JPanel(new GridBagLayout());
				GridBagConstraints otherprimaryinfoGBC = new GridBagConstraints();
				otherprimaryinfoGBC.gridx = 0;
				otherprimaryinfoGBC.gridy = 2;
				otherprimaryinfoGBC.weightx = 1;
				otherprimaryinfoGBC.weighty = 0.8;
				otherprimaryinfoGBC.fill = GridBagConstraints.BOTH;
				otherprimaryinfoGBC.insets = new Insets(-20,0,0,0);
				primaryinfoPanel.add(otherprimaryinfoPanel, otherprimaryinfoGBC);
				
			// GOVT NUMBERS PANEL
			govtnumbersPanel.setBackground(Color.WHITE);
			GridBagConstraints govtnumbersGBC = new GridBagConstraints();
			govtnumbersGBC.gridx = 0;
			govtnumbersGBC.gridy = 1;
			govtnumbersGBC.weightx = 1;
			govtnumbersGBC.weighty = 0.3;
			govtnumbersGBC.fill = GridBagConstraints.BOTH;
			govtnumbersGBC.insets = new Insets(10,10,5,30);
			rightDashboardPanel.add(govtnumbersPanel, govtnumbersGBC);
			
			// ADDRESS PANEL
			addressPanel.setBackground(Color.WHITE);
			GridBagConstraints addressGBC = new GridBagConstraints();
			addressGBC.gridx = 0;
			addressGBC.gridy = 2;
			addressGBC.weightx = 1;
			addressGBC.weighty = 0.3;
			addressGBC.fill = GridBagConstraints.BOTH;
			addressGBC.insets = new Insets(5,10,30,30);
			rightDashboardPanel.add(addressPanel, addressGBC);

		
	    // JFrame setup
        setTitle("MotorPH Payroll System | Dashboard");
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);   
		add(mainPanelLayout);
		setVisible(true);
		
		// MotorPH Logo
		ImageIcon motorphlogoAdmin = new ImageIcon("resources/images/MotorPH-Logo.png");
        JLabel motorPHLogo = new JLabel(motorphlogoAdmin);
        // Admin Logo at the right side of menu bar
        ImageIcon admindisplayLogo = new ImageIcon("resources/images/Admin-Logo.png");
        JLabel adminLogo = new JLabel(admindisplayLogo);
        // Employee Profile Photo placeholder
        ImageIcon empprofilePhoto = new ImageIcon("resources/images/profile-pic-emp.png");
        JLabel empPhoto = new JLabel(empprofilePhoto);
        
        // Set borders for search bar
        Border outerBorder = new LineBorder(Color.GRAY, 2, true); // outer border
		Border innerPadding = new EmptyBorder(5, 9, 5, 10); // inner border
		employeeIdField.setBorder(new CompoundBorder(outerBorder, innerPadding));
		logoutButton.setBorder(outerBorder);
		
		// assign color for buttons and its fonts
		computeButton.setBackground(Color.decode(navyblueColor)); 
		computeButton.setForeground(Color.WHITE); 
		computeButton.setPreferredSize(new Dimension(175, 50));
		
		leaveRequestButton.setBackground(Color.decode(navyblueColor)); 
		leaveRequestButton.setForeground(Color.WHITE); 
		leaveRequestButton.setPreferredSize(new Dimension(175, 50));
		
		updateRequestsButton.setBackground(Color.decode(navyblueColor)); 
		updateRequestsButton.setForeground(Color.WHITE); 
		updateRequestsButton.setPreferredSize(new Dimension(175, 50));
		
		employeeListButton.setBackground(Color.decode(navyblueColor)); 
		employeeListButton.setForeground(Color.WHITE); 
		employeeListButton.setPreferredSize(new Dimension(175, 50));
		
		logoutButton.setBackground(Color.WHITE); 
		logoutButton.setForeground(Color.RED); 
		logoutButton.setPreferredSize(new Dimension(175, 50));
		
		// Placing logo in menu bar
		GridBagConstraints gbc_motorPHLogo = new GridBagConstraints();
        gbc_motorPHLogo.gridx = 0;
        gbc_motorPHLogo.gridy = 0;
        gbc_motorPHLogo.insets = new Insets(0, 0, 0, 150);
		menubarPanel.add(motorPHLogo, gbc_motorPHLogo);
		
		GridBagConstraints gbc_employeeId = new GridBagConstraints();
		gbc_employeeId.gridx = 1;
		gbc_employeeId.gridy = 0;
		gbc_employeeId.anchor = GridBagConstraints.WEST;
		gbc_employeeId.insets = new Insets(0, 18, 0, 100);
		menubarPanel.add(employeeId, gbc_employeeId);
		
		GridBagConstraints gbc_employeeIdField = new GridBagConstraints();
		gbc_employeeIdField.gridx = 1;
		gbc_employeeIdField.gridy = 0;
		menubarPanel.add(employeeIdField, gbc_employeeIdField);
		
		GridBagConstraints gbc_adminLogo = new GridBagConstraints();
        gbc_adminLogo.gridx = 2;
        gbc_adminLogo.gridy = 0;
        gbc_adminLogo.insets = new Insets(0, 325, 0, 0);
		menubarPanel.add(adminLogo, gbc_adminLogo);
		
		// placing buttons in side bar
		GridBagConstraints gbc_computeButton = new GridBagConstraints();
		gbc_computeButton.gridx = 0;
		gbc_computeButton.gridy = 0;
		gbc_computeButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_computeButton.insets = new Insets(30, 0, 15, 0);
		sidebarPanel.add(computeButton, gbc_computeButton);
		
		GridBagConstraints gbc_leaveRequestButton = new GridBagConstraints();
		gbc_leaveRequestButton.gridx = 0;
		gbc_leaveRequestButton.gridy = 1;
		gbc_leaveRequestButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_leaveRequestButton.insets = new Insets(15, 0, 15, 0);
		sidebarPanel.add(leaveRequestButton, gbc_leaveRequestButton);
		
		GridBagConstraints gbc_updateRequestsButton = new GridBagConstraints();
		gbc_updateRequestsButton.gridx = 0;
		gbc_updateRequestsButton.gridy = 2;
		gbc_updateRequestsButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_updateRequestsButton.insets = new Insets(15, 0, 15, 0);
		sidebarPanel.add(updateRequestsButton, gbc_updateRequestsButton);
		
		GridBagConstraints gbc_employeeListButton = new GridBagConstraints();
		gbc_employeeListButton.gridx = 0;
		gbc_employeeListButton.gridy = 3;
		gbc_employeeListButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_employeeListButton.insets = new Insets(15, 0, 150, 0);
		sidebarPanel.add(employeeListButton, gbc_employeeListButton);
		
		GridBagConstraints gbc_logoutButton = new GridBagConstraints();
		gbc_logoutButton.gridx = 0;
		gbc_logoutButton.gridy = 4;
		gbc_logoutButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_logoutButton.insets = new Insets(100, 0, 50, 0);
		sidebarPanel.add(logoutButton, gbc_logoutButton);
		
		// placing profile photo in dashboard
		GridBagConstraints gbc_empPhoto = new GridBagConstraints();
		gbc_empPhoto.gridx = 0;
		gbc_empPhoto.gridy = 0;
		gbc_empPhoto.weightx = 1;
		gbc_empPhoto.weighty = 1;
		gbc_empPhoto.insets = new Insets(0,100,0,0);
		profilepicturePanel.add(empPhoto, gbc_empPhoto);
		
		// search bar label
		employeeId.setFont(new java.awt.Font("Sans Serif", Font.PLAIN, 14)); // NOI18N
		employeeId.setText("Search ID");
		
		// employee search bar
		employeeIdField.setFont(poppinsText); // NOI18N
		employeeIdField.setToolTipText("Please enter an ID");
		employeeIdField.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				employeeIdFieldActionPerformed(evt);
			}
		});
		
		// set fonts and texts for buttons
		searchButton.setFont(poppinsText); // NOI18N
		searchButton.setText("Search");
		searchButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				searchButtonActionPerformed(evt);
			}
		});

		computeButton.setFont(poppinsButtonsFont); // NOI18N
		computeButton.setText("Compute Salary");
		computeButton.setEnabled(false);
		computeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				computeButtonActionPerformed(evt);
			}
		});

		employeeListButton.setFont(poppinsButtonsFont); // NOI18N
		employeeListButton.setText("Employee List");
		employeeListButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				employeeListButtonActionPerformed(evt);
			}
		});

		leaveRequestButton.setFont(poppinsButtonsFont); // NOI18N
		leaveRequestButton.setText("Leave Requests");
		leaveRequestButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				leaveRequestButtonActionPerformed(evt);
			}
		});

		updateRequestsButton.setFont(poppinsButtonsFont); // NOI18N
        updateRequestsButton.setText("Update Requests");
        updateRequestsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateRequestsButtonActionPerformed(evt);
            }
        });

        logoutButton.setFont(poppinsButtonsFont); // NOI18N
		logoutButton.setText("Log Out");
		logoutButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				logoutButtonActionPerformed(evt);
			}
		});
		
		// set fonts and color for primary information panel
		fullName.setFont(poppinsFullNameLabelFont); // NOI18N
		fullName.setForeground(Color.decode(navyblueColor));
	
		firstNameValue.setFont(poppinsFullNameOutputFont); // NOI18N
		firstNameValue.setText(" ");
		lastNameValue.setFont(poppinsFullNameOutputFont); // NOI18N
		lastNameValue.setText("  ");

		birthday.setFont(poppinsFieldNameFont); // NOI18N
		birthday.setForeground(Color.decode(navyblueColor));
		birthday.setText("Birthday");
		birthdayValue.setFont(poppinsFieldOutputFont); // NOI18N
		birthdayValue.setText(" ");

		phoneNumber.setFont(poppinsFieldNameFont);  // NOI18N
		phoneNumber.setForeground(Color.decode(navyblueColor));
		phoneNumber.setText("Phone Number");
		phoneNumberValue.setFont(poppinsFieldOutputFont); // NOI18N
		phoneNumberValue.setText(" ");
		
		status.setFont(poppinsFieldNameFont); // NOI18N
		status.setForeground(Color.decode(navyblueColor));
		status.setText("Status");
		statusValue.setFont(poppinsFieldOutputFont);; // NOI18N
		statusValue.setText(" ");
		
		// layout primary information panel
		GroupLayout gl_fullnamePanel = new GroupLayout(fullnamePanel);
		fullnamePanel.setLayout(gl_fullnamePanel);
		gl_fullnamePanel.setHorizontalGroup(gl_fullnamePanel.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(gl_fullnamePanel.createSequentialGroup()
						
						.addGap(30,30,30) // left padding
						
						.addGroup(gl_fullnamePanel.createParallelGroup(GroupLayout.Alignment.LEADING)	
								.addComponent(fullName)
								.addComponent(firstNameValue, GroupLayout.DEFAULT_SIZE, 0, 175)
								)
								
						.addGroup(gl_fullnamePanel.createParallelGroup(GroupLayout.Alignment.LEADING)	
//								.addComponent(lastNameValue, GroupLayout.DEFAULT_SIZE,30, Short.MAX_VALUE)
								.addComponent(lastNameValue)
								)
						));
		
		gl_fullnamePanel.setVerticalGroup(gl_fullnamePanel.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(gl_fullnamePanel.createSequentialGroup()
						
						.addGap(10,10,10)
						
						.addGroup(gl_fullnamePanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(fullName))
								
						.addGroup(gl_fullnamePanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(firstNameValue)
								.addComponent(lastNameValue))
					));
		
		// set fonts and color for other primary information panel
		GroupLayout gl_otherprimaryinfoPanel = new GroupLayout(otherprimaryinfoPanel);
		otherprimaryinfoPanel.setLayout(gl_otherprimaryinfoPanel);
		gl_otherprimaryinfoPanel.setHorizontalGroup(gl_otherprimaryinfoPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(gl_otherprimaryinfoPanel.createSequentialGroup().addGap(30,30,30) // left padding
						.addGroup(gl_otherprimaryinfoPanel.createParallelGroup(GroupLayout.Alignment.LEADING)	
								.addComponent(birthday)
								.addComponent(birthdayValue, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
						
						.addGroup(gl_otherprimaryinfoPanel.createParallelGroup(GroupLayout.Alignment.LEADING)	
								.addComponent(phoneNumber)
								.addComponent(phoneNumberValue, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
						
						.addGroup(gl_otherprimaryinfoPanel.createParallelGroup(GroupLayout.Alignment.LEADING)	
								.addComponent(status)
								.addComponent(statusValue, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
						));
		
		gl_otherprimaryinfoPanel.setVerticalGroup(gl_otherprimaryinfoPanel.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(gl_otherprimaryinfoPanel.createSequentialGroup().addGap(30, 30, 30)
						
						.addGroup(gl_otherprimaryinfoPanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(birthday)
								.addComponent(phoneNumber)
								.addComponent(status))
						
						.addGroup(gl_otherprimaryinfoPanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(birthdayValue)
								.addComponent(phoneNumberValue)
								.addComponent(statusValue))	
					));
		// set fonts and color for position information panel
		position.setFont(poppinsFieldNameFont); // NOI18N
		position.setForeground(Color.decode(navyblueColor));
		position.setText("Position");
		positionValue.setFont(poppinsFieldOutputFont);  // NOI18N
		positionValue.setText(" ");

		immediateSupervisor.setFont(poppinsFieldNameFont);  // NOI18N
		immediateSupervisor.setForeground(Color.decode(navyblueColor));
		immediateSupervisor.setText("Immediate Supervisor");
		immediateSupervisorValue.setFont(poppinsFieldNameFont);  // NOI18N
		immediateSupervisorValue.setText(" ");

		hourlyRate.setFont(poppinsFieldNameFont);  // NOI18N
		hourlyRate.setForeground(Color.decode(navyblueColor));
		hourlyRate.setText("Hourly Rate");
		hourlyRateValue.setFont(poppinsFieldNameFont);  // NOI18N
		hourlyRateValue.setText(" ");
		
		// set layout for primary information panel
		GroupLayout gl_positioninfoPanel = new GroupLayout(positioninfoPanel);
		positioninfoPanel.setLayout(gl_positioninfoPanel);
		gl_positioninfoPanel.setHorizontalGroup(gl_positioninfoPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(gl_positioninfoPanel.createSequentialGroup().addGap(30,30,30) // left padding
						.addGroup(gl_positioninfoPanel.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(hourlyRate)
								.addComponent(immediateSupervisor)
								.addComponent(position)
								.addComponent(positionValue, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(immediateSupervisorValue, javax.swing.GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(hourlyRateValue, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addContainerGap(300, Short.MAX_VALUE)));
		
		gl_positioninfoPanel.setVerticalGroup(gl_positioninfoPanel.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(gl_positioninfoPanel.createSequentialGroup().addGap(30, 30, 30) // top padding
						.addComponent(position) 
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(positionValue)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(immediateSupervisor)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(immediateSupervisorValue)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(hourlyRate)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(hourlyRateValue)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		
		// set fonts and color for government account numbers panel
		sssNumber.setFont(poppinsFieldNameFont); // NOI18N
		sssNumber.setForeground(Color.decode(navyblueColor));
		sssNumber.setText("SSS Number");
		sssNumberValue.setFont(poppinsFieldOutputFont); // NOI18N
		sssNumberValue.setText(" ");

		philhealthNumber.setFont(poppinsFieldNameFont);  // NOI18N
		philhealthNumber.setForeground(Color.decode(navyblueColor));
		philhealthNumber.setText("PhilHealth Number");
		philhealthNumberValue.setFont(poppinsFieldOutputFont);// NOI18N
		philhealthNumberValue.setText(" ");

		pagibigNumber.setFont(poppinsFieldNameFont);  // NOI18N
		pagibigNumber.setForeground(Color.decode(navyblueColor));
		pagibigNumber.setText("Pag-ibig Number");
		pagibigNumberValue.setFont(poppinsFieldOutputFont); // NOI18N
		pagibigNumberValue.setText(" ");

		tinNumber.setFont(poppinsFieldNameFont); // NOI18N
		tinNumber.setForeground(Color.decode(navyblueColor));
		tinNumber.setText("TIN Number");
		tinNumberValue.setFont(poppinsFieldOutputFont); // NOI18N
		tinNumberValue.setText(" ");
		
		// set layout for government account numbers panel
		GroupLayout gl_govtnumbersPanel = new GroupLayout(govtnumbersPanel);
		govtnumbersPanel.setLayout(gl_govtnumbersPanel);

		gl_govtnumbersPanel.setHorizontalGroup(
		    gl_govtnumbersPanel.createSequentialGroup()
		        .addGap(30) // Left padding
		        .addGroup(gl_govtnumbersPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
		            .addComponent(sssNumber)
		            .addComponent(sssNumberValue)
		            .addComponent(philhealthNumber)
		            .addComponent(philhealthNumberValue)
		        )
		        .addGap(50) // Space between columns
		        .addGroup(gl_govtnumbersPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
		            .addComponent(pagibigNumber)
		            .addComponent(pagibigNumberValue)
		            .addComponent(tinNumber)
		            .addComponent(tinNumberValue)
		        )
		        .addContainerGap(30, Short.MAX_VALUE) // Right padding
		);

		gl_govtnumbersPanel.setVerticalGroup(
		    gl_govtnumbersPanel.createSequentialGroup()
		        .addGap(30) // Top padding
		        .addGroup(gl_govtnumbersPanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(sssNumber)
		            .addComponent(pagibigNumber)
		        )
		        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
		        .addGroup(gl_govtnumbersPanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(sssNumberValue)
		            .addComponent(pagibigNumberValue)
		        )
		        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
		        .addGroup(gl_govtnumbersPanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(philhealthNumber)
		            .addComponent(tinNumber)
		        )
		        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
		        .addGroup(gl_govtnumbersPanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(philhealthNumberValue)
		            .addComponent(tinNumberValue)
		        )
		        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		);
		
		// set font and colors for government account numbers panel
		address.setFont(poppinsFieldNameFont); // NOI18N
		address.setForeground(Color.decode(navyblueColor));
		address.setText("Address");
		addressValue.setFont(poppinsFieldOutputFont); // NOI18N
		addressValue.setText(" ");
		
		// set layout for address panel
		GroupLayout gl_addressPanel = new GroupLayout(addressPanel);
		addressPanel.setLayout(gl_addressPanel);
		gl_addressPanel.setHorizontalGroup(gl_addressPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(gl_addressPanel.createSequentialGroup().addGap(30,30,30) // left padding
						.addGroup(gl_addressPanel.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								
								.addComponent(address)
								.addComponent(addressValue, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
						
						.addContainerGap(300, Short.MAX_VALUE)));
		
		gl_addressPanel.setVerticalGroup(gl_addressPanel.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(gl_addressPanel.createSequentialGroup().addGap(30, 30, 30) // top padding
						.addComponent(address) 
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(addressValue)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		pack();
		setSize(1366,788);
		setLocationRelativeTo(null);
		
	}// </editor-fold>

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

}
