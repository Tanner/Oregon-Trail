package scene.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.*;
import model.Party.Pace;
import model.Party.Rations;
import model.item.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import component.Panel;
import component.Button;
import component.Label;
import component.Positionable;
import component.SegmentedControl;
import component.modal.ComponentModal;
import component.modal.MessageModal;
import component.modal.Modal;
import core.ConstantStore;
import core.FontStore;
import core.GameDirector;
import core.SoundStore;

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
	private Game game;
	
	private ComponentModal<SegmentedControl> fileLoadModal, fileSaveModal;
	
	/**
	 * Creates the scene which selects the scene by which the player can select the scene they wish this scene to select 
	 */
	public SceneSelectorScene(Game game) {
		this.player = game.getPlayer();
		this.game = game;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);

		SoundStore.get().stopMusic();
		
		Font fieldFont = FontStore.get().getFont(FontStore.FontID.FIELD);
		
		SceneID scenes[] = SceneID.values();
		
		setUpSaveFiles();
				
		int size = (int) Math.ceil(Math.sqrt(scenes.length + 2)); // 2 for reset party and game
		int height = (container.getHeight() - (size + 1) * MARGIN) / size;
		int width = (container.getWidth() - (size + 1) * MARGIN) / size;
		
		// Create all the buttons for the scenes
		buttons = new ArrayList<Button>();
		for (SceneID scene : scenes) {
			buttons.add(new Button(container, width, height, new Label(container, width - MARGIN, height - MARGIN, fieldFont, Color.white, scene.getName())));
		}
		
		// Create extra function button
		buttons.add(new Button(container, width, height, new Label(container, width - MARGIN, height - MARGIN, fieldFont, Color.white, ConstantStore.get("SCENE_SELECTOR_SCENE", "RESET_GAME"))));
		buttons.add(new Button(container, width, height, new Label(container, width - MARGIN, height - MARGIN, fieldFont, Color.white, ConstantStore.get("SCENE_SELECTOR_SCENE", "RESET_PARTY"))));
		buttons.add(new Button(container, width, height, new Label(container, width - MARGIN, height - MARGIN, fieldFont, Color.white, ConstantStore.get("SCENE_SELECTOR_SCENE", "SAVE_GAME"))));
		buttons.add(new Button(container, width, height, new Label(container, width - MARGIN, height - MARGIN, fieldFont, Color.white, ConstantStore.get("SCENE_SELECTOR_SCENE", "LOAD_GAME"))));
		
		
		
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
	}
	
	private void setUpSaveFiles() {

		List<String> fileList = new ArrayList<String>();
		File file  = new File("resources/serialized/");
		if(file.exists()) {
			for(String fileName : file.list()) {
				fileList.add(fileName);
			}
		}
		
		String[] saveFileList = new String[5];
		int numEmpty = 0;
		for(int i = 1; i <= 5; i++) {
			if(fileList.contains("Game " + i + ".ser")) {
				saveFileList[i-1] = ("Game " + i);
			} else {
				saveFileList[i-1] = ("Empty");
				numEmpty++;
			}
		}
		int[] emptyFiles = new int[numEmpty];
		int j = 0;
		for(int i = 0; i < 5; i++) {
			if(saveFileList[i] == "Empty") {
				emptyFiles[j] = i;
				j++;
			}
		}
		
		SegmentedControl loadableFiles = new SegmentedControl(container, 500, 200, 5, 1, 0, true, 1, saveFileList);
		loadableFiles.setDisabled(emptyFiles);
		
		fileLoadModal = new ComponentModal<SegmentedControl>(container, SceneSelectorScene.this, "Choose a file to load", 2, loadableFiles);
		fileLoadModal.setButtonText(fileLoadModal.getCancelButtonIndex(), ConstantStore.get("GENERAL", "CANCEL"));
		
		fileSaveModal = new ComponentModal<SegmentedControl>(container, SceneSelectorScene.this, "Choose a slot to save", 2, new SegmentedControl(container, 500, 200, 5, 1, 0, true, 1, saveFileList));
		fileSaveModal.setButtonText(fileSaveModal.getCancelButtonIndex(), ConstantStore.get("GENERAL", "CANCEL"));
		
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
				GameDirector.sharedSceneListener().requestScene(SceneID.MAINMENU, SceneSelectorScene.this, false);
			} else if (buttonText.equals(SceneID.LOADING.getName())) {
				// Loading
				GameDirector.sharedSceneListener().requestScene(SceneID.LOADING, SceneSelectorScene.this, false);
			} else if (buttonText.equals(SceneID.SPLASH.getName())) {
				// Splash
				GameDirector.sharedSceneListener().requestScene(SceneID.SPLASH, SceneSelectorScene.this, false);
			} else if (buttonText.equals(SceneID.PARTYCREATION.getName())) {
				// Party Creation
				GameDirector.sharedSceneListener().requestScene(SceneID.PARTYCREATION, SceneSelectorScene.this, false);
			} else if (buttonText.equals(SceneID.TOWN.getName())) {
				// Town
				if (player.getParty() == null) {
					warnBecauseNoParty();
					return;
				}
				
				GameDirector.sharedSceneListener().requestScene(SceneID.TOWN, SceneSelectorScene.this, false);
			} else if (buttonText.equals(SceneID.STORE.getName())) {
				// Store
				if (player.getParty() == null) {
					warnBecauseNoParty();
					return;
				}
				
				GameDirector.sharedSceneListener().requestScene(SceneID.STORE, SceneSelectorScene.this, false);
			} else if (buttonText.equals(SceneID.PARTYINVENTORY.getName())) {
				// Party Inventory
				if (player.getParty() == null) {
					warnBecauseNoParty();
					return;
				}
				
				GameDirector.sharedSceneListener().requestScene(SceneID.PARTYINVENTORY, SceneSelectorScene.this, false);
			} else if (buttonText.equals(SceneID.HUNT.getName())) {
				// Hunt
				if (player.getParty() == null) {
					warnBecauseNoParty();
					return;
				}
				
				GameDirector.sharedSceneListener().requestScene(SceneID.HUNT, SceneSelectorScene.this, false);
			} else if (buttonText.equals(SceneID.TRAIL.getName())) {
				// Trail
				if (player.getParty() == null) {
					warnBecauseNoParty();
					return;
				}
				
				GameDirector.sharedSceneListener().requestScene(SceneID.TRAIL, SceneSelectorScene.this, false);
			} else if (buttonText.equals(SceneID.GAMEOVER.getName())) {
				// Game Over
				if (player.getParty() == null) {
					warnBecauseNoParty();
					return;
				}
				
				GameDirector.sharedSceneListener().requestScene(SceneID.GAMEOVER, SceneSelectorScene.this, false);
			} else if (buttonText.equals(SceneID.TAVERN.getName())) {
				// Game Over
				if (player.getParty() == null) {
					warnBecauseNoParty();
					return;
				}
				
				GameDirector.sharedSceneListener().requestScene(SceneID.TAVERN, SceneSelectorScene.this, false);
			}  else if (buttonText.equals(SceneID.VICTORY.getName())) {
				// Victory
				if (player.getParty() == null) {
					warnBecauseNoParty();
					return;
				}
				
				GameDirector.sharedSceneListener().requestScene(SceneID.VICTORY, SceneSelectorScene.this, false);	
			} else if (buttonText.equals(SceneID.RIVER.getName())) {
				// Game Over
				if (player.getParty() == null) {
					warnBecauseNoParty();
					return;
				}
				
				GameDirector.sharedSceneListener().requestScene(SceneID.RIVER, SceneSelectorScene.this, false);
			}  else if (buttonText.equals(SceneID.MAP.getName())) {
					// Map Scene
					if (player.getParty() == null) {
						warnBecauseNoParty();
						return;
					}
					
					GameDirector.sharedSceneListener().requestScene(SceneID.MAP, SceneSelectorScene.this, false);
			} else if (buttonText.equals(SceneID.COMPONENTTEST.getName())) {
				// Component Test
				GameDirector.sharedSceneListener().requestScene(SceneID.COMPONENTTEST, SceneSelectorScene.this, false);
			} else if (buttonText.equals(SceneID.TRAILTEST.getName())) {
				// Trail Test
				if (player.getParty() == null) {
					warnBecauseNoParty();
					return;
				}
				
				GameDirector.sharedSceneListener().requestScene(SceneID.TRAILTEST, SceneSelectorScene.this, false);
			} else if (buttonText.equals(ConstantStore.get("SCENE_SELECTOR_SCENE", "RESET_GAME"))) {
				game.reset();
				player = game.getPlayer();
			} else if (buttonText.equals(ConstantStore.get("SCENE_SELECTOR_SCENE", "RESET_PARTY"))) {
				player.setParty(makeRandomParty());
			} else if (buttonText.equals(ConstantStore.get("SCENE_SELECTOR_SCENE", "SAVE_GAME"))) {
				setUpSaveFiles();
				showModal(fileSaveModal);
			} else if (buttonText.equals(ConstantStore.get("SCENE_SELECTOR_SCENE", "LOAD_GAME"))) {
				setUpSaveFiles();
				showModal(fileLoadModal);
			}
		}
		
		/**
		 * Warn the user that they need a party to continue.
		 */
		private void warnBecauseNoParty() {
			showModal((new MessageModal(container, SceneSelectorScene.this, ConstantStore.get("SCENE_SELECTOR_SCENE", "ERR_NO_PARTY_FOR_SCENE"))));
		}
	}
	
	/**
	 * Makes a random party for the set party button.
	 * @return The random party
	 */
	public Party makeRandomParty() {
		
		Random random = new Random();
		ArrayList<Person> people = new ArrayList<Person>();
		Person partyLeader;
		
		partyLeader = new Person("Albert");
		partyLeader.setIsMale(true);
		partyLeader.setLeader(true);
		people.add(partyLeader);
		Person betty = new Person("Betty");
		betty.setIsMale(false);
		people.add(betty);
		people.add(new Person("Carl"));
		Person diane = new Person("Diane");
		diane.setIsMale(false);
		people.add(diane);
				
		for (Person person : people) {
			ArrayList<Skill> personSkill = new ArrayList<Skill>();
			person.setProfession(Profession.values()[random.nextInt(Profession.values().length)]);
	
			int skillPoints = 0;
			
			// Randomly assign some skills
			Skill tempSkill = Skill.values()[random.nextInt(Skill.values().length)];
			while (tempSkill != Skill.NONE && personSkill.size() < 3 && (skillPoints + tempSkill.getCost()) < 120) {
				if (!personSkill.contains(tempSkill)) {
					personSkill.add(tempSkill);
					skillPoints += tempSkill.getCost();
				}
				
				tempSkill = Skill.values()[random.nextInt(Skill.values().length)];
			}
			
			for (Skill skill : personSkill) {
				person.addSkill(skill);
			}
			
			person.getInventory().addRandomItems();
			
		}
		
		Vehicle vehicle = new Wagon();
		for(int i = 0; i < 99; i++) {
			vehicle.addItemToInventory(new Item(ItemType.MEAT));
		}
		vehicle.getInventory().addRandomItems();
		
		List<Animal> animalList = new ArrayList<Animal>();
		for(int i = 0; i < 4; i++) {
			Animal animal = new Animal(ItemType.OX);
			animalList.add(animal);
		}
		
		
		Party.Pace pace = Pace.STEADY;
		Party.Rations rations = Rations.BAREBONES;

		Party party = new Party(pace, rations, partyLeader, people, new Time());
		party.setVehicle(vehicle);
		party.addAnimals(animalList);
		
		party.setLocation(game.getWorldMap().getMapHead());
		party.setTrail(party.getLocation().getOutBoundTrailByIndex(0));
		
		return party;
	}
	
	public void dismissModal(Modal modal, int button) {
		super.dismissModal(modal, button);
		if(modal == fileLoadModal && button != modal.getCancelButtonIndex()) {
			game = GameDirector.sharedSceneListener().deserialize("Game " + (fileLoadModal.getComponent().getSelection()[0] + 1));
			player = game.getPlayer();
		}
		if(modal == fileSaveModal && button != modal.getCancelButtonIndex()) {
			GameDirector.sharedSceneListener().serialize("Game " + (fileSaveModal.getComponent().getSelection()[0]+1));
		}
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
}
