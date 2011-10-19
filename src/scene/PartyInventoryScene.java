package scene;

import java.util.ArrayList;

import model.Inventory;
import model.Item;
import model.Item.ITEM_TYPE;
import model.Party;
import model.Person;
import model.item.Vehicle;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import component.Button;
import component.CountingButton;
import component.ItemListener;
import component.Label;
import component.Modal;
import component.Label.Alignment;
import component.OwnerInventoryButtons;
import component.Panel;
import component.Positionable;
import component.Positionable.ReferencePoint;

import core.ConstantStore;
import core.FontManager;
import core.GameDirector;
import core.Logger;

public class PartyInventoryScene extends Scene {
	public static final SceneID ID = SceneID.PartyInventory;
	
	public static enum EXTRA_BUTTON_FUNC {SELL, DROP};
	public static enum Mode {NORMAL, TRANSFER};
	
	private static final int PADDING = 20;
	
	private static final int NUM_COLS = 2;
	private static final int BUTTON_HEIGHT = 40;
	private static final int BUTTON_WIDTH = 200;
	
	private static final int BIN_BUTTON_HEIGHT = 100;
	private static final int BIN_BUTTON_WIDTH = BUTTON_WIDTH;
	
	private Party party;
	private Inventory[] binInventory;
	private Inventory storeInventory;
	
	private OwnerInventoryButtons playerInventoryButtons[];
	private OwnerInventoryButtons vehicleInventoryButtons;
	
	private Button closeButton, transferButton, functionButton;
	private CountingButton binButton;
	
	private EXTRA_BUTTON_FUNC extraButtonFunctionality;
	private static Mode currentMode;
	
	public PartyInventoryScene(Party party) {
		this.party = party;
		
		this.extraButtonFunctionality = EXTRA_BUTTON_FUNC.DROP;
		
		currentMode = Mode.NORMAL;
	}
	
	public PartyInventoryScene(Party party, Inventory storeInventory) {
		this.party = party;
		
		this.storeInventory = storeInventory;
		extraButtonFunctionality = EXTRA_BUTTON_FUNC.SELL;
		
		currentMode = Mode.NORMAL;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);

		Font fieldFont = GameDirector.sharedSceneListener().getFontManager().getFont(FontManager.FontID.FIELD);
		
		ArrayList<Person> members = party.getPartyMembers();
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
		
		mainLayer.addAsGrid(personPanels, mainLayer.getPosition(ReferencePoint.TopLeft), (int)(members.size() / 2), NUM_COLS, PADDING, PADDING, peopleSpacing, PADDING);
		
		// Create Vehicle inventories (if one exists)
		if (vehicle != null) {
			int lastOddIndex = personPanels.length - 1;
			if (lastOddIndex % 2 != 0) {
				lastOddIndex--;
			}
			Positionable locationReference = personPanels[lastOddIndex];
	
			vehicleInventoryButtons = new OwnerInventoryButtons(vehicle);
			vehicleInventoryButtons.setFont(fieldFont);
			vehicleInventoryButtons.setListener(new OwnerInventoryButtonsListener());
			
			vehicleInventoryButtons.makePanel(container);
			
			mainLayer.add(vehicleInventoryButtons.getPanel(), locationReference.getPosition(ReferencePoint.BottomLeft), ReferencePoint.TopLeft, 0, PADDING);
		}
		
