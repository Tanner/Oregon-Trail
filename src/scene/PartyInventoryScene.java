package scene;

import java.util.ArrayList;
import java.util.List;

import model.Item;
import model.Party;
import model.Person;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.StateBasedGame;

import component.Button;
import component.Component;
import component.ConditionBar;
import component.Label;
import component.Panel;
import component.Positionable;
import component.Positionable.ReferencePoint;

import core.FontManager;
import core.GameDirector;

public class PartyInventoryScene extends Scene {
	public static final SceneID ID = SceneID.PartyInventory;
	private static final int PADDING = 20;
	
	private static final int ITEM_BUTTON_WIDTH = 80;
	private static final int ITEM_BUTTON_HEIGHT = 40;
	private static final int ITEM_CONDITION_BAR_HEIGHT = 5;
	private static final int CONDITION_BAR_PADDING = 4;
	
	private Party party;
	
	public PartyInventoryScene(Party party) {
		this.party = party;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);

		Font fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);
		
		ArrayList<Person> members = party.getPartyMembers();
		
		ArrayList<Panel> personPanels = new ArrayList<Panel>();
		for (Person person : members) {
			List<Item> inventory = person.getInventoryAsList();
			
			int width = ((ITEM_BUTTON_WIDTH + PADDING) * inventory.size()) + fieldFont.getWidth(person.getName());
			Panel panel = new Panel(container, width, ITEM_BUTTON_HEIGHT + ITEM_CONDITION_BAR_HEIGHT);
			
			Label nameLabel = new Label(container, fieldFont, Color.white, person.getName());
			panel.add(nameLabel, panel.getPosition(ReferencePoint.CenterLeft), ReferencePoint.CenterLeft, 0, 0);
			
			Positionable lastPositionReference = nameLabel;
			for (Item item : inventory) {
				Button button = new Button(container, ITEM_BUTTON_WIDTH, ITEM_BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, item.getName()));
				ConditionBar conditionBar = new ConditionBar(container, ITEM_BUTTON_WIDTH, ITEM_CONDITION_BAR_HEIGHT, item);
				
				panel.add(button, lastPositionReference.getPosition(ReferencePoint.CenterRight), ReferencePoint.CenterLeft, PADDING, 0);
				panel.add(conditionBar, button.getPosition(ReferencePoint.BottomCenter), ReferencePoint.TopCenter, 0, CONDITION_BAR_PADDING);
				
				lastPositionReference = button;
			}
			
			personPanels.add(panel);
		}
		
		Component[] personPanelsArray = new Component[personPanels.size()];
		for (Panel panel : personPanels) {
			personPanelsArray[personPanels.indexOf(panel)] = panel;
		}
		
		mainLayer.addAsGrid(personPanelsArray, mainLayer.getPosition(ReferencePoint.TopLeft), (int)(members.size() / 2), 2, PADDING, PADDING, PADDING, PADDING);
		
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
}