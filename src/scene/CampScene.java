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
import component.sprite.ParallaxSprite;
import core.ConstantStore;
import core.FontStore;
import core.GameDirector;
import core.SoundStore;

public class CampScene extends Scene {
	public static final SceneID ID = SceneID.CAMP;
	
	private static final int MARGIN = 10;
	private static final int HEIGHT = 80;
	private static final int PARTY_Y_OFFSET = -190;
	
	private static final int CLICK_WAIT_TIME = 1000;
	private static final int STEP_COUNT_TRIGGER = 2;
	
	private static final int INFO_WIDTH = 200;
	
	private Party party;
	
	private Panel sky;
	private ParallaxPanel parallaxPanel;
	private Panel hudPanel;
	
	private int clickCounter;
	private int timeElapsed;
	
	private Button inventoryButton;
	private Button mapButton;
	private Button huntButton;
	private Button leaveButton;
	
	private Label dateLabel;
	private Label timeLabel;
	
	private Particle campFire;

	private AnimatingColor skyAnimatingColor;
	
	private PartyComponent partyComponent;
	
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
		
		Color overlayColor = SceneryFactory.getOverlayColorForHour(party.getTime().getTime());
		mainLayer.setOverlayColor(overlayColor);
		
		Font fieldFont = FontStore.get(FontStore.FontID.FIELD);
		
		hudPanel = new Panel(container, container.getWidth(), HEIGHT, Color.gray);
		hudPanel.setBackgroundColor(Color.gray);
		hudPanel.setBevelWidth(2);
		hudPanel.setBevel(Component.BevelType.OUT);
		hudPanel.setBottomBorderWidth(2);
		hudPanel.setBorderColor(Color.black);
		
		ButtonListener listener = new ButtonListener();
		Button buttons[] = new Button[4];
		
		int buttonWidth = (container.getWidth() - MARGIN * 2 - MARGIN * (buttons.length - 1) - INFO_WIDTH - MARGIN) / buttons.length;
		int buttonHeight = HEIGHT - MARGIN * 2;
		
		Label tempLabel = new Label(container, buttonWidth, fieldFont, Color.white, ConstantStore.get("CAMP_SCENE", "INVENTORY"));
		inventoryButton = new Button(container, buttonWidth, buttonHeight, tempLabel);
		inventoryButton.addListener(listener);
		
		tempLabel = new Label(container, buttonWidth, fieldFont, Color.white, ConstantStore.get("CAMP_SCENE", "MAP"));
		mapButton = new Button(container, buttonWidth, buttonHeight, tempLabel);
		mapButton.addListener(listener);
		
		tempLabel = new Label(container, buttonWidth, fieldFont, Color.white, ConstantStore.get("CAMP_SCENE", "HUNT"));
		huntButton = new Button(container, buttonWidth, buttonHeight, tempLabel);
		huntButton.addListener(listener);
		
		tempLabel = new Label(container, buttonWidth, fieldFont, Color.white, ConstantStore.get("CAMP_SCENE", "LEAVE"));
		leaveButton = new Button(container, buttonWidth, buttonHeight, tempLabel);
		leaveButton.addListener(listener);
				
		buttons[0] = leaveButton;
		buttons[1] = inventoryButton;
		buttons[2] = mapButton;
		buttons[3] = huntButton;
		
		hudPanel.addAsRow(buttons, hudPanel.getPosition(ReferencePoint.TOPLEFT), MARGIN, MARGIN, MARGIN);
		
		timeLabel = new Label(container, INFO_WIDTH, fieldFont.getLineHeight(), fieldFont, Color.white, "");
		hudPanel.add(timeLabel, hudPanel.getPosition(ReferencePoint.CENTERRIGHT), ReferencePoint.BOTTOMRIGHT, -MARGIN, - MARGIN / 2);
		
		dateLabel = new Label(container, INFO_WIDTH, fieldFont.getLineHeight(), fieldFont, Color.white, "");
		hudPanel.add(dateLabel, hudPanel.getPosition(ReferencePoint.CENTERRIGHT), ReferencePoint.TOPRIGHT, -MARGIN, MARGIN / 2);
		
		mainLayer.add(hudPanel, mainLayer.getPosition(ReferencePoint.TOPLEFT), Positionable.ReferencePoint.TOPLEFT);
		
		campFire = new Particle(container, 1, 0, 10);
		campFire.addFire(560, 290, 15);
		backgroundLayer.add(campFire);
		
		partyComponent = new PartyComponent(container, container.getWidth(), parallaxPanel.getHeight(), party.getPartyComponentDataSources());
		mainLayer.add(partyComponent, mainLayer.getPosition(ReferencePoint.BOTTOMRIGHT), ReferencePoint.BOTTOMRIGHT, 0, PARTY_Y_OFFSET);

		adjustSetting();
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		timeElapsed += delta;
		
		if (timeElapsed % CLICK_WAIT_TIME < timeElapsed) {
			clickCounter++;
			timeElapsed = 0;
		}
		
		if (skyAnimatingColor != null) {
			skyAnimatingColor.update(delta);
		}
		mainLayer.update(delta);
		
		for (ParallaxSprite sprite : parallaxPanel.getSprites()) {
			sprite.move(delta);
		}
		
		campFire.update(delta);
		
		partyComponent.update(delta, timeElapsed * party.getPace().getSpeed());
		
		if (clickCounter >= STEP_COUNT_TRIGGER) {
			party.getTime().advanceTime();
			party.rest();
			
			adjustSetting();
			
			clickCounter = 0;
		}
	}
	
	/**
	 * Adjust the setting for the scene based on time of day.
	 */
	private void adjustSetting() {
		int hour = party.getTime().getTime();
		
		timeLabel.setText(party.getTime().get12HourTime());
		dateLabel.setText(party.getTime().getDayMonthYear());
		
		skyAnimatingColor = SceneryFactory.getSkyAnimatingColor(hour, CLICK_WAIT_TIME * STEP_COUNT_TRIGGER);
		sky.setBackgroundColor(skyAnimatingColor);
		
		AnimatingColor overlayAnimatingColor = SceneryFactory.getOverlayAnimatingColor(hour, CLICK_WAIT_TIME * STEP_COUNT_TRIGGER);
		mainLayer.setOverlayColor(overlayAnimatingColor);
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
			
			if (source == inventoryButton) {
				GameDirector.sharedSceneListener().requestScene(SceneID.PARTYINVENTORY, CampScene.this, false);
			} else if (source == mapButton) {
				GameDirector.sharedSceneListener().requestScene(SceneID.MAP, CampScene.this, false);
			} else if (source == leaveButton) {
				GameDirector.sharedSceneListener().sceneDidEnd(CampScene.this);
			}
		}
	}
}