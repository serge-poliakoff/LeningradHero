package Game.storages.treasury;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import Game.bag.items.Baggable;
import domain.DI.ServiceResolver;
import domain.Graphics.RendererBase;
import domain.Graphics.Vector2;
import domain.playerInput.dragndrop.DragDropEvent;
import domain.playerInput.dragndrop.DragManager;
import domain.playerInput.dragndrop.DragStartEvent;

import domain.playerInput.dragndrop.DroppableArea;

public class Treasury extends DroppableArea {
	private List<Baggable> trs;
	private int w, h;
	
	///creates a storage for Baggable objects
	///@param pos position of storage on screen
	///@param w storage width in units
	///@param h storage height in units
	public Treasury(Vector2 pos, int w, int h) {
		Objects.requireNonNull(pos);
		
		var renderer = new TreasuryRenderer(w, h);
		super(pos, renderer);
		
		this.trs = new ArrayList<Baggable>();
		this.w = w;
		this.h = h;
	}
	
	public Vector2 getSize() {
		return new Vector2(w, h);
	}
	
	public void addTreasure(Baggable x) {
		Vector2 newPos = Vector2.rndInsideRect(getAbsolutePosition(), w - 2, h - 2);
		x.moveTo(newPos);
		trs.add(x);
		
		x.setActive(getActive());
	}
	
	public Baggable[] getTreasures() {
		return trs.toArray(new Baggable[] {});
	}
	
	private void addWithPos(Baggable x) {
		trs.add(x);
		x.setParent(this);
		x.setActive(getActive());
	}

	@Override
	protected void onDrag(DragStartEvent ev) {
		var captured = ev.captured();
		if (!(captured instanceof Baggable)) return;
		
		if (trs.contains(captured)) {
			captured.resetParent();
			captured.setGetBack(() -> {
				addTreasure((Baggable)captured);
			});
			trs.remove(captured);
		}
	}

	@Override
	protected void onDrop(DragDropEvent ev) {
		if (!(ev.dropped() instanceof Baggable)) return;
		
		var dropPos = ev.dropped().getAbsolutePosition();
		var pos = this.getAbsolutePosition();
		var corner = pos.add(new Vector2(w, h));
		
		IO.println("Catched drop of" + ev.dropped() + " at " + dropPos);
		
		//extract to a static method in Vector2
		if (Vector2.isInsideRect(dropPos, pos, corner)) {
				var dragManager = ServiceResolver.getService(DragManager.class);
				if (dragManager == null)
					throw new RuntimeException("No drag manager in Di");	//Di exception or Engine exception
				dragManager.signalDropSuccess();
				addWithPos((Baggable)ev.dropped());
		}
		
	}
	
	@Override
	protected void onEnabled() {
		for(var tr : trs)
			tr.setActive(true);
	}
	
	@Override
	protected void onDisabled() {
		for(var tr : trs)
			tr.setActive(false);
	}

	@Override
	protected void onDispose() {
		onDisabled();
		trs.clear();
	}

}
