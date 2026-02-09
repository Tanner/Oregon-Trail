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
import { Scene, SceneDelegate } from '../scene/Scene';
import { SceneID } from '../scene/SceneID';
import { Game } from '../model/Game';
import { WorldMap } from '../model/WorldMap';
import { FadeOutTransition, FadeInTransition, RotateTransition } from './Transition';
import { Color } from './Color';

export class GameDirector implements SceneDelegate {
  private canvas: HTMLCanvasElement;
  private ctx: CanvasRenderingContext2D;
  private sceneDirector: SceneDirector;
  private inputManager: InputManager;
  private lastTimestamp: number = 0;
  private running: boolean = false;
  private game: Game;
  private worldMap: WorldMap;

  constructor(canvas: HTMLCanvasElement) {
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

    // Expose requestScene to scenes via window
    (window as any).__requestScene = (id: SceneID, lastScene: Scene | null, replace: boolean) => {
      this.requestScene(id, lastScene, replace);
    };
  }

  start(): void {
    if (this.running) return;
    this.running = true;

    // Skip splash, go straight to loading
    const loadingScene = new LoadingScene(this.canvas.width, this.canvas.height, () => {
      const mainMenuScene = this.sceneForSceneID(SceneID.MAINMENU);
      if (mainMenuScene) {
        this.sceneDirector.pushScene(mainMenuScene, true);
      }
    });

    this.sceneDirector.pushScene(loadingScene, false);
    this.lastTimestamp = performance.now();
    this.gameLoop(this.lastTimestamp);
  }

  private gameLoop(timestamp: number): void {
    if (!this.running) return;

    const delta = timestamp - this.lastTimestamp;
    this.lastTimestamp = timestamp;

    this.sceneDirector.update(delta);

    this.ctx.fillStyle = '#000000';
    this.ctx.fillRect(0, 0, this.canvas.width, this.canvas.height);

    this.sceneDirector.render(this.ctx);

    requestAnimationFrame((ts) => this.gameLoop(ts));
  }

  stop(): void {
    this.running = false;
  }

  private sceneForSceneID(id: SceneID, _lastScene?: Scene): Scene | null {
    let scene: Scene | null = null;

    switch (id) {
      case SceneID.SPLASH:
        scene = new SplashScene(this.canvas.width, this.canvas.height, () => {
          this.requestScene(SceneID.LOADING, null, true);
        });
        break;
      case SceneID.LOADING:
        scene = new LoadingScene(this.canvas.width, this.canvas.height, () => {
          this.requestScene(SceneID.MAINMENU, null, true);
        });
        break;
      case SceneID.MAINMENU:
        scene = new MainMenuScene(this.canvas.width, this.canvas.height);
        break;
      case SceneID.PARTYCREATION:
        scene = new PartyCreationScene(this.canvas.width, this.canvas.height);
        break;
      case SceneID.TOWN:
        this.game.resetStoreInventory(this.worldMap.getCurrLocationNode());
        // TownScene not yet implemented - placeholder
        console.log('TownScene requested but not yet implemented');
        break;
      case SceneID.STORE:
        // StoreScene not yet implemented - placeholder
        console.log('StoreScene requested but not yet implemented');
        break;
      case SceneID.PARTYINVENTORY:
        // PartyInventoryScene not yet implemented - placeholder
        console.log('PartyInventoryScene requested but not yet implemented');
        break;
      case SceneID.HUNT:
        // HuntScene not yet implemented - placeholder
        console.log('HuntScene requested but not yet implemented');
        break;
      case SceneID.TRAIL:
        // TrailScene in progress - placeholder
        console.log('TrailScene requested but not yet implemented');
        break;
      case SceneID.GAMEOVER:
        scene = new GameOverScene(this.canvas.width, this.canvas.height);
        break;
      case SceneID.VICTORY:
        scene = new VictoryScene(this.canvas.width, this.canvas.height);
        break;
      case SceneID.MAP:
        scene = new MapScene(this.game.getWorldMap());
        break;
      case SceneID.RIVER:
        // RiverScene not yet implemented - placeholder
        console.log('RiverScene requested but not yet implemented');
        break;
      case SceneID.OPTIONS:
        scene = new OptionsScene(this.canvas.width, this.canvas.height);
        (scene as OptionsScene).setOnMainMenu(() => this.resetToMainMenu());
        (scene as OptionsScene).setOnBack(() => {
          const currentScene = this.sceneDirector.currentScene();
          if (currentScene) {
            currentScene.leave();
          }
        });
        break;
      case SceneID.TAVERN:
        // TavernScene not yet implemented - placeholder
        console.log('TavernScene requested but not yet implemented');
        break;
      case SceneID.SCENESELECTOR:
        // SceneSelectorScene not yet implemented - placeholder
        console.log('SceneSelectorScene requested but not yet implemented');
        break;
    }

    if (scene) {
      scene.setSceneDelegate(this);
    }

    return scene;
  }

  requestScene(id: SceneID | null, lastScene: Scene | null, popLastScene: boolean): void {
    let outTransition: FadeOutTransition | RotateTransition | null = null;
    let inTransition: FadeInTransition | RotateTransition | null = null;

    if (
      this.game.getPlayer().getParty() &&
      this.game.getPlayer().getParty()!.getLocation()
    ) {
      this.worldMap.setCurrLocationNode(this.game.getPlayer().getParty()!.getLocation()!);
      const trail = this.game.getPlayer().getParty()!.getTrail();
      if (trail) {
        this.worldMap.setCurrTrail(trail);
      }
    }

    let newScene: Scene | null = id !== null ? this.sceneForSceneID(id, lastScene || undefined) : null;

    if (this.worldMap.getCurrLocationNode().getRank() === this.worldMap.getMaxRank()) {
      newScene = new VictoryScene(this.canvas.width, this.canvas.height);
    }

    if (newScene instanceof VictoryScene || newScene instanceof GameOverScene) {
      inTransition = new RotateTransition(Color.black);
    } else if (newScene && lastScene) {
      // Special case: PartyInventoryScene from StoreScene
      // Note: Requires StoreScene implementation

      // Special case: After PartyCreationScene
      const lastSceneID = lastScene.getID();
      if (lastSceneID === SceneID.PARTYCREATION) {
        this.game.getPlayer().getParty()!.setLocation(this.game.getWorldMap().getMapHead());
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

  resetToMainMenu(): void {
    const mainMenuScene = this.sceneForSceneID(SceneID.MAINMENU);
    if (mainMenuScene) {
      this.sceneDirector.replaceStackWithScene(mainMenuScene);
    }
    this.game = new Game(this.game.getWorldMap());
  }

  // Save/load stubs for Phase 4
  serialize(_saveName: string): void {
    console.log('Save functionality not yet implemented - Phase 4');
  }

  deserialize(_saveName: string): Game | null {
    console.log('Load functionality not yet implemented - Phase 4');
    return null;
  }
}
