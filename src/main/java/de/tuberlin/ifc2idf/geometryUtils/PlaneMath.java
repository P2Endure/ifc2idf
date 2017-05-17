package de.tuberlin.ifc2idf.geometryUtils;

import org.bimserver.models.geometry.GeometryInfo;

public class PlaneMath {


	//TODO add contructor for the plane
	//TODO add class DistanceToPlane
	//TODO add class ProjectPoint
	//TODO add class project vector
	//TODO add class MovePlane

	private static double planeTolerance = 1.0e-06;
	private static double doubleMax = 1.0e+99;

	// Given a line defined by the two points point1,point2; and a plane defined by the
	// normal n2 and point p0, compute an intersection. The parametric
	// coordinate along the line is returned in t, and the coordinates of
	// intersection are returned in x. A zero is returned if the plane and line
	// do not intersect between (0<=t<=1). If the plane and line are parallel,
	// zero is returned and t is set to VTK_LARGE_DOUBLE.
	public static DoubleResult intersectionPlaneLine(
			double point1[], double point2[],
			double n2[], double p0[],
			double t, double x[]) {
		double num, den;
		double[] p21 = new double[3];
		double fabsden, fabstolerance;
		DoubleResult result = new DoubleResult(0, 0.0);

		//compute line vector
		p21[0] = point2[0] -point1[0];
		p21[1] = point2[1] -point1[1];
		p21[2] = point2[2] -point1[2];

		/**
		 * Compute denominator
		 * if ~0 -> line and plane are parallel
		 */

		num = VectorMath.dot(n2, p0) - (n2[0] * point1[0] + n2[1] * point1[1] + n2[2] * point1[2]);
		den = n2[0] * p21[0] + n2[1] * p21[1] + n2[2] * p21[2];

		/**
		 * if the denominator with respect to numerator is "zero", then the line and
		 * the plane are considered parallel
		 */

		if (den < 0.0) {

			fabsden = -den;

		}else {
			fabsden = den;
		}

		if (num < 0.0) {
			fabstolerance = -num * planeTolerance;
		} else {
			fabstolerance = num * planeTolerance;
		}

		if (fabsden <= fabstolerance) {
			t = doubleMax;
			result.setIntegerVal(0);
			result.setDoubleVal(t);
			return result;
		}

		//valid intersection
		t = num / den;
		result.setDoubleVal(t);

		x[0] = point1[0] + t * p21[0];
		x[1] = point1[1] + t * p21[1];
		x[2] = point1[2] + t * p21[2];

		if (t >= 0.0 && t <= 1.0) {
			result.setIntegerVal(1);
			return result;
		} else {
			result.setIntegerVal(0);
			return result;
		}
	}


	/**
	 *
	 * @param vertex <double[3]>: represent the vertex for which we compute the distance to the plane
	 * @param planeN <Vector>: the plane normal n = (A, B, C)
	 * @param planeOrigin <double[3]> : any point P = (x, y, z) on the plane which satisfy the following: A*x0 + B * y0 + C * z0 + D = 0
	 * @return
	 */
	public static double pointPlaneDist (double[] vertex, Vector planeN, double[] planeOrigin) {
		double distance = 0;
		//point on plane
		double x0 = planeOrigin[0];
		double y0 = planeOrigin[1];
		double z0 = planeOrigin[2];
		//plane normal
		double A = planeN.X;
		double B = planeN.Y;
		double C = planeN.Z;
		double D = -(A * x0 + B * y0 + C * z0);

		distance = (A * vertex[0] + B * vertex[1] + C * vertex[2] + D) / Math.sqrt(Math.pow(A, 2) + Math.pow(B, 2) + Math.pow(C, 2)) + D;

		return distance;
	}
	/*
	 * Given a line defined by the two points point1,point2; and a plane defined by the
	 * normal n2 and point p0, compute an intersection.
	 * the coordinates of intersection are returned in x.
	 */


