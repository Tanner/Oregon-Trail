package component;

import org.newdawn.slick.*;
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
 */
public class SegmentedControl extends Component {
	
	private Font font = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);
	private Color color = Color.white;
	
	private final int STATES;
	private final int maxSelected;
	
	private int rowHeight, colWidth;
	
	private boolean[] selection;
	private int singleSelection;
	private boolean[] permanent;
	private ToggleButton[] buttons;
	
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
		
		STATES = labels.length;
		this.maxSelected = maxSelected;
		buttons = new ToggleButton[STATES];
		selection = new boolean[STATES];
		permanent = new boolean[STATES];
		singleSelection = -1;
		
		rowHeight = (getHeight() - (rows + 1) * margin) / rows;
		colWidth = (getWidth() - (cols + 1) * margin) / cols;
		
		for (int i = 0; i < STATES; i++) {
			Label current = new Label(context, font, color, labels[i]);
			
			buttons[i] = new ToggleButton(context, colWidth, rowHeight, current);
			buttons[i].setDisableAutoToggle(true);
			buttons[i].addListener(new SegmentListener(i));
			
			// if there's no spacing, then set first button's left border to 0
			if (margin <= 0) {
				if (i > 0) {
					buttons[i].setLeftBorderWidth(0);
				}
				if (i > STATES / rows - 1) {
					buttons[i].setTopBorderWidth(0);
				}
			}
		}
		
		//Center the control in the middle of the width/height if there is left over padding
		int xOffset = (width - cols * colWidth - margin * (cols - 1) ) / 2;
		int yOffset = (height - rows * rowHeight - margin * (rows - 1) ) / 2;
		
		this.addAsGrid(buttons, getPosition(Positionable.ReferencePoint.TopLeft), rows, cols, xOffset, yOffset, margin, margin);
			
		clear();		
	}
	
	@Override
	public void render(GUIContext context, Graphics g) throws SlickException {
		if (!isVisible()) {
			return;
		}
		
		super.render(context, g);
	}
	
	/**
	 * Update all buttons to their current state.
	 */
	public void updateButtons() {
		for (int i = 0; i < STATES; i++) {
			if (permanent[i] || selection[i] || i == singleSelection) {
				buttons[i].setActive(true);
			} else {
				buttons[i].setActive(false);
			}
		}
	}
	
	/**
	 * Set which buttons are selected.
	 * @param selection An array of selections to set, with the values of the array corresponding to the index of the button
	 */
	public void setSelection(int[] selection) {
		if ( selection.length == 1) {
			singleSelection = selection[0];
		} else {
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
	 * Set buttons to be permanently toggled.
	 * @param permanent An array of buttons to be permanently toggled, with the values of the array corresponding to the index of the button
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
	 * Clear the selections of the buttons.
	 */
	public void clear() {
		Arrays.fill(this.selection, false);
		Arrays.fill(this.permanent, false);
		if (maxSelected == 1) {
			singleSelection = 0;
			buttons[singleSelection].setActive(true);
		} else {
			singleSelection = -1;
		}
	}
	
	/**
	 * Set the font used by each button.
	 * @param f The new font for each button
	 */
	public void setFont(Font f) {
		font = f;
		for (Button b : buttons) {
			b.setFont(font);
		}
	}
	
	/**
	 * Set the font color used by each button.
	 * @param c The new color from each button
	 */
	public void setColor(Color c) {
		color = c;
		for (Button b : buttons) {
			b.setLabelColor(color);
		}
	}
	
	/**
	 * Return the number of buttons selected.
	 * @return The number of buttons selected
	 */
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
	 * Returns the current state of the segmented controller as an array,
	 * with each index of the array corresponding the index of the button
	 * that was selected.
	 * @return The current position of the clicked button
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
	
	/**
	 * Listener for the buttons of {@code SegmentedControl}.
	 */
	private class SegmentListener implements ComponentListener {
		private int ordinal;
		
		/**
		 * Constructs a {@code SegmentedListener} with an ordinal value
		 * corresponding to the index of the button in {@code SegmentedControl}.
		 * @param ordinal The ordinal value of the button in the {@code SegmentedControl}
		 */
		public SegmentListener(int ordinal) {
			this.ordinal = ordinal;
		}
		
		@Override
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
