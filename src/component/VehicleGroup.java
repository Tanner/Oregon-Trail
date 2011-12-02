package component;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.GUIContext;

import component.sprite.AnimatingSprite;
import component.sprite.Sprite;
import component.sprite.WagonSprite;

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
		WagonSprite wagonSprite = new WagonSprite(context);

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