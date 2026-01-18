package domain.map;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import Game.Weapons.WoodenSword;
import Game.bag.items.Baggable;
import Game.bag.items.unlock.BagUnlock;
import Game.enemies.Enemy;
import Game.enemies.Rat.Rat;
import Game.resources.RatMeat.RatMeat;
import domain.Graphics.Vector2;
import domain.combat.IEnemy;
import domain.eventing.EventBus;
import domain.menu.MenuBase;
import domain.menu.OpenMenuEvent;
import menu.CombatMenu;
import menu.RoomMenu;

public class GameMap {
	private Room[][] map;
	private int plX = 0, plY = 0;
	private int mapX = 11, mapY = 5;
	
	private Room emptyRoom() {
		return new Room(true, null, new Baggable[] {});
	}
	
	private Room combatRoom() {
		var rnd = 1 + Math.round(Math.random() * 2);
		Supplier<List<Enemy>> getEnemies = () -> {
			var ens = new ArrayList<Enemy>();
			for (int i = 0; i < rnd; i ++){
				ens.add( new Rat(new Vector2(0, 0)));
			}
			return ens;
		};
		Supplier<List<Baggable>> getReward = () -> {
			var rw = new ArrayList<Baggable>();
			if (rnd == 4){
				rw.add(new BagUnlock(
						new int[][] {
							new int[] { 1 }
						}, new Vector2(0,0)));
				return rw;
			}
			for (int i = 0; i < rnd; i ++){
				rw.add( new RatMeat());
			}
			return rw;
		};
		Supplier<MenuBase> combat = () -> new CombatMenu(
				getEnemies,
				getReward);
		return new Room(false, combat, new Baggable[] {});
	}
	
	private Room treasureRoom() {
		return new Room(true, null, new Baggable[] { new WoodenSword() });
	}
	
	public GameMap() {
		map = new Room[mapX][mapY];
		for(int i = 0; i < mapX; i ++)
			for(int j = 0; j < mapY; j ++)
				map[i][j] = null;
		
		
		plX = plY = 0;
	}
	
	public void create() {
		map[0][0] = new Room(true, null, new Baggable[] {new WoodenSword(),
				new BagUnlock(
						new int[][] {
							new int[] { 1 }
						}, new Vector2(0,0))});
		map[0][1] = combatRoom();
		map[1][1] = combatRoom();
		map[0][1] = treasureRoom();
		map[1][2] = combatRoom();
		map[2][1] = treasureRoom();
		map[2][2] = emptyRoom();
		map[2][3] = treasureRoom();
		map[3][2] = combatRoom();
		map[3][3] = treasureRoom();
		map[4][2] = combatRoom();
		map[5][2] = treasureRoom();
		map[5][1] = combatRoom();
		map[6][0] = treasureRoom();
		map[6][1] = emptyRoom();
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
		
		//make room investigated if it wasn't
		//map[x][y].setFree();
		
		var room = new RoomMenu(map[x][y]);
		EventBus.PublishEvent(OpenMenuEvent.class, 
				new OpenMenuEvent(room, true));
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
		
		if (map[destX][destY] == null || !map[destX][destY].getFreePassage()) {
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
