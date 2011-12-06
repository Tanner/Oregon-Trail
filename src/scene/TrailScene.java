package scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import model.Notification;
import model.Party;
import model.item.ItemType;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;
import scene.encounter.*;
import component.AnimatingColor;
import component.Label;
import component.Label.Alignment;
import component.Panel;
//import component.PartyComponent;
import component.PartyMemberGroup;
import component.Positionable.ReferencePoint;
import component.SceneryFactory;
import component.SegmentedControl;
import component.Toolbar;
import component.VehicleGroup;
import component.hud.TrailHUD;
import component.modal.ChoiceModal;
import component.modal.Modal;
import component.parallax.ParallaxPanel;
import component.parallax.ParallaxComponent;
import core.ConstantStore;
import core.FontStore;
import core.FontStore.FontID;
import core.GameDirector;
import core.SoundStore;

/**
 * TrailScene is where the {@code Party} travels from place to place.
 */
public class TrailScene extends Scene {
	public static final SceneID ID = SceneID.TRAIL;
		
	private static final int PARTY_Y_OFFSET = -200;
	
	private static final int CLICK_WAIT_TIME = 1000;
	private static final int STEP_COUNT_TRIGGER = 2;
	
	private static final int NEAR_MAX_ELAPSED_TIME_SLOW = 20;
	private static final int NEAR_MAX_ELAPSED_TIME_FAST = 1;
	private static final int FAR_MAX_ELAPSED_TIME = 100;
	
	private TrailSceneState state;
	
	private int clickCounter;
	private int timeElapsed;
		
	private Panel sky;
	private ParallaxPanel parallaxPanel;
	private ParallaxComponent ground;
	private ParallaxComponent trail;
	
	private Party party;
	private RandomEncounterTable randomEncounterTable;
	
	private TrailHUD hud;
	private Toolbar toolbar;
	
	private SegmentedControl paceSegmentedControl;
	private SegmentedControl rationsSegmentedControl;
	
	private AnimatingColor skyAnimatingColor;
	
	private List<PartyMemberGroup> partyMembers;
	private VehicleGroup vehicle;
	
	private EncounterNotification currentEncounterNotification;
	private Modal encounterModal;
	
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
		
		hud = new TrailHUD(container, TrailHUD.Mode.TRAIL, new HUDListener());
		showHUD(hud);
		
		int toolbarXMargin = 10;
		toolbar = new Toolbar(container, container.getWidth(), 40);
		hudLayer.add(toolbar, hud.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.TOPLEFT);
		
		Label paceLabel = new Label(container, FontStore.get().getFont(FontID.FIELD), Color.white, ConstantStore.get("GENERAL", "PACE_LABEL"));
		paceLabel.setAlignment(Alignment.LEFT);
		Label rationsLabel = new Label(container, FontStore.get().getFont(FontID.FIELD), Color.white, ConstantStore.get("GENERAL", "RATIONS_LABEL"));
		rationsLabel.setAlignment(Alignment.LEFT);
		
		toolbar.add(paceLabel, toolbar.getPosition(ReferencePoint.CENTERLEFT), ReferencePoint.CENTERLEFT, toolbarXMargin, -2);

		int segmentedControlWidth = (toolbar.getWidth() - paceLabel.getWidth() - rationsLabel.getWidth() - toolbarXMargin * 6) / 2;

		String[] paceLabels = new String[Party.Pace.values().length];
		int currentPaceIndex = -1;
		for (int i = 0; i < paceLabels.length; i++) {
			paceLabels[i] = Party.Pace.values()[i].toString();
			if (party.getPace() == Party.Pace.values()[i]) {
				currentPaceIndex = i;
			}
		}
		
		paceSegmentedControl = new SegmentedControl(container, segmentedControlWidth, toolbar.getHeight() - 14, 1, 3, 0, true, 1, paceLabels);
		paceSegmentedControl.setSelection(new int[]{currentPaceIndex});
		toolbar.add(paceSegmentedControl, paceLabel.getPosition(ReferencePoint.CENTERRIGHT), ReferencePoint.CENTERLEFT, toolbarXMargin, 0);
		paceSegmentedControl.addListener(new ToolbarComponentListener());
		
		toolbar.add(rationsLabel, paceSegmentedControl.getPosition(ReferencePoint.CENTERRIGHT), ReferencePoint.CENTERLEFT, toolbarXMargin * 2, 0);
		
