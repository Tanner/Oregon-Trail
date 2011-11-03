package scene;

import java.util.ArrayList;
import java.util.List;

import model.Notification;
import model.Party;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;
import scene.encounter.*;
import component.AnimatingColor;
import component.Component;
import component.Component.BevelType;
import component.HUD;
import component.Label;
import component.Label.Alignment;
import component.Panel;
import component.ParallaxPanel;
import component.PartyComponent;
import component.Positionable.ReferencePoint;
import component.SceneryFactory;
import component.SegmentedControl;
import component.Toolbar;
import component.modal.ChoiceModal;
import component.modal.Modal;
import component.sprite.ParallaxSprite;
import core.ConstantStore;
import core.FontStore;
import core.FontStore.FontID;
import core.GameDirector;
import core.Logger;
import core.SoundStore;

/**
 * TrailScene is where the {@code Party} travels from place to place.
 */
public class TrailScene extends Scene {
	public static final SceneID ID = SceneID.TRAIL;
	
	private static final int CLICK_WAIT_TIME = 1000;
	private static final int STEP_COUNT_TRIGGER = 2;
	
	private static final int NEAR_MAX_ELAPSED_TIME_SLOW = 20;
	private static final int NEAR_MAX_ELAPSED_TIME_FAST = 1;
	private static final int FAR_MAX_ELAPSED_TIME = 100;
	
	private int clickCounter;
	private int timeElapsed;
		
	private Panel sky;
	private ParallaxPanel parallaxPanel;
	
	private Party party;
	private RandomEncounterTable randomEncounterTable;
	
	private HUD hud;
	
	private SegmentedControl paceSegmentedControl;
	private SegmentedControl rationsSegmentedControl;
	
	private AnimatingColor skyAnimatingColor;
	
	private PartyComponent partyComponent;
	
