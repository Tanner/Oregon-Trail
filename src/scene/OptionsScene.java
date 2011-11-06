package scene;

import java.io.File;
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
import component.SegmentedControl;
import component.Spinner;
import component.Positionable.ReferencePoint;
import component.modal.ComponentModal;

import core.ConstantStore;
import core.FontStore;
import core.GameDirector;
import core.SoundStore;

public class OptionsScene extends Scene {
	public static final SceneID ID = SceneID.RIVER;
	
	private final int BUTTON_WIDTH = 250;
	private final int BUTTON_HEIGHT = 50;
	private final int PADDING = 20;
	
	private ComponentModal<SegmentedControl> fileSaveModal;
	private Button mainMenu, save, back;
	private Spinner volume;
	
	public OptionsScene() {
		setUpSaveFiles();
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		Font fieldFont = FontStore.get().getFont(FontStore.FontID.FIELD);
		Font h1Font = FontStore.get().getFont(FontStore.FontID.H2);
		
		Label options = new Label(container, BUTTON_WIDTH, BUTTON_HEIGHT, h1Font, Color.white, "Options");
		options.setAlignment(Alignment.CENTER);

		ButtonListener listener = new ButtonListener();
		Label tempLabel = new Label(container, fieldFont, Color.white, "Main Menu");
		mainMenu = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT,tempLabel);
		mainMenu.addListener(listener);
		tempLabel = new Label(container, fieldFont, Color.white, "Save");
		save = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT,tempLabel);
		save.addListener(listener);
		if (GameDirector.sharedSceneListener().getGame().getPlayer().getParty() == null) {
			save.setDisabled(true);
			save.setTooltipEnabled(true);
			save.setTooltipMessage("Please start a game\nbefore you can save.");
		}
//		tempLabel = new Label(container, fieldFont, Color.white, "Exit");
//		exit = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT,tempLabel);
//		exit.addListener(listener);
		tempLabel = new Label(container, fieldFont, Color.white, "Back");
		back = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT,tempLabel);
		back.addListener(listener);
		tempLabel = new Label(container, fieldFont, Color.white, "Volume");
		String[] volumeLevels = new String[11];
		for (int i = 0; i <= 10; i++) {
			volumeLevels[i] = "" + i*10;
		}
		volume = new Spinner(container, BUTTON_WIDTH, BUTTON_HEIGHT, fieldFont, Color.white, true, volumeLevels);
		volume.addListener(listener);
		System.out.println(SoundStore.get().getVolume() * 10);
		volume.setState((int) (SoundStore.get().getVolume() * 10));
		
		ArrayList<Component> components = new ArrayList<Component>();
		components.add(options);
		components.add(mainMenu);
		components.add(save);
		components.add(volume);
//		components.add(exit);
		components.add(back);
		mainLayer.addAsColumn(components.iterator(), mainLayer.getPosition(ReferencePoint.TOPCENTER), -BUTTON_WIDTH/2, 75, PADDING);
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
	}
	
	private void setUpSaveFiles() {
		List<String> fileList = new ArrayList<String>();
		File file  = new File("resources/serialized/");
		if(file.exists()) {
			for(String fileName : file.list()) {
				fileList.add(fileName);
			}
		}
		
		String[] saveFileList = new String[5];
		int numEmpty = 0;
		for(int i = 1; i <= 5; i++) {
			if(fileList.contains("game" + i + ".ser")) {
				saveFileList[i-1] = ("game" + i);
			} else {
				saveFileList[i-1] = ("Empty");
				numEmpty++;
			}
		}
		int[] emptyFiles = new int[numEmpty];
		int j = 0;
		for(int i = 0; i < 5; i++) {
			if(saveFileList[i] == "Empty") {
				emptyFiles[j] = i;
				j++;
			}
		}
		
		SegmentedControl loadableFiles = new SegmentedControl(container, 500, 200, 5, 1, 0, true, 1, saveFileList);
		loadableFiles.setDisabled(emptyFiles);
		
		fileSaveModal = new ComponentModal<SegmentedControl>(container, OptionsScene.this, "Choose a slot to save", 2, new SegmentedControl(container, 500, 200, 5, 1, 0, true, 1, saveFileList));
		fileSaveModal.setButtonText(fileSaveModal.getCancelButtonIndex(), ConstantStore.get("GENERAL", "CANCEL"));
		
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
	
	private class ButtonListener implements ComponentListener {
		public void componentActivated(AbstractComponent source) {
			if (source == mainMenu) {
				GameDirector.sharedSceneListener().resetToMainMenu();
//			} else if ( source == exit ) {
//				System.exit(0);
			} else if ( source == back ) {
				GameDirector.sharedSceneListener().sceneDidEnd(OptionsScene.this);
			} else if ( source == save ) {
				setUpSaveFiles();
				showModal(fileSaveModal);
			}
			else if ( source == volume ) {
				System.out.printf("Before volume: %f\n", SoundStore.get().getVolume());
				SoundStore.get().setVolume( volume.getState() / 100.0f );
				System.out.printf("After volume: %f\n", SoundStore.get().getVolume());
			}
		}
	}

}
