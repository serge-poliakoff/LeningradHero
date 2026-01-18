package domain.eventing;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;


//make all events have the same interface IGameEvent
public class EventBus {
	private final Map<Class, ArrayList<Consumer<Object>>> handlers;
	private static EventBus _inst;
	
	private EventBus() {
		handlers = new LinkedHashMap<Class, ArrayList<Consumer<Object>>>();
	}
	
	private static EventBus getBus() {
		if (_inst == null)
			_inst = new EventBus();
		return _inst;
	}
	
	public static <E extends Object> void registerListener(Class<E> event, Consumer<E> listener) {
		Objects.requireNonNull(event);
		Objects.requireNonNull(listener);
		
		var listeners = getBus().handlers.computeIfAbsent(event,
				k -> new ArrayList<Consumer<Object>>());
		listeners.add((Consumer<Object>) listener);
	}
	
	public static <E> void supressListener(Class<? extends Object> evType, Consumer<? extends Object> listener) {
		Objects.requireNonNull(evType);
		Objects.requireNonNull(listener);
		
		var listeners = getBus().handlers.get(evType);
		if (listeners == null)
			throw new NullPointerException("Deleting listener for inexistant event");
		listeners.remove(listener);
	}
	
	public static <E extends Object> void PublishEvent(Class<E> eventClass, E event) {
		var listeners = getBus().handlers.get(eventClass);
		
		if (listeners == null) {
			IO.print("Firing event with no listeners: " + eventClass);
			return;
		}
		
		for(var listener : listeners){
			listener.accept(event);
		}
	}
}
