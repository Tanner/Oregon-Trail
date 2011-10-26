package scene;

import model.Party;
import model.Party.Pace;
import model.Party.Rations;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import component.Button;
import component.Label;
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
	private static final int BUTTON_WIDTH = 200;
	private static final int BUTTON_HEIGHT = 30;
	private static final int LABEL_WIDTH = 100;
	
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
