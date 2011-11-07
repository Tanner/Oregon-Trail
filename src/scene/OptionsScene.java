package scene;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import component.Button;
import component.Component;
import component.Label;
import component.Label.Alignment;
import component.Panel;
import component.Positionable;
import component.SegmentedControl;
import component.Spinner;
import component.Positionable.ReferencePoint;
import component.modal.ComponentModal;
import component.modal.Modal;

import core.FontStore;
import core.GameDirector;
import core.SoundStore;

/**
 * A scene to provide an options for saving/volume etc
 */
public class OptionsScene extends Scene {
	
	public static final SceneID ID = SceneID.RIVER;
	
	private final int TOP_PADDING = 75;
	private final int BUTTON_WIDTH = 250;
	private final int BUTTON_HEIGHT = 50;
	private final int PADDING = 20;
	
	private final int SOUND_INCREMENT = 10;
	private final float MAX_VOLUME = 100.0f;
	
	private final Color bgColor = new Color(0x4671D5);
	
	private Button mainMenu, save, back;
	private Spinner volume;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		Font fieldFont = FontStore.get().getFont(FontStore.FontID.FIELD);
		Font h1Font = FontStore.get().getFont(FontStore.FontID.H2);
		ArrayList<Component> components = new ArrayList<Component>();
		
		//Title
		Label options = new Label(container, BUTTON_WIDTH, BUTTON_HEIGHT, h1Font, Color.white, "Options");
		options.setAlignment(Alignment.CENTER);
		components.add(options);
		
		//Create all buttons
		ButtonListener listener = new ButtonListener();
		Label tempLabel = new Label(container, fieldFont, Color.white, "Reset to Main Menu");
		mainMenu = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT,tempLabel);
		mainMenu.addListener(listener);
		components.add(mainMenu);
		
		tempLabel = new Label(container, fieldFont, Color.white, "Save");
		save = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT,tempLabel);
		save.addListener(listener);
		components.add(save);
		
		//Create volume spinner
		Label volumeLabel = new Label(container, BUTTON_WIDTH, fieldFont.getLineHeight(), fieldFont, Color.white, "Volume");
		volumeLabel.setAlignment(Alignment.CENTER);
		
		String[] volumeLevels = new String[11];
		for (int i = 0; i <= SOUND_INCREMENT ; i++) {
			volumeLevels[i] = "" + i*SOUND_INCREMENT;
		}
		volume = new Spinner(container, BUTTON_WIDTH, BUTTON_HEIGHT, fieldFont, Color.white, true, volumeLevels);
		volume.addListener(listener);
		System.out.println(SoundStore.get().getVolume() * SOUND_INCREMENT );
		volume.setState((int) (SoundStore.get().getVolume() * SOUND_INCREMENT ));
		
		List<Component> volumeList = new ArrayList<Component>();
		volumeList.add(volumeLabel);
		volumeList.add(volume);
		Panel volumePanel = new Panel(container, BUTTON_WIDTH,  volume.getHeight() + volumeLabel.getHeight());
		volumePanel.addAsColumn(volumeList.iterator(), mainLayer.getPosition(ReferencePoint.TOPLEFT), 0, 0, 0);
		components.add(volumePanel);
		
		tempLabel = new Label(container, fieldFont, Color.white, "Back");
		back = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT,tempLabel);
		back.addListener(listener);
		tempLabel = new Label(container, fieldFont, Color.white, "Volume");
		components.add(back);
		
		//Add to layers
		mainLayer.addAsColumn(components.iterator(), mainLayer.getPosition(ReferencePoint.TOPCENTER), -BUTTON_WIDTH/2, TOP_PADDING, PADDING);
		backgroundLayer.add(new Panel(container, bgColor), backgroundLayer.getPosition(ReferencePoint.TOPLEFT), Positionable.ReferencePoint.TOPLEFT);
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
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
			GameDirector.sharedSceneListener().serialize("Game " + (loadControl.getSelection()[0]+1));
		}
	}
	
	/**
	 * Listener for all the buttons
	 */
	private class ButtonListener implements ComponentListener {
		public void componentActivated(AbstractComponent source) {
			if (source == mainMenu) {
				GameDirector.sharedSceneListener().resetToMainMenu();
			} else if ( source == back ) {
				GameDirector.sharedSceneListener().sceneDidEnd(OptionsScene.this);
			} else if ( source == save ) {
				showModal(GameDirector.sharedSceneListener().getLoadSaveModal(false, OptionsScene.this));
			}
			else if ( source == volume ) {
				SoundStore.get().setVolume( volume.getState() / MAX_VOLUME );
			}
		}
	}
}
