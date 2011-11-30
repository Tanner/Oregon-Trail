package scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Party;
import model.Person;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import component.Panel;
import component.Positionable;
import component.Positionable.ReferencePoint;
import component.SegmentedControl;
import component.modal.ComponentModal;
import component.modal.MessageModal;
import component.modal.Modal;
import component.parallax.ParallaxPanel;
import component.parallax.ParallaxComponent;
import component.parallax.ParallaxComponentLoop;
import component.sprite.AnimatingSprite;
import component.sprite.Sprite;
import core.GameDirector;
import core.ConstantStore;
import core.SoundStore;

/**
 * A river crossing scene where the user is given a few choices
 * to cross a river.
 */
public class RiverScene extends Scene {
	public static final SceneID ID = SceneID.RIVER;
	
	private final int NUM_CLOUDS = 5;
	private final int CLOUD_OFFSET = 20;
	private final int CLOUD_DISTANCE_VARIANCE = 10;
	private final int CLOUD_OFFSET_VARIANCE = 10;
	private final int CLOUD_DISTANCE = 300;
	
	private final int MAX_RIVER_DEPTH = 8;
	private final int MAX_TOLL = 1000;
	private final int MIN_TOLL = 100;
	private final int FORD_DANGER_DEPTH = 3;
	private final int CAULK_DANGER_DEPTH = 4;
	private final int RIVER_CROSS_TIME = 3000;
	private final int BRIDGE_CROSS_TIME = 5500;
	

	private ParallaxPanel riverParallaxPanel;
	private ParallaxPanel cloudParallaxPanel;
	
	private AnimatingSprite wagon;
	private AnimatingSprite wagonWheels;
	
	private Sprite bridge;
	
	private SegmentedControl crossingChoicesControl;
	private ComponentModal<SegmentedControl> crossingChoicesModal;
	private MessageModal successModal;
	private Modal nextModal;
	
	private Party party;
	
	private int riverDepth;
	private int tollPrice;
	private int crossTime;
	
	private String successModalMessage;

	private boolean haveWaited;
	private boolean waiting;
	private boolean crossingBridge;
	private boolean crossingRiver;
	private boolean didTakeDamage;
	private boolean ani;
	
