package scene;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import component.*;
import component.Positionable.ReferencePoint;
import component.sprite.Sprite;
import core.FontStore;
import core.GameDirector;
import core.SoundStore;

public class CampScene extends Scene {
	
	public static final SceneID ID = SceneID.CAMP;
	
	private static final int PADDING = 20;
	private static final int BUTTON_WIDTH = 315;
	private static final int BUTTON_HEIGHT = 30;
		
	private Panel buttonPanel;
	//1: inventoryButton
	//2: managementButton
	//3: mapButton
	//4: huntButton
	//5: somethingButton
	//6: continueButton
	private Button[] bottomButtons;
	private Particle campFire;
	
	@Override
	public void enter(GameContainer container, StateBasedGame game)  {
		super.enter(container, game);
		SoundStore.get().loopMusic("Crackling Fire");
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);

		backgroundLayer.add(new Sprite(container, new Image("resources/graphics/backgrounds/camp.png")));
		
		Font fieldFont = FontStore.get(FontStore.FontID.FIELD);
		
		buttonPanel = new Panel(container, container.getWidth(), 120, Color.gray);
		buttonPanel.setBevelWidth(2);
		buttonPanel.setBevel(Component.BevelType.OUT);
		buttonPanel.setTopBorderWidth(2);
		buttonPanel.setBorderColor(Color.black);
		bottomButtons = new Button[6];
		
		Label tempLabel = new Label(container, BUTTON_WIDTH, fieldFont, Color.white, "Inventory");
		bottomButtons[0] = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, tempLabel);
		tempLabel = new Label(container, BUTTON_WIDTH, fieldFont, Color.white, "Party Management");
		bottomButtons[1] = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, tempLabel);
		tempLabel = new Label(container, BUTTON_WIDTH, fieldFont, Color.white, "Map");
		bottomButtons[2] = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, tempLabel);
		tempLabel = new Label(container, BUTTON_WIDTH, fieldFont, Color.white, "Hunt");
		bottomButtons[3] = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, tempLabel);
		tempLabel = new Label(container, BUTTON_WIDTH, fieldFont, Color.white, "Something");
		bottomButtons[4] = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, tempLabel);
		tempLabel = new Label(container, BUTTON_WIDTH, fieldFont, Color.white, "Continue");
		bottomButtons[5] = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, tempLabel);
		
		ButtonListener listener = new ButtonListener();
		bottomButtons[0].addListener(listener);
		bottomButtons[1].addListener(listener);
		bottomButtons[5].addListener(listener);
		
		buttonPanel.addAsGrid(bottomButtons, buttonPanel.getPosition(ReferencePoint.TOPLEFT), 2, 3, PADDING, PADDING, PADDING, PADDING);
		mainLayer.add(buttonPanel, mainLayer.getPosition(ReferencePoint.BOTTOMLEFT), Positionable.ReferencePoint.BOTTOMLEFT);
		
		campFire = new Particle(container, 1, 0, 10);
		campFire.addFire(560, 290, 15);
		backgroundLayer.add(campFire);
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		campFire.update(delta);
	}
	
	@Override
	public int getID() {
		return ID.ordinal();
	}
	
	/**
	 * ButtonListener
	 */
	private class ButtonListener implements ComponentListener {
		public void componentActivated(AbstractComponent source) {
			SoundStore.get().stopMusic();
			if (source == bottomButtons[0]) {
				GameDirector.sharedSceneListener().requestScene(SceneID.PARTYINVENTORY, CampScene.this, false);
			} else if (source == bottomButtons[1]) {
				GameDirector.sharedSceneListener().requestScene(SceneID.PARTYMANAGEMENTSCENE, CampScene.this, false);
			} else if (source == bottomButtons[5]) {
				GameDirector.sharedSceneListener().sceneDidEnd(CampScene.this);
			}
		}
	}

}
