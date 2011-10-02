package scene.test;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import component.Background;
import component.Button;
import component.Label;
import core.FontManager;
import core.GameDirector;

import scene.Scene;
import scene.layout.GridLayout;

/**
 * Be able to switch to any scene from this scene.
 * 
 * @author Tanner Smith
 */
public class SceneSelectorScene extends Scene {
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		mainLayer.setLayout(new GridLayout(container, 4, 4));
		
		UnicodeFont h2 = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.H2);
		Label mainMenuLabel = new Label(container, 0, 0, h2, Color.white, "Main Menu");
		Button mainMenuSceneButton = new Button(container, mainMenuLabel, new Vector2f(0, 0));
		
		mainLayer.add(mainMenuSceneButton);
		
		backgroundLayer.add(new Background(container, Color.blue));
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

	}
}
