package Game.graphicComponents;

import java.util.Objects;

public record NotificationEvent(Object issuer, String text) {
	public NotificationEvent {
		Objects.requireNonNull(issuer);
		Objects.requireNonNull(text);
	}
}
