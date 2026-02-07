# Oregon Trail Web Port — Design Document

## Goal

Faithful preservation port of the Java/Slick2D Oregon Trail game to the web. The game should look, sound, and play identically to the original. Anyone with a browser can play it — no Java or LWJGL required.

## Decisions

| Decision | Choice |
|---|---|
| Goal | Preservation / nostalgia — pixel-faithful reproduction |
| Rendering | All Canvas (single `<canvas>`, no DOM UI) |
| Audio | Full port — all 30 OGG music tracks and SFX |
| Language | TypeScript |
| Build tool | Vite |
| Deployment | Static site on GitHub Pages |
| Save system | `localStorage` with JSON serialization (5 slots) |
| Canvas size | Fixed 1024x576, no scaling |

## Project Structure

Everything lives in `web/` at the repo root. The Java source is untouched.

```
Oregon-Trail/
├── src/                        # Original Java source (unchanged)
├── resources/                  # Original assets (unchanged)
├── web/
│   ├── index.html              # Single HTML page with <canvas>
│   ├── vite.config.ts
│   ├── tsconfig.json
│   ├── package.json
│   ├── src/
│   │   ├── main.ts             # Entry point
│   │   ├── core/
│   │   │   ├── GameDirector.ts
│   │   │   ├── SceneDirector.ts
│   │   │   ├── ImageStore.ts
│   │   │   ├── SoundStore.ts
│   │   │   ├── FontStore.ts
│   │   │   ├── InputManager.ts
│   │   │   └── ConstantStore.ts
│   │   ├── component/
│   │   │   ├── Component.ts
│   │   │   ├── Panel.ts
│   │   │   ├── Button.ts
│   │   │   ├── Label.ts
│   │   │   ├── TextField.ts
│   │   │   ├── SegmentedControl.ts
│   │   │   ├── Counter.ts
│   │   │   ├── Spinner.ts
│   │   │   ├── ConditionBar.ts
│   │   │   ├── modal/
│   │   │   │   ├── Modal.ts
│   │   │   │   ├── MessageModal.ts
│   │   │   │   ├── ChoiceModal.ts
│   │   │   │   └── ComponentModal.ts
│   │   │   ├── hud/
│   │   │   │   ├── HUD.ts
│   │   │   │   ├── TrailHUD.ts
│   │   │   │   ├── TownHUD.ts
│   │   │   │   └── HuntHUD.ts
│   │   │   ├── parallax/
│   │   │   │   ├── ParallaxPanel.ts
│   │   │   │   └── ParallaxLayer.ts
│   │   │   └── sprite/
│   │   │       ├── Sprite.ts
│   │   │       ├── AnimatingSprite.ts
│   │   │       ├── HunterAnimatingSprite.ts
│   │   │       ├── PreyAnimatingSprite.ts
│   │   │       └── WagonSprite.ts
│   │   ├── model/
│   │   │   ├── Game.ts
│   │   │   ├── Party.ts
│   │   │   ├── Person.ts
│   │   │   ├── Player.ts
│   │   │   ├── Inventory.ts
│   │   │   ├── Item.ts
│   │   │   ├── ItemType.ts
│   │   │   ├── Condition.ts
│   │   │   ├── Time.ts
│   │   │   ├── WorldMap.ts
│   │   │   ├── LocationNode.ts
│   │   │   ├── TrailEdge.ts
│   │   │   └── ...
│   │   └── scene/
│   │       ├── Scene.ts
│   │       ├── SplashScene.ts
│   │       ├── LoadingScene.ts
│   │       ├── MainMenuScene.ts
│   │       ├── PartyCreationScene.ts
│   │       ├── TownScene.ts
│   │       ├── StoreScene.ts
│   │       ├── TrailScene.ts
│   │       ├── HuntScene.ts
│   │       ├── RiverScene.ts
│   │       ├── MapScene.ts
│   │       ├── PartyInventoryScene.ts
│   │       ├── TavernScene.ts
│   │       ├── OptionsScene.ts
│   │       ├── GameOverScene.ts
│   │       ├── VictoryScene.ts
│   │       └── encounter/
│   │           └── RandomEncounterTable.ts
│   └── public/
│       ├── graphics/           # All 296 PNGs (same directory layout)
│       ├── sounds/             # All 30 OGGs
│       └── fonts/              # Bitmap font .fnt + .png pairs
```

