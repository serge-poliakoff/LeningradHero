package Game.rendering;

import java.awt.Color;
import java.awt.Graphics2D;

import domain.DI.ServiceResolver;

public class BaseBackgroundRenderer{
	
	public void render(Graphics2D gr) {
		//IO.print("Rendering background...");
		var window = (BaseLayoutInformation) ServiceResolver.getService(BaseLayoutInformation.class);
		gr.clearRect(0, window.getBackgroundWallTop(), 
				window.width(), window.getBackgroundWallBottom() - window.getBackgroundWallTop());
		int i = 0;
		int brickXSize = window.baseCellSize();
		int brickYSize = window.baseCellSize() / 3;
		gr.setColor(new Color(50,50,50));
		for(int y = window.getBackgroundWallBottom();
				y > window.getBackgroundWallTop();
				y -= brickYSize) {
			int xOff =  ((i++) % 2) * brickXSize / 2; 
			for (int x = xOff; x < window.width(); x += brickXSize) {
				
				gr.fillRect(x, y, brickXSize - 1, brickYSize - 1);
			}
		}
	}
}
