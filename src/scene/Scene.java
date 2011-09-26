package scene;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public abstract class Scene extends BasicGameState {
	public static SceneID ID = SceneID.Default;
	
	public abstract void init(GameContainer container, StateBasedGame game) throws SlickException;

	public abstract void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException;

	public abstract void update(GameContainer container, StateBasedGame game, int delta) throws SlickException;
	
	@Override
	public int getID() {
		return ID.ordinal();
	}
}
