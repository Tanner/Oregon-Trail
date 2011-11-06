package core;

//import org.newdawn.slick.Music;

import component.modal.Modal;

import model.Game;
import scene.Scene;
import scene.SceneID;

/**
 * An interface between scenes and the game director.
 */
public interface SceneListener {
	/**
	 * Request a new {@code Scene} to be presented.
	 * @param id The ID for the scene requested
	 */
	public void requestScene(SceneID id, Scene lastScene, boolean popLastScene);
	
	/**
	 * Handle scene ending.
	 * @param scene {@code Scene} that ended 
	 */
	public void sceneDidEnd(Scene scene);
	
	/**
	 * Show a {@code SceneSelectorScene}.
	 */
	public void showSceneSelector();
	
	/**
	 * Show a {@code MainMenuScene}.
	 */
	public void resetToMainMenu();
	
	/**
	 * Get the font manager.
	 * @return A {@code FontManager} object
	 */
	public FontStore getFontManager();
	
	public Game getGame();
	
	public void serialize(String name);

	public Game deserialize(String name);
	
	public Modal getLoadSaveModal(boolean isLoad, Scene scene);

}
