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
	
	private ConditionPanel conditionPanel;
	
	/**
	 * Constructs a new {@code ConditionBar} with a {@code GUIContext}, width, height, and a {@code Conditioned}.
	 * @param context The GUI context
	 * @param width The width
	 * @param height The height
	 * @param condition The {@code Conditioned} to use
	 */
	public ConditionBar(GUIContext context, int width, int height, Condition condition) {
		super(context, width, height);

		conditionPanel = new ConditionPanel(context, getWidth(), getHeight(), Color.gray);
		add(conditionPanel, getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
		
		setCondition(condition);
		setDisableText(true);

		normalColor = new Color(0x00a300);
		warningColor = new Color(0xe0e000);
		dangerColor = Color.red;
	}
	
	/**
	 * Constructs a new {@code ConditionBar} with a {@code GUIContext}, width, height, a {@code Conditioned}, and a {@code Font} for text.
	 * @param context The GUI context
	 * @param width The width
	 * @param height The height
	 * @param condition The {@code Conditioned} to use
	 * @param font Font to use for the text overlay
	 */
	public ConditionBar(GUIContext context, int width, int height, Condition condition, Font font) {
		this(context, width, height, condition);
				
		label = new Label(context, getWidth(), getHeight(), font, Color.white, "0%");
		label.setAlignment(Alignment.CENTER);
		
		updateLabelText();
		
		add(label, getPosition(ReferencePoint.CENTERCENTER), ReferencePoint.CENTERCENTER, 0, 0);
		
		setDisableText(false);
	}
	
	@Override
	public void render(GUIContext context, Graphics g) throws SlickException {
		if (!isVisible() || condition == null) {
			return;
		}
		
		super.render(context, g);
	}
	
	/**
	 * Get the bar color to use.
	 * @return Bar color that corresponds with the {@code Condition} value
	 */
	public Color getBarColor() {
		Color barColor = normalColor;
		if (condition.getPercentage() < dangerLevel) {
			barColor = dangerColor;
		} else if (condition.getPercentage() < warningLevel) {
			barColor = warningColor;
		}
		
		return barColor;
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
		
		updateLabelText();
	}
	
	/**
	 * Update the label text with whatever condition value is present.
	 */
	public void updateLabelText() {
		if (condition != null) {
			double percentage = condition.getPercentage();
			if (label != null) {
				label.setText((int) (percentage * 100) + "%");
			}
			
			conditionPanel.setPercentage(percentage);
		}
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
		
		if (label != null) {
			label.setVisible(!disableText);
		}
	}
	
	/**
	 * Set the font for the condition bar.
	 * @param font New font
	 */
	public void setFont(Font font) {
		label.setFont(font);
	}
	
	private class ConditionPanel extends Panel {
		private double percentage;
		
		/**
		 * Constructs a new ConditionPanel with a {@code GUIContext}, width, height, and {@code Color}.
		 * @param context Container
		 * @param width Width
		 * @param height Height
		 * @param color Background color
		 */
		public ConditionPanel(GUIContext context, int width, int height, Color color) {
			super(context, width, height, color);
			
			percentage = 0;
		}
		
		@Override
		public void render(GUIContext context, Graphics g) throws SlickException {
			if (!isVisible()) {
				return;
			}
			
			super.render(context, g);
			
			g.setColor(getBarColor());
			g.fillRect(getX(), getY(), (int) (getWidth() * percentage), getHeight());
		}
		
		/**
		 * Sets the percentage of the bar.
		 * @param percentage New percentage
		 */
		public void setPercentage(double percentage) {
			this.percentage = percentage;
		}
	}
}