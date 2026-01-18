package Game.rendering;

import domain.Graphics.Vector2;

public record BaseLayoutInformation(int width, int height, int baseCellSize, Vector2 bagPosition) {
	
	///@returns Y coordinates of the bottom of background wall, rendered in most menus (in pixels)
	public int getBackgroundWallBottom() {
		return height / 4 * 3;
	}
	
	///@returns Y coordinates of the top of background wall, rendered in most menus (in pixels)
	public int getBackgroundWallTop() {
		return height / 2;
	}
	
	///@returns X coordinates of the center of the screen (in units)
	public int getXCenterUnits() {
		return width / baseCellSize / 2;
	}
	
	///@returns Y coordinates of the bottom of background wall, rendered in most menus (in units)
	public int getUnitBackgroundWallBottom() {
		return getBackgroundWallBottom() / baseCellSize;
	}
	
	///@returns player position, in units
	public Vector2 playerPosition() {
		return new Vector2(
			width / baseCellSize / 6,
			getBackgroundWallBottom() / baseCellSize - 4);
	}
	
}
