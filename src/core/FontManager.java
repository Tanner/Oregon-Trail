package core;

import java.awt.Color;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.font.effects.ShadowEffect;

/**
 * Manages fonts for use in the game.
 */
public class FontManager {
	public enum FontID {
		H1,
		H2,
		FIELD,
		BIG_FIELD
	}
	
	private UnicodeFont h1Font;
	private UnicodeFont h2Font;
	private UnicodeFont fieldFont;
	private UnicodeFont bigFieldFont;
	
	/**
	 * Initializes everything for use.
	 */
	public void init() {
		try {
			String glyphsToAdd = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890- .,:!\"'?$";
			h1Font = new UnicodeFont("resources/04B_03__.ttf", 48, true, false);
//			h1.addAsciiGlyphs();
			h1Font.addGlyphs(glyphsToAdd);
			h1Font.getEffects().add(new ShadowEffect(Color.black, 0, 1, 1f));
			h1Font.getEffects().add(new ColorEffect());
			h1Font.loadGlyphs();
			
			h2Font = new UnicodeFont("resources/04B_03__.ttf", 32, true, false);
			h2Font.addGlyphs(glyphsToAdd);
			h2Font.getEffects().add(new ShadowEffect(Color.black, 0, 1, 1f));
			h2Font.getEffects().add(new ColorEffect());
			h2Font.loadGlyphs();
			
			fieldFont = new UnicodeFont("resources/04B_03__.ttf", 20, true, false);
			fieldFont.addGlyphs(glyphsToAdd);
			fieldFont.getEffects().add(new ShadowEffect(Color.black, 0, 1, 1f));
			fieldFont.getEffects().add(new ColorEffect());
			fieldFont.loadGlyphs();
			
			bigFieldFont = new UnicodeFont("resources/04B_03__.ttf", 28, true, false);
			bigFieldFont.addGlyphs(glyphsToAdd);
			bigFieldFont.getEffects().add(new ShadowEffect(Color.black, 0, 1, 1f));
			bigFieldFont.getEffects().add(new ColorEffect());
			bigFieldFont.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the font for the ID.
	 * @param id Requested font ID
	 * @return Font requested or null if not found
	 */
	public UnicodeFont getFont(FontID id) {
		switch(id) {
		case H1:
			return h1Font;
		case H2:
			return h2Font;
		case FIELD:
			return fieldFont;
		case BIG_FIELD:
			return fieldFont;
		}
		
		return null;
	}
}
