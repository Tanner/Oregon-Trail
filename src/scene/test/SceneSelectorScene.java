package scene.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Party;
import model.Party.Pace;
import model.Person;
import model.Player;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import component.Panel;
import component.Button;
import component.Component;
import component.Label;
import core.FontManager;
import core.GameDirector;
import core.Logger;

import scene.Scene;
import scene.SceneID;
import scene.layout.GridLayout;

/**
 * Be able to switch to any scene from this scene.
 * 
 * @author Tanner Smith
 */
public class SceneSelectorScene extends Scene {
	public static final SceneID ID = SceneID.SceneSelector;

	private ButtonListener buttonListener;
	private List<Button> buttons;
	
	private Player player;
	
	public SceneSelectorScene(Player player) {
		this.player = player;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		mainLayer.setLayout(new GridLayout(container, 4, 4));
		
		buttons = new ArrayList<Button>();
		
		UnicodeFont fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);
		
		Label mainMenuLabel = new Label(container, fieldFont, Color.white, "Main Menu");
		Button mainMenuButton = new Button(container, mainMenuLabel);
		buttons.add(mainMenuButton);
		
		Label partyCreationLabel = new Label(container, fieldFont, Color.white, "Party Creation");
		Button partyCreationButton = new Button(container, partyCreationLabel);
		buttons.add(partyCreationButton);
		
		Label townLabel = new Label(container, fieldFont, Color.white, "Town");
		Button townButton = new Button(container, townLabel);
		buttons.add(townButton);
		
		Label storeLabel = new Label(container, fieldFont, Color.white, "Store");
		Button storeButton = new Button(container, storeLabel);
		buttons.add(storeButton);
		
		Label partyInventorySceneLabel = new Label(container, fieldFont, Color.white, "Party Inventory Scene");
		Button partyInventorySceneButton = new Button(container, partyInventorySceneLabel);
		buttons.add(partyInventorySceneButton);
		
		Label componentsLabel = new Label(container, fieldFont, Color.white, "Components");
		Button componentsButton = new Button(container, componentsLabel);
		buttons.add(componentsButton);
		
		Label partyRemoveLabel = new Label(container, fieldFont, Color.white, "Remove party");
		Button partyRemoveButton = new Button(container, partyRemoveLabel);
		buttons.add(partyRemoveButton);
		
		Label partyAddLabel = new Label(container, fieldFont, Color.white, "Add Party");
		Button partyAddButton = new Button(container, partyAddLabel);
		buttons.add(partyAddButton);
		
		buttonListener = new ButtonListener();
		
		for (Button b : buttons) {
			mainLayer.add(b);
		}
		
		backgroundLayer.add(new Panel(container, Color.black));
		
		start();
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		return;
	}

	@Override
	public void start() {
		super.start();
		
		for (Button b : buttons) {
			b.addListener(buttonListener);
		}
	}

	@Override
	public void pause() {
		super.pause();
		
		for (Button b : buttons) {
			b.removeListener(buttonListener);
		}
	}

	@Override
	public void stop() {
		super.stop();
		
		for (Button b : buttons) {
			b.removeListener(buttonListener);
		}
		
		mainLayer.setAcceptingInput(false);
	}
	
	private class ButtonListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent component) {
			if (component == buttons.get(0)) {
				GameDirector.sharedSceneDelegate().requestScene(SceneID.MainMenu, SceneSelectorScene.this);
			} else if (component == buttons.get(1)) {
				GameDirector.sharedSceneDelegate().requestScene(SceneID.PartyCreation, SceneSelectorScene.this);
			} else if (component == buttons.get(2)) {
				GameDirector.sharedSceneDelegate().requestScene(SceneID.Town, SceneSelectorScene.this);
			} else if (component == buttons.get(3)) {
				GameDirector.sharedSceneDelegate().requestScene(SceneID.Store, SceneSelectorScene.this);
			} else if (component == buttons.get(4)) {
				GameDirector.sharedSceneDelegate().requestScene(SceneID.PartyInventoryScene, SceneSelectorScene.this);
			} else if (component == buttons.get(5)) {
				GameDirector.sharedSceneDelegate().requestScene(SceneID.ComponentTest, SceneSelectorScene.this);
			} else if (component == buttons.get(6)) {
				player.setParty(null);
			} else if (component == buttons.get(7)) {
				player.setParty(makeRandomParty());
			}
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
		
		for(Person person : people) {
			ArrayList<Person.Skill> personSkill = new ArrayList<Person.Skill>();
			person.setProfession(Person.Profession.values()[random.nextInt(Person.Profession.values().length)]);
	
			int skillPoints = 0;
			for (Person.Skill tempSkill = Person.Skill.values()[random.nextInt(Person.Skill.values().length)];
			tempSkill != Person.Skill.NONE && personSkill.size() < 3 && (skillPoints + tempSkill.getCost()) < 120; 
			tempSkill = Person.Skill.values()[random.nextInt(Person.Skill.values().length)]) {
				if(!personSkill.contains(tempSkill)){
					personSkill.add(tempSkill);
					skillPoints += tempSkill.getCost();
				}
			}
			for(Person.Skill skill : personSkill) {
				person.addSkill(skill);
			}
		}
		Party.Pace pace = Party.Pace.values()[random.nextInt(Party.Pace.values().length)];
		Party.Rations rations = Party.Rations.values()[random.nextInt(Party.Rations.values().length)];
		
		Party party = new Party(pace, rations, people);
		
		return party;
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
}
