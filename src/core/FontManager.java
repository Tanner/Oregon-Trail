package core;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;


public class FontManager {
	public enum FontID {
		H1,
		H2
	}
	
	private UnicodeFont h1;
	private UnicodeFont h2;
	
	public void init() {
		try {
			String glyphsToAdd = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890- .!";
			h1 = new UnicodeFont("resources/LVDCGO.ttf", 36, true, false);
//			h1.addAsciiGlyphs();
			h1.addGlyphs(glyphsToAdd);
			h1.getEffects().add(new ColorEffect());
			h1.loadGlyphs();
			
			h2 = new UnicodeFont("resources/LVDCGO.ttf", 18, true, false);
			h2.addGlyphs(glyphsToAdd);
			h2.getEffects().add(new ColorEffect());
			h2.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public UnicodeFont getFont(FontID id) {
		switch(id) {
		case H1:
			return h1;
		case H2:
			return h2;
		}
		
		return null;
	}
}
