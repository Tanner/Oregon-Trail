package scene;

import model.Condition;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

import component.ConditionBar;
import component.Label;
import component.Positionable.ReferencePoint;
import core.FontStore;
import core.GameDirector;
import core.Loader;

public class LoadingScene extends Scene {
	public static final SceneID ID = SceneID.LOADING;
	
	private int PADDING = 20;
	
	private int BAR_WIDTH = 200;
	private int BAR_HEIGHT = 10;
	
	public static enum LOAD_ITEMS { SOUNDS, FONTS };
	private int currentItemIndex;
	private final int MAX_LOAD_AMOUNT;
	private double currentLoadAmount;
	
	private Label loadLabel;
	private Condition loadCondition;
	private ConditionBar loadingBar;
	
	private Loader loader;
	
	public LoadingScene() {
		currentItemIndex = 0;
		currentLoadAmount = 0;

		loadCondition = new Condition(0, 100, 0);
		MAX_LOAD_AMOUNT = (int) (loadCondition.getMax() / LOAD_ITEMS.values().length);
	
		loader = new Loader();
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);

		Font h2 = FontStore.get().getFont(FontStore.FontID.H2);
		Font field = FontStore.get().getFont(FontStore.FontID.FIELD);
		
		Label loadingLabel = new Label(container, h2, Color.white, "Loading...");
		mainLayer.add(loadingLabel, mainLayer.getPosition(ReferencePoint.CENTERCENTER), ReferencePoint.CENTERCENTER);
		
		loadingBar = new ConditionBar(container, BAR_WIDTH, BAR_HEIGHT, loadCondition);
		mainLayer.add(loadingBar, loadingLabel.getPosition(ReferencePoint.BOTTOMCENTER), ReferencePoint.TOPCENTER, 0, PADDING);
		
		loadLabel = new Label(container, BAR_WIDTH, field, Color.white, "Loading load scene...");
		mainLayer.add(loadLabel, loadingBar.getPosition(ReferencePoint.BOTTOMCENTER), ReferencePoint.TOPCENTER, 0, PADDING);
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if (loader.getState() == Thread.State.RUNNABLE) {
			if (currentLoadAmount <= MAX_LOAD_AMOUNT) {
				double increaseAmount = ((-1.0 / MAX_LOAD_AMOUNT) * currentLoadAmount) + 1;
				
				loadCondition.increase(increaseAmount);
				loadingBar.update();
				
				currentLoadAmount += increaseAmount;
			}
			
			return;
		}
		
		if (loader.getState() == Thread.State.TERMINATED) {
			loadCondition.increase(MAX_LOAD_AMOUNT - currentLoadAmount);
			loadingBar.update();
			
			if (currentItemIndex == LOAD_ITEMS.values().length - 1) {
				loadLabel.setText("Loading complete.");
				
				GameDirector.sharedSceneListener().requestScene(SceneID.MAINMENU, this, true);
				return;
			} else {
				loader = new Loader();
				
				currentLoadAmount = 0;
				currentItemIndex++;
			}
		}
		
		LOAD_ITEMS currentLoadingItem = LOAD_ITEMS.values()[currentItemIndex];

		if (currentLoadingItem == LOAD_ITEMS.SOUNDS) {
			loadLabel.setText("Loading sounds...");
		} else if (currentLoadingItem == LOAD_ITEMS.FONTS) {
			loadLabel.setText("Loading fonts...");
		}
		
		System.out.println(loadLabel.getText());

		loader.setItemToLoad(currentLoadingItem);
		loader.start();
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
}