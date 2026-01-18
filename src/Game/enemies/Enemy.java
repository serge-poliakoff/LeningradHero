package Game.enemies;

import domain.Graphics.RendererBase;
import domain.Graphics.Vector2;
import domain.combat.Attack;
import domain.combat.IEnemy;
import domain.playerInput.clicking.Clickable;

public abstract class Enemy extends Clickable {

	protected Enemy(Vector2 pos, RendererBase renderer) {
		super(pos, renderer);
	}
	
	
	protected abstract Boolean isClicked(Vector2 click);
	
	public abstract void setSelected(boolean selected);
	
	public abstract void attack();
	
	public abstract void inflictDamage(Attack attack);

	public abstract boolean isDead();
	
	@Override
	protected void onObjectDispose() {}

}
