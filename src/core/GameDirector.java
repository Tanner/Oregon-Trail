package core;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import scene.*;
import scene.test.*;

import model.*;

public class GameDirector implements SceneDelegate, SceneDirectorDelegate {
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
			container.setDisplayMode(720, 480, false);
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
			return new PartyCreationScene();
		case Town:
			return new TownScene();
		case Store:
			return new StoreScene();
		case SceneSelector:
			return new SceneSelectorScene();
		case ComponentTest:
			return new ComponentTestScene();
		case GridLayoutTest:
			return new GridLayoutTestScene();
		}
		
		return null;
	}
	
	/*----------------------
	  SceneDelegate
	  ----------------------*/
	@Override
	public void requestScene(SceneID id) {
		Scene newScene = sceneForSceneID(id);
		sceneDirector.pushScene(newScene, true);
	}
	
	@Override
	public void sceneDidEnd(Scene scene) {
		sceneDirector.popScene(true);
	}
	
	@Override
	public FontManager getFontManager() {
		return fontManager;
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
