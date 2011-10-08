package component;

import org.newdawn.slick.gui.GUIContext;

/**
 * Toggle Button is a button that holds its state until its clicked again.
 */
public class ToggleButton extends Button {
	/**
	 * Creates a toglge button.
	 * @param container Container for the  button
	 * @param label Label for the button
	 * @param width Width of the button
	 * @param height Height of the button
	 */
	public ToggleButton(GUIContext container, Label label, int width, int height) {
		super(container, label, width, height);
	}
	
	/**
	 * Creates a button.
	 * @param container Container for the button
	 * @param label Label for the button
	 * @param origin Position of the button
	 */
	public ToggleButton(GUIContext container, Label label) {
		super(container, label);
	}
	
	@Override
	public void mousePressed(int button, int mx, int my) {
		if (!visible || !isAcceptingInput()) {
			return;
		}
		
		if (button == 0 && over && !disabled) {
			active = !active;
			input.consumeEvent();
		}
	}
	
	@Override
	public void mouseReleased(int button, int mx, int my) {
		if (!visible) {
			return;
		}
		
 		if (button == 0 && over && !disabled && active) {
			notifyListeners();
			input.consumeEvent();
		}
	}
}
