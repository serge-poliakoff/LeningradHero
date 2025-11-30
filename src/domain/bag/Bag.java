package domain.bag;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain.eventing.EventBus;

public class Bag {
	
	record BagPlacement(int x, int y, int or, List<int[]> cellsOcc) {};
	
	private int[][] space;
	
	private Map<Baggable, BagPlacement> equipement;
	
	public Bag (int[][] initialSpace){
		this.space = initialSpace;
		this.equipement = new HashMap<Baggable, BagPlacement>();
	}
	
	public int[][] getBagSpace(){
		return space;
	}
	
	public void renderEquipement(Graphics2D gr, int cellSize) {
		equipement.forEach((item, value)  -> {
			//IO.println("Rendering equipement: " + item.getClass().toString());
			var itemGr = (Graphics2D) gr.create(value.x * cellSize,
					value.y * cellSize,
					cellSize * 10, cellSize * 10);
			item.renderSelf(itemGr, cellSize);
			
			var itemOrient = value.or;
			((Graphics2D) itemGr).rotate(Math.PI / 2 * itemOrient);
		});
	}
	
	public Baggable findItemByCell(int x, int y) {
		var els = equipement.entrySet().iterator();
		
		while(els.hasNext()) {
			var cur = els.next();
			var curEl = cur.getValue();
			var curKey = cur.getKey();
			
			for(var cellOcc : curEl.cellsOcc) {
				if (cellOcc[0] == x && cellOcc[1] == y) {
					return curKey;
				}
			}
		}
		
		return null;
	}
	
	//returns x, y of top-left corner and orientation
	public int[] getItemPosition(Baggable item) {
		var cellsOccupied = equipement.get(item);
		if (cellsOccupied == null) {
			return null;
		}
		return new int[] {
				cellsOccupied.x,
				cellsOccupied.y,
				cellsOccupied.or
		};
		//position is specified as top-left corner
	}
	
	//0 - success, -1 - failure
	public int tryAddItem(Baggable item, int x, int y, int orient) {
		List<int[]> cellsOccupied = new ArrayList<int[]>();
		List<Baggable> toDrop = new ArrayList<Baggable>();
		try {
			var shape = item.getShape();
			//orient if possible
			for(int i = 0; i < shape.length; i ++) {
				for(int j = 0; j < shape[i].length; j++) {
					if (shape[i][j] == 0) continue;
					int intersect = shape[i][j] * space[x+i][y+j]; 
					if (intersect == -1) {
						IO.println("Inventory bounds corrupted at"
								+ (x+i) + " " + (y+j));
						return -1;
					}
					if (intersect == 1) {
						toDrop.add(findItemByCell(x+i, y+j));
						//dropItem(findItemByCell(x+i, y+j));
						//no dropping before it's certain that the insertions is possible
					}
					cellsOccupied.add(new int[]{x+i, y+j});
				}
			}
		}catch(ArrayIndexOutOfBoundsException e) {
			IO.println("Array out of bound exception while inserting"
					+ item.getClass().getName());
			return -1;
		}
		
		for(var it : toDrop)
			dropItem(it);
		
		equipement.put(item, 
				new BagPlacement(x, y, orient, cellsOccupied));
		for(var cell : cellsOccupied) {
			space[cell[0]][cell[1]] = 1;
		}
		
		return 0;
	}
	
	//returns 1 if no object was dropped, 0 if it was
	public int dropItem(Baggable item) {
		var cellsOccupied = equipement.get(item);
		if (cellsOccupied == null) {
			return 1;
		}
		
		for(var cell : equipement.get(item).cellsOcc) {
			space[cell[0]][cell[1]] = 0;
		}
		equipement.remove(item);
		
		var ev = new DropItemEvent(this, item);
		EventBus.PublishEvent(DropItemEvent.class, ev);
		
		return 0;
	}
	
}
