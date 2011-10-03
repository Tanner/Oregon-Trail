package scene;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import component.Background;
import component.Button;
import component.Label;
import component.Positionable;
import core.FontManager;
import core.GameDirector;

public class PartyCreationScene extends Scene {
	public final static SceneID ID = SceneID.PartyCreation;
	private final static int PADDING = 20;
	
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		UnicodeFont fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);
		int buttonCount = 4;
		int buttonWidth = (container.getWidth() - PADDING * (buttonCount + 1)) / buttonCount;
		int buttonheight = 150;

		Label firstPersonLabel = new Label(container, fieldFont, Color.white, "New Player");
		Button firstPersonButton = new Button(container, firstPersonLabel, buttonWidth, 150);
		mainLayer.add(firstPersonButton);
		
		Label secondPersonLabel = new Label(container, fieldFont, Color.white, "New Player");
		Button secondPersonButton = new Button(container, secondPersonLabel, buttonWidth, 150);
		mainLayer.add(secondPersonButton, firstPersonButton.getPosition(Positionable.ReferencePoint.TopRight), Positionable.ReferencePoint.TopLeft, PADDING, 0);
		
		Label thirdPersonLabel = new Label(container, fieldFont, Color.white, "New Player");
		Button thirdPersonButton = new Button(container, thirdPersonLabel, buttonWidth, 150);
		mainLayer.add(thirdPersonButton, secondPersonButton.getPosition(Positionable.ReferencePoint.TopRight), Positionable.ReferencePoint.TopLeft, PADDING, 0);
		
		Label fourthPersonLabel = new Label(container, fieldFont, Color.white, "New Player");
		Button fourthPersonButton = new Button(container, fourthPersonLabel, buttonWidth, 150);
		mainLayer.add(fourthPersonButton, thirdPersonButton.getPosition(Positionable.ReferencePoint.TopRight), Positionable.ReferencePoint.TopLeft, PADDING, 0);
				
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
