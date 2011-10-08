package component;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.*;

import core.FontManager;
import core.GameDirector;
import core.Logger;

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
/**
 * @author Computer
 *
 */
public class SegmentedControl extends Component {
	
	private int margin;
	private Font font = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);
	private Color color = Color.white;
	
	private final int STATES;
	private final int maxSelected;
	
	private int rows, cols, rowHeight, colWidth;
	
	private String[] labels;
	
	private boolean[] selection;
	private int singleSelection;
	private boolean[] permanent;
	private Button[] buttons;
	
	/**
	 * Create a new SegmentedControl with a given font, color, width, height.
	 * The variable list of strings dictate the number of buttons created.
	 * All buttons are created with the same width.
	 * Only one button is allowed to be selected at a time.
	 *  
	 * @param context The game's GUIContext object
	 * @param width The width of the entire controller.
	 * @param height The height of the entire controller.
	 * @param rows The number of desired rows the controller will have.
	 * @param cols The number of desired columns the controller will have
	 * @param margin The padding between each button
	 * @param maxSelected The maximum number of selected buttons at a time
	 * @param labels The labels for each segmented button.
	 */
	public SegmentedControl(GUIContext context, int width, int height, int rows, int cols, int margin, int maxSelected, String ... labels) {
		super(context, width, height);
		
		this.rows = rows;
		this.cols = cols;
		this.margin = margin;
		this.labels = labels;
		STATES = labels.length;
		this.maxSelected = maxSelected;
		buttons = new Button[STATES];
		selection = new boolean[STATES];
		permanent = new boolean[STATES];
		singleSelection = -1;
		
		generateButtons(context);
			
		clear();		
	}
	
	/**
	 * Create each button for the SegmentedControl, and add listeners
	 * to each button.
	 * 
	 * @param context The graphics context for the game
	 */
	public void generateButtons(GUIContext context) {
		for (int i = 0; i < STATES; i++) {
			Label current = new Label(context, font, color, labels[i]);
			buttons[i] = new Button(context, current, colWidth, rowHeight);
			buttons[i].addListener(new SegmentListener(i));
		}
		
		boolean roundedCorners = (margin == 0) ? true : false;
		
		buttons[0].setPosition(this.getPosition(Positionable.ReferencePoint.TopLeft),
				Positionable.ReferencePoint.TopLeft, 0, 0);
		buttons[0].setTopLeftRoundedCorner(roundedCorners);
		if (rows == 1) {
			buttons[0].setBottomLeftRoundedCorner(roundedCorners);
		}
		for (int i = 1; i < cols; i++) {
			buttons[i].setPosition(buttons[i-1].getPosition(Positionable.ReferencePoint.TopRight),
					Positionable.ReferencePoint.TopLeft, margin, 0);
			if (i == cols - 1) {
				buttons[i].setTopRightRoundedCorner(roundedCorners);
				if (rows == 1) {
					buttons[i].setBottomRightRoundedCorner(roundedCorners);
				}
			}
		}
		OuterLoop:
		for (int i = 1; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (parsePosition(i,j) >= STATES) 
					break OuterLoop;
				buttons[parsePosition(i,j)].setPosition(buttons[parsePosition(i-1,j)].getPosition(Positionable.ReferencePoint.BottomLeft),
					Positionable.ReferencePoint.TopLeft, 0, margin);
				if (i == rows-1 && j == cols-1) {
					buttons[parsePosition(i, j)].setBottomRightRoundedCorner(roundedCorners);
				}
				if (i == rows-1 && j == 0) {
					buttons[parsePosition(i, j)].setBottomLeftRoundedCorner(roundedCorners);
				}
			}
		}
	}
	
	/**
	 * Update all buttons to their current state/color
	 */
	public void updateButtons() {
		for (int i = 0; i < STATES; i++) {
			if (permanent[i] || selection[i] || i == singleSelection)
				buttons[i].setButtonColor(Color.darkGray);
			else
				buttons[i].setButtonColor(Color.gray);
		}
	}
	
	
	/**
	 * This method allows you to set which buttons are selected in the current
	 * SegmentedControl.  The correct use of this is to get the selection array
	 * from getSelection(), and pass it back in to this method.
	 * 
	 * @param selection An array of selections to set on the SegmentedControl
	 */
	public void setSelection(int[] selection) {
		if ( selection.length == 1) {
			singleSelection = selection[0];
		}
		else {
			for (int i : selection) {
				if (this.permanent[i] != true) {
					this.selection[i] = true;
				} else {
					this.selection[i] = false;
				}
			}
		}
		updateButtons();
	}
	
	/**
	 * This method will allow multiple buttons to be turned on permanently.  They
	 * can not be unselected, and they count towards the maximum possible selected
	 * button count.  Only works if the controller is set to allow more than
	 * one button selection.
	 * 
	 * @param permanent An array of indices to set as permanently selected
	 */
	public void setPermanent(int[] permanent) {
		if (maxSelected > 1) {
			Arrays.fill(this.permanent, false);
			for (int i : permanent) {
				this.permanent[i] = true;
			}
			setSelection(permanent);
		}
		updateButtons();
	}
	
	/**
	 * Clear the state of the SegmentedController.
	 * Sets button selection back to default if only 1 button is allowed to be
	 * changed.  Otherwise, clear all selections.
	 */
	public void clear() {
		Arrays.fill(this.selection, false);
		Arrays.fill(this.permanent, false);
		if ( maxSelected == 1) {
			singleSelection = 0;
			buttons[singleSelection].setButtonColor(Color.darkGray);
		}
		else
			singleSelection = -1;
	}
	
	/**
	 * Change the font used by each button on the SegmentedController.
	 * 
	 * @param f The new font to set.
	 */
	public void setFont(Font f) {
		font = f;
		for (Button b : buttons)
			b.setFont(font);
	}
	
	/**
	 * Change the font color used by each button on the SegmentedController.
	 * 
	 * @param c The new color to set.
	 */
	public void setColor(Color c) {
		color = c;
		for (Button b : buttons)
			b.setLabelColor(color);
	}
	
	private int getNumSelected() {
		int count = 0;
		for (boolean b : selection) {
			if (b) {
				count++;
			}
		}
		for (boolean b : permanent) {
			if (b) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Returns the current state of the segmented controller.  This value
	 * will be the position from the left of the button, with the value of the
	 * far left button being 0
	 * 
	 * @return the current position of the clicked button
	 */
	public int[] getSelection() {
		if (maxSelected > 1) {
			int[] returnStates = new int[getNumSelected()];
			int ptr = 0;
			for (int i = 0; i < returnStates.length; i++) {
				while (!selection[ptr] && !permanent[ptr]) {
					ptr++;
				}
				returnStates[i] = ptr;
				ptr++;
			}
			return returnStates;
		}
		else {
			int[] returnStates = {singleSelection};
			return returnStates;
		}
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
	
	public void setAcceptingInput(boolean acceptingInput) {
		super.setAcceptingInput(acceptingInput);
		
		for (Button b : buttons) {
			b.setAcceptingInput(acceptingInput);
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
			Logger.log("Selected before button press: " + Arrays.toString(getSelection()), Logger.Level.DEBUG);
			if (maxSelected > 1 && !permanent[ordinal]) {
				if (selection[ordinal])  {
					Logger.log("Segmented control deselecting " + ordinal, Logger.Level.DEBUG);
					selection[ordinal] = false;
				}
				else if (getNumSelected() < maxSelected) {
					Logger.log("Segmented control selecting " + ordinal, Logger.Level.DEBUG);
					selection[ordinal] = true;
				}
			}
			else {
				singleSelection = ordinal;
			}
			updateButtons();
			Logger.log("Selected after button press: " + Arrays.toString(getSelection()), Logger.Level.DEBUG);
		}
	}
}
