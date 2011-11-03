package component;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

import component.sprite.AnimatingSprite;

import core.FontStore;
import core.FontStore.FontID;

public class PartyComponent extends Component {
	private final static int MARGIN = 10;
	private final static int CONDITION_BAR_HEIGHT = 20;
	
	private List<PartyComponentDataSource> dataSources;
	
	private List<Label> names;
	private List<ConditionBar> conditionBars;
	private List<AnimatingSprite> sprites;
	
	public PartyComponent(GUIContext context, int width, int height, List<PartyComponentDataSource> dataSources) {
		super(context, width, height);
		
		this.dataSources = dataSources;
		
		names = new ArrayList<Label>();
		conditionBars = new ArrayList<ConditionBar>();
		sprites = new ArrayList<AnimatingSprite>();
		
		for (int i = 0; i < dataSources.size(); i++) {			
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
			
			int componentWidth = sprite.getWidth();
			
			Label name = new Label(context, componentWidth, FontStore.get(FontID.FIELD), Color.white, dataSources.get(i).getName());
			names.add(name);
			
			ConditionBar cb = new ConditionBar(context,
					componentWidth,
					CONDITION_BAR_HEIGHT,
					dataSources.get(i).getCondition(),
					FontStore.get(FontID.FIELD));
			conditionBars.add(cb);
		}

		Label[] nameArrayay = new Label[names.size()];
		int groupWidth = 0;
		for (int i = 0; i < nameArrayay.length; i++) {
			nameArrayay[i] = names.get(i);
			groupWidth += nameArrayay[i].getWidth();
			groupWidth += MARGIN;
		}
		addAsRow(nameArrayay,
				new Vector2f(getPosition(ReferencePoint.TOPRIGHT).x - groupWidth, getPosition(ReferencePoint.TOPRIGHT).y),
				0,
				0,
				MARGIN);
		
		ConditionBar[] conditionBarArray = new ConditionBar[conditionBars.size()];
		for (int i = 0; i < conditionBarArray.length; i++) {
			conditionBarArray[i] = conditionBars.get(i);
		}
		addAsRow(conditionBarArray,
				names.get(0).getPosition(ReferencePoint.BOTTOMLEFT),
				0,
				MARGIN,
				MARGIN);
		
		AnimatingSprite[] spriteArray = new AnimatingSprite[sprites.size()];
		for (int i = 0; i < spriteArray.length; i++) {
			spriteArray[i] = sprites.get(i);
		}
		addAsRow(spriteArray,
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
