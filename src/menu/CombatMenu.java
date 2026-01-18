package menu;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;
import com.github.forax.zen.KeyboardEvent.Key;
import com.github.forax.zen.PointerEvent.Action;

import Game.Weapons.WoodenSword;
import Game.bag.DropItemEvent;
import Game.bag.DropStockEvent;
import Game.bag.GI.BagGI;
import Game.bag.items.Baggable;
import Game.enemies.Enemy;
import Game.player.Player;
import Game.player.PlayerDeadEvent;
import Game.rendering.BaseBackgroundRenderer;
import Game.rendering.BaseLayoutInformation;
import domain.DI.ServiceResolver;
import domain.Graphics.Vector2;
import domain.combat.Attack;
import domain.combat.IEnemy;
import domain.combat.events.AttackEvent;
import domain.eventing.EventBus;
import domain.map.LiberateRoomEvent;
import domain.menu.MenuBase;
import domain.menu.OpenMenuEvent;
import domain.playerInput.clicking.OnClickEvent;

public class CombatMenu extends MenuBase {
	private List<Enemy> enemies;
	private Supplier<List<Baggable>> reward;
	private Enemy inSight;
	
	private BaseLayoutInformation layout;
	private int cellSize;
	private BaseBackgroundRenderer bg;

	private boolean playerDead;
	
	private Vector2 randomEnemyPos() {
		var layout = ServiceResolver.getService(BaseLayoutInformation.class);
		return Vector2.rndInsideRect(
				new Vector2(layout.getXCenterUnits(), layout.getUnitBackgroundWallBottom() - 2),
				layout.getXCenterUnits() / 3, 5);
	}
	
	public CombatMenu(Supplier<List<Enemy>> enemies, Supplier<List<Baggable>> reward){
		var ens = enemies.get().toArray(new Enemy[]{});
		this(ens, reward);
	}

	///creates a combat menu with given enemies and a reward, which would be given at the end of combat
	public CombatMenu(Enemy enemies[], Supplier<List<Baggable>> reward) {
		Objects.requireNonNull(enemies);
		Objects.requireNonNull(reward);
		
		layout = ServiceResolver.getService(BaseLayoutInformation.class);
		
		this.enemies = new ArrayList<Enemy>();
		for (var enemy : enemies) {
			this.enemies.add(enemy);
			enemy.moveTo(randomEnemyPos());
			addObject(enemy);
		}
		
		inSight = enemies[0];
		
		Consumer<AttackEvent> attackListener = x -> {
			var attack = ((AttackEvent)x).attack();
			takeAttack(attack);
		};
		addListener(AttackEvent.class, attackListener);
		
		Consumer<OnClickEvent> clickListener = ev -> {
			onClicked(ev);
		};
		addListener(OnClickEvent.class, clickListener);
		
		this.reward = reward;
		
		bg = ServiceResolver.getService(BaseBackgroundRenderer.class);

		playerDead = false;
	}
	
	@Override
	protected void onAwake() {
		ServiceResolver.getService(BagGI.class).setActive(true);
	}
	
	private void onClicked(OnClickEvent ev) {
		var captured = ev.issuer();
		if (!(captured instanceof Enemy)) return;
		
		if (enemies.contains(captured)) {
			if (inSight != null) inSight.setSelected(false);
			inSight = (Enemy) captured;
			inSight.setSelected(true);
		}
	}
	
	private void takeAttack(Attack att) {
		if (inSight == null) {
			return;
		}
		inSight.inflictDamage(att);
	}
	
	@Override
	public void onRender(Graphics2D gr) {
		bg.render(gr);
	}
	
	public void makeCycle() {
		//removing dead
		var it = enemies.iterator();
		int numAlive = 0;
		while (it.hasNext()) {
			var cur = it.next();
			if (cur.isDead()) {
				if (cur == inSight)
					inSight = null;
				it.remove();
				cur.dispose();
				removeObject(cur);
			}else {
				cur.attack();
				numAlive++;
			}
		}
		
		var player_res = ServiceResolver.getService(Player.class).getResources();
		player_res.repose();
		if (player_res.isDead()) {
			EventBus.PublishEvent(PlayerDeadEvent.class, new PlayerDeadEvent());
		}
		if (numAlive == 0) {
			dispose();
		}
	}
	
	@Override
	public boolean handleKey(KeyboardEvent ev) {
		if (ev.action() != KeyboardEvent.Action.KEY_PRESSED) {
			return false;
		}
		
		var key = ev.key();
		switch(key) {
			case Key.SPACE:
				if (!ServiceResolver.getService(BagGI.class).getActive()){
					ServiceResolver.getService(BagGI.class).setActive(true);
				}else{
					IO.println("Boosting...");
					var playerResources = ServiceResolver.getService(Player.class).getResources();
					playerResources.boostProtection();
				}
				return true;
			case Key.P:
				makeCycle();
				return true;
			default: return false;
		}
	}

	@Override
	protected void onMenuDispose() {
		EventBus.PublishEvent(LiberateRoomEvent.class, new LiberateRoomEvent());
		for (var rewardItem : reward.get()){
			rewardItem.setActive(true);
			EventBus.PublishEvent(DropItemEvent.class, new DropItemEvent(rewardItem));
		}
	}

}
