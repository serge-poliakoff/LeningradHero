package domain.map;

import java.util.Objects;
import java.util.function.Supplier;

import Game.bag.items.Baggable;
import domain.menu.MenuBase;

//this class serves as a record of a game room,
//a base to create a game level, with it's graphical interface
//as the player makes progress, this record can be changed:
//a completed fight, open lock etc.
public class Room {
	private boolean freePassage;
	private Supplier<MenuBase> contentFabric;

	private Baggable[] stock;
	
	///@param freePassage indicates if room is free - not locked, doesn't have any combat or important plot twist
	///@param content a fabric method to create room's content, which will initiate new menu with content
	/// - nullable (if room is empty or just contains some treasures)
	/// - is invoked after first render of room
	///@param stock items that are saved in room. Can't be null
	public Room(boolean freePassage, Supplier<MenuBase> content, Baggable[] stock) {
		Objects.requireNonNull(stock);
		
		this.freePassage = freePassage;
		this.contentFabric = content;	//nullable - empty / treasure room
		this.stock = stock;
		for(var it : stock) {
			it.setActive(false);
		}
	}
	
	public boolean getFreePassage() {
		return freePassage;
	}
	
	public Supplier<MenuBase> getContentFabric() {
		return contentFabric;
	}

	public Baggable[] getStock() {
		return stock;
	}
	
	public void setFree() {
		this.freePassage = true;
		this.contentFabric = null;
	}
	
	///rewrites room's stock
	///@param stock items that must be saved in room
	///@throws NullPointerException if stock is null
	public void saveResources(Baggable[] stock) {
		Objects.requireNonNull(stock);
		
		this.stock = stock;
	}
}
