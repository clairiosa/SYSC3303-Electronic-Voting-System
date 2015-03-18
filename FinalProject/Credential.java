package FinalProject;

import java.io.Serializable;

public class Credential implements Serializable {
	private String user;
	private String pin;
	
	public Credential(String user, String pin){
		this.user = user;
		this.pin = pin;
	}
	
	public String getUser(){
		return user;
	}
	
	public String getPin(){
		return pin;
	}
}
