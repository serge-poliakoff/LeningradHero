package menu;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import com.github.forax.zen.*;
import com.github.forax.zen.KeyboardEvent.Action;
import com.github.forax.zen.KeyboardEvent.Key;

import Game.graphicComponents.BagMenuStock;
import domain.DI.ServiceResolver;
import domain.bag.Bag;
import domain.bag.Baggable;
import domain.bag.DropItemEvent;
import domain.bag.DropStockEvent;
import domain.eventing.EventBus;
import domain.eventing.IGameEvent;
import domain.menu.IMenu;
import domain.menu.MenuBase;

//SPACE for accept
public class BagMenu extends MenuBase {
	
	
	//private Map<Baggable, int[]> stock;
	private BagMenuStock stock;
	private Baggable inHand;
	private int x;
	private int y;
	private int orient;
	
	//move to abstract MenuBase
	//private Map<Class<? extends IGameEvent>,Consumer<Object>> listeners 
	//	= new HashMap<Class<? extends IGameEvent>, Consumer<Object>>();
	
	//DI
	private Bag bagManager;
	//render
	private int windowW;
	private int windowH;
	private int cellSize;
	private int offX;
	private int offY;
	
	//computed on creation
	private int maxXCell;
	private int maxYCell;
	
	//private Boolean disposed = false;
	
	//@Override
	//public Boolean getDisposed() {
	//	return disposed;
	//}
	
	//to do:
	// cut stock to a dedicated class with graphic representation (not a menu itself)
	// selection of items in stock by mouse pointer seems to be justified
	// in dispose of menu, emit event "DropStockEvent"
	
	//to do:
	// encapsulate dispose logic with listeners onto abstract class MenuBase
	// expose an abstract onDispose function to do anything specific to this Menu before it's
	// dispose (like emit "DropStockEvent") as specified earlier
	
	//to do:
	// clear Bag & BagMenu code from orientation, as it is now encapsulated in the item
	public BagMenu(Baggable[] stock, Baggable inHand){
		this.bagManager = ServiceResolver.getService(Bag.class);
		
		this.x = this.y = this.orient = 0;
		if (inHand != null) {
			this.inHand = inHand;
		}
		
		//maximal index of cells on X and Y axis
		maxXCell = bagManager.getBagSpace().length - 1;
		maxYCell = bagManager.getBagSpace()[0].length - 1;
		
		//to do: to DI also
		windowW = 800;
		cellSize = 60;
		windowH = (maxYCell + 1) * cellSize;
		offX = 450;
		offY = 200;
		
		/*this.stock = new HashMap<Baggable, int[]>();
		if (stock != null) {
			//here positions are relative
			int x_pos = maxXCell * cellSize;
			int y_pos = cellSize * 2;
			for(var item : stock) {
				x_pos += cellSize * 3;
				if (x_pos > windowW - cellSize*2) {
					x_pos = maxXCell * cellSize;
					y_pos += cellSize * 3;
				}
				this.stock.put(item, new int[] {x_pos, y_pos});
			}
		}*/
		this.stock = new BagMenuStock(windowW - cellSize * (maxXCell + 1),
				windowH, cellSize / 2);
		if (stock != null) {
			for(var item: stock) {
				this.stock.addToStock(item);
			}
		}
		
		Consumer<Object> addToStockListener = x -> {
			var ev = (DropItemEvent)x;
			addToStock(ev.item());
		};
		addListener(DropItemEvent.class, addToStockListener);
		//listeners.put(DropItemEvent.class, addToStockListener);
		//EventBus.registerListener(DropItemEvent.class, addToStockListener);
		
	}
	
	private void addToStock(Baggable x) {
		IO.println("Adding " + x + " to stock");
		stock.addToStock(x);
	}
	