		String[] rationLabels = new String[Party.Rations.values().length];
		int currentRationIndex = -1;
		for (int i = 0; i < rationLabels.length; i++) {
			rationLabels[i] = Party.Rations.values()[i].toString();
			if (party.getRations() == Party.Rations.values()[i]) {
				currentRationIndex = i;
			}
		}
		rationsSegmentedControl = new SegmentedControl(container, segmentedControlWidth, toolbar.getHeight() - 14, 1, 3, 0, true, 1, rationLabels);
		rationsSegmentedControl.setSelection(new int[]{currentRationIndex});
		toolbar.add(rationsSegmentedControl, rationsLabel.getPosition(ReferencePoint.CENTERRIGHT), ReferencePoint.CENTERLEFT, toolbarXMargin, 0);
		rationsSegmentedControl.addListener(new ToolbarComponentListener());
		
		sky = SceneryFactory.getSky(container, party.getTime().getTime());
		backgroundLayer.add(sky);
		
		parallaxPanel = SceneryFactory.getScenery(container);
		ground = SceneryFactory.getGround(container);
		parallaxPanel.add(ground, parallaxPanel.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT);
		trail = SceneryFactory.getTrail(container);
		parallaxPanel.add(trail, ground.getPosition(ReferencePoint.CENTERLEFT), ReferencePoint.CENTERLEFT);
		ParallaxComponent hillA = SceneryFactory.getHillA(container);
		parallaxPanel.add(hillA, ground.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.BOTTOMLEFT);
		ParallaxComponent hillB = SceneryFactory.getHillB(container);
		parallaxPanel.add(hillB, ground.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.BOTTOMLEFT);
		
		backgroundLayer.add(parallaxPanel);
		
		partyMembers = new ArrayList<PartyMemberGroup>();
		for (int i = 0; i < party.getPartyComponentDataSources().size(); i++) {
			partyMembers.add(new PartyMemberGroup(container, party.getPartyComponentDataSources().get(i)));
		}
		int partyMembersWidth = 0;
		int partyMembersHeight = partyMembers.get(0).getHeight();
		int partyMembersPadding = 10;
		for (PartyMemberGroup pg : partyMembers) {
			partyMembersWidth += pg.getWidth();
			partyMembersWidth += partyMembersPadding;
		}
		partyMembersWidth -= partyMembersPadding;
		
		int vehicleWidth = 0;
		if (party.getVehicle() != null) {
			vehicle = new VehicleGroup(container, party.getVehicle());
			vehicleWidth = vehicle.getWidth();
		}
		
		mainLayer.addAsRow(partyMembers.iterator(), trail.getPosition(ReferencePoint.BOTTOMRIGHT), -partyMembersWidth - 15 - vehicleWidth, -partyMembersHeight - 25, 10);
		
		if (vehicle != null) {
			mainLayer.add(vehicle, partyMembers.get(partyMembers.size() - 1).getPosition(ReferencePoint.BOTTOMRIGHT), ReferencePoint.BOTTOMLEFT);
		}
		
//		partyComponent = new PartyComponent(container, container.getWidth(), parallaxPanel.getHeight(), party.getPartyComponentDataSources());
//		mainLayer.add(partyComponent, mainLayer.getPosition(ReferencePoint.BOTTOMRIGHT), ReferencePoint.BOTTOMRIGHT, 0, PARTY_Y_OFFSET);
		
		clickCounter = 0;
		
		setState(new WalkingState());
		
		adjustSetting();
		
