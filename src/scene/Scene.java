package scene;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public abstract class Scene extends BasicGameState {
	protected SceneLayer backgroundLayer;
	protected SceneLayer mainLayer;
	protected SceneLayer hudLayer;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		backgroundLayer = new SceneLayer(container);
		mainLayer = new SceneLayer(container);
		hudLayer = new SceneLayer(container);
	}

	@Override
	public final void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		backgroundLayer.render(container, g);
		mainLayer.render(container, g);
		hudLayer.render(container, g);
	}

	@Override
	public abstract void update(GameContainer container, StateBasedGame game, int delta) throws SlickException;
	
	public void start() {
		
	}
	
	public void pause() {
		
	}
	
	public void stop() {
		
	}
	
	@Override
	public abstract int getID();
}
