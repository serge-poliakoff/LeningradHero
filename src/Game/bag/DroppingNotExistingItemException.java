package Game.bag;

import Game.bag.items.Baggable;

public class DroppingNotExistingItemException extends RuntimeException {

	public DroppingNotExistingItemException(Baggable item) {
		super(item.toString());
	}

}
