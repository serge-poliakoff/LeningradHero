package domain.playerInput.dragndrop;

import java.util.Objects;

public record DragDropEvent(Object issuer, Draggable dropped) {
	public DragDropEvent {
		Objects.requireNonNull(issuer);
		Objects.requireNonNull(dropped);
	}
}
