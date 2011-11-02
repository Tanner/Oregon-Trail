package scene;

import model.Party;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import component.*;
import component.Positionable.ReferencePoint;
import core.ConstantStore;
import core.FontStore;
import core.GameDirector;
import core.SoundStore;

public class CampScene extends Scene {
	public static final SceneID ID = SceneID.CAMP;
	
	private static final int PADDING = 20;
	private static final int BUTTON_WIDTH = 315;
	private static final int BUTTON_HEIGHT = 30;
	
	private Party party;
	
	private Panel sky;
	private ParallaxPanel parallaxPanel;
	private Panel buttonPanel;
	
	private Button inventoryButton;
	private Button partyManagementButton;
	private Button mapButton;
	private Button huntButton;
	private Button miscButton;
	private Button leaveButton;
	
	private Particle campFire;
	
	public CampScene(Party party) {
		this.party = party;
	}
	
	public CampScene(Party party, ParallaxPanel parallaxPanel) {
		this(party);
		
		this.parallaxPanel = parallaxPanel;
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game)  {
		super.enter(container, game);
		SoundStore.get().loopMusic("Crackling Fire");
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);

		sky = SceneryFactory.getSky(container, party.getTime().getTime());
		backgroundLayer.add(sky);
		
		if (parallaxPanel == null) {
			parallaxPanel = SceneryFactory.getScenery(container);
		}
		backgroundLayer.add(parallaxPanel);
		
		backgroundLayer.setOverlayColor(SceneryFactory.getBackgroundOverlayColor(party.getTime().getTime()));
		
		Font fieldFont = FontStore.get(FontStore.FontID.FIELD);
		
		buttonPanel = new Panel(container, container.getWidth(), 120, Color.gray);
		buttonPanel.setBevelWidth(2);
		buttonPanel.setBevel(Component.BevelType.OUT);
		buttonPanel.setTopBorderWidth(2);
		buttonPanel.setBorderColor(Color.black);
		
		ButtonListener listener = new ButtonListener();
		
		Label tempLabel = new Label(container, BUTTON_WIDTH, fieldFont, Color.white, ConstantStore.get("CAMP_SCENE", "INVENTORY"));
		inventoryButton = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, tempLabel);
		inventoryButton.addListener(listener);
		
		tempLabel = new Label(container, BUTTON_WIDTH, fieldFont, Color.white, ConstantStore.get("CAMP_SCENE", "PARTY_MANAGEMENT"));
		partyManagementButton = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, tempLabel);
		partyManagementButton.addListener(listener);
		
		tempLabel = new Label(container, BUTTON_WIDTH, fieldFont, Color.white, ConstantStore.get("CAMP_SCENE", "MAP"));
		mapButton = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, tempLabel);
		mapButton.addListener(listener);
		
		tempLabel = new Label(container, BUTTON_WIDTH, fieldFont, Color.white, ConstantStore.get("CAMP_SCENE", "HUNT"));
		huntButton = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, tempLabel);
		huntButton.addListener(listener);
		
		tempLabel = new Label(container, BUTTON_WIDTH, fieldFont, Color.white, ConstantStore.get("CAMP_SCENE", "MISC"));
		miscButton = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, tempLabel);
		miscButton.addListener(listener);
		
		tempLabel = new Label(container, BUTTON_WIDTH, fieldFont, Color.white, ConstantStore.get("CAMP_SCENE", "LEAVE"));
		leaveButton = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, tempLabel);
		leaveButton.addListener(listener);
				
		Button buttons[] = new Button[6];
		buttons[0] = inventoryButton;
		buttons[1] = partyManagementButton;
		buttons[2] = mapButton;
		buttons[3] = leaveButton;
		buttons[4] = miscButton;
		buttons[5] = huntButton;
		
		buttonPanel.addAsGrid(buttons, buttonPanel.getPosition(ReferencePoint.TOPLEFT), 2, 3, PADDING, PADDING, PADDING, PADDING);
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
			SoundStore.get().playSound("Click");
			
			if (source == inventoryButton) {
				GameDirector.sharedSceneListener().requestScene(SceneID.PARTYINVENTORY, CampScene.this, false);
			} else if (source == partyManagementButton) {
				GameDirector.sharedSceneListener().requestScene(SceneID.PARTYMANAGEMENTSCENE, CampScene.this, false);
			} else if (source == mapButton) {
				GameDirector.sharedSceneListener().requestScene(SceneID.MAP, CampScene.this, false);
			} else if (source == leaveButton) {
				GameDirector.sharedSceneListener().sceneDidEnd(CampScene.this);
			}
		}
	}
}