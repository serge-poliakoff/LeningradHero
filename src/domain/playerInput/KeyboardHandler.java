package domain.playerInput;

import com.github.forax.zen.KeyboardEvent;

///unified inteface for a class that can handle a keyboard event
public interface KeyboardHandler {
	
	///tries to react on keyboard input
	///@return
	/// - true if event was consumed
	/// - false otherwise
	public boolean handleKey(KeyboardEvent ev);
}
