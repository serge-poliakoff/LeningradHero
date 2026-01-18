package domain;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import Game.player.PlayerDeadEvent;
import domain.DI.ServiceResolver;
import domain.Errors.StartUpException;
import domain.Graphics.GraphicsAdapter;
import domain.Graphics.RendererBase;
import domain.Graphics.UpdateEvent;
import domain.Graphics.Vector2;
import domain.eventing.EventBus;
import domain.eventing.Subscribeable;

public abstract class DrawableObject extends Subscribeable {
	private DrawableObject parent;
	private Vector2 pos;
	private double rotation;
	private float scale;
	
	private final RendererBase renderer;
	private GraphicsAdapter adapter;
	
	private boolean active;
	
	protected DrawableObject(Vector2 pos, RendererBase renderer) {
		Objects.requireNonNull(pos);
		Objects.requireNonNull(renderer);
		
		this.pos = pos;
		this.rotation = 0;
		this.scale = 1;
		
		this.active = true;
		this.renderer = renderer;
		this.adapter = ServiceResolver.getService(GraphicsAdapter.class);
		if (adapter == null) {
			throw new StartUpException("Adapter, required for DrawableObject" + this + " was not present in DI");
		}
		Consumer<UpdateEvent> k = (ev) -> {
			if (active) {
				Update();
				adapter.callRenderer(ev.gr(), renderer, getAbsolutePosition(), rotation, scale);
			}
		};
		Consumer<PlayerDeadEvent> k2 = (ev) -> {
			this.setActive(false);
			this.dispose();
		};
		addListener(UpdateEvent.class, k);
		addListener(PlayerDeadEvent.class, k2);
	}
	
	public void setActive(boolean active) {
		if (active == true) {
			onEnabled();
		}
		if (active == false) {
			onDisabled();
		}
		this.active = active;
	}
	
	public boolean getActive() {
		return active;
	}
	
	protected void onEnabled() {};
	protected void onDisabled() {};
	
	protected RendererBase getRenderer() {
		return renderer;
	}
	
	///sets parent for object
	///@param parent new parent for object - must not be null
	///if you want object to have no parent, use resetParent() method
	///@see resetParent
	public final void setParent(DrawableObject parent) {
		Objects.requireNonNull(parent);
		if (this.parent != null) {
			resetParent();
		}
		pos = pos.substract(parent.getAbsolutePosition());
		this.parent = parent;
	}
	
	///sets object to global space, which means it wouldn't have any parent no more
	///takes no action if object is already at the global scope
	public final void resetParent() {
		if (parent == null) {
			return;
		}
		pos = parent.getAbsolutePosition().add(pos);
		parent = null;
	}
	
	///@return object's relative position
	public final Vector2 getPosition() {
		return pos;
	}
	
	///@return object's absolute position
	public final Vector2 getAbsolutePosition() {
		if (parent == null)
			return pos;
		return pos.add(parent.getAbsolutePosition());
	}
	
	///moves object by vector
	///@param move that will be added to object's position
	public final void translate(Vector2 move){
	 	pos = pos.add(move);
	}
	
	///changes object's relative position
	///@param position new position of object
	public final void moveTo(Vector2 position) {
		pos = position;
	}
	
	///rotate object by "radians" radians
	public final void rotate(double radians) {
		rotation += radians;
	}
	
	///changes object rotation to "radians" radians 
	public final void rotateTo(double radians) {
		rotation = radians;
	}
	
	public final void setScale(float scale) {
		this.scale = scale;
	}
	
	/// a function which is called every frame before rendering
	protected void Update() {
		
	}
	
	///liberates all resources, associated with object
	///before it is actually destroyed by GC
	@Override
	protected final void onDispose() {
		onObjectDispose();
		renderer.dispose();
	}

	protected abstract void onObjectDispose();
}
