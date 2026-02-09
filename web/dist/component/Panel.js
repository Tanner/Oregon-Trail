import { Component, ReferencePoint } from './Component';
import { Color } from '../core/Color';
import { Sprite } from './Sprite';
export class Panel extends Component {
    constructor(width, height, backgroundColorOrImage) {
        super(width, height);
        this.backgroundImage = null;
        if (backgroundColorOrImage instanceof Color) {
            this.setBackgroundColor(backgroundColorOrImage);
        }
        else if (backgroundColorOrImage instanceof HTMLImageElement) {
            this.backgroundImage = backgroundColorOrImage;
            const sprite = new Sprite(width, backgroundColorOrImage);
            this.add(sprite, this.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
        }
    }
    render(ctx) {
        if (!this.isVisible()) {
            return;
        }
        super.render(ctx);
    }
}
//# sourceMappingURL=Panel.js.map