package domain.bag;

import java.awt.Graphics;
import java.awt.Graphics2D;

import utils.ArrayDeformer;

public abstract class Baggable {
	private int[][] shape;
	protected int cost;
	
	protected Baggable (int[][] shape, int cost){
		this.shape = shape;
		this.cost = cost;
	}
	
	public int getCost() {
		return cost;
	}
	
	public int[][] getShape(){
		return shape;
	}
	
	//indicates if it is possible to turn object. False for maledictions
	public void rotate() {
		var deformer = new ArrayDeformer();
		shape = deformer.rotate(shape, 1);
	}
	
	//indicates if it is possible to drop object from hand in BagMenu. False for maledictions
	public abstract Boolean dropPossible();
	
	public abstract void renderSelf(Graphics2D gr, int cellSize);
}
