package core;

import java.util.HashMap;
import java.util.Map;
import core.ConstantStore;
//import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Manages images for use in the game.
 */
public class ImageStore {	
	private static ImageStore imageStore;
/*	private final String PATH_GRAPHICS = "resources/graphics/";
	private final String PATH_BKGRND = PATH_GRAPHICS + "backgrounds/";
	private final String PATH_ANIMALS = PATH_GRAPHICS + "animals/";
	private final String PATH_ICONS = PATH_GRAPHICS + "icons/";
	private final String PATH_GROUND = PATH_GRAPHICS + "ground/";
	private final String PATH_PEOPLE = PATH_GRAPHICS + "people/";
	private final String PATH_BUILDINGS = PATH_GRAPHICS + "buildings/";
	private final String PATH_HUNT = PATH_GRAPHICS + "hunt/";
	private final String PATH_TRAIL = PATH_GRAPHICS + "trail/";
*/
	
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
		IMAGES.put("LOGO", new Image(ConstantStore.PATH_GRAPHICS + "logo.png", false, Image.FILTER_NEAREST));
		IMAGES.put("MAP_BACKGROUND", new Image(ConstantStore.PATH_BKGRND + "map.png", false, Image.FILTER_NEAREST));
		IMAGES.put("TRAIL_MAP", new Image(ConstantStore.PATH_BKGRND + "playerMap.png", false, Image.FILTER_NEAREST));
		IMAGES.put("DIRT_BACKGROUND", new Image(ConstantStore.PATH_BKGRND + "dark_dirt.png"));
		IMAGES.put("CAMP_ICON", new Image(ConstantStore.PATH_ICONS + "fire.png", false, Image.FILTER_NEAREST));
		IMAGES.put("INVENTORY_ICON", new Image(ConstantStore.PATH_ICONS + "pack.png", false, Image.FILTER_NEAREST));
		IMAGES.put("TRAIL_ICON", new Image(ConstantStore.PATH_ICONS + "trail.png", false, Image.FILTER_NEAREST));
		
		IMAGES.put("MAP_POINTER1", new Image(ConstantStore.PATH_ICONS + "mapPointer1.png", false, Image.FILTER_NEAREST));
		IMAGES.put("MAP_POINTER2", new Image(ConstantStore.PATH_ICONS + "mapPointer2.png", false, Image.FILTER_NEAREST));
		IMAGES.put("MAP_POINTER3", new Image(ConstantStore.PATH_ICONS + "mapPointer3.png", false, Image.FILTER_NEAREST));
		IMAGES.put("MAP_POINTER4", new Image(ConstantStore.PATH_ICONS + "mapPointer4.png", false, Image.FILTER_NEAREST));
		IMAGES.put("MAP_POINTER5", new Image(ConstantStore.PATH_ICONS + "mapPointer5.png", false, Image.FILTER_NEAREST));
		IMAGES.put("MAP_POINTER6", new Image(ConstantStore.PATH_ICONS + "mapPointer6.png", false, Image.FILTER_NEAREST));
		
		IMAGES.put("GRASS", new Image(ConstantStore.PATH_GROUND + "grass.png", false, Image.FILTER_NEAREST));
		IMAGES.put("TRAIL", new Image(ConstantStore.PATH_GROUND + "trail.png", false, Image.FILTER_NEAREST));
		IMAGES.put("HILL_A", new Image(ConstantStore.PATH_BKGRND + "hill_a.png", false, Image.FILTER_NEAREST));
		IMAGES.put("HILL_B", new Image(ConstantStore.PATH_BKGRND + "hill_b.png", false, Image.FILTER_NEAREST));
		IMAGES.put("TREE", new Image(ConstantStore.PATH_GROUND + "tree.png", false, Image.FILTER_NEAREST));
		IMAGES.put("DEER", new Image(ConstantStore.PATH_ANIMALS + "deer.png", false, Image.FILTER_NEAREST));
		
		IMAGES.put("CLOUD_A", new Image(ConstantStore.PATH_BKGRND + "cloud_a.png", false, Image.FILTER_NEAREST));
		IMAGES.put("CLOUD_B", new Image(ConstantStore.PATH_BKGRND + "cloud_b.png", false, Image.FILTER_NEAREST));
		IMAGES.put("CLOUD_C", new Image(ConstantStore.PATH_BKGRND + "cloud_c.png", false, Image.FILTER_NEAREST));
		
