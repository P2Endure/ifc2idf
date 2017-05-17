package de.tuberlin.ifc2idf.geometryUtils;

public class VectorMath {
	/*TODO: for the operations between vectors to add a test to check if the dimension of the vectors match.
	 * if not throw and warning message
	 * e.g. if (u.dimension !=v.dimension)
	 * 			throw new IllegalArgumentException("Dimensions of the vectors does not match");
	*/

	//(dot)
    public static double dot(double v1[], double v2[]) {
        return ((v1[0] * v2[0]) + (v1[1] * v2[1]) + (v1[2] * v2[2]));
    }

    //(subtract of two vectors)
    public static double[] minus(double v1[], double v2[]){
    	double[] minusVector = new double[3];

    	for (int i = 0; i < 3; i++) {
    		minusVector[i] = v1[i] - v2[i];
    	}
        return minusVector;
    }

    //(addition of two vectors)
    public static double[] addition(double v1[], double v2[]){
    	double[] addVector = new double[3];

    	for (int i = 0; i < 3; i++) {
    		addVector[i] = v1[i] + v2[i];
    	}
        return addVector;
    }

    //(scalar product)
    public static double[] scalarProduct(double v1[], double r){
    	double[] scalarProdVector = new double[3];

    	for (int i = 0; i < 3; i++) {
    		scalarProdVector[i] = v1[i] * r;
    	}
        return scalarProdVector;
    }

    // (cross product)
    public static double[] crossProduct(double v1[], double v2[]){
    	double[] crossProdVector = new double[3];

    	crossProdVector[0] = v1[1] * v2[2] - v1[2] * v2[1];
    	crossProdVector[1] = v1[2] * v2[0] - v1[0] * v2[2];
    	crossProdVector[2] = v1[0] * v2[1] - v1[1] * v2[0];

    	return crossProdVector;
    }

    //(magnitude or length)
    public static double length(double v1[]){
        return (double) Math.abs(Math.sqrt(Math.pow(v1[0], 2) + Math.pow(v1[1], 2) + Math.pow(v1[2], 2)));
    }

    //(normalize the vector)
	public static double[] normalize(double v1[]) {
		double[] uNorm = new double[3];

		double l = length(v1);

		if (l != 0){
			uNorm[0] =  v1[0] / l;
			uNorm[1] =  v1[1] / l;
			uNorm[2] =  v1[2] / l;
		}

		return uNorm;
	}

	public static double[] vec2Point (double[] p1, double[] p2) {
		double[] vec = new double[3];

		for (int i = 0; i < 3; i++) {
			vec[i] = p2[i] - p1[i];
		}
		return vec;
	}

}
