/**
 * 
 */
package component;

//import java.util.Map;

import java.util.HashMap;
import java.util.Map;

import model.HuntingMap;
import model.huntingMap.TerrainObject;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.GUIContext;

import component.Positionable.ReferencePoint;
import component.sprite.Sprite;

import core.ConstantStore;
import core.ImageStore;


/**
 * builds the hunting grounds panel that will hold the terrain that the hunting minigame will occur upon
 */
public class HuntingGroundsComponent extends Component {

	private Panel huntLocalPanel;
	
	/**# of map boxes in the x direction (# of cols)*/
	private int numCols;
	/**#of map boxes in the y direction (# of rows)*/
	private int numRows;
	/** structure holding a map of sprites based on the sprite's coordinates on the map, used to determine collision  */
	private Image[][] collisionMap;
	/** main layer width in pix - subtract from this.getx to get accurate topleft origin for panel*/
	private int mainLayerWidth;
	/** main layer height in pix - subtract from this.gety to get accurate topleft origin for panel*/
	private int mainLayerHeight;
	
	
	private HuntingMap huntLayout;
	
	public HuntingGroundsComponent(GUIContext context, int width, int height, HuntingMap huntLayout, int containerWidth, int containerHeight) {
		super(context, width, height);

		//this.collisionMap = new HashMap<Coord,Image>();
		this.huntLayout = huntLayout;
		this.numCols = this.huntLayout.getHuntingGroundsMap()[0].length;
		this.numRows = this.huntLayout.getHuntingGroundsMap().length;
		this.collisionMap = new Image[numRows][numCols];

		
		this.mainLayerWidth = containerWidth;
		this.mainLayerHeight = containerHeight;
		
		this.huntLocalPanel = new Panel(container, width, height);
		
		
		add(huntLocalPanel,getPosition(ReferencePoint.TOPLEFT),ReferencePoint.TOPLEFT);
	}//constructor

	public void displayTerrain(GUIContext context, HuntingMap huntLayout){
	
		for (int huntMapCols = 0; huntMapCols < numCols; huntMapCols ++){
			for (int huntMapRows = 0; huntMapRows < numRows; huntMapRows++){
				//System.out.printf("x%d y%d %s | ", huntMapCols, huntMapRows, huntLayout.getHuntingGroundsMap()[huntMapRows][huntMapCols].getImageStoreName() );
				//Sprite tempShadImage = new Sprite(context,ImageStore.get().IMAGES.get(huntLayout.getHuntingGroundsMap()[huntMapRows][huntMapCols].getImageStoreShadName()));
				Image tmpImage = ImageStore.get().IMAGES.get(huntLayout.getHuntingGroundsMap()[huntMapRows][huntMapCols].getImageStoreShadName());
				collisionMap[huntMapRows][huntMapCols] = tmpImage;

				if (!huntLayout.getHuntingGroundsMap()[huntMapRows][huntMapCols].getImageStoreName().contains("9")){
					Sprite tempImage = new Sprite(context,ImageStore.get().IMAGES.get(huntLayout.getHuntingGroundsMap()[huntMapRows][huntMapCols].getImageStoreName()));
					huntLocalPanel.add(tempImage, huntLocalPanel.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT, (int) (huntMapCols * huntLayout.getTILE_WIDTH()), (int)(huntMapRows * huntLayout.getTILE_HEIGHT()));
					//huntLocalPanel.add(tempShadImage, huntLocalPanel.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT, (int) (huntMapCols * huntLayout.getTILE_WIDTH()), (int)(huntMapRows * huntLayout.getTILE_HEIGHT()));
				}
			}//for huntmapy
			//System.out.printf("\n");
		}//for huntmapx
	}
	
	/**
	 * if not colliding with anything, move toon appropriately
	 * @param moveMapX the amount to move in the x direction
	 * @param moveMapY the amount to move in the y direction
	 * @param toonSprite the hunter's sprite
	 * @param direction 0-8 for cardinal direction of toon
	 */

