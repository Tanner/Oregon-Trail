package component;

import model.Condition;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

/**
 * A bar that represents the status of a {@code Conditioned} object as a progress bar.
 */
public class ConditionBar extends Component {
	private Condition condition;
	
	private double warningLevel = 0.50;
	private double dangerLevel = 0.25;
	
	private Color normalColor;
	private Color warningColor;
	private Color dangerColor;
	private Color backgroundColor;
	
	private boolean disableText;
	private Font font;
	
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
		
		normalColor = Color.green;
		warningColor = Color.yellow;
		dangerColor = Color.red;
		
		setDisableText(true);
		
		backgroundColor = Color.gray;
		
		setBackgroundColor(backgroundColor);
	}
	
	@Override
	public void render(GUIContext context, Graphics g) throws SlickException {
		if (!isVisible() || condition == null) {
			return;
		}
		
		super.render(context, g);
		
		double percentage = condition.getPercentage();
		Color barColor = normalColor;
		if (percentage < dangerLevel) {
			barColor = dangerColor;
		} else if (percentage < warningLevel) {
			barColor = warningColor;
		}
		
		g.setColor(barColor);
		g.fillRect(getX(), getY(), (float)(getWidth() * percentage), getHeight());
		
		Label label = new Label(context, getHeight(), getWidth(), font, Color.white, (percentage * 100)+"%");
		add(label, getPosition(ReferencePoint.CenterCenter), ReferencePoint.CenterCenter, 0, getHeight() / 2);
	}

	/**
	 * Get the current condition.
	 * @return Current condition
	 */
	public Condition getCondition() {
		return condition;
	}

	/**
	 * Set a new condition.
	 * @param condition New condition
	 */
	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	/**
	 * Get the background color.
	 * @return Background color
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Set a new background color.
	 * @param backgroundColor New background color
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Get whether or not the text is disabled.
	 * @return Whether or not the text is disabled
	 */
	public boolean getDisableText() {
		return disableText;
	}

	/**
	 * Set whether or not the text is disabled.
	 * @param disableText Whether or not the text is disabled
	 */
	public void setDisableText(boolean disableText) {
		this.disableText = disableText;
	}
	
	/**
	 * Set the font for the condition bar.
	 * @param font New font
	 */
	public void setFont(Font font) {
		this.font = font;
	}
}