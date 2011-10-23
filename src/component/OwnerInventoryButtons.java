package component;

import java.util.ArrayList;
import java.util.List;

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

import component.Label.Alignment;
import component.Positionable.ReferencePoint;
import core.FontManager;
import core.GameDirector;

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
		List<Item.ITEM_TYPE> slots = inventoried.getInventory().getPopulatedSlots();
		
		int panelHeight = ITEM_BUTTON_HEIGHT + CONDITION_BAR_PADDING + ITEM_CONDITION_BAR_HEIGHT;
		int panelWidth = ((ITEM_BUTTON_WIDTH + PADDING) * inventoried.getMaxSize()) - PADDING;
		
		int maxInventorySize = inventoried.getInventory().getMaxSize();
		
		Panel itemPanel = new Panel(container, panelWidth, panelHeight);
		
		Positionable lastPositionReference = itemPanel;
		for (int i = 0; i < maxInventorySize; i++) {
			Vector2f position = lastPositionReference.getPosition(ReferencePoint.TOPRIGHT);
			int padding = PADDING;
			
			if (i == 0) {
				position = lastPositionReference.getPosition(ReferencePoint.TOPLEFT);
				padding = 0;
			} else if (i == maxInventorySize) {
				padding = 0;
			}
			
			Font fieldFont = GameDirector.sharedSceneListener().getFontManager().getFont(FontManager.FontID.FIELD);
			
			Label label = new Label(container, ITEM_BUTTON_WIDTH, ITEM_BUTTON_HEIGHT, fieldFont, Color.white, "");
			label.setAlignment(Alignment.CENTER);
			
			Counter button = new Counter(container, ITEM_BUTTON_WIDTH, ITEM_BUTTON_HEIGHT, label);
			button.setCountUpOnLeftClick(false);
			button.setDisableAutoCount(true);
			
			SlotConditionGroup slotConditionGroup = new SlotConditionGroup(container, ITEM_BUTTON_WIDTH, panelHeight, font, button, new Condition(0, 0, 0));
			slotConditionGroup.addListener(new ButtonListener());
			
			if (i < slots.size()) {
				String name = slots.get(i).getName();
				int amount = inventoried.getInventory().getNumberOf(slots.get(i));
				Condition condition = inventoried.getInventory().getConditionOf(slots.get(i));
				
				slotConditionGroup.changeContents(slots.get(i), name, amount, condition);
			}
						
			itemSlots.add(i, slotConditionGroup);
			itemPanel.add(itemSlots.get(i), position, ReferencePoint.TOPLEFT, padding, 0);
			
			lastPositionReference = itemSlots.get(i);
		}
		
		Label nameLabel = new Label(container, font, Color.white, inventoried.getName());
		
		panel = new Panel(container, panelWidth, panelHeight + NAME_PADDING + (int)nameLabel.getFontHeight());
		panel.add(nameLabel, panel.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT, 0, 0);
		panel.add(itemPanel, nameLabel.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.TOPLEFT, 0, NAME_PADDING);
		
		weightBar = new ConditionBar(container, ITEM_BUTTON_WIDTH * 2 + PADDING, nameLabel.getHeight(), getWeightCondition(), font);
		weightBar.setDisableText(false);
		panel.add(weightBar, itemPanel.getPosition(ReferencePoint.TOPRIGHT), ReferencePoint.BOTTOMRIGHT, 0, -NAME_PADDING);
		
		updateGraphics();
	}
	
	/**
	 * Update the contents of the buttons and the weightbar to be current
	 */
	public void updateGraphics() {
		List<Item.ITEM_TYPE> slots = inventoried.getInventory().getPopulatedSlots();

		int maxInventorySize = inventoried.getInventory().getMaxSize();

		for (int i = 0; i < maxInventorySize; i++) {
			if (i < slots.size()) {
				String name = slots.get(i).getName();
				int amount = inventoried.getInventory().getNumberOf(slots.get(i));
				Condition condition = inventoried.getInventory().getConditionOf(slots.get(i));
				
				itemSlots.get(i).setDisable(false);
				itemSlots.get(i).changeContents(slots.get(i), name, amount, condition);
				
				itemSlots.get(i).setCurrentMode(SlotConditionGroup.Mode.NORMAL);
			} else {
				if (PartyInventoryScene.getCurrentMode() == Mode.TRANSFER && i == slots.size()) {
					itemSlots.get(i).setDisable(false);
					itemSlots.get(i).setCurrentMode(SlotConditionGroup.Mode.TRANSFER);
				} else {
					itemSlots.get(i).setDisable(true);
					
					itemSlots.get(i).setCurrentMode(SlotConditionGroup.Mode.NORMAL);
				}
			}
		}
		
		weightBar.setCondition(getWeightCondition());
	}
	
	/**
	 * Add an item to the {@code Inventoried} inventory.
	 * @param items List of Items to add to the inventory
	 * @return Whether or not the addition was successful
	 */
	public void addItemToInventory(List<Item> items) {
		inventoried.addItemsToInventory(items);
		updateGraphics();
	}
	
	/**
	 * Remove an item from the {@code Inventoried} inventory.
	 * @param item ITEM_TYPE to remove
	 * @param quantity Number of the item to remove
	 * @return Whether or not the removal was successful
	 */
	public List<Item> removeItemFromInventory(ITEM_TYPE item, int quantity) {
		return inventoried.removeItemFromInventory(item, quantity);
	}
	
	/**
	 * Check to see if an {@code ITEM_TYPE} can be added.
	 * @param item ITEM_TYPE to check
	 * @param quantity Number of the item to check
	 * @return Whether or not the check return true or false
	 */
	public boolean canAddItems(ITEM_TYPE item, int quantity) {
		return inventoried.canGetItem(item, quantity);
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
			
			if (PartyInventoryScene.getCurrentMode() == PartyInventoryScene.Mode.NORMAL) {
				listener.itemButtonPressed(OwnerInventoryButtons.this, item);
			} else if (slotConditionGroup.getCurrentMode() == SlotConditionGroup.Mode.TRANSFER) {
				listener.itemButtonPressed(OwnerInventoryButtons.this);
			}
			
			updateGraphics();
		}
	}
}