		// Simulate attempts at adding stuff, just like update(int delta) will in the TrailSceneState classes
		// This exact formula is kind of a guess, but it seems to work well
		int numberAttemptsToAddStuff = ground.getMaxElapsedTime() * container.getWidth() * 2 / CLICK_WAIT_TIME;
		for (int i = 0; i < numberAttemptsToAddStuff; i++) {
			if (SceneryFactory.shouldAddCloud()) {
				ParallaxComponent cloud = SceneryFactory.getCloud(container, true);
				parallaxPanel.add(cloud, toolbar.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.TOPLEFT, 0, 0);
			}
			if (SceneryFactory.shouldAddTree()) {
				ParallaxComponent tree = SceneryFactory.getTree(container, true);
				int yOffset = (int) (tree.getScale() * 20) / 2;;
				parallaxPanel.add(tree, ground.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.BOTTOMLEFT, 0, yOffset);
			}
		}
}
		
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		state.update(container, delta);
	}
	
	private void randomAnimalSound() {
		Set<String> sounds = SoundStore.get().getPlayingSounds();
		if(!sounds.contains("CowMoo") 
				&& !sounds.contains("Donkey") 
				&& !sounds.contains("HorseWhinny")) {
			int random = new Random().nextInt(10000);
			if(random < party.getNumberOfAnimals(ItemType.OX)) {
				SoundStore.get().playSound("CowMoo", .25f);
				return;
			}
			random -= party.getNumberOfAnimals(ItemType.OX);
			if (random < party.getNumberOfAnimals(ItemType.MULE)) {
				SoundStore.get().playSound("Donkey", .25f);
				return;
			}
			random -= party.getNumberOfAnimals(ItemType.MULE);
			if (random < party.getNumberOfAnimals(ItemType.HORSE)) {
				SoundStore.get().playSound("HorseWhinny", .25f);
				return;
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
		
		if (encounterMessage != null) {
			modalMessage.append(encounterMessage);
		}
		
		if (modalMessage.length() != 0) {
			SoundStore.get().stopSound("Steps");
			encounterModal = new ChoiceModal(container,
					this,
					modalMessage.toString().trim(),
					2);
			encounterModal.setButtonText(encounterModal.getCancelButtonIndex(), ConstantStore.get("TRAIL_SCENE", "CAMP"));
			encounterModal.setButtonText(encounterModal.getCancelButtonIndex() + 1, ConstantStore.get("GENERAL", "CONTINUE"));

			showModal(encounterModal);
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
		ParallaxComponent.setMaxElapsedTimes((int) map(party.getPace().getSpeed(), Party.Pace.STEADY.getSpeed(), Party.Pace.GRUELING.getSpeed(), NEAR_MAX_ELAPSED_TIME_SLOW, NEAR_MAX_ELAPSED_TIME_FAST), FAR_MAX_ELAPSED_TIME);
	
		// Because we changed the max elapsed times, we have to update the new max elapsed time for each sprite
		for (ParallaxComponent sprite : parallaxPanel.getParallaxComponents()) {
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
	public void dismissModal(Modal modal, int button) {
		super.dismissModal(modal, button);
		SoundStore.get().stopAllSound();
		if (button == modal.getCancelButtonIndex()) {
			setState(new CampState());
		}
		
		if (modal == encounterModal) {
			if (currentEncounterNotification.getSceneID() != null) {
				SoundStore.get().stopSound("Steps");
				GameDirector.sharedSceneListener().requestScene(currentEncounterNotification.getSceneID(), this, false);
//				setState(new RiverState());
			}
		}
	}
	
	private void setState(TrailSceneState state) {
		this.state = state;
		state.init();
	}
	
	private abstract class TrailSceneState {				
		public abstract void init();
		
		public void update(GameContainer container, int delta) throws SlickException {
			if(party.getTime().getTime() == 19) {
				if(SoundStore.get().getPlayingSounds() == null || !SoundStore.get().getPlayingSounds().contains("WolfHowl"))
					SoundStore.get().playSound("WolfHowl");
			}	
			if(party.getTime().getTime() >= 19 || party.getTime().getTime() < 5) {
				if(SoundStore.get().getPlayingMusic() == null || !SoundStore.get().getPlayingMusic().equals("NightTheme")) {
					SoundStore.get().loopMusic("NightTheme");
				}
			} else if(SoundStore.get().getPlayingMusic() == null || !SoundStore.get().getPlayingMusic().equals("DayTheme")) {
					SoundStore.get().loopMusic("DayTheme");
			}
			randomAnimalSound();
			
			if (isPaused()) {
				return;
			}
			
			
			timeElapsed += delta;
			
			if (party.getTrail().getConditionPercentage() == 0.0) {
				party.setLocation(party.getTrail().getDestination());
				SoundStore.get().stop();
				GameDirector.sharedSceneListener().requestScene(SceneID.TOWN, TrailScene.this, true);
			}
			
			if (skyAnimatingColor != null) {
				skyAnimatingColor.update(delta);
			}
			mainLayer.update(delta);
			
			parallaxPanel.update(delta);
			
			if (timeElapsed % CLICK_WAIT_TIME < timeElapsed) {
				clickCounter++;
				hud.updateNotifications();
				timeElapsed = 0;
			}
			
			if (clickCounter >= STEP_COUNT_TRIGGER) {
				party.getTime().advanceTime();
				clickCounter = 0;
				adjustSetting();
			}
		}
	}
	
	private abstract class MovingState extends TrailSceneState {
		public static final int DEER_OFFSET = 10;

		public void init() {
			SoundStore.get().stopSound("Steps");

			hud.setMode(TrailHUD.Mode.TRAIL);
			
			for (ParallaxComponent pc : parallaxPanel.getParallaxComponents()) {
				pc.setPaused(false);
			}
		}
		
		@Override
		public void update(GameContainer container, int delta) throws SlickException {
			super.update(container, delta);
			
			if (isPaused()) {
				return;
			}
			
			for (PartyMemberGroup pg : partyMembers) {
				pg.update(delta);
			}
			
			if (vehicle != null) {
				vehicle.update(delta);
			}
			
			if (!SoundStore.get().getPlayingSounds().contains("Steps")) {
				SoundStore.get().playSound("Steps");
			}
			
			boolean showingNotifications = !hud.isNotificationsEmpty();
			if (showingNotifications) {
				return;
			}
			
			List<Notification> notifications = party.walk();
			hud.updatePartyInformation(party.getTime().get12HourTime(), party.getTime().getDayMonthYear());
			if (party.getPartyMembers().isEmpty()) {
				SoundStore.get().stopAllSound();
				GameDirector.sharedSceneListener().requestScene(SceneID.GAMEOVER, TrailScene.this, true);
			}
			
			hud.addNotification("" + party.getTrail().getRoughDistanceToGo() + party.getTrail().getDestination().getName());
			if(party.getTime().getTime() != 19 && party.getTime().getTime() != 5 && 
				party.getTrail().getConditionPercentage() != 1 && 
				party.getTrail().getConditionPercentage() != 0) {
				
				EncounterNotification encounterNotification = randomEncounterTable.getRandomEncounter(party.getTime().getTimeOfDay().ordinal());
				handleNotifications(notifications, encounterNotification.getNotification().getMessage());
				currentEncounterNotification = encounterNotification;
			}
		}
	}
	
	private class WalkingState extends MovingState {
		private int counter;
		public void update(GameContainer container, int delta) throws SlickException {
			super.update(container, delta);
			
			if (isPaused()) {
				return;
			}
			
			counter += delta;
			if (counter >= CLICK_WAIT_TIME) {
				if (SceneryFactory.shouldAddCloud()) {
					ParallaxComponent cloud = SceneryFactory.getCloud(container, false);
					int yOffset = (int) (cloud.getScale() * 20) / 2;;
					parallaxPanel.add(cloud, toolbar.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.TOPLEFT, -cloud.getWidth(), yOffset);
				}
				if (SceneryFactory.shouldAddTree()) {
					ParallaxComponent tree = SceneryFactory.getTree(container, false);
					int yOffset = (int) (tree.getScale() * 20) / 2;;
					parallaxPanel.add(tree, ground.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.BOTTOMLEFT, -tree.getWidth(), yOffset);
				}
				if (SceneryFactory.shouldAddDeer()) {
					ParallaxComponent deer = SceneryFactory.getDeer(container);
					parallaxPanel.add(deer, ground.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.BOTTOMLEFT, -deer.getWidth(), DEER_OFFSET);
				}
				
				counter = 0;
			}
		}
	}

	/*
	private class RiverState extends MovingState {
		public void update(GameContainer container, int delta) throws SlickException {
			super.update(container, delta);
			
			
		}
	}
	*/
	
	private class CampState extends TrailSceneState {
		
		public void init() {
			hud.setMode(TrailHUD.Mode.CAMP);
			
			for (ParallaxComponent pc : parallaxPanel.getParallaxComponents()) {
				pc.setPaused(true);
			}
		}
		
		@Override
		public void update(GameContainer container, int delta) throws SlickException {
			super.update(container, delta);
		}
	}
	
	private class HUDListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent component) {			
			if (component == hud.getMenuButton()) {
				setState(new CampState());
			} else if (component == hud.getInventoryButton()) {
				GameDirector.sharedSceneListener().requestScene(SceneID.PARTYINVENTORY, TrailScene.this, false);
			} else if (component == hud.getMapButton()) {
				GameDirector.sharedSceneListener().requestScene(SceneID.MAP, TrailScene.this, false);
			} else if (component == hud.getHuntButton()) {
				GameDirector.sharedSceneListener().requestScene(SceneID.HUNT, TrailScene.this, false);
			} else if (component == hud.getLeaveButton()) {
				setState(new WalkingState());
			}
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
