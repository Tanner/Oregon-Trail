package component;

import org.newdawn.slick.geom.Vector2f;

/**
 * Interface for the changing and getting the positions.
 */
public interface Positionable {
	public static enum ReferencePoint {
		TopLeft, TopCenter, TopRight, CenterLeft, CenterCenter, CenterRight, BottomLeft, BottomCenter, BottomRight
	}
	
	/**
	 * Sets the position of a component in relation to the given reference point.
	 * @param x New X
	 * @param y New Y
	 * @param referencePoint Reference point in which to set the new position
	 */
	public void setPosition(Vector2f location, ReferencePoint referencePoint);
	
	public void setPosition(Vector2f location, ReferencePoint referencePoint, int xOffset, int yOffset);
	
	/**
	 * Get the position of a component in relation to the given reference point.
	 * @param referencePoint Reference point in which to get the position
	 * @return Position of the component
	 */
	public Vector2f getPosition(ReferencePoint referencePoint);
}
