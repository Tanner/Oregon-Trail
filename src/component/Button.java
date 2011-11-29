package component;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import component.sprite.Sprite;

import core.ConstantStore;
import core.SoundStore;

/**
 * {@code Button} inherits from {@code Component} to extend features
 * that provides necessary functionality to behave like a button.
 */
public class Button extends Component implements Disableable {
	private Label label;
	private Sprite sprite;
	
	private Color buttonColor;
	private Color buttonActiveColor;
	private Color buttonDisabledColor;
	
	private boolean active;
	private boolean disabled;
	
	/**
	 * Constructs a {@code Button} with a width, a height, and a {@code Label}.
	 * @param context The GUI context
	 * @param width The width
	 * @param height The height
	 */
	public Button(GUIContext context, int width, int height) {
		super(context, width, height);
		
		buttonColor = ConstantStore.COLORS.get("INTERACTIVE_NORMAL");
		buttonActiveColor = ConstantStore.COLORS.get("INTERACTIVE_ACTIVE");
		buttonDisabledColor = ConstantStore.COLORS.get("INTERACTIVE_DISABLED");
		
		setBackgroundColor(buttonColor);
		setBevel(BevelType.OUT);
		setBevelWidth(2);
		setBorderColor(Color.black);
		setBorderWidth(2);
	}
	
	/**
	 * Constructs a {@code Button} with a width, a height, and a {@code Label}.
	 * @param context The GUI context
	 * @param width The width
	 * @param height The height
	 * @param label A label
	 */
	public Button(GUIContext context, int width, int height, Label label) {
		this(context, width, height);
		
		setLabel(label);
	}
	/**
	 * Constructs a {@code Button} with a width, a height, and a {@code Label}.
	 * @param context The GUI context
	 * @param width The width
	 * @param height The height
	 * @param label A label
	 */
	public Button(GUIContext context, int width, int height, Color color) {
		this(context, width, height);
		
		setLabel(label);
		setBackgroundColor(color);
		
	}
	
	/**
	 * Constructs a {@code Button} with a width, a height, a {@code Sprite},
	 * and a {@code Label}.
	 * @param context The GUI context
	 * @param width The width
	 * @param height The height
	 * @param sprite A sprite
	 * @param label A label
	 */
	public Button(GUIContext context, int width, int height, Sprite sprite, Label label) {
		this(context, width, height);
		
		setSprite(sprite);
		setLabel(label);
	}
	
	/**
	 * Generates the layout.
	 */
	public void layout() {
		if (label != null && label.isVisible() && sprite != null && sprite.isVisible()) {
			sprite.setPosition(this.getPosition(ReferencePoint.CENTERCENTER), ReferencePoint.BOTTOMCENTER);
			label.setPosition(this.getPosition(ReferencePoint.CENTERCENTER), ReferencePoint.TOPCENTER);
		} else {
			if (label != null) {
				label.setPosition(this.getPosition(ReferencePoint.CENTERCENTER), ReferencePoint.CENTERCENTER);
			}
			
			if (sprite != null) {
				sprite.setPosition(this.getPosition(ReferencePoint.CENTERCENTER), ReferencePoint.CENTERCENTER);
			}
		}
	}
	
	/**
	 * Sets the {@code Label}.
	 * @param label New Label to use
	 */
	public void setLabel(Label label) {
		if (this.label != null) {
			remove(this.label);
		}
		
		this.label = label;
		
		if (label != null) {
			add(label, getPosition(Positionable.ReferencePoint.CENTERCENTER), Positionable.ReferencePoint.CENTERCENTER);
		}
		
		layout();
	}
	
	/**
	 * Sets the {@code Sprite}.
	 * @param sprite New Sprite to use
	 */
	public void setSprite(Sprite sprite) {
		if (this.sprite != null) {
			remove(this.sprite);
		}
		
		this.sprite = sprite;
		
		if (sprite != null) {
			add(sprite, getPosition(Positionable.ReferencePoint.CENTERCENTER), Positionable.ReferencePoint.CENTERCENTER);
		}
		
		layout();
	}
	
	/**
	 * Sets whether or not the {@code Label} is shown.
	 * @param showLabel Whether or not the label is shown (true is visible)
	 */
	public void setShowLabel(boolean showLabel) {
		if (this.label == null) {
			return;
		}
		
		label.setVisible(showLabel);
		
		layout();
	}
	
