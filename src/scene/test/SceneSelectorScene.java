package scene.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.*;
import model.Item.ITEM_TYPE;
import model.item.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import component.Modal;
import component.Panel;
import component.Button;
import component.Label;
import component.Positionable;
import core.ConstantStore;
import core.FontManager;
import core.GameDirector;

import scene.Scene;
import scene.SceneID;

/**
 * Scene to start another scene.
 */
public class SceneSelectorScene extends Scene {
	public static final SceneID ID = SceneID.SCENESELECTOR;
	
	private static final int MARGIN = 20;

	private List<Button> buttons;
	
	private Player player;
	
	/**
	 * Creates the scene which selects the scene by which the player can select the scene they wish this scene to select 
	 */
	public SceneSelectorScene(Player player) {
		this.player = player;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		Font fieldFont = GameDirector.sharedSceneListener().getFontManager().getFont(FontManager.FontID.FIELD);
		
		SceneID scenes[] = SceneID.values();
		
		int size = (int)Math.ceil(Math.sqrt(scenes.length));
		int height = (container.getHeight() - (size + 1) * MARGIN) / size;
		int width = (container.getWidth() - (size + 1) * MARGIN) / size;
		
		// Create all the buttons for the scenes
		buttons = new ArrayList<Button>();
		for (SceneID scene : scenes) {
			buttons.add(new Button(container, width, height, new Label(container, fieldFont, Color.white, scene.getName())));
		}
		
		// Create extra function button
		buttons.add(new Button(container, width, height, new Label(container, fieldFont, Color.white, ConstantStore.get("SCENE_SELECTOR_SCENE", "ADD_PARTY"))));
		buttons.add(new Button(container, width, height, new Label(container, fieldFont, Color.white, ConstantStore.get("SCENE_SELECTOR_SCENE", "REMOVE_PARTY"))));
		
		// Add listeners to buttons and make an array
		ButtonListener buttonListener = new ButtonListener();
		
		Button[] buttonsToAdd = new Button[buttons.size()];
		for (int i = 0; i < buttons.size(); i++) {
			buttonsToAdd[i] = buttons.get(i);
			buttonsToAdd[i].addListener(buttonListener);
		}
		
		// Add stuff
		mainLayer.addAsGrid(buttonsToAdd, mainLayer.getPosition(Positionable.ReferencePoint.TOPLEFT), size, size, MARGIN, MARGIN, MARGIN, MARGIN);
		
		backgroundLayer.add(new Panel(container, Color.black));
		
		start();
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		return;
	}
	
