package component;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.GUIContext;

import component.sprite.AnimatingSprite;

import core.FontStore;
import core.ImageStore;
import core.FontStore.FontID;

public class VehicleGroup extends GridComponent {
	private static final int X_SPACING = 5;
	private static final int Y_SPACING = 5;
	private static final int CONDITION_BAR_HEIGHT = 20;
	private static final double STEP_CHANCE = 0.01;
	private static final int STEP_Y_DISPLACEMENT = 1;

	public VehicleGroup(GUIContext context, VehicleDataSource dataSource) {
		super(context, createComponents(context, dataSource), X_SPACING, Y_SPACING);
		
		this.setShouldUpdateComponents(true);
	}

	private static final Component[][] createComponents(GUIContext context, VehicleDataSource dataSource) {
		Animation wagonAnimation = new Animation(new Image[] {
				ImageStore.get().getImage("TRAIL_WAGON") 
				}, 1);
		Animation bigWheelAnimation = new Animation(new Image[] {
				ImageStore.get().getImage("BIG_WHEEL_3"),
				ImageStore.get().getImage("BIG_WHEEL_2"),
				ImageStore.get().getImage("BIG_WHEEL_1")
		}, 250);
		Animation smallWheelAnimation = new Animation(new Image[] {
				ImageStore.get().getImage("SMALL_WHEEL_3"),
				ImageStore.get().getImage("SMALL_WHEEL_2"),
				ImageStore.get().getImage("SMALL_WHEEL_1")
		}, 250);
		
		AnimatingSprite wagonSprite = new AnimatingSprite(context,
				wagonAnimation.getWidth() * 2,
				wagonAnimation,
				AnimatingSprite.Direction.RIGHT);
		wagonSprite.setShouldUpdateComponents(true);
		AnimatingSprite bigWheelSprite = new AnimatingSprite(context,
				bigWheelAnimation.getWidth() * 2,
				bigWheelAnimation,
				AnimatingSprite.Direction.RIGHT);
		AnimatingSprite smallWheelSprite = new AnimatingSprite(context,
				smallWheelAnimation.getWidth() * 2,
				smallWheelAnimation,
				AnimatingSprite.Direction.RIGHT);
		
		bigWheelAnimation.setAutoUpdate(true);
		
		wagonSprite.add(bigWheelSprite, wagonSprite.getPosition(ReferencePoint.BOTTOMRIGHT), ReferencePoint.BOTTOMRIGHT, -45, 0);
		wagonSprite.add(smallWheelSprite, wagonSprite.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT, 5, 0);		

		int componentWidth = wagonSprite.getWidth();
				
		ConditionBar conditionBar = new ConditionBar(context,
				componentWidth,
				CONDITION_BAR_HEIGHT,
				dataSource.getCondition(),
				FontStore.get().getFont(FontID.FIELD));
		
		Component components[][] = {
				{ conditionBar },
				{ wagonSprite }
		};
		
		return components;
	}
	
	@Override
	public void update(int delta) {
		super.update(delta);
		
		this.setTranslation(0, 0);
		if (Math.random() < STEP_CHANCE) {
			int upOrDown = Math.random() < 0.5 ? -1 : 1;
			this.setTranslation(0, upOrDown * STEP_Y_DISPLACEMENT);
		}
	}
}