package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 * Manages all the sounds for use in the game.
 */
public class SoundStore {
	private static SoundStore soundStore;
	
	private float globalVolume = 1;
	
	public Map<String, Sound> sounds;
	private Map<String, Music> musics;
	
	private Set<String> playingSounds = new HashSet<String>();
	private List<String> townSongs = new ArrayList<String>();
	
	/**
	 * Constructs a new {@code SoundStore} and initializes it.
	 */
	private SoundStore() {
		soundStore = this;
		sounds = new HashMap<String, Sound>();
		musics = new HashMap<String, Music>();
		try {
			initialize();
		} catch (SlickException e) {
			System.err.println(e.getStackTrace());
		}
	}
	
	/**
	 * Returns the {@code SoundStore} instance. Creates one if one does not exist.
	 * @return Instance of SoundStore
	 */
	public static SoundStore get() {
		if (soundStore == null) {
			soundStore = new SoundStore();
		}
		
		return soundStore;
	}
	
	/**
	 * Initializes the store with all the images.
	 * @throws SlickException
	 */
	private void initialize() throws SlickException {
		addToMusic("Crackling Fire", new Music("resources/sounds/crackling_fire.ogg"));
		addToMusic("GBU", new Music("resources/sounds/GBU.ogg"));
		addToMusic("River", new Music("resources/sounds/river.ogg"));
		addToSounds("Smooth", new Sound("resources/sounds/smooth.ogg"));
		addToSounds("Steps", new Sound("resources/sounds/steps.ogg"));
		addToSounds("Click", new Sound("resources/sounds/click.ogg"));
		addToSounds("ItemGet", new Sound("resources/sounds/itemGet.ogg"));
		addToSounds("WolfHowl", new Sound("resources/sounds/wolfHowl.ogg"));
		addToSounds("Rooster", new Sound("resources/sounds/rooster.ogg"));
		addToSounds("Splash", new Sound("resources/sounds/splash.ogg"));
		addToMusic("DayTheme", new Music("resources/sounds/walkingDaytimeMusic.ogg"));
		addToMusic("NightTheme", new Music("resources/sounds/Creepy Wind.ogg"));
		addToMusic("FFD", new Music("resources/sounds/FFD.ogg"));
		addToMusic("MS", new Music("resources/sounds/MagnificentSeven.ogg"));
		addToSounds("RK", new Sound("resources/sounds/RiverKwai.ogg"));
		addToSounds("CowMoo", new Sound("resources/sounds/cowMoo.ogg"));
		addToSounds("Donkey", new Sound("resources/sounds/Donkey.ogg"));
		addToSounds("HorseWhinny", new Sound("resources/sounds/HorseWhinny.ogg"));
		addToMusic("FarewellCheyenne", new Music("resources/sounds/FarewellCheyenne.ogg"));
		addToMusic("HangEmHigh", new Music("resources/sounds/HangEmHigh.ogg"));
		addToMusic("HowTheWest", new Music("resources/sounds/HowTheWest.ogg"));
		addToMusic("JesseJames", new Music("resources/sounds/JesseJames.ogg"));
		addToMusic("MyNameIsNobody", new Music("resources/sounds/MyNameIsNobody.ogg"));
		addToMusic("WanderingTrail", new Music("resources/sounds/WanderingTrail.ogg"));
		
		townSongs.add("FarewellCheyenne");
		townSongs.add("HangEmHigh");
		townSongs.add("HowTheWest");
		townSongs.add("JesseJames");
		townSongs.add("MyNameIsNobody");
		townSongs.add("FFD");
		townSongs.add("MS");
	}
	
	public void playTownMusic() {
		Random random = new Random();
		playMusic(townSongs.get(random.nextInt(townSongs.size())));
		
	}
	
	/**
	 * Get the key of the music that is currently playing.
	 * @return Name of the music currently playing
	 */
	public String getPlayingMusic() {
		for (String name : musics.keySet()) {
			if (musics.get(name).playing()) {
				return name;
			}
		}
		
		return null;
	}
	
