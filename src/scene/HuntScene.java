/**
 * 
 */
package scene;


import model.Party;
import model.Person;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import component.Button;
import component.CountingButton;
import component.ItemListener;
import component.Label;
import component.Modal;
import component.Label.Alignment;
import component.OwnerInventoryButtons;
import component.Panel;
import component.Positionable;
import component.Positionable.ReferencePoint;
import component.sprite.Sprite;

import core.ConstantStore;
import core.FontManager;
import core.GameDirector;
import core.Logger;

/**
 * @author 
 *
 */
public class HuntScene extends Scene {
	public static final SceneID ID = SceneID.HUNT;

	//the member of the party engaged in the hunt
	private Person hunter;
	
	private Image ground;
	private Sprite toonHunter;
	
	
	/**
	 * Constructs a {@code HuntScene} with a {@code Person} who will be the hunter
	 * @param hunter the single member of the party who is going to hunt.
	 */
	public HuntScene(Person hunter){
		this.hunter = hunter;
		
		
	}
	
	public HuntScene(Party party){
		//grap a person from the party to be the hunter
		this.hunter = party.getPartyMembers().get(0);
	}
	
	
	/**
	 * Initializes the state for the {@code HuntScene} resources
	 * 
	 */
	
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);

		Font h2 = GameDirector.sharedSceneListener().getFontManager().getFont(FontManager.FontID.H2);
		
		Label infoLabel = new Label(container, h2, Color.white, "Just making the hunt scene");
		
		mainLayer.add(infoLabel, infoLabel.getPosition(Positionable.ReferencePoint.BOTTOMCENTER), Positionable.ReferencePoint.TOPCENTER, 0, 5);

		
		Image leftImage = new Image("resources/marioLeft.png");
		Image rightImage = new Image("resources/marioRight.png");
		toonHunter = new Sprite(container, leftImage, rightImage);
		toonHunter.setScale(0.1f);
		
		mainLayer.add(toonHunter, mainLayer.getPosition(Positionable.ReferencePoint.CENTERCENTER), Positionable.ReferencePoint.BOTTOMCENTER, 0, -5);
		
		backgroundLayer.add(new Panel(container, new Image("resources/dark_dirt.png")));

		
	}
	
	
	
	/* (non-Javadoc)
	 * @see scene.Scene#update(org.newdawn.slick.GameContainer, org.newdawn.slick.state.StateBasedGame, int)
	 */
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
	}

	@Override
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_ESCAPE) {
			GameDirector.sharedSceneListener().sceneDidEnd(this);
		}
		if (key == Input.KEY_D){//pressed the right key
			
		}
		if (key == Input.KEY_W){//pressed the forward key
			
		}
		if (key == Input.KEY_A){//pressed the left key
			
		}
		if (key == Input.KEY_S){//pressed the back key
			
		}
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		if ( mainLayer.isVisible() && mainLayer.isAcceptingInput()) {
			

		}
	}
	
	
	/* (non-Javadoc)
	 * @see scene.Scene#getID()
	 */
	public int getID() {
		return ID.ordinal();
	}
	
	

}
