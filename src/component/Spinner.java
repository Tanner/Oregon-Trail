package component;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.*;

import component.Label.Alignment;

/**
 * {@code Spinner} inherits from {@code Component} to extend features that
 * that allow the user to select specific choices.
 * Scrolls up or down through a list of options that each represent
 * a specific ordinal value.  Does not allow the user to go below/above
 * the 0/MAX_STATE values.
 */
public class Spinner extends Component {
	private final double PADDING = 1.25;
	private final int MAX_STATE;
		
	private String[] fields;
	private Label label;
	private int state;
	
	private boolean treatAsNumbers;
	
	private Button upButton, downButton, labelButton;
	private ButtonListener listener;
	
	/**
	 * Creates a new Spinner.  Note if you pass in true before the list of Strings,
	 * it will assume all Strings will contain ints, and it will return the int value
	 * of the String instead of the state
	 * @param context The GUI context
	 * @param font The font of the Spinner label
	 * @param c The color of the Spinner label
	 * @param width The width of the spinner
	 * @param height The height of the spinner
	 * @param treatAsNumbers Instead of returning the ordinal of a state, returns the value of the field parsed to an int
	 * @param fields A variable length list of Strings that will be displayed for each state (first String is state 0)
	 */
	public Spinner(GUIContext context, int width, int height, Font font, Color c, boolean treatAsNumbers, String ... fields) {
		super(context, width, height);
		
		MAX_STATE = fields.length - 1;
		state = 0;
		this.fields = fields;
		this.treatAsNumbers = treatAsNumbers;
		
		listener = new ButtonListener();

		Label upLabel = new Label(context, font, c, "Up");
		Label downLabel = new Label(context, font, c, "Down");
		int butWidth = (int)(font.getWidth("Down")*PADDING);
		label = new Label(context, width - butWidth, font, c, fields[0]);
		label.setAlignment(Alignment.Center);
		upButton = new Button(context, butWidth, height/2, upLabel);
		downButton = new Button(context, butWidth, height/2, downLabel);
		labelButton = new Button(context, width-butWidth, height, label);
		
		//Set rounded corners, disable the label button and re-color it
		upButton.setTopLeftRoundedCorner(true);
		downButton.setBottomLeftRoundedCorner(true);
		labelButton.setTopRightRoundedCorner(true);
		labelButton.setBottomRightRoundedCorner(true);
		labelButton.setDisabled(true);
		labelButton.setButtonActiveColor(Color.gray);
		
		add(upButton, getPosition(ReferencePoint.TopLeft), Positionable.ReferencePoint.TopLeft);
		add(downButton, this.getPosition(ReferencePoint.BottomLeft), Positionable.ReferencePoint.BottomLeft);
		//Sets the label text to be in the center of the textbox
		add(labelButton, upButton.getPosition(ReferencePoint.TopRight), Positionable.ReferencePoint.TopLeft);
		
		listener = new ButtonListener();
		upButton.addListener(listener);
		downButton.addListener(listener);
		
		refreshState();
	}
	
	/**
	 * Gets the current state of the Spinner. State 0 corresponds
	 * to the first String of the constructor.
	 * Alternate behavior is you can pass in a String of numbers,
	 * and if treatAsNumbers is true, it 
	 * 
	 * @return The current state
	 */
	public int getState() {
		return (!treatAsNumbers) ? state : Integer.parseInt(fields[state]);
	}
	
	/**
	 * Gets the text representation of the current state of the Spinner.
	 * @return The current state in String form
	 */
	public String getText() {
		return fields[state];
	}

	@Override
	public void render(GUIContext context, Graphics g) throws SlickException {
		if (!isVisible()) {
			return;
		}
		
		super.render(context, g);
	}
	
	/**
	 * Refresh the buttons based off of the state.
	 */
	private void refreshState() {
		if (state == 0) {
			upButton.setDisabled(false);
			downButton.setDisabled(true);
		} else if (state == MAX_STATE) {
			upButton.setDisabled(true);
			downButton.setDisabled(false);
		} else {
			upButton.setDisabled(false);
			downButton.setDisabled(false);
		}
	}

	/**
	 * A listener for the up/down buttons of {@code Spinner}.
	 */
	private class ButtonListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent source) {
			if (source == upButton) {
				state = (state == MAX_STATE) ? state : state + 1;
			} else {
				state = (state == 0) ? 0 : state - 1;
			}
			label.setText(fields[state]);
			refreshState();
		}
	}
}
