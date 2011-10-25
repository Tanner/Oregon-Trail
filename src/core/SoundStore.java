package core;

public class SoundStore {
	
	private static SoundStore soundStore;
	private SoundStore() {
		soundStore = this;
	}
	
	public SoundStore get() {
		return soundStore;
	}
}
