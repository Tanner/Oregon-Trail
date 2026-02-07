# Oregon Trail Web Port — Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Port the Java/Slick2D Oregon Trail game to a TypeScript/Canvas web app, preserving the original look, feel, and gameplay.

**Architecture:** Single `<canvas>` element at 1024x576, TypeScript with Vite, all rendering via Canvas 2D API. Mirrors the Java package hierarchy 1:1 under `web/src/`. Game loop uses `requestAnimationFrame` with `update(delta)` / `render(ctx)` matching Slick2D's pattern.

**Tech Stack:** TypeScript, Vite, Canvas 2D API, Web Audio API, GitHub Pages

**Reference:** The Java source in `src/` is the ground truth. Every TypeScript file maps to a Java file. When in doubt, read the Java.

**Design doc:** `docs/plans/2026-02-07-web-port-design.md`

---

## Phase 1: Core Engine + Loading

### Task 1: Project Scaffolding

**Files:**
- Create: `web/package.json`
- Create: `web/tsconfig.json`
- Create: `web/vite.config.ts`
- Create: `web/index.html`
- Create: `web/src/main.ts`

**Step 1: Initialize the project**

```bash
cd web
npm init -y
npm install --save-dev typescript vite
```

**Step 2: Create `web/tsconfig.json`**

```json
{
  "compilerOptions": {
    "target": "ES2020",
    "module": "ESNext",
    "moduleResolution": "bundler",
    "strict": true,
    "noUnusedLocals": true,
    "noUnusedParameters": true,
    "esModuleInterop": true,
    "sourceMap": true,
    "outDir": "dist",
    "rootDir": "src"
  },
  "include": ["src"]
}
```

**Step 3: Create `web/vite.config.ts`**

```ts
import { defineConfig } from "vite";

export default defineConfig({
  root: ".",
  build: {
    outDir: "dist",
  },
});
```

**Step 4: Create `web/index.html`**

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Oregon Trail</title>
  <style>
    body {
      margin: 0;
      background: #000;
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
    }
    canvas {
      image-rendering: pixelated;
      image-rendering: crisp-edges;
    }
  </style>
</head>
<body>
  <canvas id="game" width="1024" height="576"></canvas>
  <script type="module" src="/src/main.ts"></script>
</body>
</html>
```

**Step 5: Create `web/src/main.ts`** (minimal — just verify canvas works)

```ts
const canvas = document.getElementById("game") as HTMLCanvasElement;
const ctx = canvas.getContext("2d")!;
ctx.fillStyle = "#000";
ctx.fillRect(0, 0, 1024, 576);
ctx.fillStyle = "#fff";
ctx.font = "24px monospace";
ctx.fillText("Oregon Trail - Canvas OK", 350, 290);
```

**Step 6: Verify it runs**

```bash
cd web && npx vite --open
```

Expected: Black canvas with white "Oregon Trail - Canvas OK" text centered.

**Step 7: Commit**

```bash
git add web/
git commit -m "feat: scaffold web port with Vite + TypeScript + Canvas"
```

---

### Task 2: Copy Assets to `web/public/`

**Files:**
- Create: `web/public/graphics/` (copy from `resources/graphics/`)
- Create: `web/public/sounds/` (copy from `resources/sounds/`)
- Create: `web/public/fonts/` (copy from `resources/fonts/`)

**Step 1: Copy assets preserving directory structure**

```bash
mkdir -p web/public
cp -r resources/graphics web/public/graphics
cp -r resources/sounds web/public/sounds
cp -r resources/fonts web/public/fonts
```

**Step 2: Verify a sample image loads**

Add to `web/src/main.ts`:
```ts
const img = new Image();
img.onload = () => {
  ctx.drawImage(img, 400, 200);
  console.log("Image loaded:", img.width, img.height);
};
img.src = "/graphics/logo.png";
```

Run `npx vite` and verify the logo appears on the canvas.

**Step 3: Commit**

```bash
git add web/public/
git commit -m "feat: copy game assets to web/public"
```

---

### Task 3: Color Utility

**Files:**
- Create: `web/src/core/Color.ts`

**Reference:** Slick2D's `Color` class is used everywhere. We need a simple RGBA color class.

**Step 1: Create `web/src/core/Color.ts`**

```ts
export class Color {
  constructor(
    public r: number,
    public g: number,
    public b: number,
    public a: number = 1
  ) {}

  toCSS(): string {
    return `rgba(${Math.round(this.r * 255)}, ${Math.round(this.g * 255)}, ${Math.round(this.b * 255)}, ${this.a})`;
  }

  darker(amount: number): Color {
    return new Color(
      this.r * (1 - amount),
      this.g * (1 - amount),
      this.b * (1 - amount),
      this.a
    );
  }

  brighter(amount: number): Color {
    return new Color(
      Math.min(1, this.r + amount),
      Math.min(1, this.g + amount),
      Math.min(1, this.b + amount),
      this.a
    );
  }

  withAlpha(a: number): Color {
    return new Color(this.r, this.g, this.b, a);
  }

  static fromHex(hex: number): Color {
    return new Color(
      ((hex >> 16) & 0xff) / 255,
      ((hex >> 8) & 0xff) / 255,
      (hex & 0xff) / 255
    );
  }

