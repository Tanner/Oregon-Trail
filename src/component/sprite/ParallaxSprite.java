package component.sprite;

import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.Image;

import component.Component;
import component.Panel;

/**
 * A sprite that shifts a certain amount on the screen and loops around.
 */
public class ParallaxSprite extends Component {
	private Sprite spriteA;
	private Sprite spriteB;
	
	private Panel panelA;
	private Panel panelB;
	
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
		
		panelA = new Panel(context, spriteA.getWidth(), spriteA.getHeight());
		panelA.add(spriteA, getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
		
		panelB = new Panel(context, spriteB.getWidth(), spriteB.getHeight());
		panelB.add(spriteB, getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
		
		add(panelA, getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT);
		add(panelB, panelA.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT, context.getWidth(), 0);
		
		this.maxElapsedTime = maxElapsedTime;
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
		}
		
		if (panelB.getX() > container.getWidth()) {
			panelB.setLocation(panelA.getX() - container.getWidth(), panelB.getY());
		}
	}
}
