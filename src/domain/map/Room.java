package domain.map;

import java.util.function.Supplier;

import domain.bag.Baggable;
import domain.menu.MenuBase;

//this class serves as a record of a game room,
//a base to create a game level, with it's graphical interface
//as the player makes progress, this record can be changed:
//a completed fight, open lock etc.
public class Room {
	private int freePassage;
	private Supplier<MenuBase> contentFabric;

	private Baggable[] stock;
	
	public Room(int freePassage, Supplier<MenuBase> content, Baggable[] stock) {
		this.freePassage = freePassage;
		this.contentFabric = content;	//nullable - empty / treasure room
		this.stock = stock;	//nullable also
	}
	
	public int getFreePassage() {
		return freePassage;
	}
	
	public Supplier<MenuBase> getContentFabric() {
		return contentFabric;
	}

	public Baggable[] getStock() {
		return stock;
	}
	
	public void setFree() {
		this.freePassage = 1;
		this.contentFabric = null;
	}
	
	public void saveResources(Baggable[] stock) {
		this.stock = stock;
	}
}
