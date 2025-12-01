import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

import com.github.forax.zen.*;

import Game.Weapons.WoodenSword;
import domain.DI.ServiceResolver;
import domain.bag.Bag;
import domain.eventing.EventBus;
import domain.map.GameMap;
import domain.map.Room;
import domain.menu.IMenu;
import domain.menu.MenuBase;
import domain.menu.OpenMenuEvent;
import menu.BagMenu;
import menu.RoomMenu;
import utils.ArrayDeformer;

public class Main {
	
	private static List<MenuBase> menus = new ArrayList<MenuBase>();
	
	private static void printArr(int[][] source) {
		var n = source.length;
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				IO.print(source[i][j] + " ");
			}
			IO.println();
		}
		IO.println();
	}
	
	public static void Main(String[] args) {
		/*var arr = new int[][] {
			new int[] {0, 1, 0},
			new int[] {1, 1, 1},
			new int[] {1, 0, 0}
		};
		
		var deformer = new ArrayDeformer();
		for(int i = 0; i < 4; i ++)
			printArr(deformer.rotate(arr, i));*/
		var map = new GameMap();
		map.printMap();
		IO.println();
		IO.println("Is it possible to travel to 1,1 ? " + map.travelPossible(1, 1));
		map.printMap();
		IO.println();
		IO.println("Is it possible to travel to 2,2 ? " + map.travelPossible(2, 2));
	}
	
	public static void main(String[] args) {
		//logging
		EventBus.registerListener(KeyboardEvent.class, ev -> {
			System.out.println(ev);
		});
		
		//adding menus to the render queue
		EventBus.registerListener(OpenMenuEvent.class, ev -> {
			IO.println("Opening menu " + ((OpenMenuEvent)ev).menu());
			menus.add(
					((OpenMenuEvent)ev).menu()
					);
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
	    ServiceResolver.RegisterService(() -> bagManager);
	    var map = new GameMap();
	    ServiceResolver.RegisterService(() -> map);
		
	    var room = new Room(1, null, null);
		menus.add(new RoomMenu(room));
		
		RandomGenerator random = RandomGenerator.getDefault();
		Application.run(Color.BLACK, context -> {

		      // get the screen info
		      var screenInfo = context.getScreenInfo();
		      var width = screenInfo.width();
		      var height = screenInfo.height();
		      
		      
		      
		      for(;;) {
		        // get the current event or wait 100 milliseconds
		        Event event = context.pollOrWaitEvent(1000);
		        
		        var curMenu = menus.get(menus.size() - 1);
		        if (event != null) {
		          // debug, print the event
		          //System.out.println(event);

		          switch (event) {
		            case PointerEvent pointerEvent -> {
		            	curMenu.handleMouse(pointerEvent);
		            }
		            case KeyboardEvent keyboardEvent -> {
		            	curMenu.handleKey(keyboardEvent);
		            	
		            	//for instance a unified quit menu event is pressing on ESC button
		            	//so we need to perform this check only here
		            	if (curMenu.getDisposed()) {
		            		menus.remove(menus.size() - 1);
		            		if (menus.size() == 0) {
		            			//no more menus to render -> exit app
		            			context.dispose();
		            			return;
		            		}
		            	}
		            }
		          }
		        }

		        
		        context.renderFrame(graphics -> 
		        	menus.get(menus.size() - 1).renderSelf(graphics));

		        // execute all the graphics instructions on a buffer using a Graphics2D object,
		        // then draw the buffer on the screen area
		        
		      }
		    });
	}
}
