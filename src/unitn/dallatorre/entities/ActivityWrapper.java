package unitn.dallatorre.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="activities")
@XmlAccessorType(XmlAccessType.FIELD)
public class ActivityWrapper implements Serializable{
	// Wrapper class used to wrap a list of Activity objects inside a parent Activities, for XML/Json communications
	private List<Activity> activity;
	
	public List<Activity> getActivity() {
		return activity;
	}
	
	public void setActivity(List<Activity> newActivity) {
		activity = newActivity;
	}
	// Method used to remove all the activities which are not of the given TYPE
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
	// Method used to remove all activities which do not have a certain ID
	public List<Activity> filterActivities(int id) {
		List<Activity> filteredActivities = new ArrayList<Activity>();
		for (final Activity activity : activity) {
			if (activity.getId() == id) {
				filteredActivities.add(activity);
			}
		}
		activity.clear();
		activity.addAll(filteredActivities);
		return getActivity();
	}
	// Method used to remove all the activities which are outside a date range
	public List<Activity> filterActivities(Date beginDate, Date endDate) {
		List<Activity> filteredActivities = new ArrayList<Activity>();
		for (final Activity activity : activity) {
			if (activity.isInBetween(beginDate, endDate)) {
				filteredActivities.add(activity);
			}
		}
		activity.clear();
		activity.addAll(filteredActivities);
		return getActivity();
	}
		
}
