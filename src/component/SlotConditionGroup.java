package component;

import model.Condition;
import model.Item.ITEM_TYPE;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

/**
 * A group consisting of a {@code CountingButton} and {@code ConditionBar}.
 * 
 * Both the {@code CountingButton} and the {@code ConditionBar} are linked to a specific item and item slot for an {@code Item}.
 */
public class SlotConditionGroup extends ComponentConditionGroup<CountingButton> {
	public static enum Mode {NORMAL, TRANSFER};
	private Mode currentMode;
	
	private ITEM_TYPE item;
	
	/**
	 * Construct a {@code SlotConditionGroup} with a {@code GUIContext}, width, height, {@code Font}, and the pocket number.
	 * @param container The container for this {@code Component}
	 * @param width Width
	 * @param height Height
	 * @param font Font to use
	 * @param pocketNumber The "pocket number" that the {@code Inventoried} has on it to use
	 */
	public SlotConditionGroup(GUIContext container, int width, int height, Font font, CountingButton button, Condition condition) {
		super(container, width, height, font, button, condition);
		
		button.addListener(new ButtonListener());
		
		setDisable(true);
	}

	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		if (!isVisible()) {
			return;
		}
		
		super.render(container, g);
	}
	
	/**
	 * Get the ITEM_TYPE.
	 * @return ITEM_TYPE
	 */
	public ITEM_TYPE getItem() {
		return item;
	}
	
	/**
	 * Changes the contents of the group.
	 * @param item New ITEM_TYPE
	 * @param name New name
	 * @param amount New quantity
	 * @param condition Condition to use
	 */
	public void changeContents(ITEM_TYPE item, String name, int amount, Condition condition) {
		if (currentMode == Mode.TRANSFER) {
			return;
		}
		
		this.item = item;
		
		component.setText(name);
		component.setCount(amount);
		component.setMax(amount);
		
		conditionBar.setCondition(condition);
	}
	
	/**
	 * Get the current mode.
	 * @return Current mode
	 */
	public Mode getCurrentMode() {
		return currentMode;
	}

	/**
	 * Set the current mode.
	 * @param currentMode Current mode
	 */
	public void setCurrentMode(Mode currentMode) {
		this.currentMode = currentMode;
		
		if (currentMode == Mode.TRANSFER) {
			component.setText("Free");
			component.setHideCount(true);
			component.setDisabled(false);
			
			conditionBar.setVisible(false);
		}
	}
	
	@Override
	public void setDisable(boolean disabled) {
		super.setDisable(disabled);
		
		if (disabled) {
			component.setText("None");
		}
		
		component.setHideCount(disabled);
	}
	
	private class ButtonListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent component) {
			notifyListeners();
		}
	}
}
