package Game.storages;

import java.util.Objects;

import Game.storages.treasury.Treasury;
import domain.Graphics.RendererBase;
import domain.Graphics.Vector2;
import domain.playerInput.clicking.Clickable;

public class StorageEntry extends Clickable {
	private final int w;
	private final int h;
	private final Treasury trs;
	
	///creates an object through which user can open/close a treasure storage
	///@param pos position of object in units
	///@param trs inner storage
	public StorageEntry(Vector2 pos, Treasury trs) {
		Objects.requireNonNull(trs);
		
		this.trs = trs;
		trs.setActive(false);
		this.w = 4;
		this.h = 4;
		var renderer = new StorageEntryRenderer(4, 4);
		super(pos, renderer);
		
		trs.setParent(this);
		var trsSize = trs.getSize();
		trs.moveTo(new Vector2(-1 * (trsSize.x() / 4), -1 * trsSize.y() - 2));
	}

	@Override
	protected Boolean isClicked(Vector2 click) {
		var corner = getPosition().add(new Vector2(w, h));
		return Vector2.isInsideRect(click, getPosition(), corner);
	}

	@Override
	protected void onClick() {
		trs.setActive(!trs.getActive());
	}
	
	@Override
	protected void onDispose() {
		trs.dispose();
	}

}
