package scene;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import component.Label;
import component.Panel;
import component.Positionable;
import core.FontManager;
import core.GameDirector;

public class GameOverScene extends Scene {
	public static final SceneID ID = SceneID.HUNT;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		Font h1 = GameDirector.sharedSceneListener().getFontManager().getFont(FontManager.FontID.H1);		
		Label titleLabel = new Label(container, h1, Color.red, "Game Over");
		mainLayer.add(titleLabel, mainLayer.getPosition(Positionable.ReferencePoint.CENTERCENTER), Positionable.ReferencePoint.CENTERCENTER);
		
		backgroundLayer.add(new Panel(container, Color.black));
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
}