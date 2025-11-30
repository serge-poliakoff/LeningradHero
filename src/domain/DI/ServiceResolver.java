package domain.DI;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ServiceResolver {
	
	private ServiceResolver() {}	//pure static class
	
	private static Map<Class<? extends Object>, Supplier<Object>> container
		= new HashMap<Class<? extends Object>, Supplier<Object>>();
	
	public static <T> void RegisterService(Supplier<T> fabric){
		var key = fabric.get().getClass();
		container.put(key, (Supplier<Object>) fabric);
	}
	
	/*public static <T>void addSingletone(T instance){
		RegisterService(() -> instance);
	}*/
	 
	
	
	public static <T> T getService(Class<T> serviceClass) {
		var fabric = container.get(serviceClass);
		var service = (T) fabric.get();
		return service;
	}
}
