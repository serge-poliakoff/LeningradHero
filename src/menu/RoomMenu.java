package menu;

import java.awt.Color;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.KeyboardEvent.Action;
import com.github.forax.zen.KeyboardEvent.Key;
import com.github.forax.zen.PointerEvent;

import Game.Weapons.WoodenSword;
import domain.bag.Bag;
import domain.bag.Baggable;
import domain.bag.DropStockEvent;
import domain.eventing.EventBus;
import domain.map.LiberateRoomEvent;
import domain.map.Room;
import domain.menu.IMenu;
import domain.menu.MenuBase;
import domain.menu.OpenMenuEvent;

public class RoomMenu extends MenuBase {
	
	
	//private int freePassage = 1;
	//private MenuBase content;
	
	private Room baseRecord;
	
	private Baggable[] stock;
	//test
	//private Bag bagManager;
	
	public RoomMenu(Room room) {
		Objects.requireNonNull(room);
		
		this.baseRecord = room;
		
		/*this.freePassage = room.getFreePassage();
		var contentSupplier = room.getContentFabric(); //can be null if the room is empty
		
		if(contentSupplier != null) {
			content = contentSupplier.get();
			EventBus.PublishEvent(OpenMenuEvent.class, new OpenMenuEvent(this, content));
			
		}*/
		
		stock = room.getStock();
		
		Consumer<Object> liberationListener = x -> {
			//var ev = (LiberateRoomEvent)x;
			baseRecord.setFree();
		};
		addListener(LiberateRoomEvent.class, liberationListener);
		
		Consumer<Object> stockListener = x -> {
			var ev = (DropStockEvent)x;
			stock = ev.stock();
		};
		addListener(DropStockEvent.class, stockListener);
		
	      //int res = bagManager.tryAddItem(new WoodenSword(), 0, 2, 2);
	      //IO.println(res==0?"Sword inserted":"Failed to insert sword...");
	}
	
	
	@Override
	public void handleKey(KeyboardEvent ev) {
		if (ev.action() != Action.KEY_PRESSED) {
			return;
		}
		var key = ev.key();
		switch(key) {
			case Key.SPACE:
				if (baseRecord.getContentFabric() != null) {
					var content = baseRecord.getContentFabric().get();
					EventBus.PublishEvent(OpenMenuEvent.class, content);
				}else {
					//no need to clear room's stock - at onDispose
					//	the BagMenu always emits DropStockEvent, and room's stock
					// 	would be set to it's value
					var itemsCollectMenu = new BagMenu(stock, null);
					
					var openMenuEv = new OpenMenuEvent(this, itemsCollectMenu);
					EventBus.PublishEvent(OpenMenuEvent.class, openMenuEv);
				}
				break;
			case Key.ESCAPE:
				dispose();
				break;
		}
	}
	
	@Override
	public void renderSelf(Graphics2D gr) {
		gr.clearRect(0, 0, 1920, 1200);
		int i = 0;
		for(int y = 800; y > 500; y -= 20) {
			int xOff =  ((i++) % 2) * 30; 
			for (int x = xOff; x < 1920; x += 60) {
				gr.setColor(new Color(50,50,50));
				gr.fillRect(x, y, 59, 19);
			}
		}
	}

	@Override
	public void handleMouse(PointerEvent ev) {
		return;
	}

}
