package scene;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.StateBasedGame;

import component.Background;
import component.Label;
import component.Positionable;

import core.*;
import sprite.*;


public class TownScene extends Scene {
	public static final SceneID ID = SceneID.Town;
	
	private Sprite mario;
	private Sprite luigi;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		UnicodeFont h1 = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.H1);
		UnicodeFont h2 = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.H2);
		
		Label titleLabel = new Label(container, h1, Color.white, "Town");
		Label subtitleLabel = new Label(container, h2, Color.white, "Press Enter to Go to Store");
		
		mainLayer.add(titleLabel, mainLayer.getPosition(Positionable.ReferencePoint.CenterCenter), Positionable.ReferencePoint.BottomCenter);
		mainLayer.add(subtitleLabel, titleLabel.getPosition(Positionable.ReferencePoint.BottomCenter), Positionable.ReferencePoint.TopCenter, 0, 10);
		
		Image marioImage = new Image("resources/mario.png");
		mario = new Sprite(container, marioImage, marioImage.getFlippedCopy(true, false));
		mario.setLocation(0, container.getHeight() - mario.getHeight());
		
		Image luigiImage = new Image("resources/luigi.png");
		luigi = new Sprite(container, luigiImage, luigiImage.getFlippedCopy(true, false));
		luigi.setLocation(200, container.getHeight() - luigi.getHeight());
		
		mainLayer.add(mario);
		mainLayer.add(luigi);
		
		backgroundLayer.add(new Background(container, new Color(0x003e84)));
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		mario.update(container, game, delta);
		luigi.update(container, game, delta);
	}
	
	public void keyReleased(int key, char c) {
		if (key == Input.KEY_ENTER) {
			GameDirector.sharedSceneDelegate().requestScene(SceneID.Store);
		}
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
}
