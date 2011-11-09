package scene;

import java.io.File;
import java.util.ArrayList;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import component.Button;
import component.Component;
import component.Label;
import component.Panel;
import component.Positionable;
import component.SegmentedControl;
import component.modal.ComponentModal;
import component.modal.Modal;
import component.sprite.Sprite;

import core.*;

/**
 * Scene displayed upon game startup - displays title of game.
 */

public class MainMenuScene extends Scene {
	public static final SceneID ID = SceneID.MAINMENU;
	private final int BUTTON_WIDTH = 230;
	private final int BUTTON_HEIGHT = 60;
	private final int PADDING = 20;
	private final int LOGO_PADDING = 75;
	
	private Button newGameButton, loadButton, optionsButton, quitButton;
	
	private boolean loadEnabled;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		SoundStore.get().setVolume(.5f);
		SoundStore.get().loopMusic("GBU");
		
		//Make buttons
		Font fieldFont = FontStore.get().getFont(FontStore.FontID.FIELD);
		ArrayList<Component> componentList = new ArrayList<Component>();
		newGameButton = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, ConstantStore.get("MAIN_MENU", "NEW_GAME")));
		newGameButton.addListener(new ButtonListener());
		componentList.add(newGameButton);
		loadButton = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, "Load"));
		loadButton.addListener(new ButtonListener());
		loadButton.setDisabled(!loadEnabled);		
		componentList.add(loadButton);
		optionsButton = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, "Options"));
		optionsButton.addListener(new ButtonListener());
		componentList.add(optionsButton);
		quitButton = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, "Quit"));
		quitButton.addListener(new ButtonListener());
		componentList.add(quitButton);
		
		//Logo
		Sprite logoSprite = new Sprite(container, 480, ImageStore.get().getImage("LOGO"));
		
		
		mainLayer.add(logoSprite, mainLayer.getPosition(Positionable.ReferencePoint.TOPCENTER), Positionable.ReferencePoint.TOPCENTER, 0, LOGO_PADDING);
		mainLayer.addAsRow(componentList.iterator(), mainLayer.getPosition(Positionable.ReferencePoint.BOTTOMLEFT), PADDING, -BUTTON_HEIGHT*2, PADDING);
		backgroundLayer.add(new Panel(container, ImageStore.get().getImage("TRAIL_MAP")));
	}

	@Override
	public void prepareToEnter() {
		super.prepareToEnter();
		File file = new File("resources/serialized/");
		if(!file.exists() || !hasLoadableFiles(file)) {
			loadEnabled = false;
		} else {
			loadEnabled = true;
		}
		if(loadButton != null) {
			loadButton.setDisabled(!loadEnabled);
		}
	}
	
	private boolean hasLoadableFiles(File file) {
		for(String name : file.list()) {
			for(int i = 1; i <= 5; i++) {
				if(name == "Game " + i + ".ser") {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		return;
	}
	
	@Override
	public int getID() {
		return ID.ordinal();
	}
	
	@SuppressWarnings("unchecked")
	public void dismissModal(Modal modal, int button) {
		super.dismissModal(modal, button);
		if (button != modal.getCancelButtonIndex()) {
			ComponentModal<SegmentedControl> loadModal = (ComponentModal<SegmentedControl>) modal;
			SegmentedControl loadControl = loadModal.getComponent();
			GameDirector.sharedSceneListener().deserialize("Game " + (loadControl.getSelection()[0] + 1));
		}
	}
	
	private class ButtonListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent source) {
			if (source == newGameButton) {
				SoundStore.get().setMusicVolume(.25f);
				GameDirector.sharedSceneListener().requestScene(SceneID.PARTYCREATION, MainMenuScene.this, true);
			} else if (source == loadButton) {
				showModal(GameDirector.sharedSceneListener().getLoadSaveModal(true, MainMenuScene.this));
			} else if (source == optionsButton) {
				GameDirector.sharedSceneListener().requestScene(SceneID.OPTIONS, MainMenuScene.this, false);
			} else if (source == quitButton) {
				System.exit(0);
			}
		}
	}
}
