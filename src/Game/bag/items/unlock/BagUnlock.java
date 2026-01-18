package Game.bag.items.unlock;

import Game.bag.items.Baggable;
import domain.Graphics.RendererBase;
import domain.Graphics.Vector2;

public class BagUnlock extends Baggable {
	
	public BagUnlock(int[][] shape, Vector2 pos) {
		int cost = 0;
		var renderer = new BagUnlockRenderer();
		String[] desc = new String[]{
			"A thing that can unlock more place in your bag",
			"A good, a rare thing..."
		};
		super(shape, cost, desc, pos, renderer);

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
	protected void onObjectDispose() {
		
	}

}
