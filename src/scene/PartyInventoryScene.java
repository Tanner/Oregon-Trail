package scene;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import model.Inventory;
import model.Item;
import model.Item.ITEM_TYPE;
import model.Party;
import model.Person;
import model.item.Vehicle;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import component.Button;
import component.Counter;
import component.ItemListener;
import component.Label;
import component.Label.Alignment;
import component.OwnerInventoryButtons;
import component.Panel;
import component.Positionable;
import component.Positionable.ReferencePoint;
import component.modal.MessageModal;
import component.modal.Modal;
import component.sprite.Sprite;

import core.ConstantStore;
import core.FontStore;
import core.GameDirector;
import core.Logger;

/**
 * {@code PartyInventoryScene} can manipulate the party's inventory and allow the user to
 * sell, transfer, or drop {@code Item}.
 */
public class PartyInventoryScene extends Scene {
	public static final SceneID ID = SceneID.PARTYINVENTORY;
	
	public static enum EXTRA_BUTTON_FUNC {SELL, DROP};
	public static enum Mode {NORMAL, TRANSFER};
	
	private static final int PADDING = 20;
	
	private static final int NUM_COLS = 2;
	private static final int BUTTON_HEIGHT = 30;
	
	private static final int BIN_BUTTON_HEIGHT = 120;
	
	private Party party;
	private Inventory[] binInventory;
	private Inventory storeInventory;
	
	private OwnerInventoryButtons playerInventoryButtons[];
	private OwnerInventoryButtons vehicleInventoryButtons;
	
	private Button closeButton, transferButton, functionButton;
	private Counter binButton;
	
	private EXTRA_BUTTON_FUNC extraButtonFunctionality;
	private static Mode currentMode;
	
	/**
	 * Constructs a {@code PartyInventoryScene} with just a {@code Party}.
	 * Allows you to drop {@code Item} as extra functionality.
	 * @param party Party to use for the scene
	 */
	public PartyInventoryScene(Party party) {
		this.party = party;
		
		this.extraButtonFunctionality = EXTRA_BUTTON_FUNC.DROP;
		
		currentMode = Mode.NORMAL;
	}
	
	/**
	 * Constructs a {@code PartyInventoryScene} with just a {@code Party} and an {@code Inventory}.
	 * Allows you to sell {@code Item} to the {@code Inventory} supplied as extra functionality.
	 * @param party Party to use for the scene
	 * @param storeInventory Inventory to sell items back to
	 */
	public PartyInventoryScene(Party party, Inventory storeInventory) {
		this.party = party;
		
		this.storeInventory = storeInventory;
		extraButtonFunctionality = EXTRA_BUTTON_FUNC.SELL;
		
		currentMode = Mode.NORMAL;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);

		Font fieldFont = FontStore.get(FontStore.FontID.FIELD);
		
		List<Person> members = party.getPartyMembers();
		Vehicle vehicle = party.getVehicle();
		
		// Create a grid of all the party members and their inventories
		int maxInventorySize = Person.MAX_INVENTORY_SIZE;
				
		Panel personPanels[] = new Panel[party.getPartyMembers().size()];
		playerInventoryButtons = new OwnerInventoryButtons[personPanels.length];
		
		for (int i = 0; i < personPanels.length; i++) {
			Person person = members.get(i);
			
			playerInventoryButtons[i] = new OwnerInventoryButtons(person);
			playerInventoryButtons[i].setFont(fieldFont);
			playerInventoryButtons[i].setListener(new OwnerInventoryButtonsListener());
			
			playerInventoryButtons[i].makePanel(container);
			
			personPanels[i] = playerInventoryButtons[i].getPanel();
		}
		
		int panelWidth = ((OwnerInventoryButtons.getButtonWidth() + PADDING) * maxInventorySize) - PADDING;
		int peopleSpacing = ((container.getWidth() - (2 * PADDING)) - (panelWidth * NUM_COLS)) / (NUM_COLS - 1) - 4;
		
		mainLayer.addAsGrid(personPanels, mainLayer.getPosition(ReferencePoint.TOPLEFT), (members.size() / 2), NUM_COLS, PADDING, PADDING, peopleSpacing, PADDING);
		
