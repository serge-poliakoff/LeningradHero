package domain.playerInput.dragndrop;

import java.util.function.Consumer;

import domain.DrawableObject;
import domain.Graphics.RendererBase;
import domain.Graphics.Vector2;

///an area where you can drop objects and from where you can drag them
public abstract class DroppableArea extends DrawableObject {

	protected DroppableArea(Vector2 pos, RendererBase renderer) {
		super(pos, renderer);
		
		Consumer<DragStartEvent> dragListener = ev -> {
			if (this.getActive()) {
				onDrag(ev);
			}
		};
		Consumer<DragDropEvent> dropListener = ev -> {
			if (this.getActive()) {
				onDrop(ev);
			}
		};
		addListener(DragStartEvent.class, dragListener);
		addListener(DragDropEvent.class, dropListener);
	}
	
	protected abstract void onDrag(DragStartEvent ev);
	protected abstract void onDrop(DragDropEvent ev);
	
}
