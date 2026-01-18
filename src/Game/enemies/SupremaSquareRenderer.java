package Game.enemies;

import java.awt.Color;

import domain.Graphics.RendererBase;

class SupremaSquareRenderer extends RendererBase {
	
	public SupremaSquareRenderer() {
		super();
	}
	
	@Override
	protected void onRender() {
		var gr = getGraphics();
		gr.setColor(Color.red);
		gr.fillRect(0, 0, 30, 30);
	}

	@Override
	protected void onDispose() {
		
	}
}