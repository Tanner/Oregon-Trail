package scene;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.*;
import model.item.*;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.gui.*;
import org.newdawn.slick.state.*;
import component.*;
import component.Positionable.ReferencePoint;
import component.modal.ComponentModal;
import component.modal.MessageModal;
import component.modal.Modal;
import component.sprite.Sprite;
import core.*;

/**
 * A scene that displays an inventory, and allows the user to
 * purchase items.
 */
public class StoreScene extends Scene {
	public static final SceneID ID = SceneID.STORE;
	
	private final int PADDING = 20;
	private final int REGULAR_BUTTON_HEIGHT = 30;
	private final int INVENTORY_BUTTON_WIDTH = 130;
	private final int INVENTORY_BUTTON_HEIGHT = 90;
	private final Color BACKGROUND_COLOR = new Color(0x0C5DA5);
	private final Color TEXT_PANEL_COLOR = new Color(0x679FD2);
	
	private List<ItemType> buttonMap;
	private Counter[] storeInventory;
	private Panel storeInventoryButtons, textPanel;
	private Button cancelButton, clearButton, inventoryButton, buyButton;
	private Label[] itemDescription;
	private Label partyMoney;
	private ComponentModal<SegmentedControl> buyModal;
	private MessageModal failedBuyModal;
	
	private List<Item> currentPurchase;
	private List<Inventoried> currentParty;
	
	private ItemType currentItem = null;
	private ItemType hoverItem = null;
	
	private Inventory inv;
	private Party party;
	
	private double priceModifier;
	
