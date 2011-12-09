package core;

//import org.newdawn.slick.Music;

import component.modal.Modal;

import model.Game;
import model.worldMap.TrailEdge;
import core.ConstantStore;
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
	
	/**
	 * Save the game data using serialization.
	 * @param name The filename to store the serialized data
	 */
	public void serialize(String name);
	
	/**
	 * Load the game data using serialization.
	 * @param name The serialized file to load from
	 */
	public Game deserialize(String name);
	
	/**
	 * Generates a modal with the correct save files, and allows the user
	 * to either save or load a game.
	 * @param isLoad True if you want a load modal, false if you want a save modal
	 * @param scene The scene you want the modal to return to
	 * @return A modal for saving or loading a game
	 */
	public Modal getLoadSaveModal(boolean isLoad, Scene scene);
	
	/**
	 * Updates the map when a map is purchased from the store.
	 * @param map The 
	 */
	public void updateMap(ConstantStore.StateIdx state);

	public TrailEdge trailBlaze();
}
