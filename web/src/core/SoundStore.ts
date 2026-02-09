class SoundStoreClass {
  private audioContext: AudioContext | null = null;
  private sounds: Map<string, AudioBuffer> = new Map();
  private musics: Map<string, AudioBuffer> = new Map();
  private initialized: boolean = false;

  private globalVolume: number = 1;
  private musicGain: GainNode | null = null;
  private sfxGain: GainNode | null = null;

  private currentMusic: AudioBufferSourceNode | null = null;
  private currentMusicName: string | null = null;
  private playingSounds: Map<string, AudioBufferSourceNode> = new Map();

  private townSongs: string[] = [
    "FarewellCheyenne",
    "HangEmHigh",
    "HowTheWest",
    "JesseJames",
    "MyNameIsNobody",
    "FFD",
    "MS"
  ];

  private huntSong: string = "KnightsOfCydonia";

  private initializeAudioContext(): void {
    if (this.audioContext === null) {
      this.audioContext = new AudioContext();
      this.musicGain = this.audioContext.createGain();
      this.musicGain.connect(this.audioContext.destination);
      this.sfxGain = this.audioContext.createGain();
      this.sfxGain.connect(this.audioContext.destination);
    }
  }

  async initialize(onProgress?: (loaded: number, total: number) => void): Promise<void> {
    if (this.initialized) return;

    this.initializeAudioContext();

    const manifest = this.buildManifest();
    const total = manifest.length;
    let loaded = 0;

    const loadPromises = manifest.map(async ({ key, path, isMusic }) => {
      try {
        const response = await fetch(path);
        const arrayBuffer = await response.arrayBuffer();
        const audioBuffer = await this.audioContext!.decodeAudioData(arrayBuffer);

        if (isMusic) {
          this.musics.set(key, audioBuffer);
        } else {
          this.sounds.set(key, audioBuffer);
        }

        loaded++;
        if (onProgress) {
          onProgress(loaded, total);
        }
      } catch (error) {
        console.error(`Failed to load sound: ${path}`, error);
      }
    });

    await Promise.all(loadPromises);
    this.initialized = true;
  }

  private buildManifest(): Array<{ key: string; path: string; isMusic: boolean }> {
    const manifest: Array<{ key: string; path: string; isMusic: boolean }> = [];

    manifest.push({ key: "CracklingFire", path: "/sounds/crackling_fire.ogg", isMusic: true });
    manifest.push({ key: "GBU", path: "/sounds/GBU.ogg", isMusic: true });
    manifest.push({ key: "River", path: "/sounds/river.ogg", isMusic: true });
    manifest.push({ key: "Smooth", path: "/sounds/smooth.ogg", isMusic: false });
    manifest.push({ key: "Steps", path: "/sounds/steps.ogg", isMusic: false });
    manifest.push({ key: "Shot", path: "/sounds/Shotgun_Blast.ogg", isMusic: false });
    manifest.push({ key: "Ricochet", path: "/sounds/Ricochet.ogg", isMusic: false });
    manifest.push({ key: "GunCock", path: "/sounds/Shotgun_Cock.ogg", isMusic: false });
    manifest.push({ key: "Click", path: "/sounds/click.ogg", isMusic: false });
    manifest.push({ key: "ItemGet", path: "/sounds/itemGet.ogg", isMusic: false });
    manifest.push({ key: "WolfHowl", path: "/sounds/wolfHowl.ogg", isMusic: false });
    manifest.push({ key: "Rooster", path: "/sounds/rooster.ogg", isMusic: false });
    manifest.push({ key: "Splash", path: "/sounds/splash.ogg", isMusic: false });
    manifest.push({ key: "DayTheme", path: "/sounds/walkingDaytimeMusic.ogg", isMusic: true });
    manifest.push({ key: "NightTheme", path: "/sounds/Creepy Wind.ogg", isMusic: true });
    manifest.push({ key: "FFD", path: "/sounds/FFD.ogg", isMusic: true });
    manifest.push({ key: "MS", path: "/sounds/MagnificentSeven.ogg", isMusic: true });
    manifest.push({ key: "RK", path: "/sounds/RiverKwai.ogg", isMusic: false });
    manifest.push({ key: "CowMoo", path: "/sounds/cowMoo.ogg", isMusic: false });
    manifest.push({ key: "Donkey", path: "/sounds/Donkey.ogg", isMusic: false });
    manifest.push({ key: "HorseWhinny", path: "/sounds/HorseWhinny.ogg", isMusic: false });
    manifest.push({ key: "FarewellCheyenne", path: "/sounds/FarewellCheyenne.ogg", isMusic: true });
    manifest.push({ key: "HangEmHigh", path: "/sounds/HangEmHigh.ogg", isMusic: true });
    manifest.push({ key: "HowTheWest", path: "/sounds/HowTheWest.ogg", isMusic: true });
    manifest.push({ key: "JesseJames", path: "/sounds/JesseJames.ogg", isMusic: true });
    manifest.push({ key: "MyNameIsNobody", path: "/sounds/MyNameIsNobody.ogg", isMusic: true });
    manifest.push({ key: "WanderingTrail", path: "/sounds/WanderingTrail.ogg", isMusic: true });
    manifest.push({ key: "KnightsOfCydonia", path: "/sounds/KnightsOfCydonia.ogg", isMusic: true });
    manifest.push({ key: "PigSqueal", path: "/sounds/pig_squeal.ogg", isMusic: false });
    manifest.push({ key: "CowSqueal", path: "/sounds/cow_squeal.ogg", isMusic: false });

    return manifest;
  }

  playTownMusic(): void {
    const randomIndex = Math.floor(Math.random() * this.townSongs.length);
    this.playMusic(this.townSongs[randomIndex]);
  }

  playHuntMusic(): void {
    this.loopMusic(this.huntSong);
  }

  loopMusic(name: string): void {
    this.stopMusic();
    const buffer = this.musics.get(name);
    if (!buffer || !this.audioContext || !this.musicGain) return;

    const source = this.audioContext.createBufferSource();
    source.buffer = buffer;
    source.loop = true;
    source.connect(this.musicGain);
    source.start(0);

    this.currentMusic = source;
    this.currentMusicName = name;
  }

  playMusic(name: string, volume?: number): void {
    this.stopMusic();
    const buffer = this.musics.get(name);
    if (!buffer || !this.audioContext || !this.musicGain) return;

    const source = this.audioContext.createBufferSource();
    source.buffer = buffer;
    source.loop = false;
    source.connect(this.musicGain);

    if (volume !== undefined) {
      this.musicGain.gain.value = this.globalVolume * volume;
    } else {
      this.musicGain.gain.value = this.globalVolume;
    }

    source.start(0);
    this.currentMusic = source;
    this.currentMusicName = name;
  }

  stopMusic(): void {
    if (this.currentMusic) {
      try {
        this.currentMusic.stop();
      } catch (e) {
      }
      this.currentMusic = null;
      this.currentMusicName = null;
    }
  }

  playSound(name: string, volume?: number): void {
    const buffer = this.sounds.get(name);
    if (!buffer || !this.audioContext || !this.sfxGain) return;

    const source = this.audioContext.createBufferSource();
    source.buffer = buffer;

    const gainNode = this.audioContext.createGain();
    gainNode.gain.value = volume !== undefined ? (this.globalVolume * volume) : this.globalVolume;

    source.connect(gainNode);
    gainNode.connect(this.sfxGain);
    source.start(0);

    source.onended = () => {
      this.playingSounds.delete(name);
    };

    this.playingSounds.set(name, source);
  }

  loopSound(name: string): void {
    const buffer = this.sounds.get(name);
    if (!buffer || !this.audioContext || !this.sfxGain) return;

    const source = this.audioContext.createBufferSource();
    source.buffer = buffer;
    source.loop = true;
    source.connect(this.sfxGain);
    source.start(0);

    this.playingSounds.set(name, source);
  }

  stopSound(name: string): void {
    const source = this.playingSounds.get(name);
    if (source) {
      try {
        source.stop();
      } catch (e) {
      }
      this.playingSounds.delete(name);
    }
  }

  stop(): void {
    this.stopMusic();
    for (const [name] of this.playingSounds) {
      this.stopSound(name);
    }
  }

  setVolume(volume: number): void {
    this.globalVolume = Math.max(0, Math.min(1, volume));
    if (this.musicGain) {
      this.musicGain.gain.value = this.globalVolume;
    }
    if (this.sfxGain) {
      this.sfxGain.gain.value = this.globalVolume;
    }
  }

  getVolume(): number {
    return this.globalVolume;
  }

  getPlayingMusic(): string | null {
    return this.currentMusicName;
  }
}

export const SoundStore = new SoundStoreClass();
