package entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class ImageRenderComponent extends RenderComponent {
	protected Image image;

	public ImageRenderComponent(String id, String imagePath) {
		super(id);
		try {
			this.image = new Image(imagePath, false, Image.FILTER_NEAREST);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		Vector2f pos = owner.getPosition();
		float scale = owner.getScale();

		image.draw((int)pos.x, (int)pos.y, scale);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) {
		image.rotate(owner.getRotation() - image.getRotation());
	}

	@Override
	public float getWidth() {
		return image.getWidth();
	}

	@Override
	public float getHeight() {
		return image.getHeight();
	}
}
