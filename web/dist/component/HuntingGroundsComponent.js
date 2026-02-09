import { Component } from './Component';
import { ImageStore } from '../core/ImageStore';
export var TerrainType;
(function (TerrainType) {
    TerrainType["GRASS"] = "GRASS";
    TerrainType["SNOW"] = "SNOW";
    TerrainType["MOUNTAIN"] = "MOUNTAIN";
    TerrainType["DESERT"] = "DESERT";
})(TerrainType || (TerrainType = {}));
/**
 * Large scrollable hunting grounds with tile-based terrain.
 */
export class HuntingGroundsComponent extends Component {
    constructor(terrainType = TerrainType.GRASS) {
        super(HuntingGroundsComponent.MAP_SIZE, HuntingGroundsComponent.MAP_SIZE);
        this.viewportX = 0;
        this.viewportY = 0;
        this.terrainType = terrainType;
        this.backgroundImage = this.getBackgroundImage();
    }
    getBackgroundImage() {
        switch (this.terrainType) {
            case TerrainType.GRASS:
                return ImageStore.getImage('HUNT_GRASS');
            case TerrainType.SNOW:
                return ImageStore.getImage('HUNT_SNOW');
            case TerrainType.MOUNTAIN:
                return ImageStore.getImage('HUNT_MOUNTAIN');
            case TerrainType.DESERT:
                return ImageStore.getImage('HUNT_DESERT');
        }
    }
    setViewport(x, y) {
        this.viewportX = Math.max(0, Math.min(x, HuntingGroundsComponent.MAP_SIZE - 800));
        this.viewportY = Math.max(0, Math.min(y, HuntingGroundsComponent.MAP_SIZE - 600));
    }
    getViewportX() {
        return this.viewportX;
    }
    getViewportY() {
        return this.viewportY;
    }
    render(ctx) {
        if (!this.isVisible())
            return;
        ctx.save();
        ctx.imageSmoothingEnabled = false;
        const tilesX = Math.ceil(800 / this.backgroundImage.width) + 1;
        const tilesY = Math.ceil(600 / this.backgroundImage.height) + 1;
        const offsetX = -(this.viewportX % this.backgroundImage.width);
        const offsetY = -(this.viewportY % this.backgroundImage.height);
        for (let y = 0; y < tilesY; y++) {
            for (let x = 0; x < tilesX; x++) {
                ctx.drawImage(this.backgroundImage, offsetX + x * this.backgroundImage.width, offsetY + y * this.backgroundImage.height);
            }
        }
        ctx.restore();
        super.render(ctx);
    }
    worldToScreen(worldX, worldY) {
        return {
            x: worldX - this.viewportX,
            y: worldY - this.viewportY
        };
    }
    screenToWorld(screenX, screenY) {
        return {
            x: screenX + this.viewportX,
            y: screenY + this.viewportY
        };
    }
}
HuntingGroundsComponent.MAP_SIZE = 2400;
//# sourceMappingURL=HuntingGroundsComponent.js.map