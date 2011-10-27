package component;

import org.newdawn.slick.Color;

public class AnimatingColor {
	private Color oldColor;
	private Color newColor;
	private float duration;
	private float progress;
	private boolean animating;
	
	public AnimatingColor(Color oldColor, Color newColor, float duration) {
		this.oldColor = oldColor;
		this.newColor = newColor;
		
		this.duration = duration;
		progress = 0f;
		animating = true;
	}
	
	public Color getColor() {
		if (!animating) {
			return newColor;
		}
		
		progress += 1f;
		
		if (progress >= duration) {
			animating = false;
		}
		
		return oldColor.scaleCopy(1f - progress / duration).addToCopy(newColor.scaleCopy(progress / duration));
	}
}
