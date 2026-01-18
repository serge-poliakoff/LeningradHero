package domain.eventing;

import java.util.function.Consumer;

public interface Subscribeable {
	<E extends Object> void addListener(Class<E> ev,Consumer<E> li);
}
