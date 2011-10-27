package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class SoundStore {
	
	private static SoundStore soundStore;
	
	private float musicVolume = 1;
	private float soundVolume = 1;
	
	private Map<String, Sound> sounds;
	private Map<String, Music> musics;
	
	/**
	 * Private constructor, creates the singleton
	 */
	private SoundStore(){
		soundStore = this;
		sounds = new HashMap<String, Sound>();
		musics = new HashMap<String, Music>();
		try {
			initialize();
		} catch (SlickException e) {
		}
	}
	
	/**
	 * If soundstore doesn't exist, create it, otherwise just return it
	 * @return the instance of soundstore
	 */
	public static SoundStore get(){
		if(soundStore == null) {
			soundStore = new SoundStore();
		}
		return soundStore;
	}
	
	/**
	 * Creates all the sounds so that there is no lag in making them later.
	 * @throws SlickException
	 */
	private void initialize() throws SlickException {
		addToSounds("Click", new Sound("resources/music/click.ogg"));
		addToMusic("Crackling Fire", new Music("resources/music/crackling_fire.ogg"));
		//addToMusic("GBU", new Music("resources/music/GBUogg.ogg"));
		//addToMusic("Smooth", new Music("resources/music/smoothogg2.ogg"));
	}
	
	/**
	 * Adds a music to the list of music in the store
	 * @param name The name the music is referred to by
	 * @param music The music itself
	 */
	private void addToMusic(String name, Music music) {
		musics.put(name, music);	
	}
	
	/**
	 * Sets the overall volume to a specific level (0 is mute, 1 is full volume)
	 * @param volume The volume to set to.
	 */
	public void setVolume(float volume) {
		musicVolume = volume < 0 ? 0 : volume > 1 ? 1 : volume;
		soundVolume = volume < 0 ? 0 : volume > 1 ? 1 : volume;
		setMusicVolume(musicVolume);
		setSoundVolume(soundVolume);
	}
	
	/**
	 * Sets just the music volumes to a specific level. 
	 * Updates the current musics volume if it is playing
	 * @param volume The volume to set to
	 */
	public void setMusicVolume(float volume) {
		musicVolume = volume < 0 ? 0 : volume > 1 ? 1 : volume;
		for(String name : musics.keySet()) {
			if(musics.get(name).playing()) {
				musics.get(name).setVolume(musicVolume);
			}
		}
	}

	/**
	 * Sets the sound effect volume to a specific level.  
	 * Does not update currently playing sound effects.
	 * @param volume The volume to set it to.
	 */
	public void setSoundVolume(float volume) {
		soundVolume = volume < 0 ? 0 : volume > 1 ? 1 : volume;
	}
	
	/**
	 * Plays the music, but on loop
	 * @param name The key for the music.
	 */
	public void loopMusic(String name) {
		stopMusic();
		musics.get(name).loop();
		musics.get(name).setVolume(musicVolume);
	}
	
	/**
	 * Plays the music not on loop
	 * @param name The key for the music
	 */
	public void playMusic(String name) {
		stopMusic();
		musics.get(name).play();
		musics.get(name).setVolume(musicVolume);
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
		for(String name : musics.keySet()) {
			if(musics.get(name).playing()) {
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
	 * Plays a sound
	 * @param name The key of the sound
	 */
	public void playSound(String name) {
		sounds.get(name).play(1, soundVolume);
	}
	
	/**
	 * Stops the specific sound being played.
	 * @param name The sound to stop
	 */
	public void stopSound(String name) {
		sounds.get(name).stop();
	}
	
	/**
	 * Returns a list of all sounds currently playing
	 * @return A list of all sounds currently playing
	 */
	public List<String> getPlayingSounds() {
		List<String> nameList = new ArrayList<String>();
		for(String name : sounds.keySet()) {
			if(sounds.get(name).playing()) {
				nameList.add(name);
			}
		}
		return nameList;
	}
	
	/**
	 * Stops all playing sounds
	 */
	public void stopAllSound() {
		for(String name : sounds.keySet()) {
			sounds.get(name).stop();
		}
	}
}
