package scene;

import model.Party;
import model.RandomEncounterTable;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import component.Positionable.ReferencePoint;
import component.sprite.ParallaxSprite;

public class TrailScene extends Scene {
	public static final SceneID ID = SceneID.TRAIL;
	
	private ParallaxSprite ground;
	private ParallaxSprite trees;
	
	private Party party;
	private RandomEncounterTable randomEncounterTable;
	
	public TrailScene(Party party, RandomEncounterTable randomEncounterTable) {
		this.party = party;
		this.randomEncounterTable = randomEncounterTable;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);		
		ground = new ParallaxSprite(container, container.getWidth(), new Image("resources/graphics/ground/trail.png", false, Image.FILTER_NEAREST), 300);
		trees = new ParallaxSprite(container, container.getWidth(), new Image("resources/graphics/test/trees.png"), 900);
		
		backgroundLayer.add(ground, backgroundLayer.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT);
		backgroundLayer.add(trees, ground.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.BOTTOMLEFT);
	}
		
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		ground.move(delta);
		trees.move(delta);
	}
	
	@Override
	public int getID() {
		return ID.ordinal();
	}
}
