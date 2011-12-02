package component;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.particles.effects.FireEmitter;

import core.ConstantStore;
public class Particle extends Component {

	private ParticleSystem system;
	private int deltaX, deltaY, distance;
	
	public Particle(GUIContext context, int deltaX, int deltaY, int distance) throws SlickException {
		super(context, context.getWidth(), context.getHeight());
		system = new ParticleSystem(new Image(ConstantStore.PATH_TEST + "particle.tga", true));
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		this.distance = distance;
	}
	
	public void addFire(int x, int y, int size) {
		system.addEmitter(new FireEmitter(x, y, size));
	}
	
	public void render(GUIContext context, Graphics g) throws SlickException {
		if (!isVisible()) {
			return;
		}

		for (int i = 0; i < distance; i++) {
			g.translate(deltaX, deltaY);
			system.render();
		}
		g.resetTransform();
		
		super.render(container, g);
	}
	
	public void update(int delta) {
		system.update(delta);
	}
}
