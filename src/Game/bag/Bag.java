package Game.bag;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import Game.bag.items.Baggable;
import Game.bag.items.unlock.BagUnlock;
import Game.rendering.BaseLayoutInformation;
import domain.DrawableObject;
import domain.DI.ServiceResolver;
import domain.Graphics.Vector2;
import domain.eventing.EventBus;

///internal logic lying on BagGI object. Works only with it, so operates only relative positions
public class Bag {
	private int[][] space;
	
	private Set<Baggable> equipement;
	
	
	public Bag (int[][] initialSpace){
		this.space = initialSpace;
		this.equipement = new HashSet<Baggable>();
	}
	
	public int[][] getBagSpace(){
		return space;
	}
	
	public Set<Baggable> getEquipement() {
		return equipement;
	}
	
	///tries to find bag item by the position of one cell it occupies
	///@param cellPos position of a cell occupied by an object described as Vector2
	///@return null if given cell is free, Baggable that occupies else
	public Baggable findItemByCell(Vector2 cellPos) {
		var els = equipement.iterator();
		
		while(els.hasNext()) {
			var cur = els.next();
			var pos = cur.getPosition();
			var shape = cur.getShape();
			for(int i = 0; i < shape.length; i ++) {
				for(int j = 0; j < shape[i].length; j++) {
					if (pos.add(new Vector2(i, j)).equals(cellPos)) {
						return cur;
					}
				}
			}
		}
		
		return null;
	}
	
	//supress
	///tells if item is present in bag
	public Boolean contains(Baggable item) {
		return equipement.contains(item);
	}
	
	///tries to fit suggested item into bag, dropping down all objects that do not cooperate.
	///If item does not fit, no action is taken
	///@param item item to fit
	///@return - true if item was inserted successfully
	/// - false if suggested position is corrupting bag's borders
	public Boolean tryAddItem(Baggable item) {
		if (item instanceof BagUnlock) {
			
			return tryAddUnlock((BagUnlock)item);
		}
		List<Vector2> cellsOccupied = new ArrayList<Vector2>();
		List<Baggable> toDrop = new ArrayList<Baggable>();
		var shape = item.getShape();
		int x = item.getPosition().x();
		int y = item.getPosition().y();
		for(int i = 0; i < shape.length; i ++) {
			for(int j = 0; j < shape[i].length; j++) {
				if (shape[i][j] == 0) continue;
				//check for out of bounds
				if (x + i > space.length || y + j > space[i].length) return false;
				
				int intersect = shape[i][j] * space[x+i][y+j]; 
				if (intersect == -1) {
					IO.println("Inventory bounds corrupted at"
							+ (x+i) + " " + (y+j));
					return false;
				}
				if (intersect == 1) {
					toDrop.add(findItemByCell(new Vector2(x+i, y+j)));
				}
				cellsOccupied.add(new Vector2(x+i, y+j));
			}
		}
		
		for(var it : toDrop)
			dropItem(it);
		
		equipement.add(item);
		for(var cell : cellsOccupied) {
			space[cell.x()][cell.y()] = 1;
		}
		
		return true;
	}
	
	private boolean tryAddUnlock(BagUnlock unlock) {
		IO.println("Adding unlock at position " + unlock.getPosition());
		List<Vector2> cellsToLiberate= new ArrayList<Vector2>();
		var shape = unlock.getShape();
		int x = unlock.getPosition().x();
		int y = unlock.getPosition().y();
		for(int i = 0; i < shape.length; i ++) {
			for(int j = 0; j < shape[i].length; j++) {
				if (shape[i][j] == 0) continue;
				
				if (x + i > space.length || y + j > space[i].length) return false;
				
				int intersect = shape[i][j] * space[x+i][y+j]; 
				if (intersect == -1) {
					cellsToLiberate.add(new Vector2(x+i, y+j));
				}
			}
		}
		
		for(var cell : cellsToLiberate) {
			space[cell.x()][cell.y()] = 0;
		}
		
		unlock.setActive(false);
		return true;
	}
	
	
	public void dropItem(Baggable item){
		Objects.requireNonNull(item);
		
		var isPresent = equipement.contains(item);
		if (!isPresent) {
			throw new DroppingNotExistingItemException(item);
		}
		
		var pos = item.getPosition();
		var shape = item.getShape();
		for(int i = 0; i < shape.length; i ++) {
			for(int j = 0; j < shape[i].length; j++) {
				if (shape[i][j] == 0) continue;
				space[pos.x() + i][pos.y() + j] = 0;
			}
		}
		equipement.remove(item);
		EventBus.PublishEvent(DropItemEvent.class, new DropItemEvent(item));
		return;
	}
	
}
