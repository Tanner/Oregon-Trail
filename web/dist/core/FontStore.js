import { BitmapFont } from './BitmapFont';
export var FontID;
(function (FontID) {
    FontID["H1"] = "H1";
    FontID["H2"] = "H2";
    FontID["FIELD"] = "FIELD";
})(FontID || (FontID = {}));
class FontStoreClass {
    constructor() {
        this.fonts = new Map();
        this.initialized = false;
    }
    async initialize() {
        if (this.initialized)
            return;
        const fontConfigs = [
            { id: FontID.H1, fnt: '/fonts/04b03_h1.fnt', png: '/fonts/04b03_h1.png' },
            { id: FontID.H2, fnt: '/fonts/04b03_h2.fnt', png: '/fonts/04b03_h2.png' },
            { id: FontID.FIELD, fnt: '/fonts/04b03_field.fnt', png: '/fonts/04b03_field.png' },
        ];
        await Promise.all(fontConfigs.map(async (config) => {
            const font = new BitmapFont();
            await font.load(config.fnt, config.png);
            this.fonts.set(config.id, font);
        }));
        this.initialized = true;
    }
    getFont(id) {
        const font = this.fonts.get(id);
        if (!font) {
            throw new Error(`Font ${id} not loaded`);
        }
        return font;
    }
}
export const FontStore = new FontStoreClass();
//# sourceMappingURL=FontStore.js.map