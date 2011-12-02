package component.sprite;

import org.newdawn.slick.Image;
import org.newdawn.slick.gui.GUIContext;

import core.ImageStore;

public class WagonSprite extends Sprite {
	private static final int WHEEL_ROTATION_TIME = 75; 
	private static final int WHEEL_ROTATION_AMOUNT = -1;
	
	private Sprite bigWheelSprite;
	private Sprite smallWheelSprite;
	private int wheelRotationUpdateCounter = 0;

	public WagonSprite(GUIContext context) {
		super(context, ImageStore.get().getImage("TRAIL_WAGON").getWidth() * 2, ImageStore.get().getImage("TRAIL_WAGON"));
		
		Image bigWheel = ImageStore.get().getImage("BIG_WHEEL_1");
		Image smallWheel = ImageStore.get().getImage("SMALL_WHEEL_1");
		
		bigWheelSprite = new Sprite(context,
				bigWheel.getWidth() * 2,
				bigWheel);
		smallWheelSprite = new Sprite(context,
				smallWheel.getWidth() * 2,
				smallWheel);
		
		add(bigWheelSprite, getPosition(ReferencePoint.BOTTOMRIGHT), ReferencePoint.BOTTOMRIGHT, -10, 0);
		add(smallWheelSprite, getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT, 50, 0);
	}

	public void update(int delta) {
		super.update(delta);
		
		wheelRotationUpdateCounter += delta;
		
		if (wheelRotationUpdateCounter > WHEEL_ROTATION_TIME) {
			bigWheelSprite.rotate(WHEEL_ROTATION_AMOUNT);
			smallWheelSprite.rotate(WHEEL_ROTATION_AMOUNT);
			
			wheelRotationUpdateCounter = 0;
		}
	}
}
