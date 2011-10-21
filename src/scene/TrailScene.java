package scene;

import model.Party;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import component.sprite.Sprite;

public class TrailScene extends Scene {
	
	public static final SceneID ID = SceneID.TRAIL;
	
	private final int DELTA_X = 4;
	Sprite backdrop1, backdrop2;
	Sprite trees1, trees2;
	private Party party;
	private int backgroundCounter, treesCounter;
	public TrailScene(Party party) {
		this.party = party;
		backgroundCounter = 0;
		treesCounter = 0;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		//Image backdropImage = new Image("resources/trailBackground.png");
		backdrop1 = new Sprite(container, new Image("resources/trailBackground.png"));
		backdrop2 = new Sprite(container, new Image("resources/trailBackground.png"));
		trees1 = new Sprite(container, new Image("resources/trees.png"));
		trees2 = new Sprite(container, new Image("resources/trees.png"));
		backdrop2.setLocation(-container.getWidth(),0);
		trees2.setLocation(-container.getWidth(), 0);
		backgroundLayer.add(backdrop1);
		backgroundLayer.add(backdrop2);
		backgroundLayer.add(trees1);
		backgroundLayer.add(trees2);
	}
		
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		backgroundCounter += delta;
		treesCounter += delta;
		if ( backgroundCounter > 300) {
			backdrop1.setLocation(backdrop1.getX() + DELTA_X, backdrop1.getY());
			backdrop2.setLocation(backdrop2.getX() + DELTA_X, backdrop2.getY());
			backgroundCounter = 0;
		}
		if ( backdrop1.getX() > container.getWidth())
			backdrop1.setLocation(backdrop2.getX() - container.getWidth(), 0);
		if ( backdrop2.getX() > container.getWidth())
			backdrop2.setLocation(backdrop1.getX() - container.getWidth(), 0);
		
		if ( treesCounter > 100) {
			trees1.setLocation(trees1.getX() + DELTA_X, trees1.getY());
			trees2.setLocation(trees2.getX() + DELTA_X, trees2.getY());
			treesCounter = 0;
		}
		if ( trees1.getX() > container.getWidth())
			trees1.setLocation(trees2.getX() - container.getWidth(), 0);
		if ( trees2.getX() > container.getWidth())
			trees2.setLocation(trees1.getX() - container.getWidth(), 0);
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
}
