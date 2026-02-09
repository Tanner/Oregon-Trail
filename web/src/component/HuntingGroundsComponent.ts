import { Component } from './Component';
import { ImageStore } from '../core/ImageStore';

export enum TerrainType {
  GRASS = 'GRASS',
  SNOW = 'SNOW',
  MOUNTAIN = 'MOUNTAIN',
  DESERT = 'DESERT'
}

/**
 * Large scrollable hunting grounds with tile-based terrain.
 */
export class HuntingGroundsComponent extends Component {
  private static readonly MAP_SIZE = 2400;
  private terrainType: TerrainType;
  private backgroundImage: HTMLImageElement;
  private viewportX: number = 0;
  private viewportY: number = 0;

  constructor(terrainType: TerrainType = TerrainType.GRASS) {
    super(HuntingGroundsComponent.MAP_SIZE, HuntingGroundsComponent.MAP_SIZE);
    this.terrainType = terrainType;
    this.backgroundImage = this.getBackgroundImage();
  }

  private getBackgroundImage(): HTMLImageElement {
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

  setViewport(x: number, y: number): void {
    this.viewportX = Math.max(0, Math.min(x, HuntingGroundsComponent.MAP_SIZE - 800));
    this.viewportY = Math.max(0, Math.min(y, HuntingGroundsComponent.MAP_SIZE - 600));
  }

  getViewportX(): number {
    return this.viewportX;
  }

  getViewportY(): number {
    return this.viewportY;
  }

  override render(ctx: CanvasRenderingContext2D): void {
    if (!this.isVisible()) return;

    ctx.save();
    ctx.imageSmoothingEnabled = false;

    const tilesX = Math.ceil(800 / this.backgroundImage.width) + 1;
    const tilesY = Math.ceil(600 / this.backgroundImage.height) + 1;

    const offsetX = -(this.viewportX % this.backgroundImage.width);
    const offsetY = -(this.viewportY % this.backgroundImage.height);

    for (let y = 0; y < tilesY; y++) {
      for (let x = 0; x < tilesX; x++) {
        ctx.drawImage(
          this.backgroundImage,
          offsetX + x * this.backgroundImage.width,
          offsetY + y * this.backgroundImage.height
        );
      }
    }

    ctx.restore();
    super.render(ctx);
  }

  worldToScreen(worldX: number, worldY: number): { x: number; y: number } {
    return {
      x: worldX - this.viewportX,
      y: worldY - this.viewportY
    };
  }

  screenToWorld(screenX: number, screenY: number): { x: number; y: number } {
    return {
      x: screenX + this.viewportX,
      y: screenY + this.viewportY
    };
  }
}
