package entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class FlippableImageRenderComponent extends ImageRenderComponent {
	private Image leftImage;
	private Image rightImage;

	public FlippableImageRenderComponent(String id, String rightImagePath) {
		super(id, rightImagePath);
		try {
			this.rightImage = new Image(rightImagePath, false, Image.FILTER_NEAREST);
			this.leftImage = rightImage.getFlippedCopy(true, false);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) {
		
		
		if (owner.getXDirection() >= 0) {
			image = rightImage;
		} else {
			image = leftImage;
		}
		
		super.update(container, game, delta);
	}
}
