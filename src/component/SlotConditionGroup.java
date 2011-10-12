package component;

import java.util.PriorityQueue;

import model.Condition;
import model.Item;

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
	
	private int slotNumber;
	
	public SlotConditionGroup(GUIContext container, int width, int height, Font font, int slotNumber) {
		super(container, width, height);
		
		this.slotNumber = slotNumber;
		
		Label label = new Label(container, width, font, Color.white, "");
		label.setAlignment(Alignment.Center);
		
		button = new CountingButton(container, width, ITEM_BUTTON_HEIGHT, label);
		conditionBar = new ConditionBar(container, width, ITEM_CONDITION_BAR_HEIGHT, null);
		
		button.setCountUpOnLeftClick(false);
		button.addListener(new ButtonListener());
		
		int padding = height - (ITEM_BUTTON_HEIGHT + ITEM_CONDITION_BAR_HEIGHT);
		
		add(button, this.getPosition(ReferencePoint.TopLeft), ReferencePoint.TopLeft, 0, 0);
		add(conditionBar, button.getPosition(ReferencePoint.BottomCenter), ReferencePoint.TopCenter, 0, padding);
		
		changeContents(null, 0, null);
	}

	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		if (!isVisible()) {
			return;
		}
		
		super.render(container, g);
	}
	
	public int getSlotNumber() {
		return slotNumber;
	}
	
	public void changeContents(String name, int amount, Condition condition) {
		if (name != null) {
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