	/**
	 * Adds a music to the list of music in the store
	 * @param name The name the music is referred to by (the key)
	 * @param music The {@code Music} object
	 */
	private void addToMusic(String name, Music music) {
		musics.put(name, music);
	}
	
	/**
	 * Sets the overall volume to a specific level (0 is mute, 1 is full volume).
	 * @param volume The volume to set to.
	 */
	public void setVolume(float volume) {
		globalVolume = volume < 0 ? 0 : volume > 1 ? 1 : volume;
		setMusicVolume(1);
	}
	
	/**
	 * Gets the global volume.
	 * @return Float value of volume (0 is mute, 1 is full volume)
	 */
	public float getVolume() {
		return globalVolume;
	}
	
	/**
	 * Sets just the music volumes to a specific level. 
	 * Updates the current musics volume if it is playing
	 * @param volume The volume to set to
	 */
	public void setMusicVolume(float volume) {
		if(getPlayingMusic() != null) {
			musics.get(getPlayingMusic()).setVolume(globalVolume * (volume < 0 ? 0 : volume > 1 ? 1 : volume));
		}
	}

	/**
	 * Plays the music, but on loop
	 * @param name The key for the music.
	 */
	public void loopMusic(String name) {
		stopMusic();
		musics.get(name).loop();
		musics.get(name).setVolume(globalVolume);
	}
	
	/**
	 * Plays the music not on loop
	 * @param name The key for the music
	 */
	public void playMusic(String name) {
		stopMusic();
		musics.get(name).play();
		musics.get(name).setVolume(globalVolume);
	}
	
	/**
	 * Plays the music at a specific volume
	 * @param name
	 * @param volume
	 */
	public void playMusic(String name, float volume) {
		stopMusic();
		musics.get(name).play();
		setMusicVolume(volume);
	}
	
	/**
	 * Stops the currently playing music
	 */
	public void stopMusic() {
		for (String name : musics.keySet()) {
			if (musics.get(name).playing()) {
				musics.get(name).stop();
			}
		}
	}
	
	/**
	 * Adds a sound effect to the list
	 * @param name The key for the sound
	 * @param sound The sound itself
	 */
	private void addToSounds(String name, Sound sound) {
		sounds.put(name, sound);
	}
	
	/**
	 * Plays a sound.
	 * @param name The key of the sound
	 */
	public void playSound(String name) {
		sounds.get(name).play(1, globalVolume);
		playingSounds.add(name);
	}
	
	/**
	 * Play a sound at the given volume.
	 * @param name The key of the sound
	 * @param volume Volume to play this sound at
	 */
	public void playSound(String name, float volume) {
		sounds.get(name).play(1, globalVolume * volume);
		playingSounds.add(name);
	}
	
	/**
	 * Stops the specific sound being played.
	 * @param name The sound to stop
	 */
	public void stopSound(String name) {
		sounds.get(name).stop();
		playingSounds.remove(name);
	}
	
	/**
	 * Returns a list of all sounds currently playing
	 * @return A list of all sounds currently playing
	 */
	public Set<String> getPlayingSounds() {
		Set<String> newSet = new HashSet<String>();
		for (String name : playingSounds) {
			if (sounds.get(name).playing()) {
				newSet.add(name);
			}
		}

		return newSet;
	}
	
	/**
	 * Stops all playing sounds
	 */
	public void stopAllSound() {
		for (String name : sounds.keySet()) {
			if (sounds.get(name).playing()) {
				sounds.get(name).stop();
			}
		}
		
		playingSounds.clear();
	}

	/**
	 * Loop the sound forever.
	 * @param name Key value for the sound
	 */
	public void loopSound(String name) {
		sounds.get(name).loop();
		
	}

	/**
	 * Stop everything - music and sounds.
	 */
	public void stop() {
		stopAllSound();
		stopMusic();
	}
}
