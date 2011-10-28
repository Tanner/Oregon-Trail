package component;

import org.newdawn.slick.Color;

public class AnimatingColor extends Color {
	private Color oldColor;
	private Color newColor;
	private int duration;
	private int progress;
	private boolean animating;
	
	public AnimatingColor(Color oldColor, Color newColor, int duration) {
		super(oldColor);
		
		this.oldColor = oldColor;
		this.newColor = newColor;
		
		this.duration = duration;
		progress = 0;
		animating = true;
	}
	
	private void setRGBA(Color color) {
		this.r = color.r;
		this.g = color.g;
		this.b = color.b;
		this.a = color.a;
	}
	
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
