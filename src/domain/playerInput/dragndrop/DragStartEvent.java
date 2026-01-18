package domain.playerInput.dragndrop;

import java.util.Objects;

public record DragStartEvent(Object issuer, Draggable captured)  {
	public DragStartEvent {
		Objects.requireNonNull(issuer);
		Objects.requireNonNull(captured);
	}
}
