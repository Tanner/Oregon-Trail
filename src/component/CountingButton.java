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
 * {@code CountingButton} inherits from {@code Button} to add a flag that counts up or down.
 */
public class CountingButton extends Button {
	private int count;
	private int min = 0;
	private int max = Integer.MAX_VALUE;
	private boolean countUpOnLeftClick;
	private boolean hideCount;
	
	/**
	 * Constructs a {@code CountingButton} with a width and height.
	 * @param container The GUI context
	 * @param width Width of the button
	 * @param height Height of the button
	 * @param label Label for the button
	 */
	public CountingButton(GUIContext context, int width, int height, Label label) {
		super(context, width, height, label);
		
		count = 0;
		countUpOnLeftClick = true;
		hideCount = false;
	}

	/**
	 * Constructs a {@code CountingButton} with a label.
	 * @param container Container for the button
	 * @param label Label for the button
	 * @param origin Position of the button
	 */
	public CountingButton(GUIContext container, Label label) {
		super(container, label);
		
		count = 0;
		countUpOnLeftClick = true;
		hideCount = false;
	}
	
	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		super.render(container, g);
		
		Font fieldFont = GameDirector.sharedSceneListener().getFontManager().getFont(FontManager.FontID.FIELD);
		
		if (!hideCount) {
			Label countLabel = new Label(container, fieldFont, Color.white, ""+count);
			countLabel.setBackgroundColor(Color.red);
			countLabel.setPosition(getPosition(ReferencePoint.TopRight), ReferencePoint.CenterCenter);
			countLabel.setAlignment(Alignment.Center);
			countLabel.render(container, g);
		}
	}
	
	@Override
	public void mousePressed(int button, int mx, int my) {
		if (!isVisible() || !isAcceptingInput()) {
			return;
		}
		
		if (isMouseOver() && !isDisabled()) {
			setActive(true);
			input.consumeEvent();
		}
	}
	
	@Override
	public void mouseReleased(int button, int mx, int my) {
		if (!isVisible()) {
			return;
		}
		
 		if (isMouseOver() && !isDisabled() && isActive()) {
			notifyListeners();
			input.consumeEvent();
			setActive(false);
			
			if (button == 0 && countUpOnLeftClick && count < max) {
				count++;
			} else if (button != 0 && countUpOnLeftClick && count > min) {
				count--;
			} else if (button == 0 && !countUpOnLeftClick && count > min) {
				count--;
			} else if (button != 0 && !countUpOnLeftClick && count < max) {
				count++;
			}
		}
	}
	
	/**
	 * Return whether to count up on left click.
	 * @return Counting up on left click
	 */
	public boolean isCountUpOnLeftClick() {
		return countUpOnLeftClick;
	}

	/**
	 * Change whether to count up on left click.
	 * @param countUpOnLeftClick New value
	 */
	public void setCountUpOnLeftClick(boolean countUpOnLeftClick) {
		this.countUpOnLeftClick = countUpOnLeftClick;
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

	/**
	 * Get whether the count is hidden or not.
	 * @return Whether the count is hidden or not
	 */
	public boolean getHideCount() {
		return hideCount;
	}

	/**
	 * Set whether the count is hidden or not.
	 * @param hideCount Whether the count is hidden or not
	 */
	public void setHideCount(boolean hideCount) {
		this.hideCount = hideCount;
	}
}
