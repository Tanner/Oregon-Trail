package core;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Font;
import org.newdawn.slick.SlickException;

/**
 * Manages fonts for use in the game.
 */
public class FontStore {
	
	public enum FontID {
		H1,
		H2,
		FIELD,
		BIG_FIELD
	}
	
	private static FontStore fontManager;
	
	public static final Map<String, Font> FONTS;
	
	static {
			fontManager = new FontStore();
			FONTS = new HashMap<String, Font>();
		try {
			fontManager.init();
		} catch (SlickException e) {
			System.err.println(e.getStackTrace());
		}
		
	}
	
	public void init() throws SlickException {
		FONTS.put(FontID.H1.toString(), new AngelCodeFont("resources/fonts/04b03_h1.fnt", "resources/fonts/04b03_h1.png", false));
		FONTS.put(FontID.H2.toString(), new AngelCodeFont("resources/fonts/04b03_h2.fnt", "resources/fonts/04b03_h2.png", false));
		FONTS.put(FontID.FIELD.toString(), new AngelCodeFont("resources/fonts/04b03_field.fnt", "resources/fonts/04b03_field.png", false));
	}
	
	public static Font get(FontID font) {
		return FONTS.get(font.toString());
	}

}
