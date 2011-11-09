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
	
	private static FontStore fontStore;
	
	public final Map<String, Font> FONTS;
	
	/**
	 * Constructs a new {@code FontStore} and initializes it.
	 */
	private FontStore() {
		fontStore = this;
		
		FONTS = new HashMap<String, Font>();
		
		try {
			initialize();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Initializes the store with all the images.
	 * @throws SlickException
	 */
	private void initialize() throws SlickException {
		FONTS.put(FontID.H1.toString(), new AngelCodeFont("resources/fonts/04b03_h1.fnt", "resources/fonts/04b03_h1.png", false));
		FONTS.put(FontID.H2.toString(), new AngelCodeFont("resources/fonts/04b03_h2.fnt", "resources/fonts/04b03_h2.png", false));
		FONTS.put(FontID.FIELD.toString(), new AngelCodeFont("resources/fonts/04b03_field.fnt", "resources/fonts/04b03_field.png", false));
	}
	
	/**
	 * Get the {@code Font} for the key.
	 * @param name Key for the font
	 * @return Font at that key, if any
	 */
	public Font getFont(FontID font) {
		return FONTS.get(font.toString());
	}
	
	/**
	 * Returns the {@code FontStore} instance. Creates one if one does not exist.
	 * @return Instance of FontStore
	 */
	public static FontStore get() {
		if (fontStore == null) {
			fontStore = new FontStore();
		}
		
		return fontStore;
	}
}
