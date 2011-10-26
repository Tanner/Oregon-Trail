package scene;

import java.io.File;
import java.io.IOException;

import model.Party;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.particles.*;
import org.newdawn.slick.particles.effects.FireEmitter;
import org.newdawn.slick.state.StateBasedGame;

import component.*;
import component.Positionable.ReferencePoint;
import component.sprite.Sprite;
import core.FontManager;
import core.GameDirector;

public class CampScene extends Scene {
	
	public static final SceneID ID = SceneID.CAMP;
	
	private static final int PADDING = 20;
	private static final int BUTTON_WIDTH = 315;
	private static final int BUTTON_HEIGHT = 30;
	private static final int LABEL_WIDTH = 100;
	
	private ConfigurableEmitter fire;
	private HUD hud;
	private Panel buttonPanel;
	//1: inventoryButton
	//2: managementButton
	//3: mapButton
	//4: huntButton
	//5: somethingButton
	//6: continueButton
	private Button[] bottomButtons;
	private Party party;
	
	private ParticleSystem system;
	/** The particle blending mode */
	private int mode = ParticleSystem.BLEND_COMBINE;
	
	
	public CampScene(Party party) {
		this.party = party;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		backgroundLayer.add(new Sprite(container, new Image("resources/graphics/backgrounds/camp.png")));
		Font fieldFont = GameDirector.sharedSceneListener().getFontManager().getFont(FontManager.FontID.FIELD);
		buttonPanel = new Panel(container, container.getWidth(), 120, Color.gray);
		buttonPanel.setBevelWidth(2);
		buttonPanel.setBevel(Component.BevelType.OUT);
		buttonPanel.setTopBorderWidth(2);
		buttonPanel.setBorderColor(Color.black);
		bottomButtons = new Button[6];
		
		Image image = new Image("resources/graphics/test/particle.tga", true);
		system = new ParticleSystem(image);
		FireEmitter fire = new FireEmitter(570,400,10);
		system.addEmitter(fire);
		
		Label tempLabel = new Label(container, BUTTON_WIDTH, fieldFont, Color.white, "Inventory");
		bottomButtons[0] = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, tempLabel);
		tempLabel = new Label(container, BUTTON_WIDTH, fieldFont, Color.white, "Party Management");
		bottomButtons[1] = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, tempLabel);
		tempLabel = new Label(container, BUTTON_WIDTH, fieldFont, Color.white, "Map");
		bottomButtons[2] = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, tempLabel);
		tempLabel = new Label(container, BUTTON_WIDTH, fieldFont, Color.white, "Hunt");
		bottomButtons[3] = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, tempLabel);
		tempLabel = new Label(container, BUTTON_WIDTH, fieldFont, Color.white, "Something");
		bottomButtons[4] = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, tempLabel);
		tempLabel = new Label(container, BUTTON_WIDTH, fieldFont, Color.white, "Continue");
		bottomButtons[5] = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, tempLabel);
		buttonPanel.addAsGrid(bottomButtons, buttonPanel.getPosition(ReferencePoint.TOPLEFT), 2, 3, PADDING, PADDING, PADDING, PADDING);
		mainLayer.add(buttonPanel, mainLayer.getPosition(ReferencePoint.BOTTOMLEFT), Positionable.ReferencePoint.BOTTOMLEFT);
		hud = new HUD(container, party, new HUDListener());
		showHUD(hud);
	}
	
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		system.update(delta);
		
	}

//	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
//		super.render(container, game, g);
//		for (int i=0;i<5;i++) {
//			g.translate(1,1);
//			system.render();
//		}
//		g.resetTransform();
//	}
	
	@Override
	public int getID() {
		return ID.ordinal();
	}
	
	private class HUDListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent component) {
			GameDirector.sharedSceneListener().requestScene(SceneID.PARTYINVENTORY, CampScene.this, false);
		}
	}

}
