package Game.Weapons;


import java.util.Objects;

import Game.bag.items.Baggable;
import Game.graphicComponents.NotificationEvent;
import Game.player.Player;
import Game.rendering.BaseLayoutInformation;
import domain.DI.ServiceResolver;
import domain.Graphics.Vector2;
import domain.combat.Attack;
import domain.combat.events.AttackEvent;
import domain.eventing.EventBus;

public class WoodenSword extends Baggable {
	private Attack attack;
	private int countUsage;

	private final int maxUsageCount = 2;

	public WoodenSword() {
		attack = new Attack(3, 1, 0);	//3 damage, 1 energy used
		String[] description = new String[]{
			"A sword, made of wood ?",
			"Maybe rather a toothstick or ",
			" an old rotten tree brach !",
			"Can be used only twice"
		};
		var shape = new int[][] {
			new int[] { 0, 1},
			new int[] { 1, 0}
		};
		var renderer = new WoodenSwordRenderer();
		var initPos = ServiceResolver.getService(BaseLayoutInformation.class).bagPosition();
		super(shape, 2, description, initPos, renderer);
		//this.setActive(false);
		//IO.println(this + " init pose: " + initPos);
		renderer.setGameObject(this);
		countUsage = 0;
	}

	@Override
	public Boolean dropPossible() {
		return true;
	}

	@Override
	public void use() {
		if (!this.getActive()) return;

		var playerResources = ServiceResolver.getService(Player.class).getResources();
		var pos = playerResources.useAttackPossible(attack);
		if(pos) {
			EventBus.PublishEvent(AttackEvent.class, new AttackEvent(this, attack));
			countUsage ++;
		}
		if (countUsage >= maxUsageCount){
			var bag = Objects.requireNonNull(getBag());
			this.setActive(false);
			bag.dropItem(this);
			dispose();
		}
	}
	
	@Override
	protected void Update() {
		
	}
	
	@Override
	public Boolean rotatePossible() {
		return true;
	}

	@Override
	protected void onObjectDispose() {

	}

	@Override
	protected void onClick() {
		
	}
	
}
