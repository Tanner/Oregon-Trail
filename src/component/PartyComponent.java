package component;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.gui.GUIContext;

import core.FontStore;
import core.FontStore.FontID;

public class PartyComponent extends Component {
	private final static int MARGIN = 20;
	private final static int CONDITION_BAR_HEIGHT = 20;
	
	private PartyComponentDataSource dataSource;
	private List<ConditionBar> conditionBars;
	
	public PartyComponent(GUIContext context, int width, int height, PartyComponentDataSource dataSource) {
		super(context, width, height);
		
		this.dataSource = dataSource;
		
		conditionBars = new ArrayList<ConditionBar>();
		int memberCount = dataSource.getMemberCount();
		for (int i = 0; i < memberCount; i++) {
			ConditionBar cb = new ConditionBar(context,
					(getWidth() - MARGIN * memberCount) / memberCount,
					CONDITION_BAR_HEIGHT,
					dataSource.getHealthCondition(i),
					FontStore.get(FontID.FIELD));
			conditionBars.add(cb);
		}
		
		ConditionBar[] conditionBarArr = new ConditionBar[conditionBars.size()];
		for (int i = 0; i < conditionBarArr.length; i++) {
			conditionBarArr[i] = conditionBars.get(i);
		}
		
		addAsGrid(conditionBarArr,
				getPosition(ReferencePoint.BOTTOMLEFT),
				1,
				memberCount,
				0,
				-MARGIN - CONDITION_BAR_HEIGHT,
				MARGIN,
				0);
	}
	
	public void update(int delta) {
		for (ConditionBar cb : conditionBars) {
			cb.update();
		}
	}
}
