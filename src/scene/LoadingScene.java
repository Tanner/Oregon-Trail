package scene;

import java.io.IOException;

import model.Condition;

import org.newdawn.slick.*;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.StateBasedGame;

import component.ConditionBar;
import component.Label;
import component.Positionable.ReferencePoint;
import core.FontStore;
import core.GameDirector;
import core.ImageStore;
import core.SoundStore;

/**
 * A screen that shows a loading bar that gradually fills to 100% as
 * the game's resources are loaded (sound, images, fonts, etc).
 */
public class LoadingScene extends Scene {
	public static final SceneID ID = SceneID.LOADING;
	
	private int PADDING = 20;
	
	private int BAR_WIDTH = 200;
	private int BAR_HEIGHT = 10;
	
	private Label loadLabel;
	private Label errorLabel;
	private Condition loadCondition;
	private ConditionBar loadingBar;
	
	private DeferredResource nextResource;
	
	private String error;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);

		Font h2 = FontStore.get().getFont(FontStore.FontID.H2);
		Font field = FontStore.get().getFont(FontStore.FontID.FIELD);
		
		LoadingList.setDeferredLoading(true);

		try {
			SoundStore.get();
			ImageStore.get();
		} catch (Exception e) {
			e.printStackTrace();
			error = e.getMessage();
		}
		
		loadCondition = new Condition(0, LoadingList.get().getTotalResources(), 0); 
		
		loadingBar = new ConditionBar(container, BAR_WIDTH, BAR_HEIGHT, loadCondition);
		mainLayer.add(loadingBar, mainLayer.getPosition(ReferencePoint.CENTERCENTER), ReferencePoint.TOPCENTER);
		
		Label loadingLabel = new Label(container, h2, Color.white, "Loading...");
		mainLayer.add(loadingLabel, loadingBar.getPosition(ReferencePoint.TOPCENTER), ReferencePoint.BOTTOMCENTER, 0, -PADDING);
		
		loadLabel = new Label(container, container.getWidth(), field, Color.white, "Loading load scene...");
		mainLayer.add(loadLabel, loadingBar.getPosition(ReferencePoint.BOTTOMCENTER), ReferencePoint.TOPCENTER, 0, PADDING);
		
		errorLabel = new Label(container, container.getWidth(), field, Color.red, "Unknown Error");
		errorLabel.setVisible(false);
		mainLayer.add(errorLabel, loadLabel.getPosition(ReferencePoint.BOTTOMCENTER), ReferencePoint.TOPCENTER, 0, PADDING);
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if (nextResource != null) {
			String path = nextResource.getDescription();
			
			String description = "nothing";
			if (path.contains("graphics")) {
				description = "images";
			} else if (path.contains("sounds")) {
				description = "sounds";
			}
			loadLabel.setText("Loading " + description + "...");
			
			loadCondition.increase(1);
			loadingBar.update();

			try {
				nextResource.load();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				error = "Error: Attempted to load empty file";
			} catch (Exception e) {
				e.printStackTrace();
				error = "Error: Confused by earlier Exceptions, bailing out";
			}
			
			nextResource = null;
		}
		
		if (error != null) {
			errorLabel.setText(error);
			errorLabel.setVisible(true);
		} else {	
			if (LoadingList.get().getRemainingResources() > 0) {
				nextResource = LoadingList.get().getNext();
			} else {
				GameDirector.sharedSceneListener().requestScene(SceneID.MAINMENU, this, true);
			}
		}
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
}