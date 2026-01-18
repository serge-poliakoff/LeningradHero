package Game.bag.items.unlock;

import Game.bag.items.Baggable;
import domain.Graphics.RendererBase;
import domain.Graphics.Vector2;

public class BagUnlock extends Baggable {
	private BagUnlockRenderer renderer;
	
	public BagUnlock(int[][] shape, Vector2 pos) {
		int cost = 0;
		var renderer = new BagUnlockRenderer();
		super(shape, cost, pos, renderer);
		
		this.renderer = renderer;
		renderer.setGameObject(this);
	}

	@Override
	public Boolean dropPossible() {
		return false;
	}

	@Override
	public Boolean rotatePossible() {
		return true;
	}

	@Override
	public void use() {
		
	}

	@Override
	protected void onClick() {
		renderer.setSelected(true);
	}
	
	@Override
	protected void onUnClick() {
		renderer.setSelected(false);
	}

	@Override
	protected void onDispose() {
		
	}

}