	public void moveToon(int moveMapX, int moveMapY, Sprite hunterSprite, int direction) {
		//if direction equals 4 then not moving - no need to check for collision
		int toonHeight = hunterSprite.getHeight();
		int toonWidth = hunterSprite.getWidth();
		int toonX = hunterSprite.getX();
		int toonY = hunterSprite.getY();
		moveMapX *= .5;
		moveMapY *= .5;
		int toonAbsX = -1 * (this.getX() - (this.mainLayerWidth/2));//absolute position of toon relative to underlying panel's position within layer
		int toonAbsY = -1 * (this.getY() - (this.mainLayerHeight/2));//absolute position of toon relative to underlying panel's position within layer
		int edgeX = toonAbsX + 24;
		int edgeY = toonAbsY + 24;

		
		if (direction != 4){
			double moveMod = 1;
			//System.out.println("before x:" + edgeX+ "|y:" + edgeY + " | toonX : " + toonX + " toonY : " + toonY);
			
			//System.out.println(" hunt panel's location " + this.getX() + " | " + this.getY() + ": accurate toon X : " + toonAbsX + " Y : " + toonAbsY + " toon's direction : " + direction);
		
			if ((direction == 0) || (direction == 1) || (direction == 2)){//toon moving up - use toon height/2 subtracted from y location for collision	
				edgeY -= toonHeight/2;
			}	
			if ((direction == 6) || (direction == 7) || (direction == 8)){//toon moving down -  use toon height/2 added to y location for collision		
				edgeY += toonHeight/2;
			}
			if ((direction == 0) || (direction == 3) || (direction == 6)){//toon moving left - use toon width/2 subtracted from x location for collision	
				edgeX -= toonWidth;
			}
			if ((direction == 2) || (direction == 5) || (direction == 8)){//toon moving right - use toon width/2 added to x location for collision	
				edgeX += toonWidth/2;			
			}
			
			
			//System.out.println("\tx:" + edgeX+ "|y:" + edgeY + " | toonX : " + toonX + " toonY : " + toonY);
			//pass appropriate edge to test to find movement modifier based on terrain
			moveMod = terrainCollision(edgeX, edgeY);
			if (moveMod < 0){
				this.setLocation((int)(-1 * ((this.huntLayout.MAP_WIDTH) /2) + mainLayerWidth/2), (int)(-1 * (this.huntLayout.MAP_HEIGHT) + mainLayerHeight/2));
			} else {
				moveMapX *= moveMod;
				moveMapY *= moveMod;
				this.setLocation(this.getX() + moveMapX, this.getY()+ moveMapY);
			}
		}//if not 4	
		else {
			double tmp = terrainCollision(edgeX, edgeY);
			if (tmp <= 0){
				this.setLocation(this.getX() + 1, this.getY() + 1);
			}
		}
	}//move toon method
	
	/**
	 * determines the terrain-caused movement modification, if any, based on the terrain color at a particular point
	 * @param mobX the actor's X location
	 * @param mobY the actor's Y location
	 * @return the movement speed multiplicative modifier based on terrain - either .6 for trees or 0 for rocks (no movement)
	 */	
	public double terrainCollision(int mobX, int mobY){
		double resMod = 1;
		
		int tileCol = mobX / ((int)this.huntLayout.TILE_WIDTH);
		int tileRow = mobY /((int)this.huntLayout.TILE_HEIGHT);
		//System.out.println("\tin collision col: " + tileCol +" row: " + tileRow );
		//System.out.println("\tx coord of toon X : " + mobX + " coord of toon Y :" + mobY);
		
		int pixX = mobX % ((int)this.huntLayout.TILE_WIDTH);
		int pixY = mobY % ((int)this.huntLayout.TILE_HEIGHT);
		//System.out.println("\timage pixel X val  " + pixX + " image pixel Y val " + pixY);

		//System.out.println(collisionMap);
		//System.out.println(this.collisionMap.size());
		if (((tileRow > 0) && (tileRow < this.huntLayout.getHuntingGroundsMap().length-1)) 
			&& (tileCol > 0) && (tileCol < this.huntLayout.getHuntingGroundsMap()[0].length-1)){
			
			Image tmpImage = this.collisionMap[tileRow][tileCol];
			Color colColor = new Color(1,1,1,0);
			if (tmpImage != null){
				colColor = tmpImage.getColor(pixX, pixY);
				//System.out.println(" color at location given : " + colColor );	
			} else {
				//System.out.println(" no color at location given " );
			}
			
			if (colColor.equals(Color.black)){//stones
				resMod = 0;
				
			} else if (colColor.equals(Color.red)) {//trees
				resMod = .4;
			}
		} else {
			resMod = -1;
		}
		return resMod;
	}
	
	
	
}//hunting grounds component
