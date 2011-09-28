package entity;

public abstract class AnimationComponent extends Component {
	protected boolean enabled;
	
	public void start() {
		this.enabled = true;
	}
	
	public void stop() {
		this.enabled = false;
		owner.animationDidStop(this);
	}
}
