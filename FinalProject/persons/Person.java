/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	Person.java
 *
 */



package FinalProject.persons;



/**
 * Parent class for any person.
 */
public class Person {

	String first;
	String last;
	String address;
	String postal;
	String city;
	String province;


	/**
	 * Empty constructor.
	 */
	public Person(){
		first = last = address = postal = city = province = "";
	}

	public void setFirst() {}
	public void setLast() {}
	public void setAddress() {}
	public void setPostal() {}
	public void setCity() {}
	public void setProvince() {}

	public String getFirst() { return first; }
	public String getLast() { return last; }
	public String getAddress() { return address; }
	public String getPostal() { return postal; }
	public String getCity() { return city; }
	public String getProvince() { return province; }
}