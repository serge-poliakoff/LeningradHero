package Game.enemies.Rat;

import java.awt.Color;
import java.util.Objects;

import domain.Errors.RenderException;
import domain.Graphics.RendererBase;
import domain.combat.ResourceManager;

class RatRenderer extends RendererBase {
	
	private ResourceManager rm;
	private String name;
	private int initialHealth;
	private boolean selected;
	
	public RatRenderer() {
		super();
		selected = false;
	}
	
	public void setResourceManager(ResourceManager rm) {
		Objects.requireNonNull(rm);
		this.rm = rm;
		this.initialHealth = rm.getHealth();
	}
	
	public void setName(String name) {
		Objects.requireNonNull(name);
		this.name = name;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	@Override
	protected void onRender() {
		if (rm == null) throw new RenderException("No resource manager was provided");
		
		var gr = getGraphics();
		var cellSize = getCellSize();
		
		if (rm.getHealth() < 2)
			gr.setColor(new Color(255,205,195));
		else
			gr.setColor(new Color(205,225,195));
		gr.fillRect(cellSize, cellSize, cellSize * 2, cellSize * 2);
		gr.fillRect(cellSize / 2, cellSize * 2, cellSize * 2, cellSize);
		
		if (selected) {
			gr.setColor(Color.white);
			gr.drawRect(0, 0, cellSize * 3, cellSize * 3);
		}
		
		//name & health bar fond
		gr.setColor(Color.white);
		gr.drawBytes(name.getBytes(), 0, name.length(), cellSize / 2, 12);
		gr.fillRect(0, 20, cellSize * 3, 5);
		
		gr.setColor(Color.green);
		var hpProc = (int)Math.round((rm.getHealth() + 0.0) / initialHealth * cellSize * 3);
		gr.fillRect(0, 20, hpProc , 5);
	}

}
