package scene;

import model.Party;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Scene allows the user to modify they're party.
 */
public class PartyManagementScene extends Scene {
	public static final SceneID ID = SceneID.PARTYMANAGEMENTSCENE;

	private Party party;
	
	public PartyManagementScene(Party party) {
		this.party = party;
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
}
