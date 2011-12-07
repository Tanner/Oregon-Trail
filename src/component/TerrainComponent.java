package component;

import model.huntingMap.TerrainObject;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import core.ConstantStore;
import core.ImageStore;

public class TerrainComponent extends Component {
	private Image dispImage;
	private Image collideImage;
	private TerrainObject sourceObject;
	private String imageStoreName;
	private String imageStoreShadowName;
	private Panel dispPanel;
	private Panel collidePanel;

	public TerrainComponent(GUIContext context, TerrainObject sourceObject) throws SlickException{
		super(context, sourceObject.getImageX(), sourceObject.getImageY());
		this.sourceObject = sourceObject;
		//getting correct image from image store for this component
		dispImage = ImageStore.get().IMAGES.get(this.sourceObject.getImageStoreName());
		collideImage = ImageStore.get().IMAGES.get(this.sourceObject.getImageStoreShadName());
		
		Panel dispPanel = new Panel (context,dispImage);
		Panel collidePanel = new Panel (context,collideImage);
		
		add(dispPanel, getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
		
	}

	
	
	
	
}
