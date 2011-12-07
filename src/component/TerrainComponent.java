package component;

import model.huntingMap.TerrainObject;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import core.ImageStore;
/**
 * this component is intended to hold a terrain sprite for display in the hunt scene.
 * 
 */
public class TerrainComponent extends Component {
	private Image dispImage;
	private Image collideImage;
	private TerrainObject sourceObject;
	private String imageStoreName;
	private String imageStoreShadowName;
	private Panel dispPanel;
	private Panel collidePanel;

	public TerrainComponent(GUIContext context, TerrainObject sourceObject){
		super(context, sourceObject.getImageX(), sourceObject.getImageY());
		this.sourceObject = sourceObject;
		//getting correct image from image store for this component
		dispImage = ImageStore.get().IMAGES.get(this.sourceObject.getImageStoreName());
		collideImage = ImageStore.get().IMAGES.get(this.sourceObject.getImageStoreShadName());
		
		this.dispPanel = new Panel (context,dispImage);
		this.collidePanel = new Panel (context,collideImage);
		
		add(this.dispPanel, getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
		
	}

	
	
	
	
}
