package scene;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import component.HUD;
import component.Component;
import component.Modal;
import component.ModalListener;
import component.Tooltip;
import component.Positionable.ReferencePoint;

/**
 * How the game displays information to the player.  Inherited by the containers which execute the game functionality
 */
public abstract class Scene extends BasicGameState implements ModalListener {
	protected static GameContainer container;	
	private static Tooltip tooltip;
	
	protected SceneLayer backgroundLayer;
	protected SceneLayer mainLayer;
	protected SceneLayer hudLayer;
	protected SceneLayer modalLayer;
	
	private boolean active;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		Scene.container = container;
		
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
		
		if (tooltip != null) {
			if (tooltip.getOwner().isMouseOver()) {
				tooltip.render(container, g);
			} else {
				removeTooltip();
			}
		}
	}

	@Override
	public abstract void update(GameContainer container, StateBasedGame game, int delta) throws SlickException;

	/**
	 * Activates a modal screen 
	 * @param modal the screen to be displayed via modality
	 */
	public void showModal(Modal modal) {
		removeTooltip();
		
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
		removeTooltip();
		
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
	
	@Override
	public void enter(GameContainer container, StateBasedGame game)  {
		mainLayer.setAcceptingInput(true);
		modalLayer.setAcceptingInput(true);
		setActive(true);
	}
	
	@Override
	public void leave(GameContainer container, StateBasedGame game)  {
		mainLayer.setAcceptingInput(false);
		modalLayer.setAcceptingInput(false);
		setActive(false);
		
		removeTooltip();
	}
	
	@Override
	public abstract int getID();
	
	public static void showTooltip(int x, int y, Component owner, String message) {
		if (tooltip != null) {
			if (tooltip.getOwner() != owner && owner.isVisible()) {
				tooltip = new Tooltip(container, owner, message);
			}
			
			if (tooltip.getOwner().isVisible()) {
				setTooltipPosition(x, y);
			} else{
				removeTooltip();
			}
		} else if (owner.isVisible()){
			tooltip = new Tooltip(container, owner, message);
			setTooltipPosition(x, y);
		}
	}
	
	public static void removeTooltip() {
		tooltip = null;
	}
	
	private static void setTooltipPosition(int x, int y) {
		ReferencePoint referencePoint = ReferencePoint.TOPLEFT;
		
		final int BOTTOM_OFFSET = 20;
		final int TOP_OFFSET = 10;
		
		if (x + tooltip.getWidth() >= container.getWidth()) {
			if (y + BOTTOM_OFFSET + tooltip.getHeight() >= container.getHeight()) {
				referencePoint = ReferencePoint.BOTTOMRIGHT;
				y -= TOP_OFFSET;
			} else {
				referencePoint = ReferencePoint.TOPRIGHT;
				y += BOTTOM_OFFSET;
			}
		} else {
			if (y + BOTTOM_OFFSET + tooltip.getHeight() >= container.getHeight()) {
				referencePoint = ReferencePoint.BOTTOMLEFT;
				y -= TOP_OFFSET;
			} else {
				referencePoint = ReferencePoint.TOPLEFT;
				y += BOTTOM_OFFSET;
			}
		}
		
		tooltip.setPosition(new Vector2f(x, y), referencePoint);
	}

	public boolean isActive() {
		return active;
	}

	private void setActive(boolean active) {
		this.active = active;
	}
	
	/*
	public void setMusic(Music music) {
		if (this.music == null) {
			this.music = music;
			GameDirector.sharedSceneListener().playMusic(music);	
		}
	}*/
}
