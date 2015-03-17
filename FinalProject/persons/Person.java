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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String name;
	String address;
	String postal;
	String city;
	String province;
	String districtId;

	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	/**
	 * Empty constructor.
	 */
	public Person(String name) {
		this.name = name;
		address = postal = city = province = "";
	}

	public Person(String name, String districtId) {
		this.name = name;
		this.districtId = districtId;
		address = postal = city = province = "";
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress() {
	}

	public void setPostal() {
	}

	public void setCity() {
	}

	public void setProvince() {
	}

	public void setDistrict() {
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getPostal() {
		return postal;
	}

	public String getCity() {
		return city;
	}

	public String getProvince() {
		return province;
	}
}
