package scene;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
//import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import component.AnimatingColor;
import component.Panel;
import component.Positionable.ReferencePoint;
import component.sprite.Sprite;

import core.GameDirector;
import core.ImageStore;

/**
 * A Scene with a short animated introduction displaying the null && void team
 * name.
 */
public class SplashScene extends Scene {
	public static final SceneID ID = SceneID.SPLASH;
	
	private static final int WAIT_TIME = 3000;
	private static final int DELAY = 100;
	private int time;
	
	private Panel whitePanel;
	private Panel blackPanel;
	private AnimatingColor whitePanelAnimatingColor;
	private AnimatingColor blackPanelAnimatingColor;
	private Sprite nullLogo;
	private Sprite voidLogo;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container,  game);
		
		whitePanel = new Panel(container, container.getWidth() / 2, container.getHeight());
		whitePanelAnimatingColor = new AnimatingColor(Color.black, Color.white, WAIT_TIME / 4 - DELAY);
		whitePanel.setBackgroundColor(whitePanelAnimatingColor);
		backgroundLayer.add(whitePanel, mainLayer.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
		
		blackPanel = new Panel(container, container.getWidth() / 2, container.getHeight());
		blackPanelAnimatingColor = new AnimatingColor(Color.black, Color.white, WAIT_TIME / 4 - DELAY);
		blackPanel.setBackgroundColor(blackPanelAnimatingColor);
		backgroundLayer.add(blackPanel, mainLayer.getPosition(ReferencePoint.TOPRIGHT), ReferencePoint.TOPRIGHT);
		
		nullLogo = new Sprite(container, ImageStore.get().IMAGES.get("NULL")); //new Image(ConstantStore.PATH_LOGO + "null.png", false, Image.FILTER_NEAREST));
		whitePanel.add(nullLogo, whitePanel.getPosition(ReferencePoint.CENTERRIGHT), ReferencePoint.CENTERRIGHT, -10, 0);
		nullLogo.setVisible(false);
		
		voidLogo = new Sprite(container, ImageStore.get().IMAGES.get("VOID"));  //new Image(ConstantStore.PATH_LOGO + "void.png", false, Image.FILTER_NEAREST));
		blackPanel.add(voidLogo, whitePanel.getPosition(ReferencePoint.CENTERRIGHT), ReferencePoint.CENTERLEFT, 10, 0);
		voidLogo.setVisible(false);
		
		time = 0;
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		time += delta;
		
		if (time >= WAIT_TIME) {
			GameDirector.sharedSceneListener().requestScene(SceneID.LOADING, this, true);
		} else if (time >= WAIT_TIME * 1 / 2 + DELAY) {
			voidLogo.setVisible(true);
			
			blackPanelAnimatingColor.update(delta);
		} else if (time >= WAIT_TIME / 4 + DELAY) {
			nullLogo.setVisible(true);
			whitePanelAnimatingColor.update(delta);
			
			blackPanelAnimatingColor = new AnimatingColor(Color.white, Color.black, WAIT_TIME / 4 - DELAY);
			blackPanel.setBackgroundColor(blackPanelAnimatingColor);
		} else if (time < WAIT_TIME / 4) {
			blackPanelAnimatingColor.update(delta);
		} 
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
}