	public RiverScene(Party party) {
		this.party = party;
		riverDepth = (int) (Math.random() * MAX_RIVER_DEPTH) + 1;
		tollPrice = (int) (Math.random() * MAX_TOLL - MIN_TOLL) + MIN_TOLL;
		haveWaited = false;
		waiting = false;
		crossTime = 0;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		bridge = new Sprite(container, new Image(ConstantStore.PATH_RIVER + "bridge.png"));
		mainLayer.add(bridge, mainLayer.getPosition(ReferencePoint.CENTERCENTER), ReferencePoint.CENTERCENTER, 100, 100);
		
		//Animating wagon sprites
		Animation wagonAni = new Animation();
		wagonAni.addFrame(new Image(ConstantStore.PATH_RIVER + "wagonriver1.png"), 100);
		wagonAni.addFrame(new Image(ConstantStore.PATH_RIVER + "wagonriver2.png"), 100);
		wagonAni.addFrame(new Image(ConstantStore.PATH_RIVER + "wagonriver3.png"), 100);
		wagon = new AnimatingSprite(container, 50, wagonAni, AnimatingSprite.Direction.LEFT);
		wagonAni = new Animation();
		wagonAni.addFrame(new Image(ConstantStore.PATH_RIVER + "wagonwheels1.png"), 100);
		wagonAni.addFrame(new Image(ConstantStore.PATH_RIVER + "wagonwheels2.png"), 100);
		wagonWheels = new AnimatingSprite(container, 44, wagonAni, AnimatingSprite.Direction.LEFT);
		mainLayer.add(wagon, mainLayer.getPosition(ReferencePoint.CENTERCENTER), Positionable.ReferencePoint.CENTERCENTER, -50, 120);
		mainLayer.add(wagonWheels, mainLayer.getPosition(ReferencePoint.CENTERCENTER), Positionable.ReferencePoint.CENTERCENTER, -50, 120);
		wagonWheels.setVisible(false);
		wagon.setVisible(false);
		


		makeChoiceModal();

		Random random = new Random();
		riverParallaxPanel = new ParallaxPanel(container, container.getWidth(), container.getHeight());
		cloudParallaxPanel = new ParallaxPanel(container, container.getWidth(), container.getHeight());
		ParallaxComponent.MAX_DISTANCE = 600;
		ParallaxComponent water = new ParallaxComponentLoop(container, container.getWidth() + 1, new Image(ConstantStore.PATH_GROUND + "water.png", false, Image.FILTER_LINEAR),1);
		riverParallaxPanel.add(water, backgroundLayer.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT);
		
		//Clouds
		Image[] cloudImages = new Image[3];
		cloudImages[0] = new Image(ConstantStore.PATH_BKGRND + "cloud_a.png", false, Image.FILTER_NEAREST);
		cloudImages[1] = new Image(ConstantStore.PATH_BKGRND + "cloud_b.png", false, Image.FILTER_NEAREST);
		cloudImages[2] = new Image(ConstantStore.PATH_BKGRND + "cloud_c.png", false, Image.FILTER_NEAREST);
		
		for (int i = 0; i < NUM_CLOUDS; i++) {
			int distance = CLOUD_DISTANCE + random.nextInt(CLOUD_DISTANCE_VARIANCE * 2) - CLOUD_DISTANCE_VARIANCE;
			int cloudImage = random.nextInt(cloudImages.length);
			
			int offset = CLOUD_OFFSET + random.nextInt(CLOUD_OFFSET_VARIANCE * 2) - CLOUD_OFFSET_VARIANCE;
			
			ParallaxComponent cloud = new ParallaxComponent(container, cloudImages[cloudImage], distance, true);
			cloudParallaxPanel.add(cloud, backgroundLayer.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT, 0, offset);
		}
		
		Panel backgroundColor = new Panel(container, new Color(0x579cdd));
		backgroundLayer.add(backgroundColor, backgroundLayer.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT);
		backgroundLayer.add(riverParallaxPanel, backgroundLayer.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT, 0, 200);
		Sprite ground = new Sprite(container, container.getWidth() + 1, new Image(ConstantStore.PATH_GROUND + "riveredge.png", false, Image.FILTER_NEAREST));
		backgroundLayer.add(ground, backgroundLayer.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT);
		Sprite mountain = new Sprite(container, 800, new Image(ConstantStore.PATH_BKGRND + "hill_b.png", false, Image.FILTER_NEAREST));
		backgroundLayer.add(mountain, backgroundLayer.getPosition(ReferencePoint.TOPRIGHT), ReferencePoint.TOPRIGHT, 0, 50);
		mountain = new Sprite(container, 800, new Image(ConstantStore.PATH_BKGRND + "hill_a.png", false, Image.FILTER_NEAREST));
		backgroundLayer.add(mountain, backgroundLayer.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT, 0, 50);
		ground = new Sprite(container, container.getWidth() + 1, new Image(ConstantStore.PATH_GROUND + "riveredgetop.png", false, Image.FILTER_NEAREST));
		backgroundLayer.add(ground, backgroundLayer.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
		backgroundLayer.add(cloudParallaxPanel, backgroundLayer.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT);
	}
	
	
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		for (ParallaxComponent sprite : riverParallaxPanel.getParallaxComponents()) {
			sprite.update(delta);
		}
		for (ParallaxComponent sprite : cloudParallaxPanel.getParallaxComponents()) {
			sprite.update(delta);
		}
		if ( !isPaused() ) {
			crossTime += delta;
			if (waiting) {
				if ( crossTime > RIVER_CROSS_TIME ) {
					crossTime = 0;
					waiting = false;
					wagon.setVisible(true);
					showModal(nextModal);
				}
			}
			else {
				wagon.update(container, delta);
				if ( crossingRiver ) {
					ani = !ani;
					if (ani)
						wagon.setLocation(wagon.getX(), wagon.getY() - 1);
					if ( crossTime > RIVER_CROSS_TIME ) {
						if (didTakeDamage) {
							SoundStore.get().playSound("Splash");
						}
						crossTime = 0;
						showModal(nextModal);
					}
				}
				else if (crossingBridge) {
					wagon.setLocation(wagon.getX(), wagon.getY() - 1);
					if ( crossTime > BRIDGE_CROSS_TIME ) {
						crossTime = 0;
						showModal(nextModal);
					}
				}
			}
		}
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)  {
		super.enter(container, game);
		showModal(crossingChoicesModal);
		wagon.setVisible(true);
		SoundStore.get().loopMusic("River");
	}
	
	@Override
	public int getID() {
		return ID.ordinal();
	}
	
	@Override
	public void dismissModal(Modal modal, int button) {
		super.dismissModal(modal, button);
		if (modal == crossingChoicesModal) {
			int[] choice = crossingChoicesModal.getComponent().getSelection();
			switch ( choice[0] ) {
				case 0 : ford(); break;
				case 1 : caulk(); break;
				case 2 : payToll(); break;
				case 3 : delay(); break;
			}
		}
		else if (modal == successModal) {
			SoundStore.get().stopMusic();
			GameDirector.sharedSceneListener().sceneDidEnd(this);
		}
	}
	
