package component;

import java.util.PriorityQueue;

import model.Item;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

import component.Positionable.ReferencePoint;

public class SlotConditionGroup extends Component {
	private static final int ITEM_BUTTON_HEIGHT = 40;
	private static final int ITEM_CONDITION_BAR_HEIGHT = 5;
	
	private Panel panel;
	private CountingButton button;
	private ConditionBar conditionBar;
	
	private int slotNumber;
	
	public SlotConditionGroup(GUIContext context, int width, int height, Font font, int slotNumber, PriorityQueue<Item> items) {
		super(context, width, height);
		
		this.slotNumber = slotNumber;
		
		CountingButton button = new CountingButton(container, width, ITEM_BUTTON_HEIGHT, new Label(container, font, Color.white, items.peek().getName()));
		button.setCountUpOnLeftClick(false);
		button.setMax(items.size());
		button.setCount(items.size());
		button.addListener(new ButtonListener());
		add(button, this.getPosition(ReferencePoint.TopLeft), ReferencePoint.TopLeft, 0, 0);
		
		ConditionBar conditionBar = new ConditionBar(container, width, ITEM_CONDITION_BAR_HEIGHT, items.peek().getStatus());		
		
		int padding = height - (ITEM_BUTTON_HEIGHT + ITEM_CONDITION_BAR_HEIGHT);
		
		add(conditionBar, button.getPosition(ReferencePoint.BottomCenter), ReferencePoint.TopCenter, 0, padding);
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
	
	public void changeContents(PriorityQueue<Item> items) {
		button.setText(items.peek().getName());
		button.setCount(items.size());
		
		conditionBar.setCondition(items.peek().getStatus());
	}
	
	private class ButtonListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent component) {
			notifyListeners();
		}
	}
}
