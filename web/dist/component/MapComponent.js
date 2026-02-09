import { Component } from './Component';
import { Color } from '../core/Color';
/**
 * Component that renders the trail map with gradient lines connecting locations.
 * Draws trails between location nodes with colors based on rank and whether taken.
 */
export class MapComponent extends Component {
    constructor(width, height, worldMap, devMode = false) {
        super(width, height);
        this.worldMap = worldMap;
        this.devMode = devMode;
    }
    render(ctx) {
        if (!this.isVisible()) {
            return;
        }
        ctx.save();
        for (let rank = 0; rank <= this.worldMap.getMaxRank(); rank++) {
            const nodes = this.worldMap.getMapNodes().get(rank) || [];
            for (const node of nodes) {
                const outboundTrails = node.getOutboundTrails();
                for (const trail of outboundTrails) {
                    if (trail.isVisible() || this.devMode) {
                        this.drawIndividualTrail(ctx, trail.getOrigin().getPlayerMapX(), trail.getOrigin().getPlayerMapY(), trail.getDestination().getPlayerMapX(), trail.getDestination().getPlayerMapY(), node, trail);
                    }
                }
            }
        }
        ctx.restore();
        super.render(ctx);
    }
    drawIndividualTrail(ctx, startX, startY, endX, endY, node, trail) {
        ctx.lineWidth = 3;
        const colorMult = node.getRank() / this.worldMap.getMaxRank();
        let startColor;
        let endColor;
        if (trail.isTaken()) {
            startColor = new Color(0, 255, 255);
            endColor = new Color(0, 255, 255);
        }
        else {
            startColor = new Color(Math.floor(100 * colorMult), Math.floor(50 * colorMult), Math.floor(30 * colorMult));
            endColor = new Color(Math.floor(255 * colorMult), Math.floor(128 * colorMult), Math.floor(64 * colorMult));
        }
        const gradient = ctx.createLinearGradient(startX + 5, startY + 5, endX + 5, endY + 5);
        gradient.addColorStop(0, startColor.toCSS());
        gradient.addColorStop(1, endColor.toCSS());
        ctx.strokeStyle = gradient;
        ctx.beginPath();
        ctx.moveTo(startX + 5, startY + 5);
        ctx.lineTo(endX + 5, endY + 5);
        ctx.stroke();
    }
}
//# sourceMappingURL=MapComponent.js.map