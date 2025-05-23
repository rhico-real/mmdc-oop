package Classes;

import java.util.UUID;
import DAO.LeaveRequestDAO;

public class LeaveRequest {

	private String id;
	private String employeeNum;
	private String firstName;
	private String lastName;
	private String startDate;
	private String endDate;
	private String notes;
	private String leaveType;
	private String approved = "Not Approved Yet";

	public LeaveRequest(String employeeNum) {
		this.setEmployeeNum(employeeNum);
		this.setId(UUID.randomUUID().toString());
	}
	
	public String getId			() { return id; 		}
	public String getStartDate	() { return startDate;	}
	public String getEndDate	() { return endDate;	}
	public String getNotes		() { return notes;		}
	public String getLeaveType	() { return leaveType;	}
	public String getEmployeeNum() { return employeeNum;}
	public String isApproved	() { return approved;	}
	public String getFirstName	() { return firstName;	}
	public String getLastName	() { return lastName;	}

	
	public void setId			(String id			) { this.id 		 = id; 			}
	public void setStartDate	(String startDate	) {	this.startDate 	 = startDate;	}
	public void setEndDate		(String endDate		) {	this.endDate 	 = endDate;		}
	public void setNotes		(String notes		) {	this.notes 		 = notes;		}
	public void setLeaveType	(String leaveType	) {	this.leaveType 	 = leaveType;	}
	public void setApproved		(String approved	) {	this.approved 	 = approved;	}
	public void setEmployeeNum	(String employeeNum	) {	this.employeeNum = employeeNum;	}
	public void setFirstName	(String firstName	) {	this.firstName 	 = firstName;	}
	public void setLastName		(String lastName	) {	this.lastName 	 = lastName;	}

	public static void setLeaveRequestInformationObject(String leaveRequestId, LeaveRequest leaveRequest) {
		// Use LeaveRequestDAO to get leave request information from database
		LeaveRequest leaveRequestInfo = LeaveRequestDAO.getLeaveRequestById(leaveRequestId);
		
		if (leaveRequestInfo != null) {
			// Set the leave request information
			leaveRequest.setEmployeeNum	( leaveRequestInfo.getEmployeeNum()	);
			leaveRequest.setLastName	( leaveRequestInfo.getLastName()	);
			leaveRequest.setFirstName	( leaveRequestInfo.getFirstName()	);
			leaveRequest.setEndDate		( leaveRequestInfo.getEndDate()		);
			leaveRequest.setStartDate	( leaveRequestInfo.getStartDate()	);
			leaveRequest.setLeaveType	( leaveRequestInfo.getLeaveType()	);
			leaveRequest.setNotes		( leaveRequestInfo.getNotes()		);
			leaveRequest.setApproved	( leaveRequestInfo.isApproved()		);
		}
	}
}
