/**
 * 
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import model.huntingMap.TerrainObject;

import core.ConstantStore;


/**
 * this manufactures the terrain for the hunt scene
 */
@SuppressWarnings("serial")
public class HuntingMap implements Serializable {
	/**width of the tile to be used for terrain*/
	public final double TILE_WIDTH = 48;
	/**HEIGHT of the tile to be used for terrain*/
	public final double TILE_HEIGHT = 48;
	/**density of terrain in this map - high value means very dense 1-100*/
	public final int MAP_DENSITY;
	/**how much rocks over forests dominate the landscape on this map 1 - 100*/
	public final int MAP_STONY;
	/**width of the hunting map	 */
	public final double MAP_WIDTH;
	/**height of the hunting map	 */
	public final double MAP_HEIGHT;
	/**x location of this section of the hunting map on the main map	 */
	public final double MAP_X_LOC;
	/**y location of this section of the hunting map on the main map */
	public final double MAP_Y_LOC;
	/** the number of images going across this map = map width/image width */
	public int mapXMax;
	/** the number of images going down this map = map height/image height */
	public int mapYMax;
	/**the background graphic type for this map*/
	private ConstantStore.bckGroundType bckGround;
	/** 2d array that holds each hunting grounds graphics object for the actual map*/
	private TerrainObject[][] huntingGroundsMap;
	/**random generator used for entire hunt map*/
	private Random huntMapRand;

	
	/**
	 * builds the layout for the hunt scene
	 * @param dblMapStats an array of doubles holding the various numeric descriptive quantities of the map
	 * 		idx 0 = map width
	 * 		idx 1 = map height
	 * 		idx 2 = map main map x location
	 * 		idx 3 = map main map y location
	 * @param intMapStats integer array of stats of the map, related to how likely and what kind of terrain is to be painted
	 * 		idx 0 = how dense with terrain this section of the map is
	 * 		idx 1 = how much trees over rocks dominate this terrain
	 * @param mapDensity how dense with terrain this map is
	 * @param environs the environment of this map -determines the background gif
	 */
	public HuntingMap(double[] dblMapStats, int[] intMapStats, ConstantStore.Environments environs) {
		this.MAP_WIDTH = dblMapStats[0];//in pixels
		this.MAP_HEIGHT = dblMapStats[1];//in pixels
		this.MAP_X_LOC = dblMapStats[2];
		this.MAP_Y_LOC = dblMapStats[3];
		
		this.MAP_DENSITY = intMapStats[0];
		this.MAP_STONY = intMapStats[1];
		
		this.mapXMax = (int)(this.MAP_WIDTH/this.TILE_WIDTH);
		this.mapYMax = (int)(this.MAP_HEIGHT/this.TILE_HEIGHT);
		
		switch (environs) {

		case FOREST :
		case PLAINS	: 		this.bckGround = ConstantStore.bckGroundType.GRASS;
							break;
		case HILLS :		
		case MOUNTAINS :	this.bckGround = ConstantStore.bckGroundType.MOUNTAIN;
							break;
			
		case DESERT :		this.bckGround = ConstantStore.bckGroundType.DESERT;
							break;
		
		case SNOWY_FOREST :
		case SNOWY_HILLS :
		case SNOWY_MOUNTAINS :
		case SNOWY_PLAINS :	this.bckGround = ConstantStore.bckGroundType.SNOW;
							break;
		
		}//end switch
		
		this.huntMapRand = new Random();
		huntingGroundsMap = new TerrainObject[this.mapXMax][this.mapYMax];
		this.generateMap();			
	}//constructor
	
