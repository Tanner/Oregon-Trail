package core;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

/**
 * Manages fonts for use in the game.
 * @author Ryan Ashcraft
 */
public class FontManager {
	public enum FontID {
		H1,
		H2,
		FIELD
	}
	
	private UnicodeFont h1;
	private UnicodeFont h2;
	private UnicodeFont fieldFont;
	
	/**
	 * Initializes everything for use.
	 */
	public void init() {
		try {
			String glyphsToAdd = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890- .!\"'?$";
			h1 = new UnicodeFont("resources/ARCADEPI.ttf", 36, true, false);
//			h1.addAsciiGlyphs();
			h1.addGlyphs(glyphsToAdd);
			h1.getEffects().add(new ColorEffect());
			h1.loadGlyphs();
			
			h2 = new UnicodeFont("resources/ARCADEPI.ttf", 24, true, false);
			h2.addGlyphs(glyphsToAdd);
			h2.getEffects().add(new ColorEffect());
			h2.loadGlyphs();
			
			fieldFont = new UnicodeFont("resources/ARCADEPI.ttf", 15, true, false);
			fieldFont.addGlyphs(glyphsToAdd);
			fieldFont.getEffects().add(new ColorEffect());
			fieldFont.loadGlyphs();
			
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the font for the ID.
	 * @param id Requested font ID
	 * @return Font
	 */
	public UnicodeFont getFont(FontID id) {
		switch(id) {
		case H1:
			return h1;
		case H2:
			return h2;
		case FIELD:
			return fieldFont;
		}
		
		return null;
	}
}
