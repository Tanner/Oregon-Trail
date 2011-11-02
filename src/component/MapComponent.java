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
	
	public MapComponent(GUIContext context, int width, int height){
		super(context, width, height);
		
	}
	
	public MapComponent(GUIContext context, WorldMap worldMap){
		super(context, context.getWidth(), context.getHeight());
		this.worldMap = worldMap;
		MapPanel mapPanel = new MapPanel(context, context.getWidth(),context.getHeight(), ConstantStore.COLORS.get("TRANSLUCENT_OVERLAY"));
		add(mapPanel, getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
				
		
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

		/**
		 * this will draw a trail from a startX,startY to endX,endY
		 * 
		 * @param startX
		 * @param startY
		 * @param endX
		 * @param endY
		 */
		private void drawIndividualTrail(GUIContext context, Graphics g, double startX, double startY, double endX, double endY, LocationNode node){
			g.setLineWidth(3);
			double colorMult = (node.getRank()/(1.0 * worldMap.getMaxRank()));
			Color startColor = new Color((int) (100 * colorMult), (int)(50 * colorMult), (int)(30 * colorMult));
			Color endColor = new Color((int) (255 * colorMult), (int)(128 * colorMult), (int)(64 * colorMult));
			g.drawGradientLine((float)startX + 5, (float)startY + 5, startColor, (float) endX + 5, (float) endY + 5, endColor);
		}

		@Override
		public void render(GUIContext context, Graphics g) throws SlickException {
			
			for (int j = 0; j <= worldMap.MAX_RANK; j++){
				for(LocationNode node : worldMap.getMapNodes().get(j)) {
					for (int i = 0; i < node.getTrails(); i++){
						drawIndividualTrail(context, g, node.getOutBoundTrailByIndex(i).getOrigin().getPlayerMapX(),
							node.getOutBoundTrailByIndex(i).getOrigin().getPlayerMapY(),
							node.getOutBoundTrailByIndex(i).getDestination().getPlayerMapX(),
							node.getOutBoundTrailByIndex(i).getDestination().getPlayerMapY(), node);
					}//iTrails for
				}//for location node in node lists
			}//for i <= maxrank
		}
	}

}
