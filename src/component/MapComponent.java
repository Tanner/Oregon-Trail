package component;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import model.WorldMap;
import model.worldMap.*;
import core.ConstantStore;

/**
 * This component will hold the cities and the trails for the map 
 *
 */
public class MapComponent extends Component {
	private WorldMap worldMap;
	private Boolean devMode;
	
	public MapComponent(GUIContext context, int width, int height){
		super(context, width, height);
		
	}
	
	public MapComponent(GUIContext context, WorldMap worldMap, Boolean devMode){
		super(context, context.getWidth(), context.getHeight());
		this.worldMap = worldMap;
		this.devMode = devMode;
		MapPanel mapPanel = new MapPanel(context, context.getWidth(),context.getHeight(), ConstantStore.COLORS.get("TRANSLUCENT_OVERLAY"));
		add(mapPanel, getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
		
	}
	
	/**
	 * this will draw a trail from a startX,startY to endX,endY
	 * 
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @param node the Node this trail originated in
	 * @param trail the trail we are painting
	 */
	private void drawIndividualTrail(GUIContext context, Graphics g, double startX, double startY, double endX, double endY, LocationNode node, TrailEdge trail){
		g.setLineWidth(3);
		Color startColor;
		Color endColor;
		double colorMult = (node.getRank()/(1.0 * worldMap.getMaxRank()));
		if (trail.isTaken()) {
			startColor = new Color(0, 255, 255);
			endColor = new Color(0,255,255);				
		} else {
			startColor = new Color((int) (100 * colorMult), (int)(50 * colorMult), (int)(30 * colorMult));
			endColor = new Color((int) (255 * colorMult), (int)(128 * colorMult), (int)(64 * colorMult));			
		}
		g.drawGradientLine((float)startX + 5, (float)startY + 5, startColor, (float) endX + 5, (float) endY + 5, endColor);
	}

	@Override
	public void render(GUIContext context, Graphics g) throws SlickException {
		super.render(context, g);
		for (int rank = 0; rank <= worldMap.MAX_RANK; rank++){
			for(LocationNode node : worldMap.getMapNodes().get(rank)) {
				for (int nodeTrail = 0; nodeTrail < node.getTrails(); nodeTrail++){
					if ((node.getOutBoundTrailByIndex(nodeTrail).isVisible()) || this.devMode)  {
					drawIndividualTrail(context, g, node.getOutBoundTrailByIndex(nodeTrail).getOrigin().getPlayerMapX(),
						node.getOutBoundTrailByIndex(nodeTrail).getOrigin().getPlayerMapY(),
						node.getOutBoundTrailByIndex(nodeTrail).getDestination().getPlayerMapX(),
						node.getOutBoundTrailByIndex(nodeTrail).getDestination().getPlayerMapY(), node, node.getOutBoundTrailByIndex(nodeTrail));
					}//if visible draw it
				}//iTrails for
			}//for location node in node lists
		}//for i <= maxrank
	}

	private class MapPanel extends Panel {
		
		/**
		 * Constructs a new MapPanel with a {@code GUIContext}, width, height, and {@code Color}.
		 * @param context Container
		 * @param width Width
		 * @param height Height
		 * @param color Background color
		 */
		public MapPanel(GUIContext context, int width, int height, Color color) {
			super(context, width, height, color);
		}

	}

}
