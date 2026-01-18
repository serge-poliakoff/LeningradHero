package domain.Graphics;

import domain.DI.ServiceResolver;
import domain.playerInput.dragndrop.DragManager;

public record Vector2(int x, int y) {
	public Vector2 add(Vector2 b) {
		return new Vector2(x + b.x, y + b.y);
	}
	
	public Vector2 add(int bx, int by) {
		return new Vector2(x + bx, y + by);
	}
	
	public Vector2 substract(Vector2 b) {
		return new Vector2(x - b.x, y - b.y);
	}
	
	public Vector2 substract(int bx, int by) {
		return new Vector2(x - bx, y - by);
	}
	
	///returns random position vector inside a rectangle defined by parameters
	///@param leftTop left top corner of the rectangle
	///@param w width of rectangle
	///@param h height of rectangle
	public static Vector2 rndInsideRect(Vector2 leftTop, int w, int h) {
		var xOff = (int)Math.round(Math.ceil(Math.random() * w));
		var yOff = (int)Math.round(Math.ceil(Math.random() * h));
		
		return leftTop.add(xOff, yOff);
	}
	
	///checks if dropPos is inside the rectangle (inclusive) defined by pos and corner
	///@param pos left-top corner of rectangle
	///@param corner right-bottom corner of rectangle
	///@return true if first vector is inside of given rectangle, false otherwise 
	public static Boolean isInsideRect(Vector2 dropPos, Vector2 pos, Vector2 corner) {
		if (dropPos.x() >= pos.x() && dropPos.y() >= pos.y()) {
			if (dropPos.x() <= corner.x() && dropPos.y() <= corner.y()) {
				return true;
			}
		}
		return false;
	}
}
