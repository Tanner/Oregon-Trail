package model.item;



import model.Item;

/**
 * The wagon object, by which we shall storm the trail
 * @author NULL&void
 *
 */

public class Wagon extends Vehicle {
	
	/**
	 * Makes a wagon
	 */
	public Wagon() {
		super(2000, Item.ITEM_TYPE.WAGON);
	}
}
