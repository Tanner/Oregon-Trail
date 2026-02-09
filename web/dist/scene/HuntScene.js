import { Scene } from './Scene';
import { SceneID } from './SceneID';
import { HuntingGroundsComponent, TerrainType } from '../component/HuntingGroundsComponent';
import { HunterAnimatingSprite } from '../component/sprite/HunterAnimatingSprite';
import { PreyAnimatingSprite, PreyType } from '../component/sprite/PreyAnimatingSprite';
import { HuntHUD } from '../component/hud/HuntHUD';
import { Sprite } from '../component/Sprite';
import { ImageStore } from '../core/ImageStore';
import { ReferencePoint } from '../component/Component';
/**
 * HuntScene - Hunting minigame with 8-directional movement and shooting mechanics.
 */
export class HuntScene extends Scene {
    constructor(canvasWidth, canvasHeight, party) {
        super();
        this.prey = [];
        this.ammo = 0;
        this.ammoBoxes = 0;
        this.gunCocked = false;
        this.meatCollected = 0;
        this.keys = new Set();
        this.mouseX = 400;
        this.mouseY = 300;
        this.hunterWorldX = 1200;
        this.hunterWorldY = 1200;
        this.party = party;
        this.huntingGrounds = new HuntingGroundsComponent(TerrainType.GRASS);
        this.hunter = new HunterAnimatingSprite(48);
        this.reticle = new Sprite(32, undefined, ImageStore.getImage('HUNT_RETICLE'));
        this.hud = new HuntHUD(canvasWidth, () => this.onCamp(), () => this.onInventory());
        this.initialize();
    }
    initialize() {
        this.countAmmo();
        this.backgroundLayer.add(this.huntingGrounds);
        this.huntingGrounds.setPosition({ x: 0, y: 0 }, ReferencePoint.TOPLEFT);
        this.spawnPrey();
        this.mainLayer.add(this.hunter);
        this.mainLayer.add(this.reticle);
        this.hudLayer.add(this.hud);
        this.hud.setPosition({ x: 0, y: 0 }, ReferencePoint.TOPLEFT);
        this.updateViewport();
    }
    countAmmo() {
        this.ammo = 20;
        this.ammoBoxes = 2;
        this.hud.setAmmo(this.ammo, this.ammoBoxes);
    }
    spawnPrey() {
        const preyCount = Math.floor(Math.random() * 4) + 3;
        for (let i = 0; i < preyCount && i < HuntScene.MAX_PREY; i++) {
            const type = Math.random() < 0.5 ? PreyType.COW : PreyType.PIG;
            const preySprite = new PreyAnimatingSprite(64, type);
            const worldX = Math.random() * 2000 + 200;
            const worldY = Math.random() * 2000 + 200;
            preySprite.setLocation(worldX, worldY);
            this.prey.push(preySprite);
            this.mainLayer.add(preySprite);
        }
    }
    update(delta) {
        super.update(delta);
        this.handleMovement(delta);
        for (const preySprite of this.prey) {
            if (preySprite.isAlive()) {
                preySprite.fleeFrom(this.hunterWorldX, this.hunterWorldY, delta);
                preySprite.update(delta);
            }
        }
        this.hunter.update(delta);
        this.updateViewport();
    }
    handleMovement(delta) {
        let dx = 0;
        let dy = 0;
        if (this.keys.has('w') || this.keys.has('ArrowUp'))
            dy -= 1;
        if (this.keys.has('s') || this.keys.has('ArrowDown'))
            dy += 1;
        if (this.keys.has('a') || this.keys.has('ArrowLeft'))
            dx -= 1;
        if (this.keys.has('d') || this.keys.has('ArrowRight'))
            dx += 1;
        if (dx !== 0 || dy !== 0) {
            const length = Math.sqrt(dx * dx + dy * dy);
            dx = (dx / length) * HuntScene.MOVE_SPEED * (delta / 16);
            dy = (dy / length) * HuntScene.MOVE_SPEED * (delta / 16);
            this.hunterWorldX += dx;
            this.hunterWorldY += dy;
            this.hunterWorldX = Math.max(0, Math.min(this.hunterWorldX, 2400));
            this.hunterWorldY = Math.max(0, Math.min(this.hunterWorldY, 2400));
            this.hunter.setDirectionFromMovement(dx, dy);
        }
    }
    updateViewport() {
        const viewportX = this.hunterWorldX - 400;
        const viewportY = this.hunterWorldY - 300;
        this.huntingGrounds.setViewport(viewportX, viewportY);
        const screenPos = this.huntingGrounds.worldToScreen(this.hunterWorldX, this.hunterWorldY);
        this.hunter.setPosition({ x: screenPos.x, y: screenPos.y }, ReferencePoint.CENTERCENTER);
        for (const preySprite of this.prey) {
            const preyScreen = this.huntingGrounds.worldToScreen(preySprite.getX(), preySprite.getY());
            preySprite.setPosition({ x: preyScreen.x, y: preyScreen.y }, ReferencePoint.CENTERCENTER);
        }
        this.reticle.setPosition({ x: this.mouseX, y: this.mouseY }, ReferencePoint.CENTERCENTER);
    }
    keyPressed(key, code) {
        this.keys.add(key.toLowerCase());
        this.keys.add(code);
    }
    keyReleased(key, code) {
        this.keys.delete(key.toLowerCase());
        this.keys.delete(code);
    }
    mouseMoved(x, y) {
        super.mouseMoved(x, y);
        this.mouseX = x;
        this.mouseY = y;
    }
    mousePressed(button, x, y) {
        super.mousePressed(button, x, y);
        if (button === 2) {
            this.cockGun();
        }
        else if (button === 0 && this.gunCocked) {
            this.fire(x, y);
        }
    }
    cockGun() {
        if (this.ammo > 0) {
            this.gunCocked = true;
            console.log('Gun cocked');
        }
        else if (this.ammoBoxes > 0) {
            this.ammoBoxes--;
            this.ammo = HuntScene.BULLETS_PER_BOX;
            this.gunCocked = true;
            this.hud.setAmmo(this.ammo, this.ammoBoxes);
            console.log('Opened ammo box');
        }
        else {
            console.log('Out of ammo');
        }
    }
    fire(targetX, targetY) {
        if (!this.gunCocked || this.ammo <= 0)
            return;
        this.ammo--;
        this.gunCocked = false;
        this.hud.setAmmo(this.ammo, this.ammoBoxes);
        const hunterScreenX = this.hunter.getX() + this.hunter.getWidth() / 2;
        const hunterScreenY = this.hunter.getY() + this.hunter.getHeight() / 2;
        for (const preySprite of this.prey) {
            if (!preySprite.isAlive())
                continue;
            const preyX = preySprite.getX() + preySprite.getWidth() / 2;
            const preyY = preySprite.getY() + preySprite.getHeight() / 2;
            const distance = this.distanceToLineSegment(preyX, preyY, hunterScreenX, hunterScreenY, targetX, targetY);
            if (distance < 32) {
                preySprite.kill();
                this.meatCollected += preySprite.getMeatValue();
                console.log(`Hit! Collected ${preySprite.getMeatValue()} lbs of meat`);
                break;
            }
        }
    }
    distanceToLineSegment(px, py, x1, y1, x2, y2) {
        const dx = x2 - x1;
        const dy = y2 - y1;
        const lengthSquared = dx * dx + dy * dy;
        if (lengthSquared === 0) {
            return Math.sqrt((px - x1) * (px - x1) + (py - y1) * (py - y1));
        }
        let t = ((px - x1) * dx + (py - y1) * dy) / lengthSquared;
        t = Math.max(0, Math.min(1, t));
        const nearestX = x1 + t * dx;
        const nearestY = y1 + t * dy;
        return Math.sqrt((px - nearestX) * (px - nearestX) + (py - nearestY) * (py - nearestY));
    }
    onCamp() {
        console.log(`Hunt ended. Collected ${this.meatCollected} lbs of meat`);
    }
    onInventory() {
        console.log('Opening inventory');
    }
    getID() {
        return SceneID.HUNT;
    }
}
HuntScene.MOVE_SPEED = 3;
HuntScene.BULLETS_PER_BOX = 20;
HuntScene.MAX_PREY = 6;
//# sourceMappingURL=HuntScene.js.map