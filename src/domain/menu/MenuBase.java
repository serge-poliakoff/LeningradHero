package domain.menu;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.github.forax.zen.KeyboardEvent;

import domain.DrawableObject;
import domain.eventing.Subscribeable;
import domain.playerInput.KeyboardHandler;

public abstract class MenuBase extends Subscribeable implements KeyboardHandler{
	///DrawableObject objects, associated with this menu
	///note that it is up to user of engine to keep track that each object is not disposed yet
	private List<DrawableObject> objects = new ArrayList<DrawableObject>();
	
	private boolean disposed = false;
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
	
	public Boolean getDisposed() {
		return disposed;
	}
	
	@Override
	protected final void onDispose() {
		onMenuDispose();
		objects.forEach(obj -> obj.dispose());
		disposed = true;
	}

	protected abstract void onMenuDispose();
}
