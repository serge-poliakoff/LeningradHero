package Game.enemies;

import java.awt.Color;

import domain.DrawableObject;
import domain.Graphics.RendererBase;
import domain.Graphics.Vector2;

public class SupremaSquare extends DrawableObject {
	
	public SupremaSquare(Vector2 pos) {
		var renderer = new SupremaSquareRenderer();
		super(pos, renderer);
		
	}
	
	@Override
	protected final void Update() {
		//IO.println("Updating...");
		//this.translate(new Vector2(1, 1));
		this.rotate(Math.PI / 6);
		
	}

	@Override
	protected void onDispose() {
		// TODO Auto-generated method stub
		
	}
}
