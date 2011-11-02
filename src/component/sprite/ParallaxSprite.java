package component.sprite;

import java.util.Random;

import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.Image;

import component.Component;
import component.Panel;

/**
 * A sprite that shifts a certain amount on the screen and can scale with distance.
 */
public class ParallaxSprite extends Component implements Comparable<ParallaxSprite> {
	public static int DELTA_X = 1;
	protected static int NEAR_MAX_ELAPSED_TIME = 1;
	protected static int FAR_MAX_ELAPSED_TIME = 100 - NEAR_MAX_ELAPSED_TIME;
	
	public static int MAX_DISTANCE = 0;
	
	protected static double FAR_SPRITE_SIZE_PERCENTAGE = 0.50;
	
	protected Sprite sprite;
	protected Panel panel;
	
	protected int distance;
	protected double scale = 1.0;
	
	protected int elapsedTime;
	protected int maxElapsedTime;

	private boolean randomXPosition;
	private Random random;
	
	/**
	 * Constructs a ParallaxSprite with a context, spriteWidth, and image. Sprite scales with distance and can have a random X position.
	 * @param context Context to use
	 * @param spriteWidth Width the sprite should be (e.g. for scaling)
	 * @param image Image to use for the sprite
	 * @param minDistance The closest this sprite can be to the view
	 * @param maxDistance The farthest this sprite can be to the view
	 * @param distance What the distance this sprite should be
	 * @param randomXPosition Whether or not the sprite should be in a random position in the container
	 */
	public ParallaxSprite(GUIContext context, int spriteWidth, Image image, int minDistance, int maxDistance, int distance, boolean randomXPosition) {
		super(context, context.getWidth(), spriteWidth * image.getHeight() / image.getWidth());
		
		this.randomXPosition = randomXPosition;

		this.distance = distance;
		setMaxElapsedTime(distance);
		
		random = new Random();
		
		if (minDistance == 0) {
			minDistance = 1;
		}
		
		double distancePercent = 1.0;
		
		if (maxDistance - minDistance != 0) {
			distancePercent = (double) (distance - minDistance) / (maxDistance - minDistance);
		}
		double scaleRange = 1.0 - FAR_SPRITE_SIZE_PERCENTAGE;
		scale = 1.0 - (distancePercent * scaleRange);
		
		spriteWidth = (int) (spriteWidth * scale);
		
		sprite = new Sprite(context, spriteWidth, image);
		
		panel = new Panel(context, context.getWidth(), sprite.getHeight());
		panel.add(sprite, getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT, getXOffset(), 0);
		
		add(panel, getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT, 0, 0);
	}
	
	/**
	 * Constructs a ParallaxSprite with a context, spriteWidth, and image. Sprite can have a random X position.
	 * @param context Context to use
	 * @param spriteWidth Width the sprite should be (e.g. for scaling)
	 * @param image Image to use for the sprite
	 * @param distance What the distance this sprite should be
	 * @param randomXPosition Whether or not the sprite should be in a random position in the container
	 */
	public ParallaxSprite(GUIContext context, int spriteWidth, Image image, int distance, boolean randomXPosition) {
		super(context, context.getWidth(), spriteWidth * image.getHeight() / image.getWidth());
		
		this.randomXPosition = randomXPosition;
		
		this.distance = distance;
		setMaxElapsedTime(distance);
		
		random = new Random();
		
		sprite = new Sprite(context, spriteWidth, image);
		
		panel = new Panel(context, context.getWidth(), sprite.getHeight());
		panel.add(sprite, getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT, getXOffset(), 0);
		
		add(panel, getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT, 0, 0);
	}
	
	/**
	 * Constructs a ParallaxSprite with a context, spriteWidth, and image. Sprite can have a random X position.
	 * @param context Context to use
	 * @param image Image to use for the sprite
	 * @param distance What the distance this sprite should be
	 * @param randomXPosition Whether or not the sprite should be in a random position in the container
	 */
	public ParallaxSprite(GUIContext context, Image image, int distance, boolean randomXPosition) {
		this(context, image.getWidth() * 2, image, distance, randomXPosition);
	}
	
	/**
	 * Constructs a ParallaxSprite with a context, spriteWidth, and image. Sprite can have a random X position.
	 * @param context Context to use
	 * @param spriteWidth Width the sprite should be (e.g. for scaling)
	 * @param image Image to use for the sprite
	 * @param distance What the distance this sprite should be
	 */
	public ParallaxSprite(GUIContext context, int spriteWidth, Image image, int distance) {
		this(context, spriteWidth, image, distance, false);
	}
	
	/**
	 * Constructs a ParallaxSprite with a context, spriteWidth, and image. Sprite can have a random X position.
	 * @param context Context to use
	 * @param image Image to use for the sprite
	 * @param distance What the distance this sprite should be
	 */
	public ParallaxSprite(GUIContext context, Image image, int distance) {
		this(context, image, distance, false);
	}
	
	/**
	 * Moves the ParallaxSprite when the elapsedTime is greater than the maxElapsedTime. Loop images around when needed.
	 * @param delta Amount of time passed
	 */
	public void move(int delta) {
		elapsedTime += delta;
		
		if (elapsedTime > maxElapsedTime) {
			panel.setLocation(panel.getX() + DELTA_X, panel.getY());
			elapsedTime = 0;
		}
		
		if (sprite.getX() > container.getWidth()) {
			panel.setLocation(0 - panel.getWidth(), panel.getY());
			
			sprite.setPosition(panel.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT, getXOffset(), 0);
		}
	}
	
	/**
	 * Set the maxElapsedTime using distance.
	 * @param distance Distance from the viewer
	 */
	private void setMaxElapsedTime(int distance) {
		maxElapsedTime = ((FAR_MAX_ELAPSED_TIME * distance) / MAX_DISTANCE) + NEAR_MAX_ELAPSED_TIME;
	}
	
	/**
	 * Get a new x-axis offset. Returns a new value if random is on, zero otherwise.
	 * @return A new x-axis offset
	 */
	private int getXOffset() {
		if (randomXPosition) {
			return random.nextInt(panel.getWidth() - sprite.getWidth() + 1);
		}
		
		return 0;
	}
	
	/**
	 * Get the width of the {@code Sprite}.
	 * @return Width of the sprite
	 */
	public int getSpriteWidth() {
		return sprite.getWidth();
	}
	
	/**
	 * Get the distance from the viewer.
	 * @return Distance from viewer
	 */
	public int getDistance() {
		return distance;
	}
	
	/**
	 * Get the scale of the {@code Sprite}.
	 * @return Scale of the Sprite.
	 */
	public double getScale() {
		return scale;
	}
	
	/**
	 * Set the Max Elapsed Times (i.e. NEAR and FAR_MAX_ELAPSED_TIME) for the {@code Sprite} movement.
	 * @param nearMaxElapsedTime New near time
	 * @param farMaxElapsedTime New far time
	 */
	public static void setMaxElapsedTimes(int nearMaxElapsedTime, int farMaxElapsedTime) {
		NEAR_MAX_ELAPSED_TIME = nearMaxElapsedTime;
		FAR_MAX_ELAPSED_TIME = farMaxElapsedTime - NEAR_MAX_ELAPSED_TIME;
	}

	@Override
	public int compareTo(ParallaxSprite sprite) {
		return sprite.distance - this.distance;
	}
	
	@Override
	public String toString() {
		return sprite + " at " + distance;
	}
}
