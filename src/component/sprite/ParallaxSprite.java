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
	protected final int DELTA_X = 1;
	protected static final int NEAR_MAX_ELAPSED_TIME = 1;
	protected static final int FAR_MAX_ELAPSED_TIME = 100 - NEAR_MAX_ELAPSED_TIME;
	
	public static int MAX_DISTANCE = 0;
	
	protected static double FAR_SPRITE_SIZE_PERCENTAGE = 0.50;
	
	protected Sprite sprite;
	protected Panel panel;
	
	protected int distance;
	protected double scale = 1.0;
	
	protected int elapsedTime;
	protected final int maxElapsedTime;

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
		super(context, spriteWidth, spriteWidth * image.getHeight() / image.getWidth());
		
		this.randomXPosition = randomXPosition;
		
		maxElapsedTime = ((FAR_MAX_ELAPSED_TIME * distance) / MAX_DISTANCE) + NEAR_MAX_ELAPSED_TIME;
		
		random = new Random();
		
		this.distance = distance;
		
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
		
		maxElapsedTime = ((FAR_MAX_ELAPSED_TIME * distance) / MAX_DISTANCE) + NEAR_MAX_ELAPSED_TIME;
		
		random = new Random();
		
		this.distance = distance;
		
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
	 */
	public ParallaxSprite(GUIContext context, int spriteWidth, Image image, int distance) {
		this(context, spriteWidth, image, distance, false);
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
	
	private int getXOffset() {
		if (randomXPosition) {
			return random.nextInt(panel.getWidth() - sprite.getWidth() + 1);
		}
		
		return 0;
	}
	
	public int getSpriteWidth() {
		return sprite.getWidth();
	}
	
	public int getDistance() {
		return distance;
	}
	
	public double getScale() {
		return scale;
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
