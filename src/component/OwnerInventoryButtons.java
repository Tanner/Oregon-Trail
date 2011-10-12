package component;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

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
		ArrayList<PriorityQueue<Item>> slots = inventoried.getInventory().getPopulatedSlots();
		
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
				padding = 0;
			} else if (i == items.size()) {
				padding = 0;
			}
			
			itemSlots.add(i, new SlotConditionGroup(container, ITEM_BUTTON_WIDTH, panelHeight, font, i, items));
			itemPanel.add(itemSlots.get(i), position, ReferencePoint.TopLeft, padding, 0);
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
