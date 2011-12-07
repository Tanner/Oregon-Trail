package component.sprite;

import org.newdawn.slick.Animation;
import org.newdawn.slick.gui.GUIContext;

public class PreyAnimatingSprite extends AnimatingSprite {

	public PreyAnimatingSprite(GUIContext context, 
			Animation leftAnimation, Animation rightAnimation, 
			Animation downAnimation, Animation upAnimation, 
			Direction direction) {
		super(context, leftAnimation.getWidth(), leftAnimation.getHeight());
			
		this.leftAnimation = leftAnimation;
		this.rightAnimation = rightAnimation;
		this.upAnimation = upAnimation;
		this.downAnimation = downAnimation;
		
		leftAnimation.setAutoUpdate(false);
		rightAnimation.setAutoUpdate(false);
		upAnimation.setAutoUpdate(false);
		downAnimation.setAutoUpdate(false);
		
		//removes warnings until animation coding is finally handled.
		this.upAnimation.getWidth(); 
		this.downAnimation.getWidth(); 
		this.leftAnimation.getWidth(); 
		this.rightAnimation.getWidth(); 
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


}//preyAnimatingSprite method
