package core;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Manages images for use in the game.
 */
public class ImageStore {	
	private static ImageStore imageStore;
	
	public final Map<String, Image> IMAGES;
	
	/**
	 * Constructs a new {@code ImageStore} and initializes it.
	 */
	private ImageStore() {
		imageStore = this;
		
		IMAGES = new HashMap<String, Image>();
		
		try {
			initialize();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Initializes the store with all the images.
	 * @throws SlickException
	 */
	private void initialize() throws SlickException {
		IMAGES.put("LOGO", new Image("resources/graphics/logo.png", false, Image.FILTER_NEAREST));
		IMAGES.put("MAP_BACKGROUND", new Image("resources/graphics/backgrounds/map.png", false, Image.FILTER_NEAREST));
		IMAGES.put("TRAIL_MAP", new Image("resources/graphics/backgrounds/playerMap.png", false, Image.FILTER_NEAREST));
		IMAGES.put("DIRT_BACKGROUND", new Image("resources/graphics/backgrounds/dark_dirt.png"));
		IMAGES.put("CAMP_ICON", new Image("resources/graphics/icons/fire.png", false, Image.FILTER_NEAREST));
		IMAGES.put("TRAIL_ICON", new Image("resources/graphics/icons/trail.png", false, Image.FILTER_NEAREST));
		
		IMAGES.put("GRASS", new Image("resources/graphics/ground/grass.png", false, Image.FILTER_NEAREST));
		IMAGES.put("TRAIL", new Image("resources/graphics/ground/trail.png", false, Image.FILTER_NEAREST));
		IMAGES.put("HILL_A", new Image("resources/graphics/backgrounds/hill_a.png", false, Image.FILTER_NEAREST));
		IMAGES.put("HILL_B", new Image("resources/graphics/backgrounds/hill_b.png", false, Image.FILTER_NEAREST));
		IMAGES.put("TREE", new Image("resources/graphics/ground/tree.png", false, Image.FILTER_NEAREST));
		IMAGES.put("DEER", new Image("resources/graphics/animals/deer.png", false, Image.FILTER_NEAREST));
		
		IMAGES.put("CLOUD_A", new Image("resources/graphics/backgrounds/cloud_a.png", false, Image.FILTER_NEAREST));
		IMAGES.put("CLOUD_B", new Image("resources/graphics/backgrounds/cloud_b.png", false, Image.FILTER_NEAREST));
		IMAGES.put("CLOUD_C", new Image("resources/graphics/backgrounds/cloud_c.png", false, Image.FILTER_NEAREST));
		
		IMAGES.put("HUNTER_LEFT", new Image("resources/graphics/hunt/hunterLeftFaceSide1.png", false, Image.FILTER_NEAREST));
		IMAGES.put("HUNTER_RIGHT", new Image("resources/graphics/hunt/hunterRightFaceSide1.png", false, Image.FILTER_NEAREST));
		
		IMAGES.put("STORE_BUILDING", new Image("resources/graphics/buildings/general-store.png", false, Image.FILTER_NEAREST));
	}
	
	/**
	 * Get the {@code Image} for the key.
	 * @param name Key for the image
	 * @return Image at that key, if any
	 */
	public Image getImage(String name) {
		return IMAGES.get(name);
	}
	
	/**
	 * Returns the {@code ImageStore} instance. Creates one if one does not exist.
	 * @return Instance of ImageStore
	 */
	public static ImageStore get() {
		if (imageStore == null) {
			imageStore = new ImageStore();
		}
		
		return imageStore;
	}
}
