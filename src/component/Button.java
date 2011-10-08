package component;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import core.ConstantStore;

/**
 * A component that is a Button.
 * 
 * @author Tanner Smith
 */
public class Button extends Component {
	private static final int CORNER_RADIUS = 2;
	
	private Label label;
	private Color buttonColor;
	private Color buttonActiveColor;
	private boolean over;
	private boolean active;
	private boolean disabled;
	private int topLeftCornerRadius;
	private int bottomLeftCornerRadius;
	private int topRightCornerRadius;
	private int bottomRightCornerRadius;
	private boolean beveled;
		
	/**
	 * Creates a button.
	 * @param container Container for the  button
	 * @param label Label for the button
	 * @param width Width of the button
	 * @param height Height of the button
	 */
	public Button(GUIContext container, Label label, int width, int height) {
		super(container, width, height);
		
		this.label = label;
		label.setAlignment(Label.Alignment.Center);
		
		buttonColor = ConstantStore.COLORS.get("INTERACTIVE_NORMAL");
		buttonActiveColor = ConstantStore.COLORS.get("INTERACTIVE_ACTIVE");
		
		beveled = true;
		
		container.getInput().addMouseListener(this);
	}
	
	/**
	 * Creates a button.
	 * @param container Container for the button
	 * @param label Label for the button
	 * @param origin Position of the button
	 */
	public Button(GUIContext container, Label label) {
		this(container, label, label.getWidth(), label.getHeight());
	}

	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		if (!visible) {
			return;
		}
		
		super.render(container, g);
		
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
		
		label.render(container, g);
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		if (!visible) {
			return;
		}
		
		over = getArea().contains(newx, newy);
	}
	
	@Override
	public void mousePressed(int button, int mx, int my) {
		if (!visible || !isAcceptingInput()) {
			return;
		}
		
		if (button == 0 && over && !disabled) {
			active = true;
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
	
	public void setButtonActiveColor(Color color) {
		buttonActiveColor = color;
	}
	
	public void setFont(Font font) {
		label.setFont(font);
	}
	
	public void setLabelColor(Color color) {
		label.setColor(color);
	}
	
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
	public void setRoundedCorners(boolean rounded) {
		setTopLeftRoundedCorner(rounded);
		setBottomLeftRoundedCorner(rounded);
		setTopRightRoundedCorner(rounded);
		setBottomRightRoundedCorner(rounded);
	}
	
	public void setTopLeftRoundedCorner(boolean rounded) {
		if (rounded) {
			this.topLeftCornerRadius = CORNER_RADIUS;
		} else {
			this.topLeftCornerRadius = 0;
		}
	}
	
	public void setBottomLeftRoundedCorner(boolean rounded) {
		if (rounded) {
			this.bottomLeftCornerRadius = CORNER_RADIUS;
		} else {
			this.bottomLeftCornerRadius = 0;
		}
	}
	
	public void setTopRightRoundedCorner(boolean rounded) {
		if (rounded) {
			this.topRightCornerRadius = CORNER_RADIUS;
		} else {
			this.topRightCornerRadius = 0;
		}
	}
	
	public void setBottomRightRoundedCorner(boolean rounded) {
		if (rounded) {
			this.bottomRightCornerRadius = CORNER_RADIUS;
		} else {
			this.bottomRightCornerRadius = 0;
		}
	}
	
	public void setBeveled(boolean beveled) {
		this.beveled = beveled;
	}
}
