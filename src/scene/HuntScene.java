package scene;

//import java.util.Map;

import java.awt.Cursor;
import java.util.ArrayList;
import java.util.Random;

import model.HuntingMap;

//import model.huntingMap.TerrainObject;
import model.Item;
import model.Party;
import model.Person;
import model.WorldMap;
import model.huntingMap.PreyCow;
import model.huntingMap.PreyPig;
import model.huntingMap.TerrainObject;
import model.item.ItemType;

import org.newdawn.slick.Animation;
//import org.newdawn.slick.Color;
//import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;


import component.Component;
import component.HuntingGroundsComponent;
import component.Panel;
import component.TerrainComponent;
import component.Positionable.ReferencePoint;
import component.hud.HuntHUD;
import component.sprite.AnimatingSprite;
import component.sprite.AnimatingSprite.Direction;
import component.sprite.HunterAnimatingSprite;
import component.sprite.PreyAnimatingSprite;
import component.sprite.Sprite;

import core.ConstantStore;
//import core.FontStore;
import core.GameDirector;
import core.ImageStore;
import core.SoundStore;

/**
 * Hunt scene is where you kill animals and get food.
 */
public class HuntScene extends Scene {
	public static final SceneID ID = SceneID.HUNT;
	
	/**maximum number of any particular prey creature in the hunt scene*/
	private final int MAXPREY = 20;
	/**the hud for this scene to hold important information and give access to camp and inventory*/
	private HuntHUD hud;
	/**a toggle-like int that will help manage a busy scene by alternating updates between mobs and player*/
	private int toggleCount;
	/**the world map, to be used to determine actual in-world location*/
	private WorldMap worldMap;
	/**the reticle for the gun in the scene, moves with the mouse*/
	private Sprite reticle;
	/**the graphic representing the hunter*/
	private AnimatingSprite hunterSprite;
	/**the member of the party that is hunting - currently party leader*/
	private Person hunter;
	/**single random gen for entire scene*/
	private Random huntSceneRand;
	/**amount of available ammo this hunter has */
	private int ammoCount = 0;
	/**number of boxes of ammo this hunter has */
	private int ammoBoxes = 0;
	/**whether the gun has been cocked or not*/
	private boolean gunCocked;
	/**amount of meat acquired this hunt session*/
	private int meat = 0;
	/**the width of the map	 */
	private double mapWidth;
	/**the height of the map */
	private double mapHeight;
	/**the cow or cows in the hunt scene - may end up an array*/
	private ArrayList<PreyCow> preyCow;
	
	/**the pig or pigs in the hunt scene - may end up an array*/
	private ArrayList<PreyPig> preyPig;
	/**the panel that holds the graphical objects*/
	private HuntingGroundsComponent huntPanel;

	
	
	/**
	 * Constructs a {@code HuntScene} with a {@code Person} who will be the hunter
	 * @param hunter the single member of the party who is going to hunt.
	 */
	public HuntScene(Person hunter, WorldMap worldMap){
		this.hunter = hunter;
		this.worldMap = worldMap;		
	}
	
	public HuntScene(Party party, WorldMap worldMap){
		this.hunter = party.getPartyMembers().get(0);
		this.worldMap = worldMap;
	}
	
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		toggleCount = 0;
		huntSceneRand = new Random();
		super.init(container, game);
		SoundStore.get().stopAllSound();
		SoundStore.get().playHuntMusic();
		//gun starts cocked
		//need to press right mouse button to cock gun, left to fire
		gunCocked = true;
		
		hud = new HuntHUD(container, new HUDListener());
		
		Image retImage = ImageStore.get().IMAGES.get("HUNT_RETICLE");
		reticle = new Sprite(container, retImage.getWidth()*2, retImage.getHeight()*2, retImage);
		
