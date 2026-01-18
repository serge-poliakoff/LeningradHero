package Game.player;

import java.awt.Color;
import java.awt.Graphics2D;

import Game.rendering.BaseLayoutInformation;
import domain.DrawableObject;
import domain.DI.ServiceResolver;

public class Player extends DrawableObject {
	private PlayerResources resources;
	
	public Player() {
		resources = new PlayerResources(6, 3, 0, 0);
		var renderer = new PlayerRenderer();
		var pos = ServiceResolver.getService(BaseLayoutInformation.class).playerPosition();
		super(pos, renderer);
	}
	
	public PlayerResources getResources() {
		return resources;
	}

	@Override
	protected void onObjectDispose() {
			
	}

}