	/**
	 * generates the map for this hunting instance by building terrain layouts randomly.
	 */
	private void generateMap(){
		
		//speed modification for moving through this terrain
		double objMoveMod;
		//chance shot is blocked each turn in this terrain
		double objStopShot;

		HuntTerrainGenerator mapGen = new HuntTerrainGenerator(this.mapYMax, this.mapXMax, this.MAP_DENSITY, this.MAP_STONY, huntMapRand);
		
		//leave space in call for arguments to pass to generate map
		int[] altArgs = new int[1];
		altArgs[0]=0;
		//loop through possible positions of tiles on map as per mapgen construct
		for (int row = 0; row < mapGen.getTiles().length; row++){
			for (int col = 0; col < mapGen.getTiles()[row].length; col++){ 
				//if type is 1 then rock, 2 then tree
				String namePrefix = (mapGen.getTypes()[row][col] == 1) ? ("rock1") : ("tree1");
				String nameExt = getExt(mapGen.getTiles()[row][col], namePrefix);
				if (mapGen.getTiles()[row][col] == '9'){//9 is always empty tile, regardless of stone or tree
					objMoveMod = 1;
					objStopShot = 0;
				}//nameExt not --
				else {//not empty - determine whether should be impassable or just difficult (stone or tree)
					if (namePrefix.equals("rock1")){//impassable
						objMoveMod = 0;
						objStopShot = 1;
					} else {//tree
						objMoveMod = .6;
						objStopShot = .5;
					}//if either rock or stone
				}//if based on name extension
				huntingGroundsMap[row][col] = new TerrainObject(nameExt, objStopShot, objStopShot, bckGround, objStopShot, objStopShot);								
			}//for y
		}//for x
	}//generate map method

/**
 * builds the tile name from the generated data
 * @param prefix what tile, from 0 to f hex in chars
 * @param type what type of tile (atm either stone1 or tree1)

 * @return
 */
	private String getExt(char prefix, String type){
		
		String retVal = type + prefix + ((huntMapRand.nextBoolean()) ? "1" : "0") ;
		return retVal;
	}
	
	
	/**
	 * @return the huntingGroundsMap
	 */
	public TerrainObject[][] getHuntingGroundsMap() {
		return huntingGroundsMap;
	}

	
	/**
	 * george's wonder class 
	 * that generates the terrain for the hunt map
	 */
	private class HuntTerrainGenerator {
		//Rows and cols are final
		private final int totalRows;
		private final int totalCols;
//		private final static int treeProc = 100 - stoneProc;
		
		private Tiles[][] tiles;
		private int[][] types;
		
		/**
		 * builds the huntTerrainGenerator
		 * @param totalRows number of tiles in the y direction on the map
		 * @param totalCols number of tiles in the x direction on the map
		 * @param procChance chance that a particular tile will have terrain
		 * @param stoneProc chance that a particular terrain block will be stone(impassable), as opposed to trees(passable but difficult)
		 */
		public HuntTerrainGenerator(int totalRows, int totalCols, int procChance, int stoneProc, Random huntMapRand) {
			this.totalRows = totalRows;
			this.totalCols = totalCols;
			int currentType = 1;
			tiles = new Tiles[this.totalRows][this.totalCols];
			types = new int[this.totalRows][this.totalCols];
			//Set up the border of empty tiles around our map
			for(int row = 0; row < this.totalRows; row++) {
				tiles[row][0] = Tiles.EMPTY;
				types[row][0] = 0;
				tiles[row][totalCols-1] = Tiles.EMPTY;
				types[row][totalCols-1] = 0;
			}
			for(int col = 0; col < this.totalCols; col++) {
				tiles[0][col] = Tiles.EMPTY;
				types[0][col] = 0;
				tiles[totalRows-1][col] = Tiles.EMPTY;
				types[totalRows-1][col] = 0;
			}
			//proc means we place an object
			//If we proc, place a tile and make it the right type.
			for(int row = 0; row < this.totalRows; row++) {
				for(int col = 0; col < this.totalCols; col++) {
					if(tiles[row][col] == null) {//check if this null tile gets some terrain
						if (huntMapRand.nextInt(100) < procChance) {//if so figure out what kind and place it
							currentType =  (huntMapRand.nextInt(100) < stoneProc) ? 1 : 2;
							placeTile(row, col, huntMapRand, currentType);
						} else {//if no terrain, put default empty in place
							  tiles[row][col] = Tiles.EMPTY;
							  types[row][col] = 1;		//needs to be either 1 or 2 - originally had 0 in types, 
							  							//but i am building tileset with the empties with both tree1 and rock1 sets having an empty tile  						
						}
					}
				}
			}//for i rows
			
			//Make all the null tiles empty
/*			for(int i = 0; i < totalRows; i++) {
				for(int j = 0; j < totalCols; j++) {
					if(tiles[i][j] == null) {
					  tiles[i][j] = Tiles.EMPTY;
					  types[i][j] = 0;
					}//if tiles are null then tiles = empty const and the type at this location is 0 (empty)
				}//for j = cols
			}//for i = rows
*/
			
		}//huntTerrainGenerator constructor
		
