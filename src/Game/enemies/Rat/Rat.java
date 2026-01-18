package Game.enemies.Rat;

import java.awt.Color;
import java.awt.Graphics2D;

import domain.combat.IEnemy;
import domain.combat.ResourceManager;
import domain.DrawableObject;
import domain.DI.ServiceResolver;
import domain.Graphics.RendererBase;
import domain.Graphics.Vector2;
import domain.combat.Attack;
import Game.enemies.Enemy;
import Game.player.Player;
import Game.player.PlayerResources;

public class Rat extends Enemy{
	
	private Attack attacks[];
	private ResourceManager rm;
	private String name;
	private int initialHealth;
	private Vector2 colliderDims;
	
	public Rat(Vector2 pos) {
		var renderer = new RatRenderer();
		super(pos, renderer);
		initialHealth = 5;
		rm = new ResourceManager(initialHealth, 1, 0, 0);
		attacks = new Attack[] {
				new Attack(1, 0, 0)
		};
		name = "Rat (small)";
		renderer.setResourceManager(rm);
		renderer.setName(name);
		colliderDims = new Vector2(4, 4);
	}

	@Override
	public void attack() {
		var attIndex = Math.round(Math.floor(Math.random() * attacks.length));
		
		var result = rm.useAttackPossible(attacks[(int) attIndex]);
		if (result) {
			var pl_res = ServiceResolver.getService(Player.class).getResources();
			pl_res.inflictDamage(attacks[(int) attIndex]);
		}else {
			rm.boostProtection();
		}
	}
	

	@Override
	public void inflictDamage(Attack attack) {
		rm.inflictDamage(attack);
	}

	@Override
	public boolean isDead() {
		return rm.isDead();
	}

	@Override
	protected void onObjectDispose() {
		
	}

	@Override
	protected Boolean isClicked(Vector2 click) {
		if (Vector2.isInsideRect(click, getAbsolutePosition(), getAbsolutePosition().add(colliderDims))) {
			return true;
		}
		return false;
	}

	@Override
	public void setSelected(boolean selected) {
		((RatRenderer)getRenderer()).setSelected(selected);
	}
	
}
