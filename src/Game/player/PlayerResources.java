package Game.player;

import domain.combat.ResourceManager;

public class PlayerResources extends ResourceManager {

	public PlayerResources(int health, int energy, int mana, int protectionLevel) {
		super(health, energy, mana, protectionLevel);
		// TODO Auto-generated constructor stub
	}

	public void heal(int amount){
		this.setHealth(this.getHealth() + amount);
	}

}
