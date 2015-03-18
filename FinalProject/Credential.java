package FinalProject;

import java.io.Serializable;

public class Credential implements Serializable {

	private static final long serialVersionUID = 1L;

	private String user;
	private String pin;

	public Credential(String user, String pin) {
		super();
		this.user = user;
		this.pin = pin;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

}
