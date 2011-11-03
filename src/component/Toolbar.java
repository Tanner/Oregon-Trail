package component;

import org.newdawn.slick.Color;
import org.newdawn.slick.gui.GUIContext;

public class Toolbar extends Component {
	public Toolbar(GUIContext context, int width, int height) {
		super(context, width, height);
		
		setBackgroundColor(Color.darkGray);
		
		setBevel(BevelType.IN);
		setBevelWidth(2);
		setBottomBorderWidth(2);
		setBorderColor(Color.black);
	}
}
