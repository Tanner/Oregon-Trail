package scene.test;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import scene.Scene;
import component.Label;
import core.FontManager;
import core.GameDirector;
import scene.layout.GridLayout;
import sprite.Sprite;

public class GridLayoutTestScene extends Scene {

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		this.layout = new GridLayout(container, 2, 2);
		
		Image marioImage = new Image("resources/mario.png");
		Sprite mario = new Sprite(container, marioImage, marioImage.getFlippedCopy(true, false));
		
		Image luigiImage = new Image("resources/luigi.png");
		Sprite luigi = new Sprite(container, luigiImage, luigiImage.getFlippedCopy(true, false));
		
		Label label = new Label(container, 0, 0, GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.H1), Color.white, "foo");
		add(label);
		
		add(mario);
		add(luigi);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		super.render(container, game, g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
	}
}
