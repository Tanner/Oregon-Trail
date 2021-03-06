package component;

import org.newdawn.slick.Color;
import org.newdawn.slick.gui.GUIContext;

import core.FontStore;
import core.FontStore.FontID;

public class Tooltip extends Label {
	private static final int PADDING = 5;
	
	private Component owner;

	public Tooltip(GUIContext context, Component owner, String message) {
		super(context,
				FontStore.get().getFont(FontID.FIELD).getWidth(message) + PADDING * 2,
				FontStore.get().getFont(FontID.FIELD).getLineHeight() * Label.getNumberOfNewlines(message) + PADDING * 2,
				FontStore.get().getFont(FontID.FIELD), 
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
