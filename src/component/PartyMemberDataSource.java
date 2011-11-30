package component;

import model.Conditioned;

public interface PartyMemberDataSource extends Conditioned {
	String getName();
	boolean isDead();
	boolean isMale();
}
