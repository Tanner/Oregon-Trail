package component;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import component.sprite.Sprite;

import core.ConstantStore;

/**
 * {@code Button} inherits from {@code Component} to extend features that provides necessary
 * functionality to behave like a button.
 */
public class Button extends Component implements Disableable {	
	private Label label;
	
	private Color buttonColor;
	private Color buttonActiveColor;
	private Color buttonDisabledColor;
	private Color buttonBorderColor;
	
	protected boolean active;
	protected boolean disabled;
	
	/**
	 * Constructs a {@code Button} with a width, a height, a {@code Sprite}, and a {@code Label}.
	 * @param context The GUI context
	 * @param width The width
	 * @param height The height
	 * @param sprite A sprite
	 * @param label A label
	 */
	public Button(GUIContext context, int width, int height, Sprite sprite, Label label) {
		super(context, width, height);
		
		add(sprite, getPosition(Positionable.ReferencePoint.TopCenter), Positionable.ReferencePoint.TopCenter);
		
		this.label = label;
		add(label, getPosition(Positionable.ReferencePoint.BottomCenter), Positionable.ReferencePoint.BottomCenter);
		
		buttonColor = ConstantStore.COLORS.get("INTERACTIVE_NORMAL");
		buttonActiveColor = ConstantStore.COLORS.get("INTERACTIVE_ACTIVE");
		buttonDisabledColor = ConstantStore.COLORS.get("INTERACTIVE_DISABLED");
		buttonBorderColor = ConstantStore.COLORS.get("INTERACTIVE_BORDER_DARK");
		
		setBackgroundColor(buttonColor);
		setBevel(BevelType.Out);
		setBevelWidth(2);
		setBorderColor(buttonBorderColor);
		setBorderWidth(2);
		
		container.getInput().addMouseListener(this);
	}
	
	/**
	 * Constructs a {@code Button} with a width, a height, and a {@code Label}.
	 * @param context The GUI context
	 * @param width The width
	 * @param height The height
	 * @param label A label
	 */
	public Button(GUIContext context, int width, int height, Label label) {
		super(context, width, height);
		
		this.label = label;
		this.add(label, getPosition(Positionable.ReferencePoint.CenterCenter), Positionable.ReferencePoint.CenterCenter);
		
		buttonColor = ConstantStore.COLORS.get("INTERACTIVE_NORMAL");
		buttonActiveColor = ConstantStore.COLORS.get("INTERACTIVE_ACTIVE");
		buttonDisabledColor = ConstantStore.COLORS.get("INTERACTIVE_DISABLED");
		
		setBackgroundColor(buttonColor);
		setBevel(BevelType.Out);
		setBevelWidth(2);
		setBorderColor(Color.black);
		setBorderWidth(2);
		
		container.getInput().addMouseListener(this);
	}
	
	/**
	 * Constructs a {@code Button} with a {@code Label}.
	 * @param context The GUI context
	 * @param label A label
	 */
	public Button(GUIContext container, Label label) {
		this(container, label.getWidth(), label.getHeight(), label);
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
		if (!isVisible()) {
			return;
		}
		
		super.mouseMoved(oldx, oldy, newx, newy);
	}
	
	@Override
	public void mousePressed(int button, int mx, int my) {
		if (!isVisible() || !isAcceptingInput()) {
			return;
		}
		
		if (button == 0 && isMouseOver() && !disabled) {
			setActive(true);
			input.consumeEvent();
		}
	}
	
	@Override
	public void mouseReleased(int button, int mx, int my) {
		if (!isVisible()) {
			return;
		}
		
 		if (button == 0 && isMouseOver() && !disabled && active) {
			notifyListeners();
			input.consumeEvent();
			setActive(false);
		}
	}
	
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);

		if (label != null) {
			label.setPosition(this.getPosition(Positionable.ReferencePoint.CenterCenter), Positionable.ReferencePoint.CenterCenter);
		}
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
		label.setFont(font);
	}
	
	/**
	 * Sets the color of the label.
	 * @param color The new color for the label
	 */
	public void setLabelColor(Color color) {
		label.setColor(color);
	}
	
	@Override
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
		
		if (disabled) {
			setBackgroundColor(buttonDisabledColor);
			setBevel(BevelType.None);
		} else {
			if (isActive()) {
				setBackgroundColor(buttonActiveColor);
				setBevel(BevelType.In);
			} else {
				setBackgroundColor(buttonColor);
				setBevel(BevelType.Out);
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
		this.active = active;
		if (active) {
			setBackgroundColor(buttonActiveColor);
			setBevel(BevelType.In);
		} else {
			setBackgroundColor(buttonColor);
			setBevel(BevelType.Out);
		}
	}
}