The `web/src/` directory mirrors the Java package hierarchy 1:1. `web/src/model/Party.ts` is the translation of `src/model/Party.java`. This makes cross-referencing during the port trivial.

## Core Engine

### Canvas Setup

`index.html` contains a single `<canvas id="game" width="1024" height="576">` element. Fixed size, no scaling, no CSS transforms. The canvas is centered on the page with a black background behind it.

### Game Loop

```ts
let lastTime = 0;

function loop(timestamp: number) {
  const delta = timestamp - lastTime;
  lastTime = timestamp;

  sceneDirector.update(delta);
  sceneDirector.render(ctx);

  requestAnimationFrame(loop);
}
```

`requestAnimationFrame` drives the loop. Every scene and component receives `update(delta)` and `render(ctx)`, directly matching Slick2D's pattern. The `delta` value is milliseconds since the last frame — all time-dependent logic (animation, movement, game clock) uses this value, making the port a line-by-line translation from the Java.

### Input Manager

A single `InputManager` listens on the canvas for `mousedown`, `mouseup`, `mousemove`, `keydown`, and `keyup`. Events are forwarded to the current scene, which dispatches them down the component tree. No coordinate translation needed since the canvas is fixed at 1024x576.

## Scene System

### SceneDirector

Manages a stack of scenes, matching the original:

```ts
class SceneDirector {
  private stack: Scene[] = [];

  pushScene(scene: Scene, popLast: boolean, transition?: Transition) { ... }
  popScene(transition?: Transition) { ... }
  currentScene(): Scene { return this.stack[this.stack.length - 1]; }

  update(delta: number) { this.currentScene().update(delta); }
  render(ctx: CanvasRenderingContext2D) { this.currentScene().render(ctx); }
}
```

### Scene Base Class

Each scene has 4 rendering layers, drawn back-to-front:

```ts
abstract class Scene {
  backgroundLayer: Component[];
  mainLayer: Component[];
  hudLayer: Component[];
  modalLayer: Component[];

  render(ctx: CanvasRenderingContext2D) {
    for (const layer of [this.backgroundLayer, this.mainLayer,
                          this.hudLayer, this.modalLayer]) {
      for (const component of layer) {
        if (component.visible) component.render(ctx);
      }
    }
  }
}
```

When a modal is showing, input is disabled on lower layers.

### Transitions

Fade and rotate transitions between scenes. Implemented by rendering the outgoing scene to an offscreen `OffscreenCanvas`, then animating opacity over ~300ms before the new scene takes over.

### Scene List

All 18 scenes are ported: Splash, Loading, MainMenu, PartyCreation, Town, Store, Trail, Hunt, River, Map, PartyInventory, Tavern, Options, GameOver, Victory, and SceneSelector (debug).

The 2 test scenes (ComponentTest, TrailTest) are skipped.

## Component System

### Base Component

Mirrors the Java original's 676-line `Component.java`:

```ts
class Component {
  x: number; y: number;
  width: number; height: number;
  visible: boolean;
  parent: Component | null;
  children: Component[];
  positionReference: PositionReference;  // 9-point positioning
  backgroundColor: Color | null;
  borderColor: Color | null;
  hasBevel: boolean;

  update(delta: number) {
    for (const child of this.children) child.update(delta);
  }

  render(ctx: CanvasRenderingContext2D) {
    if (!this.visible) return;
    this.renderBackground(ctx);
    this.renderBorder(ctx);
    this.renderContent(ctx);  // Subclasses override
    for (const child of this.children) child.render(ctx);
  }

  containsPoint(x: number, y: number): boolean { ... }
}
```

