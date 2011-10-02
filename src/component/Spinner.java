package component;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.*;

public class Spinner extends Component {
	
	private final int PADDING = 20;
	private final int MAX_STATE;
	private String[] fields;
	private Label label;
	private int state;
	private int x, y;
	private Font font;
	
	private MouseOverArea upButton, downButton;
	private ButtonListener listener;
	
	public Spinner(GUIContext context, int x, int y, Font font, Color c, Image up, Image down, String ... fields) {
		super(context);
		state = 0;
		MAX_STATE = fields.length - 1;
		this.x = x;
		this.y = y;
		this.fields = fields;
		this.font = font;
		listener = new ButtonListener();
		upButton = new MouseOverArea(context, up, x, y, listener);
		downButton = new MouseOverArea(context, down, x, y + upButton.getHeight(), listener);
		int textY = (upButton.getHeight()*2-font.getLineHeight()) / 2;
		label = new Label(context, x + upButton.getWidth() + 5, y + textY, font, c, fields[0]);
		
	}
	
	public int getState() {
		return state;
	}

	@Override
	public int getHeight() {
		return upButton.getHeight() * 2;
	}

	@Override
	public int getWidth() {
		return upButton.getWidth() + PADDING + font.getWidth(fields[state]);
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public void render(GUIContext context, Graphics g) throws SlickException {
		label.setText(fields[state]);
		label.render(context, g);
		upButton.render(context, g);
		downButton.render(context,g);
	}

	@Override
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	private class ButtonListener implements ComponentListener {
		public void componentActivated(AbstractComponent source) {
			if (source == upButton) {
				state = (state == MAX_STATE) ? state : state + 1;
			}
			else {
				state = (state == 0) ? 0 : state - 1;
			}
		}
	}
}