		//container.setMouseCursor(retImage, retImage.getWidth()/2, retImage.getHeight()/2);
		updateHUD();
		//hud.setNotification(location.getName());
		super.showHUD(hud);			
		//Font h2 = FontStore.get().getFont(FontStore.FontID.H2);
		this.mapWidth = 2400;		//map x dimension
		this.mapHeight = 2400;	//map y dimension
		double worldMapX = 0;			//worldMap.getLocationNode.getX - these may be implemented someday. probably not
		double worldMapY = 0;			//worldMap.getLocationNode.gety
		
		int terrainChance = 10;		// chance that there's a terrain object, out of 100
		int rockChance = 50;		//chance that it's a tree vs a rock, out of 100
		double [] dblArgs = {mapWidth, mapHeight, worldMapX, worldMapY};
		int [] intArgs = {terrainChance, rockChance};
			
 		HuntingMap huntLayout = new HuntingMap(container, dblArgs, intArgs, ConstantStore.Environments.PLAINS);


 		huntPanel = new HuntingGroundsComponent(container, (int)mapWidth, (int)mapHeight, huntLayout);
 
 		hunterSprite = new HunterAnimatingSprite(container,
			//	48,
				new Animation(new Image[] {ImageStore.get().getImage("HUNTER_LEFT")}, 250),
				new Animation(new Image[] {ImageStore.get().getImage("HUNTER_RIGHT")}, 250),
				new Animation(new Image[] {ImageStore.get().getImage("HUNTER_FRONT")}, 250),
				new Animation(new Image[] {ImageStore.get().getImage("HUNTER_BACK")}, 250),
				new Animation(new Image[] {ImageStore.get().getImage("HUNTER_UPPERLEFT")}, 250),
				new Animation(new Image[] {ImageStore.get().getImage("HUNTER_UPPERRIGHT")}, 250),
				new Animation(new Image[] {ImageStore.get().getImage("HUNTER_LOWERLEFT")}, 250),
				new Animation(new Image[] {ImageStore.get().getImage("HUNTER_LOWERRIGHT")}, 250),
				AnimatingSprite.Direction.RIGHT);
				
 		int numPrey = huntSceneRand.nextInt(MAXPREY)+1;
 		System.out.println(numPrey);
 		preyCow = new ArrayList<PreyCow>();
 		preyPig = new ArrayList<PreyPig>();
 		
 		for (int prey = 0; prey < numPrey; prey ++){
			preyCow.add(prey,new PreyCow(container, game, huntSceneRand, (int)mapWidth, (int)mapHeight));
			preyPig.add(prey,new PreyPig(container, game, huntSceneRand, (int)mapWidth, (int)mapHeight));
			//displaying sprites
			System.out.println("loading the " + prey + "th pig at x= " + preyPig.get(prey).getxLocation() + " and y= " + preyPig.get(prey).getyLocation());
			huntPanel.add(preyPig.get(prey).getPreySprite(),
					huntPanel.getPosition(ReferencePoint.TOPLEFT),
					ReferencePoint.TOPLEFT,
					preyPig.get(prey).getxLocation(),
					preyPig.get(prey).getyLocation());
			
			System.out.println("loading the " + prey + "th cow at x= " + preyCow.get(prey).getxLocation() + " and y= " + preyCow.get(prey).getyLocation());
			huntPanel.add(preyCow.get(prey).getPreySprite(),
					huntPanel.getPosition(ReferencePoint.TOPLEFT),
					ReferencePoint.TOPLEFT,
					preyCow.get(prey).getxLocation(),
					preyCow.get(prey).getyLocation());

			huntPanel.setVisible(true);

 		}
 		huntPanel.displayTerrain(container, huntLayout);
 		mainLayer.add((Component)huntPanel, mainLayer.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT, (int)(-1 * mapWidth/2), (int) (-1 * mapHeight/2));

 		mainLayer.add(hunterSprite,
				mainLayer.getPosition(ReferencePoint.CENTERCENTER),
				ReferencePoint.TOPLEFT,
				20,
				25);
 
