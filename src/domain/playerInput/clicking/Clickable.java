package domain.playerInput.clicking;

import java.util.function.Consumer;

import com.github.forax.zen.PointerEvent;

import domain.DrawableObject;
import domain.Graphics.RendererBase;
import domain.Graphics.Vector2;
import domain.eventing.EventBus;
import domain.playerInput.MouseEvent;

public abstract class Clickable extends DrawableObject {

	protected Clickable(Vector2 pos, RendererBase renderer) {
		super(pos, renderer);
		
		Consumer<MouseEvent> clickHandler = ev -> {
			if(getActive()) {
				onClickEv(ev);
			}
		};
		addListener(MouseEvent.class, clickHandler);
	}
	
	private void onClickEv(MouseEvent ev) {
		if (ev.action() == PointerEvent.Action.POINTER_MOVE) return;
		
		if(isClicked(ev.pos())) {
			if (ev.action() == PointerEvent.Action.POINTER_DOWN) {
				onClick();
				EventBus.PublishEvent(OnClickEvent.class, new OnClickEvent(this));
			}else {
				//pointer up
				onUnClick();
			}
		}
	}
	
	///this method must check if the mouse click was on this object or not
	///@param click mouse click position in global space as Vector2
	///@return true if click was on object, false if not
	protected abstract Boolean isClicked(Vector2 click);
	
	///additional actions for object to execute if it has received a click upon
	///this function is called before publishing OnClickEvent
	protected void onClick() {};
	
	///executed when pointer was raised up at the object
	protected void onUnClick() {};
	
	@Override
	protected abstract void onObjectDispose();

}
