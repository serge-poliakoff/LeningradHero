package domain.map;

import java.util.Objects;

import domain.eventing.IGameEvent;

public record LiberateRoomEvent(Object issuer) implements IGameEvent {
	public LiberateRoomEvent {
		Objects.requireNonNull(issuer);
	}
}
