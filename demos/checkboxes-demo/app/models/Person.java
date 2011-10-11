package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.jpa.GenericModel;

@Entity
public class Person extends GenericModel {
	@Id
	public Integer id;
	public String firstname; 
	public String lastname; 
	public Integer age;
	
	public Person() {
	}
	
	public Person(int id, String fn, String ln, int a) {
		this.id = id;
		firstname = fn;
		lastname = ln;
		age = a;
	}

	public String getFullname() {
		return firstname + " " + lastname;
	}
	@Override
	public String toString() {
		return String.format("%s %s is %d years old", firstname, lastname, age);
	}
}
