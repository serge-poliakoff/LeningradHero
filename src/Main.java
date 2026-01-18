import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

import com.github.forax.zen.*;
import com.github.forax.zen.KeyboardEvent.Key;

import Game.Weapons.WoodenSword;
import Game.bag.Bag;
import Game.bag.GI.BagGI;
import Game.bag.items.Baggable;
import Game.bag.items.unlock.BagUnlock;
import Game.enemies.SupremaSquare;
import Game.enemies.Rat.Rat;
import Game.graphicComponents.NotificationManager;
import Game.graphicComponents.PlayerStats;
import Game.graphicComponents.UIComponent;
import Game.player.Player;
import Game.rendering.BaseBackgroundRenderer;
import Game.rendering.BaseLayoutInformation;
import Game.storages.treasury.Treasury;
import domain.DI.ServiceResolver;
import domain.Graphics.GraphicsAdapter;
import domain.Graphics.PreRenderEvent;
import domain.Graphics.UpdateEvent;
import domain.Graphics.Vector2;
import domain.combat.IEnemy;
import domain.eventing.EventBus;
import domain.map.GameMap;
import domain.map.Room;
import domain.menu.IMenu;
import domain.menu.MenuBase;
import domain.menu.OpenMenuEvent;
import domain.playerInput.KeyboardHandler;
import domain.playerInput.MouseEvent;
import domain.playerInput.dragndrop.DragManager;
import domain.playerInput.dragndrop.Draggable;
import menu.BadEndMenu;
import menu.CombatMenu;
import menu.RoomMenu;
import utils.ArrayDeformer;

public class Main {
	
	private static List<MenuBase> menus = new ArrayList<MenuBase>();
	
	
	public static void main(String[] args) {
		
		//adding menus to the render queue
		EventBus.registerListener(OpenMenuEvent.class, ev -> {
			var event = (OpenMenuEvent) ev;
			
			ServiceResolver.getService(BagGI.class).setActive(false);
			//check if menu is ending
			if (event.menu() instanceof BadEndMenu) {
				for(var menu : menus) {
					menu.dispose();
				}
				menus.clear();
				menus.add(event.menu());
				return;
			}
			
			if (ev.disposeLast() && menus.size() > 0)
				menus.remove(menus.size() - 1);
			menus.add(
					((OpenMenuEvent)ev).menu()
					);
			IO.println("Now last menu is " + menus.getLast());
		});
		
		//configure DI
		int[][] bag = new int[][] {
	    	  new int[] {-1, -1, -1, -1, -1},
	    	  new int[] {-1, 0, 0, 0, -1},
	    	  new int[] {-1, 0, 0, 0, -1},
	    	  new int[] {-1, 0, 0, 0, -1},
	    	  new int[] {-1, -1, -1, -1, -1}
	      };
	    var bagManager = new Bag(bag);
	    
	    
	    var map = new GameMap();
	    ServiceResolver.RegisterService(() -> map);
	    
	    var backgroundProvider = new BaseBackgroundRenderer();
	    ServiceResolver.RegisterService(() -> backgroundProvider);
	    
	    var dragManager = new DragManager();
	    ServiceResolver.RegisterService(() -> dragManager);
		
		var grComponents = new UIComponent[] {
				new PlayerStats(),
				new NotificationManager()
		};
		
		RandomGenerator random = RandomGenerator.getDefault();
		
		Application.run(Color.BLACK, context -> {
			var info = context.getScreenInfo();
			int cellSize = 30;
			
			IO.println("Screen info: " + info);
			var layoutInfo = new BaseLayoutInformation(
				info.width(), info.height(), cellSize, new Vector2(10, 4));
			ServiceResolver.RegisterService(() -> layoutInfo);
			
			var adapter = new GraphicsAdapter();
			ServiceResolver.RegisterService(() -> adapter);
			var bagGI  = new BagGI(bagManager);
			ServiceResolver.RegisterService(() -> bagGI);
			
			var player = new Player();
		    ServiceResolver.RegisterService(() -> player);
			
			map.create();
			map.travel(0, 0);
			
			var keyboardListeners = new ArrayList<KeyboardHandler>(); 
		    keyboardListeners.add(dragManager);
		    keyboardListeners.add(bagGI);
		    keyboardListeners.add(menus.getLast());
			
		    PointerEvent lastPointerEvent = null;
		    
		    for(;;) {
		        // get the current event or wait 100 milliseconds
		        Event event = context.pollOrWaitEvent(100);
		        EventBus.PublishEvent(PreRenderEvent.class, new PreRenderEvent());
		        
		        var curMenu = menus.getLast();
		        if (event != null) {
		          // debug, print the event
		          //System.out.println(event + " to " + curMenu);

		          switch (event) {
		            case PointerEvent pointerEvent -> {
		            	int x = pointerEvent.location().x() / cellSize;
		            	int y = pointerEvent.location().y() / cellSize;
		            	
		            	int deltaSquare = 0;
		            	if (lastPointerEvent != null) {
		            		int difx = (pointerEvent.location().x() - lastPointerEvent.location().x());
		            		difx *= difx;
		            		int dify = (pointerEvent.location().x() - lastPointerEvent.location().x());
		            		dify *= dify;
		            		
		            		deltaSquare = difx + dify;
		            	}
		            	lastPointerEvent = pointerEvent;
		            	
		            	var mousePos = new Vector2(x, y);
		            	EventBus.PublishEvent(MouseEvent.class, 
		            			new MouseEvent(mousePos, deltaSquare, pointerEvent.action()));
		            }
		            case KeyboardEvent keyboardEvent -> {
		            	keyboardListeners.set(
		            			keyboardListeners.size() - 1,
		            			curMenu);
		            	for (var kl : keyboardListeners) {
		            		if (kl.handleKey(keyboardEvent)) {
		            			//IO.println("Keyboard catched by " + kl);
		            			break;
		            		}
		            		//IO.println("Keyboard ignored by " + kl);
		            	}
		            	//for instance a unified quit menu event is pressing on ESC button
		            	//so we need to perform this check only here
		            	if (curMenu.getDisposed()) {
		            		menus.remove(curMenu);
		            		
		            		if (menus.size() == 0) {
		            			//no more menus to render -> exit app
		            			context.dispose();
		            			return;
		            		}
		            		
		            	}
		            }
		          }
		        }

		        
		        context.renderFrame(graphics -> {
		        	
		        	graphics.clearRect(0, 0, info.width(), info.height());
		        	menus.getLast().renderSelf(graphics);
		        	EventBus.PublishEvent(UpdateEvent.class, new UpdateEvent(graphics));
		        	
		        	for(var uicomp : grComponents) {
		        		uicomp.renderSelf(graphics);
		        	}
		        });
		        	
		        	
		        // execute all the graphics instructions on a buffer using a Graphics2D object,
		        // then draw the buffer on the screen area
		        
		      }
		    });
	}
}
