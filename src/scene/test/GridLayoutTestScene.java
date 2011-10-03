package scene.test;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import scene.Scene;
import scene.SceneID;
import component.Background;
import component.Label;
import core.FontManager;
import core.GameDirector;
import scene.layout.GridLayout;
import sprite.Sprite;

public class GridLayoutTestScene extends Scene {
	public static final SceneID ID = SceneID.GridLayoutTest;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		mainLayer.setLayout(new GridLayout(container, 2, 2));
		
		Image marioImage = new Image("resources/mario.png");
		Sprite mario = new Sprite(container, marioImage, marioImage.getFlippedCopy(true, false));
		
		Image luigiImage = new Image("resources/luigi.png");
		Sprite luigi = new Sprite(container, luigiImage, luigiImage.getFlippedCopy(true, false));
		
		Label label = new Label(container, GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.H1), Color.white, "foo");
		
		mainLayer.add(label);
		mainLayer.add(mario);
		mainLayer.add(luigi);
		
		backgroundLayer.add(new Background(container, Color.blue));
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
	}
	
	@Override
	public int getID() {
		return ID.ordinal();
	}
}
