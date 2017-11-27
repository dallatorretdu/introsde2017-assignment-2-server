package unitn.dallatorre.entities;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Type;

import unitn.dallatorre.dao.PersonActivitiesDao;

@Entity
@Table(name="Person")
@NamedQuery(name="Person.findAll", query="SELECT p FROM Person p")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Person implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    @XmlAttribute(name = "id", required = true)
    private Integer id;
    
	@XmlElement(required = true)
    private String firstname;
    @XmlElement(required = true)
    private String lastname;
    @XmlElement(required = true)
    private Date birthdate;   
    @XmlElement(required = false)
    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    private List<Activity> activitypreference;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public List<Activity> getActivitypreference() {
		return activitypreference;
	}

	public void setActivitypreference(List<Activity> activitypreference) {
		this.activitypreference = activitypreference;
	}
	
	public String toString() {
		String out = this.getFirstname() + " " + this.getLastname() + "<br>   Activities: ";
		for (Activity activity: this.getActivitypreference()) {
		    out += "<br>       " + activity.getName() + " at " + activity.getPlace() +" of type: " + activity.getType().getType();
		}
		return out;
	}

	public static Person savePerson(Person p) {
		EntityManager em = PersonActivitiesDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(p);
		tx.commit();
		PersonActivitiesDao.instance.closeConnections(em);
	    return p;
	}
	
	public static Person updatePerson(Person p) {
		EntityManager em = PersonActivitiesDao.instance.createEntityManager();
		
		if(p.getId() == null) {
			TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE p.firstname=?1 AND p.lastname=?2",Person.class);
			query.setParameter(1, p.getFirstname());
			query.setParameter(2, p.getLastname());
			List<Person> foundPersons = query.getResultList();
			if (foundPersons.size()>0) {
				p.setId(foundPersons.get(0).getId());
			}
		}
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p=em.merge(p);
		tx.commit();
		PersonActivitiesDao.instance.closeConnections(em);
	    return p;
	}
	
	public static void removePerson(Person p) {
		EntityManager em = PersonActivitiesDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
	    p=em.merge(p);
	    em.remove(p);
	    tx.commit();
	    PersonActivitiesDao.instance.closeConnections(em);
	}
	
	public static Person getPersonById(int personId) {
		EntityManager em = PersonActivitiesDao.instance.createEntityManager();
		Person p = em.find(Person.class, personId);
		PersonActivitiesDao.instance.closeConnections(em);
		return p;
	}
	
	public static List<Person> getAllPersons() {
		EntityManager em = PersonActivitiesDao.instance.createEntityManager();
		TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p",Person.class);
		return query.getResultList();
	}
}
