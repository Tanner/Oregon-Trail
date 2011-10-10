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
	
	private double warningLevel = 0.50;
	private double dangerLevel = 0.25;
	
	private Color normalColor;
	private Color warningColor;
	private Color dangerColor;
	
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
		
		setBackgroundColor(Color.gray);
	}
	
	@Override
	public void render(GUIContext context, Graphics g) throws SlickException {
		if (!isVisible()) {
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
	}
}