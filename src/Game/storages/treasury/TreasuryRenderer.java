package Game.storages.treasury;

import java.awt.AlphaComposite;
import java.awt.Color;

import domain.Graphics.RendererBase;

class TreasuryRenderer extends RendererBase {
	private int width;
	private int height;
	
	public TreasuryRenderer(int w, int h) {
		super();
		this.width = w * this.getCellSize();
		this.height = h * this.getCellSize();
	}
	
	@Override
	protected void onRender() {
		var gr = this.getGraphics();
		
		gr.setColor(new Color(.5f,.5f,.5f,.5f));
		gr.fillRect(0, 0, width, height);
		
	}

}
