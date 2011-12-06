package scene;

import java.util.Map;

import model.HuntingMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
//import component.Positionable;
import component.Positionable.ReferencePoint;
import component.hud.HuntHUD;
import component.sprite.AnimatingSprite;

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
	
	private WorldMap worldMap;

	//the member of the party engaged in the hunt
	//private Person hunter;
	
	//private Image ground;
	//private AnimatingSprite[] huntingParty;
	private AnimatingSprite hunterSprite;
	
	private Person hunter;
	
	/**
	 * Constructs a {@code HuntScene} with a {@code Person} who will be the hunter
	 * @param hunter the single member of the party who is going to hunt.
	 */
	public HuntScene(Person hunter, WorldMap worldMap){
		this.hunter = hunter;
		this.worldMap = worldMap;
		
	}
	
	public HuntScene(Party party){
		//grap a person from the party to be the hunter
		//this.hunter = party.getPartyMembers().get(0);
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
// 		Map<Integer, TerrainObject> tmpLayoutArray = huntLayout.getHuntingGroundsMap();
 		
 		HuntingGroundsComponent huntPanel = new HuntingGroundsComponent(container, 4800, 4800, huntLayout);
  	
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

		} else if (moveLowerLeft(container)){

		} else if (moveUpperRight(container)){

		} else if (moveLowerRight(container)){

		} else if (moveLeft(container)){

		} else if (moveUp(container)){

		} else if (moveRight(container)){

		} else if (moveDown(container)){

		} 
			
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

/**
 * 	//TODO: Call huntTerrainGenerator
 */
