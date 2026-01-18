package Game.bag;

import java.util.Objects;

import Game.bag.items.Baggable;

public record DropStockEvent(Object issuer, Baggable[] stock) {
	public DropStockEvent {
		Objects.requireNonNull(issuer);
		Objects.requireNonNull(stock);
	}
}
