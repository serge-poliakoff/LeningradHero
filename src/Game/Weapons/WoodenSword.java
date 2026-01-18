package Game.Weapons;


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
	
	public WoodenSword() {
		attack = new Attack(3, 1, 0);	//3 damage, 1 energy used
		
		var shape = new int[][] {
			new int[] { 0, 1},
			new int[] { 1, 0}
		};
		var renderer = new WoodenSwordRenderer();
		var initPos = ServiceResolver.getService(BaseLayoutInformation.class).bagPosition();
		super(shape, 2, initPos, renderer);
		//this.setActive(false);
		IO.println(this + " init pose: " + initPos);
		renderer.setGameObject(this);
	}

	@Override
	public Boolean dropPossible() {
		return true;
	}

	@Override
	public void use() {
		var playerResources = ServiceResolver.getService(Player.class).getResources();
		var pos = playerResources.useAttackPossible(attack);
		if(pos) {
			EventBus.PublishEvent(AttackEvent.class, new AttackEvent(this, attack));
		}else {
			EventBus.PublishEvent(NotificationEvent.class,
					new NotificationEvent(this, "Impossible to use " + attack + " - energy/mana not sufficient"));
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
	protected void onDispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onClick() {
		// TODO Auto-generated method stub
		
	}
	
}
