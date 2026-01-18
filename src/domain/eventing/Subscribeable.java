package domain.eventing;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class Subscribeable {
	private Map<Class<? extends Object>,Consumer<? extends Object>> listeners 
		= new HashMap<Class<? extends Object>, Consumer<? extends Object>>();
	
	protected <E> void addListener(Class<E> ev,Consumer<E> li) {
		Objects.requireNonNull(ev);
		Objects.requireNonNull(li);

		listeners.put(ev, li);
		EventBus.registerListener(ev, li);
	}

	public final void dispose(){
		IO.println(this + " is disposing...");
		onDispose();
		
		listeners.forEach((evType, listener) -> {
			EventBus.supressListener(evType, listener);
		});
	}

	protected abstract void onDispose();
}
