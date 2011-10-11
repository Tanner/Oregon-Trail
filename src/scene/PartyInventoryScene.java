package scene;

import java.util.ArrayList;
import java.util.List;

import model.Inventory;
import model.Item;
import model.Party;
import model.Person;
import model.item.Vehicle;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import component.Button;
import component.Component;
import component.ConditionBar;
import component.CountingButton;
import component.Label;
import component.Panel;
import component.Positionable;
import component.Positionable.ReferencePoint;

import core.ConstantStore;
import core.FontManager;
import core.GameDirector;

public class PartyInventoryScene extends Scene {
	public static final SceneID ID = SceneID.PartyInventory;
	private static final int PADDING = 20;
	
	private static final int NUM_COLS = 2;
	private static final int ITEM_BUTTON_WIDTH = 80;
	private static final int ITEM_BUTTON_HEIGHT = 40;
	private static final int ITEM_CONDITION_BAR_HEIGHT = 5;
	private static final int CONDITION_BAR_PADDING = 4;
	private static final int NAME_PADDING = 10;
	
	private Party party;
	
	private Button closeButton, transferButton;
	
	public PartyInventoryScene(Party party) {
		this.party = party;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);

		Font fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);
		
		ArrayList<Person> members = party.getPartyMembers();
		Vehicle vehicle = party.getVehicle();

		int panelHeight = ITEM_BUTTON_HEIGHT + CONDITION_BAR_PADDING + ITEM_CONDITION_BAR_HEIGHT;
		
		// Create a grid of all the party members and their inventories
		int maxInventorySize = 0;
		for (Person person : members) {
			Inventory inventory = person.getInventory();
			
			if (maxInventorySize < inventory.getMaxSize()) {
				maxInventorySize = inventory.getMaxSize();
			}
		}
		
		int panelWidth = ((ITEM_BUTTON_WIDTH + PADDING) * maxInventorySize);
		
		ArrayList<Panel> personPanels = new ArrayList<Panel>();
		for (Person person : members) {
			List<Item> inventory = person.getInventoryAsList();
			Panel itemPanel = new Panel(container, panelWidth, panelHeight);
			
			Positionable lastPositionReference = itemPanel;
			for (int i = 0; i < inventory.size(); i++) {
				Item item = inventory.get(i);
				
				Vector2f position = lastPositionReference.getPosition(ReferencePoint.TopRight);
				int padding = PADDING;
				
				if (i == 0) {
					position = lastPositionReference.getPosition(ReferencePoint.TopLeft);
					padding = 0;
				}
				
				lastPositionReference = createItemButton(item, position, fieldFont, itemPanel, padding);
			}
			
			Label nameLabel = new Label(container, fieldFont, Color.white, person.getName());			
			Panel panel = new Panel(container, panelWidth, panelHeight + NAME_PADDING + (int)nameLabel.getFontHeight());
			
			panel.add(nameLabel, panel.getPosition(ReferencePoint.TopLeft), ReferencePoint.TopLeft, 0, 0);
			panel.add(itemPanel, nameLabel.getPosition(ReferencePoint.BottomLeft), ReferencePoint.TopLeft, 0, NAME_PADDING);
			
			personPanels.add(panel);
		}
		
		Component[] personPanelsArray = new Component[personPanels.size()];
		for (Panel panel : personPanels) {
			personPanelsArray[personPanels.indexOf(panel)] = panel;
		}
		
		int peopleSpacing = ((container.getWidth() - (2 * PADDING)) - (panelWidth * NUM_COLS)) / (NUM_COLS - 1);
		mainLayer.addAsGrid(personPanelsArray, mainLayer.getPosition(ReferencePoint.TopLeft), (int)(members.size() / 2), NUM_COLS, PADDING, PADDING, peopleSpacing, PADDING);
		
		// Create Vehicle inventories (if one exists)
		Label wagonLabel = new Label(container, fieldFont, Color.white, vehicle.getName());
		
		// Add the vehicle label to the last left handed side person label
		int lastOddIndex = personPanels.size() - 1;
		if (lastOddIndex % 2 != 0) {
			lastOddIndex--;
		}
		Positionable locationReference = personPanels.get(lastOddIndex);
		
		mainLayer.add(wagonLabel, locationReference.getPosition(ReferencePoint.BottomLeft), ReferencePoint.TopLeft, 0, PADDING);
		
		// Now make all the item condition bar buttons things
		List<Item> vehicleItems = vehicle.getInventory().getItems();
		
		ArrayList<Panel> vehicleItemPanels = new ArrayList<Panel>();
		
		for (int i = 0; i < vehicleItems.size(); i++) {
			Item item = vehicleItems.get(i);

			Panel itemPanel = new Panel(container, ITEM_BUTTON_WIDTH, panelHeight);
			
			createItemButton(item, itemPanel.getPosition(ReferencePoint.TopLeft), fieldFont, itemPanel, 0);
			
			vehicleItemPanels.add(itemPanel);
		}
		
		// Now add the grid of all the item buttons
		Component[] vehicleItemPanelsArray = new Component[vehicleItemPanels.size()];
		for (Panel panel : vehicleItemPanels) {
			vehicleItemPanelsArray[vehicleItemPanels.indexOf(panel)] = panel;
		}
		
		int numCols = (int)(((container.getWidth() - (2 * PADDING)) - wagonLabel.getFontWidth() - PADDING) / (ITEM_BUTTON_WIDTH + PADDING));
		int numRows = vehicleItems.size() / numCols;
		
		mainLayer.addAsGrid(vehicleItemPanelsArray, wagonLabel.getPosition(ReferencePoint.BottomLeft), numRows, numCols, 0, PADDING, PADDING, PADDING);
		
		// Close button
		closeButton = new Button(container, 200, 40, new Label(container, fieldFont, Color.white, ConstantStore.get("GENERAL", "CLOSE")));
		closeButton.addListener(new ButtonListener());
		mainLayer.add(closeButton, mainLayer.getPosition(ReferencePoint.BottomLeft), ReferencePoint.BottomLeft, PADDING, -PADDING);
		
		// Transfer button
		transferButton = new Button(container, 200, 40, new Label(container, fieldFont, Color.white, ConstantStore.get("PARTY_INVENTORY_SCENE", "TRANSFER")));
		transferButton.addListener(new ButtonListener());
		mainLayer.add(transferButton, mainLayer.getPosition(ReferencePoint.BottomRight), ReferencePoint.BottomRight, -PADDING, -PADDING);
		
		backgroundLayer.add(new Panel(container, new Color(0x3b2d59)));
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		return;
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
	
	public Button createItemButton(Item item, Vector2f position, Font font, Panel panel, int offset) {	
		CountingButton button = new CountingButton(container, ITEM_BUTTON_WIDTH, ITEM_BUTTON_HEIGHT, new Label(container, font, Color.white, item.getName()));
		button.setCountUpOnLeftClick(false);
		button.setMax(item.getNumberOf());
		button.setCount(item.getNumberOf());
		
		ConditionBar conditionBar = new ConditionBar(container, ITEM_BUTTON_WIDTH, ITEM_CONDITION_BAR_HEIGHT, item.getStatus());
		
		panel.add(button, position, ReferencePoint.TopLeft, offset, 0);
		panel.add(conditionBar, button.getPosition(ReferencePoint.BottomCenter), ReferencePoint.TopCenter, 0, CONDITION_BAR_PADDING);
		
		return button;
	}
	
	private class ButtonListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent component) {
			if (component == closeButton) {
				GameDirector.sharedSceneDelegate().sceneDidEnd(PartyInventoryScene.this);
			}
		}
	}
}