	/**
	 * They are buttons with something to say. We listen to them.
	 */
	private class ButtonListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent component) {
			String buttonText = ((Button) component).getText();
			
			if (buttonText.equals(SceneID.MAINMENU.getName())) {
				// Main Menu
				GameDirector.sharedSceneListener().requestScene(SceneID.MAINMENU, SceneSelectorScene.this);
			} else if (buttonText.equals(SceneID.PARTYCREATION.getName())) {
				// Party Creation
				GameDirector.sharedSceneListener().requestScene(SceneID.PARTYCREATION, SceneSelectorScene.this);
			} else if (buttonText.equals(SceneID.TOWN.getName())) {
				// Town
				if (player.getParty() == null) {
					warnBecauseNoParty();
					return;
				}
				
				GameDirector.sharedSceneListener().requestScene(SceneID.TOWN, SceneSelectorScene.this);
			} else if (buttonText.equals(SceneID.STORE.getName())) {
				// Store
				if (player.getParty() == null) {
					warnBecauseNoParty();
					return;
				}
				
				GameDirector.sharedSceneListener().requestScene(SceneID.STORE, SceneSelectorScene.this);
			} else if (buttonText.equals(SceneID.PARTYINVENTORY.getName())) {
				// Party Inventory
				if (player.getParty() == null) {
					warnBecauseNoParty();
					return;
				}
				
				GameDirector.sharedSceneListener().requestScene(SceneID.PARTYINVENTORY, SceneSelectorScene.this);
			} else if (buttonText.equals(SceneID.HUNT.getName())) {
				// Hunt
				if (player.getParty() == null) {
					warnBecauseNoParty();
					return;		
				}
				
				GameDirector.sharedSceneListener().requestScene(SceneID.HUNT, SceneSelectorScene.this);
			} else if (buttonText.equals(SceneID.COMPONENTTEST.getName())) {
				// Component Test
				GameDirector.sharedSceneListener().requestScene(SceneID.COMPONENTTEST, SceneSelectorScene.this);
			} else if (buttonText.equals(SceneID.TRAILTEST.getName())) {
				// Trail Test
				if (player.getParty() == null) {
					warnBecauseNoParty();
					return;		
				}
				
				GameDirector.sharedSceneListener().requestScene(SceneID.TRAILTEST, SceneSelectorScene.this);	
			} else if (buttonText.equals(SceneID.TRAIL.getName())) {
				// Trail Test
				if (player.getParty() == null) {
					warnBecauseNoParty();
					return;		
				}
				
				GameDirector.sharedSceneListener().requestScene(SceneID.TRAIL, SceneSelectorScene.this);
			} else if (buttonText.equals(SceneID.GAMEOVER.getName())) {
				// Trail Test
				if (player.getParty() == null) {
					warnBecauseNoParty();
					return;		
				}
				
				GameDirector.sharedSceneListener().requestScene(SceneID.GAMEOVER, SceneSelectorScene.this);
			}
			
			else if (buttonText.equals(ConstantStore.get("SCENE_SELECTOR_SCENE", "REMOVE_PARTY"))) {
				player.setParty(null);
			} else if (buttonText.equals(ConstantStore.get("SCENE_SELECTOR_SCENE", "ADD_PARTY"))) {
				player.setParty(makeRandomParty());
			}
		}
		
		/**
		 * Warn the user that they need a party to continnue.
		 */
		private void warnBecauseNoParty() {
			showModal((new Modal(container, SceneSelectorScene.this, ConstantStore.get("SCENE_SELECTOR_SCENE", "ERR_NO_PARTY_FOR_SCENE"), ConstantStore.get("GENERAL", "OK"))));
		}
	}
	
	/**
	 * Makes a random party for the set party button.
	 * @return The random party
	 */
	public Party makeRandomParty() {
		
		Random random = new Random();
		ArrayList<Person> people = new ArrayList<Person>();
		
		people.add(new Person("Alice"));
		people.add(new Person("Bob"));
		people.add(new Person("Carl"));
		people.add(new Person("Diane"));
		
		for (Person person : people) {
			ArrayList<Person.Skill> personSkill = new ArrayList<Person.Skill>();
			person.setProfession(Person.Profession.values()[random.nextInt(Person.Profession.values().length)]);
	
			int skillPoints = 0;
			
			// Randomly assign some skills
			Person.Skill tempSkill = Person.Skill.values()[random.nextInt(Person.Skill.values().length)];
			while (tempSkill != Person.Skill.NONE && personSkill.size() < 3 && (skillPoints + tempSkill.getCost()) < 120) {
				if (!personSkill.contains(tempSkill)) {
					personSkill.add(tempSkill);
					skillPoints += tempSkill.getCost();
				}
				
				tempSkill = Person.Skill.values()[random.nextInt(Person.Skill.values().length)];
			}
			
			for (Person.Skill skill : personSkill) {
				person.addSkill(skill);
			}
			
			addRandomItems(person);
			
			// Randomly hurt party members
			person.decreaseHealth(random.nextInt(100));
		}
		
		Vehicle vehicle = new Wagon();
		addRandomItems(vehicle);
		
		Party.Pace pace = Party.Pace.values()[random.nextInt(Party.Pace.values().length)];
		Party.Rations rations = Party.Rations.values()[random.nextInt(Party.Rations.values().length)];

		Party party = new Party(pace, rations, people);
		party.setVehicle(vehicle);
		
		return party;
	}
	
	public void addRandomItems(Inventoried inventoried) {
		Random random = new Random();
		
		int numberOfItemsToAdd = random.nextInt(inventoried.getMaxSize()) + 2;
		
		for (int i = 0; i < numberOfItemsToAdd; i++) {
			Item item;
			int attempts = 0;
			do {
				int randomItem = random.nextInt(ITEM_TYPE.values().length);
				item = new Item(ITEM_TYPE.values()[randomItem]);
				
				attempts++;
			} while(!inventoried.canGetItem(item.getType(), 1) && attempts < ITEM_TYPE.values().length);
			
			item.decreaseStatus(random.nextInt(101));
			
			inventoried.addItemToInventory(item);
		}
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
}
