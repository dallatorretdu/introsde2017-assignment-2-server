package unitn.dallatorre.entities;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="People")
@XmlAccessorType(XmlAccessType.FIELD)
public class PeopleWrapper implements Serializable{
	// Wrapper use to encapsulate a list of Person objects into a People parent element
	protected List<Person> person;
	
	public List<Person> getPersons() {
		return person;
	}
	
	public void setPersons(List<Person> personList) {
		person = personList;
	}
	// Method used to load all the Person elements from the Db
	public void readAllPersons() {
		person = Person.getAllPersons();
	}
}
