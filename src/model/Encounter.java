package model;

import scene.Scene;

public class Encounter {

	private Notification notification;
	private Scene scene;
	
	public Encounter(Notification notification, Scene scene) {
		this.notification = notification;
		this.scene = scene;
	}
	
}
