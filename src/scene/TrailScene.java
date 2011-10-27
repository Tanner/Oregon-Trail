package scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Notification;
import model.Party;
import model.RandomEncounterTable;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import component.HUD;
import component.Panel;
import component.ParallaxPanel;
import component.Positionable.ReferencePoint;
import component.modal.ChoiceModal;
import component.modal.MessageModal;
import component.modal.Modal;
import component.sprite.ParallaxSprite;
import core.GameDirector;
import core.Logger;

public class TrailScene extends Scene {
	public static final SceneID ID = SceneID.TRAIL;
	
	private static final int CLICK_WAIT_TIME = 1000;
	private static final int STEP_COUNT_TRIGGER = 2;
	
	private static final int GROUND_DISTANCE = 10;
	private static final int TREE_DISTANCE = 200;
	
	private int clickCounter;
	private int timeElapsed;
	private boolean paused;
	
	private ParallaxSprite ground;
	private ArrayList<ParallaxSprite> trees;
	
	private Party party;
	private RandomEncounterTable randomEncounterTable;
	
	private HUD hud;
	
	public TrailScene(Party party, RandomEncounterTable randomEncounterTable) {
		this.party = party;
		this.randomEncounterTable = randomEncounterTable;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);		
		
		backgroundLayer.add(new Panel(container, new Color(0x66a9c4)));
		
		ParallaxPanel parallaxPanel = new ParallaxPanel(container, container.getWidth(), container.getHeight());
		
		ParallaxSprite.MAX_DISTANCE = TREE_DISTANCE;
		
		ground = new ParallaxSprite(container, container.getWidth(), new Image("resources/graphics/ground/grass.png", false, Image.FILTER_NEAREST), GROUND_DISTANCE, false);
		parallaxPanel.add(ground, backgroundLayer.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT);
		
		trees = new ArrayList<ParallaxSprite>();
		
		Random random = new Random();
		
		for (int i = 0; i < 20; i++) {
			int distance = random.nextInt(TREE_DISTANCE);
			int offset = 20;
			
			if (distance <= TREE_DISTANCE / 3) {
				distance = random.nextInt(GROUND_DISTANCE);
				
				offset += GROUND_DISTANCE - distance;
			}
			
			System.out.print(distance+" ");
			
			ParallaxSprite tree = new ParallaxSprite(container, 96, new Image("resources/graphics/ground/tree.png", false, Image.FILTER_NEAREST), distance, true);
			trees.add(tree);

			parallaxPanel.add(tree, ground.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.BOTTOMLEFT, 0, offset);
		}

		hud = new HUD(container, party, new HUDListener());
		showHUD(hud);
		
		backgroundLayer.add(parallaxPanel);
		
		clickCounter = 0;
	}
		
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(!isPaused()) {
			timeElapsed += delta;
			
			if (timeElapsed % CLICK_WAIT_TIME < timeElapsed) {
				clickCounter++;
				hud.updateNotifications();
				timeElapsed = 0;
			}
			
			ground.move(delta);
			
			for (ParallaxSprite tree : trees) {
				tree.move(delta);
			}
			
			paused = !hud.isNotificationsEmpty();
			
			if (paused) {
				return;
			}
			
			if (clickCounter >= STEP_COUNT_TRIGGER) {
				List<Notification> notifications = party.walk();
				
				if (party.getPartyMembers().isEmpty()) {
					GameDirector.sharedSceneListener().requestScene(SceneID.GAMEOVER, this, true);
				}
				Logger.log("Current distance travelled = " + party.getLocation(), Logger.Level.INFO);
				GameDirector.sharedSceneListener().requestScene(randomEncounterTable.getRandomEncounter(), this, false);
	
				hud.updatePartyInformation();
				List<String> messages = new ArrayList<String>();
				for(Notification notification : notifications) {
					if(notification.getIsModal()) {
						ChoiceModal campModal = new ChoiceModal(container, this, notification.getMessage());
						campModal.setDismissButtonText("Camp");
						campModal.setCancelButtonText("Continue");
						showModal(campModal);
					} else {
						messages.add(notification.getMessage());
					}
				}
				hud.addNotifications(messages);
			
				clickCounter = 0;
			}
		}
	}
	
	@Override
	public int getID() {
		return ID.ordinal();
	}
	
	@Override
	public void dismissModal(Modal modal, boolean cancelled) {
		super.dismissModal(modal, cancelled);
		if (!cancelled) {
			GameDirector.sharedSceneListener().requestScene(SceneID.CAMP, this, false);
		}
	}
	
	private class HUDListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent component) {
			GameDirector.sharedSceneListener().requestScene(SceneID.CAMP, TrailScene.this, false);
		}
	}
}
