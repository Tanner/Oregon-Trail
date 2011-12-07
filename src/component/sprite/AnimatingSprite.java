package component.sprite;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

public class AnimatingSprite extends Sprite {
	private static final int MOVE_SPEED_REDUCTION = 6;
	
	private Animation leftAnimation;
	private Animation rightAnimation;
	private Animation upAnimation;
	private Animation downAnimation;
	private Animation upperLeftAnimation;
	private Animation upperRightAnimation;
	private Animation lowerLeftAnimation;
	private Animation lowerRightAnimation;
	private Animation currentAnimation;
	/**whether this sprite has 8 directions of movement*/
	private boolean isEightDirectional = false;
	/**whether this sprite is currently animating or not*/
	private boolean isMoving = false;
	
	public static enum Direction { LEFT, RIGHT, FRONT, BACK, UPPER_LEFT, UPPER_RIGHT, LOWER_LEFT, LOWER_RIGHT }
	private Direction xDirection = Direction.LEFT;

	/**
	 * Constructs an {@code AnimatingSprite} with a right, left, up and down animation.
	 * @param context The GUI context
	 * @param width The width
	 * @param animation Animation for when facing left
	 * @param direction Direction Direction facing
	 */
	public AnimatingSprite(GUIContext context, int width, 
			Animation leftAnimation, Animation rightAnimation, 
			Animation downAnimation, Animation upAnimation, 
			Animation upperLeftAnimation, Animation upperRightAnimation, 
			Animation lowerLeftAnimation, Animation lowerRightAnimation, 
			Direction direction) {
		super(context, width, width * leftAnimation.getHeight() / leftAnimation.getWidth());
		
		this.leftAnimation = leftAnimation;
		this.rightAnimation = rightAnimation;
		this.upAnimation = upAnimation;
		this.downAnimation = downAnimation;
		this.upperLeftAnimation = upperLeftAnimation;
		this.upperRightAnimation = upperRightAnimation;
		this.lowerLeftAnimation = lowerLeftAnimation;
		this.lowerRightAnimation = lowerRightAnimation;
		
		leftAnimation.setAutoUpdate(false);
		rightAnimation.setAutoUpdate(false);
		upAnimation.setAutoUpdate(false);
		downAnimation.setAutoUpdate(false);
		upperLeftAnimation.setAutoUpdate(false);
		upperRightAnimation.setAutoUpdate(false);
		lowerLeftAnimation.setAutoUpdate(false);
		lowerRightAnimation.setAutoUpdate(false);
		
		//removes warnings until animation coding is finally handled.
		this.upAnimation.getWidth(); 
		this.downAnimation.getWidth(); 
		this.upperLeftAnimation.getWidth(); 
		this.upperRightAnimation.getWidth(); 
		this.lowerLeftAnimation.getWidth(); 
		this.lowerRightAnimation.getWidth(); 
		this.isEightDirectional = true;
	
		xDirection = Direction.FRONT;
		currentAnimation = downAnimation;
	}	
	/**
	 * Constructs an {@code AnimatingSprite} with a right and left animation.
	 * @param context The GUI context
	 * @param width The width
	 * @param animation Animation for when facing left
	 * @param direction Direction Direction facing
	 */
	public AnimatingSprite(GUIContext context, int width, Animation animation, Direction direction) {
		super(context, width, width * animation.getHeight() / animation.getWidth());
		
		animation.setAutoUpdate(false);
		
		Animation reflectedAnimation = new Animation();
		for (int i = 0; i < animation.getFrameCount(); i++) {
			reflectedAnimation.addFrame(animation.getImage(i).getFlippedCopy(true, false), animation.getDuration(i));
		}
		reflectedAnimation.setAutoUpdate(false);
		
		if (direction == Direction.LEFT) {
			this.leftAnimation = animation;
			this.rightAnimation = reflectedAnimation;
		} else {
			this.leftAnimation = reflectedAnimation;
			this.rightAnimation = animation;
		}
		
		xDirection = Direction.LEFT;
		currentAnimation = leftAnimation;
	}
	
	/**
	 * Constructs an {@code AnimatingSprite} with a right and left animation.
	 * @param context The GUI context
	 * @param width The width
	 * @param animation Animation for when facing left
	 * @param direction Direction Direction facing
	 */
	public AnimatingSprite(GUIContext context, int width, Animation leftAnimation, Animation rightAnimation, Direction direction) {
		super(context, width, width * leftAnimation.getHeight() / leftAnimation.getWidth());
		
		this.leftAnimation = leftAnimation;
		this.rightAnimation = rightAnimation;
		
		leftAnimation.setAutoUpdate(false);
		rightAnimation.setAutoUpdate(false);
		
		xDirection = Direction.LEFT;
		currentAnimation = leftAnimation;
	}
	
	/**
	 * Updates the Sprite on a clock cycle.
	 * @param container Container displaying the component
	 * @param delta Time since last update
	 */
	public void update(int delta) {
		super.update(delta);
		
		if (isEightDirectional){
			//sprite has 8 directions of possible movement
			switch (xDirection) {
				case LEFT :
					currentAnimation = leftAnimation;
					break;
				case RIGHT : 
					currentAnimation = rightAnimation;
					break;
				case FRONT : 
					currentAnimation = downAnimation;
					break;
				case BACK :
					currentAnimation = upAnimation;
					
					break;
				case UPPER_LEFT :
					currentAnimation = upperLeftAnimation;
					
					break;
				case UPPER_RIGHT :
					currentAnimation = upperRightAnimation;
					
					break;
				case LOWER_LEFT : 
					currentAnimation = lowerLeftAnimation;
					
					break;
				case LOWER_RIGHT : 
					currentAnimation = lowerRightAnimation;
					
					break;
				default :
					currentAnimation = downAnimation;
					break;		
			}
			
//			if (isMoving){
//				currentAnimation.setAutoUpdate(true);
//			} else {
//				currentAnimation.setAutoUpdate(false);	
//			}
			
			
		} else {
		//sprite does not have 8 directional animation
			if (xDirection == Direction.LEFT) {
				currentAnimation = leftAnimation;
			} else {
				currentAnimation = rightAnimation;
			}
		}
		
		currentAnimation.update(delta);
	}//update method
	

	/**
	 * @return the isMoving
	 */
	public boolean isMoving() {
		return isMoving;
	}
	/**
	 * @param isMoving the isMoving to set
	 */
	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}

	
	@Override
	public void render(GUIContext context, Graphics g) throws SlickException {
		if (!isVisible()) {
			return;
		}
				
		if (currentAnimation != null) {
			currentAnimation.draw(getX(), getY(), getWidth(), getHeight());
		}
		
		super.render(context, g);
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

	public void moveLeft(int delta) {
		delta /= MOVE_SPEED_REDUCTION;
		setLocation(getX() - delta, getY());
		
		setDirectionFacing(Direction.LEFT);
	}

	public void moveRight(int delta) {
		delta /= MOVE_SPEED_REDUCTION;
		setLocation(getX() + delta, getY());
		
		setDirectionFacing(Direction.RIGHT);
	}
}