### Key Subclasses

Each is a direct translation of its Java counterpart:

- **Panel** — container with background/border
- **Button** — bitmap font label, hover/pressed states, click callback
- **Label** — bitmap font text rendering with alignment
- **TextField** — handles `keydown` events directly, draws text and blinking cursor on Canvas
- **SegmentedControl** — row of toggle buttons (pace/rations controls)
- **Counter** — +/- spinner for store quantities
- **ConditionBar** — green/yellow/red health bar
- **Spinner** — loading animation

### Modal System

- **Modal** — base class, added to scene's `modalLayer`, disables input on lower layers
- **MessageModal** — text message with OK button
- **ChoiceModal** — text with multiple button options
- **ComponentModal\<T\>** — wraps an arbitrary component in a modal

`ModalListener` callbacks translate to TypeScript callback functions.

### Mouse Event Dispatch

The scene walks the component tree top-down (modal layer first) to find the topmost visible component under the cursor. Events are delivered to that component only. This prevents click-through to lower layers.

## Asset Loading

### ImageStore

Eagerly loads all ~296 PNGs at startup, matching the original:

```ts
class ImageStore {
  private images: Map<string, HTMLImageElement> = new Map();

  async initialize(onProgress: (pct: number) => void): Promise<void> {
    const manifest: [string, string][] = [
      ["WAGON", "graphics/trail/wagon.png"],
      ["DEER", "graphics/animals/deer.png"],
      // ... all entries from Java's ImageStore.initialize()
    ];

    let loaded = 0;
    await Promise.all(manifest.map(([key, path]) =>
      new Promise<void>((resolve) => {
        const img = new Image();
        img.onload = () => {
          this.images.set(key, img);
          loaded++;
          onProgress(loaded / manifest.length);
          resolve();
        };
        img.src = path;
      })
    ));
  }
}
```

All images load in parallel. The LoadingScene displays progress via the `onProgress` callback.

### SoundStore

Loads all 30 OGG files via `fetch()` → `ArrayBuffer` → `AudioContext.decodeAudioData()` → stored as `AudioBuffer` in a Map.

Playback:
- **SFX** — Create a new `AudioBufferSourceNode` per play, connect to an SFX `GainNode`
- **Music** — Persistent source node connected to a Music `GainNode`, with crossfade for playlist transitions
- **Volume** — Separate `GainNode` for music and SFX channels

Browser autoplay policy: the `AudioContext` is created on the first user click (splash screen or main menu). The original already requires clicking through the splash screen, so this is natural.

### FontStore

Parses AngelCode bitmap font `.fnt` files (text format) at load time. Each character's position, size, and offset in the sprite sheet PNG is stored in a Map. Text rendering draws character-by-character from the sprite sheet onto the Canvas.

This preserves the exact pixel font look of the original. Four font sizes: H1, H2, FIELD, BIG_FIELD.

## Game Models

### Overview

26 model classes, all pure logic with no rendering dependencies. Each Java class translates nearly line-for-line to TypeScript.

- **Game** — top-level state container (Player, WorldMap, Inventory)
- **Party** — core gameplay: `walk()` advances the party, consumes food, degrades conditions, checks encounters (~880 lines)
- **Person** — name, profession, skills array, health Condition, personal inventory
- **Player** — extends Person with party leadership
- **Inventory** — size/weight-limited storage, add/remove by ItemType
- **Item / ItemType** — food, tools, weapons, animals, vehicles with weight, cost, condition degradation
- **Condition** — current/max value pair, used for health on people, animals, wagon, items
- **Time** — 24-hour clock, day/month/year, time-of-day categories (morning/afternoon/evening/night)
- **WorldMap** — graph of LocationNodes connected by TrailEdges
- **LocationNode** — a town or landmark with name, territory, rank (distance from start)
- **TrailEdge** — connection between nodes with length/difficulty

### Enums

Java enums translate directly to TypeScript enums: `Pace`, `Rations`, `Profession`, `Skill`, `Territory`, `ItemType`.

