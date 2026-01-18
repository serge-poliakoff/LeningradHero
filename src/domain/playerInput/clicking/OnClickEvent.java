package domain.playerInput.clicking;

import java.util.Objects;

public record OnClickEvent(Object issuer) {
	public OnClickEvent {
		Objects.requireNonNull(issuer);
	}
}
