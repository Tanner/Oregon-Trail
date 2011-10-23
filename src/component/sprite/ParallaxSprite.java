package component.sprite;

import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.Image;

import component.Component;

/**
 * A sprite that shifts a certain amount on the screen and loops around.
 */
public class ParallaxSprite extends Component {
	private Sprite spriteA;
	private Sprite spriteB;
	
	private int elapsedTime;
	private final int maxElapsedTime;
	
	private final int DELTA_X = 4;
	
	/**
	 * Constructs a ParallaxSprite with a context, spriteWidth, image, and maxOffset.
	 * @param context Context to use
	 * @param spriteWidth Width the sprite should be (e.g. for scaling)
	 * @param image Image to use for the sprite
	 * @param maxElapsedTime Amount of time to wait until the sprites move (larger means slower)
	 */
	public ParallaxSprite(GUIContext context, int spriteWidth, Image image, int maxElapsedTime) {
		super(context, context.getWidth(), spriteWidth * image.getHeight() / image.getWidth());
		
		spriteA = new Sprite(context, spriteWidth, image);
		spriteB = new Sprite(context, spriteWidth, image);
		
		add(spriteA, getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT);
		add(spriteB, spriteA.getPosition(ReferencePoint.BOTTOMRIGHT), ReferencePoint.BOTTOMLEFT);
		
		this.maxElapsedTime = maxElapsedTime;
	}
	
	/**
	 * Moves the ParallaxSprite when the elapsedTime is greater than the maxElapsedTime. Loop images around when needed.
	 * @param delta Amount of time passed
	 */
	public void move(int delta) {
		elapsedTime += delta;
		
		if (elapsedTime > maxElapsedTime) {
			spriteA.setLocation(spriteA.getX() + DELTA_X, spriteA.getY());
			spriteB.setLocation(spriteB.getX() + DELTA_X, spriteB.getY());
			elapsedTime = 0;
		}
		
		if (spriteA.getX() > container.getWidth()) {
			spriteA.setLocation(spriteB.getX() - container.getWidth(), spriteA.getY());
		}
		
		if (spriteB.getX() > container.getWidth()) {
			spriteB.setLocation(spriteA.getX() - container.getWidth(), spriteB.getY());
		}
	}
}
