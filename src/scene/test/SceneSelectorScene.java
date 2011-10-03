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
	public static final SceneID ID = SceneID.SceneSelector;

	private ButtonListener buttonListener;
	private List<Button> buttons;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		mainLayer.setLayout(new GridLayout(container, 4, 4));
		
		buttons = new ArrayList<Button>();
		
		UnicodeFont fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);
		Label mainMenuLabel = new Label(container, fieldFont, Color.white, "Main\nMenu");
		Button mainMenuButton = new Button(container, mainMenuLabel, new Vector2f(0, 0));
		buttons.add(mainMenuButton);
		Label partyCreationLabel = new Label(container, fieldFont, Color.white, "Party\nCreation");
		Button partyCreationButton = new Button(container, partyCreationLabel, new Vector2f(0, 0));
		buttons.add(partyCreationButton);
		Label townLabel = new Label(container, fieldFont, Color.white, "Town");
		Button townButton = new Button(container, townLabel, new Vector2f(0, 0));
		buttons.add(townButton);
		Label storeLabel = new Label(container, fieldFont, Color.white, "Store");
		Button storeButton = new Button(container, storeLabel, new Vector2f(0, 0));
		buttons.add(storeButton);
		Label componentsLabel = new Label(container, fieldFont, Color.white, "Components");
		Button componentsButton = new Button(container, componentsLabel, new Vector2f(0, 0));
		buttons.add(componentsButton);
		
		buttonListener = new ButtonListener();
		
		for (Button b : buttons) {
			mainLayer.add(b);
		}
		
		backgroundLayer.add(new Background(container, Color.black));
		
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
				GameDirector.sharedSceneDelegate().requestScene(SceneID.PartyCreation);
			} else if (component == buttons.get(2)) {
				GameDirector.sharedSceneDelegate().requestScene(SceneID.Town);
			} else if (component == buttons.get(3)) {
				GameDirector.sharedSceneDelegate().requestScene(SceneID.Store);
			} else if (component == buttons.get(4)) {
				GameDirector.sharedSceneDelegate().requestScene(SceneID.ComponentTest);
			}
			
		}
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
}
