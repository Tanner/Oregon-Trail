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
import component.OwnerInventoryButtons;
import component.Panel;
import component.Positionable;
import component.Positionable.ReferencePoint;

import core.ConstantStore;
import core.FontManager;
import core.GameDirector;

public class PartyInventoryScene extends Scene {
	public static final SceneID ID = SceneID.PartyInventory;
	
	public static enum EXTRA_BUTTON_FUNC { NONE, SELL, DROP};
	
	private static final int PADDING = 20;
	
	private static final int NUM_COLS = 2;
	private static final int ITEM_BUTTON_WIDTH = 80;
	private static final int ITEM_BUTTON_HEIGHT = 40;
	private static final int ITEM_CONDITION_BAR_HEIGHT = 5;
	private static final int CONDITION_BAR_PADDING = 4;
	private static final int NAME_PADDING = 10;
	
	private Party party;
	
	private OwnerInventoryButtons playerInventoryButtons[];
	private OwnerInventoryButtons vehicleInventoryButtons;
	
	private Button closeButton, transferButton, functionButton;
	private EXTRA_BUTTON_FUNC extraButtonFunctionality;
	
	public PartyInventoryScene(Party party, EXTRA_BUTTON_FUNC extraButtonFunctionality) {
		this.party = party;
		
		this.extraButtonFunctionality = extraButtonFunctionality;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);

		Font fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);
		
		ArrayList<Person> members = party.getPartyMembers();
		Vehicle vehicle = party.getVehicle();
		
		// Create a grid of all the party members and their inventories
		int maxInventorySize = Person.MAX_INVENTORY_SIZE;
				
		Panel personPanels[] = new Panel[party.getPartyMembers().size()];
		playerInventoryButtons = new OwnerInventoryButtons[personPanels.length];
		
		for (int i = 0; i < personPanels.length; i++) {
			Person person = members.get(i);
			
			playerInventoryButtons[i] = new OwnerInventoryButtons(person);
			personPanels[i] = playerInventoryButtons[i].getPanel(container);
		}
		
		int panelWidth = ((ITEM_BUTTON_WIDTH + PADDING) * maxInventorySize) - PADDING;
		int peopleSpacing = ((container.getWidth() - (2 * PADDING)) - (panelWidth * NUM_COLS)) / (NUM_COLS - 1) - 4;
		
		mainLayer.addAsGrid(personPanels, mainLayer.getPosition(ReferencePoint.TopLeft), (int)(members.size() / 2), NUM_COLS, PADDING, PADDING, peopleSpacing, PADDING);
		
		// Create Vehicle inventories (if one exists)
		int lastOddIndex = personPanels.length - 1;
		if (lastOddIndex % 2 != 0) {
			lastOddIndex--;
		}
		Positionable locationReference = personPanels[lastOddIndex];

		vehicleInventoryButtons = new OwnerInventoryButtons(vehicle);
		mainLayer.add(vehicleInventoryButtons.getPanel(container), locationReference.getPosition(ReferencePoint.BottomLeft), ReferencePoint.TopLeft, 0, PADDING);
		
		// Close button
		closeButton = new Button(container, 200, 40, new Label(container, fieldFont, Color.white, ConstantStore.get("GENERAL", "CLOSE")));
		closeButton.addListener(new ButtonListener());
		mainLayer.add(closeButton, mainLayer.getPosition(ReferencePoint.BottomLeft), ReferencePoint.BottomLeft, PADDING, -PADDING);
		
		// Transfer button
		transferButton = new Button(container, 200, 40, new Label(container, fieldFont, Color.white, ConstantStore.get("PARTY_INVENTORY_SCENE", "TRANSFER")));
		transferButton.addListener(new ButtonListener());
		mainLayer.add(transferButton, mainLayer.getPosition(ReferencePoint.BottomRight), ReferencePoint.BottomRight, -PADDING, -PADDING);
		
		// Function button
		if (extraButtonFunctionality != EXTRA_BUTTON_FUNC.NONE) {
			String functionText = ConstantStore.get("PARTY_INVENTORY_SCENE", "DROP");
			
			if (extraButtonFunctionality == EXTRA_BUTTON_FUNC.SELL) {
				functionText = ConstantStore.get("PARTY_INVENTORY_SCENE", "SELL");
			}
			
			functionButton = new Button(container, 200, 40, new Label(container, fieldFont, Color.white, functionText));
			functionButton.addListener(new ButtonListener());
			mainLayer.add(functionButton, transferButton.getPosition(ReferencePoint.BottomLeft), ReferencePoint.BottomRight, -PADDING, 0);
		}
		
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
	
	private class ButtonListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent component) {
			if (component == closeButton) {
				GameDirector.sharedSceneDelegate().sceneDidEnd(PartyInventoryScene.this);
			}
		}
	}
}