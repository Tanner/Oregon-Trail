package model;

public class Notification {

	private String message;
	private boolean isModal;
	
	public Notification(String message) {
		this(message, false);
	}
	
	public Notification(String message, boolean isModal) {
		this.message = message;
		this.isModal = isModal;
	}
	
	public String getMessage() {
		return message;
	}
	
	public boolean getIsModal() {
		return isModal;
	}
}
