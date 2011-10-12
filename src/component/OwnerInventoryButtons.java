package component;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import model.Condition;
import model.Inventoried;
import model.Inventory;
import model.Item;
import model.Person;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;

import component.Positionable.ReferencePoint;

public class OwnerInventoryButtons {
	private static final int ITEM_BUTTON_WIDTH = 80;
	private static final int ITEM_BUTTON_HEIGHT = 40;
	private static final int ITEM_CONDITION_BAR_HEIGHT = 5;
	private static final int CONDITION_BAR_PADDING = 4;
	private static final int NAME_PADDING = 10;
	private static final int PADDING = 20;
	
	private Inventoried inventoried;
	private ArrayList<SlotConditionGroup> itemSlots;
	private Font font;
	
	public OwnerInventoryButtons(Inventoried inventoried) {
		this.inventoried = inventoried;
		
		itemSlots = new ArrayList<SlotConditionGroup>();
	}
	
	public Panel getPanel(GameContainer container) {
		ArrayList<Item.ITEM_TYPE> slots = inventoried.getInventory().getPopulatedSlots();
		
		int panelHeight = ITEM_BUTTON_HEIGHT + CONDITION_BAR_PADDING + ITEM_CONDITION_BAR_HEIGHT;
		int panelWidth = ((ITEM_BUTTON_WIDTH + PADDING) * Person.MAX_INVENTORY_SIZE) - PADDING;
		
		int maxInventorySize = inventoried.getInventory().getMaxSize();
		
		Panel itemPanel = new Panel(container, panelWidth, panelHeight);
		
		Positionable lastPositionReference = itemPanel;
		for (int i = 0; i < maxInventorySize; i++) {
			Vector2f position = lastPositionReference.getPosition(ReferencePoint.TopRight);
			int padding = PADDING;
			
			if (i == 0) {
				position = lastPositionReference.getPosition(ReferencePoint.TopLeft);
				padding = 0;
			} else if (i == maxInventorySize) {
				padding = 0;
			}
			
			SlotConditionGroup slotConditionGroup = new SlotConditionGroup(container, ITEM_BUTTON_WIDTH, panelHeight, font, i);
			
			if (i < slots.size()) {
				String name = slots.get(i).getName();
				int amount = inventoried.getInventory().getNumberOf(slots.get(i));
				Condition condition = inventoried.getInventory().getConditionOf(slots.get(i));
				
				slotConditionGroup.changeContents(slots.get(i), name, amount, condition);
				slotConditionGroup.addListener(new ButtonListener());
			}
			
			itemSlots.add(i, slotConditionGroup);
			itemPanel.add(itemSlots.get(i), position, ReferencePoint.TopLeft, padding, 0);
			
			lastPositionReference = itemSlots.get(i);
		}
		
		Label nameLabel = new Label(container, font, Color.white, inventoried.getName());
		
		Panel panel = new Panel(container, panelWidth, panelHeight + NAME_PADDING + (int)nameLabel.getFontHeight());
		panel.add(nameLabel, panel.getPosition(ReferencePoint.TopLeft), ReferencePoint.TopLeft, 0, 0);
		panel.add(itemPanel, nameLabel.getPosition(ReferencePoint.BottomLeft), ReferencePoint.TopLeft, 0, NAME_PADDING);
		
		return panel;
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