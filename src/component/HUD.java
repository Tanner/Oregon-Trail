package component;

import model.datasource.HUDDataSource;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

import component.Label.Alignment;
import component.Label.VerticalAlignment;

import core.ConstantStore;
import core.FontManager;
import core.GameDirector;

/**
 * A HUD holds quick information for the player.
 */
public class HUD extends Component {
	private static final int MARGIN = 10;
	private static final int HEIGHT = 70;
	
	private static final int INFO_WIDTH = 200;
	
	private Button menuButton;
	private Label dateLabel;
	private Label moneyLabel;
	private Label notificationLabel;
	
	private HUDDataSource data;
	
	/**
	 * Constructs a HUD with a {@code GUIContext} and {@code HUDDataSource}.
	 * @param context Context
	 * @param data Data source to use
	 */
	public HUD(GUIContext context, HUDDataSource data, ComponentListener listener) {
		super(context, context.getWidth(), HEIGHT);
		
		this.data = data;
		
		Font fieldFont = GameDirector.sharedSceneListener().getFontManager().getFont(FontManager.FontID.FIELD);
		
		int height = HEIGHT - (2 * MARGIN);
		
		Label menuLabel = new Label(context, fieldFont, Color.white, "Menu");
		menuButton = new Button(context, menuLabel.getWidth() + (2 * MARGIN), height, menuLabel);
		menuButton.addListener(listener);
		add(menuButton, getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT, MARGIN, MARGIN);
		
		dateLabel = new Label(context, INFO_WIDTH, fieldFont, Color.white, "");
		add(dateLabel, getPosition(ReferencePoint.TOPRIGHT), ReferencePoint.TOPRIGHT, -MARGIN, MARGIN);
		
		moneyLabel = new Label(context, INFO_WIDTH, fieldFont, Color.white, "");
		add(moneyLabel, getPosition(ReferencePoint.BOTTOMRIGHT), ReferencePoint.BOTTOMRIGHT, -MARGIN, -MARGIN);
		
		int notificationWidth = context.getWidth() - menuButton.getWidth() - INFO_WIDTH -  MARGIN * 4;
		
		notificationLabel = new Label(context, notificationWidth, height, fieldFont, Color.white, "Oh hai");
		notificationLabel.setVerticalAlignment(VerticalAlignment.CENTER);
		notificationLabel.setBackgroundColor(Color.black);
		notificationLabel.setBorderColor(Color.lightGray);
		notificationLabel.setBorderWidth(2);
		add(notificationLabel, menuButton.getPosition(ReferencePoint.TOPRIGHT), ReferencePoint.TOPLEFT, MARGIN, 0);
		
		setBackgroundColor(Color.gray);
		setBevelWidth(2);
		setBevel(Component.BevelType.OUT);
		
		updatePartyInformation();
	}
	
	public void updatePartyInformation() {
		setMoney(data.getMoney());
	}
	
	public void setDate(String date) {
		dateLabel.setText(date);
	}
	
	public void setMoney(int money) {
		moneyLabel.setText(ConstantStore.get("GENERAL", "MONEY_SYMBOL") + money);
	}
	
	public void setNotification(String message) {
		notificationLabel.setText(message);
	}
}
