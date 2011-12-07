package scene;

//import java.util.Map;

import java.util.Random;

import model.HuntingMap;

//import model.huntingMap.TerrainObject;
import model.Item;
import model.Party;
import model.Person;
import model.WorldMap;
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

//import component.Label;
import component.Component;
import component.HuntingGroundsComponent;
import component.Panel;
import component.TerrainComponent;
//import component.Positionable;
import component.Positionable.ReferencePoint;
import component.hud.HuntHUD;
import component.sprite.AnimatingSprite;
import component.sprite.AnimatingSprite.Direction;
import component.sprite.HunterAnimatingSprite;
import component.sprite.PreyAnimatingSprite;

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

	private HuntHUD hud;
	/**the image to be tiled as the background of the hunt component*/
	private Image background;
	/**the world map, to be used to determine actual in-world location*/
	private WorldMap worldMap;

	/**the graphic representing the hunter*/
	private AnimatingSprite hunterSprite;
	/**the graphic representing the cow(s), if any exist on the screen*/
	private AnimatingSprite cowSprite;
	/**the graphic representing the hunter*/
	private AnimatingSprite pigSprite;
	/**the member of the party that is hunting - currently party leader*/
	private Person hunter;
	/**single random gen for entire scene*/
	private Random huntSceneRand;
	
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
		huntSceneRand = new Random();
		super.init(container, game);
		SoundStore.get().stopAllSound();
		SoundStore.get().playHuntMusic();
		hud = new HuntHUD(container, new HUDListener());
		updateHUD();
		//hud.setNotification(location.getName());
		super.showHUD(hud);			
		//Font h2 = FontStore.get().getFont(FontStore.FontID.H2);
		double [] dblArgs = new double[4];
		int [] intArgs = new int[2];
		
		dblArgs[0] = 2400;	//map x dimension
		dblArgs[1] = 2400;	//map y dimension
		dblArgs[2] = 0;			//worldMap.getLocationNode.getX - these may be implemented someday.
		dblArgs[3] = 0;			//worldMap.getLocationNode.gety
		
		intArgs[0] = 50;		// chance that there's a terrain object
		intArgs[1] = 50;		//chance that it's a tree vs a rock
		
 		HuntingMap huntLayout = new HuntingMap(container, dblArgs, intArgs, ConstantStore.Environments.PLAINS);
		
		switch (huntLayout.getBckGround()){
			case GRASS : 	this.background = ImageStore.get().getImage("HUNT_GRASS");
							break;
			case SNOW : 	this.background = ImageStore.get().getImage("HUNT_SNOW");
							break;
			case MOUNTAIN : this.background = ImageStore.get().getImage("HUNT_MOUNTAIN");
							break;
			case DESERT : 	this.background = ImageStore.get().getImage("HUNT_DESERT");
							break;
			default :		this.background = ImageStore.get().getImage("HUNT_GRASS");
							break;
		}

		
 		TerrainObject[][] tmpLayoutArray = huntLayout.getHuntingGroundsMap();
 		
 		Panel backgroundPanel = new Panel(container, this.background.getWidth() * 2,this.background.getHeight() * 2, this.background);
 		
 		Panel[] bkgroundAra = new Panel[361];
 		for (int i = 0; i < 361; i++){
 			bkgroundAra[i] = backgroundPanel;
 		}
 		
		Panel huntPanel = new Panel(container, (int)huntLayout.getMAP_WIDTH(), (int)huntLayout.getMAP_HEIGHT());
		
		huntPanel.addAsGrid((Component[])bkgroundAra,huntPanel.getPosition(ReferencePoint.CENTERCENTER), 19,19, 256, 256, 0,0);
		
 		
 //		huntPanel.addAsGrid(huntLayout.getHuntingGroundsComponents(), huntPanel.getPosition(ReferencePoint.TOPLEFT), 0, 0);
