package scene;

import java.util.ArrayList;
import java.util.PriorityQueue;

import model.*;
import model.item.*;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.gui.*;
import org.newdawn.slick.state.*;
import component.*;
import component.Positionable.ReferencePoint;
import core.*;

/**
 * A scene that displays an inventory, and allows the user to
 * purchase items.
 */
public class StoreScene extends Scene {
	
	public static final SceneID ID = SceneID.Store;
	
	private final int PADDING = 20;
	private final int WIDE_PADDING = PADDING * 2;
	private final int BUTTON_WIDTH = 260;
	private final int BUTTON_HEIGHT = 45;
	private final int INVENTORY_BUTTON_WIDTH = 130;
	private final int INVENTORY_BUTTON_HEIGHT = 90;
	private final Color BACKGROUND_COLOR = new Color(0x0C5DA5);
	private final Color TEXT_PANEL_COLOR = new Color(0x679FD2);
		
	private CountingButton[] storeInventory;
	private Panel storeInventoryButtons, textPanel;
	private Button cancelButton, clearButton, inventoryButton, buyButton;
	private Label[] itemDescription;
	private Label partyMoney;
	private Modal buyModal;
	
	private int currentItem = -1;
	private int hoverItem = -1;
	
	String tempDescription = "This is an item description.\nIt is a good item, maybe a sonic screwdriver.\n\nYep.";
	Inventory inv;
	ArrayList<PriorityQueue<Item>> tempInv;
	Party p;
	
