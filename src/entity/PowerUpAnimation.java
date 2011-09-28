package entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class PowerUpAnimation extends AnimationComponent {
	private int step;
	private static final int STEP_DURATION = 240;
	private static final int ELASTIC_STEP_DURATION = 60;
	private static final int FULL_SCALE_DELAY = 600;
	
	@Override
	public void stop() {		
		owner.setScale(1);
		step = 0;

		super.stop();
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) {
		step += delta;
		
		if (step >= 6*STEP_DURATION + FULL_SCALE_DELAY) {
			owner.setScale(1);
			stop();
		} else if (step >=5*STEP_DURATION + FULL_SCALE_DELAY) {
			owner.setScale(2);
		} else if (step >= 4*STEP_DURATION + FULL_SCALE_DELAY) {
			owner.setScale(3);
		} else if (step >= 3*STEP_DURATION) {
			owner.setScale(4);
		} else if (step >= 2*STEP_DURATION) {
			owner.setScale(3);
		} else if (step >= 2*STEP_DURATION - ELASTIC_STEP_DURATION) {
			owner.setScale(3.2f);
		} else if (step >= 1*STEP_DURATION) {
			owner.setScale(2);
		} else if (step >= 1*STEP_DURATION - ELASTIC_STEP_DURATION) {
			owner.setScale(2.2f);
		}
		
		Vector2f pos = owner.getPosition();
		pos.y = container.getHeight() - owner.getHeight();
	}
}