// 		for (int row = 0; row < tmpLayoutArray.length; row++){
// 			for (int col = 0; col < tmpLayoutArray[row].length; col++){
// 				huntPanel.add(new TerrainComponent(container, tmpLayoutArray[row][col]), huntPanel.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT, row * 48, col * 48);
// 					
// 			}//for col 0 to row array length		
//  		}//for row 0 to array length
 		
 		mainLayer.add(huntPanel,huntPanel.getPosition(ReferencePoint.CENTERCENTER), ReferencePoint.CENTERCENTER);
 		Image[] pigAnimLeft = new Image[6];
 		Image[] pigAnimRight = new Image[6];
 		Image[] pigAnimFront = new Image[6];
 		Image[] pigAnimBack = new Image[6];
 		
 		Image[] cowAnimLeft = new Image[9];
 		Image[] cowAnimRight = new Image[9];
 		Image[] cowAnimFront = new Image[9];
 		Image[] cowAnimBack = new Image[9];
 		
 		
 		//set up the animation arrays for the pig and the cow
 		for (int incr = 1; incr < 7; incr++){
 			
 	 		pigAnimLeft[incr - 1] = ImageStore.get().getImage("HUNT_PIGLEFT" + incr);
 	 		pigAnimRight[incr - 1] = ImageStore.get().getImage("HUNT_PIGRIGHT" + incr);
 	 		pigAnimFront[incr - 1] = ImageStore.get().getImage("HUNT_PIGFRONT" + incr);
 	 		pigAnimBack[incr - 1] = ImageStore.get().getImage("HUNT_PIGBACK" + incr);
 	 		
 	 		cowAnimLeft[incr - 1] = ImageStore.get().getImage("HUNT_COWLEFT" + incr);
 	 		cowAnimRight[incr - 1] = ImageStore.get().getImage("HUNT_COWRIGHT" + incr);
 	 		cowAnimFront[incr - 1] = ImageStore.get().getImage("HUNT_COWFRONT" + incr);
 	 		cowAnimBack[incr - 1] = ImageStore.get().getImage("HUNT_COWBACK" + incr);
 		}
 		
 		for (int incr = 7; incr < 10; incr ++) {
 	 		cowAnimLeft[incr - 1] = ImageStore.get().getImage("HUNT_COWLEFT" + incr);
 	 		cowAnimRight[incr - 1] = ImageStore.get().getImage("HUNT_COWRIGHT" + incr);
 	 		cowAnimFront[incr - 1] = ImageStore.get().getImage("HUNT_COWFRONT" + incr);
 	 		cowAnimBack[incr - 1] = ImageStore.get().getImage("HUNT_COWBACK" + incr);		
 		}
	
 		
		pigSprite =  new PreyAnimatingSprite(container,
//				48,
				new Animation(pigAnimLeft, 250),
				new Animation(pigAnimRight, 250),
				new Animation(pigAnimFront, 250),
				new Animation(pigAnimBack, 250),
				AnimatingSprite.Direction.RIGHT);
		cowSprite =  new PreyAnimatingSprite(container,
//				48,
				new Animation(cowAnimLeft, 250),
				new Animation(cowAnimRight, 250),
				new Animation(cowAnimFront, 250),
				new Animation(cowAnimBack, 250),
				AnimatingSprite.Direction.RIGHT);

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
		
		mainLayer.add(hunterSprite,
				new Vector2f(mainLayer.getWidth()/2,mainLayer.getHeight()/2),
				ReferencePoint.CENTERCENTER,
				20,
				25);
		
		mainLayer.add(pigSprite,
				new Vector2f(mainLayer.getWidth()/2,mainLayer.getHeight()/2),
				ReferencePoint.CENTERCENTER,
				200,
				250);
		mainLayer.add(cowSprite,
				new Vector2f(mainLayer.getWidth()/2,mainLayer.getHeight()/2),
				ReferencePoint.CENTERCENTER,
				-100,
				-150);
		
		//build background
		
		backgroundLayer.add(new Panel(container, new Image(ConstantStore. PATH_BKGRND + "dark_dirt.png")));
	}
	
	/**
	 * update the player's toon to reflect input
	 * @param container
	 * @param game
	 * @param delta
	 * @throws SlickException
	 */
	

	private void updatePlayer(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		int movMapX;
		int movMapY;
	
		if (moveUpperLeft(container)){
			hunterSprite.setDirectionFacing(Direction.UPPER_LEFT);
			hunterSprite.setMoving(true);
			//move map down and to right
			movMapX = (int)(.71 * delta);
			movMapY = (int)(.71 * delta);
			

		} else if (moveLowerLeft(container)){
			hunterSprite.setDirectionFacing(Direction.LOWER_LEFT);
			hunterSprite.setMoving(true);
			//move map up and to right
			movMapX = (int)(.71 * delta);
			movMapY = (int)(-.71 * delta);

		} else if (moveUpperRight(container)){
			hunterSprite.setDirectionFacing(Direction.UPPER_RIGHT);
			hunterSprite.setMoving(true);
			//move map down and to left
			movMapX = (int)(-.71 * delta);
			movMapY = (int)(.71 * delta);

		} else if (moveLowerRight(container)){
			hunterSprite.setDirectionFacing(Direction.LOWER_RIGHT);
			hunterSprite.setMoving(true);
			//move map down and to left
			movMapX = (int)(-.71 * delta);
			movMapY = (int)(-.71 * delta);

		} else if (moveLeft(container)){
			hunterSprite.setDirectionFacing(Direction.LEFT);
			hunterSprite.setMoving(true);
			//move map to right
			movMapX = delta;
			movMapY = 0;

		} else if (moveUp(container)){
			hunterSprite.setDirectionFacing(Direction.BACK);
			hunterSprite.setMoving(true);
			//move map down
			movMapX = 0;
			movMapY = delta;			

		} else if (moveRight(container)){
			hunterSprite.setDirectionFacing(Direction.RIGHT);
			hunterSprite.setMoving(true);
			//move map to left
			movMapX = -1 * delta;
			movMapY = 0;

		} else if (moveDown(container)){
			hunterSprite.setDirectionFacing(Direction.FRONT);
			hunterSprite.setMoving(true);
			//move map up
			movMapX = 0;
			movMapY = -1 * delta;
			
		} else {
			hunterSprite.setMoving(false);
			movMapX = 0;
			movMapY = 0;
		
		}
		
		//here we would update map with new move data values
		
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
	
	
	/**
	 * determines whether a shot is colliding with anything
	 * @param shotX - the destination x of the shot
	 * @param shotY - the destination y of the shot
	 * @return whether there was a target or not at the shot location
	 */
	private boolean determineCollsion(int shotX, int shotY){
		boolean check = false;
		//code to compare shotX,shotY to any animals or enemies on the visible area of the screen
		return check;
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
		SoundStore.get().playSound("Shot",(float).5);
		
		if (huntSceneRand.nextInt(10) < 4){
			SoundStore.get().playSound("Ricochet");
		}
		
		if (determineCollsion(mx,my)){//determine if there's been a hit
			
			
		}
		//regardless, decrement ammo
		decrementAmmo();
		
		//this.mouseOver = getArea().contains(mx, my);
	}
	@Override
	public void mouseReleased(int button, int mx, int my) {
		SoundStore.get().playSound("GunCock");
	
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		if ( mainLayer.isVisible() && mainLayer.isAcceptingInput()) {
			

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
		int ammoCount = 0;
		if(hunter.getInventory().getPopulatedSlots().contains(ItemType.AMMO))
			ammoCount = (int) hunter.getInventory().getConditionOf(ItemType.AMMO).getCurrent();
		if(ammoCount != 0) {
			hud.setNotification("Current ammo: " + ammoCount + " bullets and " + (hunter.getInventory().getNumberOf(ItemType.AMMO)-1) + " extra " + ((hunter.getInventory().getNumberOf(ItemType.AMMO)-1 == 1) ? "box" : "boxes") + ".");
		} else {
			hud.setNotification("Current ammo: OUT OF AMMO");
		}
	}

}//huntscene