		// Close button
		closeButton = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, ConstantStore.get("GENERAL", "CLOSE")));
		closeButton.addListener(new ButtonListener());
		mainLayer.add(closeButton, mainLayer.getPosition(ReferencePoint.BottomLeft), ReferencePoint.BottomLeft, PADDING, -PADDING);
		
		// Transfer button
		Label transferLabel = new Label(container, BUTTON_WIDTH, BUTTON_HEIGHT, fieldFont, Color.white, ConstantStore.get("PARTY_INVENTORY_SCENE", "TRANSFER"));
		transferLabel.setAlignment(Alignment.Center);
		
		transferButton = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, transferLabel);
		transferButton.addListener(new ButtonListener());
		mainLayer.add(transferButton, mainLayer.getPosition(ReferencePoint.BottomRight), ReferencePoint.BottomRight, -PADDING, -PADDING);
		
		// Function button
		String functionText = null;
		
		if (extraButtonFunctionality == EXTRA_BUTTON_FUNC.SELL) {
			functionText = ConstantStore.get("PARTY_INVENTORY_SCENE", "SELL");
		} else {
			functionText = ConstantStore.get("PARTY_INVENTORY_SCENE", "DROP");
		}
		
		functionButton = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, functionText));
		functionButton.addListener(new ButtonListener());
		mainLayer.add(functionButton, transferButton.getPosition(ReferencePoint.BottomLeft), ReferencePoint.BottomRight, -PADDING, 0);
		
		// Bin button
		Label binLabel = new Label(container, BUTTON_WIDTH, fieldFont, Color.white, "");
		binLabel.setAlignment(Alignment.Center);
		binButton = new CountingButton(container, BIN_BUTTON_WIDTH, BIN_BUTTON_HEIGHT, binLabel);
		binButton.setDisableAutoCount(true);
		binButton.addListener(new ButtonListener());
		
		int xOffset = ((container.getWidth() - 2 * PADDING) / 2) - (BIN_BUTTON_WIDTH / 2);
		int yOffset = (int)((personPanels[personPanels.length - 1].getPosition(ReferencePoint.BottomLeft).y + PADDING + BUTTON_HEIGHT - closeButton.getPosition(ReferencePoint.TopLeft).y) / 2) + (BIN_BUTTON_HEIGHT / 2); 
		
		mainLayer.add(binButton, closeButton.getPosition(ReferencePoint.TopLeft), ReferencePoint.BottomLeft, xOffset, yOffset);
		
		int numberOfBinPockets = playerInventoryButtons.length;
		numberOfBinPockets += vehicleInventoryButtons != null ? 1 : 0;
		binInventory = new Inventory[numberOfBinPockets];
		
		for (int i = 0; i < binInventory.length; i++) {
			binInventory[i] = new Inventory(1, Integer.MAX_VALUE);
		}
		
		backgroundLayer.add(new Panel(container, new Color(0x3b2d59)));
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		return;
	}
	
	public void updateBinButton() {
		int amount = 0;
		String name = "";
		
		for (int i = 0; i < binInventory.length; i++) {
			ArrayList<ITEM_TYPE> itemType = binInventory[i].getPopulatedSlots();
			
			if (itemType.size() > 0) {
				amount += binInventory[i].getNumberOf(itemType.get(0));
				
				if (name.length() == 0) {
					name = itemType.get(0).getName();
				}
			}
		}
		
		binButton.setCount(amount);
		binButton.setText(name);
	}
	
	public void updateGraphics() {
		for (OwnerInventoryButtons oib : playerInventoryButtons) {
			oib.updateGraphics();
		}
		
		if (vehicleInventoryButtons != null) {
			vehicleInventoryButtons.updateGraphics();
		}
	}
	
	public boolean canAddItemToBin(ITEM_TYPE item) {
		for (int i = 0; i < binInventory.length; i++) {
			if (binInventory[i].canAddItems(item, 1) == false) {
				return false;
			}
		}
		
		return true;
	}
	
	public void returnBinItems() {
		for (int i = 0; i < binInventory.length; i++) {
			ArrayList<ITEM_TYPE> populated = binInventory[i].getPopulatedSlots();
			
			if (populated.size() > 0) {
				ITEM_TYPE itemToRemove = populated.get(0);
				ArrayList<Item> itemsRemoved = binInventory[i].removeItem(itemToRemove, binInventory[i].getNumberOf(itemToRemove));
				playerInventoryButtons[i].addItemToInventory(itemsRemoved);
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
	
	public int getBinSize() {
		int size = 0;
		for (int i = 0; i < binInventory.length; i++) {
			size += binInventory[i].getNumberOfItems();
		}
		
		return size;
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
	
	private class ButtonListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent component) {
			if (component == closeButton) {
				returnBinItems();
				
				GameDirector.sharedSceneListener().sceneDidEnd(PartyInventoryScene.this);
			} else if (component == functionButton) {
				if (extraButtonFunctionality == EXTRA_BUTTON_FUNC.SELL) {
					//
				} else if (extraButtonFunctionality == EXTRA_BUTTON_FUNC.DROP) {
					for (int i = 0; i < binInventory.length; i++) {
						binInventory[i].clear();
					}
					updateBinButton();
				}
			} else if (component == transferButton) {
				// Check if the bin has anything in it before proceeding
				if (getBinSize() <= 0) {
					// Nothing in the bin, so we can't transfer.
					showModal(new Modal(container, PartyInventoryScene.this, ConstantStore.get("PARTY_INVENTORY_SCENE", "ERR_EMPTY_BIN"), ConstantStore.get("GENERAL", "OK")));
					return;
				}
				
				if (currentMode == Mode.NORMAL) {
					currentMode = Mode.TRANSFER;
					transferButton.setText(ConstantStore.get("GENERAL", "CANCEL"));
				} else {
					currentMode = Mode.NORMAL;
					transferButton.setText(ConstantStore.get("PARTY_INVENTORY_SCENE", "TRANSFER"));
				}
				
				updateGraphics();
			} else if (component == binButton) {
				returnBinItems();
				updateBinButton();
			}
		}
	}
	
	private class OwnerInventoryButtonsListener implements ItemListener {
		@Override
		public void itemButtonPressed(OwnerInventoryButtons ownerInventoryButtons, ITEM_TYPE item) {
			if (!canAddItemToBin(item)) {
				Logger.log("Bin cannot hold "+item+" at the moment", Logger.Level.INFO);
				
				Logger.log("Bin is giving everyone back their items", Logger.Level.INFO);
				returnBinItems();
			}
			
			Logger.log("Item was removed! Item was "+item, Logger.Level.INFO);
			
			//Find out who removed the item
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
			ArrayList<Item> itemsRemoved = ownerInventoryButtons.removeItemFromInventory(item, 1);
			
			binInventory[binInventoryIndex].addItem(itemsRemoved);
			
			updateBinButton();
		}
		
		@Override
		public void itemButtonPressed(OwnerInventoryButtons ownerInventoryButtons) {
			ITEM_TYPE itemType = null;
			for (int i = 0; i < binInventory.length; i++) {
				ArrayList<ITEM_TYPE> populatedSlots = binInventory[i].getPopulatedSlots();
				if (populatedSlots.size() > 0) {
					itemType = populatedSlots.get(0);
					break;
				}
			}
			
			if (ownerInventoryButtons.canAddItems(itemType, getBinSize())) {
				ArrayList<Item> items = new ArrayList<Item>();
				for (int i = 0; i < binInventory.length; i++) {
					items.addAll(binInventory[i].removeItem(itemType, binInventory[i].getNumberOf(itemType)));
				}
				
				ownerInventoryButtons.addItemToInventory(items);
				
				// Reset everything
				currentMode = Mode.NORMAL;
				transferButton.setText(ConstantStore.get("PARTY_INVENTORY_SCENE", "TRANSFER"));
				
				updateGraphics();
				
				for (int i = 0; i < binInventory.length; i++) {
					binInventory[i].clear();
				}
				updateBinButton();
			} else {
				showModal(new Modal(container, PartyInventoryScene.this, ConstantStore.get("PARTY_INVENTORY_SCENE", "ERR_INV_FAIL"), ConstantStore.get("GENERAL", "OK")));
			}
		}
	}
}