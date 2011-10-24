package component;

import org.newdawn.slick.Color;
import org.newdawn.slick.gui.GUIContext;

import core.GameDirector;
import core.FontManager.FontID;

public class Tooltip extends Label {
	private static final int PADDING = 5;
	
	private Component owner;

	public Tooltip(GUIContext context, Component owner, String message) {
		super(context,
				GameDirector.sharedSceneListener().getFontManager().getFont(FontID.FIELD).getWidth(message) + PADDING * 2,
				GameDirector.sharedSceneListener().getFontManager().getFont(FontID.FIELD).getLineHeight() * Label.getNumberOfNewlines(message) + PADDING * 2,
				GameDirector.sharedSceneListener().getFontManager().getFont(FontID.FIELD), 
				Color.white,
				message);
		
		this.owner = owner;
				
		setBackgroundColor(Color.black);
	}
	
	public Component getOwner() {
		return owner;
	}
	
	@Override
	public boolean isVisible() {
		return true;
	}
}