	@Override
	public void handleKey(KeyboardEvent ev) {
		if (ev.action() != Action.KEY_PRESSED) {
			return;
		}
		var key = ev.key();
		switch(key) {
			case Key.UP:
				y = Math.max(--y, 0);
				break;
			case Key.DOWN:
				y = Math.min(++y, maxYCell);
				break;
			case Key.LEFT:
				x = Math.max(--x, 0);
				break;
			case Key.RIGHT:
				x = Math.min(++x, maxXCell);
				break;
			case Key.E:
				inHand.rotate(); //in case of maledictions it will just do nothing
				//if (inHand.isTuring())
				//	orient = (++orient) % 4;
				break;
			case Key.W:
				if (inHand != null)
					bagManager.dropItem(inHand);
				break;
			case Key.ESCAPE:
				if (inHand != null) {
					var permission = inHand.dropPossible();
					if (permission) {
						addToStock(inHand);
						inHand = null;
						
					}
				}else {
					dispose();
					//close self
				}
				break;
			case Key.SPACE:
				if (inHand != null) {
					//trying to put item on a new place
					int[] prevPos = bagManager.getItemPosition(inHand);
					bagManager.dropItem(inHand); //drop item if it is being moved
					int insertRes = bagManager.tryAddItem(inHand, x, y, 0);
					if (insertRes != 0) {
						IO.println("Inserting item failed...");
						if (prevPos != null) {
							//moving item failed
							stock.removeFromStock(inHand);
							bagManager.tryAddItem(inHand, prevPos[0], prevPos[1], prevPos[2]);
							inHand = null;
						}
						
					} else {
						//item successfully inserted
						stock.removeFromStock(inHand);
						inHand = null;
						IO.println("Item inserted successfully !");
					}
				}else {
					//retrieving item from bag
					inHand = bagManager.findItemByCell(x, y);
					if (inHand == null) break;
					var pos = bagManager.getItemPosition(inHand);
					x = pos[0];
					y = pos[1];
					orient = pos[2];
				}
				break;
			
			default:
				//attribute key to use item
		}
		//renderSelf();
	}
	
	@Override
	public void handleMouse(PointerEvent ev) {
		if (inHand != null) return;
		if (ev.action() != PointerEvent.Action.POINTER_DOWN) {
			return;
		}
		
		int x = ev.location().x();
		int y = ev.location().y();
		
		//re-base mouse location to fit BagStock
		x -= offX + cellSize * (maxXCell + 1);
		y -= offY;
		
		inHand = stock.chooseItem(x, y);
	}

	@Override
	public void renderSelf(Graphics2D gr) {
		var menu = gr.create(offX, offY, windowW, windowH);
		menu.clearRect(0, 0, windowW, windowH);
		
		
		var space = bagManager.getBagSpace();
		var colorSelected = new Color(200, 200, 200);
		var colorNormal = new Color(100,100,100);
		var colorOcc = new Color(20, 20, 20);
		
		//inventory grid
		menu.setColor(colorNormal);
		for(int i = 0; i < space.length; i ++) {
			//if (i == 1) continue;
			for(int j = 0; j < space[i].length; j ++) {
				
				menu.fillRect(i*cellSize, j * cellSize,
						cellSize, cellSize);
				if (space[i][j] == -1) {
					menu.setColor(colorSelected);
					menu.drawLine(i * cellSize, j * cellSize,
							(i + 1) * cellSize, (j + 1) * cellSize);
					menu.drawLine((i + 1) * cellSize, j * cellSize,
							i  * cellSize, (j + 1) * cellSize);
					menu.setColor(colorNormal);
				} else if (space[i][j] == 1) {
					menu.setColor(colorOcc);
					menu.fillRect(i*cellSize, j * cellSize,
							cellSize, cellSize);
					menu.setColor(colorNormal);
				}
				
				/*if (i == x && j == y) {
					menu.setColor(Color.yellow);
					menu.fillRect(i*cellSize, j * cellSize,
							cellSize, cellSize);
					menu.setColor(colorNormal);
				}*/
				
				
				menu.setColor(Color.black);
				menu.drawRect(i*cellSize, j * cellSize,
						cellSize, cellSize);
				menu.setColor(colorNormal);
			}
		}
		menu.setColor(colorSelected);
		menu.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
		
		//inventory equipment
		bagManager.renderEquipement((Graphics2D) menu, cellSize);
		
		//stock
		/*stock.forEach((item, pos) -> {
			var itemGr = (Graphics2D) menu.create(pos[0], pos[1],
					windowW - pos[0], windowH - pos[1]);
			item.renderSelf(itemGr, cellSize);
		});*/
		var stockGr = (Graphics2D) menu.create((maxXCell + 2) * cellSize, 0,
				windowW, windowH);
		stock.renderSelf(stockGr);
		
		var inf = "x = " + x + " y = " + y;
		menu.drawBytes(inf.getBytes(),
				0, inf.length(), cellSize * maxXCell,windowH - cellSize);
		
		//in hand
		if (inHand != null) {
			var inHandGr = (Graphics2D) menu.create(x*cellSize, y * cellSize,
					maxXCell* cellSize, maxYCell * cellSize);
			var inHandAlha = AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 0.5f);
			inHandGr.setComposite(inHandAlha);
			
			inHand.renderSelf(inHandGr, cellSize);
			inHandGr.rotate(Math.PI / 2 * orient);
		}
	}
	
	//move to abstract MenuBase
	@Override
	protected void onDispose() {
		var unusedStock = stock.popAll();
		var ev = new DropStockEvent(this, unusedStock);
		
		EventBus.PublishEvent(DropStockEvent.class, ev);
	}
}
