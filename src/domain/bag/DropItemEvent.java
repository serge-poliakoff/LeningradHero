package domain.bag;

import java.util.Objects;

import domain.eventing.IGameEvent;

public record DropItemEvent(Object issuer, Baggable item) implements IGameEvent {
	public DropItemEvent {
		Objects.requireNonNull(issuer);
		Objects.requireNonNull(item);
	}
	
}
