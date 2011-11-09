package scene;

import java.util.Random;

import model.Party;
import model.Person;
import model.worldMap.LocationNode;
import model.worldMap.TrailEdge;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import component.AnimatingColor;
import component.Button;
import component.Panel;
import component.Positionable.ReferencePoint;
import component.SceneryFactory;
import component.SegmentedControl;
import component.hud.TownHUD;
import component.modal.*;
import component.parallax.ParallaxComponent;
import component.parallax.ParallaxComponentLoop;
import component.parallax.ParallaxPanel;
import component.sprite.AnimatingSprite;
import component.sprite.Sprite;

import core.*;

/**
 * The scene which represents a town.
 */
public class TownScene extends Scene {
	public static final SceneID ID = SceneID.TOWN;
	
	private static final int CLICK_WAIT_TIME = 1000;
	private static final int STEP_COUNT_TRIGGER = 2;
		
	private int clickCounter;
	private int timeElapsed;
	
	private Party party;
	
	private LocationNode location;
	
	private ComponentModal<SegmentedControl> trailChoiceModal;
	
	private Panel sky;
	private AnimatingColor skyAnimatingColor;
	
	private AnimatingSprite partyLeaderSprite;
	
	private TownHUD hud;

	private Sprite store;
	
	/**
	 * Builds town scene
	 * @param party where/who the action is
	 */
	public TownScene(Party party, LocationNode location) {
		this.party = party;
		this.location = location;
		for (Person p : party.getPartyMembers()) {
			Logger.log(p.getName() + ", the " + p.getProfession() + ", entered the town.", Logger.Level.INFO);
		}
		
		if (location.getTrails() != 0) {
			String[] trails = new String[location.getTrails()];
			for (int i = 0; i < location.getTrails(); i++) {
				TrailEdge temp = location.getOutBoundTrailByIndex(i);
				trails[i] = temp.getRoughLength() + " and to the " + temp.getRoughDirection() + ", " + temp.getDangerRating()  +  " \n" + temp.getName();
			}
			
			trailChoiceModal = new ComponentModal<SegmentedControl>(container,
					this,
					ConstantStore.get("TOWN_SCENE", "TRAIL_CHOICE"),
					2,
					new SegmentedControl(container, 700, 300, 3, 1, 20, true, 1, trails));
			trailChoiceModal.setButtonText(trailChoiceModal.getCancelButtonIndex(), ConstantStore.get("GENERAL", "CANCEL"));
		};
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);

		Random random = new Random();
		SoundStore.get().playMusic(random.nextBoolean() ? "FFD" : "MS");
		
		hud = new TownHUD(container, new HUDListener());
		hud.setNotification(location.getName());
		super.showHUD(hud);	
		
		sky = SceneryFactory.getSky(container, party.getTime().getTime());
		backgroundLayer.add(sky);
		
		ParallaxComponent.MAX_DISTANCE = 1;
		
		ParallaxPanel parallaxPanel = new ParallaxPanel(container, container.getWidth(), container.getHeight());
		
		ParallaxComponentLoop ground = SceneryFactory.getGround(container);
		parallaxPanel.add(ground, parallaxPanel.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT);	
		
		ParallaxComponentLoop trail = SceneryFactory.getTrail(container);
		parallaxPanel.add(trail, ground.getPosition(ReferencePoint.CENTERLEFT), ReferencePoint.CENTERLEFT, 80, 0);

		ParallaxComponentLoop hillB = SceneryFactory.getHillB(container);
		parallaxPanel.add(hillB, ground.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.BOTTOMLEFT, 80, 0);
		
		ParallaxComponentLoop hillA = SceneryFactory.getHillA(container);
		parallaxPanel.add(hillA, ground.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.BOTTOMLEFT);
		
		mainLayer.add(parallaxPanel, mainLayer.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT);
		
		store = new Sprite(container, 400, ImageStore.get().getImage("STORE_BUILDING"));
		mainLayer.add(store, ground.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.BOTTOMLEFT, 20, 40);
		
		partyLeaderSprite = new AnimatingSprite(container,
				96,
				new Animation(new Image[] {ImageStore.get().getImage("HUNTER_LEFT")}, 250),
				new Animation(new Image[] {ImageStore.get().getImage("HUNTER_RIGHT")}, 250),
				AnimatingSprite.Direction.RIGHT);
		mainLayer.add(partyLeaderSprite,
				new Vector2f(mainLayer.getWidth(), trail.getPosition(ReferencePoint.BOTTOMRIGHT).y),
				ReferencePoint.BOTTOMRIGHT,
				-20,
				-25);
		
		adjustSetting();
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(SoundStore.get().getPlayingMusic() == null) {
			SoundStore.get().playTownMusic();
		}
		
		timeElapsed += delta;
		
		if (container.getInput().isKeyDown(Input.KEY_LEFT)
				&& partyLeaderSprite.getX() > 0) {
			partyLeaderSprite.moveLeft(delta);
			partyLeaderSprite.update(container, delta);
		} else if (container.getInput().isKeyDown(Input.KEY_RIGHT)
				&& partyLeaderSprite.getX() + partyLeaderSprite.getWidth() < container.getWidth()) {
			partyLeaderSprite.moveRight(delta);
			partyLeaderSprite.update(container, delta);
		}
		
		if (partyLeaderSprite.getX() < store.getX() + store.getWidth()
				&& partyLeaderSprite.getX() > store.getX()) {
			hud.setNotification(ConstantStore.get("TOWN_SCENE", "ENTER_STORE_INSTRUCTION"));
		} else {
			hud.setNotification(location.getName());
		}
		
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
		
		hud.updatePartyInformation(party.getTime().get12HourTime(), party.getTime().getDayMonthYear());
	}
	
	@Override
	public void keyReleased(int key, char c) {
		if (key == Input.KEY_ENTER
				&& partyLeaderSprite.getX() < store.getX() + store.getWidth()
				&& partyLeaderSprite.getX() > store.getX()) {
			GameDirector.sharedSceneListener().requestScene(SceneID.STORE, this, false);
		}
	}

	@Override
	public int getID() {
		return ID.ordinal();
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
	
	private class HUDListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent component) {
			if (component == hud.getTrailButton()) {
				showModal(trailChoiceModal);
			}
		}
	}
}
