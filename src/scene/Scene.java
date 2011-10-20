package scene;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import component.HUD;
import component.Modal;
import component.ModalListener;

/**
 * How the game displays information to the player.  Inherited by the containers which execute the game functionality
 * @author NULL&&void
 *
 */
public abstract class Scene extends BasicGameState implements ModalListener {
	protected GameContainer container;
	protected SceneLayer backgroundLayer;
	protected SceneLayer mainLayer;
	protected SceneLayer hudLayer;
	protected SceneLayer modalLayer;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		this.container = container;
		
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
	 * Activates a modal screen 
	 * @param modal the screen to be displayed via modality
	 */
	public void showModal(Modal modal) {
		modalLayer.add(modal);
		
		backgroundLayer.setAcceptingInput(false);
		mainLayer.setAcceptingInput(false);
		hudLayer.setAcceptingInput(false);
		
		modalLayer.setAcceptingInput(true);
		modalLayer.setVisible(true);
	}
	
	/**
	 * Deactivates a modal screen
	 * @param the modal by which the activation shall be not
	 */
	public void dismissModal(Modal modal, boolean cancelled) {
		// Make Modal invisible and set accepting input to false before removing it!
		modalLayer.setVisible(false);
		modalLayer.setAcceptingInput(false);
		modalLayer.remove(modal);
		
		backgroundLayer.setAcceptingInput(true);
		mainLayer.setAcceptingInput(true);
		hudLayer.setAcceptingInput(true);
	}
	
	public void showHUD(HUD hud) {
		hudLayer.add(hud);
	}
	
	/**
	 * Things to do when the Scene is started.
	 */
	public void start() {
		mainLayer.setAcceptingInput(true);
		modalLayer.setAcceptingInput(true);
	}
	
	/**
	 * Things to do when the Scene is paused.
	 */
	public void pause() {
		mainLayer.setAcceptingInput(false);
		modalLayer.setAcceptingInput(false);
	}
	
	/**
	 * Things to do when the Scene is stopped.
	 */
	public void stop() {
		mainLayer.setAcceptingInput(false);
		modalLayer.setAcceptingInput(false);
	}
	
	@Override
	public abstract int getID();
}
