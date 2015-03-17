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
import FinalProject.persons.Voter;

/**
 * 
 * @author damianpolan
 * 
 */
public class NextDistrict implements Serializable {

	protected String nextDistrictName;

	/**
	 * Key: name of the Person
	 * Value: Person that belongs to the district of the given name
	 */
	protected ConcurrentHashMap<String, Person> people;

	public DistrictPeople(String newDistrictName,
			ConcurrentHashMap<String, Person> people) {
		super();
		this.newDistrictName = newDistrictName;
		this.people = new ConcurrentHashMap<String, Person>();
	}

	public String getNewDistrictName() {
		return newDistrictName;
	}

	public void setNewDistrictName(String newDistrictName) {
		this.newDistrictName = newDistrictName;
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