	/**
	 * Construct a TrailScene with a {@code Party} and a {@code RandomEncounterTable}.
	 * @param party Party to use
	 * @param randomEncounterTable RandomEncounterTable to use
	 */
	public TrailScene(Party party, RandomEncounterTable randomEncounterTable) {
		this.party = party;
		this.randomEncounterTable = randomEncounterTable;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		hud = new HUD(container, party, new HUDListener());
		showHUD(hud);
		
		int toolbarXMargin = 10;
		Toolbar toolbar = new Toolbar(container, container.getWidth(), 40);
		hudLayer.add(toolbar, hud.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.TOPLEFT);
		
		Label paceLabel = new Label(container, FontStore.get(FontID.FIELD), Color.white, ConstantStore.get("GENERAL", "PACE_LABEL"));
		paceLabel.setAlignment(Alignment.LEFT);
		Label rationsLabel = new Label(container, FontStore.get(FontID.FIELD), Color.white, ConstantStore.get("GENERAL", "RATIONS_LABEL"));
		rationsLabel.setAlignment(Alignment.LEFT);
		
		toolbar.add(paceLabel, toolbar.getPosition(ReferencePoint.CENTERLEFT), ReferencePoint.CENTERLEFT, toolbarXMargin, -2);

		int segmentedControlWidth = (toolbar.getWidth() - paceLabel.getWidth() - rationsLabel.getWidth() - toolbarXMargin * 6) / 2;

		String[] paceLabels = new String[Party.Pace.values().length];
		for (int i = 0; i < paceLabels.length; i++) {
			paceLabels[i] = Party.Pace.values()[i].toString();
		}
		
		paceSegmentedControl = new SegmentedControl(container, segmentedControlWidth, toolbar.getHeight() - 14, 1, 3, 0, true, 1, paceLabels);
		toolbar.add(paceSegmentedControl, paceLabel.getPosition(ReferencePoint.CENTERRIGHT), ReferencePoint.CENTERLEFT, toolbarXMargin, 0);
		paceSegmentedControl.addListener(new ToolbarComponentListener());
		
		toolbar.add(rationsLabel, paceSegmentedControl.getPosition(ReferencePoint.CENTERRIGHT), ReferencePoint.CENTERLEFT, toolbarXMargin * 2, 0);
		
		String[] rationLabels = new String[Party.Rations.values().length];
		for (int i = 0; i < rationLabels.length; i++) {
			rationLabels[i] = Party.Rations.values()[i].toString();
		}
		rationsSegmentedControl = new SegmentedControl(container, segmentedControlWidth, toolbar.getHeight() - 14, 1, 3, 0, true, 1, rationLabels);
		toolbar.add(rationsSegmentedControl, rationsLabel.getPosition(ReferencePoint.CENTERRIGHT), ReferencePoint.CENTERLEFT, toolbarXMargin, 0);
		rationsSegmentedControl.addListener(new ToolbarComponentListener());
		
		sky = SceneryFactory.getSky(container, party.getTime().getTime());
		backgroundLayer.add(sky);
		
		parallaxPanel = SceneryFactory.getScenery(container);
		backgroundLayer.add(parallaxPanel);
		
		partyComponent = new PartyComponent(container, container.getWidth(), 250, party.getPartyComponentDataSources());
		mainLayer.add(partyComponent, mainLayer.getPosition(ReferencePoint.BOTTOMRIGHT), ReferencePoint.BOTTOMRIGHT, 0, 0);
		
		clickCounter = 0;
		
		adjustSetting();
}
		
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(!isPaused()) {
			timeElapsed += delta;
			if (party.getTrail().getConditionPercentage() == 0.0) {
				party.setLocation(party.getTrail().getDestination());
				SoundStore.get().stop();
				GameDirector.sharedSceneListener().requestScene(SceneID.TOWN, this, true);
			} else if (!SoundStore.get().getPlayingSounds().contains("Steps")) {
				SoundStore.get().playSound("Steps");
			}
			
			if (skyAnimatingColor != null) {
				skyAnimatingColor.update(delta);
			}
			mainLayer.update(delta);
			
			if (timeElapsed % CLICK_WAIT_TIME < timeElapsed) {
				clickCounter++;
				hud.updateNotifications();
				timeElapsed = 0;
			}
			
			for (ParallaxSprite sprite : parallaxPanel.getSprites()) {
				sprite.move(delta);
			}
			
			partyComponent.update(delta, timeElapsed);
			
			boolean pause = !hud.isNotificationsEmpty();
			
			if (pause) {
				return;
			}
						
			if (clickCounter >= STEP_COUNT_TRIGGER) {
				party.getTime().advanceTime();
				List<Notification> notifications = party.walk();
				hud.updatePartyInformation(party.getTime().get12HourTime(), party.getTime().getDayMonthYear());
				if (party.getPartyMembers().isEmpty()) {
					SoundStore.get().stopAllSound();
					GameDirector.sharedSceneListener().requestScene(SceneID.GAMEOVER, this, true);
				}
				Logger.log("Last Town = " + party.getLocation(), Logger.Level.INFO);
				
				hud.addNotification("" + party.getTrail().getRoughDistanceToGo() + party.getTrail().getDestination().getName());
				
				EncounterNotification encounterNotification = randomEncounterTable.getRandomEncounter();
				
				handleNotifications(notifications, encounterNotification.getNotification().getMessage());
				
				if (encounterNotification.getSceneID() != null)
					SoundStore.get().stopSound("Steps");
					GameDirector.sharedSceneListener().requestScene(encounterNotification.getSceneID(), this, false);

				clickCounter = 0;
				
				adjustSetting();
			}
		}
	}
	
	@Override
	public void prepareToEnter() {
		super.prepareToEnter();
		
		if (parallaxPanel != null) {
			backgroundLayer.add(parallaxPanel);
		}
	}
	
	/**
	 * Handle the incoming messages from walk and Encounters, consolidating
	 * everything into one modal window.
	 * @param notifications Notifications from walk()
	 * @param encounterMessage Notifications from encounter
	 */
	private void handleNotifications(List<Notification> notifications, String encounterMessage) {
		List<String> messages = new ArrayList<String>();
		StringBuilder modalMessage = new StringBuilder();
		for(Notification notification : notifications) {
			if(notification.getIsModal()) {
				modalMessage.append(notification.getMessage() + "\n");
			} else {
				messages.add(notification.getMessage());
			}
		}
		
		if (encounterMessage != null)
			modalMessage.append(encounterMessage);
		
		if (modalMessage.length() != 0) {
			SoundStore.get().stopSound("Steps");
			ChoiceModal campModal = new ChoiceModal(container, this, modalMessage.toString().trim());
			campModal.setCancelButtonText(ConstantStore.get("TRAIL_SCENE", "CAMP"));
			campModal.setDismissButtonText(ConstantStore.get("GENERAL", "CONTINUE"));
			showModal(campModal);
		}
		
		hud.addNotifications(messages);
	}
	
	/**
	 * Adjust the setting for the scene based on time of day.
	 */
	private void adjustSetting() {
		int hour = party.getTime().getTime();
		
		hud.setTime(party.getTime().get12HourTime());
		hud.setDate(party.getTime().getDayMonthYear());
		
		skyAnimatingColor = SceneryFactory.getSkyAnimatingColor(hour, CLICK_WAIT_TIME * STEP_COUNT_TRIGGER);
		sky.setBackgroundColor(skyAnimatingColor);
		
		AnimatingColor overlayAnimatingColor = SceneryFactory.getOverlayAnimatingColor(hour, CLICK_WAIT_TIME * STEP_COUNT_TRIGGER);
		mainLayer.setOverlayColor(overlayAnimatingColor);

		// Determine our display speed
		ParallaxSprite.setMaxElapsedTimes((int) map(party.getPace().getSpeed(), Party.Pace.STEADY.getSpeed(), Party.Pace.GRUELING.getSpeed(), NEAR_MAX_ELAPSED_TIME_SLOW, NEAR_MAX_ELAPSED_TIME_FAST), FAR_MAX_ELAPSED_TIME);
	
		// Because we changed the max elapsed times, we have to update the new max elapsed time for each sprite
		for (ParallaxSprite sprite : parallaxPanel.getSprites()) {
			sprite.setMaxElapsedTime(sprite.getDistance());
		}
	}
	
	/**
	 * Map a number from its input range to its number in the own output range.
	 * @param x Number to map to the new range
	 * @param inMin x's min value
	 * @param inMax x's max value
	 * @param outMin Output's min value
	 * @param outMax Output's max value
	 * @return Value contorted to outMin and outMax
	 */
	public float map(float x, float inMin, float inMax, float outMin, float outMax) {
		  return (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
	}
	
	/**
	 * Get the {@code ParallaxPanel}.
	 * @return ParallaxPanel
	 */
	public ParallaxPanel getParallaxPanel() {		
		return parallaxPanel;
	}
	
	@Override
	public void leave(GameContainer container, StateBasedGame game)  {
		super.leave(container, game);
		
		backgroundLayer.remove(parallaxPanel);
	}
	
	@Override
	public int getID() {
		return ID.ordinal();
	}
	
	@Override
	public void dismissModal(Modal modal, boolean cancelled) {
		super.dismissModal(modal, cancelled);
		SoundStore.get().stopAllSound();
		if (cancelled) {
			GameDirector.sharedSceneListener().requestScene(SceneID.CAMP, this, false);
		}
	}
	
	private class HUDListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent component) {
			SoundStore.get().stopAllSound();
			GameDirector.sharedSceneListener().requestScene(SceneID.CAMP, TrailScene.this, false);
		}
	}
	
	private class ToolbarComponentListener implements ComponentListener {

		@Override
		public void componentActivated(AbstractComponent source) {
			if (source == paceSegmentedControl) {
				party.setPace(Party.Pace.values()[paceSegmentedControl.getSelection()[0]]);
			} else if (source == rationsSegmentedControl) {
				party.setRations(Party.Rations.values()[rationsSegmentedControl.getSelection()[0]]);
			}
		}
		
	}
}
