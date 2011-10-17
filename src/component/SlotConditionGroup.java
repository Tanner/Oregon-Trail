package component;

import model.Condition;
import model.Item.ITEM_TYPE;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

import component.Label.Alignment;

/**
 * A group consisting of a {@code Button} and {@code ConditionBar}.
 * 
 * Both the {@code Button} and the {@code ConditionBar} are linked to a specific item and item slot for an {@code Item}.
 * @author tanner
 *
 */
public class SlotConditionGroup extends Component {
	private static final int ITEM_BUTTON_HEIGHT = 40;
	private static final int ITEM_CONDITION_BAR_HEIGHT = 5;
	
	private CountingButton button;
	private ConditionBar conditionBar;
	
	private int pocketNumber;
	private ITEM_TYPE item;
	
	/**
	 * Construct a {@code SlotConditionGroup} with a {@code GUIContext}, width, height, {@code Font}, and the pocket number.
	 * @param container The container for this {@code Component}
	 * @param width Width
	 * @param height Height
	 * @param font Font to use
	 * @param pocketNumber The "pocket number" that the {@code Inventoried} has on it to use
	 */
	public SlotConditionGroup(GUIContext container, int width, int height, Font font, int pocketNumber) {
		super(container, width, height);
		
		this.pocketNumber = pocketNumber;
		
		Label label = new Label(container, width, font, Color.white, "");
		label.setAlignment(Alignment.Center);
		
		button = new CountingButton(container, width, ITEM_BUTTON_HEIGHT, label);
		conditionBar = new ConditionBar(container, width, ITEM_CONDITION_BAR_HEIGHT, null);
		
		button.setCountUpOnLeftClick(false);
		button.setDisableAutoCount(true);
		button.addListener(new ButtonListener());
		
		int padding = height - (ITEM_BUTTON_HEIGHT + ITEM_CONDITION_BAR_HEIGHT);
		
		add(button, this.getPosition(ReferencePoint.TopLeft), ReferencePoint.TopLeft, 0, 0);
		add(conditionBar, button.getPosition(ReferencePoint.BottomCenter), ReferencePoint.TopCenter, 0, padding);
		
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
	 * Get the pocket number.
	 * @return Pocket number
	 */
	public int getPocketNumber() {
		return pocketNumber;
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
		this.item = item;
		
		button.setText(name);
		button.setCount(amount);
		button.setMax(amount);
		
		conditionBar.setCondition(condition);
	}
	
	public void setDisable(boolean disabled) {
		if (disabled) {
			button.setText("None");
		}
		
		button.setHideCount(disabled);
		button.setDisabled(disabled);
		
		conditionBar.setVisible(!disabled);
	}
	
	private class ButtonListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent component) {
			notifyListeners();
		}
	}
}
