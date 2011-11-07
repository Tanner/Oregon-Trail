package scene;

import java.util.Random;

import model.Party;
import model.Person;
import model.worldMap.LocationNode;
import model.worldMap.TrailEdge;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import component.AnimatingColor;
import component.Button;
import component.Panel;
import component.Label;
import component.Positionable;
import component.Positionable.ReferencePoint;
import component.SceneryFactory;
import component.SegmentedControl;
import component.modal.*;
import component.parallax.ParallaxComponent;
import component.parallax.ParallaxComponentLoop;
import component.parallax.ParallaxPanel;
import component.sprite.Sprite;

import core.*;

/**
 * The scene which represents a town.
 */
public class TownScene extends Scene {
	public static final SceneID ID = SceneID.TOWN;
	
	private static final int BUTTON_WIDTH = 200;
	private static final int BUTTON_HEIGHT = 60;
	
	private static final int CLICK_WAIT_TIME = 1000;
	private static final int STEP_COUNT_TRIGGER = 2;
	
	private int clickCounter;
	private int timeElapsed;
	
	private Party party;
	
	private LocationNode location;
	
	private Button trailButton;
	
	private ComponentModal<SegmentedControl> trailChoiceModal;
	
	private Panel sky;
	private AnimatingColor skyAnimatingColor;
	
	/**
	 * builds town scene
	 * @param party where/who the action is
	 */
	public TownScene(Party party, LocationNode location) {
		this.party = party;
		this.location = location;
		for (Person p : party.getPartyMembers()) {
			Logger.log(p.getName() + ", the " + p.getProfession() + ", entered the town.", Logger.Level.INFO);
		}
		if(location.getTrails() != 0) {
			String[] trails = new String[location.getTrails()];
			for(int i = 0; i < location.getTrails(); i++) {
				TrailEdge temp = location.getOutBoundTrailByIndex(i);
				trails[i] = temp.getRoughLength() + " and to the " + temp.getRoughDirection() + ", " + temp.getDangerRating()  +  " \n" + temp.getName();
				}
			trailChoiceModal = new ComponentModal<SegmentedControl>(container,
					this,
					ConstantStore.get("TOWN_SCENE", "TRAIL_CHOICE"),
					2,
					new SegmentedControl(container, 700, 300, 3, 1, 20, true, 1, trails));
			trailChoiceModal.setButtonText(trailChoiceModal.getCancelButtonIndex(), ConstantStore.get("GENERAL", "CANCEL"));
		}
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);

		Random random = new Random();
		SoundStore.get().playMusic(random.nextBoolean() ? "FFD" : "MS");
		
		sky = SceneryFactory.getSky(container, party.getTime().getTime());
		backgroundLayer.add(sky);
		
		ParallaxComponent.MAX_DISTANCE = 1;
		
		ParallaxPanel terrain = new ParallaxPanel(container, container.getWidth(), container.getHeight());
		
		ParallaxComponentLoop ground = SceneryFactory.getGround(container);
		terrain.add(ground, terrain.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT);	
		
		ParallaxComponentLoop trail = SceneryFactory.getTrail(container);
		terrain.add(trail, ground.getPosition(ReferencePoint.CENTERLEFT), ReferencePoint.CENTERLEFT);

		ParallaxComponentLoop hillB = SceneryFactory.getHillB(container);
		terrain.add(hillB, ground.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.BOTTOMLEFT, 80, 0);
		
		ParallaxComponentLoop hillA = SceneryFactory.getHillA(container);
		terrain.add(hillA, ground.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.BOTTOMLEFT);
		
		mainLayer.add(terrain, mainLayer.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT);
		
		Sprite store = new Sprite(container, 400, ImageStore.get().getImage("STORE_BUILDING"));
		mainLayer.add(store, ground.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.BOTTOMLEFT, 10, 40);
		
		Font h1 = FontStore.get().getFont(FontStore.FontID.H1);
		Font h2 = FontStore.get().getFont(FontStore.FontID.H2);
		Font fieldFont = FontStore.get().getFont(FontStore.FontID.FIELD);
		
		Label titleLabel = new Label(container, h1, Color.white, location.getName());
		Label subtitleLabel = new Label(container, h2, Color.white, "Press Enter to Go to Store");
		
		trailButton = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, "Go To Trail"));
		trailButton.addListener(new ButtonListener());
		
		mainLayer.add(trailButton, mainLayer.getPosition(ReferencePoint.BOTTOMCENTER), Positionable.ReferencePoint.TOPCENTER, 0, -100);
		mainLayer.add(titleLabel, mainLayer.getPosition(Positionable.ReferencePoint.CENTERCENTER), Positionable.ReferencePoint.BOTTOMCENTER, 0, -5);
		mainLayer.add(subtitleLabel, titleLabel.getPosition(Positionable.ReferencePoint.BOTTOMCENTER), Positionable.ReferencePoint.TOPCENTER, 0, 5);
		
		adjustSetting();
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(SoundStore.get().getPlayingMusic() == null) {
			Random random = new Random();
			SoundStore.get().playMusic(random.nextBoolean() ? "FFD" : "MS");
		}
		
		timeElapsed += delta;
		
		if (skyAnimatingColor != null) {
			skyAnimatingColor.update(delta);
		}
		mainLayer.update(delta);
		
		if (timeElapsed % CLICK_WAIT_TIME < timeElapsed) {
			clickCounter++;
			timeElapsed = 0;
		}
		
		if (clickCounter >= STEP_COUNT_TRIGGER) {
			party.getTime().advanceTime();
			clickCounter = 0;
			adjustSetting();
		}
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game)  {
		super.enter(container, game);
	}
	
	/**
	 * Adjust the setting for the scene based on time of day.
	 */
	private void adjustSetting() {
		int hour = party.getTime().getTime();
		
		skyAnimatingColor = SceneryFactory.getSkyAnimatingColor(hour, CLICK_WAIT_TIME * STEP_COUNT_TRIGGER);
		sky.setBackgroundColor(skyAnimatingColor);
		
		AnimatingColor overlayAnimatingColor = SceneryFactory.getOverlayAnimatingColor(hour, CLICK_WAIT_TIME * STEP_COUNT_TRIGGER);
		mainLayer.setOverlayColor(overlayAnimatingColor);
	}

	@Override
	public void keyReleased(int key, char c) {
		if (key == Input.KEY_ENTER) {
			GameDirector.sharedSceneListener().requestScene(SceneID.STORE, this, false);
		}
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
	
	/**
	 * Temporary listener to allow movement to the trail
	 */
	private class ButtonListener implements ComponentListener {
		public void componentActivated(AbstractComponent source) {
			showModal(trailChoiceModal);
		}
	}
	
	@Override
	public void dismissModal(Modal modal, int button) {
		super.dismissModal(modal, button);
		
		if (modal == trailChoiceModal) {
			if (button != modal.getCancelButtonIndex()) {
				party.setTrail(party.getLocation().getOutBoundTrailByIndex(trailChoiceModal.getComponent().getSelection()[0]));
				GameDirector.sharedSceneListener().requestScene(SceneID.TRAIL, TownScene.this, true);
			}
		}
	}
}
