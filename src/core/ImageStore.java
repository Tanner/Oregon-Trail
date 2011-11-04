package core;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Font;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Manages images for use in the game.
 */
public class ImageStore {	
	private static ImageStore imageStore;
	
	public final Map<String, Image> IMAGES;
	
	private ImageStore() {
		imageStore = this;
		
		IMAGES = new HashMap<String, Image>();
		
		try {
			initialize();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	private void initialize() throws SlickException {
		IMAGES.put("LOGO", new Image("resources/graphics/logo.png", false, Image.FILTER_NEAREST));
		IMAGES.put("MAP", new Image("resources/graphics/backgrounds/map.png", false, Image.FILTER_NEAREST));
	}
	
	public Image getImage(String name) {
		return IMAGES.get(name);
	}
	
	public static ImageStore get() {
		if (imageStore == null) {
			imageStore = new ImageStore();
		}
		
		return imageStore;
	}
}
