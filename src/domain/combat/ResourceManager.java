package domain.combat;

public class ResourceManager {
	private int health;
	private int energy;
	private int mana;
	private int protectionLevel;	//substracted from each incoming attack damage
	
	private int energyRestore;
	
	private Boolean dead;
	
	public ResourceManager(int health, int energy, int mana, int protectionLevel) {
		this.health = health;
		this.energy = energy;
		this.mana = mana;
		this.protectionLevel = protectionLevel;
		
		energyRestore = 2;
		
		dead = false;
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getProtection() {
		return protectionLevel;
	}
	
	public int getEnergy() {
		return energy;
	}
	
	//override in player to emit "PlayerDead" event
	public void inflictDamage(Attack attack) {
		health -= attack.damage() - protectionLevel;
		if (health <= 0) {
			dead = true;
		}
	}
	
	public Boolean useAttackPossible(Attack attack) {
		if (mana >= attack.manaUse() && energy >= attack.energyUse()) {
			mana -= attack.manaUse();
			energy -= attack.energyUse();
			return true;	//meant that then Enemy/Player will use the attack
		}
		return false;
	}
	
	public void boostProtection() {
		protectionLevel += 5;
	}
	
	public void repose() {
		energy += energyRestore;
	}
	
	public Boolean isDead() {
		return dead;
	}
}
