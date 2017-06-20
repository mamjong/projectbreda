package nl.gemeente.breda.bredaapp.util;

public class Converter {
	
	public ResultValue convertLatLongToRD(double latitude, double longitude) {
		int referenceRdX = 155000;
		int referenceRdY = 463000;
		
		double referenceWgs84X = 52.15517;
		double referenceWgs84Y = 5.387206;
		
		double[][] rpq = new double[4][5];
		
		rpq[0][1] = 190094.945;
		rpq[1][1] = -11832.228;
		rpq[2][1] = -114.221;
		rpq[0][3] = -32.391;
		rpq[1][0] = -0.705;
		rpq[3][1] = -2.340;
		rpq[0][2] = -0.008;
		rpq[1][3] = -0.608;
		rpq[2][3] = 0.148;
		
		double[][] spq = new double[4][5];
		
		spq[0][1] = 0.433;
		spq[0][2] = 3638.893;
		spq[0][4] = 0.092;
		spq[1][0] = 309056.544;
		spq[2][0] = 73.077;
		spq[1][2] = -157.984;
		spq[3][0] = 59.788;
		spq[2][2] = -6.439;
		spq[1][1] = -0.032;
		spq[1][4] = -0.054;
		
		double d_lat = (0.36 * (latitude - referenceWgs84X));
		double d_long = (0.36 * (longitude - referenceWgs84Y));
		
		double calc_lat = 0;
		double calc_long = 0;
		
		for (int p = 0; p < 4; p++) {
			for (int q = 0; q < 5; q++) {
				calc_lat += rpq[p][q] * Math.pow(d_lat, p) * Math.pow(d_long, q);
				calc_long += spq[p][q] * Math.pow(d_lat, p) * Math.pow(d_long, q);
			}
		}
		
		double rd_x = (referenceRdX + calc_lat);
		double rd_y = (referenceRdY + calc_long);
		
		return new ResultValue(rd_x, rd_y);
	}
	
	public ResultValue convertRDToLatLong(double x, double y) {
		int referenceRdX = 155000;
		int referenceRdY = 463000;
		
		double dX = (x - referenceRdX) * (Math.pow(10, -5));
		double dY = (y - referenceRdY) * (Math.pow(10, -5));
		
		double sumN =
				(3235.65389 * dY) +
						(-32.58297 * Math.pow(dX, 2)) +
						(-0.2475 * Math.pow(dY, 2)) +
						(-0.84978 * Math.pow(dX, 2) * dY) +
						(-0.0655 * Math.pow(dY, 3)) +
						(-0.01709 * Math.pow(dX, 2) * Math.pow(dY, 2)) +
						(-0.00738 * dX) +
						(0.0053 * Math.pow(dX, 4)) +
						(-0.00039 * Math.pow(dX, 2) * Math.pow(dY, 3)) +
						(0.00033 * Math.pow(dX, 4) * dY) +
						(-0.00012 * dX * dY);
		
		double sumE =
				(5260.52916 * dX) +
						(105.94684 * dX * dY) +
						(2.45656 * dX * Math.pow(dY, 2)) +
						(-0.81885 * Math.pow(dX, 3)) +
						(0.05594 * dX * Math.pow(dY, 3)) +
						(-0.05607 * Math.pow(dX, 3) * dY) +
						(0.01199 * dY) +
						(-0.00256 * Math.pow(dX, 3) * Math.pow(dY, 2)) +
						(0.00128 * dX * Math.pow(dY, 4)) +
						(0.00022 * Math.pow(dY, 2)) +
						(-0.00022 * Math.pow(dX, 2)) +
						(0.00026 * Math.pow(dX, 5));
		
		double referenceWgs84X = 52.15517;
		double referenceWgs84Y = 5.387206;
		double latitude = referenceWgs84X + (sumN / 3600);
		double longitude = referenceWgs84Y + (sumE / 3600);
		
		return new ResultValue(latitude, longitude);
	}
	
	public int toInt(double value) {
		return (int) Math.round(value);
	}
	
	public float toFloat(double value) {
		return (float) value;
	}
	
	public static boolean isInt(String input) {
		try {
			Integer.parseInt(input);
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
}