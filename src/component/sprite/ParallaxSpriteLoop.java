package component.sprite;

import org.newdawn.slick.Image;
import org.newdawn.slick.gui.GUIContext;

import component.Panel;

public class ParallaxSpriteLoop extends ParallaxSprite {
	private Sprite sprite;
	private Panel panel;
	
	/**
	 * Constructs a ParallaxSprite with a context, spriteWidth, and image. Sprite can have a random X position.
	 * @param context Context to use
	 * @param spriteWidth Width the sprite should be (e.g. for scaling)
	 * @param image Image to use for the sprite
	 * @param distance What the distance this sprite should be
	 * @param randomXPosition Whether or not the sprite should be in a random position in the container
	 */
	public ParallaxSpriteLoop(GUIContext context, int spriteWidth, Image image, int distance) {
		super(context, spriteWidth, image, distance);
		
		sprite = new Sprite(context, spriteWidth, image);
		
		panel = new Panel(context, context.getWidth(), sprite.getHeight());
		panel.add(sprite, getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT, 0, 0);
		
		add(panel, super.panel.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMRIGHT, 0, 0);
	}
	
	@Override
	public void move(int delta) {
		elapsedTime += delta;
		
		if (elapsedTime > maxElapsedTime) {
			super.panel.setLocation(super.panel.getX() + DELTA_X, super.panel.getY());
			panel.setLocation(panel.getX() + DELTA_X, panel.getY());
			elapsedTime = 0;
		}
		
		if (super.panel.getX() > container.getWidth()) {
			super.panel.setLocation(0 - panel.getWidth(), super.panel.getY());
		}
		
		if (panel.getX() > container.getWidth()) {
			panel.setLocation(0 - super.panel.getWidth(), panel.getY());
		}
	}
}
