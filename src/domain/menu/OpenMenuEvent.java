package domain.menu;

import java.util.Objects;


///event of opening new menu
public record OpenMenuEvent(MenuBase menu, boolean disposeLast) {
	
	///@param menu new menu to open
	///@param disposeLase indicates if current menu must be disposed, or just hidden
	public OpenMenuEvent {
		Objects.requireNonNull(menu);
	}
}
