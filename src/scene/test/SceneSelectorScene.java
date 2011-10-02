package scene.test;

import java.util.ArrayList;
import java.util.List;

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
	public static final SceneID ID = SceneID.SceneSelectorScene;

	private ButtonListener buttonListener;
	private List<Button> buttons;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		mainLayer.setLayout(new GridLayout(container, 4, 4));
		
		buttons = new ArrayList<Button>();
		
		UnicodeFont h2 = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.H2);
		Label mainMenuLabel = new Label(container, h2, Color.white, "Main\nMenu");
		Button mainMenuButton = new Button(container, mainMenuLabel, new Vector2f(0, 0));
		buttons.add(mainMenuButton);
		Label townLabel = new Label(container, h2, Color.white, "Town");
		Button townButton = new Button(container, townLabel, new Vector2f(0, 0));
		buttons.add(townButton);
		Label storeLabel = new Label(container, h2, Color.white, "Store");
		Button storeButton = new Button(container, storeLabel, new Vector2f(0, 0));
		buttons.add(storeButton);
		Label componentsLabel = new Label(container, h2, Color.white, "Components");
		Button componentsButton = new Button(container, componentsLabel, new Vector2f(0, 0));
		buttons.add(componentsButton);
		
		buttonListener = new ButtonListener();
		
		for (Button b : buttons) {
			mainLayer.add(b);
		}
		
		backgroundLayer.add(new Background(container, Color.blue));
		
		start();
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

	}
	
	public void start() {
		for (Button b : buttons) {
			b.addListener(buttonListener);
		}
	}
	
	public void pause() {
		for (Button b : buttons) {
			b.removeListener(buttonListener);
		}
	}
	
	public void end() {
		for (Button b : buttons) {
			b.removeListener(buttonListener);
		}
	}
	
	private class ButtonListener implements ComponentListener {

		@Override
		public void componentActivated(AbstractComponent component) {
			if (component == buttons.get(0)) {
				GameDirector.sharedSceneDelegate().requestScene(SceneID.MainMenu);
			} else if (component == buttons.get(1)) {
				GameDirector.sharedSceneDelegate().requestScene(SceneID.TownScene);
			} else if (component == buttons.get(2)) {
				GameDirector.sharedSceneDelegate().requestScene(SceneID.StoreScene);
			} else if (component == buttons.get(3)) {
				GameDirector.sharedSceneDelegate().requestScene(SceneID.ComponentTestScene);
			}
			
		}
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
}
