package Game.player;

import java.awt.Color;

import domain.Graphics.RendererBase;

public class PlayerRenderer extends RendererBase {

	@Override
	protected void onRender() {
		var gr = getGraphics();
		int cellSize = getCellSize();
		gr.setColor(new Color(215,210,195));
		gr.fillRect(cellSize, cellSize, cellSize * 2, cellSize * 4);
		gr.setColor(Color.white);
		gr.drawBytes("Player".getBytes(), 0, 6, cellSize, 10);
	}

}
