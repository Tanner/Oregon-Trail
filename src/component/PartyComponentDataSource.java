package component;

import model.Condition;

public interface PartyComponentDataSource {
	public Condition getHealthCondition(int person);
	public int getMemberCount();
}
