package menu;

import java.awt.Color;
import java.awt.Graphics2D;

import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;
import com.github.forax.zen.KeyboardEvent.Action;
import com.github.forax.zen.KeyboardEvent.Key;

import domain.DI.ServiceResolver;
import domain.map.GameMap;
import domain.menu.MenuBase;

public class MapMenu extends MenuBase {
	
	private int offX = 300;
	private int offY = 200;
	private int cellSize = 100;
	
	private Color colorBg = new Color(100, 100, 100);
	private Color colorLibre = new Color(200, 190, 150);
	private Color colorClosed = new Color(100, 90, 70);
	
	private int maxX, maxY;
	private int winW, winH;
	private GameMap map;
	
	private int x, y;
	private int plX, plY; //these values won't change for the life cycle of the menu
	
	public MapMenu() {
		map = ServiceResolver.getService(GameMap.class);
		
		var shape = map.getShape();
		maxX = shape[0];
		maxY = shape[1];
		winW = maxX * cellSize;
		winH = maxY * cellSize;
		
		var plPos = map.getPlayerPos();
		plX = x = plPos[0];
		plY = y = plPos[1];
	}
	
	@Override
	public void onRender(Graphics2D gr) {
		var menu = gr.create(offX, offY, winW, winH);
		menu.clearRect(0, 0, winW, winH);
		
		//map grid
		var mapGrid = map.getMap();
		for(int i = 0; i < maxX; i ++) {
			for(int j = 0; j < maxY; j ++) {
			
				if (mapGrid[i][j] == null) {
					menu.setColor(colorBg);
				}else if(!mapGrid[i][j].getFreePassage()) {
					menu.setColor(colorClosed);
				}else {
					menu.setColor(colorLibre);
				}
				
				menu.fillRect(i*cellSize, j * cellSize,
						cellSize, cellSize);
						
				menu.setColor(Color.black);
				menu.drawRect(i*cellSize, j * cellSize,
								cellSize, cellSize);
			}
		}
		menu.setColor(Color.red);
		menu.fillRect(plX * cellSize + 5, plY * cellSize + 5, cellSize - 10, cellSize - 10);
		menu.setColor(Color.black);
		menu.drawRect(x * cellSize + 5, y * cellSize + 5, cellSize - 10, cellSize - 10);
	}

	@Override
	public boolean handleKey(KeyboardEvent ev) {
		if (ev.action() != Action.KEY_PRESSED) {
			return false;
		}
		var key = ev.key();
		switch(key) {
			case Key.UP:
				y = Math.max(--y, 0);
				break;
			case Key.DOWN:
				y = Math.min(++y, maxY - 1);
				break;
			case Key.LEFT:
				x = Math.max(--x, 0);
				break;
			case Key.RIGHT:
				x = Math.min(++x, maxX - 1);
				break;
			case Key.ESCAPE:
				dispose();
				break;
			case Key.SPACE:
				var res = map.travel(x, y);
				if (res == 0) {
					dispose();
				}
				break;
		}
		return false;
	}

}
