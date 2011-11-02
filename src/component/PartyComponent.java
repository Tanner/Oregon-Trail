package component;

import java.util.ArrayList;
import java.util.List;

import model.Conditioned;

import org.newdawn.slick.gui.GUIContext;

import core.FontStore;
import core.FontStore.FontID;

public class PartyComponent extends Component {
	private final static int MARGIN = 20;
	private final static int CONDITION_BAR_WIDTH = 80;
	private final static int CONDITION_BAR_HEIGHT = 20;
	
	private List<PartyComponentDataSource> dataSources;
	private List<ConditionBar> conditionBars;
	
	public PartyComponent(GUIContext context, int width, int height, List<PartyComponentDataSource> dataSources) {
		super(context, width, height);
		
		this.dataSources = dataSources;
		
		conditionBars = new ArrayList<ConditionBar>();
				
		for (int i = 0; i < dataSources.size(); i++) {
			ConditionBar cb = new ConditionBar(context,
					CONDITION_BAR_WIDTH,
					CONDITION_BAR_HEIGHT,
					dataSources.get(i).getCondition(),
					FontStore.get(FontID.FIELD));
			conditionBars.add(cb);
		}
		
		ConditionBar[] conditionBarArr = new ConditionBar[conditionBars.size()];
		for (int i = 0; i < conditionBarArr.length; i++) {
			conditionBarArr[i] = conditionBars.get(i);
		}
		
		addAsGrid(conditionBarArr,
				getPosition(ReferencePoint.TOPLEFT),
				1,
				conditionBarArr.length,
				0,
				0,
				MARGIN,
				0);
	}
	
	public void update(int delta, int timeElapsed) {
		for (int i = dataSources.size() - 1; i >= 0; i--) {
			conditionBars.get(i).update();
			
			if (dataSources.get(i).isDead()) {
				remove(conditionBars.get(i));
				conditionBars.remove(i);
				dataSources.remove(i);
			}
			
			conditionBars.get(i).setTranslation(0, (int)(2 * Math.sin(timeElapsed / 100 - (i + 1))));
		}
	}
}
