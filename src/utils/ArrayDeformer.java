package utils;

public class ArrayDeformer {
	//rotate given square array (MxM)dimensions on quarts*90 degrees
	public int[][] rotate(int[][] source, int quarts){
		return switch(quarts) {
			case 1 -> rotate90(source);
			case 2 -> rotate180(source);
			case 3 -> rotate90(rotate180(source));
			default -> source;
		};
	}
	
	private int[][] rotate90(int[][] source){
		var n = source.length;
		var res = new int[n][n];
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				res[i][j] = source[n - 1 - j][i];
			}
		}
		
		return res;
	}
	
	private int[][] rotate180(int[][] source){
		var n = source.length;
		var res = new int[n][n];
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				res[i][j] = source[n - 1 - i][n - 1 - j];
			}
		}
		
		return res;
	}
}
