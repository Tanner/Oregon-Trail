import { BitmapFont } from './BitmapFont';

export enum FontID {
  H1 = "H1",
  H2 = "H2",
  FIELD = "FIELD",
}

class FontStoreClass {
  private fonts: Map<FontID, BitmapFont> = new Map();
  private initialized: boolean = false;

  async initialize(): Promise<void> {
    if (this.initialized) return;

    const fontConfigs = [
      { id: FontID.H1, fnt: '/fonts/04b03_h1.fnt', png: '/fonts/04b03_h1.png' },
      { id: FontID.H2, fnt: '/fonts/04b03_h2.fnt', png: '/fonts/04b03_h2.png' },
      { id: FontID.FIELD, fnt: '/fonts/04b03_field.fnt', png: '/fonts/04b03_field.png' },
    ];

    await Promise.all(
      fontConfigs.map(async (config) => {
        const font = new BitmapFont();
        await font.load(config.fnt, config.png);
        this.fonts.set(config.id, font);
      })
    );

    this.initialized = true;
  }

  getFont(id: FontID): BitmapFont {
    const font = this.fonts.get(id);
    if (!font) {
      throw new Error(`Font ${id} not loaded`);
    }
    return font;
  }
}

export const FontStore = new FontStoreClass();