		//Places a tile
		private void placeTile(int row, int col, Random random, int currentType) {
			if(tiles[row][col] == null) {
				List<Tiles> possibleList = new ArrayList<Tiles>();
				//At the beginning, the tile can be anything
				for(Tiles tile : Tiles.values())
					possibleList.add(tile);
				//Then, for each neighbor, go through and remove anything that they cant be next to
				if(row != 0) {
					Tiles finder = tiles[row - 1][col];
					List<Tiles> helper = new ArrayList<Tiles>();
					if(finder == null) {
						for(Tiles tile : Tiles.values())
							helper.add(tile);
					} else {
						helper = finder.getPossible(4);
					}
					List<Tiles> pretendList = new ArrayList<Tiles>();
					for(Tiles tile : possibleList)
						pretendList.add(tile);
					
					for (Tiles tile : pretendList) {
						if (!helper.contains(tile)) {
							possibleList.remove(tile);
						}
					}
					
				}
				if(col != 0) {
					Tiles finder = tiles[row][col - 1];
					List<Tiles> helper = new ArrayList<Tiles>();
					if(finder == null) {
						for(Tiles tile : Tiles.values())
							helper.add(tile);
					} else {
						helper = finder.getPossible(2);
					}
					List<Tiles> pretendList = new ArrayList<Tiles>();
					for(Tiles tile : possibleList)
						pretendList.add(tile);
					
					for (Tiles tile : pretendList) {
						if (!helper.contains(tile)) {
							possibleList.remove(tile);
						}
					}
				}
				if(col != this.totalCols - 1) {
					Tiles finder = tiles[row][col + 1];
					List<Tiles> helper = new ArrayList<Tiles>();
					if(finder == null) {
						for(Tiles tile : Tiles.values())
							helper.add(tile);
					} else {
						helper = finder.getPossible(1);
					}
					
					List<Tiles> pretendList = new ArrayList<Tiles>();
					for(Tiles tile : possibleList)
						pretendList.add(tile);
					
					for (Tiles tile : pretendList) {
						if (!helper.contains(tile)) {
							possibleList.remove(tile);
						}
					}
				}
				
				if(row != this.totalRows - 1) {
					Tiles finder = tiles[row + 1][col];
					List<Tiles> helper = new ArrayList<Tiles>();
					if(finder == null) {
						for(Tiles tile : Tiles.values())
							helper.add(tile);
					} else {
						helper = finder.getPossible(3);
					}
					List<Tiles> pretendList = new ArrayList<Tiles>();
					for(Tiles tile : possibleList)
						pretendList.add(tile);
					
					for (Tiles tile : pretendList) {
						if (!helper.contains(tile)) {
							possibleList.remove(tile);
						}
					}
				}
				
				//Choose a random of the type possible, and make it the right type
				if(possibleList.size() != 0) {
					tiles[row][col] = possibleList.get(random.nextInt(possibleList.size()));

					types[row][col] = currentType;
				}
				else {
					tiles[row][col] = Tiles.EMPTY;

					types[row][col] = 0;
				}
//				System.out.println(tiles[row][col] + "\n");
//				System.out.println(this.toString());
				
				//If we made an empty tile, we dont place anything nearby
				if(tiles[row][col] == Tiles.EMPTY) {
					types[row][col] = 0;
					return;
				}
				
				//Otherwise, place something in all neighbors
				if(row != 0)
					placeTile(row - 1, col, random, currentType);
				if(col != this.totalCols - 1)
					placeTile(row, col + 1, random, currentType);
				if(col != 0)
					placeTile(row, col - 1, random, currentType);
				if(row != this.totalRows - 1)
					placeTile(row + 1, col, random, currentType);
			}
		}

