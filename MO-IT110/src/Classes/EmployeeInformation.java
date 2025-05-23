package Classes;

import com.google.gson.annotations.SerializedName;
import DAO.EmployeeDAO;

public class EmployeeInformation extends User {

	@SerializedName("last_name")			private String lastName;
	@SerializedName("first_name")			private String firstName;
	private String birthday;
	private String address;
	@SerializedName("phone_number")			private String phoneNumber;
	@SerializedName("Status")				private String status;
	@SerializedName("Position")				private String position;
	@SerializedName("immediate_supervisor")	private String supervisor;
	@SerializedName("hourly_rate")			private double hourlyRate;

	public EmployeeInformation(String userId, String password) { 
		super(userId, password); 
	}
	
	public EmployeeInformation(String employeeNumber) {
		super(employeeNumber);
	}

	
	public String getLastName			() { return lastName; 	}
	public String getFirstName			() { return firstName;	}
	public String getBirthday			() { return birthday;	}
	public String getAddress			() { return address;	}
	public String getPhoneNumber		() { return phoneNumber;}
	public String getStatus				() { return status;		}
	public String getPosition			() { return position;	}
	public String getSupervisor			() { return supervisor; }
	public double getHourlyRate			() { return hourlyRate; }
	
	public void setLastName				(String lastName	) { this.lastName 	= lastName;		}
	public void setFirstName			(String firstName	) { this.firstName 	= firstName; 	}
	public void setBirthday				(String birthday	) {	this.birthday 	= birthday;		}
	public void setAddress				(String address		) {	this.address 	= address;		}
	public void setPhoneNumber			(String phoneNumber	) {	this.phoneNumber= phoneNumber; 	}
	public void setStatus				(String status		) {	this.status 	= status;	 	}
	public void setPosition				(String position	) {	this.position 	= position; 	}
	public void setSupervisor			(String supervisor	) { this.supervisor = supervisor; 	}
	public void setHourlyRate			(double value		) { this.hourlyRate = value; 		}

	public static void setEmployeeInformationObject(String employeeNumber, GovernmentIdentification employeeGI,
			Compensation employeeComp) {

		// Use EmployeeDAO to get employee information from database
		GovernmentIdentification employeeGovInfo = EmployeeDAO.getEmployeeGovId(employeeNumber);
		Compensation employeeCompInfo = EmployeeDAO.getEmployeeCompensation(employeeNumber);
		
		if (employeeGovInfo != null && employeeCompInfo != null) {
			// Set the employee's identity information
			employeeGI.setLastName(employeeGovInfo.getLastName());
			employeeGI.setFirstName(employeeGovInfo.getFirstName());
			employeeGI.setBirthday(employeeGovInfo.getBirthday());
			employeeGI.setAddress(employeeGovInfo.getAddress());
			employeeGI.setPhoneNumber(employeeGovInfo.getPhoneNumber());
			employeeGI.setSupervisor(employeeGovInfo.getSupervisor());
			employeeGI.setStatus(employeeGovInfo.getStatus());
			employeeGI.setPosition(employeeGovInfo.getPosition());
			
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
}
