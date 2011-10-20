package component;

import model.datasource.HUDDataSource;

import org.newdawn.slick.gui.GUIContext;

public class HUD extends Component {
	public HUD(GUIContext context, HUDDataSource data) {
		super(context, context.getWidth(), context.getHeight());
	}
}
