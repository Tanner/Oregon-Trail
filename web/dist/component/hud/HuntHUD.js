import { HUD } from './HUD';
import { Button } from '../Button';
import { Label } from '../Label';
import { ReferencePoint } from '../Component';
import { Color } from '../../core/Color';
import { FontStore, FontID } from '../../core/FontStore';
export class HuntHUD extends HUD {
    constructor(width, onCamp, onInventory) {
        super(width, HUD.HEIGHT);
        const fieldFont = FontStore.getFont(FontID.FIELD);
        this.ammoLabel = new Label(300, fieldFont.getLineHeight(), fieldFont, Color.white, 'Ammo: 0 bullets, 0 boxes');
        this.add(this.ammoLabel);
        this.ammoLabel.setPosition({ x: HuntHUD.MARGIN, y: HUD.HEIGHT / 2 }, ReferencePoint.CENTERLEFT, 0, 0);
        this.campButton = new Button(100, HuntHUD.BUTTON_HEIGHT, Label.withTextWidth(fieldFont, Color.white, 'Camp'));
        this.campButton.addClickListener(onCamp);
        this.campButton.layout();
        this.inventoryButton = new Button(100, HuntHUD.BUTTON_HEIGHT, Label.withTextWidth(fieldFont, Color.white, 'Inventory'));
        this.inventoryButton.addClickListener(onInventory);
        this.inventoryButton.layout();
        this.add(this.campButton);
        this.campButton.setPosition({ x: width - HuntHUD.MARGIN - 100, y: HUD.HEIGHT / 2 }, ReferencePoint.CENTERRIGHT, 0, 0);
        this.add(this.inventoryButton);
        this.inventoryButton.setPosition({ x: this.campButton.getX() - HuntHUD.MARGIN, y: HUD.HEIGHT / 2 }, ReferencePoint.CENTERRIGHT, 0, 0);
    }
    setAmmo(bullets, boxes) {
        this.ammoLabel.setText(`Ammo: ${bullets} bullets, ${boxes} boxes`);
    }
}
HuntHUD.MARGIN = 10;
HuntHUD.BUTTON_HEIGHT = HUD.HEIGHT - 2 * HuntHUD.MARGIN;
//# sourceMappingURL=HuntHUD.js.map