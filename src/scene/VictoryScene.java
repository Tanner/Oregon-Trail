package scene;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import component.Label;
import component.Panel;
import component.Positionable;
import core.FontStore;

/**
 * A scene displayed upon reaching the final destination along the trail.
 */
public class VictoryScene extends Scene {
	public static final SceneID ID = SceneID.VICTORY;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		Font h1 = FontStore.get().getFont(FontStore.FontID.H1);
		Label titleLabel = new Label(container, h1, Color.green, "Victory!");
		mainLayer.add(titleLabel, mainLayer.getPosition(Positionable.ReferencePoint.CENTERCENTER), Positionable.ReferencePoint.CENTERCENTER);
		
		backgroundLayer.add(new Panel(container, Color.black));
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		return;
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
}

