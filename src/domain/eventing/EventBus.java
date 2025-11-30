package domain.eventing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;


//make all events have the same interface IGameEvent
public class EventBus {
	private Map<Class, Set<Consumer<Object>>> handlers;
	private static EventBus _inst;
	
	private EventBus() {
		handlers = new HashMap<Class, Set<Consumer<Object>>>();
	}
	
	private static EventBus getBus() {
		if (_inst == null)
			_inst = new EventBus();
		return _inst;
	}
	
	public static void registerListener(Class event, Consumer<Object> listener) {
		Objects.requireNonNull(event);
		Objects.requireNonNull(listener);
		
		var listeners = getBus().handlers.computeIfAbsent(event,
				k -> new HashSet<Consumer<Object>>());
		listeners.add(listener);
	}
	
	public static void supressListener(Class event, Consumer<Object> listener) {
		Objects.requireNonNull(event);
		Objects.requireNonNull(listener);
		
		var listeners = _inst.handlers.get(event);
		if (listeners == null)
			throw new NullPointerException("Deleting listener for inexistant event");
		listeners.remove(listener);
	}
	
	public static void PublishEvent(Class eventClass, Object event) {
		var listeners = _inst.handlers.get(eventClass);
		
		if (listeners == null) {
			IO.print("Firing event with no listeners: " + eventClass);
			return;
		}
		
		for(var listener : listeners){
			listener.accept(event);
		}
	}
}
