package domain.playerInput;

import java.util.Objects;

import com.github.forax.zen.PointerEvent;

import domain.Graphics.Vector2;

public record MouseEvent(Vector2 pos, int deltaSquare, PointerEvent.Action action) {
	public MouseEvent {
		Objects.requireNonNull(pos);
		Objects.requireNonNull(action);
	}
}
