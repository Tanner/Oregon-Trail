package component.sprite;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.gui.GUIContext;

public class AnimatingSprite extends Sprite {
	private Animation leftAnimation;
	private Animation rightAnimation;
	private Animation currentAnimation;
	
	public static enum Direction { LEFT, RIGHT }
	private Direction xDirection = Direction.LEFT;
	
	/**
	 * Constructs a {@code Sprite} with a right and left animation.
	 * @param context The GUI context
	 * @param leftAnimation Animation for when facing left
	 */
	public AnimatingSprite(GUIContext context, Animation animation, Direction direction) {
		super(context, animation.getWidth(), animation.getHeight());
		
		Animation reflectedAnimation = new Animation();
		for (int i = 0; i < animation.getFrameCount(); i++) {
			reflectedAnimation.addFrame(animation.getImage(i).getFlippedCopy(true, false), animation.getDuration(i));
		}
		
		if (direction == Direction.LEFT) {
			this.leftAnimation = animation;
			this.rightAnimation = reflectedAnimation;
		} else {
			this.leftAnimation = reflectedAnimation;
			this.rightAnimation = animation;
		}
		
		xDirection = Direction.LEFT;
		currentAnimation = leftAnimation;

		setImage(currentAnimation.getCurrentFrame());
	}
	
	/**
	 * Updates the Sprite on a clock cycle.
	 * @param container Container displaying the component
	 * @param game Game containing the Entity
	 * @param delta Time since last update
	 */
	public void update(GameContainer container, int delta) {
		if (xDirection == Direction.LEFT) {
			currentAnimation = leftAnimation;
		} else {
			currentAnimation = rightAnimation;
		}
		
		setImage(currentAnimation.getCurrentFrame());

		currentAnimation.update(delta);
	}
	
	/**
	 * Get which direction the Sprite is facing.
	 * @return Direction the Sprite is facing
	 */
	public Direction getDirectionFacing() {
		return xDirection;
	}
	
	/**
	 * Sets the direction the Sprite is facing
	 * @param xDirection New direction the Sprite is facing
	 */
	public void setDirectionFacing(Direction xDirection) {
		this.xDirection = xDirection;
	}
}
