package component;

import java.util.PriorityQueue;

import model.Condition;
import model.Item;
import model.Item.ITEM_TYPE;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

import component.Label.Alignment;
import component.Label.VerticalAlignment;

public class SlotConditionGroup extends Component {
	private static final int ITEM_BUTTON_HEIGHT = 40;
	private static final int ITEM_CONDITION_BAR_HEIGHT = 5;
	
	private CountingButton button;
	private ConditionBar conditionBar;
	
	private int pocketNumber;
	private ITEM_TYPE item;
	
	public SlotConditionGroup(GUIContext container, int width, int height, Font font, int pocketNumber) {
		super(container, width, height);
		
		this.pocketNumber = pocketNumber;
		
		Label label = new Label(container, width, font, Color.white, "");
		label.setAlignment(Alignment.Center);
		
		button = new CountingButton(container, width, ITEM_BUTTON_HEIGHT, label);
		conditionBar = new ConditionBar(container, width, ITEM_CONDITION_BAR_HEIGHT, null);
		
		button.setCountUpOnLeftClick(false);
		button.addListener(new ButtonListener());
		
		int padding = height - (ITEM_BUTTON_HEIGHT + ITEM_CONDITION_BAR_HEIGHT);
		
		add(button, this.getPosition(ReferencePoint.TopLeft), ReferencePoint.TopLeft, 0, 0);
		add(conditionBar, button.getPosition(ReferencePoint.BottomCenter), ReferencePoint.TopCenter, 0, padding);
		
		changeContents(ITEM_TYPE.APPLE, null, 0, null);
	}

	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		if (!isVisible()) {
			return;
		}
		
		super.render(container, g);
	}
	
	public int getPocketNumber() {
		return pocketNumber;
	}
	
	public ITEM_TYPE getItem() {
		return item;
	}
	
	public void changeContents(ITEM_TYPE item, String name, int amount, Condition condition) {
		this.item = item;
		
		if (amount != 0 && name != null) {
			button.setText(name);
			button.setDisabled(false);
			button.setCount(amount);
			button.setMax(amount);

			button.setHideCount(false);
			
			conditionBar.setCondition(condition);
		} else {
			button.setText("None");
			button.setDisabled(true);
			button.setHideCount(true);
			
			conditionBar.setCondition(null);
		}
	}
	
	private class ButtonListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent component) {
			notifyListeners();
		}
	}
}
