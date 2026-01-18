package Game.Weapons;

import java.awt.Color;
import java.util.Objects;

import Game.rendering.BaseLayoutInformation;
import domain.DI.ServiceResolver;
import domain.Errors.RenderException;
import domain.Errors.StartUpException;
import domain.Graphics.RendererBase;

class WoodenSwordRenderer extends RendererBase {
	
	private WoodenSword gameObject;
	private boolean selected;
	
	public WoodenSwordRenderer() {
		super();
		selected = false;
	}
	
	public void setGameObject(WoodenSword gameObject) {
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
		gr.setColor(new Color(200,50,50));
		for(int i = 0; i < shape.length; i++) {
			for (int j = 0; j < shape[i].length; j++) {
				if (shape[i][j] == 1) {
					gr.fillRect(i*cellSize, j * cellSize,
							cellSize, cellSize);
				}
			}
		}
	}
	
}
