package scene;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import core.*;
import entity.*;


public class TownScene extends Scene {
	public static final SceneID ID = SceneID.TownScene;
	
	private Entity mario;
	private Entity luigi;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		mario = new Entity("Mario", container);
		mario.setRenderComponent(new FlippableImageRenderComponent("MarioRender", "resources/mario.png"));
		mario.addComponent(new LeftRightMovement("MarioMovement"));
		mario.setPosition(new Vector2f(0, container.getHeight() - mario.getHeight()));
		
		luigi = new Entity("Luigi", container);
		luigi.setRenderComponent(new FlippableImageRenderComponent("LuigiRender", "resources/luigi.png"));
		luigi.addComponent(new LeftRightMovement("LuigiMovement"));
		luigi.setPosition(new Vector2f(200, container.getHeight() - luigi.getHeight()));
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.setColor(new Color(0x003e84));
		g.fillRect(0, 0, container.getWidth(), container.getHeight());
		
		UnicodeFont h1Font = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.H1);
		UnicodeFont h2Font = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.H2);

		String title = "Town Scene";
		h1Font.drawString((container.getWidth() - h1Font.getWidth(title)) / 2,
				container.getHeight()/2 - h1Font.getAscent(),
				title,
				Color.white);
		
		String instruction = "Press Enter to Enter Store";
		h2Font.drawString((container.getWidth() - h2Font.getWidth(instruction)) / 2,
				container.getHeight()/2 + h1Font.getAscent(),
				instruction,
				Color.white);
		
		mario.render(container, game, g);
		luigi.render(container, game, g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		mario.update(container, game, delta);
		luigi.update(container, game, delta);
	}
	
	public void keyReleased(int key, char c) {
		if(key == Input.KEY_ENTER) {
			GameDirector.sharedSceneDelegate().requestScene(SceneID.StoreScene);
		}
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}

}
