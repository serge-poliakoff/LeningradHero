package domain.menu;

import java.awt.Graphics2D;

import com.github.forax.zen.KeyboardEvent;

import domain.DrawableObject;

public interface IMenu {
	public void renderSelf(Graphics2D gr);
	
	public boolean handleKey(KeyboardEvent ev);
	
	public Boolean getDisposed();
	
	
}
