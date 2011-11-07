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
	
	private final int BUTTON_WIDTH = 250;
	private final int BUTTON_HEIGHT = 50;
	private final int PADDING = 20;
	
	private final int SOUND_INCREMENT = 10;
	private final float MAX_VOLUME = 100.0f;
		
	private Button mainMenuButton, saveButton, backButton;
	private Spinner volumeSpinner;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		Font fieldFont = FontStore.get().getFont(FontStore.FontID.FIELD);
		Font h1Font = FontStore.get().getFont(FontStore.FontID.H1);
		ArrayList<Component> optionComponents = new ArrayList<Component>();
		ButtonListener listener = new ButtonListener();

		// Title
		Label optionsLabel = new Label(container, BUTTON_WIDTH, h1Font.getLineHeight(), h1Font, Color.white, "Options");
		optionsLabel.setAlignment(Alignment.CENTER);
		optionComponents.add(optionsLabel);
		
		// Create volume spinner
		Label volumeLabel = new Label(container, BUTTON_WIDTH, fieldFont.getLineHeight(), fieldFont, Color.white, "Volume");
		volumeLabel.setAlignment(Alignment.CENTER);
		
		String[] volumeLevels = new String[11];
		for (int i = 0; i <= SOUND_INCREMENT ; i++) {
			volumeLevels[i] = "" + i*SOUND_INCREMENT;
		}
		volumeSpinner = new Spinner(container, BUTTON_WIDTH, BUTTON_HEIGHT, fieldFont, Color.white, true, volumeLevels);
		volumeSpinner.addListener(listener);
		volumeSpinner.setState((int) (SoundStore.get().getVolume() * SOUND_INCREMENT ));
		
		List<Component> volumeList = new ArrayList<Component>();
		volumeList.add(volumeLabel);
		volumeList.add(volumeSpinner);
		Panel volumePanel = new Panel(container, BUTTON_WIDTH,  volumeSpinner.getHeight() + volumeLabel.getHeight());
		volumePanel.addAsColumn(volumeList.iterator(), mainLayer.getPosition(ReferencePoint.TOPLEFT), 0, 0, 0);
		optionComponents.add(volumePanel);
		
		//Create all buttons
		saveButton = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, "Save"));
		saveButton.addListener(listener);
		optionComponents.add(saveButton);
		
		mainMenuButton = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, "Main Menu"));
		mainMenuButton.addListener(listener);
		optionComponents.add(mainMenuButton);
		
		backButton = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, "Back"));
		backButton.addListener(listener);
		optionComponents.add(backButton);
		
		int height = 0;
		for (Component  c : optionComponents) {
			height += c.getHeight() + PADDING;
		}
		
		//Add to layers
		mainLayer.addAsColumn(optionComponents.iterator(), mainLayer.getPosition(ReferencePoint.CENTERCENTER), -BUTTON_WIDTH/2, -height / 2, PADDING);
		backgroundLayer.add(new Panel(container, Color.black), backgroundLayer.getPosition(ReferencePoint.TOPLEFT), Positionable.ReferencePoint.TOPLEFT);
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
			if (source == mainMenuButton) {
				GameDirector.sharedSceneListener().resetToMainMenu();
			} else if (source == backButton) {
				GameDirector.sharedSceneListener().sceneDidEnd(OptionsScene.this);
			} else if (source == saveButton) {
				showModal(GameDirector.sharedSceneListener().getLoadSaveModal(false, OptionsScene.this));
			}
			else if (source == volumeSpinner) {
				SoundStore.get().setVolume(volumeSpinner.getState() / MAX_VOLUME );
			}
		}
	}
}
