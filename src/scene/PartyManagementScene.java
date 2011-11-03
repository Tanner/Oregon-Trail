package scene;

import java.util.ArrayList;
import java.util.List;

import model.Condition;
import model.Party;
import model.Party.Pace;
import model.Party.Rations;
import model.Person;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import component.Button;
import component.ConditionBar;
import component.Label;
import component.Panel;
import component.Positionable;
import component.SegmentedControl;
import component.Positionable.ReferencePoint;

import core.ConstantStore;
import core.FontStore;
import core.GameDirector;

/**
 * Scene allows the user to modify they're party.
 */
public class PartyManagementScene extends Scene {
	public static final SceneID ID = SceneID.PARTYMANAGEMENTSCENE;

	private static final int PADDING = 20;
	private static final int FUNCTION_BUTTON_PADDING = 10;
	private static final int CONDITION_BAR_PADDING = 5;
	
	private static final int FUNCTION_BUTTON_WIDTH = 70;
	private static final int FUNCTION_BUTTON_HEIGHT = 30;
	
	private static final int BUTTON_WIDTH = 200;
	private static final int BUTTON_HEIGHT = 30;
	private static final int LABEL_WIDTH = 100;
	
	private static final int AVATAR_SIZE = 60;
	private static final int CONDITION_HEIGHT = 5;
	
	private Button leaveButton;
	private SegmentedControl rationsSegmentedControl, paceSegmentedControl;
	
	private Party party;
	
	/**
	 * Constructs a new Party Management Scene.
	 * @param party Party for the scene
	 */
	public PartyManagementScene(Party party) {
		this.party = party;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		Font fieldFont = FontStore.get(FontStore.FontID.FIELD);
		
		SceneComponentListener componentListener = new SceneComponentListener();
		
		// Leave Button
		leaveButton = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, ConstantStore.get("PARTY_CREATION_SCENE", "PARTY_CONFIRM")));
		leaveButton.addListener(componentListener);
		mainLayer.add(leaveButton, mainLayer.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT, PADDING, -PADDING);
		
		// Ration Selection
		int numOfRations = Party.Rations.values().length;
		String[] rationLabels = new String[numOfRations];
		for (int i = 0; i < numOfRations; i++) {
			rationLabels[i] = Party.Rations.values()[i].toString();
		}
		rationsSegmentedControl = new SegmentedControl(container, BUTTON_WIDTH * 2 + PADDING, BUTTON_HEIGHT, 1, rationLabels.length, 0, true, 1, rationLabels);
		rationsSegmentedControl.setSelection(new int[]{party.getRations().ordinal()});
		mainLayer.add(rationsSegmentedControl, mainLayer.getPosition(ReferencePoint.BOTTOMRIGHT), ReferencePoint.BOTTOMRIGHT, -PADDING, -PADDING);
		
		Label rationsLabel = new Label(container, LABEL_WIDTH, BUTTON_HEIGHT, fieldFont, Color.white, ConstantStore.get("PARTY_CREATION_SCENE", "RATIONS_LABEL"));
		mainLayer.add(rationsLabel, rationsSegmentedControl.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMRIGHT, -PADDING, 0);
		
		// Pace Selection
		int numOfPaces = Party.Pace.values().length;
		String[] paceLabels = new String[numOfRations];
		for (int i = 0; i < numOfPaces; i++) {
			paceLabels[i] = Party.Pace.values()[i].toString();
		}
		paceSegmentedControl = new SegmentedControl(container, BUTTON_WIDTH * 2 + PADDING, BUTTON_HEIGHT, 1, paceLabels.length, 0, true, 1, paceLabels);
		paceSegmentedControl.setSelection(new int[]{party.getPace().ordinal()});
		mainLayer.add(paceSegmentedControl, rationsSegmentedControl.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.BOTTOMLEFT, 0, -PADDING);
		
		Label paceLabel = new Label(container, LABEL_WIDTH, BUTTON_HEIGHT, fieldFont, Color.white, ConstantStore.get("PARTY_CREATION_SCENE", "PACE_LABEL"));
		mainLayer.add(paceLabel, rationsLabel.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.BOTTOMLEFT, 0, -PADDING);
		
		List<Person> members = party.getPartyMembers();
		Positionable reference = mainLayer;
		ReferencePoint referencePoint = ReferencePoint.TOPRIGHT;
		int xPadding = PADDING;
		for (int i = 0; i < members.size(); i++) {
			Person person = members.get(i);
			
			int height = AVATAR_SIZE + CONDITION_HEIGHT + CONDITION_BAR_PADDING;
			
			Panel panel = new Panel(container, 300, height);
			
			Button avatar = new Button(container, AVATAR_SIZE, AVATAR_SIZE, new Label(container, fieldFont, Color.white, person.getName()));
			panel.add(avatar, panel.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
			
			ConditionBar conditionBar = new ConditionBar(container, AVATAR_SIZE, CONDITION_HEIGHT, person.getHealth());
			panel.add(conditionBar, avatar.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.TOPLEFT, 0, CONDITION_BAR_PADDING);
			
			Button killButton = new Button(container, FUNCTION_BUTTON_WIDTH, FUNCTION_BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, "Kill"));
			panel.add(killButton, avatar.getPosition(ReferencePoint.TOPRIGHT), ReferencePoint.TOPLEFT, FUNCTION_BUTTON_PADDING, 0);
			
			Button feedButton = new Button(container, FUNCTION_BUTTON_WIDTH, FUNCTION_BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, "Feed"));
			panel.add(feedButton, conditionBar.getPosition(ReferencePoint.BOTTOMRIGHT), ReferencePoint.BOTTOMLEFT, FUNCTION_BUTTON_PADDING, 0);
			
			Label profession = new Label(container, fieldFont, Color.white, person.getProfession().toString());
			panel.add(profession, killButton.getPosition(ReferencePoint.TOPRIGHT), ReferencePoint.TOPLEFT, FUNCTION_BUTTON_PADDING, 0);
			
			mainLayer.add(panel, reference.getPosition(referencePoint), ReferencePoint.TOPRIGHT, -xPadding, PADDING);
			
			reference = panel;
			referencePoint = ReferencePoint.BOTTOMRIGHT;
			xPadding = 0;
		}
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		return;
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
	
	private class SceneComponentListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent component) {
			if (component == leaveButton) {
				Pace pace = Pace.values()[paceSegmentedControl.getSelection()[0]];
				Rations rations = Rations.values()[rationsSegmentedControl.getSelection()[0]];
				party.setPace(pace);
				party.setRations(rations);
				
				GameDirector.sharedSceneListener().sceneDidEnd(PartyManagementScene.this);
			}
		}
	}
}