	/**
	 * Sets whether or not the {@code Sprite} is shown.
	 * @param showSprite Whether or not the Sprite is shown (true is visible)
	 */
	public void setShowSprite(boolean showSprite) {
		if (this.sprite == null) {
			return;
		}
		
		sprite.setVisible(showSprite);
		
		layout();
	}

	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		if (!isVisible()) {
			return;
		}
		
		super.render(container, g);
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		if (!isVisible() || !isAcceptingInput()) {
			return;
		}
		
		super.mouseMoved(oldx, oldy, newx, newy);
	}
	
	@Override
	public void mousePressed(int button, int mx, int my) {
		if (!isVisible() || !isAcceptingInput()) {
			return;
		}
		
		super.mousePressed(button, mx, my);
		
		if (button == 0 && isMouseOver() && !disabled) {
			setActive(true);
			input.consumeEvent();
		}
	}
	
	@Override
	public void mouseReleased(int button, int mx, int my) {
		if (!isVisible() || !isAcceptingInput()) {
			return;
		}
		
		super.mouseReleased(button, mx, my);
				
 		if (button == 0 && isMouseOver() && !disabled && active) {
 			SoundStore.get().playSound("Click");
			notifyListeners();
			input.consumeEvent();
		}
 		
		setActive(false);
	}
	
	/**
	 * Sets the color of the button's normal background.
	 * @param color New color for the button
	 */
	public void setButtonColor(Color color) {
		buttonColor = color;
		
		// Change current color now if not disabled nor active
		if (!disabled && !active) {
			setBackgroundColor(buttonColor);
		}
	}
	
	/**
	 * Sets the color of the button's background for the active state.
	 * @param color New color for the button in the active state
	 */
	public void setButtonActiveColor(Color color) {
		buttonActiveColor = color;
	}

	/**
	 * Sets the color of the button's background for the disabled state.
	 * @param color New color for the button in the disabled state
	 */
	public void setButtonDisabledColor(Color color) {
		buttonDisabledColor = color;
	}
	
	/**
	 * Sets the font of the label.
	 * @param font The new font for the label
	 */
	public void setFont(Font font) {
		if (label == null) {
			return;
		}
		
		label.setFont(font);
	}
	
	/**
	 * Sets the color of the label.
	 * @param color The new color for the label
	 */
	public void setLabelColor(Color color) {
		if (label == null) {
			return;
		}
		
		label.setColor(color);
	}
	
	/**
	 * Sets the text of this button.
	 * @param text New text
	 */
	public void setText(String text) {
		if (label == null) {
			return;
		}
		
		label.setText(text);
	}
	
	/**
	 * Gets the text in this button.
	 * @return Text in the button
	 */
	public String getText() {
		if (label == null) {
			return "";
		}
		
		return label.getText();
	}
	
	@Override
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
		
		if (disabled) {
			this.active = false;
			
			setBackgroundColor(buttonDisabledColor);
			setBevel(BevelType.NONE);
			
			if (label != null) {
				label.setColor(ConstantStore.COLORS.get("INTERACTIVE_LABEL_DISABLED"));
			}
		} else {
			if (isActive()) {
				setBackgroundColor(buttonActiveColor);
				setBevel(BevelType.IN);
			} else {
				setBackgroundColor(buttonColor);
				setBevel(BevelType.OUT);
			}
			
			if (label != null) {
				label.setColor(ConstantStore.COLORS.get("INTERACTIVE_LABEL_NORMAL"));
			}
		}
	}
	
	@Override
	public boolean isDisabled() {
		return disabled;
	}
	
	/**
	 * Return the button's active status.
	 * @return If the button is active or not
	 */
	public boolean isActive() {
		return active;
	}
	
	/**
	 * Change whether this button is active or not.
	 * @param active New active state
	 */
	public void setActive(boolean active) {
		if (isDisabled()) {
			return;
		}
		
		this.active = active;
		
		if (active) {
			setBackgroundColor(buttonActiveColor);
			setBevel(BevelType.IN);
		} else {
			setBackgroundColor(buttonColor);
			setBevel(BevelType.OUT);
		}
	}
	
	@Override
	public String toString() {
		return "Button[active: " + active +", label: " + label + "]";
	}
}
