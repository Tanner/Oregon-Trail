package scene;

import model.Condition;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

import component.ConditionBar;
import component.Label;
import component.Positionable.ReferencePoint;
import core.FontStore;
import core.GameDirector;
import core.SoundStore;

public class LoadingScene extends Scene {
	public static final SceneID ID = SceneID.LOADING;
	
	private int PADDING = 20;
	
	private int BAR_WIDTH = 200;
	private int BAR_HEIGHT = 10;
	
	private Label loadLabel;
	private Condition loadCondition;
	private ConditionBar loadingBar;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);

		Font h2 = FontStore.get(FontStore.FontID.H2);
		Font field = FontStore.get(FontStore.FontID.FIELD);
		
		Label loadingLabel = new Label(container, h2, Color.white, "Loading...");
		mainLayer.add(loadingLabel, mainLayer.getPosition(ReferencePoint.CENTERCENTER), ReferencePoint.CENTERCENTER);
		
		loadCondition = new Condition(0, 100, 0);
		loadingBar = new ConditionBar(container, BAR_WIDTH, BAR_HEIGHT, loadCondition);
		mainLayer.add(loadingBar, loadingLabel.getPosition(ReferencePoint.BOTTOMCENTER), ReferencePoint.TOPCENTER, 0, PADDING);
		
		loadLabel = new Label(container, BAR_WIDTH, field, Color.white, "Loading...");
		mainLayer.add(loadLabel, loadingBar.getPosition(ReferencePoint.BOTTOMCENTER), ReferencePoint.TOPCENTER, 0, PADDING);
	}
	
	public void enter(GameContainer container, StateBasedGame game)  {
		// Sound
		loadLabel.setText("Loading sounds...");
		SoundStore.get();
		loadCondition.increase(50);
		loadingBar.update();
		
		// Done
		loadCondition.increase(50);
		loadingBar.update();
		
		loadLabel.setText("Loading complete.");
		
		GameDirector.sharedSceneListener().requestScene(SceneID.MAINMENU, this, true);
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
}