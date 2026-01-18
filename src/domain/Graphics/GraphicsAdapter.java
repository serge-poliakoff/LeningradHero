package domain.Graphics;

import java.awt.Graphics2D;

import Game.rendering.BaseLayoutInformation;
import domain.DI.ServiceResolver;
import domain.Errors.StartUpException;

public class GraphicsAdapter {
	private int cellSize;
	
	public GraphicsAdapter() {
		var layout = ServiceResolver.getService(BaseLayoutInformation.class);
		if (layout == null) {
			throw new StartUpException("Base Layout Information not found");
		}
		cellSize = layout.baseCellSize();
	}
	
	public void callRenderer(Graphics2D parentGr, RendererBase renderer, Vector2 pos) {
		var childGr = (Graphics2D) parentGr.create(cellSize * pos.x(), cellSize* pos.y(), cellSize * 10, cellSize * 10);
		renderer.render(childGr);
		
	}
	
	public void callRenderer(Graphics2D parentGr, RendererBase renderer, Vector2 pos, double deg) {
		var childGr = (Graphics2D) parentGr.create(cellSize * pos.x(), cellSize* pos.y(), cellSize * 10, cellSize * 10);
		childGr.rotate(deg, cellSize, cellSize);
		renderer.render(childGr);
		
	}
	
	public void callRenderer(Graphics2D parentGr, RendererBase renderer, Vector2 pos, double deg, float scale) {
		int scaledSize = Math.round(scale * this.cellSize);
		var childGr = (Graphics2D) parentGr.create(cellSize * pos.x(), cellSize* pos.y(), cellSize * 10, cellSize * 10);
		childGr.rotate(deg, scaledSize, scaledSize);
		renderer.setCellSize(scaledSize);
		renderer.render(childGr);
	}
}
