/**
 * 
 */
package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import model.huntingMap.TerrainObject;

import core.ConstantStore;


/**
 * this manufactures the terrain for the hunt scene
 */
@SuppressWarnings("serial")
public class HuntingMap implements Serializable {
	/**density of terrain in this map - high value means very dense 1-100*/
	public final int MAP_DENSITY;
	/**how much forrests over rocks dominate the landscape on this map 1 - 100*/
	public final int MAP_FORESTY;
	/**width of the hunting map	 */
	public final double MAP_WIDTH;
	/**height of the hunting map	 */
	public final double MAP_HEIGHT;
	/**x location of this section of the hunting map on the main map	 */
	public final double MAP_X_LOC;
	/**y location of this section of the hunting map on the main map */
	public final double MAP_Y_LOC;
	/**the background graphic type for this map*/
	private ConstantStore.bckGroundType bckGround;
	/**structure that holds the huntingGroundsLayout - linear coords from upper left corner to lower right, holds objects for each coord of map*/
	private Map<Double, TerrainObject> huntingGroundsMap;
	/**random generator used for entire hunt map*/
	Random huntMapRand;
	
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
		this.MAP_WIDTH = dblMapStats[0];
		this.MAP_HEIGHT = dblMapStats[1];
		this.MAP_X_LOC = dblMapStats[2];
		this.MAP_Y_LOC = dblMapStats[3];
		
		this.MAP_DENSITY = intMapStats[0];
		this.MAP_FORESTY = intMapStats[1];
		
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
		huntingGroundsMap = new HashMap<Double, TerrainObject>();
		this.generateMap();			
	}
	
	/**
	 * generates the map for this hunting instance by building terrain layouts randomly.
	 */
	private void generateMap(){
		//build the max x and y coords of the map, with each representing a spot to place a graphic
		int xWidth = (int) (this.MAP_WIDTH / 48.0);
		int yHeight = (int) (this.MAP_HEIGHT / 48.0);
		
		for(int xCoord = 0; xCoord < xWidth; xCoord++){
			for (int yCoord = 0; yCoord < yHeight; yCoord++){
				//randomly build the hunt map
				int paintHere = huntMapRand.nextInt(100);
				if (paintHere < this.MAP_DENSITY){
					//means we are going to paint a new terrain item here
					//will be either trees, rocks or special location
					//rocks not currently implemented, will be treated like painting nothing
					int terrainChoice = huntMapRand.nextInt(100);
					if (terrainChoice < this.MAP_FORESTY){
						//this is the beginning of a forest terrain block
					
						
					} else {
						//this is a rocky terrain block
					

						
					}//pick terrain
				}//paint terrain item here

			}//for each y coord	
		}//for each x coord
		
		
		
		
		
	}//generate map method

}
