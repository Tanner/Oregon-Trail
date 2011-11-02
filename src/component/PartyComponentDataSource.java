package component;

import model.Conditioned;

public interface PartyComponentDataSource extends Conditioned {
	String getName();
	boolean isDead();
}
