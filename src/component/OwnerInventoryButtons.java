package component;

import java.util.ArrayList;
import model.Condition;
import model.Inventoried;
import model.Item;
import model.Item.ITEM_TYPE;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;

import scene.PartyInventoryScene;
import scene.PartyInventoryScene.Mode;

import component.Positionable.ReferencePoint;

/**
 * This class is a group of a {@code Button} and a {@code ConditionBar}.
 * 
 * The group is for a specific {@code Inventoried}.
 */
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
	private ConditionBar weightBar;
	private Panel panel;
	
	private ItemListener listener;
	
	/**
	 * Constructs a new {@code OwnerInventoryButtons} with an {@code Inventoried}.
	 * @param inventoried Inventoried object for this OwnerInventoryButtons
	 */
	public OwnerInventoryButtons(Inventoried inventoried) {
		this.inventoried = inventoried;
		
		itemSlots = new ArrayList<SlotConditionGroup>();
	}
	
	/**
	 * Creates the panel that holds both the {@code Button} and the {@code ConditionBar}.
	 * @param container Container
	 */
	public void makePanel(GameContainer container) {
		ArrayList<Item.ITEM_TYPE> slots = inventoried.getInventory().getPopulatedSlots();
		
		int panelHeight = ITEM_BUTTON_HEIGHT + CONDITION_BAR_PADDING + ITEM_CONDITION_BAR_HEIGHT;
		int panelWidth = ((ITEM_BUTTON_WIDTH + PADDING) * inventoried.getMaxSize()) - PADDING;
		
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
		
		panel = new Panel(container, panelWidth, panelHeight + NAME_PADDING + (int)nameLabel.getFontHeight());
		panel.add(nameLabel, panel.getPosition(ReferencePoint.TopLeft), ReferencePoint.TopLeft, 0, 0);
		panel.add(itemPanel, nameLabel.getPosition(ReferencePoint.BottomLeft), ReferencePoint.TopLeft, 0, NAME_PADDING);
		
		weightBar = new ConditionBar(container, ITEM_BUTTON_WIDTH * 2 + PADDING, nameLabel.getHeight(), getWeightCondition(), font);
		weightBar.setDisableText(false);
		panel.add(weightBar, itemPanel.getPosition(ReferencePoint.TopRight), ReferencePoint.BottomRight, 0, -NAME_PADDING);
		
		updateGraphics();
	}
	
	/**
	 * Update the contents of the buttons and the weightbar to be current
	 */
	public void updateGraphics() {
		ArrayList<Item.ITEM_TYPE> slots = inventoried.getInventory().getPopulatedSlots();

		int maxInventorySize = inventoried.getInventory().getMaxSize();

		for (int i = 0; i < maxInventorySize; i++) {
			if (i < slots.size()) {
				String name = slots.get(i).getName();
				int amount = inventoried.getInventory().getNumberOf(slots.get(i));
				Condition condition = inventoried.getInventory().getConditionOf(slots.get(i));
				
				itemSlots.get(i).setDisable(false);
				itemSlots.get(i).changeContents(slots.get(i), name, amount, condition);
			} else {
				if (PartyInventoryScene.getCurrentMode() == Mode.TRANSFER && i == slots.size()) {
					itemSlots.get(i).setDisable(false);
					itemSlots.get(i).setCurrentMode(SlotConditionGroup.Mode.TRANSFER);
				} else {
					itemSlots.get(i).setDisable(true);
				}
			}
		}
		
		weightBar.setCondition(getWeightCondition());
	}
	
	public boolean addItemToInventory(ArrayList<Item> items) {
		boolean success = false;
		
		success = inventoried.addItemToInventory(items);
		
		if (success) {
			updateGraphics();
		}
		
		return success;
	}
	
	public ArrayList<Item> removeItemFromInventory(ITEM_TYPE item, int quantity) {
		return inventoried.removeItemFromInventory(item, quantity);
	}
	
	/**
	 * Get the panel of this object.
	 * @return The Panel
	 */
	public Panel getPanel() {
		return panel;
	}
	
	/**
	 * Get the button width
	 * @return Button width
	 */
	public static int getButtonWidth() {
		return ITEM_BUTTON_WIDTH;
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
	
	/**
	 * Get the weight condition for the weight condition bar.
	 * @return Condition that represents the weight
	 */
	public Condition getWeightCondition() {
		int weightLeft = (int)(inventoried.getMaxWeight() - inventoried.getWeight());
		return new Condition(0, (int)inventoried.getMaxWeight(), weightLeft);
	}
	
	/**
	 * Set the listener.
	 * @param listener New listener
	 */
	public void setListener(ItemListener listener) {
		this.listener = listener;
	}

	private class ButtonListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent component) {
			SlotConditionGroup slotConditionGroup = (SlotConditionGroup)component;
			ITEM_TYPE item = slotConditionGroup.getItem();
			
			if (item != null) {
				listener.itemButtonPressed(OwnerInventoryButtons.this, item);
			} else {
				listener.itemButtonPressed(OwnerInventoryButtons.this);
			}
			
			updateGraphics();
		}
	}
}
