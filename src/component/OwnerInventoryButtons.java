package component;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import model.Inventory;
import model.Item;
import model.Person;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

import component.Positionable.ReferencePoint;

public class OwnerInventoryButtons {
	private static final int ITEM_BUTTON_WIDTH = 80;
	private static final int ITEM_BUTTON_HEIGHT = 40;
	private static final int ITEM_CONDITION_BAR_HEIGHT = 5;
	private static final int CONDITION_BAR_PADDING = 4;
	private static final int NAME_PADDING = 10;
	private static final int PADDING = 20;
	
	private Person person;
	private Font font;
	
	public OwnerInventoryButtons(Person person) {
	}
	
	public void createPanel(GameContainer container) {
		ArrayList<PriorityQueue<Item>> slots = person.getInventory().getSlots();
		
		int panelHeight = ITEM_BUTTON_HEIGHT + CONDITION_BAR_PADDING + ITEM_CONDITION_BAR_HEIGHT;
		int panelWidth = ((ITEM_BUTTON_WIDTH + PADDING) * Person.MAX_INVENTORY_SIZE) - PADDING;
		
		Panel itemPanel = new Panel(container, panelWidth, panelHeight);
		
		Positionable lastPositionReference = itemPanel;
		for (int i = 0; i < slots.size(); i++) {
			PriorityQueue<Item> items = slots.get(i);
			
			Vector2f position = lastPositionReference.getPosition(ReferencePoint.TopRight);
			int padding = PADDING;
			
			if (i == 0) {
				position = lastPositionReference.getPosition(ReferencePoint.TopLeft);
			}
			
			if (i == 0 || i == items.size()) {
				padding = 0;
			}
			
			lastPositionReference = createItemButton(container, items, position, font, itemPanel, padding);
		}
		
		Label nameLabel = new Label(container, font, Color.white, person.getName());
		
		Panel panel = new Panel(container, panelWidth, panelHeight + NAME_PADDING + (int)nameLabel.getFontHeight());
		panel.add(nameLabel, panel.getPosition(ReferencePoint.TopLeft), ReferencePoint.TopLeft, 0, 0);
		panel.add(itemPanel, nameLabel.getPosition(ReferencePoint.BottomLeft), ReferencePoint.TopLeft, 0, NAME_PADDING);
	}
	
	public Button createItemButton(GameContainer container, PriorityQueue<Item> items, Vector2f position, Font font, Panel panel, int offset) {	
		CountingButton button = new CountingButton(container, ITEM_BUTTON_WIDTH, ITEM_BUTTON_HEIGHT, new Label(container, font, Color.white, items.peek().getName()));
		button.setCountUpOnLeftClick(false);
		button.setMax(items.size());
		button.setCount(items.size());
		
		ConditionBar conditionBar = new ConditionBar(container, ITEM_BUTTON_WIDTH, ITEM_CONDITION_BAR_HEIGHT, items.peek().getStatus());
		
		panel.add(button, position, ReferencePoint.TopLeft, offset, 0);
		panel.add(conditionBar, button.getPosition(ReferencePoint.BottomCenter), ReferencePoint.TopCenter, 0, CONDITION_BAR_PADDING);
		
		return button;
	}

	/**
	 * @return the font
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * @param font the font to set
	 */
	public void setFont(Font font) {
		this.font = font;
	}
}
