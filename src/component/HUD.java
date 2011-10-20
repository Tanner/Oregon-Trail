package component;

import model.datasource.HUDDataSource;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.gui.GUIContext;

import core.ConstantStore;
import core.FontManager;
import core.GameDirector;

/**
 * A HUD holds quick information for the player.
 */
public class HUD extends Component {
	private static final int PADDING = 20;
	
	private static final int WIDTH = 150;
	
	private static final int CONDITION_BAR_HEIGHT = 10;
	private static final int CONDITION_BAR_PADDING = 4;
	
	private ComponentConditionGroup<Label> memberHealthGroup, vehicleStatusGroup;
	
	/**
	 * Constructs a HUD with a {@code GUIContext} and {@code HUDDataSource}.
	 * @param context Context
	 * @param data Data source to use
	 */
	public HUD(GUIContext context, HUDDataSource data) {
		super(context, context.getWidth(), context.getHeight());
		
		Font fieldFont = GameDirector.sharedSceneListener().getFontManager().getFont(FontManager.FontID.FIELD);
		
		int height = fieldFont.getLineHeight() + CONDITION_BAR_HEIGHT + CONDITION_BAR_PADDING;

		ComponentConditionGroup<Label> groups[] = (ComponentConditionGroup<Label>[])new ComponentConditionGroup[2];
		
		// Party Members Health
		Label partyMembersHealthLabel = new Label(context, WIDTH, fieldFont, Color.white, ConstantStore.get("HUD_SCENE", "MEMBERS_HEALTH"));
		memberHealthGroup = new ComponentConditionGroup<Label>(context, WIDTH, height, CONDITION_BAR_HEIGHT, partyMembersHealthLabel, data.getPartyMembersHealth());
		groups[0] = memberHealthGroup;
		
		// Vehicle Status
		Label vehicleStatusLabel = new Label(context, WIDTH, fieldFont, Color.white, ConstantStore.get("HUD_SCENE", "VEHICLE_HEALTH"));
		vehicleStatusGroup = new ComponentConditionGroup<Label>(context, WIDTH, height, CONDITION_BAR_HEIGHT, vehicleStatusLabel, data.getVehicleStatus());
		groups[1] = vehicleStatusGroup;
		
		addAsRow(groups, getPosition(ReferencePoint.TOPLEFT), PADDING, PADDING, PADDING);
	}
}
