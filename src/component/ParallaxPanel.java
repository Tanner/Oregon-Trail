package component;

import java.util.ArrayList;
import java.util.Collections;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

import component.sprite.ParallaxSprite;

/**
 * Panel that has a sorted list of {@code ParallaxSprite}.
 */
public class ParallaxPanel extends Panel {
	private ArrayList<ParallaxSprite> list;
	
	/**
	 * Construct a ParallaxPanel with a context, width, and height.
	 * @param context Context
	 * @param width Width
	 * @param height Height
	 */
	public ParallaxPanel(GUIContext context, int width, int height) {
		super(context, width, height);
		
		list = new ArrayList<ParallaxSprite>();
	}
	
	@Override
	public void add(Component component, Vector2f location, ReferencePoint referencePoint, int xOffset, int yOffset) {
		if (!(component instanceof ParallaxSprite)) {
			return;
		}
		
		super.add(component, location, referencePoint, xOffset, yOffset);
		
		ParallaxSprite sprite = (ParallaxSprite) component;
		
		list.add(sprite);
		
		Collections.sort(list);
		
		components.clear();
		components.addAll(list);
	}
	
	/**
	 * Get the list of all the {@code ParallaxSprite}.
	 * @return List of Parallax Sprites
	 */
	public ArrayList<ParallaxSprite> getSprites() {
		return list;
	}
}
