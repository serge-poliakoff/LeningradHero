package Game.bag.items.unlock;

import java.awt.Color;

import Game.Weapons.WoodenSword;
import domain.Errors.RenderException;
import domain.Graphics.RendererBase;

class BagUnlockRenderer extends RendererBase {
	private BagUnlock gameObject;

	public BagUnlockRenderer() {
		super();
	}
	
	public void setGameObject(BagUnlock gameObject) {
		this.gameObject = gameObject;
	}

	@Override
	protected void onRender() {
		var gr = getGraphics();
		if (gameObject == null) {
			throw new RenderException("GameObject is null at the time of render");
		}
		var shape = gameObject.getShape();
		int cellSize = this.getCellSize();

		
		gr.setColor(new Color(200,200,200));
		for(int i = 0; i < shape.length; i++) {
			for (int j = 0; j < shape[i].length; j++) {
				if (shape[i][j] == 1) {
					gr.fillRect(i*cellSize, j * cellSize,
							cellSize, cellSize);
				}
			}
		}

		if (gameObject.getDragged()){
			gr.setColor(Color.white);
			int h = 10;
			IO.println(gameObject.getDescription());
			for(var s : gameObject.getDescription()) {
				gr.drawBytes(s.getBytes(), 0, s.length(), 
						shape.length * cellSize, h);
				h += 15;
			}
		}
	}

}
