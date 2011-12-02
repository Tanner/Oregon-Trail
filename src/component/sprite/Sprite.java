package component.sprite;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import component.Component;

/**
 * {@code Sprite} inherits from {@code Component} to extend features
 * to represent a sprite with an left and right animation.
 */
public class Sprite extends Component {
	private Image image;
	private int rotation;
	
	public Sprite(GUIContext context, int width, int height) {
		super(context, width, height);
		
		rotation = 0;
	}
	
	public Sprite(GUIContext context, int width, Image image) {
		this(context, width, width * image.getHeight() / image.getWidth());
		
		this.image = image;
		
		image.setCenterOfRotation(getWidth() / 2, getHeight() / 2);
	}
	
	public Sprite(GUIContext context, Image image) {
		this(context, image.getWidth(), image);
	}

	@Override
	public void render(GUIContext context, Graphics g) throws SlickException {
		if (!isVisible()) {
			return;
		}
				
		if (image != null) {
			image.draw(getX(), getY(), getWidth(), getHeight());
		}
		
		super.render(context, g);
	}
	
	public void setImage(Image image) {
		this.image = image;

		image.setCenterOfRotation(getWidth() / 2, getHeight() / 2);
	}
	
	@Override
	public String toString() {
		return image.getResourceReference();
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
		
		image.rotate(rotation);
	}

	public void rotate(int delta) {
		setRotation((getRotation() + delta) % 360);
	}
}
