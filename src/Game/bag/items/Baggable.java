package Game.bag.items;

import java.util.Objects;

import Game.bag.Bag;
import domain.Graphics.RendererBase;
import domain.Graphics.Vector2;

import domain.playerInput.dragndrop.Draggable;
import utils.ArrayDeformer;

public abstract class Baggable extends Draggable {
	private int[][] shape;
	private final int cost;
	private Bag bagManager;

	private String[] description;

	protected Baggable (int[][] shape, int cost,
		String[] description,
		Vector2 pos, RendererBase renderer){
		Objects.requireNonNull(shape);
		Objects.requireNonNull(description);
		Objects.requireNonNull(renderer);

		super(pos, renderer);
		
		this.shape = shape;
		this.cost = cost;
		this.description = description;
	}

	///sets bagManager if it is needed for object
	public void setBag(Bag bagManager){
		this.bagManager = bagManager;
	}

	protected Bag getBag(){
		return this.bagManager;
	}
	
	public int getCost() {
		return cost;
	}
	
	public int[][] getShape(){
		return shape;
	}
	
	public String[] getDescription(){
		return description;
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
	
	public abstract Boolean dropPossible();
	
	public abstract Boolean rotatePossible();
	
	public abstract void use();
}
