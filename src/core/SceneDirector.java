package core;

import java.util.Stack;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.transition.* ;

import scene.*;
import scene.test.SceneSelectorScene;


public class SceneDirector extends StateBasedGame {
	private GameContainer container;
	
	private Stack<Scene> scenes;
	
	public SceneDirector(String name) {
		super(name);
		
		scenes = new Stack<Scene>();
	}

	public void pushScene(Scene scene, boolean animated) {
		scenes.push(scene);
		addState(scene);
		try {
			scene.init(this.getContainer(), this);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		if (animated) {
			enterState(scenes.peek().getID(), new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
		} else {
			enterState(scenes.peek().getID());
		}
	}
	
	public void popScene(boolean animated) {
		if (scenes.size() > 1) {
			scenes.pop();
		
			if (animated) {
				enterState(scenes.peek().getID(), new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
			} else {
				enterState(scenes.peek().getID());
			}
		}
	}
	
	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		this.container = container;
		
		SceneSelectorScene selectorScene = new SceneSelectorScene();
		addState(selectorScene);
		
		GameDirector.sharedSceneDirectorDelegate().sceneDirectorReady();
	}
	
	public void keyPressed(int key, char c) {
		Input input = container.getInput();
		
		boolean fullScreenKeyComboPressed;
		String osName = System.getProperty("os.name");
		if (osName.startsWith("Mac OS")) {
			fullScreenKeyComboPressed = (key == Input.KEY_F && input.isKeyDown(Input.KEY_LWIN) && input.isKeyDown(Input.KEY_LSHIFT));
		} else {
			fullScreenKeyComboPressed = (key == Input.KEY_F11);
		}
		
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		if (fullScreenKeyComboPressed) {
			try {
				container.setFullscreen(!container.isFullscreen());
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}
}