	public StoreScene (Party p) {
		this.p = p;
		inv = new Inventory(8,10000);
		ArrayList<Item> itemToAdd = new ArrayList<Item>();
		for (int i = 0; i < 1 + Math.random() * 10; i++) {
			itemToAdd.add(new Apple());
		}
		inv.addItem(itemToAdd);
		itemToAdd.clear();
		for (int i = 0; i < 1 + Math.random() * 10; i++) {
			itemToAdd.add(new Bread());
		}
		inv.addItem(itemToAdd);
		itemToAdd.clear();
		for (int i = 0; i < 1 + Math.random() * 10; i++) {
			itemToAdd.add(new Bullet());
		}
		inv.addItem(itemToAdd);
		itemToAdd.clear();
		for (int i = 0; i < 1 + Math.random() * 10; i++) {
			itemToAdd.add(new Gun());
		}
		inv.addItem(itemToAdd);
		itemToAdd.clear();
		for (int i = 0; i < 1 + Math.random() * 10; i++) {
			itemToAdd.add(new Meat());
		}
		inv.addItem(itemToAdd);
		itemToAdd.clear();
		for (int i = 0; i < 1 + Math.random() * 10; i++) {
			itemToAdd.add(new SonicScrewdriver());
		}
		inv.addItem(itemToAdd);
		itemToAdd.clear();
		for (int i = 0; i < 1 + Math.random() * 10; i++) {
			itemToAdd.add(new Wagon());
		}
		inv.addItem(itemToAdd);
		itemToAdd.clear();
		for (int i = 0; i < 1 + Math.random() * 10; i++) {
			itemToAdd.add(new Wheel());
		}
		inv.addItem(itemToAdd);
		tempInv = inv.getSlots();
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		createComponents();
		
		storeInventoryButtons.addAsGrid(storeInventory, mainLayer.getPosition(ReferencePoint.TopLeft), 4, 4, 0, 0, PADDING, PADDING);
		mainLayer.add(storeInventoryButtons, mainLayer.getPosition(ReferencePoint.TopLeft), Positionable.ReferencePoint.TopLeft, PADDING, PADDING);
		
		mainLayer.add(partyMoney, storeInventoryButtons.getPosition(ReferencePoint.BottomCenter),Positionable.ReferencePoint.TopCenter, 0, PADDING);

		mainLayer.add(textPanel,storeInventoryButtons.getPosition(ReferencePoint.TopRight), Positionable.ReferencePoint.TopLeft, WIDE_PADDING, 0);
		textPanel.addAsColumn(itemDescription, textPanel.getPosition(ReferencePoint.TopLeft), PADDING, PADDING, PADDING);
		
		mainLayer.add(clearButton, textPanel.getPosition(ReferencePoint.BottomCenter), Positionable.ReferencePoint.TopCenter, 0, PADDING);
		mainLayer.add(buyButton, clearButton.getPosition(ReferencePoint.BottomLeft), Positionable.ReferencePoint.TopLeft, 0, PADDING/2);
		
		Vector2f cancelPos = new Vector2f(storeInventoryButtons.getPosition(ReferencePoint.BottomLeft).getX(),buyButton.getPosition(ReferencePoint.TopLeft).getY());
		Vector2f inventoryPos = new Vector2f(storeInventoryButtons.getPosition(ReferencePoint.BottomRight).getX(),buyButton.getPosition(ReferencePoint.TopLeft).getY());
		mainLayer.add(cancelButton, cancelPos, Positionable.ReferencePoint.TopLeft, 0, 0);
		mainLayer.add(inventoryButton, inventoryPos, Positionable.ReferencePoint.TopRight, 0, 0);
		
		backgroundLayer.add(new Panel(container, BACKGROUND_COLOR));
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
		//Re-enable all buttons if currently selected item's quantity goes back to 0
		if ( currentItem != -1 && storeInventory[currentItem].getCount() == storeInventory[currentItem].getMax()) {
			currentItem = -1;
			buyButton.setDisabled(true);
			clearButton.setDisabled(true);
			for (int i = 0; i < storeInventory.length; i++) {
					storeInventory[i].setDisabled(false);
			}
			return;
		}
		//Don't do anything if there is no hovering/item selection
		if ( hoverItem == -1 && currentItem == -1 ) {
			//updateLabels(-1);
			return;
		//Display information for the item currently being hovered over
		} else if ( hoverItem != -1 && hoverItem != currentItem ) {
			updateLabels(hoverItem);
		//Display information for currently selected item, as well as disable
		//all other buttons.
		} else {
			buyButton.setDisabled(false);
			clearButton.setDisabled(false);
			updateLabels(currentItem);
			for (int i = 0; i < storeInventory.length; i++) {
				if ( i != currentItem )
					storeInventory[i].setDisabled(true);
			}	
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		if (key == Input.KEY_ESCAPE) {
			GameDirector.sharedSceneDelegate().sceneDidEnd(this);
		}
	}
	
	@Override
	public int getID() {
		return ID.ordinal();
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		for (int i = 0; i < storeInventory.length; i++) {
			if ( contains(storeInventory[i], newx, newy) )
			{ 
				hoverItem = i;
				return;
			}
		}
		hoverItem = -1;
	}
	
	/**
	 * Create all on-screen components for the scene.
	 */
	private void createComponents() {
		Label tempLabel;
		Font fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);
		
		//Create grid of store inventory buttons
		storeInventory = new CountingButton[inv.getCurrentSize()];
		int currentButton = 0;
		int i = 0;
		while ( i < inv.getMaxSize()) {
			if ( tempInv.get(i).size() != 0) {
				tempLabel = new Label(container, fieldFont, Color.white, tempInv.get(i).peek().getName());
				storeInventory[currentButton] = new CountingButton(container, INVENTORY_BUTTON_WIDTH, INVENTORY_BUTTON_HEIGHT, tempLabel);
				storeInventory[currentButton].setMax(tempInv.get(i).size());
				storeInventory[currentButton].setCount(tempInv.get(i).size());
				storeInventory[currentButton].setCountUpOnLeftClick(false);
				storeInventory[currentButton].addListener(new InventoryListener(i));
				currentButton++;
			}
			i++;
		}
		storeInventoryButtons = new Panel(container, INVENTORY_BUTTON_WIDTH * 4 + PADDING * 3, INVENTORY_BUTTON_HEIGHT * 4 + PADDING * 3);
		
		//Create money label
		partyMoney = new Label(container, storeInventoryButtons.getWidth(), BUTTON_HEIGHT, fieldFont, Color.white, "Party's Money: $" + p.getMoney());
		partyMoney.setAlignment(Label.Alignment.Center);
		
		//Create cancel & inventory buttons
		tempLabel = new Label(container, fieldFont, Color.white, "Cancel");
		cancelButton = new Button(container, BUTTON_WIDTH,BUTTON_HEIGHT,tempLabel);
		cancelButton.addListener(new ButtonListener());
		tempLabel = new Label(container, fieldFont, Color.white, "Inventory");
		inventoryButton = new Button(container, BUTTON_WIDTH,BUTTON_HEIGHT,tempLabel);
		inventoryButton.addListener(new ButtonListener());
		tempLabel = new Label(container, fieldFont, Color.white, "Clear");

		
		//Create item description text labels
		itemDescription = new Label[7];
		
		int textPanelWidth = mainLayer.getWidth() - (int) storeInventoryButtons.getPosition(ReferencePoint.TopRight).getX() - PADDING - WIDE_PADDING*2;
		int textPanelLabelWidth = textPanelWidth - PADDING*2;
		itemDescription[0] = new Label(container, textPanelLabelWidth, fieldFont, Color.white);
		itemDescription[0].setAlignment(Label.Alignment.Center);
		itemDescription[1] = new Label(container, textPanelLabelWidth, 135, fieldFont, Color.white, tempDescription);
		itemDescription[1].setAlignment(Label.Alignment.Center);
		itemDescription[1].setVerticalAlignment(Label.VerticalAlignment.Top);
		itemDescription[2] = new Label(container, textPanelLabelWidth, fieldFont, Color.white);
		itemDescription[3] = new Label(container, textPanelLabelWidth, fieldFont, Color.white);
		itemDescription[4] = new Label(container, textPanelLabelWidth, fieldFont, Color.white);
		itemDescription[5] = new Label(container, textPanelLabelWidth, fieldFont, Color.white);
		itemDescription[6] = new Label(container, textPanelLabelWidth, fieldFont, Color.white);
		updateLabels(0);
		textPanel = new Panel(container, textPanelWidth, storeInventoryButtons.getHeight(), TEXT_PANEL_COLOR);
		
		//Create clear & buy button
		clearButton = new Button(container, BUTTON_WIDTH,BUTTON_HEIGHT,tempLabel);
		clearButton.addListener(new ButtonListener());
		clearButton.setDisabled(true);
		tempLabel = new Label(container, fieldFont, Color.white, "Buy");
		buyButton = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, tempLabel);
		buyButton.addListener(new ButtonListener());
		buyButton.setDisabled(true);
	}
	
	private void createModals() {
		int itemCount = storeInventory[currentItem].getMax() - storeInventory[currentItem].getCount();
		ArrayList<Item> buyList = inv.removeItem(currentItem, itemCount);
		ArrayList<Inventoried> buyers = p.canGetItem(buyList);
		String[] names = new String[buyers.size()];
		for (int i = 0; i < names.length; i++) {
			names[i] = buyers.get(i).getName();
		}
		SegmentedControl choosePlayer = new SegmentedControl(container, 400, 200, 3, 2, 20, true, 1, names);
		buyModal = new Modal(container, this, "Choose who will buy this item", choosePlayer, "Buy", "Cancel");
	}
	
	/**
	 * Update all the side labels on the store to reflect the current
	 * active item.
	 * 
	 * @param index The index of the item you wish to display information on
	 */
	private void updateLabels(int index) {
		Item tempItem = tempInv.get(index).peek();
		int count = storeInventory[index].getMax() - storeInventory[index].getCount();
		itemDescription[0].setText(tempItem.getName());
		itemDescription[1].setText(tempItem.getDescription());
		itemDescription[2].setText("Weight: " + tempItem.getWeight() + " lbs");
		itemDescription[3].setText("Cost: $" + tempItem.getCost());
		itemDescription[4].setText("Quantity: " + count);
		itemDescription[5].setText("Total Weight: " + count*tempItem.getWeight());
		itemDescription[6].setText("Total Cost: $" + count*tempItem.getCost());
	}
	
	/**
	 * Check to see if a component contains a certain x and y coordinate
	 * in its bounds.  Used for mouse-over effect.
	 * 
	 * @param c The component to check 
	 * @param x The x coordinate to check
	 * @param y The y coordinate to check
	 * @return
	 */
	private boolean contains(Component c,  int x, int y) {
		int leftx = c.getX();
		int rightx = c.getWidth()+leftx;
		int topy = c.getY();
		int bottomy = c.getHeight() + topy;
		if ( x >= leftx && x <= rightx && y >= topy && y <= bottomy)
			return true;
		return false;
	}
	
	public void makePurchase() {
		int itemCount = storeInventory[currentItem].getMax() - storeInventory[currentItem].getCount();
		ArrayList<Item> buyList = inv.removeItem(currentItem, itemCount);
		ArrayList<Inventoried> buyers = p.canGetItem(buyList);
		if ( buyers != null ) {
				p.buyItemForInventory(buyList, buyers.get(0));
				storeInventory[currentItem].setMax(tempInv.get(currentItem).size());
		}
		else {
			inv.addItem(buyList);
		}
	}
	
	/**
	 * Check for presses from the cancel/inventory/clear buttons.
	 */
	private class ButtonListener implements ComponentListener {
		public void componentActivated(AbstractComponent source) {
			if ( source == cancelButton ) {
				GameDirector.sharedSceneDelegate().sceneDidEnd(StoreScene.this);
			} else if ( source == inventoryButton ) {
				GameDirector.sharedSceneDelegate().requestScene(SceneID.PartyInventory, StoreScene.this);
			}
			else if ( source == buyButton) {
				createModals();
				showModal(buyModal);
				//makePurchase();
			} else {
				if ( currentItem != -1) {
					storeInventory[currentItem].setCount(storeInventory[currentItem].getMax());
					updateLabels(currentItem);
				}
			}
		}
	}
	
	/**
	 *	Check for presses on the store's inventory item buttons
	 */
	private class InventoryListener implements ComponentListener {
		
		private int ordinal;
		
		public InventoryListener(int ordinal) {
			this.ordinal = ordinal;
		}
		
		public void componentActivated(AbstractComponent source) {
			if ( currentItem == -1 || storeInventory[currentItem].getCount() == 0)
				currentItem = ordinal;
		}
	}
}
