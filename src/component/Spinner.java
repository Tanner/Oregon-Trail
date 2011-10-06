package component;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.*;

/**
 * A Spinner component that allows the user to select specific choices.
 * Scrolls up or down through a list of options that each represent
 * a specific ordinal value.  Does not allow the user to go below/above
 * the 0/MAX_STATE values.
 * 
 * @author Jeremy Grebner
 */
public class Spinner extends Component {
	private final double PADDING = 1.25;
	private final int MAX_STATE;
	
	private int width, height;
	private Vector2f position;
	
	private String[] fields;
	private Label label;
	private int state;
	
	
	private boolean treatAsNumbers;
	
	private Font font;
	private Button upButton, downButton;
	private ButtonListener listener;
	
	/**
	 * Creates a new Spinner.  Note if you pass in true before the list of Strings,
	 * it will assume all Strings will contain ints, and it will return the int value
	 * of the String instead of the state
	 * 
	 * @param context The game container
	 * @param font The font of the Spinner label
	 * @param c The color of the Spinner label
	 * @param width The width of the spinner
	 * @param height The height of the spinner
	 * @param treatAsNumbers Instead of returning the ordinal of a state, returns the value of the field parsed to an int
	 * @param fields A variable length list of Strings that will be displayed for each state (first String is state 0)
	 */
	public Spinner(GUIContext context, Font font, Color c, int width, int height, boolean treatAsNumbers, String ... fields) {
		super(context);
		
		state = 0;
		MAX_STATE = fields.length - 1;
		this.fields = fields;
		this.font = font;
		this.treatAsNumbers = treatAsNumbers;
		this.width = width;
		this.height = height;
		
		listener = new ButtonListener();

		Label upLabel = new Label(context, font, c, "Up");
		Label downLabel = new Label(context, font, c, "Down");
		int butWidth = (int)(font.getWidth("Down")*PADDING);
		upButton = new Button(context, upLabel, butWidth, height/2);
		downButton = new Button(context, downLabel, butWidth,height/2);
		
		listener = new ButtonListener();
		upButton.addListener(listener);
		downButton.addListener(listener);
		
		label = new Label(context, font, c, fields[0], width - butWidth);
		
		refreshState();
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
	
	/**
	 * Gets the text representation of the current state of the Spinner.
	 * 
	 * @return the current state in String form
	 */
	public String getText() {
		return fields[state];
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getX() {
		return (int) position.getX();
	}

	@Override
	public int getY() {
		return (int) position.getY();
	}

	@Override
	public void render(GUIContext context, Graphics g) throws SlickException {
		if (!visible) {
			return;
		}
		
		g.setColor(Color.gray);
		g.fillRect(position.x, position.y, width, height);
		label.render(context, g);
		upButton.render(context, g);
		downButton.render(context, g);
	}
	
	private void refreshState() {
		label.setText(fields[state]);
		label.setPosition(upButton.getPosition(ReferencePoint.BottomRight),
				Positionable.ReferencePoint.CenterLeft,
				(width - upButton.getWidth() - font.getWidth(fields[state])) / 2,0);
		
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

	@Override
	public void setLocation(int x, int y) {
		if (position == null)
			position = new Vector2f(x,y);
		else
			position.set(x,y);
		if (upButton != null ) {
			upButton.setPosition(this.getPosition(ReferencePoint.TopLeft), Positionable.ReferencePoint.TopLeft);
			downButton.setPosition(this.getPosition(ReferencePoint.BottomLeft), Positionable.ReferencePoint.BottomLeft);
			//Sets the label text to be in the center of the textbox
			label.setPosition(upButton.getPosition(ReferencePoint.BottomRight),
					Positionable.ReferencePoint.CenterLeft,
					(width - upButton.getWidth() - font.getWidth(fields[state])) / 2,0);
		}
	}
	
	/**
	 * A listener that checks if an up/down button was pressed,
	 * and updates the spinner's state.
	 * 
	 * @author Jeremy Grebner
	 */
	private class ButtonListener implements ComponentListener {
		public void componentActivated(AbstractComponent source) {
			if (source == upButton) {
				state = (state == MAX_STATE) ? state : state + 1;
			} else {
				state = (state == 0) ? 0 : state - 1;
			}
			
			refreshState();
		}
	}

	@Override
	public void setWidth(int width) {
		// TODO
	}

	@Override
	public void setHeight(int height) {
		// TODO
	}
}
