package component;

import model.Condition;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import component.Label.Alignment;

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
	
	private boolean disableText;
	private Label label;
	
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
		
		setBackgroundColor(Color.gray);
		
		double percentage;
		if (condition == null) {
			percentage = 0;
		} else {
			percentage = condition.getPercentage();
		}
		Color barColor = normalColor;
		if (percentage < dangerLevel) {
			barColor = dangerColor;
		} else if (percentage < warningLevel) {
			barColor = warningColor;
		}
		
		Panel barPanel = new Panel(context, (int)(getWidth() * Math.min(percentage, 1)), getHeight(), barColor);
		this.add(barPanel, getPosition(ReferencePoint.TopLeft), ReferencePoint.TopLeft);
	}
	
	public ConditionBar(GUIContext context, int width, int height, Condition condition, Font font) {
		this(context, width, height, condition);
		
		String labelText = "";
		if (condition != null) {
			labelText = (condition.getPercentage() * 100)+"%";
		}
		
		label = new Label(context, getWidth(), getHeight(), font, Color.white, labelText);
		label.setAlignment(Alignment.Center);
		add(label, getPosition(ReferencePoint.CenterCenter), ReferencePoint.CenterCenter, 0, 0);
	}
	
	@Override
	public void render(GUIContext context, Graphics g) throws SlickException {
		if (!isVisible() || condition == null) {
			return;
		}
		
		double percentage = condition.getPercentage();
		if (label != null && !disableText) {
			label.setText((percentage * 100)+"%");
		}
		
		super.render(context, g);
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
		label.setFont(font);
	}
}