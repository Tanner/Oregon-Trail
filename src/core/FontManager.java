package core;

import java.awt.Color;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.font.effects.ShadowEffect;

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
			String glyphsToAdd = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890- .,:!\"'?$";
			h1 = new UnicodeFont("resources/04B_03__.ttf", 42, true, false);
//			h1.addAsciiGlyphs();
			h1.addGlyphs(glyphsToAdd);
			h1.getEffects().add(new ShadowEffect(Color.black, 0, 1, 1f));
			h1.getEffects().add(new ColorEffect());
			h1.setPaddingBottom(1);
			h1.loadGlyphs();
			
			h2 = new UnicodeFont("resources/04B_03__.ttf", 28, true, false);
			h2.addGlyphs(glyphsToAdd);
			h2.getEffects().add(new ShadowEffect(Color.black, 0, 1, 1f));
			h2.getEffects().add(new ColorEffect());
			h2.setPaddingBottom(1);
			h2.loadGlyphs();
			
			fieldFont = new UnicodeFont("resources/04B_03__.ttf", 20, true, false);
			fieldFont.addGlyphs(glyphsToAdd);
			fieldFont.getEffects().add(new ShadowEffect(Color.black, 0, 1, 1f));
			fieldFont.getEffects().add(new ColorEffect());
			fieldFont.setPaddingBottom(1);
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
