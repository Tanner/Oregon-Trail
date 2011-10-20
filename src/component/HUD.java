package component;

import model.datasource.HUDDataSource;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.gui.GUIContext;

import core.FontManager;
import core.GameDirector;

public class HUD extends Component {
	private static final int PADDING = 20;
	
	private static final int BUTTON_WIDTH = 200;
	
	private static final int CONDITION_BAR_HEIGHT = 10;
	private static final int CONDITION_BAR_PADDING = 4;
	
	private ComponentConditionGroup<Label> memberHealthGroup;
	
	public HUD(GUIContext context, HUDDataSource data) {
		super(context, context.getWidth(), context.getHeight());
		
		Font fieldFont = GameDirector.sharedSceneListener().getFontManager().getFont(FontManager.FontID.FIELD);
		
		int height = fieldFont.getLineHeight() + CONDITION_BAR_HEIGHT + CONDITION_BAR_PADDING;

		Label label = new Label(context, BUTTON_WIDTH, fieldFont, Color.white, "Party Members");
		memberHealthGroup = new ComponentConditionGroup<Label>(context, BUTTON_WIDTH, height, CONDITION_BAR_HEIGHT, label, data.getPartyMembersHealth());
		
		add(memberHealthGroup, getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT, PADDING, PADDING);
	}
}