	/*
	 * Plane: A * x + B * y + C * z + D = 0
	 * Line:  <double[]> P1(x1, y1, z1); P2(x2, y2, z2)
	 */

//	public static double[] intersectPoint_LinePlane (
//			double point1[],
//			double point2[],
//			double N2[],
//			double pointP[]) {
//
//		//the parametric equation of the line
//		/*
//		 * x = x1 + (x2 - x1) * t
//		 * y = y1 + (y2 - y1) * t
//		 * z = z1 + (z2 - z1) * t
//		 */
//		double x1 = point1[0];
//		double y1 = point1[1];
//		double z1 = point1[2];
//
//
//		double x2 = point2[0];
//		double y2 = point2[1];
//		double z2 = point2[2];
//
//		//equation for the plane is:
//		/*
//		 * A * x + B * y + C * z + D = 0
//		 */
//		double A = N2[0];
//		double B = N2[1];
//		double C = N2[2];
//
//		double x = pointP[0];
//		double y = pointP[1];
//		double z = pointP[2];
//
//
//		double D = -A * x - B * y - C * z;
//		System.out.println("D is: " + D);
//		double t = (A*x1 + B * y1 + C * z1 + D)/(A * (x2 - x1) + B * (y2 - y1) + C * (z2 - z1));
//		System.out.println("t is: " + t);
//		double[] intersPo = new double[3];
//
//		intersPo[0] = x1 + (x2 - x1) * t;
//		intersPo[1] = y1 + (y2 - y1) * t;
//		intersPo[2] = z1 + (z2 - z1) * t;
//
//		System.out.println(intersPo[0] + "," + intersPo[1] + "," +  intersPo[2]);
//
//		return intersPo;
//	}


	public static double[] intersect_point_LinePlane(
			double point1[],
			double point2[],
			double n2[], double p0[]) {

		double[] x = new double[3];
		double t;
		double num, den;
		double[] p21 = new double[3];
		double fabsden, fabstolerance;

		//compute line vector
		p21[0] = point2[0] -point1[0];
		p21[1] = point2[1] -point1[1];
		p21[2] = point2[2] -point1[2];

		/**
		 * Compute denominator
		 * if ~0 -> line and plane are parallel
		 */

		num = VectorMath.dot(n2, p0) - (n2[0] * point1[0] + n2[1] * point1[1] + n2[2] * point1[2]);
		den = n2[0] * p21[0] + n2[1] * p21[1] + n2[2] * p21[2];

		/**
		 * if the denominator with respect to numerator is "zero", then the line and
		 * the plane are considered parallel
		 */

		if (den < 0.0) {
			fabsden = -den;
		}else {
			fabsden = den;
		}

		if (num < 0.0) {
			fabstolerance = -num * planeTolerance;
		} else {
			fabstolerance = num * planeTolerance;
		}

		if (fabsden <= fabstolerance) {
			t = doubleMax;
		}

		//valid intersection
		t = num / den;

		x[0] = point1[0] + t * p21[0];
		x[1] = point1[1] + t * p21[1];
		x[2] = point1[2] + t * p21[2];

		System.out.println(x[0] + "," + x[1] + "," +  x[2]);


		return x;
	}


	//applicable only for horizontal planes
	public static boolean planeAABB_intersection (GeometryInfo geometryInfo, double n2[]) {
		//we've got the horizontal plane of a Building storey defined by the normal n2
		//and the bounding box of teh element
		//since the plane is horizontal, there is necessary to check only on z direction

		if (geometryInfo == null) {
			return true;
		}

		return (geometryInfo.getMaxBounds().getZ() > n2[2] &&
				geometryInfo.getMinBounds().getZ() < n2[2]);

	}

	//to be used for return two values
	public static class DoubleResult {
		private int integerVal;
		private double doubleVal;

		public DoubleResult (
				int integerVal,
				double doubleVal) {

			this.integerVal = integerVal;
			this.doubleVal = doubleVal;
		}

		public int getIntegerVal() {
			return integerVal;
		}
		public void setIntegerVal(int integerVal) {
			this.integerVal = integerVal;
		}
		public double getDoubleVal() {
			return doubleVal;
		}
		public void setDoubleVal(double doubleVal) {
			this.doubleVal = doubleVal;
		}

	}


}
