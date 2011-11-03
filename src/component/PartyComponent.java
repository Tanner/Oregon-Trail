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

		int groupWidth = MARGIN;

		AnimatingSprite[] spriteArray = new AnimatingSprite[sprites.size()];
		for (int i = 0; i < spriteArray.length; i++) {
			spriteArray[i] = sprites.get(i);
			groupWidth += spriteArray[i].getWidth();
			groupWidth += MARGIN;
		}
		Label[] nameArrayay = new Label[names.size()];
		for (int i = 0; i < nameArrayay.length; i++) {
			nameArrayay[i] = names.get(i);
		}
		ConditionBar[] conditionBarArray = new ConditionBar[conditionBars.size()];
		for (int i = 0; i < conditionBarArray.length; i++) {
			conditionBarArray[i] = conditionBars.get(i);
		}
		
		addAsRow(spriteArray,
				new Vector2f(getPosition(ReferencePoint.BOTTOMRIGHT).x - groupWidth, getPosition(ReferencePoint.BOTTOMRIGHT).y),
				0,
				MARGIN,
				MARGIN);
		
		addAsRow(conditionBarArray,
				new Vector2f(sprites.get(0).getPosition(ReferencePoint.TOPLEFT).x, sprites.get(0).getPosition(ReferencePoint.TOPLEFT).y - CONDITION_BAR_HEIGHT),
				0,
				-MARGIN,
				MARGIN);
		
		addAsRow(nameArrayay,
				new Vector2f(conditionBars.get(0).getPosition(ReferencePoint.TOPLEFT).x, conditionBars.get(0).getPosition(ReferencePoint.TOPLEFT).y - names.get(0).getHeight()),
				0,
				-MARGIN,
				MARGIN);
	}
	
	public void update(int delta, int timeElapsed) {
		for (int i = dataSources.size() - 1; i >= 0; i--) {
			conditionBars.get(i).update();
			sprites.get(i).update(delta);
			
			if (dataSources.get(i).isDead()) {
				remove(names.get(i));
				remove(conditionBars.get(i));
				remove(sprites.get(i));
				names.remove(i);
				conditionBars.remove(i);
				sprites.remove(i);
				dataSources.remove(i);
			} else {
				int translateY = (int)(2 * Math.sin(timeElapsed / 10000 - (i + 1)));
				names.get(i).setTranslation(0, translateY);
				conditionBars.get(i).setTranslation(0, translateY);
				sprites.get(i).setTranslation(0, translateY);
			}
		}
	}
}
