package component;

import model.Condition;
import model.Conditioned;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

/**
 * A bar that represents the status of a {@code Conditioned} object as a progress bar.
 */
public class ConditionBar extends Component {
	private Condition condition;
	private Color barColor;
	private Color backgroundColor;
	
	/**
	 * Constructs a new {@code ConditionBar} with a {@code GUIContext}, width, height, and a {@code Conditioned}.
	 * @param context The GUI context
	 * @param width The width
	 * @param height The height
	 * @param condition The {@code Conditioned} to use
	 */
	public ConditionBar(GUIContext context, int width, int height, Condition condition) {
		super(context, width, height);
		
		this.condition = condition;
		
		barColor = Color.green;
		backgroundColor = Color.gray;
	}
	
	@Override
	public void render(GUIContext context, Graphics g) throws SlickException {
		if (!isVisible()) {
			return;
		}
		
		super.render(context, g);
		
		g.setColor(backgroundColor);
		g.fillRect(getX(), getY(), getWidth(), getHeight());
		
		g.setColor(barColor);
		g.fillRect(getX(), getY(), (float)(getWidth() * condition.getPercentage()), getHeight());
	}

	/**
	 * Get the color of the bar.
	 * @return Bar color
	 */
	public Color getBarColor() {
		return barColor;
	}

	/**
	 * Change the bar color.
	 * @param barColor The new bar color
	 */
	public void setBarColor(Color barColor) {
		this.barColor = barColor;
	}

	/**
	 * Get the color of the background.
	 * @return Background color
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Change the background color.
	 * @param backgroundColor The new background color
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
}