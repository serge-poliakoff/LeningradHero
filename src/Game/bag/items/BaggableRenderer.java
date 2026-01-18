package Game.bag.items;

import java.awt.Color;
import java.util.Objects;

import domain.Errors.RenderException;
import domain.Graphics.RendererBase;

public abstract class BaggableRenderer extends RendererBase{
    private Color mainColor;
    
    private Baggable gameObject;
	
	protected BaggableRenderer(Color mainColor) {
        Objects.requireNonNull(mainColor);
		super();

        this.mainColor = mainColor;
	}
	
	public void setGameObject(Baggable gameObject) {
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
        
		for(int i = 0; i < shape.length; i++) {
			for (int j = 0; j < shape[i].length; j++) {
				if (shape[i][j] == 1) {
					gr.setColor(mainColor);
					gr.fillRect(i*cellSize, j * cellSize,
							cellSize, cellSize);
					gr.setColor(Color.black);
					gr.drawRect(i*cellSize, j * cellSize,
							cellSize, cellSize);
				}
			}
		}

		if (gameObject.getDragged()){
			gr.setColor(Color.white);
			int h = 10;
			for(var s : gameObject.getDescription()) {
				gr.drawBytes(s.getBytes(), 0, s.length(), 
						shape.length * cellSize, h);
				h += 15;
			}
		}
	}
}
