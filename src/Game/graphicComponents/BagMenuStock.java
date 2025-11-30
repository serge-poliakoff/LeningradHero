package Game.graphicComponents;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import com.github.forax.zen.PointerEvent;

import domain.bag.Baggable;

public class BagMenuStock {
	
	private Map<Baggable, int[]> stock;
	private int xMin = 0;
	private int yMin = 0;
	
	//space for each item = itemSpace * cellSize
	private final int itemSpace = 4;
	
	private int w;
	private int h;
	private int cellSize;
	
	public BagMenuStock(int w, int h, int cellSize) {
		this.w = w;
		this.h = h;
		this.cellSize = cellSize;
		
		stock = new HashMap<Baggable, int[]>();
	}
	
	private void rearrangeStock() {
		int x_pos = 0;
		int y_pos = 0;
		
		var it = stock.entrySet().iterator();
		
		while(it.hasNext()) {
			var entry = it.next();
			entry.getValue()[0] = x_pos;
			entry.getValue()[1] = y_pos;
			
			x_pos += cellSize * itemSpace;
			if (x_pos > w - (itemSpace - 1) * cellSize) {
				x_pos = 0;
				y_pos += itemSpace * cellSize;
			}
		}
		
		xMin = x_pos;
		yMin = y_pos;
	}
	
	public void addToStock(Baggable item) {
		if (stock.containsKey(item)) return;
		
		stock.put(item, new int[] {xMin, yMin});
		xMin += cellSize * itemSpace;
		if (xMin > w - (itemSpace - 1) * cellSize) {
			xMin = 0;
			yMin += itemSpace * cellSize;
		}
	}
	
	public void removeFromStock(Baggable item) {
		IO.println("Removing " + item + " from stock");
		stock.remove(item);	//optional -> no plaints if the item wasn't present
		rearrangeStock();
	}
	
	public Baggable[] popAll() {
		var res = new Baggable[stock.keySet().size()];
		stock.keySet().toArray(res);
		IO.println("Dropping stock of size: " + res.length);
		stock.clear();
		return res;
	}
	
	public Baggable chooseItem(int x, int y) {
		var it = stock.entrySet().iterator();
		
		while(it.hasNext()) {
			var entry = it.next();
			var dx = entry.getValue()[0] - x;
			var dy = entry.getValue()[1] - y;
			
			if (Math.sqrt(dx*dx + dy*dy) < itemSpace * cellSize / 2) {
				return entry.getKey();
			}
		}
		
		return null;
	}
	
	public void renderSelf(Graphics2D gr) {
		gr.clearRect(0, 0, w, h);
		stock.forEach((item, pos) -> {
			var itemGr = (Graphics2D) gr.create(pos[0], pos[1],
					itemSpace*cellSize, itemSpace * cellSize);
			item.renderSelf(itemGr, cellSize);
		});
	}
}
