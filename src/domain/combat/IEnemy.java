package domain.combat;

import domain.DrawableObject;

public interface IEnemy /*extends DrawableObject*/{
	
	public void attack();
	
	public void inflictDamage(Attack attack);
	
	//checks if enemy is still alive after getting damage
	public boolean isDead();
}
