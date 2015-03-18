/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	Ballot.java
 *
 */

package FinalProject.districtserver.inf;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import FinalProject.persons.Person;

/**
 * To be used in next milestone
 * @author damianpolan
 * 
 */
public class NextDistrict implements Serializable {

	/**
	 * Default version ID
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * The id given by the master server to this district
	 */
	protected String uniqueDistrictId;

	/**
	 * Key: name of the Person Value: Person that belongs to the district of the
	 * given name
	 */
	protected ConcurrentHashMap<String, Person> people;

	public NextDistrict(String uniqueDistrictId,
			ConcurrentHashMap<String, Person> people) {
		super();
		this.uniqueDistrictId = uniqueDistrictId;
		this.people = new ConcurrentHashMap<String, Person>();
	}

	public String getUniqueDistrictId() {
		return uniqueDistrictId;
	}

	public void setUniqueDistrictId(String newDistrictName) {
		this.uniqueDistrictId = newDistrictName;
	}

	public ConcurrentHashMap<String, Person> getPeople() {
		return people;
	}

	public void setPeople(ConcurrentHashMap<String, Person> people) {
		this.people = people;
	}

	public boolean addPerson(Person v) {
		people.put(v.getName(), v);
		return true;
	}

}
