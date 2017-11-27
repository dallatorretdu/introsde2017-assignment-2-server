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
    protected Integer id;
	
	@XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected String description;
    @XmlElement(required = true)
    protected String place;
    
    @XmlElement(required = true)
    @ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.MERGE)
    protected ActivityType type;
    
    @XmlElement(required = true)
    protected Date startdate;
    
	public Integer getId() {
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

	public static Activity saveActivity(Activity a) {
		EntityManager em = PersonActivitiesDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(a);
		tx.commit();
		PersonActivitiesDao.instance.closeConnections(em);
	    return a;
	}
	
	public static Activity updateActivity(Activity a) {
		EntityManager em = PersonActivitiesDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		a=em.merge(a);
		tx.commit();
		PersonActivitiesDao.instance.closeConnections(em);
	    return a;
	}
	
	public static void removeActivity(Activity a) {
		EntityManager em = PersonActivitiesDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
	    a=em.merge(a);
	    em.remove(a);
	    tx.commit();
	    PersonActivitiesDao.instance.closeConnections(em);
	}
	
	public static Activity getActivityById(int activityId) {
		EntityManager em = PersonActivitiesDao.instance.createEntityManager();
		Activity a = em.find(Activity.class, activityId);
		PersonActivitiesDao.instance.closeConnections(em);
		return a;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (this.getId().equals(((Activity) obj).getId())) && (this.getName().equals(((Activity) obj).getName()));
	}
	public boolean isInBetween(Date beginDate, Date endDate) {
		return (startdate.before(endDate) && startdate.after(beginDate));
	}

}
