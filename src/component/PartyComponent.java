package component;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import component.sprite.AnimatingSprite;
import component.sprite.Sprite;

import core.FontStore;
import core.FontStore.FontID;

public class PartyComponent extends Component {
	private final static int MARGIN = 20;
	private final static int CONDITION_BAR_WIDTH = 80;
	private final static int CONDITION_BAR_HEIGHT = 20;
	
	private List<PartyComponentDataSource> dataSources;
	private List<ConditionBar> conditionBars;
	private List<AnimatingSprite> sprites;
	
	public PartyComponent(GUIContext context, int width, int height, List<PartyComponentDataSource> dataSources) {
		super(context, width, height);
		
		this.dataSources = dataSources;
		
		conditionBars = new ArrayList<ConditionBar>();
		sprites = new ArrayList<AnimatingSprite>();
		
		for (int i = 0; i < dataSources.size(); i++) {
			ConditionBar cb = new ConditionBar(context,
					CONDITION_BAR_WIDTH,
					CONDITION_BAR_HEIGHT,
					dataSources.get(i).getCondition(),
					FontStore.get(FontID.FIELD));
			conditionBars.add(cb);
			
			AnimatingSprite sprite = null;
			try {
				sprite = new AnimatingSprite(context,
						new Animation(new Image[] {new Image("resources/graphics/test/mario.png", false, Image.FILTER_NEAREST),
								new Image("resources/graphics/test/luigi.png", false, Image.FILTER_NEAREST)}, 1),
								AnimatingSprite.Direction.RIGHT);
			} catch (SlickException e) {
				e.printStackTrace();
			}
			sprites.add(sprite);
		}
		
		ConditionBar[] conditionBarArr = new ConditionBar[conditionBars.size()];
		for (int i = 0; i < conditionBarArr.length; i++) {
			conditionBarArr[i] = conditionBars.get(i);
		}
		addAsRow(conditionBarArr,
				getPosition(ReferencePoint.TOPLEFT),
				0,
				0,
				MARGIN);
		
		AnimatingSprite[] spriteArr = new AnimatingSprite[sprites.size()];
		for (int i = 0; i < spriteArr.length; i++) {
			spriteArr[i] = sprites.get(i);
		}
		addAsRow(spriteArr,
				conditionBars.get(0).getPosition(ReferencePoint.BOTTOMLEFT),
				0,
				MARGIN,
				MARGIN);
	}
	
	public void update(int delta, int timeElapsed) {
		for (int i = dataSources.size() - 1; i >= 0; i--) {
			conditionBars.get(i).update();
			sprites.get(i).update(delta);
			
			if (dataSources.get(i).isDead()) {
				remove(conditionBars.get(i));
				conditionBars.remove(i);
				dataSources.remove(i);
			} else {
				int translateY = (int)(2 * Math.sin(timeElapsed / 250 - (i + 1)));
				conditionBars.get(i).setTranslation(0, translateY);
				sprites.get(i).setTranslation(0, translateY);
			}
		}
	}
}
