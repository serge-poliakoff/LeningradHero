package domain.playerInput.dragndrop;

import java.util.function.Consumer;

import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

import Game.bag.items.Baggable;
import domain.Graphics.Vector2;
import domain.eventing.EventBus;
import domain.playerInput.KeyboardHandler;
import domain.playerInput.MouseEvent;
import domain.playerInput.clicking.OnClickEvent;

public class DragManager implements KeyboardHandler{
	///minimal mouse delta square to start dragging object
	private final int deltaMin = 5;
	///the object, user is dragging or is might prepare to drag
	private Draggable inHand;
	/// signals if object is no longer counted where it have appeared
	private boolean draggedAway;
	/// signals if dragged object have found himself a new place
	private boolean dropSuccess;
	public DragManager() {
		inHand = null;
		draggedAway = false;
		dropSuccess = false;
		
		Consumer<MouseEvent> mouseListener = ev -> {
			handleMouse(ev);
		};
		Consumer<OnClickEvent> clickListener = ev -> {
			handleClicked(ev);
		};
		EventBus.registerListener(MouseEvent.class, mouseListener);
		EventBus.registerListener(OnClickEvent.class, clickListener);
	}
	
	///An object that takes draggable must call this method, to signal 
	///that dragged object have found its new place
	public void signalDropSuccess() {
		dropSuccess = true;
	}
	
	@Override
	public boolean handleKey(KeyboardEvent ev) {
		if (inHand == null) return false;
		if (ev.action() == KeyboardEvent.Action.KEY_RELEASED) return false;
		
		if (ev.key() == KeyboardEvent.Key.E) {
			if (inHand instanceof Baggable) {
				((Baggable)inHand).rotate();
			}else {
				inHand.rotate(Math.PI / 4);
			}
			return true;
		}
		
		return false;
	}
	
	private void handleMouse(MouseEvent ev) {
		if (inHand == null) return;
		
		if (ev.action() == PointerEvent.Action.POINTER_UP) {
			if (!draggedAway) return;
			tryDrop();
			return;
		}
		else if (ev.action() == PointerEvent.Action.POINTER_MOVE) {
			int x = ev.pos().x();
			int y = ev.pos().y();
			int delta = ev.deltaSquare();
			drag(x, y, delta);
			//track what you got simply
		}
	}
	
	private void drag(int x, int y, int delta) {
		if (!draggedAway) {
			//IO.println("DragManager: Start dragging request with delta = " + delta);
			if (delta < 5) return;
			EventBus.PublishEvent(DragStartEvent.class,
					new DragStartEvent(this, inHand));
			draggedAway = true;
			inHand.setDragged(draggedAway);
		}
		
		inHand.moveTo(new Vector2(x, y));
	}
	
	private void tryDrop() {
		IO.println("Dropping " + inHand);
		EventBus.PublishEvent(DragDropEvent.class,
				new DragDropEvent(this, inHand));
		if (!dropSuccess) {
			inHand.getBack();
		}
		inHand.setDragged(false);
		//reinitialise all values before next operation
		inHand = null;
		draggedAway = false;
		dropSuccess = false;
	}
	
	private void handleClicked(OnClickEvent ev) {
		if (ev.issuer() instanceof Draggable) {
			if (inHand != null) return; //doubtable case, but whatsoever
			IO.println("Captured click on draggable " + ev.issuer());
			inHand = (Draggable) ev.issuer();
		}
	}
}
