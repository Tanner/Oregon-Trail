package core;

import scene.LoadingScene.LOAD_ITEMS;

public class Loader extends Thread {
	private LOAD_ITEMS itemToLoad;
	
	@Override
	public void run() {
		if (itemToLoad == LOAD_ITEMS.SOUNDS) {
			SoundStore.get();
		} else if (itemToLoad == LOAD_ITEMS.FONTS) {
			FontStore.get();
		}
	}
	
	public void setItemToLoad(LOAD_ITEMS item) {
		itemToLoad = item;
	}
}
