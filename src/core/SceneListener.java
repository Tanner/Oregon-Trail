package core;

import scene.Scene;
import scene.SceneID;
import core.FontManager;

/**
 * An interface between scenes and the game director.
 */
public interface SceneListener {
	/**
	 * Request a new {@code Scene} to be presented.
	 * @param id The ID for the scene requested
	 */
	public void requestScene(SceneID id, Scene lastScene);
	
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
	 * Get the font manager.
	 * @return A {@code FontManager} object
	 */
	public FontManager getFontManager();
}