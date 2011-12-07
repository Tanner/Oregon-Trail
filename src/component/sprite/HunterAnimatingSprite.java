package component.sprite;

import org.newdawn.slick.Animation;
import org.newdawn.slick.gui.GUIContext;

public class HunterAnimatingSprite extends AnimatingSprite {

	/**
	 * Constructs an {@code AnimatingSprite} with a right, left, up and down and diagonal animation.
	 * @param context The GUI context
	 * @param width The width
	 * @param animation x8 Animation for when facing each of 8 directions
	 * @param direction Direction Direction facing
	 */

	public HunterAnimatingSprite(GUIContext context, Animation leftAnimation,
			Animation rightAnimation, Animation downAnimation,
			Animation upAnimation, Animation upperLeftAnimation,
			Animation upperRightAnimation, Animation lowerLeftAnimation,
			Animation lowerRightAnimation, Direction direction) {

			super(context, leftAnimation.getWidth(), leftAnimation.getHeight());
			
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
			this.leftAnimation.getWidth(); 
			this.rightAnimation.getWidth(); 
			this.upperLeftAnimation.getWidth(); 
			this.upperRightAnimation.getWidth(); 
			this.lowerLeftAnimation.getWidth(); 
			this.lowerRightAnimation.getWidth(); 
		
			xDirection = Direction.FRONT;
			currentAnimation = downAnimation;
		}//constructor
	
	/**
	 * Updates the Sprite on a clock cycle.
	 * @param container Container displaying the component
	 * @param delta Time since last update
	 */
	public void update(int delta) {
		super.update(delta);
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
			
			if (isMoving){
				currentAnimation.setAutoUpdate(true);			
				} else {
				currentAnimation.setAutoUpdate(false);	
			}
			
		currentAnimation.update(delta);
	}//update method

}//HunterAnimatingSprite class
