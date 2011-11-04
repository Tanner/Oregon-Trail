package component.modal;

/**
 * Interface that can be implemented by classes that listen for {@code Modal} events.
 */
public interface ModalListener {
	/**
	 * Dismiss the modal.
	 * @param modal The modal to be dismissed
	 * @param cancelled Index of button that was pressed
	 */
	public void dismissModal(Modal modal, int button);
}
