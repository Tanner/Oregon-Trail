package model.item;


/**
 * The wagon object, by which we shall storm the trail
 */
@SuppressWarnings("serial")
public class Wagon extends Vehicle {
	/**
	 * Makes a wagon
	 */
	public Wagon() {
		super(2000, ItemType.WAGON);
	}
}
