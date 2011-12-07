package model.huntingMap;

import component.sprite.AnimatingSprite;

public abstract class Prey {

	
	/**the graphic representing the prey*/
	protected AnimatingSprite preySprite;
		
	/**whether or not the pig should be shown*/
	protected boolean show;
	
	/**how much meat the prey gives*/
	public final int MEAT;
	
	/**how many hits it takes to kill this creature*/
	protected int hitPoints;
	
	/**the x location of this prey, relative to the top left corner of the hunt map*/
	protected int xLocation;
	
	/**the y location of this prey, relative to the top left corner of the hunt map*/
	protected int yLocation;
	
	public Prey(int meat){
		this.MEAT = meat;
		
		
	}
	/**
	 * @return the show
	 */
	public boolean isShow() {
		return show;
	}

	/**
	 * @param show the show to set
	 */
	public void setShow(boolean show) {
		this.show = show;
	}

	/**
	 * @return the preySprite
	 */
	public AnimatingSprite getPreySprite() {
		return preySprite;
	}

	/**
	 * @return the mEAT
	 */
	public int getMEAT() {
		return MEAT;
	}
	
	/**
	 * returns whether the entered x and y locations are within the hit box of this creature
	 * @param shotX
	 * @param shotY
	 * @return
	 */
	public boolean inHitBox(double shotX, double shotY){
		boolean result = false;
		
		if (((shotX > preySprite.getX()) && (shotX < preySprite.getX() + preySprite.getWidth())) 
				&& ((shotX > preySprite.getX()) && (shotX < preySprite.getX() + preySprite.getWidth()))){
			//we has a hit, result is true, decrement hitpoints
			result = true;
			this.hitPoints--;
			
		}//if collision	
		return result;
	}//in hitbox method
	/**
	 * @return the xLocation
	 */
	public int getxLocation() {
		return xLocation;
	}
	/**
	 * @param xLocation the xLocation to set
	 */
	public void setxLocation(int xLocation) {
		this.xLocation = xLocation;
	}
	/**
	 * @return the yLocation
	 */
	public int getyLocation() {
		return yLocation;
	}
	/**
	 * @param yLocation the yLocation to set
	 */
	public void setyLocation(int yLocation) {
		this.yLocation = yLocation;
	}
	
	public abstract void movePrey();
}
