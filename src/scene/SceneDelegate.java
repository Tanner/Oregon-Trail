package scene;

import core.FontManager;

public interface SceneDelegate {
	/**
	 * Request a new {@code Scene} to be presented.
	 * 
	 * @param id The ID for the scene requested
	 */
	public void requestScene(SceneID id);
	
	/**
	 * Handle scene ending.
	 * 
	 * @param scene {@code Scene} that ended 
	 */
	public void sceneDidEnd(Scene scene);
	
	/**
	 * Show a {@code SceneSelectorScene}.
	 */
	public void showSceneSelector();
	
	/**
	 * Get the font manager.
	 * 
	 * @return A {@code FontManager} object
	 */
	public FontManager getFontManager();
}
