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
	protected SceneLayer modalLayer;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		backgroundLayer = new SceneLayer(container);
		mainLayer = new SceneLayer(container);
		hudLayer = new SceneLayer(container);
		modalLayer = new SceneLayer(container);
	}

	@Override
	public final void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		backgroundLayer.render(container, g);
		mainLayer.render(container, g);
		hudLayer.render(container, g);
		modalLayer.render(container, g);
	}

	@Override
	public abstract void update(GameContainer container, StateBasedGame game, int delta) throws SlickException;
	
	/**
	 * Things to do when the Scene is started.
	 */
	public void start() {
		return;
	}
	
	/**
	 * Things to do when the Scene is paused.
	 */
	public void pause() {
		return;
	}
	
	/**
	 * Things to do when the Scene is stopped.
	 */
	public void stop() {
		return;
	}
	
	@Override
	public abstract int getID();
}
