package core;

import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import scene.*;
import scene.test.*;

import model.*;
import model.item.Apple;
import model.item.Bread;
import model.item.Bullet;
import model.item.Gun;
import model.item.Meat;
import model.item.SonicScrewdriver;
import model.item.Wagon;
import model.item.Wheel;

/**
 * Directs the logical functionality of the game. Sets everything in motion.
 */

public class GameDirector implements SceneListener, SceneDirectorListener {
	public static boolean DEBUG_MODE = false;
	
	private static GameDirector sharedDirector;
	
	private FontManager fontManager;
	
	private SceneDirector sceneDirector;
	private AppGameContainer container;
	
	private Game game;
	private Inventory storeInventory = new Inventory(8,10000);
	
	/**
	 * Constructs a game director object.
	 */
	private GameDirector() {
		 sharedDirector = this;
		 
		 fontManager = new FontManager();

		 sceneDirector = new SceneDirector("Oregon Trail", this);
		 makeInitialStoreInventory();
		 game = new Game();
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
		case MainMenu:
			return new MainMenuScene();
		case PartyCreation:
			return new PartyCreationScene(game.getPlayer());
		case Town:
			return new TownScene(game.getPlayer().getParty());
		case Store:
			return new StoreScene(game.getPlayer().getParty(), storeInventory);
		case PartyInventory:
			return new PartyInventoryScene(game.getPlayer().getParty());
		case SceneSelector:
			return new SceneSelectorScene(game.getPlayer());
		case ComponentTest:
			return new ComponentTestScene();
		case TrailTestScene:
			return new TrailTestScene(game.getPlayer().getParty());
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
	}

	/*----------------------
	  SceneDelegate
	  ----------------------*/
	@Override
	public void requestScene(SceneID id, Scene lastScene) {
		Scene newScene = null;
		if (id == SceneID.SceneSelector) {
			// Requested scene selector
			newScene = new SceneSelectorScene(game.getPlayer());
		} else if (lastScene instanceof MainMenuScene) {
			// Last scene was Main Menu Scene
			if (id == SceneID.PartyCreation) {
				// Requested Party Creation Scene
				newScene = new PartyCreationScene(game.getPlayer());
			}
		} else if (lastScene instanceof TownScene) {
			// Last scene was Town Scene
			if (id == SceneID.Store) {
				// Requested Store Scene
				newScene = new StoreScene(game.getPlayer().getParty(), storeInventory);	
			}
		} else if (lastScene instanceof StoreScene) {
			// Last scene was Store Scene
			if (id == SceneID.PartyInventory) {
				// Requested Party Inventory Scene
				newScene = new PartyInventoryScene(game.getPlayer().getParty(), ((StoreScene)lastScene).getInventory());
			}
		} else if (lastScene instanceof SceneSelectorScene) {
			// Last scene was Scene Selector Scene
			newScene =  sceneForSceneID(id);
		} else if (id == SceneID.PartyInventory) {
			// Requested Party Inventory Scene
			newScene = new PartyInventoryScene(game.getPlayer().getParty());
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
		sceneDirector.replaceStackWithScene(sceneForSceneID(SceneID.SceneSelector));
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
