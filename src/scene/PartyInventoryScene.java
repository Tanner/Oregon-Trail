package scene;

import java.util.ArrayList;
import java.util.List;

import model.Inventory;
import model.Item;
import model.Party;
import model.Person;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
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
	
	private Party party;
	
	private Button closeButton;
	
	public PartyInventoryScene(Party party) {
		this.party = party;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);

		Font fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);
		
		ArrayList<Person> members = party.getPartyMembers();
		
		int nameLabelWidth = 0;
		int nameWidth = 0;
		int maxInventorySize = 0;
		for (Person person : members) {
			Inventory inventory = person.getInventory();
			
			if (maxInventorySize < inventory.getMaxSize()) {
				maxInventorySize = inventory.getMaxSize();
			}
			
			int newWidth = fieldFont.getWidth(person.getName());
			if (nameLabelWidth < newWidth) {
				nameLabelWidth = newWidth;
			}
			
			int newNameWidth = fieldFont.getWidth(person.getName());
			if (nameWidth < newNameWidth) {
				nameWidth = newNameWidth;
			}
		}
		
		int panelWidth = ((ITEM_BUTTON_WIDTH + PADDING) * maxInventorySize) + nameWidth;
		
		ArrayList<Panel> personPanels = new ArrayList<Panel>();
		for (Person person : members) {
			List<Item> inventory = person.getInventoryAsList();
			
			int panelHeight = ITEM_BUTTON_HEIGHT + CONDITION_BAR_PADDING + ITEM_CONDITION_BAR_HEIGHT;
			Panel panel = new Panel(container, panelWidth, panelHeight);
			
			Label nameLabel = new Label(container, nameLabelWidth, panelHeight, fieldFont, Color.white, person.getName());
			panel.add(nameLabel, panel.getPosition(ReferencePoint.TopLeft), ReferencePoint.TopLeft, 0, 0);
			
			Positionable lastPositionReference = nameLabel;
			for (int i = 0; i < inventory.size(); i++) {
				Item item = inventory.get(i);
				
				lastPositionReference = createItemButtons(item, lastPositionReference, fieldFont, panel);
			}
			
			personPanels.add(panel);
		}
		
		Component[] personPanelsArray = new Component[personPanels.size()];
		for (Panel panel : personPanels) {
			personPanelsArray[personPanels.indexOf(panel)] = panel;
		}
		
		int peopleSpacing = ((container.getWidth() - (2 * PADDING)) - (panelWidth * NUM_COLS)) / (NUM_COLS - 1);
		mainLayer.addAsGrid(personPanelsArray, mainLayer.getPosition(ReferencePoint.TopLeft), (int)(members.size() / 2), NUM_COLS, PADDING, PADDING, peopleSpacing, PADDING);
		
		closeButton = new Button(container, 200, 40, new Label(container, fieldFont, Color.white, ConstantStore.get("GENERAL", "CLOSE")));
		closeButton.addListener(new ButtonListener());
		mainLayer.add(closeButton, mainLayer.getPosition(ReferencePoint.BottomLeft), ReferencePoint.BottomLeft, PADDING, -PADDING);
		
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
	
	public Button createItemButtons(Item item, Positionable locationReference, Font font, Panel panel) {	
		CountingButton button = new CountingButton(container, ITEM_BUTTON_WIDTH, ITEM_BUTTON_HEIGHT, new Label(container, font, Color.white, item.getName()));
		button.setCountUpOnLeftClick(false);
		button.setMax(item.getNumberOf());
		button.setCount(item.getNumberOf());
		
		ConditionBar conditionBar = new ConditionBar(container, ITEM_BUTTON_WIDTH, ITEM_CONDITION_BAR_HEIGHT, item.getStatus());
		
		panel.add(button, locationReference.getPosition(ReferencePoint.TopRight), ReferencePoint.TopLeft, PADDING, 0);
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