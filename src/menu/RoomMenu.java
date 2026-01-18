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
import Game.bag.Bag;
import Game.bag.DropItemEvent;
import Game.bag.DropStockEvent;
import Game.bag.GI.BagGI;
import Game.bag.items.Baggable;
import Game.player.Player;
import Game.rendering.BaseBackgroundRenderer;
import Game.rendering.BaseLayoutInformation;
import Game.storages.StorageEntry;
import Game.storages.treasury.Treasury;
import domain.DI.ServiceResolver;
import domain.Graphics.Vector2;
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
	
	private Treasury roomStock;
	
	private BaseBackgroundRenderer bg;
	
	public RoomMenu(Room room) {
		Objects.requireNonNull(room);
		
		this.baseRecord = room;
		
		roomStock = new Treasury(new Vector2(25, 15), 6, 6);
		
		var layout = ServiceResolver.getService(BaseLayoutInformation.class);
		var roomStockEntry = new StorageEntry(new Vector2(
				layout.getXCenterUnits() - 2,
				layout.getBackgroundWallBottom() / layout.baseCellSize()),
				roomStock);
		
		for (var item : room.getStock()) {
				item.setActive(true);
				roomStock.addTreasure(item);
		}
		addObject(roomStockEntry);
		
		Consumer<LiberateRoomEvent> liberationListener = x -> {
			baseRecord.setFree();
			revealObjects();
		};
		addListener(LiberateRoomEvent.class, liberationListener);
		
		Consumer<DropItemEvent> stockListener = ev -> {
			roomStock.addTreasure(ev.item());
		};
		addListener(DropItemEvent.class, stockListener);
		
		bg = ServiceResolver.getService(BaseBackgroundRenderer.class);
	      //int res = bagManager.tryAddItem(new WoodenSword(), 0, 2, 2);
	      //IO.println(res==0?"Sword inserted":"Failed to insert sword...");
	}
	
	@Override
	protected void onAwake() {
		if (baseRecord.getContentFabric() != null) {
			var content = baseRecord.getContentFabric().get();
			EventBus.PublishEvent(OpenMenuEvent.class, new OpenMenuEvent(content, false));
			hideObjects();
		}
	}
	
	@Override
	public boolean handleKey(KeyboardEvent ev) {
		if (ev.action() != Action.KEY_PRESSED) {
			return false;
		}
		var key = ev.key();
		switch(key) {
			case Key.SPACE:
				ServiceResolver.getService(BagGI.class).setActive(true);
				return true;
			case Key.M:
				//because the input-event flow, you can'not open a Map for example during a combat
				//	or an important dialog: RoomMenu won't see your input
				var mapMenu = new MapMenu();
				IO.println("Opening map menu - " + mapMenu);
				var openMenuEv = new OpenMenuEvent(mapMenu, true);
				EventBus.PublishEvent(OpenMenuEvent.class, openMenuEv);
				dispose();
				return true;
			case Key.ESCAPE:
				dispose();
				return true;
		}
		return false;
	}
	
	@Override
	protected void onMenuDispose(){
		baseRecord.saveResources(roomStock.getTreasures());
	}
	
	@Override
	public void onRender(Graphics2D gr) {
		bg.render(gr);
	}

}