		public String toString() {
			StringBuilder str = new StringBuilder();
			
			for(int row = 0; row < this.totalRows; row++) {
				for(int col = 0; col < this.totalCols; col++) {
					if(tiles[row][col] != null) {
						//Char array for numbers, types array is fine
						str.append(tiles[row][col].getIndex() + " ");
						//str.append(drawMap(tiles[row][col].getIndex()) + " ");
						//str.append(types[row][col] == 1 ? "Stone" : types[row][col] == 2 ? "Trees" : "     ");
						//str.append(types[row][col]);
					} else {
						str.append(-1 + " ");
					}//if tiles ara at row,col is null, else
				}//for col 0 to totalcols
				str.append("\n");
			}//for row = 0 to totalrows
			
			return str.toString();
		}
		
		public int[][] getTypes() {
			return types;
		}
		
		/**
		 * takes 2d array of tile ints and converts to required chars for map implementation
		 * @return 2d array of generated tiles
		 */
		public char[][] getTiles() {
			char[][] charTiles = new char[this.totalRows][this.totalCols];
			for(int row = 0; row < this.totalRows; row++) {
				for(int col = 0; col < this.totalCols; col++) {
					int index = this.tiles[row][col].getIndex();
					//converts index in array to be a character to be put in the2d char array of tiles
					charTiles[row][col] = (index == 10) ? 'a' : 
									(index == 11) ? 'b' : 
									(index == 12) ? 'c' : 
									(index == 13) ? 'd' : 
									(index == 14) ? 'e' : 
									(index == 15) ? 'f' : (char)index;					
				}
			}
			return charTiles;
		}
	}
	
	/**
	 * structure representing the tile being added to the map
	 */
	private enum Tiles {
		BOTRIGHT, 
		BOTFULL, 
		BOTLEFT, 
		RIGHTFULL, 
		FULL, 
		LEFTFULL, 
		TOPRIGHT, 
		TOPFULL, 
		TOPLEFT, 
		EMPTY,
		A, 
		B, 
		C, 
		D,
		E,
		F;
		
