package unitn.dallatorre.entities;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class People implements Serializable{
	
	protected List<Person> persons;
	
	public List<Person> getPersons() {
		return persons;
	}
	
	public void setPersons(List<Person> personList) {
		persons = personList;
	}
	
	public void readAllPersons() {
		persons = Person.getAllPersons();
	}
}
