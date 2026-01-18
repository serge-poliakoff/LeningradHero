package domain.Graphics;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import Game.rendering.BaseLayoutInformation;
import domain.DI.ServiceResolver;
import domain.Errors.StartUpException;
import domain.eventing.EventBus;

public abstract class RendererBase {
	
	private Map<Class<? extends Object>,Consumer<? extends Object>> listeners 
	= new HashMap<Class<? extends Object>, Consumer<? extends Object>>();
	private boolean disposed = false;
	
	//in order we can conserve a link in event handlers
	private Graphics2D gr[] = {null};
	private BaseLayoutInformation layout;
	private int cellSize;
	
	protected RendererBase() {
		this.layout = ServiceResolver.getService(BaseLayoutInformation.class);
		if (layout == null) {
			throw new StartUpException("Layout information is not present");
		}
		cellSize = layout.baseCellSize();
		Consumer<PreRenderEvent> disposeGraphics = (ev) -> {
			if (gr[0] != null) {
				gr[0].dispose();
				gr[0] = null;
			}
		};
		addListener(PreRenderEvent.class, disposeGraphics);
	}
	
	public void setCellSize(int size) {
		cellSize = size;
	}
	
	protected int getCellSize() {
		return cellSize;
	}
	
	protected BaseLayoutInformation getLayout() {
		return this.layout;
	}
	
	protected Graphics2D getGraphics() {
		return gr[0];
	}
	
	protected <E extends Object> void addListener(Class<E> ev,Consumer<E> li) {
		listeners.put(ev, li);
		EventBus.registerListener(ev, li);
	}
	
	///gr here is already translated graphics, so renderer can not bother himself with the questions of position
	///	rotations are to be applied in GraphicsAdapter, after the render;
	public final void render(Graphics2D gr) {
		this.gr[0] = gr;
		onRender();
	}
	
	///in this method you should draw your object
	protected abstract void onRender();
	
	
	public final void dispose() {
		if(!disposed) {
			listeners.forEach((evType, listener) -> {
				EventBus.supressListener(evType, listener);
			});
			onDispose();
			disposed = true;
		}
	}
	
	///method that will be called before object is destroyed.
	///implementation is not necessary
	protected void onDispose() {};
}
