package scene;

//import java.util.Map;

import model.HuntingMap;

//import model.huntingMap.TerrainObject;
import model.Party;
import model.Person;
import model.WorldMap;
import model.huntingMap.TerrainObject;

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
import component.HuntingGroundsComponent;
import component.Panel;
import component.TerrainComponent;
//import component.Positionable;
import component.Positionable.ReferencePoint;
import component.hud.HuntHUD;
import component.sprite.AnimatingSprite;
import component.sprite.AnimatingSprite.Direction;

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
	/**the member of the party that is hunting - currently party leader*/
	private Person hunter;
	
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
		super.init(container, game);
		SoundStore.get().stopAllSound();
		SoundStore.get().playHuntMusic();
		hud = new HuntHUD(container, new HUDListener());
		//hud.setNotification(location.getName());
		super.showHUD(hud);			
		//Font h2 = FontStore.get().getFont(FontStore.FontID.H2);
		double [] dblArgs = new double[4];
		int [] intArgs = new int[2];
		
		dblArgs[0] = 4800;
		dblArgs[1] = 4800;
		dblArgs[2] = 0;			//worldMap.getLocationNode.getX
		dblArgs[3] = 0;		
		
		intArgs[0] = 75;		//not currently used - chance that there's a terrain object
		intArgs[1] = 75;		//chance that it's a tree vs a rock
		
 		HuntingMap huntLayout = new HuntingMap(dblArgs, intArgs, ConstantStore.Environments.PLAINS);
		
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
 		
// 		Panel huntPanel = new Panel(container, (int)huntLayout.getMAP_WIDTH(), (int)huntLayout.getMAP_HEIGHT(), this.background);
 		
// 		for (int row = 0; row < tmpLayoutArray.length; row++){
// 			for (int col = 0; col < tmpLayoutArray[row].length; col++){
// 				huntPanel.add(new TerrainComponent(container, tmpLayoutArray[row][col]), huntPanel.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT, row * 48, col * 48);
// 					
// 			}//for col 0 to row array length		
//  		}//for row 0 to array length
 		
// 		mainLayer.add(huntPanel,huntPanel.getPosition(ReferencePoint.CENTERCENTER), ReferencePoint.CENTERCENTER);
  	
		hunterSprite = new AnimatingSprite(container,
				48,
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
		
		//build background
		
		backgroundLayer.add(new Panel(container, new Image(ConstantStore. PATH_BKGRND + "dark_dirt.png")));
	}
	
	
	
	/* (non-Javadoc)
	 * @see scene.Scene#update(org.newdawn.slick.GameContainer, org.newdawn.slick.state.StateBasedGame, int)
	 */
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

	
		if (moveUpperLeft(container)){
			System.out.println("upperleft");
			hunterSprite.setDirectionFacing(Direction.UPPER_LEFT);
			hunterSprite.setMoving(true);

		} else if (moveLowerLeft(container)){
			System.out.println("lowerleft");
			hunterSprite.setDirectionFacing(Direction.LOWER_LEFT);
			hunterSprite.setMoving(true);

		} else if (moveUpperRight(container)){
			System.out.println("upperright");
			hunterSprite.setDirectionFacing(Direction.UPPER_RIGHT);
			hunterSprite.setMoving(true);

		} else if (moveLowerRight(container)){
			System.out.println("lowerright");
			hunterSprite.setDirectionFacing(Direction.LOWER_RIGHT);
			hunterSprite.setMoving(true);

		} else if (moveLeft(container)){
			System.out.println("left");
			hunterSprite.setDirectionFacing(Direction.LEFT);
			hunterSprite.setMoving(true);

		} else if (moveUp(container)){
			System.out.println("up");
			hunterSprite.setDirectionFacing(Direction.BACK);
			hunterSprite.setMoving(true);

		} else if (moveRight(container)){
			System.out.println("right");
			hunterSprite.setDirectionFacing(Direction.RIGHT);
			hunterSprite.setMoving(true);

		} else if (moveDown(container)){
			System.out.println("down");
			hunterSprite.setDirectionFacing(Direction.FRONT);
			hunterSprite.setMoving(true);

		} else {
			System.out.println("not moving");
			hunterSprite.setMoving(false);
		
		}
		hunterSprite.update(delta);
		mainLayer.update(delta);


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
			&& (container.getInput().isKeyDown(Input.KEY_A)))){			

			return true;
		} else {
			return false;
		}
	}//move up direction
	
	
	@Override
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_ESCAPE) {
			GameDirector.sharedSceneListener().sceneDidEnd(this);
		}
		if (key == Input.KEY_D){//pressed the right key
			
		}
		if (key == Input.KEY_W){//pressed the forward key
			
		}
		if (key == Input.KEY_A){//pressed the left key
			
		}
		if (key == Input.KEY_S){//pressed the back key
			
		}
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

}//huntscene
