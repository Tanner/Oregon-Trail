export class BitmapFont {
    constructor() {
        this.chars = new Map();
        this.lineHeight = 0;
        this.texture = null;
    }
    async load(fntPath, texturePath) {
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
    parse(fntText) {
        const lines = fntText.split('\n');
        for (const line of lines) {
            const trimmed = line.trim();
            if (trimmed.startsWith('common ')) {
                const match = trimmed.match(/lineHeight=(\d+)/);
                if (match) {
                    this.lineHeight = parseInt(match[1], 10);
                }
            }
            else if (trimmed.startsWith('char id=')) {
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
    extractValue(line, key) {
        const regex = new RegExp(`${key}=(-?\\d+)`);
        const match = line.match(regex);
        return match ? parseInt(match[1], 10) : null;
    }
    drawText(ctx, text, x, y) {
        if (!this.texture)
            return;
        let currentX = x;
        ctx.imageSmoothingEnabled = false;
        for (let i = 0; i < text.length; i++) {
            const charCode = text.charCodeAt(i);
            const charInfo = this.chars.get(charCode);
            if (charInfo) {
                if (charInfo.width > 0 && charInfo.height > 0) {
                    ctx.drawImage(this.texture, charInfo.x, charInfo.y, charInfo.width, charInfo.height, currentX + charInfo.xoffset, y + charInfo.yoffset, charInfo.width, charInfo.height);
                }
                currentX += charInfo.xadvance;
            }
        }
    }
    getWidth(text) {
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
    getLineHeight() {
        return this.lineHeight;
    }
}
//# sourceMappingURL=BitmapFont.js.map