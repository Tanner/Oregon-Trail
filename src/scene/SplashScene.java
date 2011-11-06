package scene;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import component.Positionable.ReferencePoint;
import component.sprite.Sprite;

import core.GameDirector;

public class SplashScene extends Scene {
	public static final SceneID ID = SceneID.SPLASH;
	
	private static final int WAIT_TIME = 1000;
	private int time;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container,  game);
		
		Sprite logo = new Sprite(container, new Image("resources/graphics/splash_screen.png", false, Image.FILTER_NEAREST));
		mainLayer.add(logo, mainLayer.getPosition(ReferencePoint.CENTERCENTER), ReferencePoint.CENTERCENTER);
		
		time = 0;
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		time += delta;
		
		if (time > WAIT_TIME) {
			GameDirector.sharedSceneListener().requestScene(SceneID.LOADING, this, true);
		}
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
}