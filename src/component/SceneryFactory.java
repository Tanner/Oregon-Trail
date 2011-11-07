package component;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import component.hud.HUD;
import component.parallax.ParallaxPanel;
import component.parallax.ParallaxComponent;
import component.parallax.ParallaxComponentLoop;
import core.ImageStore;

/**
 * Generates a sky and scenery for a time of day and environment.
 */
public class SceneryFactory {
	private static final int HILL_DISTANCE_A = 300;
	private static final int HILL_DISTANCE_B = 600;
	private static final int CLOUD_DISTANCE = 400;
	private static final int GROUND_DISTANCE = 10;
	private static final int TREE_DISTANCE = 200;
	private static final int DEER_DISTANCE = 150;
	
	private static final int NUM_TREES = 40;
	private static final int TREE_OFFSET = 20;
	
	private static final int NUM_CLOUDS = 5;
	private static final int CLOUD_OFFSET = HUD.HEIGHT + 20;
	private static final int CLOUD_DISTANCE_VARIANCE = 10;
	private static final int CLOUD_OFFSET_VARIANCE = 10;
	
	private static final int DEER_OFFSET = 10;
	
	private static final float CLOUD_CHANCE = 0.2f;
	private static final float TREE_CHANCE = 0.9f;
	private static final float DEER_CHANCE = 0.05f;
	
	private static final Random random = new Random();
	
	/**
	 * Return a sky for the time period.
	 * @param container Container
	 * @param hour Time of day
	 * @return Sky panel with the background color for the time
	 * @throws SlickException
	 */
	public static Panel getSky(GameContainer container, int hour) throws SlickException {
		Panel sky = new Panel(container, skyColorForHour(hour));
		
		return sky;
	}
	
	/**
	 * Return the scenery for the time period and environment.
	 * @param container Container
	 * @return ParallaxPanel with correct scenery
	 * @throws SlickException
	 */
	public static ParallaxPanel getScenery(GameContainer container) throws SlickException {
		ParallaxPanel parallaxPanel = new ParallaxPanel(container, container.getWidth(), container.getHeight());		
		ParallaxComponent.MAX_DISTANCE = HILL_DISTANCE_B;
		return parallaxPanel;
	}
	
	public static ParallaxComponentLoop getGround(GameContainer container) throws SlickException {
		return new ParallaxComponentLoop(container, container.getWidth() + 1, new Image("resources/graphics/ground/grass.png", false, Image.FILTER_NEAREST), GROUND_DISTANCE);
	}
	
	public static ParallaxComponentLoop getTrail(GameContainer container) throws SlickException {
		return new ParallaxComponentLoop(container, container.getWidth() + 1, new Image("resources/graphics/ground/trail.png", false, Image.FILTER_NEAREST), GROUND_DISTANCE);
	}
	
	public static ParallaxComponentLoop getHillA(GameContainer container) throws SlickException {
		return new ParallaxComponentLoop(container, container.getWidth() + 1, new Image("resources/graphics/backgrounds/hill_a.png", false, Image.FILTER_NEAREST), HILL_DISTANCE_A);
	}
	
	public static ParallaxComponentLoop getHillB(GameContainer container) throws SlickException {
		return new ParallaxComponentLoop(container, container.getWidth() + 1, new Image("resources/graphics/backgrounds/hill_b.png", false, Image.FILTER_NEAREST), HILL_DISTANCE_B);
	}
	
	public static ParallaxComponent getCloud(GameContainer container, boolean randomXPosition) throws SlickException {		
		Image[] cloudImages = new Image[3];
		cloudImages[0] = ImageStore.get().getImage("CLOUD_A");
		cloudImages[1] = ImageStore.get().getImage("CLOUD_B");
		cloudImages[2] = ImageStore.get().getImage("CLOUD_C");

		int distance = CLOUD_DISTANCE + random.nextInt(CLOUD_DISTANCE_VARIANCE * 2) - CLOUD_DISTANCE_VARIANCE;
		int cloudImage = random.nextInt(cloudImages.length);
		
		ParallaxComponent cloud = new ParallaxComponent(container, cloudImages[cloudImage], distance, randomXPosition);
		cloud.setAlwaysMoving(true);
		return cloud;
	}
	
	public static ParallaxComponent getTree(GameContainer container, boolean randomXPosition) throws SlickException {		
		int distance = random.nextInt(TREE_DISTANCE);
		
		if (distance <= TREE_DISTANCE / 3) {
			distance = random.nextInt(GROUND_DISTANCE);
		}

		return new ParallaxComponent(container, 96, new Image("resources/graphics/ground/tree.png", false, Image.FILTER_NEAREST), 0, TREE_DISTANCE, distance, randomXPosition);
	}
	
	public static ParallaxComponent getDeer(GameContainer container) throws SlickException {		
		int distance = random.nextInt(DEER_DISTANCE);

		return new ParallaxComponent(container, 56, new Image("resources/graphics/animals/deer.png", false, Image.FILTER_NEAREST), 0, DEER_DISTANCE, distance, false);
	}
	
	/**
	 * Get the sky {@code AnimatingColor} for a time.
	 * @param hour Time of day
	 * @param duration Duration of animation
	 * @return AnimatingColor
	 */
	public static AnimatingColor getSkyAnimatingColor(int hour, int duration) {
		return new AnimatingColor(skyColorForHour(hour - 1), skyColorForHour(hour), duration);
	}
	
	/**
	 * Get the background overlay {@code AnimatingColor} for a time.
	 * @param hour Time of day
	 * @param duration Duration of animation
	 * @return AnimatingColor
	 */
	public static AnimatingColor getOverlayAnimatingColor(int hour, int duration) {
		 return new AnimatingColor(getOverlayColorForHour(hour - 1), getOverlayColorForHour(hour), duration);
	}
	
	/**
	 * Get the background overlay color for the hour of the day.
	 * @param hour Hour of the day
	 * @return Background overlay for that hour
	 */
	public static Color getOverlayColorForHour(int hour) {
		switch (hour + 1) {
			case 7:
				return new Color(0, 0, 0, .3f);
			case 8:
				return new Color(0, 0, 0, .1f);
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
				return new Color(0, 0, 0, 0f);
			case 18:
			case 19:
				return new Color(0, 0, 0, .1f);
			case 20:
				return new Color(0, 0, 0, .3f);
			default:
				return new Color(0, 0, 0, .5f);
		}
	}
	
	/**
	 * Get the correct sky color for the hour of day.
	 * @param hour Hour of the day
	 * @return Sky color for that hour
	 */
	private static Color skyColorForHour(int hour) {
		switch (hour + 1) {
			case 6:
				return new Color(0xeba7a4); // light pink
			case 7:
				return new Color(0xebd0a4); // light orange
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
				return new Color(0x579cdd); // light blue
			case 18:
			case 19:
				return new Color(0xdd90a4); // pink
			case 20:
				return new Color(0x4a3b48); // dark purple
			default:
				return Color.black;
		}
	}

	public static boolean shouldAddCloud() {
		return (random.nextFloat() <= CLOUD_CHANCE);
	}
	
	public static boolean shouldAddTree() {
		return (random.nextFloat() <= TREE_CHANCE);
	}

	public static boolean shouldAddDeer() {
		return (random.nextFloat() <= DEER_CHANCE);
	}
}
