package Game.bag.items;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.function.Consumer;

import com.github.forax.zen.PointerEvent;

import domain.DrawableObject;
import domain.Graphics.RendererBase;
import domain.Graphics.Vector2;
import domain.eventing.EventBus;
import domain.playerInput.MouseEvent;
import domain.playerInput.clicking.OnClickEvent;
import domain.playerInput.dragndrop.Draggable;
import utils.ArrayDeformer;

public abstract class Baggable extends Draggable {
	private int[][] shape;
	private final int cost;
	
	protected Baggable (int[][] shape, int cost, Vector2 pos, RendererBase renderer){
		super(pos, renderer);
		
		this.shape = shape;
		this.cost = cost;
		
	}
	
	public int getCost() {
		return cost;
	}
	
	public int[][] getShape(){
		return shape;
	}
	
	//designed to hide same named method on DrawableObject class
	public void rotate() {
		if (rotatePossible()) {
			var deformer = new ArrayDeformer();
			shape = deformer.rotate(shape, 1);
			//((DrawableObject)this).rotate(Math.PI / 4);
		}
	}
	
	@Override
	protected Boolean isClicked(Vector2 click) {
		
		Vector2[][] colliderMask = new Vector2[shape.length][shape[0].length];
		for (int i = 0; i < shape.length; i ++) {
			for (int j = 0; j < shape[0].length; j ++) {
				if (shape[i][j] == 1) {
					var pos = getAbsolutePosition();
					if (click.x() == (pos.x() + i) && click.y() == (pos.y() + j)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	//indicates if it is possible to drop object from hand in BagMenu. False for maledictions
	public abstract Boolean dropPossible();
	
	public abstract Boolean rotatePossible();
	
	//DI only (get player strats etc.) / CombatMenu through main class ? like a static method of getting last menu
	//normally player should'nt be capable of using weapon without a combat
	//here once again referencing this shit -> provide action to a BagMenu to choose
	public abstract void use();
}
