package Game.Weapons;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import domain.bag.*;

public class WoodenSword extends Baggable {
	
	public WoodenSword() {
		var shape = new int[][] {
			new int[] { 0, 1},
			new int[] { 1, 0}
		};
		super(shape, 0);
		
	}

	/*@Override
	public Boolean isTuring() {
		return true;
	}*/

	@Override
	public void renderSelf(Graphics2D gr, int cellSize) {
		var shape = this.getShape();
		gr.setColor(new Color(200,50,50));
		for(int i = 0; i < shape.length; i++) {
			for (int j = 0; j < shape[i].length; j++) {
				if (shape[i][j] == 1) {
					gr.fillRect(i*cellSize, j * cellSize,
							cellSize, cellSize);
				}
			}
		}
		//System.out.println("Sword is rendering !");
	}

	@Override
	public Boolean dropPossible() {
		return true;
	}
	
}
