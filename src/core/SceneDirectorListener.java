package core;

/**
 * An interface between the scene director and the game director.
 */

public interface SceneDirectorListener {
	/**
	 * Message that the scene director has initialized its starting scenes.
	 */
	public void sceneDirectorReady();
}
