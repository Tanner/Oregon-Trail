package scene.test;

import java.util.*;
import model.*;
import model.Party.Pace;
import model.Party.Rations;
import model.item.*;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.gui.*;
import org.newdawn.slick.state.*;
import component.*;
import component.Positionable.ReferencePoint;
import core.*;
import scene.*;


public class TrailTestScene extends Scene {
	public static final SceneID ID = SceneID.TrailTestScene;
	private static final int PADDING = 10;
	private static final int regularButtonHeight = 30;
	private static final int buttonWidth = 200;
	
	private Party party;
	private Vehicle vehicle;
	
	private Label vehicleInventory;
	private Label[] partyNames, partyInventory;
	private ConditionBar[] partyHealth;
	private ArrayList<Person> persons;

	private SegmentedControl rationsSegmentedControl;
	private SegmentedControl paceSegmentedControl;
	private Button updateButton, stepButton, cancelButton, partyInventoryButton;
	
	public TrailTestScene(Party party) {
		this.party = party;
		this.vehicle = party.getVehicle();
		persons = party.getPartyMembers();
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		Font fieldFont = GameDirector.sharedSceneListener().getFontManager().getFont(FontManager.FontID.FIELD);

		partyNames = new Label[persons.size()];
		partyHealth = new ConditionBar[persons.size()];
		partyInventory = new Label[persons.size()];
		for (int i = 0; i < persons.size(); i++) {
			partyNames[i] = new Label(container, fieldFont, Color.white, persons.get(i).getName());
			partyHealth[i] = new ConditionBar(container, 200, fieldFont.getLineHeight(), persons.get(i).getHealth(),fieldFont);
			partyInventory[i] = new Label(container, fieldFont, Color.white, persons.get(i).getInventory().toString());
		}
		Component[][] layout = new Component[persons.size()][3];
		for ( int i = 0; i < persons.size(); i++ ) {
			layout[i][0] = partyNames[i];
			layout[i][1] = partyHealth[i];
			layout[i][2] = partyInventory[i];
			mainLayer.addAsColumn(layout[i], new Vector2f(0,0), PADDING,PADDING+ i*100, PADDING);
		}
		
		//Stolen Segmented Control from Party Creation Scene

		// Ration Selection
		int labelWidth = 100;
		Label rationsLabel = new Label(container, labelWidth, regularButtonHeight, fieldFont, Color.white, ConstantStore.get("PARTY_CREATION_SCENE", "RATIONS_LABEL"));
		mainLayer.add(rationsLabel, mainLayer.getPosition(ReferencePoint.BottomLeft), ReferencePoint.BottomLeft, PADDING, -PADDING);
		
		int numOfRations = Party.Rations.values().length;
		String[] rationLabels = new String[numOfRations];
		for (int i = 0; i < numOfRations; i++) {
			rationLabels[i] = Party.Rations.values()[i].toString();
		}
		rationsSegmentedControl = new SegmentedControl(container, buttonWidth * 2 + PADDING, regularButtonHeight, 1, rationLabels.length, 0, true, 1, rationLabels);
		mainLayer.add(rationsSegmentedControl, rationsLabel.getPosition(ReferencePoint.TopRight), ReferencePoint.TopLeft, PADDING, 0);
		
		// Pace Selection
		Label paceLabel = new Label(container, labelWidth, regularButtonHeight, fieldFont, Color.white, ConstantStore.get("PARTY_CREATION_SCENE", "PACE_LABEL"));
		mainLayer.add(paceLabel, rationsLabel.getPosition(ReferencePoint.TopLeft), ReferencePoint.BottomLeft, 0, -PADDING);
		
		int numOfPaces = Party.Pace.values().length;
		String[] paceLabels = new String[numOfRations];
		for (int i = 0; i < numOfPaces; i++) {
			paceLabels[i] = Party.Pace.values()[i].toString();
		}
		paceSegmentedControl = new SegmentedControl(container, buttonWidth * 2 + PADDING, regularButtonHeight, 1, paceLabels.length, 0, true, 1, paceLabels);
		mainLayer.add(paceSegmentedControl, rationsSegmentedControl.getPosition(ReferencePoint.TopLeft), ReferencePoint.BottomLeft, 0, -PADDING);	
		
		if (vehicle != null) {
			vehicleInventory = new Label(container, 900, 90, fieldFont, Color.white, vehicle.getInventory().toString());
			mainLayer.add(vehicleInventory, paceSegmentedControl.getPosition(ReferencePoint.TopLeft), Positionable.ReferencePoint.BottomLeft,-110,-5);
		}
		//Buttons
		Label tempLabel = new Label(container, fieldFont, Color.white, "Update P/R");
		updateButton = new Button(container,  buttonWidth, regularButtonHeight, tempLabel);
		updateButton.addListener(new ButtonListener());
		tempLabel = new Label(container, fieldFont, Color.white, "Step");
		stepButton = new Button(container, buttonWidth, regularButtonHeight, tempLabel);
		stepButton.addListener(new ButtonListener());
		tempLabel = new Label(container, fieldFont, Color.white, "Cancel");
		cancelButton = new Button(container, buttonWidth, regularButtonHeight, tempLabel);
		cancelButton.addListener(new ButtonListener());
		tempLabel = new Label(container, fieldFont, Color.white, "Party Inventory");
		partyInventoryButton = new Button(container, buttonWidth, regularButtonHeight, tempLabel);
		partyInventoryButton.addListener(new ButtonListener());
		mainLayer.add(stepButton, mainLayer.getPosition(ReferencePoint.BottomRight), Positionable.ReferencePoint.BottomRight, -PADDING,-PADDING);	
		mainLayer.add(updateButton, stepButton.getPosition(ReferencePoint.TopRight), Positionable.ReferencePoint.BottomRight, 0, -PADDING);
		mainLayer.add(cancelButton, stepButton.getPosition(ReferencePoint.TopLeft), Positionable.ReferencePoint.TopRight, -PADDING, 0);
		mainLayer.add(partyInventoryButton, cancelButton.getPosition(ReferencePoint.TopLeft), Positionable.ReferencePoint.BottomLeft, 0, -PADDING);
		
		backgroundLayer.add(new Panel(container, new Color(0x0C5DA5)));
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
	}
	
	private class ButtonListener implements ComponentListener {
		public void componentActivated(AbstractComponent source) {
			if (source == stepButton) {
				//@TODO George, enter your "step" method call here.
				party.walk();
				updateLabels();
			} else if ( source == updateButton ){
				Pace pace = Pace.values()[paceSegmentedControl.getSelection()[0]];
				Rations rations = Rations.values()[rationsSegmentedControl.getSelection()[0]];
				party.setPace(pace);
				party.setRations(rations);
			}
			else if ( source == partyInventoryButton ) 
				GameDirector.sharedSceneListener().requestScene(SceneID.PartyInventory, TrailTestScene.this);
			else {
				GameDirector.sharedSceneListener().sceneDidEnd(TrailTestScene.this);
			}
		}
	}
	
	@Override
	public int getID() {
		return ID.ordinal();
	}

	public void updateLabels() {
		for (int i = 0; i < persons.size(); i++) {
			partyNames[i].setText(persons.get(i).getName());
			partyHealth[i].setCondition(persons.get(i).getHealth());
			partyInventory[i].setText(persons.get(i).getInventory().toString());
		}
		if (vehicle != null) {
			vehicleInventory.setText(vehicle.getInventory().toString());
		}
	}
}