	/**
	 * This method sets up
	 * @param p The players party
	 * @param storeInventory The inventory the store will use (passed from GameDirector)
	 */
	public StoreScene (Party p, Inventory storeInventory, double priceModifier) {
		this.party = p;
		this.inv = storeInventory;
		this.priceModifier = priceModifier;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		createComponents();
		
		storeInventoryButtons.addAsGrid(storeInventory, mainLayer.getPosition(ReferencePoint.TOPLEFT), 4, 4, 0, 0, PADDING, PADDING);
		mainLayer.add(storeInventoryButtons, mainLayer.getPosition(ReferencePoint.TOPLEFT), Positionable.ReferencePoint.TOPLEFT, PADDING, PADDING);
		
		mainLayer.add(partyMoney, storeInventoryButtons.getPosition(ReferencePoint.BOTTOMCENTER), Positionable.ReferencePoint.TOPCENTER, 0, PADDING);

		mainLayer.add(textPanel, mainLayer.getPosition(ReferencePoint.TOPRIGHT), Positionable.ReferencePoint.TOPRIGHT, -PADDING, PADDING);
		textPanel.addAsColumn(Arrays.asList(itemDescription).iterator(), textPanel.getPosition(ReferencePoint.TOPLEFT), PADDING, PADDING, PADDING);
		
		mainLayer.add(clearButton, mainLayer.getPosition(ReferencePoint.BOTTOMRIGHT), Positionable.ReferencePoint.BOTTOMRIGHT, -PADDING, -PADDING);
		mainLayer.add(buyButton, clearButton.getPosition(ReferencePoint.TOPLEFT), Positionable.ReferencePoint.TOPRIGHT, -PADDING, 0);
		
		Vector2f cancelPos = new Vector2f(storeInventoryButtons.getPosition(ReferencePoint.BOTTOMLEFT).getX(), buyButton.getPosition(ReferencePoint.TOPLEFT).getY());
		Vector2f inventoryPos = new Vector2f(storeInventoryButtons.getPosition(ReferencePoint.BOTTOMRIGHT).getX(), buyButton.getPosition(ReferencePoint.TOPLEFT).getY());
		mainLayer.add(cancelButton, cancelPos, Positionable.ReferencePoint.TOPLEFT, 0, 0);
		mainLayer.add(inventoryButton, inventoryPos, Positionable.ReferencePoint.TOPRIGHT, 0, 0);
		
		backgroundLayer.add(new Panel(container, BACKGROUND_COLOR));
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		//Re-enable all buttons if currently selected item's quantity goes back to 0
		if (currentItem != null && storeInventory[getButtonIndex(currentItem)].getCount() == storeInventory[getButtonIndex(currentItem)].getMax()) {
			currentItem = null;
			buyButton.setDisabled(true);
			clearButton.setDisabled(true);
			for (int i = 0; i < storeInventory.length; i++) {
					storeInventory[i].setDisabled(false);
			}
			return;
		}
		
		//Don't do anything if there is no hovering/item selection
		if (hoverItem == null && currentItem == null ) {
			return;
		} else if ( hoverItem != null && getButtonIndex(hoverItem) != getButtonIndex(currentItem) ) {
			//Display information for the item currently being hovered over
			updateLabels(hoverItem);
		} else {
			//Display information for currently selected item, as well as disable
			//all other buttons.
			buyButton.setDisabled(false);
			clearButton.setDisabled(false);
			updateLabels(currentItem);
			
			for (int i = 0; i < storeInventory.length; i++) {
				if (i != getButtonIndex(currentItem)) {
					storeInventory[i].setDisabled(true);
				}
			}
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		if (key == Input.KEY_ESCAPE) {
			GameDirector.sharedSceneListener().sceneDidEnd(this);
		}
	}
	
	@Override
	public int getID() {
		return ID.ordinal();
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		if (mainLayer.isVisible() && mainLayer.isAcceptingInput()) {
			for (int i = 0; i < storeInventory.length; i++) {
				if (((Rectangle) storeInventory[i].getArea()).contains(newx, newy)) {
					hoverItem = getItemFromButtonIndex(i);
					return;
				}
			}
			
			hoverItem = null;
		}
	}
	
	@Override
	public void dismissModal(Modal modal, int button) {
		super.dismissModal(modal, button);
		
		if (modal == buyModal) {
			if (button == modal.getCancelButtonIndex()) {
				inv.addItemsToInventory(currentPurchase);
			} else {
				int[] buyer = buyModal.getComponent().getSelection();
				party.buyItemForInventory(currentPurchase, currentParty.get(buyer[0]));
				storeInventory[getButtonIndex(currentItem)].setMax(inv.getNumberOf(currentItem));
				updatePartyMoneyLabel();
			}
		}
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game)  {
		super.enter(container, game) ;
			for (ItemType item : inv.getPopulatedSlots() ) {
				storeInventory[getButtonIndex(item)].setMax(inv.getNumberOf(item));
				storeInventory[getButtonIndex(item)].setCount(inv.getNumberOf(item));
			}
		updatePartyMoneyLabel();
	}
	
	/**
	 * Create all on-screen components for the scene.
	 */
	private void createComponents() {
		Label tempLabel;
		Font fieldFont = FontStore.get().getFont(FontStore.FontID.FIELD);
		
		//Create inventory buttons and a map to which button stands for which inventory item
		List<ItemType> inventorySlots = inv.getPopulatedSlots();
		buttonMap = new ArrayList<ItemType>();
		
		//Create money label
		partyMoney = new Label(container, INVENTORY_BUTTON_WIDTH * 4 + PADDING * 3, REGULAR_BUTTON_HEIGHT, fieldFont, Color.white, "");
		partyMoney.setAlignment(Label.Alignment.CENTER);
		updatePartyMoneyLabel();
		
		storeInventory = new Counter[inventorySlots.size()];
		for (int i = 0; i < inventorySlots.size(); i++) {
			ItemType currentType = inventorySlots.get(i);
			buttonMap.add(currentType);
			tempLabel = new Label(container, fieldFont, Color.white, currentType.getName());
			String itemImagePath = ConstantStore.PATH_ITEMS + currentType.toString().toLowerCase() + ".png";
			Sprite sprite = null;
			if (new File(itemImagePath).exists()) {
				try {
					sprite = new Sprite(container, 48, new Image(itemImagePath, false, Image.FILTER_NEAREST));
				} catch (SlickException e) {
					e.printStackTrace();
				}
			}
			storeInventory[i] = new Counter(container, INVENTORY_BUTTON_WIDTH, INVENTORY_BUTTON_HEIGHT, sprite, tempLabel);
			if (sprite != null) {
				storeInventory[i].setShowLabel(false);
			}
			storeInventory[i].setMax(inv.getNumberOf(currentType));
			storeInventory[i].setCount(inv.getNumberOf(currentType));
			storeInventory[i].setCountUpOnLeftClick(false);
			storeInventory[i].addListener(new InventoryListener(currentType));
		}
		
		storeInventoryButtons = new Panel(container,
				INVENTORY_BUTTON_WIDTH * 4 + PADDING * 3,
				container.getHeight() - REGULAR_BUTTON_HEIGHT - partyMoney.getHeight() - PADDING * 4);
		
		//Create cancel & inventory buttons
		tempLabel = new Label(container, fieldFont, Color.white, ConstantStore.get("GENERAL", "LEAVE"));
		cancelButton = new Button(container, (container.getWidth() - PADDING * 4) / 4, REGULAR_BUTTON_HEIGHT, tempLabel);
		cancelButton.addListener(new ButtonListener());
		
		tempLabel = new Label(container, fieldFont, Color.white, ConstantStore.get("STORE_SCENE", "INVENTORY"));
		inventoryButton = new Button(container, cancelButton.getWidth(), REGULAR_BUTTON_HEIGHT, tempLabel);
		inventoryButton.addListener(new ButtonListener());

		//Create item description text labels
		itemDescription = new Label[7];
		
		int textPanelWidth = mainLayer.getWidth() - (int) storeInventoryButtons.getPosition(ReferencePoint.TOPRIGHT).getX() - PADDING * 3;
		int textPanelLabelWidth = textPanelWidth - PADDING * 2;
		itemDescription[0] = new Label(container, textPanelLabelWidth, fieldFont.getLineHeight(), fieldFont, Color.white, "");
		itemDescription[0].setAlignment(Label.Alignment.CENTER);
		itemDescription[1] = new Label(container, textPanelLabelWidth, 180, fieldFont, Color.white, "");
		itemDescription[1].setAlignment(Label.Alignment.CENTER);
		itemDescription[1].setVerticalAlignment(Label.VerticalAlignment.TOP);
		itemDescription[2] = new Label(container, textPanelLabelWidth, fieldFont.getLineHeight(), fieldFont, Color.white, "");
		itemDescription[3] = new Label(container, textPanelLabelWidth, fieldFont.getLineHeight(), fieldFont, Color.white, "");
		itemDescription[4] = new Label(container, textPanelLabelWidth, fieldFont.getLineHeight(), fieldFont, Color.white, "");
		itemDescription[5] = new Label(container, textPanelLabelWidth, fieldFont.getLineHeight(), fieldFont, Color.white, "");
		itemDescription[6] = new Label(container, textPanelLabelWidth, fieldFont.getLineHeight(), fieldFont, Color.white, "");
		textPanel = new Panel(container, textPanelWidth, container.getHeight() - REGULAR_BUTTON_HEIGHT - PADDING * 3, TEXT_PANEL_COLOR);
		
		//Create clear & buy buttons
		tempLabel = new Label(container, fieldFont, Color.white, ConstantStore.get("STORE_SCENE", "CLEAR"));
		clearButton = new Button(container, textPanelWidth / 2 - PADDING / 2, REGULAR_BUTTON_HEIGHT, tempLabel);
		clearButton.addListener(new ButtonListener());
		clearButton.setDisabled(true);
		
		tempLabel = new Label(container, fieldFont, Color.white, ConstantStore.get("STORE_SCENE", "BUY"));
		buyButton = new Button(container, textPanelWidth / 2 - PADDING / 2, REGULAR_BUTTON_HEIGHT, tempLabel);
		buyButton.addListener(new ButtonListener());
		buyButton.setDisabled(true);
	}
	
	/**
	 * Update all the side labels on the store to reflect the current
	 * active item.
	 * 
	 * @param index The index of the item you wish to display information on
	 */
	private void updateLabels(ItemType currentItem) {
		int count = storeInventory[getButtonIndex(currentItem)].getMax() - storeInventory[getButtonIndex(currentItem)].getCount();
	
		itemDescription[0].setText(currentItem.getName());
		itemDescription[1].setText(currentItem.getDescription());
		itemDescription[2].setText(ConstantStore.get("STORE_SCENE", "WEIGHT") +
				currentItem.getWeight() + " " + ConstantStore.get("GENERAL", "WEIGHT_UNIT"));
		itemDescription[3].setText(ConstantStore.get("STORE_SCENE", "COST") + ConstantStore.get("GENERAL", "MONEY_SYMBOL") + (int)(currentItem.getCost()*priceModifier));
		itemDescription[4].setText(ConstantStore.get("STORE_SCENE", "QUANTITY") + count);
		itemDescription[5].setText(ConstantStore.get("STORE_SCENE", "TOTAL_WEIGHT") + count * currentItem.getWeight() + " " + ConstantStore.get("GENERAL", "WEIGHT_UNIT"));
		itemDescription[6].setText(ConstantStore.get("STORE_SCENE", "TOTAL_COST") + ConstantStore.get("GENERAL", "MONEY_SYMBOL") + count * (int)(currentItem.getCost()*priceModifier));
		updatePartyMoneyLabel();
	}
	
	/**
	 * Update the party's money label with formatting after a purchase is made.
	 */
	private void updatePartyMoneyLabel() {
		partyMoney.setText(ConstantStore.get("STORE_SCENE", "PARTY_MONEY") + ConstantStore.get("GENERAL", "MONEY_SYMBOL") + String.format("%,d", party.getMoney()));
	}
	
	/**
	 * Check the validity of a current purchase after the Buy button has been pressed.
	 * @return -1 if a failed purchase, 0 if purchase can proceed, 1 if you handled the case inside this method
	 */
	private int makePurchase() {
		int itemCount = storeInventory[getButtonIndex(currentItem)].getMax() - storeInventory[getButtonIndex(currentItem)].getCount();
		List<Inventoried> currentBuyers = party.canGetItem(currentItem, itemCount);
		System.out.println(currentBuyers);
		
		//The player doesn't have a wagon and is trying to buy one
		if (currentItem == ItemType.WAGON && party.getVehicle() == null ) {
			if (itemCount > 1 && party.getMoney() >= (int)(ItemType.WAGON.getCost()*priceModifier)) {
				//The player tries to buy too many wagons
				failedBuyModal = new MessageModal(container, this, ConstantStore.get("STORE_SCENE", "ERR_TOO_MANY_WAGON"));
				return -1;
			} else if ( party.getMoney() > (int)(currentItem.getCost()*priceModifier)) {
				//The player is able to buy the wagon
				
				party.setVehicle(new Wagon());
				inv.removeItemFromInventory(currentItem, 1);
				party.setMoney(party.getMoney() - (int)(ItemType.WAGON.getCost()*priceModifier));
				storeInventory[getButtonIndex(currentItem)].setMax(inv.getNumberOf(currentItem));
				updateLabels(currentItem);
				return 1;
			} else {
				//The player doesn't have enough money to buy a wagon at all
				failedBuyModal = new MessageModal(container, this, ConstantStore.get("STORE_SCENE", "ERR_NOT_ENOUGH_MONEY_FOR_WAGON"));
				return -1;
			}
		} else if (currentBuyers.size() == 0) {
			//Display modal if the user can not buy the currently selected item
			String errorText;
			if (currentItem.isAnimal() && party.getAnimals().size() + itemCount > 5) {
				errorText = ConstantStore.get("STORE_SCENE", "ERR_TOO_MANY_ANIMALS");
			} else if (party.getMoney() < itemCount * (int)(currentItem.getCost()*priceModifier)) {
				errorText = ConstantStore.get("STORE_SCENE", "ERR_NOT_ENOUGH_MONEY");
			} else {
				errorText = ConstantStore.get("STORE_SCENE", "ERR_CANT_CARRY");
			}
			failedBuyModal = new MessageModal(container, this, errorText);
			
			return -1;
		} else {
			//Make the purchase
			currentPurchase = inv.removeItemFromInventory(currentItem, itemCount);
			
			//Get a list of all the actual party members and vehicle if it exists
			currentParty = new ArrayList<Inventoried>();
			currentParty.addAll(party.getPartyMembers());
			if (party.getVehicle() != null)
				currentParty.add(party.getVehicle());
			
			//Get all of the people that can't buy the current item,
			//and move them to the back of the list
			List<Inventoried> disabledList = new ArrayList<Inventoried>();
			List<Inventoried> enabledList = new ArrayList<Inventoried>();
			for (int i = 0; i < currentParty.size(); i++) {
				Inventoried currentPerson = currentParty.get(i);
				boolean found = false;
				
				for (int j = 0; j < currentBuyers.size() && !found; j++) {
					if ( currentPerson == currentBuyers.get(j) ) {
						found = true;
					}
				}
				
				if (!found) {
					disabledList.add(currentPerson);
				} else {
					enabledList.add(currentPerson);
				}
			}
			currentParty.clear();
			currentParty.addAll(enabledList);
			currentParty.addAll(disabledList);
			
			int[] disabled = new int[disabledList.size()];
			for (int i = 0; i < disabledList.size(); i++)
				disabled[i] = currentParty.size() - 1 - i;
			
			//Get the names of the people in the party
			String[] names = new String[currentParty.size()];
			for (int i = 0; i < names.length; i++) {
				names[i] = currentParty.get(i).getName();
			}
			
			SegmentedControl choosePlayer = new SegmentedControl(container, 600, 200, 3, 2, 20, true, 1, names);
			choosePlayer.setDisabled(disabled);
			buyModal = new ComponentModal<SegmentedControl>(container,
					this,
					ConstantStore.get("STORE_SCENE", "PICK_RECEIVER"),
					2,
					choosePlayer);
			buyModal.setButtonText(buyModal.getCancelButtonIndex(), ConstantStore.get("GENERAL", "CANCEL"));
			buyModal.setButtonText(buyModal.getCancelButtonIndex() + 1, ConstantStore.get("STORE_SCENE", "BUY"));
			return 0;
		}
	}
	
	/**
	 * A helper method to find the button index in the inventoryButton
	 * array, given an Item type.
	 * @param item The item to 
	 * @return the index in the button array of the item
	 */
	private int getButtonIndex(ItemType item) {
		return buttonMap.indexOf(item);
	}
	
	/**
	 * A helper method to find the Item type for a certain
	 * button, given the button's index in the inventoryButton
	 * array.
	 * @param index the index of inventoryButton
	 * @return the Item that the given button is associated with
	 */
	private ItemType getItemFromButtonIndex(int index) {
		return buttonMap.get(index);
	}
	
	/**
	 * The method to return the current inventory of the store.
	 * @return The store's inventory
	 */
	public Inventory getInventory() {
		return inv;
	}
	
	/**
	 * Check for presses from the cancel/inventory/clear buttons.
	 */
	private class ButtonListener implements ComponentListener {
		public void componentActivated(AbstractComponent source) {
			System.out.println(party.getAnimals());
			if (source == cancelButton) {
				GameDirector.sharedSceneListener().sceneDidEnd(StoreScene.this);
			} else if (source == inventoryButton) {
				GameDirector.sharedSceneListener().requestScene(SceneID.PARTYINVENTORY, StoreScene.this, false);
			} else if (source == buyButton) {
				int successCode = makePurchase();
				if (successCode == 0) {
					showModal(buyModal);
				} else if (successCode == -1) {
					showModal(failedBuyModal);
				}
			} else if (currentItem != null) {
				storeInventory[getButtonIndex(currentItem)].setCount(storeInventory[getButtonIndex(currentItem)].getMax());
				updateLabels(currentItem);
			}
		}
	}
	
	/**
	 *	Check for presses on the store's inventory item buttons
	 */
	private class InventoryListener implements ComponentListener {
		private ItemType item;
		
		public InventoryListener(ItemType item) {
			this.item = item;
		}
		
		public void componentActivated(AbstractComponent source) {
			if (currentItem == null || storeInventory[getButtonIndex(item)].getCount() == 0) {
				currentItem = item;
			}
		}
	}
}
