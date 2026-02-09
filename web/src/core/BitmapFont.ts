interface CharInfo {
  x: number;
  y: number;
  width: number;
  height: number;
  xoffset: number;
  yoffset: number;
  xadvance: number;
}

export class BitmapFont {
  private chars: Map<number, CharInfo> = new Map();
  private lineHeight: number = 0;
  private texture: HTMLImageElement | null = null;

  async load(fntPath: string, texturePath: string): Promise<void> {
    const fntResponse = await fetch(fntPath);
    const fntText = await fntResponse.text();
    this.parse(fntText);

    return new Promise((resolve, reject) => {
      const img = new Image();
      img.onload = () => {
        this.texture = img;
        resolve();
      };
      img.onerror = reject;
      img.src = texturePath;
    });
  }

  private parse(fntText: string): void {
    const lines = fntText.split('\n');

    for (const line of lines) {
      const trimmed = line.trim();

      if (trimmed.startsWith('common ')) {
        const match = trimmed.match(/lineHeight=(\d+)/);
        if (match) {
          this.lineHeight = parseInt(match[1], 10);
        }
      } else if (trimmed.startsWith('char id=')) {
        const id = this.extractValue(trimmed, 'id');
        const x = this.extractValue(trimmed, 'x');
        const y = this.extractValue(trimmed, 'y');
        const width = this.extractValue(trimmed, 'width');
        const height = this.extractValue(trimmed, 'height');
        const xoffset = this.extractValue(trimmed, 'xoffset');
        const yoffset = this.extractValue(trimmed, 'yoffset');
        const xadvance = this.extractValue(trimmed, 'xadvance');

        if (id !== null && x !== null && y !== null && width !== null &&
            height !== null && xoffset !== null && yoffset !== null && xadvance !== null) {
          this.chars.set(id, { x, y, width, height, xoffset, yoffset, xadvance });
        }
      }
    }
  }

  private extractValue(line: string, key: string): number | null {
    const regex = new RegExp(`${key}=(-?\\d+)`);
    const match = line.match(regex);
    return match ? parseInt(match[1], 10) : null;
  }

  drawText(ctx: CanvasRenderingContext2D, text: string, x: number, y: number): void {
    if (!this.texture) return;

    let currentX = x;
    ctx.imageSmoothingEnabled = false;

    for (let i = 0; i < text.length; i++) {
      const charCode = text.charCodeAt(i);
      const charInfo = this.chars.get(charCode);

      if (charInfo) {
        if (charInfo.width > 0 && charInfo.height > 0) {
          ctx.drawImage(
            this.texture,
            charInfo.x, charInfo.y, charInfo.width, charInfo.height,
            Math.floor(currentX + charInfo.xoffset), Math.floor(y + charInfo.yoffset), charInfo.width, charInfo.height
          );
        }
        currentX += charInfo.xadvance;
      }
    }
  }

  getWidth(text: string): number {
    let width = 0;
    for (let i = 0; i < text.length; i++) {
      const charCode = text.charCodeAt(i);
      const charInfo = this.chars.get(charCode);
      if (charInfo) {
        width += charInfo.xadvance;
      }
    }
    return width;
  }

  getLineHeight(): number {
    return this.lineHeight;
  }
}
