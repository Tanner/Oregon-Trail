package core;

//import org.newdawn.slick.Music;

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
	 * Get the font manager.
	 * @return A {@code FontManager} object
	 */
	public FontStore getFontManager();
	
	//public void playMusic(Music music);
	
	//public void stopMusic();
}
