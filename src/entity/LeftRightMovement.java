package entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class LeftRightMovement extends Component {
	private float direction;
	private float speed;
 
	public LeftRightMovement(String id) {
		this.id = id;
	}
 
	public float getSpeed() {
		return speed;
	}
 
	public float getDirection() {
		return direction;
	}
 
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) {
		float scale = owner.getScale();
		Vector2f position = owner.getPosition();
		float moveDelta = delta / 2.0f;
		
		Input input = container.getInput();
		
//		// go up
//		if (input.isKeyDown(Input.KEY_W)) {			
//			position.y -= delta;
//			
//			// goes too far up
//			if (position.y + owner.getHeight() < 0) {
//				position.y = container.getHeight() + owner.getHeight();
//			}
//		}
//		// or go down
//		else if (input.isKeyDown(Input.KEY_S)) {
//			position.y += delta;
//			
//			// goes too far down
//			if (position.y > container.getHeight()) {
//				position.y = -owner.getHeight();
//			}
//		}
		
		// go right
		if (input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT)) {
			owner.setXDirection(1);
			position.x += moveDelta;
			
			// goes too far right
			if (position.x > container.getWidth()) {
				position.x = -owner.getWidth();
			}
		}
		// or go left
		else if (input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT)) {
			owner.setXDirection(-1);
			position.x -= moveDelta;
			
			// goes too far left
			if (position.x + owner.getWidth() < 0) {
				position.x = container.getWidth();
			}
		}
 
		owner.setPosition(position);
		owner.setScale(scale);
	}
}