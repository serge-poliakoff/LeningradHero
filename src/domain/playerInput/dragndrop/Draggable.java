package domain.playerInput.dragndrop;

import java.util.Objects;

import domain.DrawableObject;
import domain.Graphics.RendererBase;
import domain.Graphics.Vector2;
import domain.playerInput.clicking.Clickable;

///an object that can be dragged from one place to another. The place is presented by Droppable interface
public abstract class Draggable extends Clickable {
	
	private Runnable getBack;
	
	protected Draggable(Vector2 pos, RendererBase renderer) {
		super(pos, renderer);
	}
	
	public final void setGetBack(Runnable getBack) {
		Objects.requireNonNull(getBack);
		this.getBack = getBack;
	}
	
	public final void getBack() {
		if (this.getBack == null) {
			throw new RuntimeException("No get back action was set for draggable " + this);
		}
		this.getBack.run();
	}
	
}
