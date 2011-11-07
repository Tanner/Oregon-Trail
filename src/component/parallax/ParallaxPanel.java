package component.parallax;

import java.util.ArrayList;
import java.util.Collections;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

import component.Component;
import component.Panel;

/**
 * Panel that has a sorted list of {@code ParallaxSprite}.
 */
public class ParallaxPanel extends Panel {
	private ArrayList<ParallaxComponent> parallaxComponents;
	
	/**
	 * Construct a ParallaxPanel with a context, width, and height.
	 * @param context Context
	 * @param width Width
	 * @param height Height
	 */
	public ParallaxPanel(GUIContext context, int width, int height) {
		super(context, width, height);
		
		parallaxComponents = new ArrayList<ParallaxComponent>();
	}
	
	@Override
	public void render(GUIContext context, Graphics g) throws SlickException {
		super.render(container, g);
	}
	
	@Override
	public void update(int delta) {
		super.update(delta);
		
		for (int i = parallaxComponents.size() - 1; i >= 0; i--) {
			ParallaxComponent pc = parallaxComponents.get(i);
			pc.update(delta);
			
			if (pc.isExpired()) {
				parallaxComponents.remove(i);
				remove(pc);
			}
		}
	}
	
	@Override
	public void add(Component component, Vector2f location, ReferencePoint referencePoint, int xOffset, int yOffset) {
		if (!(component instanceof ParallaxComponent)) {
			return;
		}
		
		super.add(component, location, referencePoint, xOffset, yOffset);
		
		ParallaxComponent parallaxComponent = (ParallaxComponent) component;
		parallaxComponents.add(parallaxComponent);
		
		Collections.sort(parallaxComponents);
		
		for (int i = components.size() - 1; i >= 0; i--) {
			remove(components.get(i));
		}
		for (Component c : parallaxComponents) {
			components.add(c);
			c.setVisibleParent(this);
		}
	}
	
	/**
	 * Get the list of all the {@code ParallaxComponents}.
	 * @return List of Parallax COmponents
	 */
	public ArrayList<ParallaxComponent> getParallaxComponents() {
		return parallaxComponents;
	}
}
