package component;

import org.newdawn.slick.Color;

/**
 * A {@code Color} which animates (can be toggled) from one {@code Color} to another {@code Color}.
 */
@SuppressWarnings("serial")
public class AnimatingColor extends Color {
	private Color oldColor;
	private Color newColor;
	private int duration;
	private int progress;
	private boolean animating;
	
	/**
	 * Constructs an AnimatingColor with the first color, second color, and the duration.
	 * @param oldColor Color to start with
	 * @param newColor Color to end with
	 * @param duration Duration it takes to animate from the oldColor to the newColor
	 */
	public AnimatingColor(Color oldColor, Color newColor, int duration) {
		super(oldColor);
		
		this.oldColor = oldColor;
		this.newColor = newColor;
		
		this.duration = duration;
		progress = 0;
		animating = true;
	}
	
	/**
	 * Set the RGB + Alpha of the current color displayed.
	 * @param color {@code Color} to set
	 */
	private void setRGBA(Color color) {
		this.r = color.r;
		this.g = color.g;
		this.b = color.b;
		this.a = color.a;
	}
	
	/**
	 * Update the {@code Color} animation.
	 * @param delta Change in time from last update (in milliseconds)
	 */
	public void update(int delta) {
		if (!animating) {
			setRGBA(newColor);
		}
		
		progress += delta;
						
		if (progress >= duration) {
			animating = false;
			
			setRGBA(newColor);
		}
		
		Color animatingColor = oldColor.scaleCopy(1f - (float)progress / (float)duration).addToCopy(newColor.scaleCopy((float)progress / (float)duration));
		setRGBA(animatingColor);
	}
}
