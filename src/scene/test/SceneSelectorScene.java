package scene.test;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import component.Panel;
import component.Button;
import component.Component;
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
		
		Label mainMenuLabel = new Label(container, fieldFont, Color.white, "Main Menu");
		Button mainMenuButton = new Button(container, mainMenuLabel);
		buttons.add(mainMenuButton);
		
		Label partyCreationLabel = new Label(container, fieldFont, Color.white, "Party Creation");
		Button partyCreationButton = new Button(container, partyCreationLabel);
		buttons.add(partyCreationButton);
		
		Label townLabel = new Label(container, fieldFont, Color.white, "Town");
		Button townButton = new Button(container, townLabel);
		buttons.add(townButton);
		
		Label storeLabel = new Label(container, fieldFont, Color.white, "Store");
		Button storeButton = new Button(container, storeLabel);
		buttons.add(storeButton);
		
		Label partyInventorySceneLabel = new Label(container, fieldFont, Color.white, "Party Inventory Scene");
		Button partyInventorySceneButton = new Button(container, partyInventorySceneLabel);
		buttons.add(partyInventorySceneButton);
		
		Label componentsLabel = new Label(container, fieldFont, Color.white, "Components");
		Button componentsButton = new Button(container, componentsLabel);
		buttons.add(componentsButton);
		
		buttonListener = new ButtonListener();
		
		for (Button b : buttons) {
			mainLayer.add(b);
		}
		
		backgroundLayer.add(new Panel(container, Color.black));
		
		start();
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		return;
	}

	@Override
	public void start() {
		super.start();
		
		for (Button b : buttons) {
			b.addListener(buttonListener);
		}
	}

	@Override
	public void pause() {
		super.pause();
		
		for (Button b : buttons) {
			b.removeListener(buttonListener);
		}
	}

	@Override
	public void stop() {
		super.stop();
		
		for (Button b : buttons) {
			b.removeListener(buttonListener);
		}
		
		mainLayer.setAcceptingInput(false);
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
				GameDirector.sharedSceneDelegate().requestScene(SceneID.PartyInventoryScene);
			} else if (component == buttons.get(5)) {
				GameDirector.sharedSceneDelegate().requestScene(SceneID.ComponentTest);
			}
		}
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
}
