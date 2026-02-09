import { Scene } from './Scene';
import { SceneID } from './SceneID';
import { Label } from '../component/Label';
import { ConditionBar } from '../component/ConditionBar';
import { ReferencePoint } from '../component/Component';
import { Color } from '../core/Color';
import { FontStore, FontID } from '../core/FontStore';
import { ImageStore } from '../core/ImageStore';
import { SoundStore } from '../core/SoundStore';
class LoadingCondition {
    constructor(max) {
        this.current = 0;
        this.max = 0;
        this.max = max;
    }
    getCurrent() {
        return this.current;
    }
    getPercentage() {
        if (this.max === 0)
            return 0;
        return this.current / this.max;
    }
    getMin() {
        return 0;
    }
    getMax() {
        return this.max;
    }
    increase(amount) {
        this.current += amount;
        if (this.current > this.max) {
            this.current = this.max;
        }
    }
}
export class LoadingScene extends Scene {
    constructor(canvasWidth, canvasHeight, onComplete) {
        super();
        this.loading = false;
        this.onComplete = null;
        this.onComplete = onComplete;
        this.init(canvasWidth, canvasHeight);
    }
    init(canvasWidth, canvasHeight) {
        const h2Font = FontStore.getFont(FontID.H2);
        const fieldFont = FontStore.getFont(FontID.FIELD);
        this.loadCondition = new LoadingCondition(100);
        this.loadingBar = new ConditionBar(LoadingScene.BAR_WIDTH, LoadingScene.BAR_HEIGHT, this.loadCondition);
        this.mainLayer.add(this.loadingBar);
        this.loadingBar.setPosition({ x: canvasWidth / 2, y: canvasHeight / 2 }, ReferencePoint.TOPCENTER);
        const loadingLabel = Label.withTextWidth(h2Font, Color.white, 'Loading...');
        this.mainLayer.add(loadingLabel);
        loadingLabel.setPosition(this.loadingBar.getPosition(ReferencePoint.TOPCENTER), ReferencePoint.BOTTOMCENTER, 0, -LoadingScene.PADDING);
        this.loadLabel = new Label(canvasWidth, fieldFont.getLineHeight(), fieldFont, Color.white, 'Initializing...');
        this.mainLayer.add(this.loadLabel);
        this.loadLabel.setPosition(this.loadingBar.getPosition(ReferencePoint.BOTTOMCENTER), ReferencePoint.TOPCENTER, 0, LoadingScene.PADDING);
    }
    prepareToEnter() {
        super.prepareToEnter();
        this.startLoading();
    }
    async startLoading() {
        if (this.loading)
            return;
        this.loading = true;
        try {
            this.loadLabel.setText('Loading images...');
            await ImageStore.initialize((loaded, total) => {
                const percentage = (loaded / total) * 50;
                this.loadCondition = new LoadingCondition(100);
                this.loadCondition.increase(percentage);
                this.loadingBar.setCondition(this.loadCondition);
            });
            this.loadLabel.setText('Loading sounds...');
            await SoundStore.initialize((loaded, total) => {
                const percentage = 50 + (loaded / total) * 50;
                this.loadCondition = new LoadingCondition(100);
                this.loadCondition.increase(percentage);
                this.loadingBar.setCondition(this.loadCondition);
            });
            this.loadLabel.setText('Loading complete');
            this.loadCondition.increase(100);
            this.loadingBar.setCondition(this.loadCondition);
            setTimeout(() => {
                if (this.onComplete) {
                    this.onComplete();
                    this.onComplete = null;
                }
            }, 200);
        }
        catch (error) {
            this.loadLabel.setText('Error loading resources');
            this.loadLabel.setColor(Color.red);
            console.error('Loading error:', error);
        }
    }
    update(delta) {
        super.update(delta);
    }
    getID() {
        return SceneID.LOADING;
    }
}
LoadingScene.PADDING = 20;
LoadingScene.BAR_WIDTH = 200;
LoadingScene.BAR_HEIGHT = 10;
//# sourceMappingURL=LoadingScene.js.map