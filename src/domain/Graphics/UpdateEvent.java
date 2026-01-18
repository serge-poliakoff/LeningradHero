package domain.Graphics;

import java.awt.Graphics2D;
import java.util.Objects;


public record UpdateEvent(Graphics2D gr) {
	public UpdateEvent {
		Objects.requireNonNull(gr);
	}
}
