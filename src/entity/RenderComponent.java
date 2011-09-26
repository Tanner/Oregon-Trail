package entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public abstract class RenderComponent extends Component {
	public RenderComponent(String id) {
		this.id = id;
	}
	
	public abstract float getWidth();
	public abstract float getHeight();

	public abstract void render(GameContainer container, StateBasedGame game, Graphics g);
}
