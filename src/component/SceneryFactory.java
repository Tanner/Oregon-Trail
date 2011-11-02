package component;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import component.Positionable.ReferencePoint;
import component.sprite.ParallaxSprite;
import component.sprite.ParallaxSpriteLoop;

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
	private static final int CLOUD_OFFSET = 20;
	private static final int CLOUD_DISTANCE_VARIANCE = 10;
	private static final int CLOUD_OFFSET_VARIANCE = 10;
	
	private static final int DEER_OFFSET = 10;
	
	/**
	 * Return a sky for the time period.
	 * @param container Container
	 * @param party Party
	 * @return Sky panel with the background color for the time
	 * @throws SlickException
	 */
	public static Panel getSky(GameContainer container, int hour) throws SlickException {
		Panel sky = new Panel(container, skyColorForHour(hour));
		
		return sky;
	}
	
	/**
	 * Get the background overlay color for the time period.
	 * @param hour Time of day
	 * @return Color for the overlay
	 */
	public static Color getBackgroundOverlayColor(int hour) {
		return backgroundOverlayColorForHour(hour);
	}
	
	/**
	 * Return the scenery for the time period and environment.
	 * @param container Container
	 * @param party Party
	 * @return ParallaxPanel with correct scenery
	 * @throws SlickException
	 */
	public static ParallaxPanel getScenery(GameContainer container) throws SlickException {
		// Stuff
		ParallaxPanel parallaxPanel = new ParallaxPanel(container, container.getWidth(), container.getHeight());
		Random random = new Random();
		
		ParallaxSprite.MAX_DISTANCE = HILL_DISTANCE_B;
		
		// Ground
		ParallaxSprite ground = new ParallaxSpriteLoop(container, container.getWidth() + 1, new Image("resources/graphics/ground/trail.png", false, Image.FILTER_NEAREST), GROUND_DISTANCE);
		parallaxPanel.add(ground, parallaxPanel.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT);
		
		// Hills
		ParallaxSprite hillA = new ParallaxSpriteLoop(container, container.getWidth(), new Image("resources/graphics/backgrounds/hill_a.png", false, Image.FILTER_NEAREST), HILL_DISTANCE_A);
		parallaxPanel.add(hillA, ground.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.BOTTOMLEFT);
		
		ParallaxSprite hillB = new ParallaxSpriteLoop(container, container.getWidth(), new Image("resources/graphics/backgrounds/hill_b.png", false, Image.FILTER_NEAREST), HILL_DISTANCE_B);
		parallaxPanel.add(hillB, ground.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.BOTTOMLEFT);
		
		// Clouds
		Image[] cloudImages = new Image[3];
		cloudImages[0] = new Image("resources/graphics/backgrounds/cloud_a.png", false, Image.FILTER_NEAREST);
		cloudImages[1] = new Image("resources/graphics/backgrounds/cloud_b.png", false, Image.FILTER_NEAREST);
		cloudImages[2] = new Image("resources/graphics/backgrounds/cloud_c.png", false, Image.FILTER_NEAREST);
		
		for (int i = 0; i < NUM_CLOUDS; i++) {
			int distance = CLOUD_DISTANCE + random.nextInt(CLOUD_DISTANCE_VARIANCE * 2) - CLOUD_DISTANCE_VARIANCE;
			int cloudImage = random.nextInt(cloudImages.length);
			
			int offset = CLOUD_OFFSET + random.nextInt(CLOUD_OFFSET_VARIANCE * 2) - CLOUD_OFFSET_VARIANCE;
			
			ParallaxSprite cloud = new ParallaxSprite(container, cloudImages[cloudImage], distance, true);
			parallaxPanel.add(cloud, parallaxPanel.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT, 0, offset);
		}
		
		// Trees
		for (int i = 0; i < NUM_TREES; i++) {
			int distance = random.nextInt(TREE_DISTANCE);
			int offset = TREE_OFFSET;
			
			if (distance <= TREE_DISTANCE / 3) {
				distance = random.nextInt(GROUND_DISTANCE);
				
				offset += GROUND_DISTANCE - distance;
			}
			
			ParallaxSprite tree = new ParallaxSprite(container, 96, new Image("resources/graphics/ground/tree.png", false, Image.FILTER_NEAREST), 0, TREE_DISTANCE, distance, true);
			
			offset -= (int) (tree.getScale() * offset) / 2;

			parallaxPanel.add(tree, ground.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.BOTTOMLEFT, 0, offset);
		}
		
		ParallaxSprite deer = new ParallaxSprite(container, new Image("resources/graphics/animals/deer.png", false, Image.FILTER_NEAREST), DEER_DISTANCE, true);
		parallaxPanel.add(deer, ground.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.BOTTOMLEFT, 0, DEER_OFFSET);
		
		return parallaxPanel;
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
	public static AnimatingColor getBackgroundOverlayAnimatingColor(int hour, int duration) {
		 return new AnimatingColor(getBackgroundOverlayColor(hour - 1), getBackgroundOverlayColor(hour), duration);
	}
	
	/**
	 * Get the background overlay color for the hour of the day.
	 * @param hour Hour of the day
	 * @return Background overlay for that hour
	 */
	private static Color backgroundOverlayColorForHour(int hour) {
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
}