		hunterSprite.setVisible(true);

			
		mainLayer.add(reticle,
				mainLayer.getPosition(ReferencePoint.CENTERCENTER),
				ReferencePoint.CENTERCENTER,
				0,
				0);
		//build background
		
		backgroundLayer.add(new Panel(container, ImageStore.get().getImage("HUNT_GRASS")));
	}
	
	/**
	 * update the player's toon to reflect input
	 * @param container
	 * @param game
	 * @param delta
	 * @throws SlickException
	 */
	

	private void updatePlayer(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		int moveMapX;
		int moveMapY;
	
		if (moveUpperLeft(container)){
			hunterSprite.setDirectionFacing(Direction.UPPER_LEFT);
			hunterSprite.setMoving(true);
			//move map down and to right
			moveMapX = (int)(.71 * delta);
			moveMapY = (int)(.71 * delta);
			

		} else if (moveLowerLeft(container)){
			hunterSprite.setDirectionFacing(Direction.LOWER_LEFT);
			hunterSprite.setMoving(true);
			//move map up and to right
			moveMapX = (int)(.71 * delta);
			moveMapY = (int)(-.71 * delta);

		} else if (moveUpperRight(container)){
			hunterSprite.setDirectionFacing(Direction.UPPER_RIGHT);
			hunterSprite.setMoving(true);
			//move map down and to left
			moveMapX = (int)(-.71 * delta);
			moveMapY = (int)(.71 * delta);

		} else if (moveLowerRight(container)){
			hunterSprite.setDirectionFacing(Direction.LOWER_RIGHT);
			hunterSprite.setMoving(true);
			//move map down and to left
			moveMapX = (int)(-.71 * delta);
			moveMapY = (int)(-.71 * delta);

		} else if (moveLeft(container)){
			hunterSprite.setDirectionFacing(Direction.LEFT);
			hunterSprite.setMoving(true);
			//move map to right
			moveMapX = delta;
			moveMapY = 0;

		} else if (moveUp(container)){
			hunterSprite.setDirectionFacing(Direction.BACK);
			hunterSprite.setMoving(true);
			//move map down
			moveMapX = 0;
			moveMapY = delta;			

		} else if (moveRight(container)){
			hunterSprite.setDirectionFacing(Direction.RIGHT);
			hunterSprite.setMoving(true);
			//move map to left
			moveMapX = -1 * delta;
			moveMapY = 0;

		} else if (moveDown(container)){
			hunterSprite.setDirectionFacing(Direction.FRONT);
			hunterSprite.setMoving(true);
			//move map up
			moveMapX = 0;
			moveMapY = -1 * delta;
			
		} else {
			hunterSprite.setMoving(false);
			moveMapX = 0;
			moveMapY = 0;
		
		}
		
		this.huntPanel.setLocation(this.huntPanel.getX() + moveMapX, this.huntPanel.getY()+ moveMapY);
		//here we would update map with new move data values
		
		//update hunter's ammo count
		if(hunter.getInventory().getPopulatedSlots().contains(ItemType.AMMO)){
			this.ammoCount = (int) hunter.getInventory().getConditionOf(ItemType.AMMO).getCurrent();
			this.ammoBoxes = (int) hunter.getInventory().getNumberOf(ItemType.AMMO) -1;
		}
		
		hunterSprite.update(delta);
	}//update player method
	
	/* (non-Javadoc)
	 * @see scene.Scene#update(org.newdawn.slick.GameContainer, org.newdawn.slick.state.StateBasedGame, int)
	 */
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		//amounts to move the map in each direction (either + or - delta if in 4 cardinal directions, or +/- .71 * delta for combinations
		//in opposite direction to facing of toon - if player moves left, map moves to right.
		//positive x moves map from left to right, positive y moves map down
		
		//alternate between the cows and the pigs for updating
		if (this.toggleCount == 0){
			for(PreyCow cow : this.preyCow){
				
			}
			this.toggleCount = 1;
		} else {
			for(PreyPig pig : this.preyPig){
				
			}			
			this.toggleCount = 0;
		}
		this.updatePlayer(container, game, delta);
		mainLayer.update(delta);
		updateHUD();

	}

	/**
	 * decrements ammo when a shot is fired
	 */
	private void decrementAmmo(){
		//THIS DOES THE AMMO DECREASE
		Item ammoBox = null;
		if(hunter.getInventory().getPopulatedSlots().contains(ItemType.AMMO)) {
			ammoBox = hunter.removeItemFromInventory(ItemType.AMMO, 1).get(0);
		}			
		if(ammoBox != null && ammoBox.getStatus().getCurrent() > 1) {
			ammoBox.decreaseStatus(1);
			hunter.addItemToInventory(ammoBox);
		}
		//END AMMO DECREASE

	}
	
	
	
	/**
	 * determines whether a shot is colliding with anything - returns after first hit
	 * @param shotX - the destination x of the shot
	 * @param shotY - the destination y of the shot
	 * @return whether there was a target or not at the shot location
	 */
	private void determineCollsion(int shotX, int shotY){
		boolean aHit = false;
		//to account for the bigger panel behind the window
		int offsetX = huntPanel.getX();
		int offsetY = huntPanel.getY();
		shotX += (-1 * huntPanel.getX());
		shotY += (-1 * huntPanel.getY());
		
		
		int cow = preyCow.size() - 1;
		System.out.println("Shot : " + shotX + " | " + shotY + ", huntPanel x : " + offsetX + " | huntPanel y : " + offsetY);
		while ((!aHit) && (cow >= 0)){
			System.out.println("cow " + cow + " x/y : " + preyCow.get(cow).getxLocation() + " | "+ preyCow.get(cow).getyLocation()  );
			if (preyCow.get(cow).inHitBox(shotX, shotY)) {
				int shotResult = preyCow.get(cow).checkDead();
				aHit = true;
				if (shotResult != 0){ //a kill! - get rid of cow, add meat of old cow to total
					preyCow.get(cow).getPreySprite().setVisible(false);
					preyCow.get(cow).getPreySprite().remove(preyCow.get(cow).getPreySprite());
					preyCow.remove(cow);
					this.meat += shotResult;
				}//if hitpoints are 0
				cow--;
			} else {//if not a hit
				cow--;
			}
		}//while through cows
		
		
		int pig = preyPig.size() - 1;
		while ((!aHit) && (pig >= 0)){
			System.out.println("pig " + pig + " x/y : " + preyPig.get(pig).getxLocation() + " | "+ preyPig.get(pig).getyLocation()  );
			if (preyPig.get(pig).inHitBox(shotX, shotY)) {
				int shotResult = preyPig.get(pig).checkDead();
				aHit = true;
				if (shotResult != 0){ //a kill! - get rid of cow, add meat of old cow to total
					preyPig.get(pig).getPreySprite().setVisible(false);
					preyPig.get(pig).getPreySprite().remove(preyPig.get(pig).getPreySprite());
					preyPig.remove(pig);
					this.meat += shotResult;
				}//if hitpoints are 0
				pig--;
			} else {//if not a hit
				pig--;
			}
		}//while through pigs


	}
	
	@Override
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_SPACE) {
			//GameDirector.sharedSceneListener().sceneDidEnd(this);
		}
	}
	
	@Override
	public void mousePressed(int button, int mx, int my) {
		if (!isVisible() || !isAcceptingInput()) {
			return;
		}
		
		super.mousePressed(button, mx, my);
		//left button is button 0 - fires bullet
		//right button is button 1 - reloads and cocks gun
		System.out.println("mouse clicked");
		if (button == 0) {
			if ((this.ammoCount + this.ammoBoxes != 0) && (this.gunCocked)){
				
				SoundStore.get().playSound("Shot",(float).5);
				
				if (huntSceneRand.nextInt(10) < 4){
					SoundStore.get().playSound("Ricochet");
				}
				
				determineCollsion(mx,my);

				//regardless, decrement ammo
				decrementAmmo();
				this.gunCocked = false;
	
			}//if shot happened via mouse
			else {
				SoundStore.get().playSound("Click",(float).3);			
			}
		}//if button == 0
		else if (button ==1){
			if (this.gunCocked){//cocking while loaded means bullet gone - oopsie
				decrementAmmo();			
			}
			this.gunCocked = true;
			SoundStore.get().playSound("GunCock"); 	
			
		}
	}
	@Override
	public void mouseReleased(int button, int mx, int my) {

	
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		if ( mainLayer.isVisible() && mainLayer.isAcceptingInput()) {		
			reticle.setLocation(newx  - reticle.getWidth()/2, newy - reticle.getHeight()/2);
		}
	}
	
	
	@Override
	public int getID() {
		return ID.ordinal();
	}
	
	private class HUDListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent component) {
		if (component == hud.getCampButton()) {
				SoundStore.get().setMusicVolume(.25f);
				GameDirector.sharedSceneListener().sceneDidEnd(HuntScene.this);
			}//if source==campButton
		if (component == hud.getInventoryButton()){
			GameDirector.sharedSceneListener().requestScene(SceneID.PARTYINVENTORY, HuntScene.this, false);			
		}
			
		}//component activated
	}	
	
	private void updateHUD() {
		if(this.ammoCount != 0) {
			hud.setNotification("Current ammo: " + this.ammoCount + ((this.ammoCount == 1) ? " bullet and " :  " bullets and ") + (hunter.getInventory().getNumberOf(ItemType.AMMO)-1) + " extra " + ((hunter.getInventory().getNumberOf(ItemType.AMMO)-1 == 1) ? "box" : "boxes") + ".");
		} else {
			hud.setNotification("Current ammo: OUT OF AMMO");
		}
	}
	
	
	
	/**
	 * checks if the up and left keys are pressed simultaneously
	 * @param container
	 * @return whether the indicated movement has occurred
	 */
	private boolean moveUpperLeft(GameContainer container){
		if ((container.getInput().isKeyDown(Input.KEY_LEFT) 
			&& (container.getInput().isKeyDown(Input.KEY_UP))) 
			|| (container.getInput().isKeyDown(Input.KEY_Q)) 
			|| (container.getInput().isKeyDown(Input.KEY_W) && (container.getInput().isKeyDown(Input.KEY_A)))){			
			return true;
		} else {
			return false;
		}
	}//move upper left direction
	
	/**
	 * checks if the down and left keys are pressed simultaneously
	 * @param container
	 * @return whether the indicated movement has occurred
	 */
	private boolean moveLowerLeft(GameContainer container){
		if ((container.getInput().isKeyDown(Input.KEY_LEFT)  
			&& (container.getInput().isKeyDown(Input.KEY_DOWN)))
			|| (container.getInput().isKeyDown(Input.KEY_Z)) 
			|| (container.getInput().isKeyDown(Input.KEY_A) && (container.getInput().isKeyDown(Input.KEY_X)))){			
			return true;
		} else {
			return false;
		}
	}//move lower left direction
	
	/**
	 * checks if the up and right keys are pressed simultaneously
	 * @param container
	 * @return whether the indicated movement has occurred
	 */
	private boolean moveUpperRight(GameContainer container){
		if ((container.getInput().isKeyDown(Input.KEY_UP) 
			&& (container.getInput().isKeyDown(Input.KEY_RIGHT)))
			|| (container.getInput().isKeyDown(Input.KEY_E)) 
			|| (container.getInput().isKeyDown(Input.KEY_D) && (container.getInput().isKeyDown(Input.KEY_W)))){			
			return true;
		} else {
			return false;
		}
	}//move upper right direction
	
	/**
	 * checks if the down and right keys are pressed simultaneously
	 * @param container
	 * @return whether the indicated movement has occurred
	 */
	private boolean moveLowerRight(GameContainer container){
		if ((container.getInput().isKeyDown(Input.KEY_RIGHT)
			&& (container.getInput().isKeyDown(Input.KEY_DOWN)))
			|| (container.getInput().isKeyDown(Input.KEY_C)) 
			|| (container.getInput().isKeyDown(Input.KEY_D) && (container.getInput().isKeyDown(Input.KEY_X)))){			

			return true;
		} else {
			return false;
		}
	}//move upper right direction
	
	/**
	 * checks if the right key is pressed alone
	 * @param container
	 * @return whether the indicated movement has occurred
	 */
	private boolean moveRight(GameContainer container){
		if ((!(container.getInput().isKeyDown(Input.KEY_LEFT)) 
				&& !(container.getInput().isKeyDown(Input.KEY_UP)) 
				&& (container.getInput().isKeyDown(Input.KEY_RIGHT)) 
				&& !(container.getInput().isKeyDown(Input.KEY_DOWN)))
				|| ((container.getInput().isKeyDown(Input.KEY_D)) 
				&& !(container.getInput().isKeyDown(Input.KEY_W))
				&& !(container.getInput().isKeyDown(Input.KEY_X))
				&& !(container.getInput().isKeyDown(Input.KEY_A))))	{			
			return true;
		} else {
			return false;
		}
	}//move right direction

	/**
	 * checks if the left key is pressed alone
	 * @param container
	 * @return whether the indicated movement has occurred
	 */
	private boolean moveLeft(GameContainer container){
		if (((container.getInput().isKeyDown(Input.KEY_LEFT)) 
			&& !(container.getInput().isKeyDown(Input.KEY_UP)) 
			&& !(container.getInput().isKeyDown(Input.KEY_RIGHT)) 
			&& !(container.getInput().isKeyDown(Input.KEY_DOWN)))			
			|| (!(container.getInput().isKeyDown(Input.KEY_D)) 
			&& !(container.getInput().isKeyDown(Input.KEY_W))
			&& !(container.getInput().isKeyDown(Input.KEY_X))
			&& (container.getInput().isKeyDown(Input.KEY_A))))	{			
			return true;
		} else {
			return false;
		}
	}//move left direction
	
	/**
	 * checks if the down key is pressed alone
	 * @param container
	 * @return whether the indicated movement has occurred
	 */
	private boolean moveDown(GameContainer container){
		if ((!(container.getInput().isKeyDown(Input.KEY_LEFT)) 
			&& !(container.getInput().isKeyDown(Input.KEY_UP)) 
			&& !(container.getInput().isKeyDown(Input.KEY_RIGHT)) 
			&& (container.getInput().isKeyDown(Input.KEY_DOWN)))
			|| (!(container.getInput().isKeyDown(Input.KEY_D)) 
			&& !(container.getInput().isKeyDown(Input.KEY_W))
			&& (container.getInput().isKeyDown(Input.KEY_X))
			&& !(container.getInput().isKeyDown(Input.KEY_A))))	{			
			return true;
		} else {
			return false;
		}
	}//move down direction
	
	/**
	 * checks if the up key is pressed alone
	 * @param container
	 * @return whether the indicated movement has occurred
	 */
	private boolean moveUp(GameContainer container){
		if ((!(container.getInput().isKeyDown(Input.KEY_LEFT)) 
				&& (container.getInput().isKeyDown(Input.KEY_UP)) 
				&& !(container.getInput().isKeyDown(Input.KEY_RIGHT)) 
				&& !(container.getInput().isKeyDown(Input.KEY_DOWN)))
			|| (!(container.getInput().isKeyDown(Input.KEY_D)) 
			&& (container.getInput().isKeyDown(Input.KEY_W))
			&& !(container.getInput().isKeyDown(Input.KEY_X))
			&& !(container.getInput().isKeyDown(Input.KEY_A)))){			

			return true;
		} else {
			return false;
		}
	}//move up direction

}//huntscene
