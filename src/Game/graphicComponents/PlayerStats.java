package Game.graphicComponents;

import java.awt.Color;
import java.awt.Graphics2D;

import Game.player.Player;
import Game.rendering.BaseLayoutInformation;
import domain.DI.ServiceResolver;

public class PlayerStats implements UIComponent {
	BaseLayoutInformation layout;
	
	@Override
	public void renderSelf(Graphics2D gr) {
		if (layout == null)
			layout = ServiceResolver.getService(BaseLayoutInformation.class);
		
		gr.clearRect(0, 0, layout.width(), layout.baseCellSize());
		gr.setColor(Color.white);
		var resource = ServiceResolver.getService(Player.class).getResources();
		var data = "Health: " + resource.getHealth() 
			+ " Protection: " + resource.getProtection()
			+ " Energy: " + resource.getEnergy();
		gr.drawBytes(data.getBytes(), 0, data.length(), layout.baseCellSize(), layout.baseCellSize());
	}

}
