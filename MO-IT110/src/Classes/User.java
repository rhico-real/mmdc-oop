package Classes;

import DAO.UserDAO;
import java.util.Date;
import com.google.gson.annotations.SerializedName;

public class User {

	@SerializedName("employeeNum")
	private String 	employeeNumber;
	private String 	userId;
	private String 	password;
	private Boolean isVerified 	= false;
	private Boolean loginStatus = false;
	private Boolean isAdmin 	= false;
	private Boolean isHR 		= false;
	private Date 	dateRegistered;

	public User(String userId, String password) {
		this.userId 	= userId;
		this.password 	= password;
		if (!userId.equals("") && !password.equals("")) {
			authenticateLogin();
		}
	}

	public User(String employeeNumber) {		setEmployeeNumber(employeeNumber);	}

	public String 	getEmployeeNumber() { return employeeNumber;	}
	public String 	getUserId		 () { return userId;			}
	public String 	getPassword		 () { return password;			}
	public Boolean 	getIsVerified	 () { return isVerified;		}
	public Boolean 	getLoginStatus	 () { return loginStatus;		}
	public Boolean 	getIsAdmin		 () { return isAdmin;			}
	public Boolean 	getIsHR			 () { return isHR;			}
	public Date 	getDateRegistered() { return dateRegistered;	}

	public void setEmployeeNumber(String  employeeNum	) {	this.employeeNumber = employeeNum; 	  }
	public void setUserId		 (String  userId		) {	this.userId 		= userId;		  }
	public void setPassword		 (String  password		) {	this.password 		= password;		  }
	public void setIsVerified	 (Boolean isVerified	) {	this.isVerified 	= isVerified;	  }
	public void setLoginStatus	 (Boolean loginStatus	) {	this.loginStatus 	= loginStatus;	  }
	public void setIsAdmin		 (Boolean value			) {	this.isAdmin 		= value;		  }
	public void setIsHR			 (Boolean value			) {	this.isHR 			= value;		  }
	public void setDateRegistered(Date    dateRegistered) {	this.dateRegistered = dateRegistered; }

	public void authenticateLogin() {
		if (!userId.equals("admin")) {
			// Use UserDAO for authentication - authenticate with database
			User user = UserDAO.authenticateUser(userId, password);
			if (user != null) {
				setEmployeeNumber(user.getEmployeeNumber());
				setLoginStatus(true);
				setIsVerified(true);
				setIsAdmin(user.getIsAdmin());
				setIsHR(user.getIsHR());
			}
			return;
		}

		// Check if user is an admin
		setLoginStatus(authenticateAdminLogin(userId, password));
		return;
	}

	private Boolean authenticateAdminLogin(String userId, String password) {
		if (userId.equals("admin") && password.equals("123")) {
			setIsAdmin(true);
			return true;
		}
		return false;
	}
}
