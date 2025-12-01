package domain.map;

import domain.eventing.EventBus;
import domain.menu.OpenMenuEvent;
import menu.RoomMenu;

public class GameMap {
	private Room[][] map;
	private int plX = 0, plY = 0;
	private int mapX = 11, mapY = 5;
	
	private Room emptyRoom() {
		return new Room(1, null, null);
	}
	
	private Room busyRoom() {
		return new Room(0, null, null);
	}
	
	public GameMap() {
		map = new Room[mapX][mapY];
		for(int i = 0; i < mapX; i ++)
			for(int j = 0; j < mapY; j ++)
				map[i][j] = null;
		map[0][0] = emptyRoom();
		map[1][1] = emptyRoom();
		map[0][1] = emptyRoom();
		map[1][2] = busyRoom();
		map[2][1] = emptyRoom();
		map[2][2] = busyRoom();
		
		plX = plY = 0;
	}
	
	public int[] getShape() {
		return new int[] {mapX, mapY};
	}
	
	public int[] getPlayerPos() {
		return new int[] {plX, plY};
	}
	
	public Room[][] getMap(){
		return map;
	}
	
	//0 - success, -1 - failure
	public int travel(int x, int y) {
		if (!travelPossible(x, y)) return -1;
		
		var room = new RoomMenu(map[x][y]);
		EventBus.PublishEvent(OpenMenuEvent.class, 
				new OpenMenuEvent(this, room));
		plX = x;
		plY = y;
		return 0;
	}
	
	public Boolean travelPossible(int destX, int destY) {
		if (map[destX][destY] == null) return false;
		if (destX == plX && destY == plY) return true;
		
		var mapClone = new Room[mapX][mapY];
		for(int i = 0; i < mapX; i++)
			mapClone[i] = map[i].clone();
		
		return tp_rec(mapClone, destX - 1, destY) ||
				tp_rec(mapClone, destX + 1, destY)||
				tp_rec(mapClone, destX, destY - 1)||
				tp_rec(mapClone, destX, destY + 1);
	}
	
	private Boolean tp_rec(Room[][] map, int destX, int destY) {
		if (destX >= map.length || destX < 0) return false;
		if (destY >= map.length || destY < 0) return false;
		
		if (map[destX][destY] == null || map[destX][destY].getFreePassage() != 1) {
			return false;
		}
		if (destX == plX && destY == plY) return true;
		
		map[destX][destY] = null;	//prevent infinite recursion
		return tp_rec(map, destX - 1, destY) ||
				tp_rec(map, destX + 1, destY)||
				tp_rec(map, destX, destY - 1)||
				tp_rec(map, destX, destY + 1);
	}
	
	//test
	public void printMap() {
		var n = map.length;
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				var symbole = map[i][j] == null ? "-1" : (" " + map[i][j].getFreePassage());
				IO.print(symbole + " ");
			}
			IO.println();
		}
		IO.println();
	}
	
}
