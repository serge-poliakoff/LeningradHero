package Game.storages;

import java.awt.Color;

import domain.Graphics.RendererBase;

class StorageEntryRenderer extends RendererBase {
	
	private final int w, h;
	
	public StorageEntryRenderer(int w, int h) {
		super();
		
		this.w = w * this.getCellSize();
		this.h = h * this.getCellSize();
	}
	
	@Override
	protected void onRender() {
		var gr = getGraphics();
		
		gr.setColor(new Color(140, 120, 0));
		gr.fillRect(0, 0, w, h);
		gr.setColor(Color.black);
		gr.drawRect(0, 0, w , h);
		gr.fillRect(0, h/3, w, h / 10);
	}

}
