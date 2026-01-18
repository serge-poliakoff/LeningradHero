package Game.bag.GI;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

import Game.bag.Bag;
import domain.DI.ServiceResolver;
import domain.Graphics.RendererBase;
import domain.Graphics.Vector2;

class BagRenderer extends RendererBase {
	
	private int[][] space;
	
	public void setBagSpace(int[][] space) {
		this.space = space;
	}
	
	@Override
	protected void onRender() {
		var menu = getGraphics();
		
		
		var colorBlocked = new Color(40, 40, 40);
		var colorNormal = new Color(100,100,100);
		
		int cellSize = this.getCellSize();
		//inventory grid
		menu.setColor(colorNormal);
		for(int i = 0; i < space.length; i ++) {
			for(int j = 0; j < space[i].length; j ++) {
				if (space[i][j] == -1) {
					menu.setColor(colorBlocked);
					menu.fillRect(i*cellSize, j * cellSize,
							cellSize, cellSize);
					/*menu.drawLine(i * cellSize, j * cellSize,
							(i + 1) * cellSize, (j + 1) * cellSize);
					menu.drawLine((i + 1) * cellSize, j * cellSize,
							i  * cellSize, (j + 1) * cellSize);*/
					menu.setColor(colorNormal);
				} else {
					menu.fillRect(i*cellSize, j * cellSize,
							cellSize, cellSize);
				}
				
				menu.setColor(Color.black);
				menu.drawRect(i*cellSize, j * cellSize,
						cellSize, cellSize);
				menu.setColor(colorNormal);
			}
		}
		
	}

}
