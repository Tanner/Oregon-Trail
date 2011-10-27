package component;

import component.modal.Modal;

/**
 * Interface that can be implemented by classes that listen for {@code Modal} events.
 */
public interface ModalListener {
	/**
	 * Dismiss the modal.
	 * @param modal The modal to be dismissed
	 * @param cancelled If the modal was cancelled
	 */
	public void dismissModal(Modal modal, boolean cancelled);
}
