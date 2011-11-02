package scene;

import java.util.Random;

import model.Party;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import component.Panel;
import component.ParallaxPanel;
import component.Positionable.ReferencePoint;
import component.sprite.ParallaxSprite;
import component.sprite.ParallaxSpriteLoop;
import component.sprite.Sprite;

public class RiverScene extends Scene {
	public static final SceneID ID = SceneID.RIVER;
	
	private static final int NUM_CLOUDS = 5;
	private static final int CLOUD_OFFSET = 20;
	private static final int CLOUD_DISTANCE_VARIANCE = 10;
	private static final int CLOUD_OFFSET_VARIANCE = 10;
	private static final int CLOUD_DISTANCE = 300;
	
	private Party party;
	private ParallaxPanel riverParallaxPanel;
	private ParallaxPanel cloudParallaxPanel;
	
	public RiverScene(Party party) {
		this.party = party;
	}
	
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		Random random = new Random();
		riverParallaxPanel = new ParallaxPanel(container, container.getWidth(), container.getHeight());
		cloudParallaxPanel = new ParallaxPanel(container, container.getWidth(), container.getHeight());
		ParallaxSprite.MAX_DISTANCE = 600;
		ParallaxSprite water = new ParallaxSpriteLoop(container, container.getWidth() + 1, new Image("resources/graphics/ground/water.png", false, Image.FILTER_LINEAR),1);
		riverParallaxPanel.add(water, backgroundLayer.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT);

		// Clouds
		Image[] cloudImages = new Image[3];
		cloudImages[0] = new Image("resources/graphics/backgrounds/cloud_a.png", false, Image.FILTER_NEAREST);
		cloudImages[1] = new Image("resources/graphics/backgrounds/cloud_b.png", false, Image.FILTER_NEAREST);
		cloudImages[2] = new Image("resources/graphics/backgrounds/cloud_c.png", false, Image.FILTER_NEAREST);
		
		for (int i = 0; i < NUM_CLOUDS; i++) {
			int distance = CLOUD_DISTANCE + random.nextInt(CLOUD_DISTANCE_VARIANCE * 2) - CLOUD_DISTANCE_VARIANCE;
			int cloudImage = random.nextInt(cloudImages.length);
			
			int offset = CLOUD_OFFSET + random.nextInt(CLOUD_OFFSET_VARIANCE * 2) - CLOUD_OFFSET_VARIANCE;
			
			ParallaxSprite cloud = new ParallaxSprite(container, cloudImages[cloudImage], distance, true);
			cloudParallaxPanel.add(cloud, backgroundLayer.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT, 0, offset);
		}
		Panel backgroundColor = new Panel(container, new Color(0x579cdd));
		backgroundLayer.add(backgroundColor, backgroundLayer.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT);
		backgroundLayer.add(riverParallaxPanel, backgroundLayer.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT, 0, 200);
		Sprite ground = new Sprite(container, container.getWidth() + 1, new Image("resources/graphics/ground/riveredge.png", false, Image.FILTER_NEAREST));
		backgroundLayer.add(ground, backgroundLayer.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT);
		ground = new Sprite(container, container.getWidth() + 1, new Image("resources/graphics/ground/riveredgetop.png", false, Image.FILTER_NEAREST));
		backgroundLayer.add(ground, backgroundLayer.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
		backgroundLayer.add(cloudParallaxPanel, backgroundLayer.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT);

		

	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		for (ParallaxSprite sprite : riverParallaxPanel.getSprites()) {
			sprite.move(delta);
		}
		for (ParallaxSprite sprite : cloudParallaxPanel.getSprites()) {
			sprite.move(delta);
		}

	}

	@Override
	public int getID() {
		return ID.ordinal();
	}

}