//	--moved to huntingmap--
//	public class HuntTerrainGenerator {
//		//Rows and cols are final
//		private final int rows;
//		private final int cols;
////		private final static int treeProc = 100 - stoneProc;
//		
//		Tiles[][] tiles;
//		int[][] types;
//
//		public HuntTerrainGenerator(int rows, int cols, int procChance, int stoneProc) {
//			this.rows = rows;
//			this.cols = cols;
//			int currentType = 1;
//			tiles = new Tiles[rows][cols];
//			types = new int[rows][cols];
//			//Set up the border of empty tiles around our map
//			for(int i = 0; i < rows; i++) {
//				tiles[i][0] = Tiles.EMPTY;
//				types[i][0] = 0;
//				tiles[i][cols-1] = Tiles.EMPTY;
//				types[i][cols-1] = 0;
//			}
//			for(int i = 0; i < cols; i++) {
//				tiles[0][i] = Tiles.EMPTY;
//				types[0][i] = 0;
//				tiles[rows-1][i] = Tiles.EMPTY;
//				types[rows-1][i] = 0;
//			}
//			//If we proc, place a tile and make it the right type.
//			Random random = new Random();
//			for(int i = 0; i < rows; i++) {
//				for(int j = 0; j < cols; j++) {
//					if(tiles[i][j] == null) {
//						if (random.nextInt(100) < procChance) {
//							int randomNumber = random.nextInt(100);
//							currentType = randomNumber < stoneProc ? 1 : 2;
//							placeTile(i, j, random, currentType);
//						}					
//					}
//				}
//			}
//			
//			//Make all the null tiles empty
//			for(int i = 0; i < rows; i++) {
//				for(int j = 0; j < cols; j++) {
//					if(tiles[i][j] == null) {
//					  tiles[i][j] = Tiles.EMPTY;
//					  types[i][j] = 0;
//					}
//				}
//			}
//			
//		}
//		
//		//Places a tile
//		private void placeTile(int row, int col, Random random, int currentType) {
//			if(tiles[row][col] == null) {
//				List<Tiles> possibleList = new ArrayList<Tiles>();
//				//At the beginning, the tile can be anything
//				for(Tiles tile : Tiles.values())
//					possibleList.add(tile);
//				//Then, for each neighbor, go through and remove anything that they cant be next to
//				if(row != 0) {
//					Tiles finder = tiles[row - 1][col];
//					List<Tiles> helper = new ArrayList<Tiles>();
//					if(finder == null) {
//						for(Tiles tile : Tiles.values())
//							helper.add(tile);
//					} else {
//						helper = finder.getPossible(4);
//					}
//					List<Tiles> pretendList = new ArrayList<Tiles>();
//					for(Tiles tile : possibleList)
//						pretendList.add(tile);
//					
//					for (Tiles tile : pretendList) {
//						if (!helper.contains(tile)) {
//							possibleList.remove(tile);
//						}
//					}
//					
//				}
//				if(col != 0) {
//					Tiles finder = tiles[row][col - 1];
//					List<Tiles> helper = new ArrayList<Tiles>();
//					if(finder == null) {
//						for(Tiles tile : Tiles.values())
//							helper.add(tile);
//					} else {
//						helper = finder.getPossible(2);
//					}
//					List<Tiles> pretendList = new ArrayList<Tiles>();
//					for(Tiles tile : possibleList)
//						pretendList.add(tile);
//					
//					for (Tiles tile : pretendList) {
//						if (!helper.contains(tile)) {
//							possibleList.remove(tile);
//						}
//					}
//				}
//				if(col != cols - 1) {
//					Tiles finder = tiles[row][col + 1];
//					List<Tiles> helper = new ArrayList<Tiles>();
//					if(finder == null) {
//						for(Tiles tile : Tiles.values())
//							helper.add(tile);
//					} else {
//						helper = finder.getPossible(1);
//					}
//					
//					List<Tiles> pretendList = new ArrayList<Tiles>();
//					for(Tiles tile : possibleList)
//						pretendList.add(tile);
//					
//					for (Tiles tile : pretendList) {
//						if (!helper.contains(tile)) {
//							possibleList.remove(tile);
//						}
//					}
//				}
//				
//				if(row != rows - 1) {
//					Tiles finder = tiles[row + 1][col];
//					List<Tiles> helper = new ArrayList<Tiles>();
//					if(finder == null) {
//						for(Tiles tile : Tiles.values())
//							helper.add(tile);
//					} else {
//						helper = finder.getPossible(3);
//					}
//					List<Tiles> pretendList = new ArrayList<Tiles>();
//					for(Tiles tile : possibleList)
//						pretendList.add(tile);
//					
//					for (Tiles tile : pretendList) {
//						if (!helper.contains(tile)) {
//							possibleList.remove(tile);
//						}
//					}
//				}
//				
//				//Choose a random of the type possible, and make it the right type
//				if(possibleList.size() != 0) {
//					tiles[row][col] = possibleList.get(random.nextInt(possibleList.size()));
//
//					types[row][col] = currentType;
//				}
//				else {
//					tiles[row][col] = Tiles.EMPTY;
//
//					types[row][col] = 0;
//				}
////				System.out.println(tiles[row][col] + "\n");
////				System.out.println(this.toString());
//				
//				//If we made an empty tile, we dont place anything nearby
//				if(tiles[row][col] == Tiles.EMPTY) {
//					types[row][col] = 0;
//					return;
//				}
//				
//				//Otherwise, place something in all neighbors
//				if(row != 0)
//					placeTile(row - 1, col, random, currentType);
//				if(col != cols - 1)
//					placeTile(row, col + 1, random, currentType);
//				if(col != 0)
//					placeTile(row, col - 1, random, currentType);
//				if(row != rows - 1)
//					placeTile(row + 1, col, random, currentType);
//			}
//		}
//
//		public String toString() {
//			StringBuilder str = new StringBuilder();
//			
//			for(int i = 0; i < rows; i++) {
//				for(int j = 0; j < cols; j++) {
//					if(tiles[i][j] != null) {
//						//Char array for numbers, types array is fine
//						str.append(tiles[i][j].getIndex() + " ");
//						//str.append(drawMap(tiles[i][j].getIndex()) + " ");
//						//str.append(types[i][j] == 1 ? "Stone" : types[i][j] == 2 ? "Trees" : "     ");
//						//str.append(types[i][j]);
//					} else {
//						str.append(-1 + " ");
//					}
//				}
//				str.append("\n");
//			}
//			
//			return str.toString();
//		}
//		
//		public int[][] getTypes() {
//			return types;
//		}
//		
//		public char[][] getTiles() {
//			char[][] tiles = new char[rows][cols];
//			for(int i = 0; i < rows; i++) {
//				for(int j = 0; j < cols; j++) {
//					int index = this.tiles[i][j].getIndex();
//					//Deal with it!
//					tiles[i][j] = index == 10 ? 'a' : index == 11 ? 'b' : index == 12 ? 'c' : index == 13 ? 'd' : index == 14 ? 'e' :
//						index == 15 ? 'f' : (char)index;					
//				}
//			}
//			return tiles;
//		}
//	}
//	private enum Tiles {
//		BOTRIGHT, 
//		BOTFULL, 
//		BOTLEFT, 
//		RIGHTFULL, 
//		FULL, 
//		LEFTFULL, 
//		TOPRIGHT, 
//		TOPFULL, 
//		TOPLEFT, 
//		EMPTY,
//		A, 
//		B, 
//		C, 
//		D,
//		E,
//		F;
//		
//		//0 = empty, 1 = stone, 2 = tree
//		/*
//		 * 1 = left, 2 = right, 3 = up, 4 = down
//		 */
//		public List<Tiles> getPossible(int direction) {
//			Tiles current = this;
//			List<Tiles> returnList = new ArrayList<Tiles>();
//			//Right
//			if (direction == 2) {
//				//BottomHalf
//				if (current.equals(Tiles.BOTRIGHT) || current.equals(Tiles.BOTFULL) || current.equals(Tiles.B) || current.equals(Tiles.F)) {
//					returnList.add(BOTFULL);
//					returnList.add(BOTLEFT);
//					returnList.add(A);
//				//Full
//				} else if (current.equals(Tiles.RIGHTFULL) || current.equals(Tiles.FULL) || current.equals(Tiles.C) || current.equals(Tiles.A)) {
//					returnList.add(FULL);
//					returnList.add(LEFTFULL);
//					returnList.add(B);
//					returnList.add(D);
//				//TopHalf
//				} else if (current.equals(Tiles.TOPRIGHT) || current.equals(Tiles.TOPFULL) || current.equals(Tiles.D) || current.equals(Tiles.E)){
//					returnList.add(TOPFULL);
//					returnList.add(TOPLEFT);
//					returnList.add(C);
//				} else if (current.equals(Tiles.EMPTY)) {
//					returnList.add(RIGHTFULL);
//					returnList.add(TOPRIGHT);
//					returnList.add(BOTRIGHT);
//				} else {
//					returnList.add(EMPTY);
//				}
//				//Left
//			} else if (direction == 1) {
//				if (current.equals(Tiles.BOTLEFT) || current.equals(Tiles.BOTFULL) || current.equals(Tiles.A) || current.equals(Tiles.E)) {
//					returnList.add(BOTRIGHT);
//					returnList.add(BOTFULL);
//					returnList.add(B);
//				} else if (current.equals(Tiles.LEFTFULL) || current.equals(Tiles.FULL) || current.equals(Tiles.B) || current.equals(Tiles.D)) {
//					returnList.add(FULL);
//					returnList.add(A);
//					returnList.add(C);
//					returnList.add(RIGHTFULL);
//				} else if (current.equals(Tiles.TOPLEFT) || current.equals(Tiles.TOPFULL) || current.equals(Tiles.C) || current.equals(Tiles.F)){
//					returnList.add(TOPFULL);
//					returnList.add(TOPRIGHT);
//					returnList.add(D);
//				} else if (current.equals(Tiles.EMPTY)) {
//					returnList.add(TOPLEFT);
//					returnList.add(BOTLEFT);
//					returnList.add(LEFTFULL);
//				} else {
//					returnList.add(EMPTY);
//			}
//				//Up
//			} else if (direction == 3) {
//				if (current.equals(Tiles.TOPRIGHT) || current.equals(Tiles.RIGHTFULL) || current.equals(Tiles.A) || current.equals(Tiles.E)) {
//					returnList.add(RIGHTFULL);
//					returnList.add(BOTRIGHT);
//					returnList.add(C);
//				} else if (current.equals(Tiles.TOPFULL) || current.equals(Tiles.FULL) || current.equals(Tiles.C) || current.equals(Tiles.D)) {
//					returnList.add(FULL);
//					returnList.add(BOTFULL);
//					returnList.add(A);
//					returnList.add(B);
//				} else if (current.equals(Tiles.TOPLEFT) || current.equals(Tiles.LEFTFULL) || current.equals(Tiles.B) || current.equals(Tiles.F)){
//					returnList.add(BOTLEFT);
//					returnList.add(LEFTFULL);
//					returnList.add(D);
//				} else if (current.equals(Tiles.EMPTY)) {
//					returnList.add(TOPFULL);
//					returnList.add(TOPRIGHT);
//					returnList.add(TOPLEFT);
//				} else {
//					returnList.add(EMPTY);
//					
//				}
//				//Down
//			} else if (direction == 4) {
//				if (current.equals(Tiles.BOTRIGHT) || current.equals(Tiles.RIGHTFULL) || current.equals(Tiles.C) || current.equals(Tiles.F)) {
//					returnList.add(RIGHTFULL);
//					returnList.add(TOPRIGHT);
//					returnList.add(A);
//				} else if (current.equals(Tiles.BOTFULL) || current.equals(Tiles.FULL) || current.equals(Tiles.A) || current.equals(Tiles.B)) {
//					returnList.add(FULL);
//					returnList.add(TOPFULL);
//					returnList.add(C);
//					returnList.add(D);
//				} else if (current.equals(Tiles.BOTLEFT) || current.equals(Tiles.LEFTFULL) || current.equals(Tiles.D) || current.equals(Tiles.E)){
//					returnList.add(TOPLEFT);
//					returnList.add(LEFTFULL);
//					returnList.add(B);
//				} else if (current.equals(Tiles.EMPTY)) {
//					returnList.add(BOTLEFT);
//					returnList.add(BOTRIGHT);
//					returnList.add(BOTFULL);
//				} else {
//					returnList.add(EMPTY);
//				}
//			}
//			
//			return returnList;
//		}
//		
//		public int getIndex() {
//			return this.ordinal();
//		}
//	}
//	
//	private String drawMap(int index) {
//		String str = "";
//		switch (index) {
//		case 0: 
//			str = " _";
//			break;
//		case 1:
//			str = "__";
//			break;
//		case 2:
//			str = "_ ";
//			break;
//		case 3:
//			str = " |";
//			break;
//		case 4:
//			str = "  ";
//			break;
//		case 5:
//			str = "| ";
//			break;
//		case 6:
//			str = " -";
//			break;
//		case 7:
//			str = "--";
//			break;
//		case 8:
//			str = "- ";
//			break;
//		case 9:
//			str = "  ";
//			break;
//		case 10:
//			str = "_|";
//			break;
//		case 11:
//			str = "|_";
//			break;
//		case 12:
//			str = "-|";
//			break;
//		case 13:
//			str = "|-";
//			break;
//		}
//		
//		
//		return str;
//	}

}//huntscene
