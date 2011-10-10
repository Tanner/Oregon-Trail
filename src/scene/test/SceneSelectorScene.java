package scene.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.*;
import model.item.SonicScrewdriver;
import model.item.Wheel;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import component.Panel;
import component.Button;
import component.Label;
import component.Positionable;
import core.FontManager;
import core.GameDirector;

import scene.Scene;
import scene.SceneID;

/**
 * Scene to start another scene.
 */
public class SceneSelectorScene extends Scene {
	public static final SceneID ID = SceneID.SceneSelector;
	
	private static final int MARGIN = 20;

	private ButtonListener buttonListener;
	private List<Button> buttons;
	
	private Player player;
	
	public SceneSelectorScene(Player player) {
		this.player = player;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
				
		buttons = new ArrayList<Button>();
		
		UnicodeFont fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);
		String[] labels = { "Main Menu", "Party Creation", "Town", "Store", "Party Inventory", "Components", "Remove Party", "Add Party" };
		
		int rows = (int)Math.ceil(Math.sqrt(labels.length));
		int cols = (int)Math.ceil(Math.sqrt(labels.length));
		int rowHeight = (container.getHeight() - (rows + 1) * MARGIN) / rows;
		int colWidth = (container.getWidth() - (cols + 1) * MARGIN) / cols;
		for (String s : labels) {
			buttons.add(new Button(container, colWidth, rowHeight, new Label(container, fieldFont, Color.white, s)));
		}
		
		buttonListener = new ButtonListener();
		
		Button[] buttonsToAdd = new Button[buttons.size()];
		for (Button b : buttons) {
			buttonsToAdd[buttons.indexOf(b)] = b;
		}
		mainLayer.addAsGrid(buttonsToAdd,
				mainLayer.getPosition(Positionable.ReferencePoint.TopLeft),
				rows,
				cols,
				MARGIN,
				MARGIN,
				MARGIN,
				MARGIN);
		
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
				GameDirector.sharedSceneDelegate().requestScene(SceneID.PartyInventory, SceneSelectorScene.this);
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
			Wheel wheel = new Wheel(1);
			wheel.decreaseStatus(random.nextInt(100));
			SonicScrewdriver sonic = new SonicScrewdriver(10 - people.indexOf(person));
			person.addToInventory(wheel);
			person.addToInventory(sonic);
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