		//0 = empty, 1 = stone, 2 = tree
		/*
		 * 1 = left, 2 = right, 3 = up, 4 = down
		 */
		public List<Tiles> getPossible(int direction) {
			Tiles current = this;
			List<Tiles> returnList = new ArrayList<Tiles>();
			//Right
			if (direction == 2) {
				//BottomHalf
				if (current.equals(Tiles.BOTRIGHT) || current.equals(Tiles.BOTFULL) || current.equals(Tiles.B) || current.equals(Tiles.F)) {
					returnList.add(BOTFULL);
					returnList.add(BOTLEFT);
					returnList.add(A);
				//Full
				} else if (current.equals(Tiles.RIGHTFULL) || current.equals(Tiles.FULL) || current.equals(Tiles.C) || current.equals(Tiles.A)) {
					returnList.add(FULL);
					returnList.add(LEFTFULL);
					returnList.add(B);
					returnList.add(D);
				//TopHalf
				} else if (current.equals(Tiles.TOPRIGHT) || current.equals(Tiles.TOPFULL) || current.equals(Tiles.D) || current.equals(Tiles.E)){
					returnList.add(TOPFULL);
					returnList.add(TOPLEFT);
					returnList.add(C);
				} else if (current.equals(Tiles.EMPTY)) {
					returnList.add(RIGHTFULL);
					returnList.add(TOPRIGHT);
					returnList.add(BOTRIGHT);
				} else {
					returnList.add(EMPTY);
				}
				//Left
			} else if (direction == 1) {
				if (current.equals(Tiles.BOTLEFT) || current.equals(Tiles.BOTFULL) || current.equals(Tiles.A) || current.equals(Tiles.E)) {
					returnList.add(BOTRIGHT);
					returnList.add(BOTFULL);
					returnList.add(B);
				} else if (current.equals(Tiles.LEFTFULL) || current.equals(Tiles.FULL) || current.equals(Tiles.B) || current.equals(Tiles.D)) {
					returnList.add(FULL);
					returnList.add(A);
					returnList.add(C);
					returnList.add(RIGHTFULL);
				} else if (current.equals(Tiles.TOPLEFT) || current.equals(Tiles.TOPFULL) || current.equals(Tiles.C) || current.equals(Tiles.F)){
					returnList.add(TOPFULL);
					returnList.add(TOPRIGHT);
					returnList.add(D);
				} else if (current.equals(Tiles.EMPTY)) {
					returnList.add(TOPLEFT);
					returnList.add(BOTLEFT);
					returnList.add(LEFTFULL);
				} else {
					returnList.add(EMPTY);
			}
				//Up
			} else if (direction == 3) {
				if (current.equals(Tiles.TOPRIGHT) || current.equals(Tiles.RIGHTFULL) || current.equals(Tiles.A) || current.equals(Tiles.E)) {
					returnList.add(RIGHTFULL);
					returnList.add(BOTRIGHT);
					returnList.add(C);
				} else if (current.equals(Tiles.TOPFULL) || current.equals(Tiles.FULL) || current.equals(Tiles.C) || current.equals(Tiles.D)) {
					returnList.add(FULL);
					returnList.add(BOTFULL);
					returnList.add(A);
					returnList.add(B);
				} else if (current.equals(Tiles.TOPLEFT) || current.equals(Tiles.LEFTFULL) || current.equals(Tiles.B) || current.equals(Tiles.F)){
					returnList.add(BOTLEFT);
					returnList.add(LEFTFULL);
					returnList.add(D);
				} else if (current.equals(Tiles.EMPTY)) {
					returnList.add(TOPFULL);
					returnList.add(TOPRIGHT);
					returnList.add(TOPLEFT);
				} else {
					returnList.add(EMPTY);
					
				}
				//Down
			} else if (direction == 4) {
				if (current.equals(Tiles.BOTRIGHT) || current.equals(Tiles.RIGHTFULL) || current.equals(Tiles.C) || current.equals(Tiles.F)) {
					returnList.add(RIGHTFULL);
					returnList.add(TOPRIGHT);
					returnList.add(A);
				} else if (current.equals(Tiles.BOTFULL) || current.equals(Tiles.FULL) || current.equals(Tiles.A) || current.equals(Tiles.B)) {
					returnList.add(FULL);
					returnList.add(TOPFULL);
					returnList.add(C);
					returnList.add(D);
				} else if (current.equals(Tiles.BOTLEFT) || current.equals(Tiles.LEFTFULL) || current.equals(Tiles.D) || current.equals(Tiles.E)){
					returnList.add(TOPLEFT);
					returnList.add(LEFTFULL);
					returnList.add(B);
				} else if (current.equals(Tiles.EMPTY)) {
					returnList.add(BOTLEFT);
					returnList.add(BOTRIGHT);
					returnList.add(BOTFULL);
				} else {
					returnList.add(EMPTY);
				}
			}
			
			return returnList;
		}
		
		public int getIndex() {
			return this.ordinal();
		}
	}//tiles enum
	
	/**
	 * debug function to draw map in ascii generated by functionality
	 * @param index particular map object to draw
	 * @return ascii rep of object
	 */
	private String drawMap(int index) {
		String str = "";
		switch (index) {
		case 0: 
			str = " _";
			break;
		case 1:
			str = "__";
			break;
		case 2:
			str = "_ ";
			break;
		case 3:
			str = " |";
			break;
		case 4:
			str = "  ";
			break;
		case 5:
			str = "| ";
			break;
		case 6:
			str = " -";
			break;
		case 7:
			str = "--";
			break;
		case 8:
			str = "- ";
			break;
		case 9:
			str = "  ";
			break;
		case 10:
			str = "_|";
			break;
		case 11:
			str = "|_";
			break;
		case 12:
			str = "-|";
			break;
		case 13:
			str = "|-";
			break;
		}
		
		
		return str;
	}//draw map method

}//class HuntingMap

