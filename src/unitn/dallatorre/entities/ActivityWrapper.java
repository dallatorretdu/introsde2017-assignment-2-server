package unitn.dallatorre.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="activities")
@XmlAccessorType(XmlAccessType.FIELD)
public class ActivityWrapper implements Serializable{

	private List<Activity> activity;
	
	public List<Activity> getActivity() {
		return activity;
	}
	
	public void setActivity(List<Activity> newActivity) {
		activity = newActivity;
	}
	
	public List<Activity> filterActivities(ActivityType type) {
		List<Activity> filteredActivities = new ArrayList<Activity>();
		
		for (final Activity activity : activity) {
			if (activity.getType().equals(type)) {
				filteredActivities.add(activity);
			}
		}
		activity.clear();
		activity.addAll(filteredActivities);
		return getActivity();
	}
	
	public List<Activity> filterActivities(int id) {
		List<Activity> filteredActivities = new ArrayList<Activity>();
		for (final Activity activity : activity) {
			System.out.println("ID " + activity.getId());
			if (activity.getId() == id) {
				filteredActivities.add(activity);
			}
		}
		activity.clear();
		activity.addAll(filteredActivities);
		return getActivity();
	}
		
}
