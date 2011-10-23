package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import scene.*;
import scene.test.*;

import model.*;
import model.item.Wagon;

/**
 * Directs the logical functionality of the game. Sets everything in motion.
 */

public class GameDirector implements SceneListener, SceneDirectorListener {
	public static boolean DEBUG_MODE = false;
	
	private static GameDirector sharedDirector;
	
	private FontManager fontManager;
	
	private SceneDirector sceneDirector;
	private AppGameContainer container;
	
	private Random random = new Random();
	
	private Game game;
	private WorldMap worldMap;
	private Inventory storeInventory = new Inventory(8, 10000);
	
	/**
	 * Constructs a game director object.
	 */
	private GameDirector() {
		 sharedDirector = this;
		 
		 fontManager = new FontManager();

		 sceneDirector = new SceneDirector("Oregon Trail", this);
		 makeInitialStoreInventory();
		 worldMap = new WorldMap();
		 game = new Game(worldMap);
	}
	
	/**
	 * Singleton Design pattern. Returns the single instance of game director object that drives the game, for the scenes to interact with.
	 * @return handle of game director for scene to interact with
	 */
	public static SceneListener sharedSceneListener() {
		return sharedDirector;
	}

	/**
	 * Launches window that contains game.
	 */
	public void start() {
		try {
			container = new AppGameContainer(sceneDirector);
			container.setDisplayMode(1024, 576, false);
			container.setTargetFrameRate(60);
			container.setShowFPS(false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the game object.
	 * @return the game
	 */
	public Game getGame() {
		return game;
	}
	
	/**
	 * Returns the container that holds the gui element of the game.
	 * @return the game container
	 */
	public AppGameContainer getContainer() {
		return container;
	}
	
	/**
	 * Determines the appropriate game scene to return based on a passed id.
	 * @param id the {@code SceneID} desired
	 * @return the handle to the newly created scene
	 */
	private Scene sceneForSceneID(SceneID id) {
		switch (id) {
		case MAINMENU:
			return new MainMenuScene();
		case PARTYCREATION:
			return new PartyCreationScene(game.getPlayer());
		case TOWN:
			return new TownScene(game.getPlayer().getParty());
		case STORE:
			return new StoreScene(game.getPlayer().getParty(), storeInventory);
		case PARTYINVENTORY:
			return new PartyInventoryScene(game.getPlayer().getParty());
		case SCENESELECTOR:
			return new SceneSelectorScene(game.getPlayer());
		case COMPONENTTEST:
			return new ComponentTestScene();
		case TRAILTEST:
			return new TrailTestScene(game.getPlayer().getParty());
		case HUNT :
			return new HuntScene(game.getPlayer().getParty());
		case TRAIL :
			return new TrailScene(game.getPlayer().getParty(), new RandomEncounterTable(getEncounterList()));
		}
		
		return null;
	}
	
	/**
	 * This method initializes the stores inventory.
	 */
	private void makeInitialStoreInventory() {
		Inventory inv = storeInventory;
		ArrayList<Item> itemToAdd = new ArrayList<Item>();
		for (int i = 0; i < 1 + Math.random() * 10; i++) {
			itemToAdd.add(new Item(Item.ITEM_TYPE.APPLE));
		}
		inv.addItem(itemToAdd);
		itemToAdd.clear();
		for (int i = 0; i < 1 + Math.random() * 10; i++) {
			itemToAdd.add(new Item(Item.ITEM_TYPE.BREAD));
		}
		inv.addItem(itemToAdd);
		itemToAdd.clear();
		for (int i = 0; i < 1 + Math.random() * 10; i++) {
			itemToAdd.add(new Item(Item.ITEM_TYPE.AMMO));
		}
		inv.addItem(itemToAdd);
		itemToAdd.clear();
		for (int i = 0; i < 1 + Math.random() * 10; i++) {
			itemToAdd.add(new Item(Item.ITEM_TYPE.GUN));
		}
		inv.addItem(itemToAdd);
		itemToAdd.clear();
		for (int i = 0; i < 1 + Math.random() * 10; i++) {
			itemToAdd.add(new Item(Item.ITEM_TYPE.MEAT));
		}
		inv.addItem(itemToAdd);
		itemToAdd.clear();
		for (int i = 0; i < 1 + Math.random() * 10; i++) {
			itemToAdd.add(new Item(Item.ITEM_TYPE.SONIC));
		}
		inv.addItem(itemToAdd);
		itemToAdd.clear();
		for (int i = 0; i < 1 + Math.random() * 10; i++) {
			itemToAdd.add(new Wagon());
		}
		inv.addItem(itemToAdd);
		itemToAdd.clear();
		for (int i = 0; i < 1 + Math.random() * 10; i++) {
			itemToAdd.add(new Item(Item.ITEM_TYPE.WHEEL));
		}
		inv.addItem(itemToAdd);
	}

	/*----------------------
	  SceneDelegate
	  ----------------------*/
	@Override
	public void requestScene(SceneID id, Scene lastScene) {
		Scene newScene = null;
		if (id == SceneID.SCENESELECTOR) {
			// Requested scene selector
			newScene = new SceneSelectorScene(game.getPlayer());
		} else if (lastScene instanceof MainMenuScene) {
			// Last scene was Main Menu Scene
			if (id == SceneID.PARTYCREATION) {
				// Requested Party Creation Scene
				newScene = new PartyCreationScene(game.getPlayer());
			}
		} else if (lastScene instanceof TownScene) {
			// Last scene was Town Scene
			if (id == SceneID.STORE) {
				// Requested Store Scene
				newScene = new StoreScene(game.getPlayer().getParty(), storeInventory);
			}
		} else if (lastScene instanceof StoreScene) {
			// Last scene was Store Scene
			if (id == SceneID.PARTYINVENTORY) {
				// Requested Party Inventory Scene
				newScene = new PartyInventoryScene(game.getPlayer().getParty(), ((StoreScene)lastScene).getInventory());
			}
		} else if (lastScene instanceof SceneSelectorScene) {
			// Last scene was Scene Selector Scene
			newScene =  sceneForSceneID(id);
		} else if (id == SceneID.PARTYINVENTORY) {
			// Requested Party Inventory Scene
			newScene = new PartyInventoryScene(game.getPlayer().getParty());
		} else if (id == SceneID.HUNT) {
			//Requested Hunt scene
			newScene = new HuntScene(game.getPlayer().getParty());
		} else if (id == SceneID.TRAIL) {
			//Requested Trail scene
			newScene = new TrailScene(game.getPlayer().getParty(), new RandomEncounterTable(getEncounterList()));
		}
		
		if (newScene != null) {
			// If scene was actually created
			sceneDirector.pushScene(newScene, true);
		}
	}
	
	@Override
	public void sceneDidEnd(Scene scene) {
		Scene newScene = null;
		if (scene instanceof PartyCreationScene) {
			// Last scene was Party Creation Scene
			newScene = new TownScene(game.getPlayer().getParty());
		}
		
		sceneDirector.popScene(true);
		if (newScene != null) {
			sceneDirector.pushScene(newScene, true);
		}
	}
	
	@Override
	public FontManager getFontManager() {
		return fontManager;
	}
	
	@Override
	public void showSceneSelector() {
		sceneDirector.replaceStackWithScene(sceneForSceneID(SceneID.SCENESELECTOR));
	}
	
	/**
	 * Gets the encounter list to populate the trail scenes random encounter table
	 * @return The mapping of scene types to probabilities
	 */
	private Map<SceneID, Integer> getEncounterList() {
		Map<SceneID, Integer> encounterList = new HashMap<SceneID, Integer>();
		/*
		for(SceneID scene : SceneID.values()) {
			if(scene.isRandomEncounter()) {
				encounterList.add(scene);
			}
		}
		 */
		encounterList.put(SceneID.STORE, getProbability(SceneID.STORE));
		encounterList.put(SceneID.PARTYINVENTORY, getProbability(SceneID.PARTYINVENTORY));
		encounterList.put(SceneID.TRAIL, getProbability(SceneID.TRAIL));
		
		return encounterList;
	}
	
	/**
	 * Probability generator for trail scene random encounter table construction
	 * @param scene The scene to get the probability of
	 * @return The probability
	 */
	private int getProbability(SceneID scene) {
		if (scene == SceneID.STORE) {
			return random.nextInt(10);
		} else if (scene == SceneID.PARTYINVENTORY) {
			return random.nextInt(10);
		} else {
			return random.nextInt(50) + 50;
		}
	}
	/*----------------------
	  SceneDirectorDelegate
	  ----------------------*/
	@Override
	public void sceneDirectorReady() {
		fontManager.init();
	}
	
	/*----------------------
	  Main
	  ----------------------*/
	/**
	 * The game main method
	 * @param args
	 */
	public static void main(String[] args) {
		Logger.log("-----------------NEW GAME STARTED-----------------", Logger.Level.INFO);
		GameDirector gameDirector = new GameDirector();
		gameDirector.start();
	}
}
