package scene;

public enum SceneID {
	DEFAULT ("Default"),
	MAINMENU ("Main Menu"),
	PARTYCREATION ("Party Creation"),
	STORE ("Store"),
	PARTYINVENTORY ("Party Inventory"),
	TOWN ("Town"),
	HUNT ("Hunt"),
	TRAIL ("Trail"),
	PARTYMANAGEMENTSCENE ("Party Management"),
	GAMEOVER ("Game Over"),
	SCENESELECTOR ("Scene Selector"),
	COMPONENTTEST ("Component Test"),
	TRAILTEST ("Trail Test"),
	MAP ("Map"),
	CAMP ("Camp");
	
	private final String name;
	
	private SceneID(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return getName();
	}
}
