package domain.DI;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ServiceResolver {
	
	private ServiceResolver() {}	//pure static class
	
	private static Map<Class<? extends Object>, Supplier<Object>> container
		= new HashMap<Class<? extends Object>, Supplier<Object>>();
	
	///registers a fabric method for objects of class T
	///@param fabric fabric method
	public static <T> void RegisterService(Supplier<T> fabric){
		var key = fabric.get().getClass();
		container.put(key, (Supplier<Object>) fabric);
	}
	 
	///tries to retrieve registered service; throws on fail
	///@param serviceClass class of demanded service
	///@return production of fabric, associated with this service class (may be null)
	///@throws DIException if there is no fabric for provided serviceClass
	///@see getUnrequiredService
	public static <T> T getService(Class<T> serviceClass) {
		var fabric = container.get(serviceClass);
		if (fabric == null)
			throw new DIException(serviceClass);
		var service = (T) fabric.get();
		return service;
	}
	
	///tries to retrieve registered service, does not throw on fail
	///@param serviceClass class of demanded service
	///@return production of fabric, associated with this service class (may be null)
	///@see getService
	public static <T> T getUnrequiredService(Class<T> serviceClass) {
		var fabric = container.get(serviceClass);
		if (fabric == null)
			return null;
		var service = (T) fabric.get();
		return service;
	}
}
