package component;

/**
 * Interface that can be implemented by classes that listen for {@code Modal} events.
 */
public interface ModalListener {
	/**
	 * Dismiss the modal.
	 * @param modal The modal to be dismissed.
	 */
	public void dismissModal(Modal modal);
}
