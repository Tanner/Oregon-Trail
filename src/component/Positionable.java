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
	 * Sets the position in relation to the given reference point.
	 * @param location The new location
	 * @param referencePoint Reference point relative to this where to set the new position
	 */
	public void setPosition(Vector2f location, ReferencePoint referencePoint);
	
	/**
	 * Sets the position in relation to the given reference point with an offset.
	 * @param location The new location
	 * @param referencePoint Reference point relative to this where to set the new position
	 * @param xOffset The horizontal offset from the location for the new position
	 * @param yOffset The vertical offset from the location for the new position
	 */
	public void setPosition(Vector2f location, ReferencePoint referencePoint, int xOffset, int yOffset);
	
	/**
	 * Get the position in relation to the given reference point.
	 * @param referencePoint Reference point relative to this
	 * @return Position at the provided reference point
	 */
	public Vector2f getPosition(ReferencePoint referencePoint);
}
