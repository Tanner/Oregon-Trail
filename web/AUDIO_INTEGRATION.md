# Audio Integration Reference

This document describes the audio triggers that need to be added to scenes as they are implemented.

## Completed

### Button Component
**File**: `web/src/component/Button.ts`

- `notifyListeners()`: Play "Click" sound before calling listeners
- **Status**: ✓ Complete

### MainMenuScene
**File**: `web/src/scene/MainMenuScene.ts`

- `enter()`: Loop "GBU" music, set volume to 0.5
- `leave()`: Stop music
- **Status**: ✓ Complete

---

## Pending Implementation

### TownScene
**File**: `web/src/scene/TownScene.ts` (not yet created)

**Audio triggers:**
- `enter()` or `init()`: Call `SoundStore.playTownMusic()` (plays random town music from playlist)
- `update()`: If no music is playing, call `SoundStore.playTownMusic()` again

**Reference**: `src/scene/TownScene.java` lines 107, 156-158

**Town music playlist** (in SoundStore):
- FarewellCheyenne
- HangEmHigh
- HowTheWest
- JesseJames
- MyNameIsNobody
- FFD
- MS

---

### TrailScene
**File**: `web/src/scene/TrailScene.ts` (not yet created)

**Audio triggers:**

1. **Time-based music** (in `update()` method):
   - If time is 19:00 (7pm): Play "WolfHowl" sound (once)
   - If time >= 19:00 or time < 5:00 (night): Loop "NightTheme" music
   - Otherwise (daytime): Loop "DayTheme" music
   - Check current playing music before switching to avoid interruption

2. **Animal sounds** (random, in `randomAnimalSound()` method):
   - Don't play if CowMoo, Donkey, or HorseWhinny is already playing
   - Random chance based on number of animals in party:
     - OX: Play "CowMoo" at 0.25 volume
     - MULE: Play "Donkey" at 0.25 volume
     - HORSE: Play "HorseWhinny" at 0.25 volume

3. **Walking state**:
   - When walking starts: Loop "Steps" sound
   - When walking stops (camp, modal, scene end): Stop "Steps" sound

4. **Scene transitions**:
   - When trail complete (arrive at town): Stop all sounds
   - When game over (party dead): Stop all sounds
   - When showing encounter modal: Stop "Steps" sound

**Reference**: `src/scene/TrailScene.java` lines 216-235, 269, 335, 347, 358, 365-375, 386, 415, 454-456, 473

---

### HuntScene
**File**: `web/src/scene/HuntScene.ts` (not yet created)

**Audio triggers:**

1. **Scene enter** (in `init()`):
   - Stop all sounds: `SoundStore.stop()`
   - Play hunt music: `SoundStore.playHuntMusic()` (loops "KnightsOfCydonia")

2. **Shooting** (on left mouse button):
   - Play "Shot" sound at 0.3 volume
   - 40% chance: Also play "Ricochet" sound at full volume

3. **Gun cocking** (on right mouse button):
   - Play "GunCock" sound at full volume

4. **Prey killed**:
   - Cow killed: Play "CowSqueal" at 0.75 volume
   - Pig killed: Play "PigSqueal" at 0.75 volume

5. **Empty gun click**:
   - If gun not cocked or no ammo: Play "Click" at 0.3 volume

6. **Return to camp**:
   - Set music volume to 0.25 (so trail music is quieter when returning)

**Reference**: `src/scene/HuntScene.java` lines 120-121, 430, 451, 487, 490, 506, 515, 544

---

### RiverScene
**File**: `web/src/scene/RiverScene.ts` (not yet created)

**Audio triggers:**

1. **Scene enter** (in `init()` or when showing crossing modal):
   - Loop "River" music

2. **Crossing animation**:
   - If damage taken during crossing: Play "Splash" sound

3. **Bridge toll**:
   - When bridge appears: Play "RK" sound

4. **Scene exit** (success modal dismissed):
   - Stop music before returning to trail

**Reference**: `src/scene/RiverScene.java` lines 185, 207, 228, 314

---

### OptionsScene
**File**: `web/src/scene/OptionsScene.ts` (not yet created)

**Audio triggers:**

1. **Volume spinner**:
   - Initialize spinner to current volume: `SoundStore.getVolume() * 10` (assuming 0-10 scale)
   - On spinner change: `SoundStore.setVolume(spinnerValue / 10)`

**Reference**: `src/scene/OptionsScene.java` lines 71, 136

**Note**: Java uses `MAX_VOLUME = 10.0f` and `SOUND_INCREMENT = 10.0f` for the spinner.

---

## SoundStore API Reference

### Music Methods
- `loopMusic(name: string)`: Loop a music track indefinitely
- `playMusic(name: string, volume?: number)`: Play a music track once
- `stopMusic()`: Stop current music
- `playTownMusic()`: Play random track from town music playlist
- `playHuntMusic()`: Loop "KnightsOfCydonia"
- `getPlayingMusic(): string | null`: Get name of currently playing music

### Sound Effects Methods
- `playSound(name: string, volume?: number)`: Play a sound effect once
- `loopSound(name: string)`: Loop a sound effect indefinitely
- `stopSound(name: string)`: Stop a specific sound effect
- `stop()`: Stop all music and sound effects

### Volume Control
- `setVolume(volume: number)`: Set global volume (0.0 - 1.0)
- `getVolume(): number`: Get current global volume

### Available Sounds
**Music**: GBU, River, DayTheme, NightTheme, FFD, MS, FarewellCheyenne, HangEmHigh, HowTheWest, JesseJames, MyNameIsNobody, WanderingTrail, KnightsOfCydonia, CracklingFire

**SFX**: Click, Steps, Shot, Ricochet, GunCock, WolfHowl, Rooster, Splash, ItemGet, CowMoo, Donkey, HorseWhinny, PigSqueal, CowSqueal, RK, Smooth

---

## Implementation Notes

1. Always import SoundStore at the top of the file:
   ```typescript
   import { SoundStore } from '../core/SoundStore';
   ```

2. For scenes with background music, always stop music in `leave()` method to prevent overlap.

3. Volume values in Java are 0.0 - 1.0 floats. Same in TypeScript.

4. Check if sounds/music are already playing before starting them to avoid restarting:
   ```typescript
   if (SoundStore.getPlayingMusic() !== 'DayTheme') {
     SoundStore.loopMusic('DayTheme');
   }
   ```

5. The SoundStore singleton is already initialized by LoadingScene, so it's safe to use in all scenes.
