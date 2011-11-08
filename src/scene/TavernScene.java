package scene;

import model.Party;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import component.Button;
import component.Label;
import component.Positionable;
import component.SegmentedControl;
import component.Positionable.ReferencePoint;
import core.FontStore;

public class TavernScene extends Scene {
	
	public static final SceneID ID = SceneID.RIVER;
	
	private final int MAX_PARTY_SIZE = 4;
	
	private SegmentedControl hireBoard;
	private Button hireButton;
	private Party party;
	
	public TavernScene(Party party) {
		this.party = party;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		Font fieldFont = FontStore.get().getFont(FontStore.FontID.FIELD);
		int maxNewMembers = MAX_PARTY_SIZE - party.getPartyMembers().size();
		System.out.println(maxNewMembers);
		String[] names = {"Billy Joe Bob", "John Henry Jingle", "Washington George", "New Partymember", "Place Holder Jr."};
		hireBoard = new SegmentedControl(container, 300, 400, 5, 1, 10, false, maxNewMembers, names);
		hireButton = new Button(container, 280, 40, new Label(container, 300, fieldFont, Color.white, "Hire!"));
		mainLayer.add(hireBoard, mainLayer.getPosition(ReferencePoint.CENTERCENTER), Positionable.ReferencePoint.CENTERCENTER);
		mainLayer.add(hireButton, hireBoard.getPosition(ReferencePoint.BOTTOMCENTER), Positionable.ReferencePoint.TOPCENTER);
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}

}
