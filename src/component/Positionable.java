package component;

import org.newdawn.slick.geom.Vector2f;

/**
 * Interface for the changing and getting the positions.
 * 
 * @author Tanner Smith
 */
public interface Positionable {
	public static enum ReferencePoint {
		TopLeft, TopCenter, TopRight, CenterLeft, CenterCenter, CenterRight, BottomLeft, BottomCenter, BottomRight
	}
	
	public void setPosition(int x, int y, ReferencePoint referencePoint);
	public Vector2f getPosition(ReferencePoint referencePoint);
}
