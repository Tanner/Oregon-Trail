package scene;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import component.Modal;


public abstract class Scene extends BasicGameState {
	protected GameContainer container;
	protected SceneLayer backgroundLayer;
	protected SceneLayer mainLayer;
	protected SceneLayer hudLayer;
	protected ModalSceneLayer modalLayer;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		this.container = container;
		
		backgroundLayer = new SceneLayer(container);
		mainLayer = new SceneLayer(container);
		hudLayer = new SceneLayer(container);
		modalLayer = new ModalSceneLayer(container);
		modalLayer.setVisible(false);
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
	
	public void showModal(Modal modal) {
		modalLayer.add(modal);
		
		modalLayer.setVisible(true);
		backgroundLayer.setAcceptingInput(false);
		mainLayer.setAcceptingInput(false);
		hudLayer.setAcceptingInput(false);
	}
	
	public void closeModal(Modal modal) {
		// Make Modal invisible and set accepting input to false before removing it!
		modalLayer.setVisible(false);
		modalLayer.setAcceptingInput(false);
		
		modalLayer.remove(modal);
		
		backgroundLayer.setAcceptingInput(true);
		mainLayer.setAcceptingInput(true);
		hudLayer.setAcceptingInput(true);
	}
	
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
