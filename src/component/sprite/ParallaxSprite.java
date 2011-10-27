package component.sprite;

import java.util.Random;

import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.Image;

import component.Component;
import component.Panel;

/**
 * A sprite that shifts a certain amount on the screen and loops around.
 */
public class ParallaxSprite extends Component implements Comparable<ParallaxSprite> {
	private final int DELTA_X = 1;
	private static final int NEAR_MAX_ELAPSED_TIME = 1;
	private static final int FAR_MAX_ELAPSED_TIME = 100 - NEAR_MAX_ELAPSED_TIME;
	
	public static int MAX_DISTANCE = 0;
	
	private Sprite spriteA;
	private Sprite spriteB;
	
	private Panel panelA;
	private Panel panelB;
	
	private int distance;
	
	private int elapsedTime;
	private final int maxElapsedTime;

	private boolean randomXPosition;
	private Random random;
	
	/**
	 * Constructs a ParallaxSprite with a context, spriteWidth, image, and maxOffset.
	 * @param context Context to use
	 * @param spriteWidth Width the sprite should be (e.g. for scaling)
	 * @param image Image to use for the sprite
	 * @param maxElapsedTime Amount of time to wait until the sprites move (larger means slower)
	 * @param randomXPosition Whether or not the sprite should be in a random position in the container
	 */
	public ParallaxSprite(GUIContext context, int spriteWidth, Image image, int distance, boolean randomXPosition) {
		super(context, context.getWidth(), spriteWidth * image.getHeight() / image.getWidth());
		
		this.randomXPosition = randomXPosition;
		
		maxElapsedTime = ((FAR_MAX_ELAPSED_TIME * distance) / MAX_DISTANCE) + NEAR_MAX_ELAPSED_TIME;
		
		random = new Random();
		
		this.distance = distance;
		
		spriteA = new Sprite(context, spriteWidth, image);
		spriteB = new Sprite(context, spriteWidth, image);
		
		panelA = new Panel(context, context.getWidth(), spriteA.getHeight());
		panelA.add(spriteA, getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT, getXOffset(panelA, spriteA), 0);
		
		panelB = new Panel(context, context.getWidth(), spriteB.getHeight());
		panelB.add(spriteB, getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT, getXOffset(panelB, spriteA), 0);
		
		add(panelA, getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT, 0, 0);
		add(panelB, panelA.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT, context.getWidth(), 0);
	}
	
	/**
	 * Moves the ParallaxSprite when the elapsedTime is greater than the maxElapsedTime. Loop images around when needed.
	 * @param delta Amount of time passed
	 */
	public void move(int delta) {
		elapsedTime += delta;
		
		if (elapsedTime > maxElapsedTime) {
			panelA.setLocation(panelA.getX() + DELTA_X, panelA.getY());
			panelB.setLocation(panelB.getX() + DELTA_X, panelB.getY());
			elapsedTime = 0;
		}
		
		if (panelA.getX() > container.getWidth()) {
			panelA.setLocation(panelB.getX() - container.getWidth(), panelA.getY());
			
			spriteA.setPosition(panelA.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT, getXOffset(panelA, spriteA), 0);
		}
		
		if (panelB.getX() > container.getWidth()) {
			panelB.setLocation(panelA.getX() - container.getWidth(), panelB.getY());
			
			spriteB.setPosition(panelB.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT, getXOffset(panelB, spriteB), 0);
		}
	}
	
	private int getXOffset(Panel panel, Sprite sprite) {
		if (randomXPosition) {
			return random.nextInt(panel.getWidth() - sprite.getWidth() + 1);
		}
		
		return 0;
	}
	
	public int getSpriteWidth() {
		return spriteA.getWidth();
	}
	
	public int getDistance() {
		return distance;
	}

	@Override
	public int compareTo(ParallaxSprite sprite) {
		return sprite.distance - this.distance;
	}
	
	public String toString() {
		return spriteA + " at " + distance;
	}
}