	/**
	 * Construct the modal with all of the river crossing choices.  Disable
	 * choices if they are not available
	 */
	public void makeChoiceModal() {
		String[] choices = {"Ford the river", "Caulk your wagon", "Pay the toll", "Wait for an hour"};
		int[] disabled = {-1};
		if (party.getMoney() < tollPrice && haveWaited)
			disabled = new int[] {2, 3};
		else if (party.getMoney() < tollPrice)
			disabled = new int[] {2};
		else if (haveWaited)
			disabled = new int[] {3};
		crossingChoicesControl = new SegmentedControl(container, 600, 150, 2, 2, 20, true, 1, choices);
		if (disabled[0] != -1)
			crossingChoicesControl.setDisabled(disabled);
		makeChoicesModalMessage();
		crossingChoicesModal = new ComponentModal<SegmentedControl>(container, this, successModalMessage, 1, crossingChoicesControl);
	}
	
	/**
	 * Construct the message for the main choices modal
	 */
	public void makeChoicesModalMessage() {
		successModalMessage = String.format("You've come to a river.  It is %d %s deep.\n" +
			"There is a bridge with a $%d toll, and you have $%d.\nWhat do you want to do?",
			riverDepth, (riverDepth == 1) ? "foot" : "feet", tollPrice, party.getMoney());
	}
	
	/**
	 * The logic for fording a river
	 */
	private void ford() {
		crossingRiver = true;
		if (riverDepth >= FORD_DANGER_DEPTH && Math.random() > 0.5) {
			successModalMessage = "Oh no!  Why would you ford a " + riverDepth +
					" foot deep river?  Your party was damaged";
			String deaths = damage();
			successModalMessage += (deaths.equals("")) ? ", but at least no one died!" :
				" and you lost " + deaths + ".";
		}
		else {
			successModalMessage = "You successfully forded the river!  Your party sighs in" +
					" relief";
		}
		successModal = new MessageModal(container, this, successModalMessage);
		nextModal = successModal;
	}
	
	/**
	 * The logic for caulking and floating across a river
	 */
	private void caulk() {
		crossingRiver = true;
		if (riverDepth >= CAULK_DANGER_DEPTH && Math.random() > 0.5) {
			successModalMessage = "Oh no!  Your caulk didn't hold up and water leaked into " +
					"your wagon.  Your party was damaged";
			String deaths = damage();
			successModalMessage += (deaths.equals("")) ? ", but at least no one died!" :
				" and you lost " + deaths + ".";
		}
		else {
			successModalMessage = "Water started seeping into your wagon just as you " +
					"reached the shore, but you make it!  You dump the water " +
					"out of your boots, take a big swill of whiskey, and get " +
					"back on the trail.";
		}
		successModal = new MessageModal(container, this, successModalMessage);
		nextModal = successModal;
	}
	
	/**
	 * The logic for caulking and floating across a river
	 */
	private void payToll() {
		crossingBridge = true;
		party.setMoney(party.getMoney() - tollPrice);
		wagon.setVisible(false);
		wagonWheels.setVisible(true);
		wagon = wagonWheels;
		wagon.setLocation(wagon.getX() + 150, 576);
		SoundStore.get().playSound("RK");
		successModalMessage = "Your party decided to take the easy way out and " +
				"pay the the bridge toll.  Your party members thank you.";
		successModal = new MessageModal(container, this, successModalMessage);
		nextModal = successModal;
	}
	
	/**
	 * The logic for waiting to see if the river goes down
	 */
	private void delay() {
		haveWaited = true;
		waiting = true;
		wagon.setVisible(false);
		riverDepth = (int) (Math.random() * MAX_RIVER_DEPTH) + 1;
		makeChoiceModal();
		nextModal = crossingChoicesModal;
	}

	/**
	 * Damage all party members by a random amount.
	 * @return A String representation of the deaths caused by damage.
	 */
	public String damage() {
		ArrayList<String> deadMembers = new ArrayList<String>();
		didTakeDamage = true;
		if (party.getVehicle() != null) {
			party.getVehicle().decreaseStatus((int)(Math.random()*100));
		}
		List<Person> partyMembers = party.getPartyMembers();
		for ( int i = partyMembers.size() - 1; i >= 0; i-- ) {
			Person person = partyMembers.get(i);
			boolean stillAlive = party.decreaseHealth(person,(int)(Math.random()*100));
			if ( !stillAlive ) {
				deadMembers.add(person.getName());
			}
		}
		
		String deaths = "";
		if ( deadMembers.size() == 1 )
			deaths += deadMembers.get(0);
		else if (deadMembers.size() == 2)
			deaths += deadMembers.get(0) + " and " + deadMembers.get(1);
		else if (deadMembers.size() > 2) {
			for (int i = 0; i < deadMembers.size(); i++) {
				if ( i == deadMembers.size() -1 )
					deaths += "and ";
				deaths += deadMembers.get(i);
				if ( i != deadMembers.size() - 1)
					deaths += ", ";
			}
		}
		return deaths;
	}
}
