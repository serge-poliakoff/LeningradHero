package domain.bag;

import java.util.Objects;

import domain.eventing.IGameEvent;

public record DropStockEvent(Object issuer, Baggable[] stock) implements IGameEvent {
	public DropStockEvent {
		Objects.requireNonNull(issuer);
		Objects.requireNonNull(stock);
	}
}
