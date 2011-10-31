package scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Notification;
import model.Party;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;
import scene.encounter.*;
import component.AnimatingColor;
import component.HUD;
import component.Panel;
import component.ParallaxPanel;
import component.Positionable.ReferencePoint;
import component.modal.ChoiceModal;
import component.modal.Modal;
import component.sprite.ParallaxSprite;
import component.sprite.ParallaxSpriteLoop;
import core.ConstantStore;
import core.GameDirector;
import core.Logger;
import core.SoundStore;

public class TrailScene extends Scene {
	public static final SceneID ID = SceneID.TRAIL;
	
	private static final int CLICK_WAIT_TIME = 1000;
	private static final int STEP_COUNT_TRIGGER = 2;
	
	private static final int HILL_DISTANCE_A = 300;
	private static final int HILL_DISTANCE_B = 600;
	private static final int CLOUD_DISTANCE = 400;
	private static final int GROUND_DISTANCE = 10;
	private static final int TREE_DISTANCE = 200;
	private static final int DEER_DISTANCE = 150;
	
	private static final int NUM_TREES = 40;
	private static final int TREE_OFFSET = 20;
	
	private static final int NUM_CLOUDS = 5;
	private static final int CLOUD_OFFSET = 20;
	private static final int CLOUD_DISTANCE_VARIANCE = 10;
	private static final int CLOUD_OFFSET_VARIANCE = 10;
	
	private static final int DEER_OFFSET = 10;
	
	private int clickCounter;
	private int timeElapsed;
	private boolean paused;
		
	private Panel sky;
	private ParallaxPanel parallaxPanel;
	
	private Party party;
	private RandomEncounterTable randomEncounterTable;
	
	private HUD hud;
	
	private AnimatingColor skyAnimatingColor;
	
	public TrailScene(Party party, RandomEncounterTable randomEncounterTable) {
		this.party = party;
		this.randomEncounterTable = randomEncounterTable;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		hud = new HUD(container, party, new HUDListener());
		showHUD(hud);
		
		sky = new Panel(container, skyColorForHour(party.getTime().getTime()));
		backgroundLayer.add(sky);
		
		parallaxPanel = new ParallaxPanel(container, container.getWidth(), container.getHeight());
		Random random = new Random();
		
		ParallaxSprite.MAX_DISTANCE = HILL_DISTANCE_B;
		
		// Ground
		ParallaxSprite ground = new ParallaxSpriteLoop(container, container.getWidth() + 1, new Image("resources/graphics/ground/grass.png", false, Image.FILTER_NEAREST), GROUND_DISTANCE);
		parallaxPanel.add(ground, backgroundLayer.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT);
		
		// Hills
		ParallaxSprite hillA = new ParallaxSpriteLoop(container, container.getWidth(), new Image("resources/graphics/backgrounds/hill_a.png", false, Image.FILTER_NEAREST), HILL_DISTANCE_A);
		parallaxPanel.add(hillA, ground.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.BOTTOMLEFT);
		
		ParallaxSprite hillB = new ParallaxSpriteLoop(container, container.getWidth(), new Image("resources/graphics/backgrounds/hill_b.png", false, Image.FILTER_NEAREST), HILL_DISTANCE_B);
		parallaxPanel.add(hillB, ground.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.BOTTOMLEFT);
		
		// Clouds
		Image[] cloudImages = new Image[3];
		cloudImages[0] = new Image("resources/graphics/backgrounds/cloud_a.png", false, Image.FILTER_NEAREST);
		cloudImages[1] = new Image("resources/graphics/backgrounds/cloud_b.png", false, Image.FILTER_NEAREST);
		cloudImages[2] = new Image("resources/graphics/backgrounds/cloud_c.png", false, Image.FILTER_NEAREST);
		
		for (int i = 0; i < NUM_CLOUDS; i++) {
			int distance = CLOUD_DISTANCE + random.nextInt(CLOUD_DISTANCE_VARIANCE * 2) - CLOUD_DISTANCE_VARIANCE;
			int cloudImage = random.nextInt(cloudImages.length);
			
			int offset = CLOUD_OFFSET + random.nextInt(CLOUD_OFFSET_VARIANCE * 2) - CLOUD_OFFSET_VARIANCE;
			
			ParallaxSprite cloud = new ParallaxSprite(container, cloudImages[cloudImage], distance, true);
			parallaxPanel.add(cloud, hud.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.TOPLEFT, 0, offset);
		}
		
		// Trees
		for (int i = 0; i < NUM_TREES; i++) {
			int distance = random.nextInt(TREE_DISTANCE);
			int offset = TREE_OFFSET;
			
			if (distance <= TREE_DISTANCE / 3) {
				distance = random.nextInt(GROUND_DISTANCE);
				
				offset += GROUND_DISTANCE - distance;
			}
			
			ParallaxSprite tree = new ParallaxSprite(container, 96, new Image("resources/graphics/ground/tree.png", false, Image.FILTER_NEAREST), 0, TREE_DISTANCE, distance, true);
			
			offset -= (int) (tree.getScale() * offset) / 2;

			parallaxPanel.add(tree, ground.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.BOTTOMLEFT, 0, offset);
		}
		
		ParallaxSprite deer = new ParallaxSprite(container, new Image("resources/graphics/animals/deer.png", false, Image.FILTER_NEAREST), DEER_DISTANCE, true);
		parallaxPanel.add(deer, ground.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.BOTTOMLEFT, 0, DEER_OFFSET);
		
		// Add to panel stuff and other things
		backgroundLayer.add(parallaxPanel);
		
		clickCounter = 0;
		
		adjustSetting();
}
		
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(!isPaused()) {
			timeElapsed += delta;
			if (party.getTrail().getConditionPercentage() == 0.0) {
				party.setLocation(party.getTrail().getDestination());
				SoundStore.get().stopAllSound();
				GameDirector.sharedSceneListener().requestScene(SceneID.TOWN, this, true);
			} else if(!SoundStore.get().getPlayingSounds().contains("Steps")) {
				SoundStore.get().playSound("Steps");
			}
			
			if (skyAnimatingColor != null) {
				skyAnimatingColor.update(delta);
			}
			backgroundLayer.update(delta);
			
			if (timeElapsed % CLICK_WAIT_TIME < timeElapsed) {
				clickCounter++;
				hud.updateNotifications();
				timeElapsed = 0;
			}
			
			for (ParallaxSprite sprite : parallaxPanel.getSprites()) {
				sprite.move(delta);
			}
			
			paused = !hud.isNotificationsEmpty();
			
			if (paused) {
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
					SoundStore.get().stopAllSound();
					GameDirector.sharedSceneListener().requestScene(encounterNotification.getSceneID(), this, false);

				clickCounter = 0;
				
				adjustSetting();
			}
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
			SoundStore.get().stopAllSound();
			ChoiceModal campModal = new ChoiceModal(container, this, modalMessage.toString().trim());
			campModal.setCancelButtonText(ConstantStore.get("TRAIL_SCENE", "CAMP"));
			campModal.setDismissButtonText(ConstantStore.get("GENERAL", "CONTINUE"));
			showModal(campModal);
		}
		
