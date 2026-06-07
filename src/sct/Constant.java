package sct;

import java.awt.Color;

public class Constant {
	static int W = 1920;
	static int H = 1080;
	static int scale = 10;
	static int[] world_scale = {(W - 300) / scale, H / scale};
	//
	static double visco = 1;
	static double evaporation = 0;
	static double max_concentration = 5000;
	//
	public static Color gradient(Color color1, Color color2, double grad) {
		int r = Math.min(Math.max((int)(color1.getRed() * (1 - grad) + color2.getRed() * grad), 0), 255);
		int g = Math.min(Math.max((int)(color1.getGreen() * (1 - grad) + color2.getGreen() * grad), 0), 255);
		int b = Math.min(Math.max((int)(color1.getBlue() * (1 - grad) + color2.getBlue() * grad), 0), 255);
		return(new Color(r, g, b));
	}
	//
	public static int[][] movelist = {
		{0, -1},
		{1, -1},
		{1, 0},
		{1, 1},
		{0, 1},
		{-1, 1},
		{-1, 0},
		{-1, -1}
	};
	//
	public static int[] get_rotate_position(int rot, int[] sp){
		int[] pos = new int[2];
		pos[0] = sp[0] + Constant.movelist[rot][0];
		pos[1] = sp[1] + Constant.movelist[rot][1];
		if (pos[0] < 0) {
			pos[0] = world_scale[0] - 1;
		}else if(pos[0] >= world_scale[0]) {
			pos[0] = 0;
		}
		return(pos);
	}
}
