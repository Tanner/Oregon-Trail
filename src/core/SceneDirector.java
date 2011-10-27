package core;

import java.util.Stack;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.transition.* ;

import component.Component;

import scene.*;

/**
 * Handles the stack of scenes for scene display.
 */
public class SceneDirector extends StateBasedGame {
//	private GameContainer container;
	
	private Stack<Scene> scenes;
	
	/**
	 * Construct a {@code SceneDirector} object with a title for the window.
	 * 
	 * @param name Title of the window
	 */
	public SceneDirector(String name) {
		super(name);
		
		scenes = new Stack<Scene>();
	}

	/**
	 * Push a {@code Scene} on the stack. Present the {@code Scene} with an animated
	 * transition if animated is {@code true}.
	 * @param scene {@code Scene} to present
	 * @param popLastScene Boolean value whether to pop last scene
	 * @param animated Boolean value whether to animate transition
	 * @param transitionOut Transition for animating out last scene
	 * @param transitionIn Transition for animating in new scene
	 */
	public void pushScene(Scene scene, boolean popLastScene, boolean animated, Transition transitionOut, Transition transitionIn) {
		// Don't add a Scene if there's already a Scene on the stack of the same type
		for (Scene sceneOnStack : scenes) {
			if (sceneOnStack.getID() == scene.getID()) {
				return;
			}
		}
		
		scenes.push(scene);
		addState(scene);
		try {
			scene.init(this.getContainer(), this);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		if (animated) {
			enterState(scenes.peek().getID(), transitionOut, transitionIn);
		} else {
			enterState(scenes.peek().getID());
		}
				
		if (popLastScene) {
			scenes.remove(scenes.size() - 2);
		}
	}
	
	/**
	 * Pop the top {@code Scene} on the stack. Present the next {@code Scene} on the stack.
	 * Animate transition if animated is {@code true}.
	 * @param animated Boolean value whether to animate transition
	 */
	public void popScene(boolean animated) {
		if (scenes.size() <= 1) {
			return;
		}
		
		scenes.pop();
	
		if (animated) {
			enterState(scenes.peek().getID(), new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
		} else {
			enterState(scenes.peek().getID());
		}
	}
	
	/**
	 * Remove all {@code Scene} objects on the stack and add a new {@code Scene}.
	 * @param scene Scene to be added after
	 */
	public void replaceStackWithScene(Scene scene) {
		scenes.removeAllElements();
		pushScene(scene, false, false, null, null);
	}
	
	@Override
	public void initStatesList(GameContainer container) throws SlickException {
//		this.container = container;
		
		MainMenuScene mainMenu = new MainMenuScene();
		scenes.add(mainMenu);
		addState(mainMenu);
		
	}
	
	@Override
	public void keyPressed(int key, char c) {
//		Input input = container.getInput();
//		
//		boolean fullScreenKeyComboPressed;
//		String osName = System.getProperty("os.name");
//		if (osName.startsWith("Mac OS")) {
//			fullScreenKeyComboPressed = (key == Input.KEY_F && input.isKeyDown(Input.KEY_LWIN) && input.isKeyDown(Input.KEY_LSHIFT));
//		} else {
//			fullScreenKeyComboPressed = (key == Input.KEY_F11);
//		}
//				
//		if (fullScreenKeyComboPressed) {
//			try {
//				try {
//					DisplayMode[] dms = Display.getAvailableDisplayModes();
//					for (DisplayMode m : dms) {
//						Logger.log(m.toString(), Logger.Level.DEBUG);
//					}
//				} catch (LWJGLException e) {
//					e.printStackTrace();
//				}
//				
//				container.setFullscreen(!container.isFullscreen());
//			} catch (SlickException e) {
//				e.printStackTrace();
//			}
//		}
		
		if (c == '+') {
			GameDirector.sharedSceneListener().showSceneSelector();
		} else if (c == '-') {
			Component.changeDebugMode();
		}
	}
}
