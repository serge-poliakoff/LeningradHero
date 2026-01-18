package Game.bag.items.unlock;

import java.awt.Color;

import Game.Weapons.WoodenSword;
import domain.Errors.RenderException;
import domain.Graphics.RendererBase;

class BagUnlockRenderer extends RendererBase {
	private BagUnlock gameObject;
	private boolean selected;
	public BagUnlockRenderer() {
		super();
		selected = false;
	}
	
	public void setGameObject(BagUnlock gameObject) {
		this.gameObject = gameObject;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	protected void onRender() {
		var gr = getGraphics();
		if (gameObject == null) {
			throw new RenderException("GameObject is null at the time of render");
		}
		var shape = gameObject.getShape();
		int cellSize = this.getCellSize();
		int cellFilledSize = cellSize;
		int offset = 0;
		
		if (selected) {
			gr.setColor(Color.white);
			for(int i = 0; i < shape.length; i++) {
				for (int j = 0; j < shape[i].length; j++) {
					if (shape[i][j] == 1) {
						gr.fillRect(i*cellSize, j * cellSize,
								cellSize, cellSize);
					}
				}
			}
			cellFilledSize -= 2;
			offset = 1;
		}
		
		gr.setColor(new Color(200,200,200));
		for(int i = 0; i < shape.length; i++) {
			for (int j = 0; j < shape[i].length; j++) {
				if (shape[i][j] == 1) {
					gr.fillRect(offset + i*cellSize, offset + j * cellSize,
							cellFilledSize, cellFilledSize);
				}
			}
		}
	}

}
