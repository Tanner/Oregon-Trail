package component;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.*;


/**
 * A Spinner component that allows the user to select specific choices.
 * Scrolls up or down through a list of options that each represent
 * a specific ordinal value.  Does not allow the user to go below/above
 * the 0/MAX_STATE values.
 * 
 * @author Jeremy
 */
public class Spinner extends Component {
	
	//The padding between the buttons and the text
	private final int PADDING = 20;
	private final int MAX_STATE;
	private String[] fields;
	private Label label;
	private int state;
	private int x, y;
	private Font font;
	private boolean treatAsNumbers;
	
	private MouseOverArea upButton, downButton;
	private ButtonListener listener;
	
	
	/**
	 * Creates a new Spinner.  Note if you pass in true before the list of Strings,
	 * it will assume all Strings will contain ints, and it will return the int value
	 * of the String instead of the state
	 * 
	 * @param context The game container
	 * @param x The x position of the component
	 * @param y The y position of the component
	 * @param font The font of the Spinner label
	 * @param c The color of the Spinner label
	 * @param up The image for the up button
	 * @param down The image for the down button
	 * @param treatAsNumbers Instead of returning the ordinal of a state, returns the value of the field parsed to an int
	 * @param fields A variable length list of Strings that will be displayed for each state (first String is state 0)
	 */
	public Spinner(GUIContext context, int x, int y, Font font, Color c, Image up, Image down, boolean treatAsNumbers, String ... fields) {
		super(context);
		state = 0;
		MAX_STATE = fields.length - 1;
		this.x = x;
		this.y = y;
		this.fields = fields;
		this.font = font;
		this.treatAsNumbers = treatAsNumbers;
		listener = new ButtonListener();
		System.out.println("About to make buttons in constructor");
		upButton = new MouseOverArea(context, up, x, y, listener);
		downButton = new MouseOverArea(context, down, x, y + upButton.getHeight(), listener);
		int textY = (upButton.getHeight()*2-font.getLineHeight()) / 2;
		label = new Label(context, x + upButton.getWidth() + PADDING, y + textY, font, c, fields[0]);
	}
	
	/**
	 * Gets the current state of the Spinner.  State 0 corresponds
	 * to the first String of the constructor.
	 * 
	 * Alternate behavior is you can pass in a String of numbers,
	 * and if treatAsNumbers is true, it 
	 * 
	 * @return the current state
	 */
	public int getState() {
		return (!treatAsNumbers) ? state : Integer.parseInt(fields[state]);
	}

	@Override
	public int getHeight() {
		return upButton.getHeight() * 2;
	}

	@Override
	public int getWidth() {
		return upButton.getWidth() + PADDING + font.getWidth(fields[state]);
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public void render(GUIContext context, Graphics g) throws SlickException {
		label.render(context, g);
		upButton.render(context, g);
		downButton.render(context,g);
	}

	@Override
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
		if (upButton != null ) {
			upButton.setLocation(x,y);
			downButton.setLocation(x, y + upButton.getHeight());
			int textY = (upButton.getHeight()*2-font.getLineHeight()) / 2;
			label.setLocation(x + upButton.getWidth() + PADDING, y + textY);
		}
	}
	
	/**
	 * A listener that checks if an up/down button was pressed,
	 * and updates the spinner's state.
	 * 
	 * @author Jeremy
	 */
	private class ButtonListener implements ComponentListener {
		public void componentActivated(AbstractComponent source) {
			if (source == upButton) {
				state = (state == MAX_STATE) ? state : state + 1;
			}
			else {
				state = (state == 0) ? 0 : state - 1;
			}
			label.setText(fields[state]);
		}
	}
}
