package component.sprite;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.gui.GUIContext;

public class AnimatingSprite extends Sprite {
	private Animation leftAnimation;
	private Animation rightAnimation;
	private Animation currentAnimation;
	
	public static enum DirectionFacing { LEFT, RIGHT }
	private DirectionFacing xDirection = DirectionFacing.LEFT;
	
	/**
	 * Constructs a {@code Sprite} with a right and left animation.
	 * @param context The GUI context
	 * @param rightAnimation Animation for when facing right
	 * @param leftAnimation Animation for when facing left
	 */
	public AnimatingSprite(GUIContext context, Animation rightAnimation, Animation leftAnimation) {
		super(context, rightAnimation.getWidth(), rightAnimation.getHeight());
				
		this.rightAnimation = rightAnimation;
		this.leftAnimation = leftAnimation;
		
		xDirection = DirectionFacing.LEFT;
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
		if (xDirection == DirectionFacing.LEFT) {
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
	public DirectionFacing getDirectionFacing() {
		return xDirection;
	}
	
	/**
	 * Sets the direction the Sprite is facing
	 * @param xDirection New direction the Sprite is facing
	 */
	public void setDirectionFacing(DirectionFacing xDirection) {
		this.xDirection = xDirection;
	}
}
