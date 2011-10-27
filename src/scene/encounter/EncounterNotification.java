package scene.encounter;

import model.Notification;
import scene.SceneID;

public class EncounterNotification {

	private Notification notification;
	private SceneID sceneID;
	
	public EncounterNotification(Notification notification, SceneID sceneID) {
		this.notification = notification;
		this.sceneID = sceneID;
	}
	
	public Notification getNotification() {
		return notification;
	}
	
	public SceneID getSceneID() {
		return sceneID;
	}
	
}
