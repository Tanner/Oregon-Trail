package component;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import component.Label.Alignment;

import core.FontManager;
import core.GameDirector;

/**
 * Create a button that has a flag that counts up or down.
 */
public class CountingButton extends Button {
	private int count;
	private int min = 0;
	private int max = Integer.MAX_VALUE;
	
	/**
	 * Creates a counting button.
	 * @param container Container for the  button
	 * @param label Label for the button
	 * @param width Width of the button
	 * @param height Height of the button
	 */
	public CountingButton(GUIContext container, Label label, int width, int height) {
		super(container, label, width, height);
		
		count = 0;
	}

	/**
	 * Creates a button.
	 * @param container Container for the button
	 * @param label Label for the button
	 * @param origin Position of the button
	 */
	public CountingButton(GUIContext container, Label label) {
		super(container, label);
		
		count = 0;
	}
	
	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		super.render(container, g);
		
		Font fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);
		Label countLabel = new Label(container, fieldFont, Color.white, ""+count);
		countLabel.setBackgroundColor(Color.red);
		countLabel.setPosition(getPosition(ReferencePoint.TopRight), ReferencePoint.CenterCenter);
		countLabel.setAlignment(Alignment.Center);
		countLabel.render(container, g);
	}
	
	@Override
	public void mousePressed(int button, int mx, int my) {
		if (!visible || !isAcceptingInput()) {
			return;
		}
		
		if (over && !disabled) {
			active = true;
			input.consumeEvent();
		}
	}
	
	@Override
	public void mouseReleased(int button, int mx, int my) {
		if (!visible) {
			return;
		}
		
 		if (over && !disabled && active) {
			notifyListeners();
			input.consumeEvent();
			active = false;
			
			if (button == 0 && count < max) {
				count++;
			} else if (button != 0 && count > min && count <= max){
				count--;
			}
		}
	}
	
	/**
	 * Change the minimum value.
	 * @param min New min value
	 */
	public void setMin(int min) {
		this.min = min;
	}
	
	/**
	 * Get the min value.
	 * @return Min value
	 */
	public int getMin() {
		return min;
	}
	
	/**
	 * Change the max value.
	 * @param max New max value
	 */
	public void setMax(int max) {
		this.max = max;
	}
	
	/**
	 * Get the max value.
	 * @return Max value
	 */
	public int getMax() {
		return max;
	}
	
	/**
	 * Set the count to a new value.
	 * @param count New count value
	 */
	public void setCount(int count) {
		this.count = count;
	}
	
	/**
	 * Get the count.
	 * @return Count value
	 */
	public int getCount() {
		return count;
	}
}
