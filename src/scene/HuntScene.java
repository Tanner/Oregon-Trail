package scene;

import model.Party;
import model.Person;

//import org.newdawn.slick.Animation;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import component.Label;
import component.Panel;
import component.Positionable;
import component.sprite.AnimatingSprite;

import core.FontStore;
import core.GameDirector;

/**
 * Hunt scene is where you kill animals and get food.
 */
public class HuntScene extends Scene {
	public static final SceneID ID = SceneID.HUNT;

	//the member of the party engaged in the hunt
	//private Person hunter;
	
	//private Image ground;
	private AnimatingSprite toonHunter;
	
	/**
	 * Constructs a {@code HuntScene} with a {@code Person} who will be the hunter
	 * @param hunter the single member of the party who is going to hunt.
	 */
	public HuntScene(Person hunter){
		//this.hunter = hunter;
		
		
	}
	
	public HuntScene(Party party){
		//grap a person from the party to be the hunter
		//this.hunter = party.getPartyMembers().get(0);
	}
	
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);

		Font h2 = FontStore.get().getFont(FontStore.FontID.H2);
		
		Label infoLabel = new Label(container, h2, Color.white, "Just making the hunt scene");
		mainLayer.add(infoLabel, mainLayer.getPosition(Positionable.ReferencePoint.CENTERCENTER), Positionable.ReferencePoint.TOPCENTER, 0, 5);
		
		//Image leftImage = new Image("resources/graphics/test/marioLeft.png");
		//Image rightImage = new Image("resources/graphics/test/marioRight.png");
		//toonHunter = new AnimatingSprite(container, new Animation(new Image[]{leftImage}, 1), new Animation(new Image[]{rightImage}, 1));
//		toonHunter.setScale(0.1f);
		
		mainLayer.add(toonHunter, mainLayer.getPosition(Positionable.ReferencePoint.CENTERCENTER), Positionable.ReferencePoint.CENTERCENTER, 0, -5);
		
		backgroundLayer.add(new Panel(container, new Image("resources/graphics/backgrounds/dark_dirt.png")));
	}
	
	
	
	/* (non-Javadoc)
	 * @see scene.Scene#update(org.newdawn.slick.GameContainer, org.newdawn.slick.state.StateBasedGame, int)
	 */
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		toonHunter.update(container, delta);
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
	
	
	@Override
	public int getID() {
		return ID.ordinal();
	}
	
	

}
