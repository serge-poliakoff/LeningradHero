package domain.menu;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;
import com.github.forax.zen.KeyboardEvent.Action;
import com.github.forax.zen.KeyboardEvent.Key;

import domain.DrawableObject;
import domain.eventing.EventBus;
import domain.playerInput.KeyboardHandler;

public abstract class MenuBase implements KeyboardHandler{
	//add onEnabled function -> invoked on the first render
	//	public renderSelf -> public final renderSelf { if (firstFrame) onEnabled(); renderSelfProtected();}
	private Map<Class<? extends Object>,Consumer<? extends Object>> listeners 
	= new HashMap<Class<? extends Object>, Consumer<? extends Object>>();
	private boolean disposed = false;
	
	///DrawableObject objects, associated with this menu
	///note that it is up to user of engine to keep track that each object is not disposed yet
	private List<DrawableObject> objects = new ArrayList<DrawableObject>();
	
	private boolean awake = false;
	
	///adds object to keep track of it for hide/reveal operations and to delete it at dispose
	protected void addObject(DrawableObject obj) {
		Objects.requireNonNull(obj);
		objects.add(obj);
	}
	
	protected void hideObjects() {
		objects.forEach(obj -> obj.setActive(false));
	}
	
	protected void revealObjects() {
		objects.forEach(obj -> obj.setActive(true));
	}
	
	///deletes object from tracking
	///@See addObject
	protected void removeObject(DrawableObject obj) {
		Objects.requireNonNull(obj);
		objects.remove(obj);
	}
	
	public final void renderSelf(Graphics2D gr) {
		onRender(gr);
		if (!awake) {
			onAwake();
			awake = true;
		}
	}
	
	///function, called only once after the first render of menu
	protected void onAwake() {}
	
	public abstract void onRender(Graphics2D gr);
	
	public abstract boolean handleKey(KeyboardEvent ev);
	
	protected void onDispose() {}
	
	public Boolean getDisposed() {
		return disposed;
	}
	
	protected <E> void addListener(Class<E> ev,Consumer<E> li) {
		listeners.put(ev, li);
		EventBus.registerListener(ev, li);
	}
	
	public void dispose() {
		IO.println(this + " is disposing...");
		onDispose();
		
		listeners.forEach((evType, listener) -> {
			EventBus.supressListener(evType, listener);
		});
		objects.forEach(obj -> obj.dispose());
		
		disposed = true;
	}
}
