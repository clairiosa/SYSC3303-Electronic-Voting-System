/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	Person.java
 *
 */



package FinalProject.persons;


import java.io.Serializable;

/**
 * Parent class for any person.
 */
public class Person implements Serializable {

	String name;
	String address;
	String postal;
	String city;
	String province;


	/**
	 * Empty constructor.
	 */
	public Person(String name){
		this.name=name;
		address = postal = city = province = "";
	}

	public void setName(String name) {this.name=name;}
	public void setAddress() {}
	public void setPostal() {}
	public void setCity() {}
	public void setProvince() {}

	public String getName() { return name; }
	public String getAddress() { return address; }
	public String getPostal() { return postal; }
	public String getCity() { return city; }
	public String getProvince() { return province; }
}
