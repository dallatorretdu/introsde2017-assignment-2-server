package unitn.dallatorre.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="activityTypes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ActivityTypesWrapper implements Serializable{
	// Wrapper used to generate XML/Json as parent ActivityTypes that contain several ActivityType objects
	private List<String> activityType;
	
	public List<String> getActivityTypes() {
		return activityType;
	}
	
	public void setActivityTypes(List<String> types) {
		activityType = types;
	}
	// Method that uses ActivityType method and loads all types in memory
	public void readAllTypes() {
		activityType = new ArrayList<String>();
		List<ActivityType> rawTypes = ActivityType.getAllTypes();
		for (final ActivityType type : rawTypes) {
			activityType.add(type.getType());
		}
	}
}
