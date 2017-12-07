package unitn.dallatorre.entities;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Type;

import unitn.dallatorre.dao.PersonActivitiesDao;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Activity implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    @XmlAttribute(name = "id", required = true)
    protected Integer id;							// It has an ID and is required, but AUTO-GENERATED
	
	@XmlElement(required = true)
    protected String name;							// REQUIRED NAME
    @XmlElement(required = true)
    protected String description;					// REQUIRED DESCRIPTION
    @XmlElement(required = true)
    protected String place;							// REQUIRED PLACE
    
    @XmlElement(required = true)
    @ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.MERGE)
    protected ActivityType type;					// TYPE - many:one fetched eagerly, update and insertion treated as Merges
    
    @XmlElement(required = true)
    protected Date startdate;						// Start Date
    
	public Integer getId() {						// GETTERS AND SETTERS
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public ActivityType getType() {
		return type;
	}
	public void setType(ActivityType type) {
		this.type = type;
	}
	public Date getStartdate() {
		return startdate;
	}
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public static Activity saveActivity(Activity a) {	// PERSISTENCE Method
		EntityManager em = PersonActivitiesDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(a);									// persist
		tx.commit();
		PersonActivitiesDao.instance.closeConnections(em);
	    return a;
	}
	
	public static Activity updateActivity(Activity a) {	// PERSISTENCE method
		EntityManager em = PersonActivitiesDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		a=em.merge(a);									// Merge
		tx.commit();
		PersonActivitiesDao.instance.closeConnections(em);
	    return a;
	}
	
	public static void removeActivity(Activity a) {	// PERSISTENCE method
		EntityManager em = PersonActivitiesDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
	    a=em.merge(a);								// align it
	    em.remove(a);								// then Remove it
	    tx.commit();
	    PersonActivitiesDao.instance.closeConnections(em);
	}
	
	public static Activity getActivityById(int activityId) {	// PERSISTENCE method
		EntityManager em = PersonActivitiesDao.instance.createEntityManager();
		Activity a = em.find(Activity.class, activityId);		// Use FIND to search by ID
		PersonActivitiesDao.instance.closeConnections(em);
		return a;
	}
	
	// Overridden the Equals method so I could check if an activity is .equals automatically checking based on it's ID and NAME
	@Override
	public boolean equals(Object obj) {
		return (this.getId().equals(((Activity) obj).getId())) && (this.getName().equals(((Activity) obj).getName()));
	}
	// Method used to compare if the activity's date is in between 2 date values.
	public boolean isInBetween(Date beginDate, Date endDate) {
		return (startdate.before(endDate) && startdate.after(beginDate));
	}

}
