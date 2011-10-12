package core;

/**
 * Provides interface between scenes and the game director
 */

public interface SceneDirectorDelegate {
	/**
	 * Message that the scene director has initialized its starting scenes.
	 */
	public void sceneDirectorReady();
}
