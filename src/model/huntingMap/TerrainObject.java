/**
 * 
 */
package model.huntingMap;

import java.io.Serializable;

//import model.Condition;
import core.ConstantStore;

/**
 * this represents an object on the hunt map.
 */
@SuppressWarnings("serial")
public class TerrainObject implements Serializable{
	/**name of the image used to represent this object*/
	private String imgName;
	/**name of the image used to represent this object*/
	private String imgPathName;
	/**name of the image to represent the collisionImage of this object*/
	private String imgPathShadName;
	/**terrain type under this object*/
	private ConstantStore.bckGroundType baseTerrain;
	/**x coord of this object in hunting map*/
	private double objXCoord;
	/**y coord of this object in hunting map*/
	private double objYCoord;
	/**moveSpeed mod for this terrain object*/
	private double moveMod;
	/**stops shots?*/
	private double stopShotChance;
	/**x size of image associated with this object*/
	private final int imageX;
	/**y size of image associated with this object*/
	private final int imageY;
	/**the string name of the imagestore image corresponding to this image*/
	private String imageStoreName;
	/**the string name of the imagestore image corresponding to this image's collision shadow*/
	private String imageStoreShadName;
	
	
	
	/**
	 * @return the imageX
	 */
	public int getImageX() {
		return imageX;
	}

	/**
	 * @return the imageY
	 */
	public int getImageY() {
		return imageY;
	}

	/**
	 * builds a terrain object
	 * @param imgName the base file name for the image representing this object
	 * @param objXCoord x coord of object in hunting map
	 * @param objYCoord y coord of object in hunting map
	 * @param baseTerrain background terrain type
	 * @param moveMod movement modifier of this terrain object
	 * @param the likelihood that this object will stop a shot
	 */
	public TerrainObject(String imgName, double objXCoord, double objYCoord, ConstantStore.bckGroundType baseTerrain, double moveMod, double stopShotChance, int imgXSize, int imgYSize) {
		this.imageX = imgXSize;
		this.imageY = imgYSize;
		this.imgName = imgName;
		this.imgPathName = ConstantStore.PATH_HUNTTERRAIN + imgName + ".png";
		this.imgPathShadName = ConstantStore.PATH_HUNTTERRAIN + imgName + "Shad.png";
		this.imageStoreName = "HUNT_" + imgName.toUpperCase();
		this.imageStoreShadName = "HUNT_" + imgName.toUpperCase() + "SHAD";
		this.objXCoord = objXCoord;
		this.objYCoord = objYCoord;
		this.baseTerrain = baseTerrain;
		this.moveMod = moveMod;
		this.stopShotChance = stopShotChance;
		
	}//constructor
	

	/**
	 * @return the imageStoreName
	 */
	public String getImageStoreName() {
		return imageStoreName;
	}

	/**
	 * @return the imageStoreShadName
	 */
	public String getImageStoreShadName() {
		return imageStoreShadName;
	}

	/**
	 * @return the imgName
	 */
	public String getImgName() {
		return imgName;
	}

	/**
	 * @param imgName the imgName to set
	 */
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	/**
	 * @return the imgName
	 */
	public String getImgPathName() {
		return imgPathName;
	}

	/**
	 * @param imgName the imgName to set
	 */
	public void setImgPathName(String imgPathName) {
		this.imgPathName = imgPathName;
	}

	/**
	 * @return the imgShadName
	 */
	public String getImgShadName() {
		return imgPathShadName;
	}

	/**
	 * @param imgShadName the imgShadName to set
	 */
	public void setImgPathShadName(String imgShadName) {
		this.imgPathShadName = imgShadName;
	}

	/**
	 * @return the baseTerrain
	 */
	public ConstantStore.bckGroundType getBaseTerrain() {
		return baseTerrain;
	}

	/**
	 * @param baseTerrain the baseTerrain to set
	 */
	public void setBaseTerrain(ConstantStore.bckGroundType baseTerrain) {
		this.baseTerrain = baseTerrain;
	}

	/**
	 * @return the objXCoord
	 */
	public double getObjXCoord() {
		return objXCoord;
	}

	/**
	 * @param objXCoord the objXCoord to set
	 */
	public void setObjXCoord(double objXCoord) {
		this.objXCoord = objXCoord;
	}

	/**
	 * @return the objYCoord
	 */
	public double getObjYCoord() {
		return objYCoord;
	}

	/**
	 * @param objYCoord the objYCoord to set
	 */
	public void setObjYCoord(double objYCoord) {
		this.objYCoord = objYCoord;
	}

	/**
	 * @return the moveMod
	 */
	public double getMoveMod() {
		return moveMod;
	}

	/**
	 * @param moveMod the moveMod to set
	 */
	public void setMoveMod(double moveMod) {
		this.moveMod = moveMod;
	}

	/**
	 * @return the stopShotChance
	 */
	public double getStopShotChance() {
		return stopShotChance;
	}

	/**
	 * @param stopShotChance the stopShotChance to set
	 */
	public void setStopShotChance(double stopShotChance) {
		this.stopShotChance = stopShotChance;
	}

}
