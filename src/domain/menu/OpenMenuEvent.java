package domain.menu;

import java.util.Objects;

import domain.eventing.IGameEvent;

public record OpenMenuEvent(Object issuer, MenuBase menu) implements IGameEvent {
	public OpenMenuEvent {
		Objects.requireNonNull(issuer);
		Objects.requireNonNull(menu);
	}
}
