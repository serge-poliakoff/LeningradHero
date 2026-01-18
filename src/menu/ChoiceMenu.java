package menu;

import java.awt.Graphics2D;

import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

import domain.menu.MenuBase;

public class ChoiceMenu extends MenuBase {
	
	//let you choose between several objects, only one of which you can get
	//at the end opens a Bag Menu with the chosen item in hand, 
	//another object is lost forever
	
	@Override
	public void onRender(Graphics2D gr) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean handleKey(KeyboardEvent ev) {
		return false;
		// TODO Auto-generated method stub

	}

}
