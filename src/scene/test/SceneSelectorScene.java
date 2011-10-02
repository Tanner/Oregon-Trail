package scene.test;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import component.Background;
import component.Button;
import component.Label;
import core.FontManager;
import core.GameDirector;

import scene.Scene;
import scene.SceneID;
import scene.layout.GridLayout;

/**
 * Be able to switch to any scene from this scene.
 * 
 * @author Tanner Smith
 */
public class SceneSelectorScene extends Scene {
	private ButtonListener buttonListener;
	private Button mainMenuButton;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		mainLayer.setLayout(new GridLayout(container, 4, 4));
		
		UnicodeFont h2 = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.H2);
		Label mainMenuLabel = new Label(container, h2, Color.white, "Main\nMenu");
		mainMenuButton = new Button(container, mainMenuLabel, new Vector2f(0, 0));
		
		buttonListener = new ButtonListener();
		
		mainLayer.add(mainMenuButton);
		
		backgroundLayer.add(new Background(container, Color.blue));
		
		start();
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

	}
	
	public void start() {
		mainMenuButton.addListener(buttonListener);
	}
	
	public void pause() {
		mainMenuButton.removeListener(buttonListener);
	}
	
	public void end() {
		mainMenuButton.removeListener(buttonListener);
	}
	
	private class ButtonListener implements ComponentListener {

		@Override
		public void componentActivated(AbstractComponent component) {
			if (component == mainMenuButton) {
				GameDirector.sharedSceneDelegate().requestScene(SceneID.MainMenu);
			}
		}
	}
}