		IMAGES.put("TRAIL_WAGON", new Image(ConstantStore.PATH_TRAIL + "trailWagon.png", false, Image.FILTER_NEAREST));
		IMAGES.put("SMALL_WHEEL_1", new Image(ConstantStore.PATH_TRAIL + "smallwheel1.png", false, Image.FILTER_NEAREST));
		IMAGES.put("SMALL_WHEEL_2", new Image(ConstantStore.PATH_TRAIL + "smallwheel2.png", false, Image.FILTER_NEAREST));
		IMAGES.put("SMALL_WHEEL_3", new Image(ConstantStore.PATH_TRAIL + "smallwheel3.png", false, Image.FILTER_NEAREST));
		IMAGES.put("BIG_WHEEL_1", new Image(ConstantStore.PATH_TRAIL + "wheel1.png", false, Image.FILTER_NEAREST));
		IMAGES.put("BIG_WHEEL_2", new Image(ConstantStore.PATH_TRAIL + "wheel2.png", false, Image.FILTER_NEAREST));
		IMAGES.put("BIG_WHEEL_3", new Image(ConstantStore.PATH_TRAIL + "wheel3.png", false, Image.FILTER_NEAREST));
				
		//images for hunt scene
		IMAGES.put("HUNTER_LEFT", new Image(ConstantStore.PATH_HUNT + "hunterLeftFaceSide1.png", false, Image.FILTER_NEAREST));
		IMAGES.put("HUNTER_RIGHT", new Image(ConstantStore.PATH_HUNT + "hunterRightFaceSide1.png", false, Image.FILTER_NEAREST));
		IMAGES.put("HUNTER_FRONT", new Image(ConstantStore.PATH_HUNT + "hunterFront1.png", false, Image.FILTER_NEAREST));
		IMAGES.put("HUNTER_BACK", new Image(ConstantStore.PATH_HUNT + "hunterBack1.png", false, Image.FILTER_NEAREST));
		//below need to be replaced with diagonal images once they're made
		IMAGES.put("HUNTER_UPPERLEFT", new Image(ConstantStore.PATH_HUNT + "hunterLeftFaceSide1.png", false, Image.FILTER_NEAREST));
		IMAGES.put("HUNTER_UPPERRIGHT", new Image(ConstantStore.PATH_HUNT + "hunterRightFaceSide1.png", false, Image.FILTER_NEAREST));
		IMAGES.put("HUNTER_LOWERLEFT", new Image(ConstantStore.PATH_HUNT + "hunterFront1.png", false, Image.FILTER_NEAREST));
		IMAGES.put("HUNTER_LOWERRIGHT", new Image(ConstantStore.PATH_HUNT + "hunterBack1.png", false, Image.FILTER_NEAREST));

		IMAGES.put("TRAPPER_RIGHT", new Image(ConstantStore.PATH_PEOPLE + "hunterLeftFaceSide1.png", false, Image.FILTER_NEAREST));
		IMAGES.put("TRAPPER_LEFT", new Image(ConstantStore.PATH_PEOPLE + "hunterRightFaceSide1.png", false, Image.FILTER_NEAREST));
		IMAGES.put("MAIDEN_LEFT", new Image(ConstantStore.PATH_PEOPLE + "maidenLeftFaceSide1.png", false, Image.FILTER_NEAREST));
		IMAGES.put("MAIDEN_RIGHT", new Image(ConstantStore.PATH_PEOPLE + "maidenRightFaceSide1.png", false, Image.FILTER_NEAREST));
		IMAGES.put("HILLBILLY_LEFT", new Image(ConstantStore.PATH_PEOPLE + "hillbillyLeftFaceSide1.png", false, Image.FILTER_NEAREST));
		IMAGES.put("HILLBILLY_RIGHT", new Image(ConstantStore.PATH_PEOPLE + "hillbillyRightFaceSide1.png", false, Image.FILTER_NEAREST));
		
		IMAGES.put("STORE_BUILDING", new Image(ConstantStore.PATH_BUILDINGS + "general-store.png", false, Image.FILTER_NEAREST));
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