### Save / Load

Java serialization (`.ser` files) is replaced by JSON + `localStorage`:

```ts
// Save
localStorage.setItem("save-slot-1", JSON.stringify(game.toJSON()));

// Load
const data = JSON.parse(localStorage.getItem("save-slot-1")!);
const game = Game.fromJSON(data);
```

Each model class implements `toJSON()` and a static `fromJSON()`. The WorldMap graph reconstructs object references via ID-based lookups during deserialization. Five save slots, matching the original.

## Gameplay Scenes

### TrailScene

The most complex scene. Each `update(delta)`:

1. Advances parallax layers (sky, far mountains, near mountains, trees, ground) at different scroll speeds
2. Animates party member walking sprites (frame cycling)
3. Animates wagon wheels (rotation)
4. Calls `party.walk()` to advance game state
5. Updates sky color via `AnimatingColor` (linear interpolation between time-of-day color stops)
6. Checks `RandomEncounterTable` for events (thieves, potholes, items, rivers)
7. Updates HUD: location name, territory, date, time, pace/rations segmented controls

The parallax system tiles background images offset by `scrollX * speedMultiplier` per layer. `ParallaxPanel` and `ParallaxLayer` translate directly.

### HuntScene

Top-down minigame on a tile-based map:

- 8-directional hunter movement via arrow keys
- Prey sprites with flee AI (move away when hunter is within range)
- Spacebar fires — bullet travel animation with ray-rectangle hit detection
- Ammo tracking in HuntHUD
- `HuntingGroundsComponent` handles viewport scrolling and tile rendering

### RiverScene

Animated water (scrolling tiled texture) with a bridge/ford visual. A choice modal overlays the scene: ford the river, caulk the wagon, or pay for a ferry. Outcome affects party health and item condition based on river depth and party stats.

## Constants & Game Data

`ConstantStore.java` (593 lines) translates to `ConstantStore.ts`. All hardcoded data stays hardcoded — town names by territory, item properties, UI strings, color definitions. Moving to JSON data files would be cleaner but is out of scope for a faithful port.

## What We Skip

- **Debug scenes**: ComponentTestScene, TrailTestScene (SceneSelector kept for dev convenience)
- **Java serialization format**: replaced by JSON/localStorage
- **Ant build system**: replaced by Vite
- **Native libraries**: LWJGL/OpenGL replaced by Canvas 2D API
- **JUnit tests**: not ported (new tests can be written if desired)

## Implementation Phases

### Phase 1: Core Engine + Loading
Canvas setup, game loop (`requestAnimationFrame` + `update(delta)` / `render(ctx)`), InputManager, ImageStore, FontStore, SceneDirector with push/pop. SplashScene and LoadingScene.

**Deliverable:** Splash screen renders, loading bar fills as assets load.

### Phase 2: Component Framework + Menus
Component base class, Panel, Button, Label, TextField, SegmentedControl, Counter, Modal system, mouse/keyboard dispatch. MainMenuScene, PartyCreationScene, OptionsScene.

**Deliverable:** Navigate menus, create a party with names/professions/skills.

### Phase 3: Trail + Towns
Party, WorldMap, Time, Inventory, Item models. TrailScene with parallax, animated sprites, HUD, pace/rations, random encounters. TownScene, StoreScene, TavernScene, MapScene, PartyInventoryScene.

**Deliverable:** Core gameplay loop — travel between towns, buy supplies, manage party.

### Phase 4: Minigames + Endgame
HuntScene with top-down movement and prey AI. RiverScene with crossing choices. GameOverScene, VictoryScene. Save/load with localStorage.

**Deliverable:** Complete, playable game with all scenes.

### Phase 5: Audio + Polish
SoundStore with Web Audio API. Music playlists per scene, SFX triggers, volume controls in OptionsScene. Scene transitions (fade/rotate). Cross-browser testing. GitHub Pages deployment.

**Deliverable:** Faithful, complete port deployed and playable at a URL.
