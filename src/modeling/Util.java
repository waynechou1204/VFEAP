package modeling;

import java.util.ArrayList;
import java.util.Collections;

public class Util {
	public static double getLength(double p1x, double p1y, double p1z, double p2x,
            double p2y, double p2z) {
        double len = 0;
        len += Math.pow(p1x - p2x, 2);
        len += Math.pow(p1y - p2y, 2);
        len += Math.pow(p1z - p2z, 2);
        len = Math.sqrt(len);
        return len;
    }
	
	public static float getMidValueF(ArrayList<Float> arr_scale) {
        float min = Collections.min(arr_scale);
        float max = Collections.max(arr_scale);
        return (min + max) / 2;
    }

	public static float getSpanValueF(ArrayList<Float> arr_scale) {
        float min = Collections.min(arr_scale);
        float max = Collections.max(arr_scale);
        return (max - min);
    }

	public static float getMaxOfThreeF(float a, float b, float c) {
        float max = Math.max(a, b);
        max = Math.max(max, c);
        return max;
    }

	
}
