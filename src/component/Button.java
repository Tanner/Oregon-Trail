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
	private static final int CORNER_RADIUS = 2;
	
	private Label label;
	private Color buttonColor;
	private Color buttonActiveColor;
	protected boolean active;
	protected boolean disabled;
	private int topLeftCornerRadius;
	private int bottomLeftCornerRadius;
	private int topRightCornerRadius;
	private int bottomRightCornerRadius;
	private boolean beveled;
	
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
		
		beveled = true;
		
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
		
		beveled = true;
		
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
				
		Color color;
		if (active || disabled) {
			color = buttonActiveColor;
		} else {
			color = buttonColor;
		}
		Color brightColor = color.brighter(0.1f);
		Color darkColor = color.darker(0.2f);		

		g.setColor(color);
		// inner rect
		g.fillRect(getX() + CORNER_RADIUS,
				getY() + CORNER_RADIUS,
				getWidth() - CORNER_RADIUS * 2,
				getHeight() - CORNER_RADIUS * 2);
		
		// top bar
		if (beveled) {
			g.setColor(brightColor);
		} else {
			g.setColor(color);
		}
		g.fillRect(getX() + topLeftCornerRadius,
				getY(),
				getWidth() - topLeftCornerRadius - topRightCornerRadius,
				CORNER_RADIUS);
				
		// bottom bar
		if (beveled) {
			g.setColor(darkColor);
		} else {
			g.setColor(color);
		}
		g.fillRect(getX() + bottomLeftCornerRadius,
				getY() + getHeight() - CORNER_RADIUS,
				getWidth() - bottomLeftCornerRadius - bottomRightCornerRadius,
				CORNER_RADIUS);
				
		// left bar
		if (beveled) {
			g.setColor(brightColor);
		} else {
			g.setColor(color);
		}
		g.fillRect(getX(),
				getY() + topLeftCornerRadius,
				CORNER_RADIUS,
				getHeight() - topLeftCornerRadius - bottomLeftCornerRadius);
		
		// right bar
		if (beveled) {
			g.setColor(darkColor);
		} else {
			g.setColor(color);
		}
		g.fillRect(getX() + getWidth() - CORNER_RADIUS,
				getY() + topRightCornerRadius,
				CORNER_RADIUS,
				getHeight() - topRightCornerRadius - bottomRightCornerRadius);
		
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
			active = true;
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
			active = false;
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
	 * Sets the color of the button's background.
	 * @param color New color for the button
	 */
	public void setButtonColor(Color color) {
		buttonColor = color;
	}
	
	/**
	 * Sets the color of the button's background for the active state.
	 * @param color New color for the button in the active state
	 */
	public void setButtonActiveColor(Color color) {
		buttonActiveColor = color;
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
	
	/**
	 * Enable/disable rounded corners.
	 * @param rounded Enables rounded corners if {@code true}, disables if {@code false}
	 */
	public void setRoundedCorners(boolean rounded) {
		setTopLeftRoundedCorner(rounded);
		setBottomLeftRoundedCorner(rounded);
		setTopRightRoundedCorner(rounded);
		setBottomRightRoundedCorner(rounded);
	}
	
	/**
	 * Enable/disable top-left rounded corner.
	 * @param rounded Enables top-left rounded corner if {@code true}, disables if {@code false}
	 */
	public void setTopLeftRoundedCorner(boolean rounded) {
		if (rounded) {
			this.topLeftCornerRadius = CORNER_RADIUS;
		} else {
			this.topLeftCornerRadius = 0;
		}
	}
	
	/**
	 * Enable/disable bottom-left rounded corner.
	 * @param rounded Enables bottom-left rounded corner if {@code true}, disables if {@code false}
	 */
	public void setBottomLeftRoundedCorner(boolean rounded) {
		if (rounded) {
			this.bottomLeftCornerRadius = CORNER_RADIUS;
		} else {
			this.bottomLeftCornerRadius = 0;
		}
	}
	
	/**
	 * Enable/disable top-right rounded corner.
	 * @param rounded Enables top-right rounded corner if {@code true}, disables if {@code false}
	 */
	public void setTopRightRoundedCorner(boolean rounded) {
		if (rounded) {
			this.topRightCornerRadius = CORNER_RADIUS;
		} else {
			this.topRightCornerRadius = 0;
		}
	}
	
	/**
	 * Enable/disable bottom-right rounded corner.
	 * @param rounded Enables bottom-right rounded corner if {@code true}, disables if {@code false}
	 */
	public void setBottomRightRoundedCorner(boolean rounded) {
		if (rounded) {
			this.bottomRightCornerRadius = CORNER_RADIUS;
		} else {
			this.bottomRightCornerRadius = 0;
		}
	}
	
	/**
	 * Enable/disable a beveled appearance.
	 * @param beveled Enables beveled appearance if {@code true}, disables if {@code false}
	 */
	public void setBeveled(boolean beveled) {
		this.beveled = beveled;
	}
	
	@Override
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
	@Override
	public boolean isDisabled() {
		return disabled;
	}
}
