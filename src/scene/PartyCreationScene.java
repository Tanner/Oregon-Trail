package scene;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.StateBasedGame;

import component.Background;
import component.Label;
import component.Positionable;
import core.FontManager;
import core.GameDirector;

public class PartyCreationScene extends Scene {
	public final static SceneID ID = SceneID.PartyCreation;
	
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		UnicodeFont h1 = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.H1);
		
		Label titleLabel = new Label(container, h1, Color.white, "Party Creation Scene");
		
		mainLayer.add(titleLabel, mainLayer.getPosition(Positionable.ReferencePoint.CenterCenter), Positionable.ReferencePoint.CenterCenter);
		
		backgroundLayer.add(new Background(container, new Color(0xa00008)));
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
}