		hud.addNotifications(messages);
	}
	
	private void adjustSetting() {
		int hour = party.getTime().getTime();
		
		hud.setTime(party.getTime().get12HourTime());
		hud.setDate(party.getTime().getDayMonthYear());
		skyAnimatingColor = new AnimatingColor(skyColorForHour(hour-1),
				skyColorForHour(hour), CLICK_WAIT_TIME * STEP_COUNT_TRIGGER);
		sky.setBackgroundColor(skyAnimatingColor);
		
		AnimatingColor backgroundOverlayAnimatingColor = new AnimatingColor(backgroundOverlayColorForHour(hour-1),
				backgroundOverlayColorForHour(hour), CLICK_WAIT_TIME * STEP_COUNT_TRIGGER);
		this.backgroundLayer.setOverlayColor(backgroundOverlayAnimatingColor);
	}
	
	private Color skyColorForHour(int hour) {
		switch (hour + 1) {
			case 6:
				return new Color(0xeba7a4); // light pink
			case 7:
				return new Color(0xebd0a4); // light orange
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
				return new Color(0x579cdd); // light blue
			case 18:
			case 19:
				return new Color(0xdd90a4); // pink
			case 20:
				return new Color(0x4a3b48); // dark purple
			default:
				return Color.black;
		}
	}
	
	private Color backgroundOverlayColorForHour(int hour) {
		switch (hour + 1) {
			case 7:
				return new Color(0, 0, 0, .3f);
			case 8:
				return new Color(0, 0, 0, .1f);
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
				return new Color(0, 0, 0, 0f);
			case 18:
			case 19:
				return new Color(0, 0, 0, .1f);
			case 20:
				return new Color(0, 0, 0, .3f);
			default:
				return new Color(0, 0, 0, .5f);
		}
	}
	
	@Override
	public int getID() {
		return ID.ordinal();
	}
	
	@Override
	public void dismissModal(Modal modal, boolean cancelled) {
		super.dismissModal(modal, cancelled);
		SoundStore.get().stopMusic();
		if (cancelled) {
			SoundStore.get().stopAllSound();
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
}
