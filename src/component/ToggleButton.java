package component;

import org.newdawn.slick.gui.GUIContext;

/**
 * Toggle Button is a button that holds its state until its clicked again.
 */
public class ToggleButton extends Button {
	private boolean disableAutoToggle;
	
	/**
	 * Creates a toglge button.
	 * @param container Container for the  button
	 * @param label Label for the button
	 * @param width Width of the button
	 * @param height Height of the button
	 */
	public ToggleButton(GUIContext container, Label label, int width, int height) {
		super(container, width, height, label);
		
		disableAutoToggle = false;
	}
	
	/**
	 * Creates a button.
	 * @param container Container for the button
	 * @param label Label for the button
	 * @param origin Position of the button
	 */
	public ToggleButton(GUIContext container, Label label) {
		super(container, label);
		
		disableAutoToggle = false;
	}
	
	@Override
	public void mousePressed(int button, int mx, int my) {
		if (!isVisible() || !isAcceptingInput()) {
			return;
		}
		
		if (button == 0 && isMouseOver() && !disabled) {
			if (!disableAutoToggle) {
				active = !active;
			} else {
				active = true;
			}
			
			input.consumeEvent();
		}
	}
	
	@Override
	public void mouseReleased(int button, int mx, int my) {
		if (!isVisible()) {
			return;
		}
		
 		if (button == 0 && isMouseOver() && !disabled && active) {
			if (disableAutoToggle) {
				active = false;
			}
			
			notifyListeners();
			input.consumeEvent();
		}
	}
	
	/**
	 * Change the value of disable auto toggle.
	 * @param disableAutoToggle Whether auto toggle will be on or off
	 */
	public void setDisableAutoToggle(boolean disableAutoToggle) {
		this.disableAutoToggle = disableAutoToggle;
	}
	
	/**
	 * Get the disable auto toggle status.
	 * @return Auto toggle status
	 */
	public boolean getDisableAutoToggle() {
		return disableAutoToggle;
	}
	
	/**
	 * Return the button's active status.
	 * @return If the button is active or not
	 */
	public boolean getActive() {
		return active;
	}
	
	/**
	 * Change whether this button is active or not.
	 * @param active New active state
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
}
