package scene;

public enum SceneID {
	DEFAULT ("Default"),
	SPLASH ("Splash Screen"),
	LOADING ("Load Screen"),
	MAINMENU ("Main Menu"),
	PARTYCREATION ("Party Creation"),
	STORE ("Store"),
	PARTYINVENTORY ("Party Inventory"),
	TOWN ("Town"),
	HUNT ("Hunt"),
	TRAIL ("Trail"),
	GAMEOVER ("Game Over"),
	VICTORY ("Victory"),
	SCENESELECTOR ("Scene Selector"),
	COMPONENTTEST ("Component Test"),
	TRAILTEST ("Trail Test"),
	MAP ("Map"),
	RIVER ("River"),
	OPTIONS ("Options"),
	TAVERN ("Tavern");
	
	private final String name;
	
	private SceneID(String name) {
		this.name = name;
	}
	
	/**
	 * Get the name of a SceneID.
	 * @return The string representation of a SceneID
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