  static readonly white = new Color(1, 1, 1);
  static readonly black = new Color(0, 0, 0);
  static readonly gray = new Color(0.5, 0.5, 0.5);
  static readonly darkGray = new Color(0.25, 0.25, 0.25);
  static readonly red = new Color(1, 0, 0);
  static readonly green = new Color(0, 1, 0);
  static readonly yellow = new Color(1, 1, 0);
}
```

**Step 2: Commit**

```bash
git add web/src/core/Color.ts
git commit -m "feat: add Color utility class"
```

---

### Task 4: ConstantStore

**Files:**
- Create: `web/src/core/ConstantStore.ts`
- Reference: `src/core/ConstantStore.java`

**Step 1: Create `web/src/core/ConstantStore.ts`**

Translate the entire ConstantStore.java. This is a large file (~600 lines) containing:

- `StateIdx` enum (MISSOURI, KANSAS_TERRITORY, NEBRASKA_TERRITORY, COLORADO_TERRITORY, DAKOTA_TERRITORY, WASHINGTON_TERRITORY, UTAH_TERRITORY, OREGON)
- All path constants (adapt from `resources/` to web paths: `graphics/`, `sounds/`, `fonts/`)
- `LITERALS` map — all UI strings organized by scene
- `COLORS` map — all color constants using the Color class
- `TOWN_NAMES` map — town names by StateIdx
- `STATE_NAMES` map — display names by StateIdx
- Item property literals (APPLE, BREAD, GUN, AMMO, MEAT, WAGON, etc.)
- `get(outer, inner)` static accessor

Key path changes from Java:
- `resources/graphics/` → `graphics/`
- `resources/sounds/` → `sounds/`
- `resources/fonts/` → `fonts/`

**Step 2: Commit**

```bash
git add web/src/core/ConstantStore.ts
git commit -m "feat: port ConstantStore with all constants, strings, colors, town names"
```

---

### Task 5: FontStore + Bitmap Font Renderer

**Files:**
- Create: `web/src/core/FontStore.ts`
- Create: `web/src/core/BitmapFont.ts`
- Reference: `src/core/FontStore.java`, `resources/fonts/*.fnt`

**Step 1: Create `web/src/core/BitmapFont.ts`**

Parse AngelCode `.fnt` text format. The format looks like:
```
info face="04b03" size=16 ...
common lineHeight=16 base=13 scaleW=256 scaleH=256 ...
char id=32 x=0 y=0 width=0 height=0 xoffset=0 yoffset=13 xadvance=5 ...
char id=33 x=1 y=1 width=3 height=9 xoffset=0 yoffset=4 xadvance=4 ...
```

The BitmapFont class needs:
- `parse(fntText: string)` — parse the .fnt file into a character map
- `texture: HTMLImageElement` — the sprite sheet PNG
- `drawText(ctx, text, x, y)` — render text character by character
- `getWidth(text): number` — measure text width
- `getLineHeight(): number` — return line height

Each character entry stores: `x, y, width, height, xoffset, yoffset, xadvance` from the sprite sheet.

To draw a character: `ctx.drawImage(texture, charX, charY, charW, charH, drawX + xoffset, drawY + yoffset, charW, charH)`

**Step 2: Create `web/src/core/FontStore.ts`**

```ts
export enum FontID {
  H1 = "H1",
  H2 = "H2",
  FIELD = "FIELD",
}
```

Singleton that loads 3 fonts:
- H1: `fonts/04b03_h1.fnt` + `fonts/04b03_h1.png`
- H2: `fonts/04b03_h2.fnt` + `fonts/04b03_h2.png`
- FIELD: `fonts/04b03_field.fnt` + `fonts/04b03_field.png`

`async initialize()` — fetches .fnt text files, loads .png images, creates BitmapFont instances.

`getFont(id: FontID): BitmapFont` — returns the font.

**Step 3: Test by rendering text on canvas**

Temporarily in main.ts, after FontStore loads, draw "Hello Oregon Trail" with the H1 font to verify bitmap font rendering works.

**Step 4: Commit**

```bash
git add web/src/core/BitmapFont.ts web/src/core/FontStore.ts
git commit -m "feat: bitmap font parser and FontStore with AngelCode .fnt support"
```

---

### Task 6: ImageStore

**Files:**
- Create: `web/src/core/ImageStore.ts`
- Reference: `src/core/ImageStore.java`

**Step 1: Create `web/src/core/ImageStore.ts`**

Singleton. `async initialize(onProgress)` loads all images from a manifest array.

The manifest must include every key→path mapping from `ImageStore.java`. The full list is extensive (~250+ entries). Key categories:

- Logo images: `LOGO`, `NULL`, `VOID`
- Backgrounds: `MAP_BACKGROUND`, `TRAIL_MAP`, `DIRT_BACKGROUND`, `SALOON_BACKGROUND`
- Icons: `CAMP_ICON`, `INVENTORY_ICON`, `TRAIL_ICON`, `MAP_POINTER1-6`, `MAP_PARTY1-4`
- Terrain: `GRASS`, `TRAIL`, `HILL_A`, `HILL_B`, `TREE`, `DEER`, `CLOUD_A/B/C`
- Wagon: `TRAIL_WAGON`, `SMALL_WHEEL_1-3`, `BIG_WHEEL_1-3`
- Hunt sprites: `HUNTER_LEFT/RIGHT/FRONT/BACK/UPPERLEFT/UPPERRIGHT/LOWERLEFT/LOWERRIGHT`, `HUNT_RETICLE`
- Hunt prey: `HUNT_PIG[BACK|FRONT|RIGHT|LEFT][1-6]`, `HUNT_COW[BACK|FRONT|RIGHT|LEFT][1-9]`
- Hunt backgrounds: `HUNT_GRASS`, `HUNT_SNOW`, `HUNT_MOUNTAIN`, `HUNT_DESERT`
- Hunt terrain: `HUNT_TREE1[0-F][0|1]`, `HUNT_TREE1[0-F][0|1]SHAD`, `HUNT_ROCK1[0-F][0|1]`, `HUNT_ROCK1[0-F][0|1]SHAD`
- People: `TRAPPER_LEFT/RIGHT`, `MAIDEN_LEFT/RIGHT`, `HILLBILLY_LEFT/RIGHT`
- Buildings: `STORE_BUILDING`, `SALOON_BUILDING`
- Items: `HORSE_ICON`, `MULE_ICON`, `OX_ICON`

Use `Image.FILTER_NEAREST` equivalent: ensure `ctx.imageSmoothingEnabled = false` when drawing these images.

**Step 2: Commit**

```bash
git add web/src/core/ImageStore.ts
git commit -m "feat: port ImageStore with full image manifest"
```

---

### Task 7: Component Base Class

**Files:**
- Create: `web/src/component/Component.ts`
- Reference: `src/component/Component.java` (676 lines)

**Step 1: Create `web/src/component/Component.ts`**

This is the foundation of the entire UI. Port these features from Java:

**Enums:**
- `ReferencePoint` — TOPLEFT, TOPCENTER, TOPRIGHT, CENTERLEFT, CENTERCENTER, CENTERRIGHT, BOTTOMLEFT, BOTTOMCENTER, BOTTOMRIGHT
- `BevelType` — NONE, IN, OUT

**Fields (from Java Component.java):**
- `origin: {x, y}`, `translation: {x, y}`
- `width`, `height`
- `backgroundColor: Color | null`
- `beveled: BevelType`, `bevelWidth: number`
- `borderColor: Color | null`, `topBorderWidth`, `rightBorderWidth`, `bottomBorderWidth`, `leftBorderWidth`
- `components: Component[]` (children)
- `visibleParent: { isVisible(): boolean } | null`
- `mouseOver: boolean`, `visible: boolean`
- `acceptingInput: boolean`
- `listeners: ((source: Component) => void)[]`

**Methods to port:**
- `render(ctx)` — draw background (with bevel logic), draw children, draw borders
- `update(delta)` — propagate to children
- `add(component, location, referencePoint, xOffset?, yOffset?)` — position a child
- `addAsRow(components, location, xOffset, yOffset, spacing)` — layout helpers
- `addAsColumn(components, location, xOffset, yOffset, spacing)`
- `addAsGrid(components, location, rows, cols, xOffset, yOffset, xSpacing, ySpacing)`
- `remove(component)`
- `setPosition(location, referencePoint, xOffset?, yOffset?)` — 9-point positioning math
- `getPosition(referencePoint)` — inverse positioning
- `containsPoint(x, y)` — hit testing
- `getX()`, `getY()` — origin + translation
- All border/bevel/background setters
- `setVisible()`, `isVisible()` — checks parent chain
- `setAcceptingInput(accepting)` — propagates to children
- `addListener(fn)`, `notifyListeners()`
- Mouse event methods: `mouseMoved(x, y)`, `mousePressed(button, x, y)`, `mouseReleased(button, x, y)`

**Bevel rendering logic (critical for authentic look):**
- BevelType.OUT: top/left bars brighter, bottom/right bars darker
- BevelType.IN: top/left bars darker, bottom/right bars brighter
- Inner rectangle filled with base color

**Step 2: Commit**

```bash
git add web/src/component/Component.ts
git commit -m "feat: port Component base class with positioning, bevel, borders, mouse events"
```

---

### Task 8: Panel, Sprite, Label

**Files:**
- Create: `web/src/component/Panel.ts`
- Create: `web/src/component/Sprite.ts`
- Create: `web/src/component/Label.ts`
- Reference: `src/component/Panel.java`, `src/component/sprite/Sprite.java`, `src/component/Label.java`

**Step 1: Create Panel**

Simple extension of Component:
- Optional `backgroundImage: HTMLImageElement`
- Constructor variants: color-based, image-based, full-screen
- `render()`: if has backgroundImage, draw it; otherwise delegate to super

**Step 2: Create Sprite**

- Wraps an `HTMLImageElement`
- `render()`: draws image at component position with `ctx.imageSmoothingEnabled = false`
- Optional rotation support

**Step 3: Create Label**

Port from `Label.java`:
- Fields: `text`, `font: BitmapFont`, `color: Color`, `alignment: Alignment`, `verticalAlignment: VerticalAlignment`
- `Alignment` enum: CENTER, LEFT
- `VerticalAlignment` enum: CENTER, TOP
- `parseLines()` — word-wraps text to fit width (same algorithm as Java: scan by word, new line if exceeds width)
- `render()` — for each line, calculate X (based on alignment) and Y (based on verticalAlignment), draw with BitmapFont
- `setText(text)` — update and re-parse
- `calculateHeight(width, text, font)` — static helper

**Step 4: Commit**

```bash
git add web/src/component/Panel.ts web/src/component/Sprite.ts web/src/component/Label.ts
git commit -m "feat: port Panel, Sprite, Label components"
```

---

### Task 9: Button + ToggleButton

**Files:**
- Create: `web/src/component/Button.ts`
- Create: `web/src/component/ToggleButton.ts`
- Reference: `src/component/Button.java`, `src/component/ToggleButton.java`

**Step 1: Create Button**

Port from `Button.java`:
- Contains optional Label + optional Sprite child
- Color states: `buttonColor`, `buttonActiveColor`, `buttonDisabledColor` (from ConstantStore COLORS)
- Default styling: gray background, OUT bevel (2px), black border (2px)
- `layout()` — positions label/sprite based on what's visible
- State changes: disabled flattens bevel and dims colors; active inverts bevel to IN
- Mouse events: mousePressed sets active, mouseReleased fires listener callback and plays "Click" sound (defer sound to Phase 5, just fire callback for now)

**Step 2: Create ToggleButton**

Extends Button:
- `disableAutoToggle` flag
- mousePressed: toggles active state (or just activates if disableAutoToggle)
- mouseReleased: deactivates if disableAutoToggle, fires listener

**Step 3: Commit**

```bash
git add web/src/component/Button.ts web/src/component/ToggleButton.ts
git commit -m "feat: port Button and ToggleButton with state management"
```

---

### Task 10: InputManager + SceneDirector + Scene Base

**Files:**
- Create: `web/src/core/InputManager.ts`
- Create: `web/src/core/SceneDirector.ts`
- Create: `web/src/scene/Scene.ts`
- Create: `web/src/scene/SceneID.ts`
- Reference: `src/core/SceneDirector.java`, `src/scene/Scene.java`

**Step 1: Create SceneID enum**

```ts
export enum SceneID {
  SPLASH, LOADING, MAINMENU, PARTYCREATION,
  TOWN, STORE, PARTYINVENTORY, TRAIL,
  HUNT, RIVER, MAP, TAVERN,
  OPTIONS, GAMEOVER, VICTORY, SCENESELECTOR,
}
```

**Step 2: Create InputManager**

Listens on canvas for: `mousedown`, `mouseup`, `mousemove`, `keydown`, `keyup`.

Provides a delegate interface that SceneDirector implements:
```ts
interface InputDelegate {
  mousePressed(button: number, x: number, y: number): void;
  mouseReleased(button: number, x: number, y: number): void;
  mouseMoved(x: number, y: number): void;
  keyPressed(key: string, code: string): void;
  keyReleased(key: string, code: string): void;
}
```

Maps DOM key events to a consistent format. Tracks which keys are held.

**Step 3: Create Scene base class**

Port from `Scene.java`:
- 4 layers: `backgroundLayer`, `mainLayer`, `hudLayer`, `modalLayer` — each an array of Components
- `update(delta)` — updates all components in all layers
- `render(ctx)` — renders all layers back-to-front, skip invisible components
- `showModal(modal)` — adds to modalLayer, disables input on lower layers
- `dismissModal(modal)` — removes from modalLayer, re-enables input
- `prepareToEnter()` — lifecycle hook
- Mouse/key event dispatch: walk layers top-down (modal first), find component under cursor

**Step 4: Create SceneDirector**

Port from `SceneDirector.java`:
- `stack: Scene[]`
- `pushScene(scene, popLast, transition?)` — with duplicate check
- `popScene(transition?)`
- `replaceStackWithScene(scene)`
- `currentScene()`
- `update(delta)` / `render(ctx)` — delegates to current scene
- Implements InputDelegate: forwards events to current scene
- Global key handling: ESC for options, +/- for debug (match Java)

Transitions deferred to Phase 5 (just instant-switch for now).

**Step 5: Commit**

```bash
git add web/src/core/InputManager.ts web/src/core/SceneDirector.ts web/src/scene/Scene.ts web/src/scene/SceneID.ts
git commit -m "feat: port InputManager, SceneDirector, Scene base with 4-layer rendering"
```

---

### Task 11: ConditionBar

**Files:**
- Create: `web/src/component/ConditionBar.ts`
- Reference: `src/component/ConditionBar.java`

**Step 1: Create ConditionBar**

Used for health bars and the loading progress bar.
- Fields: `current`, `max`, `barColor` (green/yellow/red based on percentage)
- `render()`: draw background, then filled portion at `(current/max) * width`
- `setCondition(current, max)` — updates and recalculates color
- Label showing percentage text (optional)

**Step 2: Commit**

```bash
git add web/src/component/ConditionBar.ts
git commit -m "feat: port ConditionBar component"
```

---

### Task 12: AnimatingColor Utility

**Files:**
- Create: `web/src/core/AnimatingColor.ts`

**Step 1: Create AnimatingColor**

Used for sky color transitions in TrailScene and TownScene.
- Linearly interpolates between two Colors over a duration
- `update(delta)` — advances interpolation
- `getCurrentColor(): Color`
- `setTarget(color, durationMs)` — start new animation
- `isAnimating(): boolean`

**Step 2: Commit**

```bash
git add web/src/core/AnimatingColor.ts
git commit -m "feat: add AnimatingColor for smooth color transitions"
```

---

### Task 13: SplashScene + LoadingScene

**Files:**
- Create: `web/src/scene/SplashScene.ts`
- Create: `web/src/scene/LoadingScene.ts`
- Reference: `src/scene/SplashScene.java`, `src/scene/LoadingScene.java`

**Step 1: Create SplashScene**

Port from SplashScene.java:
- Two panels with AnimatingColor (black→white transition)
- Two logo sprites (null, void)
- 4-stage animation over 3000ms
- Auto-advances to LoadingScene after WAIT_TIME
- No user interaction

**Step 2: Create LoadingScene**

Port from LoadingScene.java:
- Shows "Loading..." title label
- ConditionBar for progress
- Label showing current resource being loaded
- On enter: kicks off ImageStore.initialize() and FontStore.initialize() with progress callback
- When complete: transitions to MainMenuScene

**Step 3: Wire up GameDirector + main.ts**

Create a minimal `web/src/core/GameDirector.ts` that:
- Gets canvas + context
- Creates SceneDirector
- Creates InputManager
- Starts with SplashScene
- Runs the game loop (`requestAnimationFrame`)

Update `web/src/main.ts` to create and start GameDirector.

**Step 4: Verify**

Run `npx vite`. Expected: splash animation plays, loading screen shows progress bar filling, transitions to a blank scene (MainMenuScene placeholder).

**Step 5: Commit**

```bash
git add web/src/scene/SplashScene.ts web/src/scene/LoadingScene.ts web/src/core/GameDirector.ts web/src/main.ts
git commit -m "feat: splash screen, loading screen, GameDirector game loop"
```

---

## Phase 2: Component Framework + Menus

### Task 14: Modal System

**Files:**
- Create: `web/src/component/modal/Modal.ts`
- Create: `web/src/component/modal/MessageModal.ts`
- Create: `web/src/component/modal/ChoiceModal.ts`
- Create: `web/src/component/modal/ComponentModal.ts`
- Reference: `src/component/modal/Modal.java`, etc.

**Step 1: Create Modal base**

Port from Modal.java:
- Full-screen component (1024x576)
- Translucent overlay panel (TRANSLUCENT_OVERLAY color: rgba(0,0,0,0.25))
- Inner panel with MODAL background color, MODAL_BORDER, 2px border
- Message label (DEFAULT_LABEL_WIDTH = 500)
- Button array at bottom
- `onDismiss: (buttonIndex: number) => void` callback (replaces ModalListener)
- ENTER key dismisses with index -1

**Step 2: Create MessageModal**

Single OK button. Panel sized to fit message + button.

**Step 3: Create ChoiceModal**

Variable number of buttons. Cancel button index = 0.

**Step 4: Create ComponentModal\<T\>**

Generic modal wrapping a custom component between message and buttons. Panel sized to fit all three.

**Step 5: Commit**

```bash
git add web/src/component/modal/
git commit -m "feat: port Modal, MessageModal, ChoiceModal, ComponentModal"
```

---

### Task 15: TextField, SegmentedControl, Counter

**Files:**
- Create: `web/src/component/TextField.ts`
- Create: `web/src/component/SegmentedControl.ts`
- Create: `web/src/component/Counter.ts`
- Reference: corresponding Java files

**Step 1: Create TextField**

Port from TextField.java:
- Contains inner Label for displaying text
- Focus state changes colors (fieldColor ↔ fieldFocusColor)
- `keyReleased()`: when focused, handle BACKSPACE (delete char), ENTER (lose focus), printable chars (append if accepted and fits)
- `AcceptedCharacters` enum: LETTERS, LETTERS_NUMBERS, NUMBERS
- Placeholder text support
- `mouseReleased()`: gain focus on click

**Step 2: Create SegmentedControl**

Port from SegmentedControl.java:
- Array of ToggleButtons in a grid layout
- `selection` boolean array, `singleSelection` index
- `requireSelection` flag, `maxSelected` count
- `permanent` array (locked selections)
- Click handler: toggles selection, updates button active states, notifies listeners
- `getSelection(): number[]`
- `setSelection(indices: number[])`

**Step 3: Create Counter**

Port from Counter.java:
- Contains internal CountingButton (extended Button)
- `count`, `min`, `max` values
- Left click increases, right click decreases (configurable via `countUpOnLeftClick`)
- Alt+click changes by 10
- Red count badge label at top-right
- `disableAutoCount` for manual control
- `hideCount` option

**Step 4: Commit**

```bash
git add web/src/component/TextField.ts web/src/component/SegmentedControl.ts web/src/component/Counter.ts
git commit -m "feat: port TextField, SegmentedControl, Counter components"
```

---

### Task 16: Enums and Small Model Classes

**Files:**
- Create: `web/src/model/Profession.ts`
- Create: `web/src/model/Skill.ts`
- Create: `web/src/model/Condition.ts`
- Create: `web/src/model/Notification.ts`
- Reference: corresponding Java files

**Step 1: Create Skill enum**

From Skill.java — 14 values with cost and name:
```
MEDICAL(50), RIVERWORK(50), SHARPSHOOTING(50), BLACKSMITHING(40),
CARPENTRY(40), FARMING(40), TRACKING(30), BOTANY(20),
COMMERCE(20), COOKING(20), MUSICAL(10), SEWING(10), SPANISH(10), NONE(0)
```

**Step 2: Create Profession enum**

From Profession.java — 25 professions with moneyDivider, startingSkill, name, startingItem:
```
BANKER(1, COMMERCE), DOCTOR(1.2, MEDICAL), ... TEACHER(5, NONE)
```
BASE_MONEY = 1600. `getMoney()` = floor(BASE_MONEY / moneyDivider).

**Step 3: Create Condition class**

From Condition.java:
- `min`, `max`, `current` (number fields)
- `increase(amount)` — caps at max
- `decrease(amount)` — caps at min
- `getPercentage()` — (current - min) / (max - min)
- `copy()` — returns new Condition with same values

**Step 4: Create Notification class**

From Notification.java:
- `message: string`
- `isModal: boolean`

**Step 5: Commit**

```bash
git add web/src/model/
git commit -m "feat: port Skill, Profession, Condition, Notification models"
```

---

### Task 17: Person + Player Models

**Files:**
- Create: `web/src/model/Person.ts`
- Create: `web/src/model/Player.ts`
- Create: `web/src/model/Inventory.ts`
- Create: `web/src/model/Item.ts`
- Create: `web/src/model/ItemType.ts`
- Reference: corresponding Java files

**Step 1: Create ItemType**

From ItemType.java — 16 enum values. Each has properties loaded from ConstantStore LITERALS:
- name, pluralName, description, cost, weight
- Flags: isFood, isPlant, isAnimal, isTool
- factor (foodFactor or repairFactor), necessaryQuality

Values: APPLE, BREAD, AMMO, GUN, MEAT, SONIC, WAGON, WHEEL, OX, TOOLS, AXLE, HORSE, MULE, STRANGEMEAT, MAP, TRADEGOODS

**Step 2: Create Item**

From Item.java:
- `status: Condition` (starts at 100)
- `type: ItemType`
- `getCost()` — type.cost * status.percentage
- `getWeight()` — type.weight
- Comparable by condition percentage

**Step 3: Create Inventory**

From Inventory.java:
- Slots: one array per ItemType (items sorted by condition, poorest first)
- `MAX_SIZE`, `MAX_WEIGHT` constraints
- `addItemsToInventory(items)`, `removeItemFromInventory(type, quantity)`
- `canGetItems(type, count)` — checks weight and size
- `getPopulatedSlots()` — returns ItemTypes that have items
- `getNumberOf(type)`, `getWeight()`, `isEmpty()`, `isFull()`

Use a sorted array instead of Java's PriorityQueue — sort by condition ascending.

**Step 4: Create Person**

From Person.java:
- Constants: BASE_SKILL_POINTS = 70, MAX_INVENTORY_SIZE = 5, MAX_INVENTORY_WEIGHT = 100
- Fields: name, profession, skills[], health (Condition at 100), inventory, weight (random 90-190), isMale, isLeader, dead
- `setProfession(p)` — sets profession, adds starting skill
- `addSkill(s)`, `removeSkill(s)`, `buySkill(s)` (deducts skill points)
- `killForFood()` — returns weight/STRANGEMEAT.weight / 2 items of STRANGEMEAT
- `makeRandom()` — random profession, skills, stats

**Step 5: Create Player**

From Player.java — extends Person with `party: Party` reference.

**Step 6: Commit**

```bash
git add web/src/model/
git commit -m "feat: port ItemType, Item, Inventory, Person, Player models"
```

---

### Task 18: MainMenuScene

**Files:**
- Create: `web/src/scene/MainMenuScene.ts`
- Reference: `src/scene/MainMenuScene.java`

**Step 1: Create MainMenuScene**

Port from MainMenuScene.java:
- **backgroundLayer**: Panel with TRAIL_MAP background image
- **mainLayer**:
  - Logo sprite (480px width, centered near top)
  - 4 buttons in a row near bottom: "New Game", "Load", "Options", "Quit"
  - Each button uses FIELD font, white label
- **Interactions**:
  - New Game → request PartyCreationScene
  - Load → show load modal (defer to Phase 4, disable button for now)
  - Options → request OptionsScene (defer to later, just log for now)
  - Quit → not applicable for web (hide or remove this button)

**Step 2: Verify**

Run the app. After loading, MainMenuScene should show the map background, logo, and 4 buttons. Clicking "New Game" should transition (even if PartyCreationScene is a placeholder).

**Step 3: Commit**

```bash
git add web/src/scene/MainMenuScene.ts
git commit -m "feat: port MainMenuScene with logo and menu buttons"
```

---

### Task 19: PartyCreationScene

**Files:**
- Create: `web/src/scene/PartyCreationScene.ts`
- Reference: `src/scene/PartyCreationScene.java`

This is a complex scene. Port from PartyCreationScene.java:

**Step 1: Layout**

- **backgroundLayer**: Panel with DIRT_BACKGROUND
- **mainLayer**: 4 person columns + bottom controls

**Each person column (x4):**
- "New Person" button (starts visible)
- TextField for name (hidden until "New Person" clicked)
- SegmentedControl for gender (Male/Female) (hidden until name entered)
- Profession panel with button + label (hidden until gender selected)
- Money label
- Skills panel with button + 3 skill labels (hidden until profession chosen)
- Delete "X" button (top-right of column)

**Bottom row:**
- Rations SegmentedControl: "Barebones", "Meager", "Filling"
- Pace SegmentedControl: "Steady", "Strenuous", "Grueling"
- "Confirm" button

**Step 2: Interaction flow**

1. Click "New Person" → show TextField
2. Enter name, press Enter → show gender selector
3. Select gender → show profession button
4. Click profession → show profession ChoiceModal (5x5 grid of 25 professions with money/starting skill tooltips)
5. Select profession → show skills panel
6. Click skills → show skill ComponentModal (SegmentedControl with 13 skills, max 3 selected, profession starter is permanent)
7. All filled → person complete
8. Click "Confirm" → validate:
   - At least 1 member
   - All have professions
   - All have 3 skills
   - No duplicate names
9. On success: create Party with pace, rations, members, Time → set on Player → transition to TownScene

**Step 3: Commit**

```bash
git add web/src/scene/PartyCreationScene.ts
git commit -m "feat: port PartyCreationScene with full party creation flow"
```

---

### Task 20: GameDirector Scene Factory

**Files:**
- Modify: `web/src/core/GameDirector.ts`
- Reference: `src/core/GameDirector.java`

**Step 1: Expand GameDirector**

Port the full scene factory from GameDirector.java:

- Create `Game` model (holds Player, WorldMap, store Inventory)
- `sceneForSceneID(id, lastScene)` factory method — creates the right scene with the right constructor args
- `requestScene(id, lastScene, popLast)` — the main scene transition method:
  - Updates worldMap current location/trail
  - Victory check (if location rank == max rank → VictoryScene)
  - Special cases: PartyInventory from Store (pass store inventory), after PartyCreation (set location to mapHead)
  - Pushes scene onto SceneDirector
- `resetToMainMenu()` — clears stack, creates new Game
- Save/load stubs (implement in Phase 4)

**Step 2: Commit**

```bash
git add web/src/core/GameDirector.ts
git commit -m "feat: expand GameDirector with scene factory and game state management"
```

---

## Phase 3: Trail + Towns

### Task 21: Time, WorldMap, LocationNode, TrailEdge Models

**Files:**
- Create: `web/src/model/Time.ts`
- Create: `web/src/model/WorldMap.ts`
- Create: `web/src/model/LocationNode.ts`
- Create: `web/src/model/TrailEdge.ts`
- Create: `web/src/model/MapObject.ts`
- Reference: corresponding Java files

**Step 1: Create Time**

From Time.java:
- Month enum with 12 values (days per month, display name)
- TimeOfDay enum: MORNING (7-12), AFTERNOON (13-18), EVENING (19-0), NIGHT (1-6)
- Constructor: random start 1860-1870
- `advanceTime()` — increment hour, handle day/month/year rollover, leap years
- `get12HourTime()`, `getDayMonthYear()`

**Step 2: Create MapObject abstract class**

From MapObject.java:
- `visible`, `quality: Condition`, `name`, `territory: StateIdx`
- Static count for ID generation

**Step 3: Create LocationNode**

From LocationNode.java (extends MapObject):
- `rank`, `trails` count, `outboundTrails: TrailEdge[]`
- `MAP_XPOS`, `MAP_YPOS` (for map display)
- `playerMapX`, `playerMapY`
- `addTrail(edge)`, `getOutboundTrails()`

**Step 4: Create TrailEdge**

From TrailEdge.java (extends MapObject):
- `destination`, `origin` (LocationNodes)
- `dangerLevel`, `length` (calculated from distance)
- `advance(movement)` — decreases quality (remaining distance)
- `getDistanceToGo()`, `getRoughDistanceToGo()` (verbal: "Very Short" to "Endless")
- `getRoughDirection()` (N/S/W/NW/SW based on dest vs origin coords)
- `getDangerRating()` (verbal scale)
- `getCurrTrailLocationX/Y()` — interpolated position on map

**Step 5: Create WorldMap**

From WorldMap.java:
- `generateMap(numLocations)` — procedural generation:
  - Creates nodes at ranks 0 to MAX_RANK (25)
  - Each node gets 1-3 outbound trails to higher-rank nodes
  - Head node: "Independence" in Missouri
  - Final: "Oregon City" in Oregon
  - Territory assignment based on coordinates
  - Town naming from ConstantStore.TOWN_NAMES
- `getMapHead()`, `getNextRankOrphanLocation()`
- `makePCInboundTrail(destNode)` — trailblazing
- `setVisibleByArea(stateIdx)` — reveals map section

**Step 6: Commit**

```bash
git add web/src/model/
git commit -m "feat: port Time, WorldMap, LocationNode, TrailEdge with procedural map generation"
```

---

### Task 22: Animal, Vehicle, Party Models

**Files:**
- Create: `web/src/model/Animal.ts`
- Create: `web/src/model/Vehicle.ts`
- Create: `web/src/model/Party.ts`
- Create: `web/src/model/Game.ts`
- Reference: corresponding Java files

**Step 1: Create Animal**

From Animal.java (extends Item):
- `moveFactor` from ConstantStore (OX: 2.0, MULE: 3.0, HORSE: 4.0)
- `dead: boolean`
- `killForFood()` → returns weight / MEAT.weight / 10 items of MEAT

**Step 2: Create Vehicle**

From Vehicle.java (extends Item, implements Inventoried-like):
- `cargo: Inventory` with max size/weight from ConstantStore (WAGON: 10 slots, 3500 weight)
- `repair(amount)` — increases status

**Step 3: Create Party**

From Party.java (~880 lines). This is the core gameplay class.

Key nested types:
- `Pace` enum: STEADY(30), STRENUOUS(55), GRUELING(80)
- `Rations` enum: FILLING(100), MEAGER(75), BAREBONES(50)

Critical methods:
- `walk(): Notification[]` — THE core gameplay method:
  1. Calculate movement = (pace.speed * moveModifier) / 30
  2. Advance trail
  3. Graze animals (health based on pace + wagon weight)
  4. For each member: increase skills, eat food, adjust health
  5. Process deaths (kill for food, remove from party)
  6. Return notifications
- `getMoveModifier()` — sum animal move factors / 10, clamped 1-5
- `eatFood(person, amount)` — recursive food consumption from person/vehicle inventory
- `grazeAnimals()` — animal health based on strain
- `rest(): Notification[]` — passive healing
- `buyItemForInventory(items, buyer)` — purchase flow

**Step 4: Create Game**

From Game.java:
- `player: Player`, `worldMap: WorldMap`, `storeInventory: Inventory`
- `resetStoreInventory(location)` — populates store based on location rank/quality
- `reset()`

**Step 5: Commit**

```bash
git add web/src/model/
git commit -m "feat: port Animal, Vehicle, Party (with walk()), Game models"
```

---

### Task 23: Parallax System

**Files:**
- Create: `web/src/component/parallax/ParallaxPanel.ts`
- Create: `web/src/component/parallax/ParallaxComponent.ts`
- Create: `web/src/component/parallax/ParallaxComponentLoop.ts`
- Reference: `src/component/parallax/` Java files

**Step 1: Create ParallaxComponent**

Extends Sprite:
- `distance: number` — affects scroll speed (far = slow, near = fast)
- `scrollX: number` — current scroll offset
- `update(delta, speed)` — advance scrollX based on distance and speed
- `render(ctx)` — draw image at position offset by scrollX

**Step 2: Create ParallaxComponentLoop**

Extends ParallaxComponent:
- Tiles the image horizontally so it repeats seamlessly
- `render(ctx)` — draws image multiple times to fill viewport, wrapping at image width

**Step 3: Create ParallaxPanel**

Container that holds ParallaxComponents sorted by distance (far to near).
- `maxDistance: number` — overall speed multiplier
- `update(delta)` — updates all children with speed based on distance ratio
- `addComponent(component, distance)`
- `pause()` / `resume()`

**Step 4: Commit**

```bash
git add web/src/component/parallax/
git commit -m "feat: port parallax scrolling system"
```

---

### Task 24: AnimatingSprite + WagonSprite

**Files:**
- Create: `web/src/component/sprite/AnimatingSprite.ts`
- Create: `web/src/component/sprite/WagonSprite.ts`
- Reference: `src/component/sprite/AnimatingSprite.java`, `src/component/sprite/WagonSprite.java`

**Step 1: Create AnimatingSprite**

From AnimatingSprite.java:
- Array of animation frames (HTMLImageElement[])
- Multiple animation sets (e.g., left-facing, right-facing)
- `currentFrame` index, `frameTimer`
- `update(delta)` — advance frame timer, cycle frames
- `setAnimation(name)` — switch animation set
- `render(ctx)` — draw current frame

**Step 2: Create WagonSprite**

From WagonSprite.java:
- Composite: wagon body image + 2 small wheels + 2 big wheels
- Wheel rotation animation (3 frames each, cycling)
- `update(delta)` — advance wheel frame timers
- `render(ctx)` — draw wagon body, draw wheels at correct positions

**Step 3: Commit**

```bash
git add web/src/component/sprite/
git commit -m "feat: port AnimatingSprite and WagonSprite"
```

---

### Task 25: HUD Components

**Files:**
- Create: `web/src/component/hud/HUD.ts`
- Create: `web/src/component/hud/TrailHUD.ts`
- Create: `web/src/component/hud/TownHUD.ts`
- Reference: `src/component/hud/` Java files

**Step 1: Create HUD base**

Container component that provides a toolbar area with buttons and info labels. Common layout for all scene HUDs.

**Step 2: Create TrailHUD**

From TrailScene.java context:
- Top bar: camp icon + location label + time/date labels
- Second bar: Pace SegmentedControl ("Steady"/"Strenuous"/"Grueling") + Rations SegmentedControl ("Filling"/"Meager"/"Barebones")
- Notifications display area
- Connects to Party model for pace/rations changes

**Step 3: Create TownHUD**

From TownScene.java context:
- Top bar: tree icon + location name label + time/date labels
- Instruction text (e.g., "Press Enter to Go Inside Store")
- Trail button

**Step 4: Commit**

```bash
git add web/src/component/hud/
git commit -m "feat: port HUD, TrailHUD, TownHUD components"
```

---

### Task 26: RandomEncounterTable

**Files:**
- Create: `web/src/scene/encounter/RandomEncounterTable.ts`
- Create: `web/src/scene/encounter/Encounter.ts`
- Reference: `src/scene/encounter/` Java files

**Step 1: Research encounter types**

Read the Java encounter files to understand all encounter types and their effects.

**Step 2: Create Encounter classes**

Each encounter has:
- Probability (5-50, random)
- `execute(party): Notification` — applies effect and returns message
- Types include: thieves (steal items/money), pothole (damage wagon), found items, river crossing trigger, illness, weather events

**Step 3: Create RandomEncounterTable**

- List of Encounters with probabilities
- `roll(): Encounter | null` — random roll against probabilities
- Called every update cycle in TrailScene

**Step 4: Commit**

```bash
git add web/src/scene/encounter/
git commit -m "feat: port random encounter system"
```

---

### Task 27: TrailScene

**Files:**
- Create: `web/src/scene/TrailScene.ts`
- Reference: `src/scene/TrailScene.java`

This is the most complex scene. Port from TrailScene.java:

**Step 1: Layer setup**

- **backgroundLayer**: Sky panel (AnimatingColor), ParallaxPanel (hills, ground, trail, clouds, trees, deer)
- **mainLayer**: PartyMemberGroup sprites (walking animation), VehicleGroup (wagon + wheels)
- **hudLayer**: TrailHUD, toolbar with buttons (Camp, Inventory, Map, Hunt, Leave)

**Step 2: State machine**

Two states:
- **Walking**: parallax scrolling, party walks, encounters checked
- **Camp**: parallax stopped, shows camp menu

**Step 3: Update logic (every ~1000ms tick)**

1. Call `party.walk()` → get Notifications
2. Advance time (every 2 ticks)
3. Update sky color based on time of day
4. Handle notifications (show modals, trigger encounters)
5. Check trail completion (distanceToGo <= 0) → transition to TownScene
6. Check game over (leader dead or all dead) → GameOverScene
7. Randomly spawn clouds/trees/deer as parallax elements

**Step 4: Party sprite animation**

- Each party member has a walking sprite that bobs up/down
- Sprites positioned along bottom of screen
- Wagon to the right of party members

**Step 5: Toolbar interactions**

- Camp button → enter CampState (stops walking, shows camp options)
- Inventory → request PartyInventoryScene
- Map → request MapScene
- Hunt → request HuntScene (requires gun in inventory)
- Leave/Continue → resume walking from camp

**Step 6: Commit**

```bash
git add web/src/scene/TrailScene.ts
git commit -m "feat: port TrailScene with parallax, walking, encounters, camp"
```

---

### Task 28: TownScene

**Files:**
- Create: `web/src/scene/TownScene.ts`
- Reference: `src/scene/TownScene.java`

**Step 1: Port TownScene**

- **backgroundLayer**: Sky panel + ParallaxPanel (hills, ground)
- **mainLayer**: Store building sprite, Saloon building sprite, party leader AnimatingSprite (walks left/right)
- **hudLayer**: TownHUD with location name, time, instruction text, trail button

**Step 2: Interactions**

- Arrow keys move party leader sprite left/right
- Enter key near store → StoreScene
- Enter key near saloon → TavernScene
- Trail button → ChoiceModal showing available trails:
  - Each trail: destination name, rough length, direction, danger
  - Optional "Trailblaze" if party has TRACKING skill
  - Select trail → set party.trail → request TrailScene

**Step 3: Time advance**

- Time advances every STEP_COUNT_TRIGGER (20) steps
- Sky color updates with time

**Step 4: Commit**

```bash
git add web/src/scene/TownScene.ts
git commit -m "feat: port TownScene with buildings, movement, trail selection"
```

---

### Task 29: StoreScene

**Files:**
- Create: `web/src/scene/StoreScene.ts`
- Reference: `src/scene/StoreScene.java`

**Step 1: Port StoreScene**

- **backgroundLayer**: Blue panel (0x0C5DA5)
- **mainLayer**:
  - 4x4 grid of Counter buttons (one per item in store inventory, showing sprite + quantity)
  - Right panel: item name, description, weight, cost (with price modifier), quantity, total weight, total cost
  - Party money label
  - Buttons: Leave, Inventory, Buy, Clear

**Step 2: Item selection**

- Click a Counter → select that item type
- Counter click increments/decrements desired quantity
- Right panel updates with selected item details
- Price modifier = 1 + (location rank / 10)

**Step 3: Purchase flow**

- Buy button → show modal with party members who can carry the item
- Select recipient → deduct money, add items to recipient's inventory
- Special cases: wagon (max 1), animals (max 6 total), map (immediate state reveal)

**Step 4: Commit**

```bash
git add web/src/scene/StoreScene.ts
git commit -m "feat: port StoreScene with item grid, purchasing, price modifiers"
```

---

### Task 30: TavernScene

**Files:**
- Create: `web/src/scene/TavernScene.ts`
- Reference: `src/scene/TavernScene.java`

**Step 1: Port TavernScene**

- **backgroundLayer**: Saloon background image
- **mainLayer**: 4 random NPC buttons with sprites above them + Leave button

**Step 2: NPC generation**

- Generate 4 random Persons with `makeRandom()`
- Display name buttons with character sprites (male: hillbilly, female: maiden)

**Step 3: Recruitment**

- Click NPC → detail modal showing name, profession, skills
- Confirm → add to party (if < 4 members)
- Party full → error modal

**Step 4: Commit**

```bash
git add web/src/scene/TavernScene.ts
git commit -m "feat: port TavernScene with NPC recruitment"
```

---

### Task 31: PartyInventoryScene

**Files:**
- Create: `web/src/scene/PartyInventoryScene.ts`
- Reference: `src/scene/PartyInventoryScene.java`

**Step 1: Port PartyInventoryScene**

- **backgroundLayer**: Purple panel (0x3b2d59)
- **mainLayer**: Person inventory panels (one per member + vehicle), bin area, action buttons

**Step 2: Inventory display**

For each person/vehicle:
- Name, health bar, weight/capacity
- Grid of item Counters (one per populated item type)
- Click item → move 1 to bin (Shift+click → move all)

**Step 3: Actions**

- Transfer: click person to give bin contents to
- Sell/Drop: sell bin contents (get money) or discard
- Close: return to previous scene

**Step 4: Animal display**

- Horse/Ox/Mule counts with icons

**Step 5: Commit**

```bash
git add web/src/scene/PartyInventoryScene.ts
git commit -m "feat: port PartyInventoryScene with transfer, sell, drop"
```

---

### Task 32: MapScene

**Files:**
- Create: `web/src/scene/MapScene.ts`
- Create: `web/src/component/MapComponent.ts`
- Reference: `src/scene/MapScene.java`, `src/component/MapComponent.java`

**Step 1: Create MapComponent**

Draws location nodes on the map image:
- Each location → small colored button (size/color by rank)
- Current position → animated pointer sprite
- Only shows visible locations

**Step 2: Port MapScene**

- **backgroundLayer**: Trail map image
- **mainLayer**: MapComponent, legend, "Return to Camp" button
- Legend shows rank tiers with color coding
- Animated pointer (6 frames) and party sprite (4 frames) at current position

**Step 3: Commit**

```bash
git add web/src/scene/MapScene.ts web/src/component/MapComponent.ts
git commit -m "feat: port MapScene with location markers, legend, animated pointer"
```

---

## Phase 4: Minigames + Endgame

### Task 33: HuntScene

**Files:**
- Create: `web/src/scene/HuntScene.ts`
- Create: `web/src/component/HuntingGroundsComponent.ts`
- Create: `web/src/component/sprite/HunterAnimatingSprite.ts`
- Create: `web/src/component/sprite/PreyAnimatingSprite.ts`
- Create: `web/src/component/hud/HuntHUD.ts`
- Reference: `src/scene/HuntScene.java` and related files

**Step 1: Create HuntingGroundsComponent**

Large (2400x2400) scrollable area:
- Tile-based terrain (trees, rocks with shadows)
- Terrain varies by territory: GRASS, SNOW, MOUNTAIN, DESERT
- Viewport scrolling (hunter stays centered, map moves)
- Collision detection helpers

**Step 2: Create HunterAnimatingSprite**

8-directional sprite:
- Uses HUNTER_LEFT, HUNTER_RIGHT, HUNTER_FRONT, HUNTER_BACK, HUNTER_UPPERLEFT, etc.
- Direction changes with WASD/arrow input

**Step 3: Create PreyAnimatingSprite**

Cows (9 frames per direction) and Pigs (6 frames per direction):
- Flee AI: move away when hunter within detection range
- Random direction changes
- Collision with terrain obstacles

**Step 4: Create HuntHUD**

- Ammo display: "Current Ammo: X bullets and Y extra boxes"
- Camp button, Inventory button

**Step 5: Port HuntScene**

- Spawn 3-6 prey (random mix of cows/pigs)
- WASD movement for hunter (8 directions, move every 3 ticks)
- Right-click to cock gun, left-click to fire
- Hit detection: check if shot line intersects prey hitbox
- Kill → drop meat → add to vehicle inventory
- Sounds: deferred to Phase 5

**Step 6: Commit**

```bash
git add web/src/scene/HuntScene.ts web/src/component/HuntingGroundsComponent.ts web/src/component/sprite/HunterAnimatingSprite.ts web/src/component/sprite/PreyAnimatingSprite.ts web/src/component/hud/HuntHUD.ts
git commit -m "feat: port HuntScene with 8-dir movement, prey AI, shooting"
```

---

### Task 34: RiverScene

**Files:**
- Create: `web/src/scene/RiverScene.ts`
- Reference: `src/scene/RiverScene.java`

**Step 1: Port RiverScene**

- **backgroundLayer**: Sky panel, parallax water (scrolling), mountains, clouds
- **mainLayer**: Wagon sprite (animated), bridge sprite (conditional)

**Step 2: Choice modal**

On enter, show ChoiceModal:
- **Ford** (always available): danger scales with river depth (random 1-8 feet)
- **Caulk** (always available): danger if depth >= 4
- **Pay toll** (if enough money): cost = random(1-20) * (rank+1), safe crossing
- **Wait** (once): re-roll river depth

**Step 3: Crossing animation**

- Ford/Caulk: wagon bobs in water for 3 seconds
- Bridge: wagon rolls across for 5.5 seconds
- If damage taken: splash effect, damage to vehicle/party

**Step 4: Outcome**

- Calculate damage (random, based on depth and method)
- Apply to vehicle and party members
- Show result notification
- Transition back to TrailScene

**Step 5: Commit**

```bash
git add web/src/scene/RiverScene.ts
git commit -m "feat: port RiverScene with crossing options, animation, damage"
```

---

### Task 35: GameOver + Victory Scenes

**Files:**
- Create: `web/src/scene/GameOverScene.ts`
- Create: `web/src/scene/VictoryScene.ts`
- Reference: corresponding Java files

**Step 1: Create GameOverScene**

Simple scene:
- Black background
- "Game Over" label (red, H1 font, centered)

**Step 2: Create VictoryScene**

Simple scene:
- Black background
- "Victory!" label (green, H1 font, centered)

**Step 3: Commit**

```bash
git add web/src/scene/GameOverScene.ts web/src/scene/VictoryScene.ts
git commit -m "feat: port GameOver and Victory scenes"
```

---

### Task 36: OptionsScene + Save/Load

**Files:**
- Create: `web/src/scene/OptionsScene.ts`
- Modify: `web/src/core/GameDirector.ts` (add save/load)
- Reference: `src/scene/OptionsScene.java`, `src/core/GameDirector.java`

**Step 1: Create OptionsScene**

- Black background
- "Options" title (H1)
- Volume spinner (0-100 by 10s) — wired to SoundStore in Phase 5, just UI for now
- Save button → show save slot modal (5 slots)
- Main Menu button → GameDirector.resetToMainMenu()
- Back button → pop scene

**Step 2: Implement save/load in GameDirector**

Save:
```ts
const saveData = game.toJSON();
localStorage.setItem(`oregon-trail-save-${slot}`, JSON.stringify(saveData));
```

Load:
```ts
const data = JSON.parse(localStorage.getItem(`oregon-trail-save-${slot}`)!);
game = Game.fromJSON(data);
// Determine which scene to show based on game state
```

Add `toJSON()` and `fromJSON()` to all model classes: Game, Party, Person, Player, Inventory, Item, WorldMap, LocationNode, TrailEdge, Time, Condition, Animal, Vehicle.

WorldMap deserialization needs to reconstruct node/edge references by ID.

**Step 3: Wire up MainMenuScene load button**

Check localStorage for existing saves, enable/disable load button accordingly.

**Step 4: Commit**

```bash
git add web/src/scene/OptionsScene.ts web/src/core/GameDirector.ts web/src/model/
git commit -m "feat: port OptionsScene, save/load with localStorage"
```

---

## Phase 5: Audio + Polish

### Task 37: SoundStore

**Files:**
- Create: `web/src/core/SoundStore.ts`
- Reference: `src/core/SoundStore.java`

**Step 1: Create SoundStore**

Singleton using Web Audio API.

`AudioContext` created on first user interaction (splash screen click).

**Loading:**
- `async initialize(onProgress)` — fetch all 30 OGG files as ArrayBuffer, decode to AudioBuffer
- Store in `sounds: Map<string, AudioBuffer>` and `musics: Map<string, AudioBuffer>`

**Playback infrastructure:**
- `musicGain: GainNode` — master music volume
- `sfxGain: GainNode` — master SFX volume
- `globalVolume: number` (0-1)
- `currentMusic: AudioBufferSourceNode | null`

**Music methods:**
- `loopMusic(name)` — stop current, create new source, loop=true, connect to musicGain
- `playMusic(name, volume?)` — play once
- `stopMusic()` — stop current music
- `playTownMusic()` — random from town playlist
- `playHuntMusic()` — loop "KnightsOfCydonia"

Town playlist: ["FarewellCheyenne", "HangEmHigh", "HowTheWest", "JesseJames", "MyNameIsNobody", "FFD", "MS"]

**SFX methods:**
- `playSound(name, volume?)` — create source, connect to sfxGain, play once
- `loopSound(name)` — create source, loop=true
- `stopSound(name)` — stop specific sound
- `stop()` — stop all

**Volume:**
- `setVolume(v)` — set globalVolume, update gain nodes
- `getVolume()`

**Full sound manifest** (from SoundStore.java):
- Music: GBU, River, DayTheme, NightTheme, FFD, MS, FarewellCheyenne, HangEmHigh, HowTheWest, JesseJames, MyNameIsNobody, WanderingTrail, KnightsOfCydonia, Crackling Fire
- SFX: Smooth, Steps, Shot, Ricochet, GunCock, Click, ItemGet, WolfHowl, Rooster, Splash, RK, CowMoo, Donkey, HorseWhinny, PigSqueal, CowSqueal

**Step 2: Update LoadingScene to also load sounds**

Add SoundStore loading after images/fonts, update progress bar.

**Step 3: Commit**

```bash
git add web/src/core/SoundStore.ts
git commit -m "feat: port SoundStore with Web Audio API, full sound manifest"
```

---

### Task 38: Wire Audio Into All Scenes

**Files:**
- Modify: `web/src/scene/MainMenuScene.ts` — loop "GBU" on enter
- Modify: `web/src/scene/TownScene.ts` — playTownMusic() on enter
- Modify: `web/src/scene/TrailScene.ts` — DayTheme/NightTheme based on time, Steps sound while walking, WolfHowl at 7pm, animal sounds based on party
- Modify: `web/src/scene/HuntScene.ts` — playHuntMusic(), Shot/Ricochet/GunCock/PigSqueal/CowSqueal
- Modify: `web/src/scene/RiverScene.ts` — River music, Splash on damage
- Modify: `web/src/component/Button.ts` — play "Click" on mouseReleased
- Modify: `web/src/scene/OptionsScene.ts` — wire volume spinner to SoundStore

**Step 1: Add sound calls to each scene**

Follow the exact triggers from the Java source. Key patterns:
- Scene enter → start music
- Scene exit → stop sounds
- TrailScene walking state → loop "Steps", play time-appropriate music
- TrailScene camp state → stop "Steps"
- Button click → play "Click"
- Hunt shot → "Shot" + 40% chance "Ricochet"
- Hunt gun cock → "GunCock"
- Prey kill → "PigSqueal" or "CowSqueal"

**Step 2: Commit**

```bash
git add web/src/scene/ web/src/component/Button.ts
git commit -m "feat: wire audio triggers into all scenes and buttons"
```

---

### Task 39: Scene Transitions

**Files:**
- Create: `web/src/core/Transition.ts`
- Modify: `web/src/core/SceneDirector.ts`

**Step 1: Create Transition system**

Two transition types from the Java:
- **FadeTransition**: render scene to offscreen canvas, animate globalAlpha from 1→0 (out) or 0→1 (in)
- **RotateTransition**: render scene to offscreen canvas, animate rotation + scale (used for victory/game over)

```ts
interface Transition {
  update(delta: number): boolean; // returns true when done
  render(ctx: CanvasRenderingContext2D, sceneImage: ImageData | OffscreenCanvas): void;
}
```

**Step 2: Wire into SceneDirector**

- During transition, render outgoing scene to OffscreenCanvas
- Apply transition effect
- When transition completes, switch to new scene

Default transitions: FadeOutTransition (black, ~500ms) → FadeInTransition (black, ~500ms)
Victory/GameOver: RotateTransition

**Step 3: Commit**

```bash
git add web/src/core/Transition.ts web/src/core/SceneDirector.ts
git commit -m "feat: add fade and rotate scene transitions"
```

---

### Task 40: Polish + GitHub Pages Deployment

**Step 1: Cross-browser testing**

Test in Chrome, Firefox, Safari. Key things to verify:
- OGG playback (Safari may need special handling — consider adding MP3 fallbacks if needed)
- Canvas rendering consistency
- Keyboard/mouse input
- localStorage save/load

**Step 2: Add Vite build config for GitHub Pages**

Update `web/vite.config.ts`:
```ts
export default defineConfig({
  root: ".",
  base: "/Oregon-Trail/",  // Match GitHub repo name
  build: {
    outDir: "dist",
  },
});
```

**Step 3: Build and test production output**

```bash
cd web && npx vite build && npx vite preview
```

Verify the built version works correctly.

**Step 4: Set up GitHub Pages deployment**

Create `.github/workflows/deploy.yml` for automated deployment from the `web/dist` folder, or manually push the `dist` folder to a `gh-pages` branch.

**Step 5: Final commit**

```bash
git add -A
git commit -m "feat: production build config, GitHub Pages deployment"
```

---

## Summary

| Phase | Tasks | What You Get |
|-------|-------|-------------|
| **1: Core Engine** | 1-13 | Canvas, game loop, asset loading, splash/loading screens |
| **2: Menus** | 14-20 | Full UI framework, party creation, main menu |
| **3: Trail + Towns** | 21-32 | Core gameplay loop, all town scenes, world map |
| **4: Minigames** | 33-36 | Hunting, river crossing, endgame, save/load |
| **5: Audio + Polish** | 37-40 | Full soundtrack, transitions, deployment |

Each task produces a working commit. Each phase produces a playable milestone.
