package component;

import org.newdawn.slick.gui.GUIContext;

/**
 * Toggle Button is a button that holds its state until its clicked again.
 */
public class ToggleButton extends Button {
	private boolean disableAutoToggle;
	
	/**
	 * Creates a {@code ToggleButton} with a label, width, and height.
	 * @param container The GUI context
	 * @param width The width
	 * @param height The height
	 * @param label A label
	 */
	public ToggleButton(GUIContext context, int width, int height, Label label) {
		super(context, width, height, label);
		
		disableAutoToggle = false;
	}
	
	@Override
	public void mousePressed(int button, int mx, int my) {
		if (!isVisible() || !isAcceptingInput()) {
			return;
		}
		
		if (button == 0 && isMouseOver() && !isDisabled()) {
			if (!disableAutoToggle) {
				setActive(!isActive());
			} else {
				setActive(true);
			}
			
			input.consumeEvent();
		}
	}
	
	@Override
	public void mouseReleased(int button, int mx, int my) {
		if (!isVisible()) {
			return;
		}
		
 		if (button == 0 && isMouseOver() && !isDisabled() && isActive()) {
			if (disableAutoToggle) {
				setActive(false);
			}
			
			notifyListeners();
			input.consumeEvent();
		}
	}
	
	/**
	 * Change the value to disable auto toggle.
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
}