		// Create Vehicle inventories (if one exists)
		if (vehicle != null) {
			int lastOddIndex = personPanels.length - 1;
			if (lastOddIndex > 0 && lastOddIndex % 2 != 0) {
				lastOddIndex--;
			}
			Positionable locationReference = personPanels[lastOddIndex];
	
			vehicleInventoryButtons = new OwnerInventoryButtons(vehicle);
			vehicleInventoryButtons.setFont(fieldFont);
			vehicleInventoryButtons.setListener(new OwnerInventoryButtonsListener());
			
			vehicleInventoryButtons.makePanel(container);
			
			mainLayer.add(vehicleInventoryButtons.getPanel(), locationReference.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.TOPLEFT, 0, PADDING);
		}
		
		int buttonWidth = (container.getWidth() - PADDING * 4) / 4;
		// Close button
		closeButton = new Button(container, buttonWidth, BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, ConstantStore.get("GENERAL", "CLOSE")));
		closeButton.addListener(new ButtonListener());
		mainLayer.add(closeButton, mainLayer.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT, PADDING, -PADDING);
		
		// Transfer button
		Label transferLabel = new Label(container, buttonWidth, BUTTON_HEIGHT, fieldFont, Color.white, ConstantStore.get("PARTY_INVENTORY_SCENE", "TRANSFER"));
		transferLabel.setAlignment(Alignment.CENTER);
		
		transferButton = new Button(container, buttonWidth, BUTTON_HEIGHT, transferLabel);
		transferButton.addListener(new ButtonListener());
		mainLayer.add(transferButton, mainLayer.getPosition(ReferencePoint.BOTTOMRIGHT), ReferencePoint.BOTTOMRIGHT, -PADDING, -PADDING);
		
		// Function button
		String functionText = null;
		
		if (extraButtonFunctionality == EXTRA_BUTTON_FUNC.SELL) {
			functionText = ConstantStore.get("PARTY_INVENTORY_SCENE", "SELL");
		} else {
			functionText = ConstantStore.get("PARTY_INVENTORY_SCENE", "DROP");
		}
		
		functionButton = new Button(container, buttonWidth, BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, functionText));
		functionButton.addListener(new ButtonListener());
		mainLayer.add(functionButton, transferButton.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMRIGHT, -PADDING, 0);
		
		// Bin button
		Label binLabel = new Label(container, buttonWidth, fieldFont, Color.white, "");
		binLabel.setAlignment(Alignment.CENTER);
		binButton = new Counter(container, buttonWidth, BIN_BUTTON_HEIGHT, binLabel);
		binButton.setDisableAutoCount(true);
		binButton.addListener(new ButtonListener());
		
		int yOffset = (int) (closeButton.getPosition(ReferencePoint.TOPLEFT).getY() - mainLayer.getPosition(ReferencePoint.CENTERCENTER).getY()) - PADDING;
		mainLayer.add(binButton, mainLayer.getPosition(ReferencePoint.CENTERCENTER), ReferencePoint.BOTTOMCENTER, 0, yOffset);
		
		int numberOfBinPockets = playerInventoryButtons.length;
		numberOfBinPockets += vehicleInventoryButtons != null ? 1 : 0;
		binInventory = new Inventory[numberOfBinPockets];
		
		for (int i = 0; i < binInventory.length; i++) {
			binInventory[i] = new Inventory(1, Integer.MAX_VALUE);
		}
		
		updateBinButton();
		
		backgroundLayer.add(new Panel(container, new Color(0x3b2d59)));
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		return;
	}
	
	/**
	 * Update the bin button to show the correct item and the correct amount of item.
	 */
	public void updateBinButton() {
		int amount = 0;
		String name = "";
		
		for (int i = 0; i < binInventory.length; i++) {
			List<ITEM_TYPE> itemType = binInventory[i].getPopulatedSlots();
			
			if (itemType.size() > 0) {
				amount += binInventory[i].getNumberOf(itemType.get(0));
				
				if (name.length() == 0) {
					name = itemType.get(0).getName();
				}
			}
		}
		
		if (amount == 0) {
			binButton.setCount(0);
			binButton.setShowLabel(false);
			binButton.setShowSprite(false);
			binButton.setHideCount(true);
			binButton.setDisabled(true);
		} else {
			String itemImagePath = "resources/graphics/icons/items/" + getBinItemType().toString().toLowerCase() + ".png";
			Sprite sprite = null;
			
			if (new File(itemImagePath).exists()) {
				try {
					sprite = new Sprite(container, 48, new Image(itemImagePath, false, Image.FILTER_NEAREST));
				} catch (SlickException e) {
					e.printStackTrace();
				}
			}
			
			binButton.setSprite(sprite);
			binButton.setText(name);
			
			if (sprite != null) {
				binButton.setShowSprite(true);
				binButton.setShowLabel(false);
			} else {
				binButton.setShowLabel(true);
				binButton.setShowSprite(false);
			}

			binButton.setCount(amount);
			binButton.setHideCount(false);
			binButton.setDisabled(false);
		}
	}
	
	/**
	 * Update all the {@code OwnerInventoryButtons} and the vehicle inventory buttons.
	 */
	public void updateGraphics() {
		for (OwnerInventoryButtons oib : playerInventoryButtons) {
			oib.updateGraphics();
		}
		
		if (vehicleInventoryButtons != null) {
			vehicleInventoryButtons.updateGraphics();
		}
	}
	
	/**
	 * Check to see if an {@code ITEM_TYPE} can be added to the bin.
	 * @param item ITEM_TYPE to check
	 * @return Whether or not the item can be added to the bin
	 */
	public boolean canAddItemToBin(ITEM_TYPE item) {
		for (int i = 0; i < binInventory.length; i++) {
			if (!binInventory[i].canGetItems(item, 1)) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Returns all the items in the bin to their correct owner.
	 */
	public void returnBinItems() {
		for (int i = 0; i < binInventory.length; i++) {
			List<ITEM_TYPE> populated = binInventory[i].getPopulatedSlots();
			
			if (populated.size() > 0) {
				ITEM_TYPE itemToRemove = populated.get(0);
				List<Item> itemsRemoved = binInventory[i].removeItemFromInventory(itemToRemove, binInventory[i].getNumberOf(itemToRemove));
				
				if (i != party.getPartyMembers().size()) {
					// If the item belongs to a person
					playerInventoryButtons[i].addItemToInventory(itemsRemoved);
				} else {
					// ... or the vehicle
					vehicleInventoryButtons.addItemToInventory(itemsRemoved);
				}
			}
		}
	}
	
	/**
	 * Get the current mode.
	 * @return Current mode
	 */
	public static Mode getCurrentMode() {
		return currentMode;
	}
	
	/**
	 * Get the size of the bin
	 * @return The number of items are in the bin
	 */
	public int getBinSize() {
		int size = 0;
		for (int i = 0; i < binInventory.length; i++) {
			size += binInventory[i].getNumberOfItems();
		}
		
		return size;
	}
	
	/**
	 * The {@code ITEM_TYPE} that the bin is currently holding, if any.
	 * @return ITEM_TYPE that the bin is holding
	 */
	public ITEM_TYPE getBinItemType() {
		ITEM_TYPE itemType = null;
		for (int i = 0; i < binInventory.length; i++) {
			List<ITEM_TYPE> populatedSlots = binInventory[i].getPopulatedSlots();
			if (populatedSlots.size() > 0) {
				itemType = populatedSlots.get(0);
				break;
			}
		}
		
		return itemType;
	}
	
	/**
	 * Get the a list of {@code Item} in the bin.
	 * @return List of Items in the bin
	 */
	public List<Item> getItemsInBin() {
		ITEM_TYPE itemType = getBinItemType();
		
		if (itemType == null) {
			// No items in the bin, return null
			return null;
		}
		
		List<Item> items = new ArrayList<Item>();
		for (int i = 0; i < binInventory.length; i++) {
			items.addAll(binInventory[i].removeItemFromInventory(itemType, binInventory[i].getNumberOf(itemType)));
		}
		
		return items;
	}
	
	/**
	 * Clears the bin out of all items and updates the bin button to reflect the changes.
	 */
	public void emptyBin() {
		for (int i = 0; i < binInventory.length; i++) {
			binInventory[i].clear();
		}
		
		updateBinButton();
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
	
	/**
	 * {@code ButtonListener} listens and acts on buttons in {@code PartyInventoryScene}.
	 */
	private class ButtonListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent component) {
			if (component == closeButton) {
				// Ok, the user hit close - return any items in the bin and go back to the previous scene
				returnBinItems();
				
				updateBinButton();
				
				GameDirector.sharedSceneListener().sceneDidEnd(PartyInventoryScene.this);
			} else if (component == functionButton) {
				// Extra functionality time - what can we do?
				if (extraButtonFunctionality == EXTRA_BUTTON_FUNC.SELL) {
					// Ok, we're going to sell so get all the items together, give them to the store and give us money!
					List<Item> items = getItemsInBin();
					
					if (items == null) {
						// No items in the bin, so stop
						showModal(new MessageModal(container, PartyInventoryScene.this, ConstantStore.get("PARTY_INVENTORY_SCENE", "ERR_EMPTY_BIN")));
						return;
					}
					
					storeInventory.addItemsToInventory(items);
					emptyBin();
					
					party.setMoney(party.getMoney() + items.size() * items.get(0).getCost());
				} else if (extraButtonFunctionality == EXTRA_BUTTON_FUNC.DROP) {
					if (getBinSize() <= 0) {
						// Nothing in the bin, so we can't drop
						showModal(new MessageModal(container, PartyInventoryScene.this, ConstantStore.get("PARTY_INVENTORY_SCENE", "ERR_EMPTY_BIN")));
						return;
					}
					
					// Ok, drop... Just empty the bin...
					emptyBin();
				}
			} else if (component == transferButton) {
				// Check if the bin has anything in it before proceeding
				if (getBinSize() <= 0) {
					// Nothing in the bin, so we can't transfer.
					showModal(new MessageModal(container, PartyInventoryScene.this, ConstantStore.get("PARTY_INVENTORY_SCENE", "ERR_EMPTY_BIN")));
					return;
				}
				
				// Change the button text depending on what mode we're in
				if (currentMode == Mode.NORMAL) {
					currentMode = Mode.TRANSFER;
					transferButton.setText(ConstantStore.get("GENERAL", "CANCEL"));
					binButton.setDisabled(true);
					functionButton.setDisabled(true);
				} else {
					currentMode = Mode.NORMAL;
					transferButton.setText(ConstantStore.get("PARTY_INVENTORY_SCENE", "TRANSFER"));
					binButton.setDisabled(false);
					functionButton.setDisabled(false);
				}
				
				updateGraphics();
			} else if (component == binButton) {
				// Return all items back to their sources
				returnBinItems();
				updateBinButton();
			}
		}
	}
	
	/**
	 * {@code OwnerInventoryButtonsListener} listens for button events from the {@code OwnerInventoryButtons}.
	 */
	private class OwnerInventoryButtonsListener implements ItemListener {
		@Override
		public void itemButtonPressed(OwnerInventoryButtons ownerInventoryButtons, ITEM_TYPE item) {
			if (currentMode == Mode.TRANSFER) {
				return;
			}
			
			// User wants to add items to the bin, check to see if the bin can hold it
			if (!canAddItemToBin(item)) {
				Logger.log("Bin cannot hold " + item + " at the moment", Logger.Level.INFO);
				
				Logger.log("Bin is giving everyone back their items", Logger.Level.INFO);
				returnBinItems();
			}
			
			// If we get this far, the bin can hold the item
			Logger.log("Item was removed! Item was " + item, Logger.Level.INFO);
			
			// Find out who removed the item
			int binInventoryIndex = -1;
			
			// Check the people
			for (int i = 0; i < playerInventoryButtons.length; i++) {
				if (playerInventoryButtons[i] == ownerInventoryButtons) {
					binInventoryIndex = i;
				}
			}
			
			// Check the vehicle (it gets the last index)
			if (vehicleInventoryButtons != null && vehicleInventoryButtons == ownerInventoryButtons) {
				binInventoryIndex = binInventory.length - 1;
			}
			
			// Add the item to the bin inventory in the correct spot so we know who the source was if we want to remove it from the bin
			// Also remove the item from the person's inventory
			List<Item> itemsRemoved = ownerInventoryButtons.removeItemFromInventory(item, 1);
			
			binInventory[binInventoryIndex].addItemsToInventory(itemsRemoved);
			
			updateBinButton();
		}
		
		@Override
		public void itemButtonPressed(OwnerInventoryButtons ownerInventoryButtons) {
			// User has clicked on a "Free" button (most likely) so we will transfer all the items into their inventory
			
			// Check to see if the source can hold all these items in the bin
			if (ownerInventoryButtons.canAddItems(getBinItemType(), getBinSize())) {
				// Add items to their inventory
				List<Item> items = getItemsInBin();
				
				ownerInventoryButtons.addItemToInventory(items);
				
				// Reset everything
				currentMode = Mode.NORMAL;
				binButton.setDisabled(false);
				functionButton.setDisabled(false);
				transferButton.setText(ConstantStore.get("PARTY_INVENTORY_SCENE", "TRANSFER"));
				
				updateGraphics();
				
				emptyBin();
			} else {
				// Oh noez! They can't do that. Yell at the user and tell them to try again later
				showModal(new MessageModal(container, PartyInventoryScene.this, ConstantStore.get("PARTY_INVENTORY_SCENE", "ERR_INV_FAIL")));
			}
		}
	}
}