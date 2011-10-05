package component;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.*;

import java.util.*;
/**
 * A component with multiple buttons.  Has two functionalities:many buttons,
 * where only one can be selected at a time, or 0 to some specified number
 * can be selected at a time.  The current button(s) selected stands is returned
 * to the user in the form of an int array, with the numbers corresponding
 * to the index of the String array passed into the constructor.
 * 
 * @author Jeremy
 * @version 10/04/2011
 */
public class SegmentedControl extends Component {
	
	private final int PADDING = 5;
	private final int STATES;
	private final int MAX_SELECTED;
	
	private int rows, cols, rowHeight, colWidth;
	private int height, width;
	private Vector2f position;
	
	
	private ArrayList<Integer> selection;
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
	 * @param rows The number of desired rows the controller will have.
	 * @param cols The number of desired columns the controller will have
	 * @param maxSelected The maximum number of selected buttons at a time
	 * @param labels The labels for each segmented button.
	 */
	public SegmentedControl(GUIContext context, Font font, Color c, int width, int height, int rows, int cols, int maxSelected, String ... labels) {
		super(context);
		
		this.rows = rows;
		this.cols = cols;

		STATES = labels.length;
		MAX_SELECTED = maxSelected;
		buttons = new Button[STATES];
		selection = new ArrayList<Integer>();
		
		for (int i = 0; i < STATES; i++) {
			Label current = new Label(context, font, c, labels[i]);
			buttons[i] = new Button(context, current, colWidth, rowHeight);
			buttons[i].addListener(new SegmentListener(i));
		}
		
		setWidth(width);
		setHeight(height);
		
		if (MAX_SELECTED == 1) {
			buttons[0].setDisabled(true);
			selection.add(0);
		}
	}
	

	/**
	 * Returns the current state of the segmented controller.  This value
	 * will be the position from the left of the button, with the value of the
	 * far left button being 0
	 * 
	 * @return the current position of the clicked button
	 */
	public int[] getState() {
		if (MAX_SELECTED > 1) {
			int[] returnStates = new int[selection.size()];
			for ( int i = 0; i < selection.size(); i++)
				returnStates[i] = selection.get(i);
			return returnStates;
		}
		else {
			int[] returnStates = {selection.get(0)};
			return returnStates;
		}
	}
	
	@Override
	public void setWidth(int width) {
		int horizPaddingAmt = (cols - 1) * PADDING;
		width -= horizPaddingAmt;
		
		this.colWidth = (int) ((double)width/cols);
		this.width = colWidth * cols + horizPaddingAmt;

		for (Button b : buttons) {
			b.setWidth(colWidth);
		}
	}

	@Override
	public void setHeight(int height) {
		int vertPaddingAmt = (rows - 1) * PADDING;
		height -= vertPaddingAmt;
		
		this.rowHeight =  (int) ((double)height/rows);
		this.height = rowHeight * rows + vertPaddingAmt;
		
		for (Button b : buttons) {
			b.setHeight(rowHeight);
		}
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
					Positionable.ReferencePoint.TopLeft, 0, 0);
			for (int i = 1; i < cols; i++) {
				buttons[i].setPosition(buttons[i-1].getPosition(Positionable.ReferencePoint.TopRight),
						Positionable.ReferencePoint.TopLeft, PADDING, 0);
			}
			OuterLoop:
			for (int i = 1; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					if (parsePosition(i,j) >= STATES) 
						break OuterLoop;
					buttons[parsePosition(i,j)].setPosition(buttons[parsePosition(i-1,j)].getPosition(Positionable.ReferencePoint.BottomLeft),
						Positionable.ReferencePoint.TopLeft, 0, PADDING);
				}
			}
		}
	}
	
	/**
	 * @param i the row number
	 * @param j the column number
	 * @return The single array index given a row and column
	 */
	private int parsePosition(int row, int col) {
		return  row*cols + col;
	}
	
	private class SegmentListener implements ComponentListener {
		private int ordinal;
		
		public SegmentListener(int ordinal) {
			this.ordinal = ordinal;
		}
		
		public void componentActivated(AbstractComponent source) {
			if (MAX_SELECTED > 1) {
				if ( selection.contains(new Integer(ordinal)) ) {
					buttons[ordinal].setButtonColor(Color.gray);
					selection.remove(new Integer(ordinal));
				}
				else if (selection.size() < MAX_SELECTED) {
					buttons[ordinal].setButtonColor(Color.darkGray);
					selection.add(new Integer(ordinal));
				}
			}
			else {
				buttons[selection.remove(0)].setDisabled(false);
				selection.add(ordinal);
				buttons[ordinal].setDisabled(true);
			}
		}
	}
}
