package component;

import java.util.Random;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.GUIContext;

import component.sprite.AnimatingSprite;
import core.FontStore;
import core.ImageStore;
import core.FontStore.FontID;

public class PartyMemberGroup extends GridComponent {
	private static final int X_SPACING = 5;
	private static final int Y_SPACING = 5;
	private static final int CONDITION_BAR_HEIGHT = 20;
	private static final double STEP_CHANCE = 0.01;
	private static final int STEP_Y_DISPLACEMENT = 1;
	/**need to use the trapper for every group, since trapper is only hunter toon*/

	public PartyMemberGroup(GUIContext context, PartyMemberDataSource dataSource) {
		super(context, createComponents(context, dataSource), X_SPACING, Y_SPACING);
		this.setShouldUpdateComponents(true);
	}

	private static final Component[][] createComponents(GUIContext context, PartyMemberDataSource dataSource) {
		Animation animation;
		Random partyRand = new Random();
		//trapper needs to be in every group to use in hunt scene - leader is always trapper.  if leader is a girl, then she's just hairy
		if (dataSource.isLeader()){
			animation = new Animation(new Image[] {
					ImageStore.get().getImage("TRAPPER_RIGHT")
			}, 1);
		} else if (dataSource.isMale()) { //male but not leader
			if (partyRand.nextInt(10) < 7){//increase chance of hillbilly since trapper is going to be in party always
				animation = new Animation(new Image[] {
						ImageStore.get().getImage("HILLBILLY_RIGHT")
				}, 1);
				
			} else {
				animation = new Animation(new Image[] {
						ImageStore.get().getImage("TRAPPER_RIGHT")
				}, 1);			
			}
		} else {//female, not leader
			animation = new Animation(new Image[] {
					ImageStore.get().getImage("MAIDEN_RIGHT")
			}, 1);
		}
		
		AnimatingSprite sprite = null;
		sprite = new AnimatingSprite(context,
				animation.getWidth() * 2,
				animation,
				AnimatingSprite.Direction.RIGHT);
		
		int componentWidth = sprite.getWidth();
		
		Label nameLabel = new Label(context, componentWidth, FontStore.get().getFont(FontID.FIELD), Color.white, dataSource.getName());
		
		ConditionBar conditionBar = new ConditionBar(context,
				componentWidth,
				CONDITION_BAR_HEIGHT,
				dataSource.getCondition(),
				FontStore.get().getFont(FontID.FIELD));
		
		Component components[][] = {
				{ nameLabel },
				{ conditionBar },
				{ sprite }
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
