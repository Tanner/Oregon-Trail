package component.hud;

import org.newdawn.slick.gui.GUIContext;

import component.Component;

/**
 * A HUD presents information to the user.
 */
public abstract class HUD extends Component {
	public static final int HEIGHT = 80;
	
	/**
	 * Constructs a HUD.
	 * @param context Context
	 * @param width Width
	 * @param height Height
	 */
	public HUD(GUIContext context, int width, int height) {
		super(context, width, height);
	}
}
