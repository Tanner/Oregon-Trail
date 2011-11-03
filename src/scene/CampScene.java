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
	
	private static final int MARGIN = 10;
	private static final int HEIGHT = 80;
	
	private Party party;
	
	private Panel sky;
	private ParallaxPanel parallaxPanel;
	private Panel buttonPanel;
	
	private Button inventoryButton;
	private Button mapButton;
	private Button huntButton;
	private Button leaveButton;
	
	private Particle campFire;
	
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
		backgroundLayer.setOverlayColor(overlayColor);
		
		Font fieldFont = FontStore.get(FontStore.FontID.FIELD);
		
		buttonPanel = new Panel(container, container.getWidth(), HEIGHT, Color.gray);
		buttonPanel.setBackgroundColor(Color.gray);
		buttonPanel.setBevelWidth(2);
		buttonPanel.setBevel(Component.BevelType.OUT);
		buttonPanel.setBottomBorderWidth(2);
		buttonPanel.setBorderColor(Color.black);
		
		ButtonListener listener = new ButtonListener();
		Button buttons[] = new Button[4];
		
		int buttonWidth = (container.getWidth() - MARGIN * 2 - MARGIN * (buttons.length - 1)) / buttons.length;
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
		
		buttonPanel.addAsRow(buttons, buttonPanel.getPosition(ReferencePoint.TOPLEFT), MARGIN, MARGIN, MARGIN);
		mainLayer.add(buttonPanel, mainLayer.getPosition(ReferencePoint.TOPLEFT), Positionable.ReferencePoint.TOPLEFT);
		
		campFire = new Particle(container, 1, 0, 10);
		campFire.addFire(560, 290, 15);
		backgroundLayer.add(campFire);
		
		partyComponent = new PartyComponent(container, 400, 150, party.getPartyComponentDataSources());
		mainLayer.add(partyComponent, mainLayer.getPosition(ReferencePoint.BOTTOMRIGHT), ReferencePoint.BOTTOMRIGHT, 0, -75);
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