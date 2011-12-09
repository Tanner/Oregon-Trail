package model.huntingMap;

import java.util.Random;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
//import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import component.sprite.AnimatingSprite;
import component.sprite.PreyAnimatingSprite;
import component.sprite.AnimatingSprite.Direction;

import core.ImageStore;



public class PreyPig extends Prey {
	//pig gives 5 meat
	private final static int pigMeat = 5;
	private GameContainer container;

	public PreyPig(GameContainer container, StateBasedGame game, Random pigRand, int mapXWidth, int mapYHeight) {
		//pig gives 5 meat
		super(pigMeat);
		this.container = container;
		//takes 2 shots to kill a pig
		this.hitPoints = 2;
		Image[] pigAnimLeft = new Image[6];
 		Image[] pigAnimRight = new Image[6];
 		Image[] pigAnimFront = new Image[6];
 		Image[] pigAnimBack = new Image[6];
 		
		for (int incr = 1; incr < 7; incr++){
 			
 	 		pigAnimLeft[incr - 1] = ImageStore.get().getImage("HUNT_PIGLEFT" + incr);
 	 		pigAnimRight[incr - 1] = ImageStore.get().getImage("HUNT_PIGRIGHT" + incr);
 	 		pigAnimFront[incr - 1] = ImageStore.get().getImage("HUNT_PIGFRONT" + incr);
 	 		pigAnimBack[incr - 1] = ImageStore.get().getImage("HUNT_PIGBACK" + incr);
 	 		
		}//for 1 to 6
 			preySprite =  new PreyAnimatingSprite(container,
// 					48,
 					new Animation(pigAnimLeft, 250),
 					new Animation(pigAnimRight, 250),
 					new Animation(pigAnimFront, 250),
 					new Animation(pigAnimBack, 250),
 					AnimatingSprite.Direction.RIGHT); 	 		
  		
 			this.xLocation = pigRand.nextInt(mapXWidth/4) + mapXWidth/2; //focus location near center of hunt map
 			this.yLocation = pigRand.nextInt(mapYHeight/4) + mapYHeight/2; //focus location near center of hunt map
 			
	}//pig constructor


	/**
	 * will determine where the pig will move to next
	 */
	@Override
	public void movePrey(int delta) {
		switch(new Random().nextInt(5)) {
		case 1:
			preySprite.setDirectionFacing(Direction.LEFT);
			preySprite.setMoving(true);
			//move map to right
			preySprite.setLocation(preySprite.getX() - delta, preySprite.getY());
			break;
		case 2:
			preySprite.setDirectionFacing(Direction.BACK);
			preySprite.setMoving(true);
			//move map down
			this.yLocation -= delta;	
			break;
		case 3:
			preySprite.setDirectionFacing(Direction.RIGHT);
			preySprite.setMoving(true);
			//move map to left
			this.xLocation += delta;
			break;
		case 4:
			preySprite.setDirectionFacing(Direction.FRONT);
			preySprite.setMoving(true);
			//move map up
			yLocation += delta;
			break;
		case 0:
			preySprite.setMoving(false);
			break;
		}
	}

	
}//pig class
