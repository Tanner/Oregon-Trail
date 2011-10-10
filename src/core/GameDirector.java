package core;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import scene.*;
import scene.test.*;

import model.*;

public class GameDirector implements SceneDelegate, SceneDirectorDelegate {
	public static final boolean DEBUG_MODE = false;
	
	private static GameDirector sharedDirector;
	
	private FontManager fontManager;
	
	private SceneDirector sceneDirector;
	private AppGameContainer container;
	
	private Game game;
	
	private GameDirector() {
		 sharedDirector = this;
		 
		 fontManager = new FontManager();

		 sceneDirector = new SceneDirector("Oregon Trail");
		 
		 game = new Game();
	}
	
	public static SceneDelegate sharedSceneDelegate() {
		if (sharedDirector == null) {
			sharedDirector = new GameDirector();
		}
		
		return sharedDirector;
	}
	
	public static SceneDirectorDelegate sharedSceneDirectorDelegate() {
		if (sharedDirector == null) {
			sharedDirector = new GameDirector();
		}
		
		return sharedDirector;
	}
	
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
	
	public Game getGame() {
		return game;
	}
	
	public AppGameContainer getContainer() {
		return container;
	}
	
	private Scene sceneForSceneID(SceneID id) {
		switch (id) {
		case MainMenu:
			return new MainMenuScene();
		case PartyCreation:
			return new PartyCreationScene(game.getPlayer());
		case Town:
			return new TownScene(game.getPlayer().getParty());
		case Store:
			return new StoreScene();
		case PartyInventory:
			return new PartyInventoryScene(game.getPlayer().getParty());
		case SceneSelector:
			return new SceneSelectorScene(game.getPlayer());
		case ComponentTest:
			return new ComponentTestScene();
		}
		
		return null;
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
		} else if (lastScene instanceof PartyCreationScene) {
			// Last scene was Party Creation Scene
			if (id == SceneID.Town) {
				// Requested Town Scene
				newScene = new TownScene(game.getPlayer().getParty());
			}
		} else if (lastScene instanceof TownScene) {
			// Last scene was Town Scene
			if (id == SceneID.Store) {
				// Requested Store Scene
				newScene = new StoreScene();	
			}
		} else if (lastScene instanceof StoreScene) {
			// Last scene was Store Scene
			if (id == SceneID.PartyInventory) {
				// Requested Party Inventory Scene
				newScene = new PartyInventoryScene(game.getPlayer().getParty());
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
		sceneDirector.popScene(true);
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
	public static void main(String[] args) {
		Logger.log("-----------------NEW GAME STARTED-----------------", Logger.Level.INFO);
		GameDirector gameDirector = new GameDirector();
		gameDirector.start();
	}
}
