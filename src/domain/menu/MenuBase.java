package domain.menu;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

import domain.eventing.EventBus;
import domain.eventing.IGameEvent;

public abstract class MenuBase implements IMenu{
	
	private Map<Class<? extends IGameEvent>,Consumer<Object>> listeners 
	= new HashMap<Class<? extends IGameEvent>, Consumer<Object>>();
	private boolean disposed = false;
	
	
	public abstract void renderSelf(Graphics2D gr);
	
	public abstract void handleKey(KeyboardEvent ev);
	
	public abstract void handleMouse(PointerEvent ev);
	
	protected void onDispose() {
		return;
	}
	
	public Boolean getDisposed() {
		return disposed;
	}
	
	protected void addListener(Class<? extends IGameEvent> ev,Consumer<Object> li) {
		listeners.put(ev, li);
		EventBus.registerListener(ev, li);
	}
	
	protected void dispose() {
		onDispose();
		
		listeners.forEach((evType, listener) -> {
			EventBus.supressListener(evType, listener);
		});
		
		disposed = true;
	}
}
