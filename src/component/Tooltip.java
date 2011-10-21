package component;

import org.newdawn.slick.Color;
import org.newdawn.slick.gui.GUIContext;

import core.GameDirector;
import core.FontManager.FontID;

public class Tooltip extends Component {
	private Component owner;

	public Tooltip(GUIContext context, int width, int height, Component owner, String message) {
		super(context, width, height);
		
		this.owner = owner;
		
		Label label = new Label(context, width, height, GameDirector.sharedSceneListener().getFontManager().getFont(FontID.FIELD), Color.white, message);
		add(label, getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
		
		setBackgroundColor(Color.black);
	}
	
	public Component getOwner() {
		return owner;
	}
}
