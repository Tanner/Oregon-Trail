package component;

import org.newdawn.slick.Color;

public class AnimatingColor {
	private Color oldColor;
	private Color newColor;
	private int duration;
	private int progress;
	private boolean animating;
	
	public AnimatingColor(Color oldColor, Color newColor, int duration) {
		this.oldColor = oldColor;
		this.newColor = newColor;
		
		this.duration = duration;
		progress = 0;
		animating = true;
	}
	
	public Color getColor(int delta) {
		if (!animating) {
			return newColor;
		}
		
		progress += delta;
						
		if (progress >= duration) {
			animating = false;
			return newColor;
		}
		
		return oldColor.scaleCopy(1f - (float)progress / (float)duration).addToCopy(newColor.scaleCopy((float)progress / (float)duration));
	}
}
