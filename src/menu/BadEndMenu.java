package menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.KeyboardEvent.Key;

import Game.rendering.BaseLayoutInformation;
import domain.DI.ServiceResolver;
import domain.menu.MenuBase;

public class BadEndMenu extends MenuBase {
	
	private BaseLayoutInformation layout;
	
	public BadEndMenu() {
		super();
		this.layout = ServiceResolver.getService(BaseLayoutInformation.class);
	}
	
	@Override
	public void onRender(Graphics2D gr) {
		gr.setBackground(Color.black);
		gr.setColor(Color.white);
		String[] lastAvertissement = new String[] {"You died !", "Better luck next time...", "Press ESCAPE to quit the game"};
		int h = layout.height() / 2 - 15;
		for(var s : lastAvertissement) {
			gr.drawBytes(s.getBytes(), 0, s.length(), 
					layout.width() / 2, h);
			h += 15;
		}
	}

	@Override
	public boolean handleKey(KeyboardEvent ev) {
		if (ev.action() != KeyboardEvent.Action.KEY_PRESSED) {
			return false;
		}
		
		var key = ev.key();
		switch(key) {
			case Key.ESCAPE:
				dispose();
				return true;
			default: return false;
		}
	}

	@Override
	protected void onMenuDispose() {
		
	}

}
