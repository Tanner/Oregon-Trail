package scene;

import core.FontManager;

public interface SceneDelegate {
	public void requestScene(SceneID id);
	public void sceneDidEnd(Scene scene);
	public FontManager getFontManager();
	public void showSceneSelector();
}
