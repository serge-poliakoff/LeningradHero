package Game.bag;

import java.util.Objects;

import Game.bag.items.Baggable;

///event, emit by bag when dropping item
///must be catch by current room's treasury or current menu
public record DropItemEvent(Baggable item) {
	public DropItemEvent {
		Objects.requireNonNull(item);
	}
	
}
