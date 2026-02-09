import { SceneDirector } from './SceneDirector';
import { InputManager } from './InputManager';
import { SplashScene } from '../scene/SplashScene';
import { LoadingScene } from '../scene/LoadingScene';
import { MainMenuScene } from '../scene/MainMenuScene';
import { PartyCreationScene } from '../scene/PartyCreationScene';
import { MapScene } from '../scene/MapScene';
import { GameOverScene } from '../scene/GameOverScene';
import { VictoryScene } from '../scene/VictoryScene';
import { OptionsScene } from '../scene/OptionsScene';
import { SceneID } from '../scene/SceneID';
import { Game } from '../model/Game';
import { WorldMap } from '../model/WorldMap';
import { FadeOutTransition, FadeInTransition, RotateTransition } from './Transition';
import { Color } from './Color';
export class GameDirector {
    constructor(canvas) {
        this.lastTimestamp = 0;
        this.running = false;
        this.canvas = canvas;
        const ctx = canvas.getContext('2d');
        if (!ctx) {
            throw new Error('Failed to get 2D context from canvas');
        }
        this.ctx = ctx;
        this.ctx.imageSmoothingEnabled = false;
        this.sceneDirector = new SceneDirector();
        this.inputManager = new InputManager(canvas);
        this.inputManager.setDelegate(this.sceneDirector);
        this.worldMap = new WorldMap(120);
        this.game = new Game(this.worldMap);
    }
    start() {
        if (this.running)
            return;
        this.running = true;
        const splashScene = new SplashScene(this.canvas.width, this.canvas.height, () => {
            this.sceneDirector.pushScene(new LoadingScene(this.canvas.width, this.canvas.height, () => {
                this.sceneDirector.pushScene(new MainMenuScene(this.canvas.width, this.canvas.height), true);
            }), true);
        });
        this.sceneDirector.pushScene(splashScene, false);
        this.lastTimestamp = performance.now();
        this.gameLoop(this.lastTimestamp);
    }
    gameLoop(timestamp) {
        if (!this.running)
            return;
        const delta = timestamp - this.lastTimestamp;
        this.lastTimestamp = timestamp;
        this.sceneDirector.update(delta);
        this.ctx.fillStyle = '#000000';
        this.ctx.fillRect(0, 0, this.canvas.width, this.canvas.height);
        this.sceneDirector.render(this.ctx);
        requestAnimationFrame((ts) => this.gameLoop(ts));
    }
    stop() {
        this.running = false;
    }
    sceneForSceneID(id, _lastScene) {
        switch (id) {
            case SceneID.SPLASH:
                return new SplashScene(this.canvas.width, this.canvas.height, () => {
                    this.requestScene(SceneID.LOADING, null, true);
                });
            case SceneID.LOADING:
                return new LoadingScene(this.canvas.width, this.canvas.height, () => {
                    this.requestScene(SceneID.MAINMENU, null, true);
                });
            case SceneID.MAINMENU:
                return new MainMenuScene(this.canvas.width, this.canvas.height);
            case SceneID.PARTYCREATION:
                return new PartyCreationScene(this.canvas.width, this.canvas.height);
            case SceneID.TOWN:
                this.game.resetStoreInventory(this.worldMap.getCurrLocationNode());
                // TownScene not yet implemented - placeholder
                console.log('TownScene requested but not yet implemented');
                return null;
            case SceneID.STORE:
                // StoreScene not yet implemented - placeholder
                console.log('StoreScene requested but not yet implemented');
                return null;
            case SceneID.PARTYINVENTORY:
                // PartyInventoryScene not yet implemented - placeholder
                console.log('PartyInventoryScene requested but not yet implemented');
                return null;
            case SceneID.HUNT:
                // HuntScene not yet implemented - placeholder
                console.log('HuntScene requested but not yet implemented');
                return null;
            case SceneID.TRAIL:
                // TrailScene in progress - placeholder
                console.log('TrailScene requested but not yet implemented');
                return null;
            case SceneID.GAMEOVER:
                return new GameOverScene(this.canvas.width, this.canvas.height);
            case SceneID.VICTORY:
                return new VictoryScene(this.canvas.width, this.canvas.height);
            case SceneID.MAP:
                return new MapScene(this.game.getWorldMap());
            case SceneID.RIVER:
                // RiverScene not yet implemented - placeholder
                console.log('RiverScene requested but not yet implemented');
                return null;
            case SceneID.OPTIONS:
                const optionsScene = new OptionsScene(this.canvas.width, this.canvas.height);
                optionsScene.setOnMainMenu(() => this.resetToMainMenu());
                optionsScene.setOnBack(() => {
                    const currentScene = this.sceneDirector.currentScene();
                    if (currentScene) {
                        currentScene.leave();
                    }
                });
                return optionsScene;
            case SceneID.TAVERN:
                // TavernScene not yet implemented - placeholder
                console.log('TavernScene requested but not yet implemented');
                return null;
            case SceneID.SCENESELECTOR:
                // SceneSelectorScene not yet implemented - placeholder
                console.log('SceneSelectorScene requested but not yet implemented');
                return null;
            default:
                return null;
        }
    }
    requestScene(id, lastScene, popLastScene) {
        let outTransition = null;
        let inTransition = null;
        if (this.game.getPlayer().getParty() &&
            this.game.getPlayer().getParty().getLocation()) {
            this.worldMap.setCurrLocationNode(this.game.getPlayer().getParty().getLocation());
            const trail = this.game.getPlayer().getParty().getTrail();
            if (trail) {
                this.worldMap.setCurrTrail(trail);
            }
        }
        let newScene = id !== null ? this.sceneForSceneID(id, lastScene || undefined) : null;
        if (this.worldMap.getCurrLocationNode().getRank() === this.worldMap.getMaxRank()) {
            newScene = new VictoryScene(this.canvas.width, this.canvas.height);
        }
        if (newScene instanceof VictoryScene || newScene instanceof GameOverScene) {
            inTransition = new RotateTransition(Color.black);
        }
        else if (newScene && lastScene) {
            // Special case: PartyInventoryScene from StoreScene
            // Note: Requires StoreScene implementation
            // Special case: After PartyCreationScene
            const lastSceneID = lastScene.getID();
            if (lastSceneID === SceneID.PARTYCREATION) {
                this.game.getPlayer().getParty().setLocation(this.game.getWorldMap().getMapHead());
            }
        }
        if (newScene) {
            if (lastScene) {
                lastScene.leave();
            }
            if (outTransition === null) {
                outTransition = new FadeOutTransition(Color.black);
            }
            if (inTransition === null) {
                inTransition = new FadeInTransition(Color.black);
            }
            this.sceneDirector.pushScene(newScene, popLastScene, true, outTransition, inTransition);
        }
    }
    resetToMainMenu() {
        const mainMenuScene = this.sceneForSceneID(SceneID.MAINMENU);
        if (mainMenuScene) {
            this.sceneDirector.replaceStackWithScene(mainMenuScene);
        }
        this.game = new Game(this.game.getWorldMap());
    }
    // Save/load stubs for Phase 4
    serialize(_saveName) {
        console.log('Save functionality not yet implemented - Phase 4');
    }
    deserialize(_saveName) {
        console.log('Load functionality not yet implemented - Phase 4');
        return null;
    }
}
//# sourceMappingURL=GameDirector.js.map