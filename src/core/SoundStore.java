package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.Music;
import org.newdawn.slick.Sound;

public class SoundStore {
	
	private static SoundStore soundStore;
	
	private Map<String, Sound> sounds;
	private Music music =  null;
	
	private SoundStore() {
		soundStore = this;
		sounds = new HashMap<String, Sound>();
	}
	
	public static SoundStore get() {
		if(soundStore == null) {
			soundStore = new SoundStore();
		}
		return soundStore;
	}
	
	public void setMusic(Music music) {
		music.stop();
		this.music = music;
		
	}
	
	public void loopMusic() {
		music.loop();
	}
	
	public void playMusic() {
		music.play();
	}
	
	public void stopMusic() {
		music.stop();
	}
	
	public void addToSounds(Sound sound, String name) {
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
