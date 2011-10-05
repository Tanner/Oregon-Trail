package component;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.*;

/**
 * A component with multiple buttons.  Only one button is allowed to be
 * set at a time.  The current button stands for the 
 * 
 * @author Jeremy
 * @version 10/04/2011
 */
public class SegmentedControl extends Component {
	
	private int height, width;
	private Vector2f position;
	
	private final int STATES;
	private int state;
	private Button[] buttons;
	
	
	/**
	 * Create a new SegmentedControl with a given font, color, width, height.
	 * The variable list of strings dictate the number of buttons created.
	 * All buttons are created with the same width.
	 * Only one button is allowed to be selected at a time.
	 *  
	 * @param context The game's GUIContext object
	 * @param font The color of the label on the button
	 * @param c The color of the label on the buttons
	 * @param width The width of the entire controller.
	 * @param height The height of the entire controller.
	 * @param labels The labels for each segmented button.
	 */
	public SegmentedControl(GUIContext context, Font font, Color c, int width, int height, String ... labels) {
		super(context);
		
		this.height = height;
		this.width = width;
		
		STATES = labels.length;
		buttons = new Button[STATES];
		state = 0;
		
		for (int i = 0; i < STATES; i++) {
			Label current = new Label(context, font, c, labels[i]);
			buttons[i] = new Button(context, current, width/STATES, height);
			buttons[i].addListener(new SegmentListener(i));
		}
	}
	
	/**
	 * Returns the current state of the segmented controller.  This value
	 * will be the position from the left of the button, with the value of the
	 * far left button being 0
	 * 
	 * @return the current position of the clicked button
	 */
	public int getState() {
		return state;
	}
	
	@Override
	public void setWidth(int width) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setHeight(int height) {
		// TODO Auto-generated method stub

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
		for ( Button b : buttons) {
			b.render(context, g);
		}
	}
	
	@Override
	public void setLocation(int x, int y) {
		if (position == null)
			position = new Vector2f(x,y);
		else
			position.set(x,y);
		
		if (buttons != null ) {
			buttons[0].setPosition(this.getPosition(Positionable.ReferencePoint.TopLeft),
					Positionable.ReferencePoint.TopLeft);
			for (int i = 1; i < STATES; i++) {
				buttons[i].setPosition(buttons[i-1].getPosition(Positionable.ReferencePoint.TopRight),
					Positionable.ReferencePoint.TopLeft);
			}
		}
	}
	
	private class SegmentListener implements ComponentListener {
		private int ordinal;
		
		public SegmentListener(int ordinal) {
			this.ordinal = ordinal;
		}
		
		public void componentActivated(AbstractComponent source) {
			buttons[state].setDisabled(false);
			state = ordinal;
			buttons[state].setDisabled(true);
		}
	}
}
