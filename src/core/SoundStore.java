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
	
	private Map<String, Sound> sounds;
	private Map<String, Music> musics;
	
	private SoundStore(){
		soundStore = this;
		sounds = new HashMap<String, Sound>();
		musics = new HashMap<String, Music>();
		try {
			initialize();
		} catch (SlickException e) {
		}
	}
	
	public static SoundStore get(){
		if(soundStore == null) {
			soundStore = new SoundStore();
		}
		return soundStore;
	}
	
	private void initialize() throws SlickException {
		addToSounds("Click", new Sound("resources/music/click.ogg"));
		addToMusic("GBU", new Music("resources/music/GBUogg.ogg"));
		addToMusic("Smooth", new Music("resources/music/smoothogg2.ogg"));
	}
	
	private void addToMusic(String name, Music music) {
		musics.put(name, music);	
	}

	public void muteMusic() {
		for(String name : musics.keySet()) {
			if (musics.get(name).playing()) {
				musics.get(name).setVolume(0);
			}
		}
	}
	
	public void setMusicVolume(float volume) {
		volume = volume < 0 ? 0 : volume > 1 ? 1 : volume;
		for(String name: musics.keySet()) {
			if(musics.get(name).playing()) {
				musics.get(name).setVolume(volume);
			}
		}
		
	}
	
	
	public void loopMusic(String name) {
		musics.get(name).loop();
	}
	
	public void playMusic(String name) {
		for(String key: musics.keySet()) {
			if(musics.get(key).playing()) {
				musics.get(key).stop();
			}
		}
		musics.get(name).play();
		setMusicVolume(1);
	}
	
	public void playMusic(String name, float volume) {
		for(String key: musics.keySet()) {
			if(musics.get(key).playing()) {
				musics.get(key).stop();
			}
		}
		musics.get(name).play();
		setMusicVolume(volume);
	}
	
	public void stopMusic() {
		for(String name : musics.keySet()) {
			if(musics.get(name).playing()) {
				musics.get(name).stop();
			}
		}
	}
	
	private void addToSounds(String name, Sound sound) {
		sounds.put(name, sound);
	}
	
	public void playSound(String name) {
		sounds.get(name).play();
		}
	
	public void stopSound(String name) {
		sounds.get(name).stop();
	}
	
	
	public List<String> getPlayingSounds() {
		List<String> nameList = new ArrayList<String>();
		for(String name : sounds.keySet()) {
			if(sounds.get(name).playing()) {
				nameList.add(name);
			}
		}
		return nameList;
	}
	
	public void clear() {
		stopAllSound();
		sounds.clear();
	}
	
	public void stopAllSound() {
		for(String name : sounds.keySet()) {
			sounds.get(name).stop();
		}
	}
}
