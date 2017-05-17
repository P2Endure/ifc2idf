package de.tuberlin.ifc2idf.geometryUtils;

public class UtilsMath {


	/**
	 * check to see if a double value is a finite number
	 * is not NaN and is not Infinite
	 * @param value
	 * @return True or False
	 */
	public static boolean IsFinite(double value) {
		return !Double.isNaN(value) && !Double.isInfinite(value);
	}

	/**
	 * to move in UtilsMath class
	 * compute the distance between two points
	 * @param centroid1 which is a vertex(x, y, z)
	 * @param centroid2 which is a vertex(x, y, z)
	 * @return the distance between centroid1 and centroid 2
	 */
	public static double pointsDist (Vertex centroid1, Vertex centroid2) {
		double distance;

		distance = Math.sqrt(Math.pow(centroid1.getX() - centroid2.getX(), 2) +
							 Math.pow(centroid2.getY() - centroid2.getY(), 2) +
							 Math.pow(centroid1.getZ() - centroid2.getZ(), 2));

		return distance;
	}

	public static double pointsDistD (double[] point1, double[] point2) {
		double distance;

		distance = Math.sqrt(Math.pow(point1[0] - point2[0], 2) +
							 Math.pow(point1[1] - point2[1], 2) +
							 Math.pow(point1[2] - point2[2], 2));

		return distance;
	}
}
