package component;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import component.Label.Alignment;
import component.sprite.Sprite;

import core.FontManager;
import core.GameDirector;

/**
 * {@code CountingButton} inherits from {@code Button} to add a flag that counts up or down.
 */
public class CountingButton extends Button {
	private static final int COUNTING_LABEL_WIDTH = 25; 
	private static final int COUNTING_LABEL_HEIGHT = 25; 
	
	private int count;
	private int min = 0;
	private int max = Integer.MAX_VALUE;
	private boolean countUpOnLeftClick;
	private boolean disableAutoCount;
	private boolean hideCount;
	private Label countLabel;
	
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
		disableAutoCount = false;
		
		Font fieldFont = GameDirector.sharedSceneListener().getFontManager().getFont(FontManager.FontID.FIELD);
		
		countLabel = new Label(container, COUNTING_LABEL_WIDTH, COUNTING_LABEL_HEIGHT, fieldFont, Color.white, "" + count);
		countLabel.setBackgroundColor(Color.red);
		countLabel.setAlignment(Alignment.CENTER);
		
		add(countLabel, this.getPosition(ReferencePoint.TOPRIGHT), ReferencePoint.CENTERCENTER, 0, 0);
	}
	
	public CountingButton(GUIContext context, int width, int height, Sprite sprite, Label label) {
		super(context, width, height, sprite, label);
		
		count = 0;
		countUpOnLeftClick = true;
		hideCount = false;
		disableAutoCount = false;
		
		Font fieldFont = GameDirector.sharedSceneListener().getFontManager().getFont(FontManager.FontID.FIELD);
		
		countLabel = new Label(container, COUNTING_LABEL_WIDTH, COUNTING_LABEL_HEIGHT, fieldFont, Color.white, "" + count);
		countLabel.setBackgroundColor(Color.red);
		countLabel.setAlignment(Alignment.CENTER);
		
		add(countLabel, this.getPosition(ReferencePoint.TOPRIGHT), ReferencePoint.CENTERCENTER, 0, 0);
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
		disableAutoCount = false;
		
		Font fieldFont = GameDirector.sharedSceneListener().getFontManager().getFont(FontManager.FontID.FIELD);
		
		countLabel = new Label(container, COUNTING_LABEL_WIDTH, COUNTING_LABEL_HEIGHT, fieldFont, Color.white, "" + count);
		countLabel.setBackgroundColor(Color.red);
		countLabel.setAlignment(Alignment.CENTER);
		
		add(countLabel, this.getPosition(ReferencePoint.TOPRIGHT), ReferencePoint.CENTERCENTER, 0, 0);
	}
	
	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		super.render(container, g);
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
			
			if (!disableAutoCount) {
				if (button == 0 && countUpOnLeftClick && count < max) {
					setCount(count + 1);
				} else if (button != 0 && countUpOnLeftClick && count > min) {
					setCount(count - 1);
				} else if (button == 0 && !countUpOnLeftClick && count > min) {
					setCount(count - 1);
				} else if (button != 0 && !countUpOnLeftClick && count < max) {
					setCount(count + 1);
				}
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
		
		countLabel.setText("" + count);
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
		
		countLabel.setVisible(!hideCount);
	}

	/**
	 * Get whether the button will auto count at all.
	 * @return Whether the button will auto count at all.
	 */
	public boolean getDisableAutoCount() {
		return disableAutoCount;
	}

	/**
	 * Set whether the button auto counts at all.
	 * @param disableAutoCount Whether the button auto counts at all
	 */
	public void setDisableAutoCount(boolean disableAutoCount) {
		this.disableAutoCount = disableAutoCount;
	}
}
