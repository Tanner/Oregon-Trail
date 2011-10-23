package scene;

import model.Party;
import model.RandomEncounterTable;

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
	Sprite clouds1, clouds2;
	Sprite wagon;
	private Party party;
	private RandomEncounterTable randomEncounterTable;
	private int backgroundCounter, treesCounter, cloudsCounter;
	public TrailScene(Party party, RandomEncounterTable randomEncounterTable) {
		this.party = party;
		this.randomEncounterTable = randomEncounterTable;
		backgroundCounter = 0;
		treesCounter = 0;
		cloudsCounter = 0;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		wagon = new Sprite(container, new Image("resources/wagon.png"));
		wagon.setLocation(450, 400);
		backdrop1 = new Sprite(container, new Image("resources/trail.png"));
		backdrop2 = new Sprite(container, new Image("resources/trail.png"));
		trees1 = new Sprite(container, new Image("resources/trees.png"));
		trees2 = new Sprite(container, new Image("resources/trees.png"));
		clouds1 = new Sprite(container, new Image("resources/clouds.png"));
		clouds2 = new Sprite(container, new Image("resources/clouds.png"));
		backdrop2.setLocation(-container.getWidth(),0);
		trees1.setLocation(0,142);
		trees2.setLocation(-container.getWidth(), 142);
		clouds2.setLocation(-container.getWidth(), 0);
		backgroundLayer.add(backdrop1);
		backgroundLayer.add(backdrop2);
		backgroundLayer.add(trees1);
		backgroundLayer.add(trees2);
		backgroundLayer.add(clouds1);
		backgroundLayer.add(clouds2);
		backgroundLayer.add(wagon);
	}
		
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		backgroundCounter = moveSprite(backdrop1, backdrop2, backgroundCounter + delta, 300);
		treesCounter = moveSprite(trees1, trees2, treesCounter + delta, 100);
		cloudsCounter = moveSprite(clouds1, clouds2, cloudsCounter + delta, 500);
	}
	
	private int moveSprite(Sprite image1, Sprite image2, int currentCount, int maxCount) {
		if ( currentCount > maxCount) {
			image1.setLocation(image1.getX() + DELTA_X, image1.getY());
			image2.setLocation(image2.getX() + DELTA_X, image2.getY());
			currentCount = 0;
		}
		if ( image1.getX() > container.getWidth())
			image1.setLocation(image2.getX() - container.getWidth(), image1.getY());
		if ( image2.getX() > container.getWidth())
			image2.setLocation(image1.getX() - container.getWidth(), image2.getY());
		return currentCount;
	}
	
	@Override
	public int getID() {
		return ID.ordinal();
	}
}
