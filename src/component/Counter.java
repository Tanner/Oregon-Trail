package component;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;
import component.Label.Alignment;
import component.sprite.Sprite;
import core.FontManager;

/**
 * {@code CountingButton} inherits from {@code Button} to add a flag that counts up or down.
 */
public class Counter extends Component implements Disableable {
	private static final int COUNTING_LABEL_WIDTH = 25; 
	private static final int COUNTING_LABEL_HEIGHT = 25; 
	
	private int count;
	private int min = 0;
	private int max = Integer.MAX_VALUE;
	private boolean countUpOnLeftClick;
	private boolean disableAutoCount;
	private boolean hideCount;
	private Label countLabel;
	private CountingButton button;
	
	/**
	 * Constructs a {@code CountingButton} with a width and height.
	 * @param container The GUI context
	 * @param width Width of the button
	 * @param height Height of the button
	 * @param label Label for the button
	 */
	public Counter(GUIContext context, int width, int height, Label label) {
		super(context, width, height);
		
		count = 0;
		countUpOnLeftClick = true;
		hideCount = false;
		disableAutoCount = false;
		
		button = new CountingButton(context, width, height, label);
		add(button, getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
		
		Font fieldFont = FontManager.get(FontManager.FontID.FIELD);
		countLabel = new Label(container, COUNTING_LABEL_WIDTH, COUNTING_LABEL_HEIGHT, fieldFont, Color.white, "" + count);
		countLabel.setBackgroundColor(Color.red);
		countLabel.setAlignment(Alignment.CENTER);
		
		add(countLabel, getPosition(ReferencePoint.TOPRIGHT), ReferencePoint.CENTERCENTER);
	}
	
	public Counter(GUIContext context, int width, int height, Sprite sprite, Label label) {
		super(context, width, height);
		
		count = 0;
		countUpOnLeftClick = true;
		hideCount = false;
		disableAutoCount = false;
		
		button = new CountingButton(context, width, height, sprite, label);
		add(button, getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
		
		Font fieldFont = FontManager.get(FontManager.FontID.FIELD);
		countLabel = new Label(container, COUNTING_LABEL_WIDTH, COUNTING_LABEL_HEIGHT, fieldFont, Color.white, "" + count);
		countLabel.setBackgroundColor(Color.red);
		countLabel.setAlignment(Alignment.CENTER);
		
		add(countLabel, this.getPosition(ReferencePoint.TOPRIGHT), ReferencePoint.CENTERCENTER, 0, 0);
	}
	
	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		super.render(container, g);
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
	
	@Override
	public boolean isDisabled() {
		return button.isDisabled();
	}

	@Override
	public void setDisabled(boolean disabled) {
		button.setDisabled(disabled);
	}
	
	private class CountingButton extends Button {
		public CountingButton(GUIContext context, int width, int height, Label label) {
			super(context, width, height, label);
		}

		public CountingButton(GUIContext context, int width, int height, Sprite sprite, Label label) {
			super(context, width, height, sprite, label);
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
				Counter.this.notifyListeners();
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
	}

	public void setText(String text) {
		button.setText(text);
	}
	
	public void setSprite(Sprite sprite) {
		button.setSprite(sprite);
	}

	public void setShowLabel(boolean showLabel) {
		button.setShowLabel(showLabel);
	}
	
	public void setShowSprite(boolean showSprite) {
		button.setShowSprite(showSprite);
	}
}

