import { Scene } from './Scene';
import { SceneID } from './SceneID';
import { Panel } from '../component/Panel';
import { Sprite } from '../component/Sprite';
import { ReferencePoint } from '../component/Component';
import { Color } from '../core/Color';
import { AnimatingColor } from '../core/AnimatingColor';
import { ImageStore } from '../core/ImageStore';
import { SoundStore } from '../core/SoundStore';
import { TownHUD } from '../component/hud/TownHUD';
import { AnimatingSprite, Direction } from '../component/sprite/AnimatingSprite';
import { ParallaxPanel } from '../component/parallax/ParallaxPanel';
import { ParallaxComponentLoop } from '../component/parallax/ParallaxComponentLoop';
import { ComponentModal } from '../component/modal/ComponentModal';
import { SegmentedControl } from '../component/SegmentedControl';
import { Skill } from '../model/Skill';
import { get as getLiteral } from '../core/ConstantStore';
export class TownScene extends Scene {
    constructor(party, location) {
        super();
        this.clickCounter = 0;
        this.timeElapsed = 0;
        this.groundY = 0;
        this.party = party;
        this.location = location;
        this.init();
    }
    init() {
        SoundStore.playTownMusic();
        this.hud = new TownHUD(TownScene.CANVAS_WIDTH, () => {
            if (this.trailChoiceModal) {
                this.showModal(this.trailChoiceModal);
            }
        });
        this.hud.setNotification(this.location.getName());
        this.hudLayer.add(this.hud);
        this.hud.setPosition({ x: 0, y: 0 }, ReferencePoint.TOPLEFT);
        const hour = this.party.getTime().getTime();
        const skyColor = this.getSkyColor(hour);
        this.sky = new Panel(TownScene.CANVAS_WIDTH, TownScene.CANVAS_HEIGHT, skyColor);
        this.backgroundLayer.add(this.sky);
        this.sky.setPosition({ x: 0, y: 0 }, ReferencePoint.TOPLEFT);
        const parallaxPanel = new ParallaxPanel(TownScene.CANVAS_WIDTH, TownScene.CANVAS_HEIGHT);
        parallaxPanel.setMaxDistance(1);
        const groundHeight = 100;
        this.groundY = TownScene.CANVAS_HEIGHT - groundHeight;
        const ground = new ParallaxComponentLoop(TownScene.CANVAS_WIDTH, groundHeight, ImageStore.getImage('GRASS'));
        parallaxPanel.addParallaxComponent(ground, 0.5);
        ground.setPosition({ x: 0, y: this.groundY }, ReferencePoint.TOPLEFT);
        const trailHeight = 50;
        const trail = new ParallaxComponentLoop(TownScene.CANVAS_WIDTH, trailHeight, ImageStore.getImage('TRAIL'));
        parallaxPanel.addParallaxComponent(trail, 0.5);
        trail.setPosition({ x: 0, y: this.groundY + 80 }, ReferencePoint.TOPLEFT);
        const hillB = new ParallaxComponentLoop(TownScene.CANVAS_WIDTH, 200, ImageStore.getImage('HILL_B'));
        parallaxPanel.addParallaxComponent(hillB, 0.7);
        hillB.setPosition({ x: 0, y: this.groundY - 120 }, ReferencePoint.TOPLEFT);
        const hillA = new ParallaxComponentLoop(TownScene.CANVAS_WIDTH, 200, ImageStore.getImage('HILL_A'));
        parallaxPanel.addParallaxComponent(hillA, 0.8);
        hillA.setPosition({ x: 0, y: this.groundY - 100 }, ReferencePoint.TOPLEFT);
        this.mainLayer.add(parallaxPanel);
        parallaxPanel.setPosition({ x: 0, y: 0 }, ReferencePoint.TOPLEFT);
        this.store = new Sprite(400, ImageStore.getImage('STORE_BUILDING'));
        this.mainLayer.add(this.store);
        this.store.setPosition({ x: 20, y: this.groundY + 50 }, ReferencePoint.BOTTOMLEFT);
        this.tavern = new Sprite(400, ImageStore.getImage('SALOON_BUILDING'));
        this.mainLayer.add(this.tavern);
        this.tavern.setPosition({ x: TownScene.CANVAS_WIDTH - 20, y: this.groundY + 50 }, ReferencePoint.BOTTOMRIGHT);
        this.partyLeaderSprite = new AnimatingSprite(96, [ImageStore.getImage('HUNTER_LEFT')], [ImageStore.getImage('HUNTER_RIGHT')], Direction.RIGHT, 250);
        this.mainLayer.add(this.partyLeaderSprite);
        this.partyLeaderSprite.setPosition({ x: TownScene.CANVAS_WIDTH / 2, y: trail.getPosition(ReferencePoint.BOTTOMCENTER).y - 25 }, ReferencePoint.BOTTOMCENTER);
        if (this.location.getTrails() > 0) {
            const trails = [];
            for (let i = 0; i < this.location.getTrails(); i++) {
                const temp = this.location.getOutBoundTrailByIndex(i);
                trails.push(`${temp.getRoughLength()} and to the ${temp.getRoughDirection()}, ${temp.getDangerRating()}\n${temp.getName()}`);
            }
            const hasTracking = this.party.getSkills().includes(Skill.TRACKING);
            const buttonCount = hasTracking ? 3 : 2;
            this.trailChoiceModal = new ComponentModal(getLiteral('TOWN_SCENE', 'TRAIL_CHOICE'), buttonCount, new SegmentedControl(700, 300, 3, 1, true, 1, trails));
            if (hasTracking) {
                this.trailChoiceModal.setButtonText(2, getLiteral('TOWN_SCENE', 'TRAILBLAZE'));
            }
            this.trailChoiceModal.setButtonText(0, getLiteral('GENERAL', 'CANCEL'));
        }
        this.adjustSetting();
    }
    getSkyColor(hour) {
        if (hour >= 6 && hour < 12) {
            return Color.fromHex(0x87CEEB);
        }
        else if (hour >= 12 && hour < 18) {
            return Color.fromHex(0x87CEEB);
        }
        else if (hour >= 18 && hour < 20) {
            return Color.fromHex(0xFF6B35);
        }
        else {
            return Color.fromHex(0x001133);
        }
    }
    adjustSetting() {
        const hour = this.party.getTime().getTime();
        const nextColor = this.getSkyColor(hour);
        this.skyAnimatingColor = new AnimatingColor(this.sky.getBackgroundColor() || Color.black, nextColor, TownScene.CLICK_WAIT_TIME * TownScene.STEP_COUNT_TRIGGER);
        this.sky.setBackgroundColor(this.skyAnimatingColor);
        this.hud.updatePartyInformation(this.party.getTime().get12HourTime(), this.party.getTime().getDayMonthYear());
    }
    update(delta) {
        super.update(delta);
        if (SoundStore.getPlayingMusic() === null) {
            SoundStore.playTownMusic();
        }
        this.timeElapsed += delta;
        const input = window.__inputState;
        if (input) {
            if (input.keysDown.has('ArrowLeft') && this.partyLeaderSprite.getX() > 0) {
                this.partyLeaderSprite.moveLeft(delta);
            }
            else if (input.keysDown.has('ArrowRight') &&
                this.partyLeaderSprite.getX() + this.partyLeaderSprite.getWidth() < TownScene.CANVAS_WIDTH) {
                this.partyLeaderSprite.moveRight(delta);
            }
        }
        const leaderX = this.partyLeaderSprite.getX();
        const storeX = this.store.getX();
        const storeWidth = this.store.getWidth();
        const tavernX = this.tavern.getX();
        const tavernWidth = this.tavern.getWidth();
        if (leaderX > storeX && leaderX < storeX + storeWidth) {
            this.hud.setNotification(getLiteral('TOWN_SCENE', 'ENTER_STORE_INSTRUCTION'));
        }
        else if (leaderX > tavernX && leaderX < tavernX + tavernWidth) {
            this.hud.setNotification(getLiteral('TOWN_SCENE', 'ENTER_TAVERN_INSTRUCTION'));
        }
        else {
            this.hud.setNotification(this.location.getName());
        }
        if (this.skyAnimatingColor) {
            this.skyAnimatingColor.update(delta);
        }
        if (this.timeElapsed >= TownScene.CLICK_WAIT_TIME) {
            this.clickCounter++;
            this.timeElapsed = 0;
        }
        if (this.clickCounter >= TownScene.STEP_COUNT_TRIGGER) {
            this.party.getTime().advanceTime();
            this.clickCounter = 0;
            this.adjustSetting();
        }
    }
    keyReleased(key, code) {
        if (key === 'Enter') {
            const leaderX = this.partyLeaderSprite.getX();
            const storeX = this.store.getX();
            const storeWidth = this.store.getWidth();
            const tavernX = this.tavern.getX();
            const tavernWidth = this.tavern.getWidth();
            if (leaderX > storeX && leaderX < storeX + storeWidth) {
                window.__requestScene?.(SceneID.STORE, this, false);
            }
            else if (leaderX > tavernX && leaderX < tavernX + tavernWidth) {
                window.__requestScene?.(SceneID.TAVERN, this, false);
            }
        }
    }
    dismissModal(modal, button) {
        super.dismissModal(modal, button);
        if (modal === this.trailChoiceModal && button !== 0) {
            const hasTracking = this.party.getSkills().includes(Skill.TRACKING);
            if (button === 2 && hasTracking) {
                const newTrail = window.__trailBlaze?.();
                if (newTrail) {
                    this.party.setTrail(newTrail);
                }
                else {
                    const randomIndex = Math.floor(Math.random() * this.location.getOutboundTrails().length);
                    this.party.setTrail(this.location.getOutBoundTrailByIndex(randomIndex));
                }
            }
            else {
                const selection = this.trailChoiceModal.getComponent().getSelection();
                if (selection.length > 0) {
                    this.party.setTrail(this.location.getOutBoundTrailByIndex(selection[0]));
                }
            }
            window.__requestScene?.(SceneID.TRAIL, this, true);
        }
    }
    getID() {
        return SceneID.TOWN;
    }
}
TownScene.CLICK_WAIT_TIME = 1000;
TownScene.STEP_COUNT_TRIGGER = 20;
TownScene.CANVAS_WIDTH = 1024;
TownScene.CANVAS_HEIGHT = 576;
//# sourceMappingURL=TownScene.js.map