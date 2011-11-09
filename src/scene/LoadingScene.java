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

public class LoadingScene extends Scene {
	public static final SceneID ID = SceneID.LOADING;
	
	private int PADDING = 20;
	
	private int BAR_WIDTH = 200;
	private int BAR_HEIGHT = 10;
	
	private Label loadLabel;
	private Condition loadCondition;
	private ConditionBar loadingBar;
	
	private DeferredResource nextResource;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);

		Font h2 = FontStore.get().getFont(FontStore.FontID.H2);
		Font field = FontStore.get().getFont(FontStore.FontID.FIELD);
		
		LoadingList.setDeferredLoading(true);

		SoundStore.get();
		ImageStore.get();
		
		loadCondition = new Condition(0, LoadingList.get().getTotalResources(), 0); 
		
		loadingBar = new ConditionBar(container, BAR_WIDTH, BAR_HEIGHT, loadCondition);
		mainLayer.add(loadingBar, mainLayer.getPosition(ReferencePoint.CENTERCENTER), ReferencePoint.TOPCENTER);
		
		Label loadingLabel = new Label(container, h2, Color.white, "Loading...");
		mainLayer.add(loadingLabel, loadingBar.getPosition(ReferencePoint.TOPCENTER), ReferencePoint.BOTTOMCENTER, 0, -PADDING);
		
		loadLabel = new Label(container, container.getWidth(), field, Color.white, "Loading load scene...");
		mainLayer.add(loadLabel, loadingBar.getPosition(ReferencePoint.BOTTOMCENTER), ReferencePoint.TOPCENTER, 0, PADDING);
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
				System.out.println(nextResource.getDescription());
				nextResource.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			nextResource = null;
		}
		
		if (LoadingList.get().getRemainingResources() > 0) {
			nextResource = LoadingList.get().getNext();
		} else {
			GameDirector.sharedSceneListener().requestScene(SceneID.MAINMENU, this, true);
		}
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
}