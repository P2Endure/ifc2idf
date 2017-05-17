package de.tuberlin.ifc2idf.geometryUtils;

public class TrianglesMath {


	/**
	 * function to compute the plane normal of a triangle
	 * */
	public static double[] getTriPlNormal (double vertex0[], double vertex1[], double vertex2[]) {
		double[] E1 = VectorMath.minus(vertex1, vertex0);
		double[] E2 = VectorMath.minus(vertex2, vertex0);
		double[] N1 = VectorMath.crossProduct(E1, E2);

		return N1;
	}

	/**
	 * return the centroid of a triangle
	 * @param triangle(verttex1, vertex2, vertex3)
	 * @return the centroid as a Vertex
	 */
	public static Vertex triCentroid (Triangles triangle) {
		Vertex centroid = new Vertex(0, 0, 0);

		centroid.setX((float) ((triangle.getVertex0()[0] + triangle.getVertex1()[0] + triangle.getVertex2()[0])/3));
		centroid.setY((float) ((triangle.getVertex0()[1] + triangle.getVertex1()[1] + triangle.getVertex2()[1])/3));
		centroid.setZ((float) ((triangle.getVertex0()[2] + triangle.getVertex1()[2] + triangle.getVertex2()[2])/3));

		return centroid;
	}


	/**
	 * Compute the minimum distance from one triangle to the other
	 * brute-force algorithm -
	 * is based on computing the distance from the points of the triangle 1 to the plane defined by triangle 2
	 * @param triangle1 <Triangles>: defined as (vertex1, vertex2, vertex3)
	 * @param triangle2 <Triangles>: defined as (vertex1, vertex2, vertex3)
	 * @return <double> return the min distance from the triangle1 vertices to the plane defined by triangel2
	 */
//	public static double minDistTriTri (Triangles tri1, Triangles tri2) {
//
//		double[] vertex11 = tri2.getVertex1();
//		double[] vertex12 = tri2.getVertex2();
//		double[] vertex13 = tri2.getVertex3();
//
//		//for triangle 1
//		Vector N1 = TrianglesMath.getTriPlNormal(VectorMath.vect(vertex11), VectorMath.vect(vertex12), VectorMath.vect(vertex13));
//		double d1 = -VectorMath.dot(N1, VectorMath.vect(vertex11));
//
//		//compute the signed distances
//		double dist0 = VectorMath.dot(N1, VectorMath.vect(tri1.getVertex1())) + d1;
//		double dist1 = VectorMath.dot(N1, VectorMath.vect(tri1.getVertex2())) + d1;
//		double dist2 = VectorMath.dot(N1, VectorMath.vect(tri1.getVertex3())) + d1;
//
//
//		if (dist0 == dist1 && dist0 == dist2) {
//			return Math.abs(dist0);
//		}
//
//
//		if (Math.abs(dist0) < Math.abs(dist1)) {
//			if (Math.abs(dist0) < Math.abs(dist2)) {
//				return Math.abs(dist0);
//			} else {
//				 return Math.abs(dist2);
//			}
//		} else {
//			if (Math.abs(dist1) < Math.abs(dist2)) {
//				return Math.abs(dist1);
//			} else {
//				return Math.abs(dist2);
//			}
//		}
//	}

	/**
	 * The orthogonal distance of the tri2 vertices to the plane of tri1
	 * @param tri1 <Triangle> triangle of the first element
	 * @param tri2 <Traingle> triangle of the second element
	 * @return <double> return the minimum distance as absolute value
	 */
	public static double minDistTriTri (Triangles tri1, Triangles tri2) {

		double[] planeN = getTriPlNormal(tri1.getVertex0(), tri1.getVertex1(), tri1.getVertex2());

		Vertex centerPoint = triCentroid(tri1);


		double[] pointNP = new double[3];
		pointNP[0] = centerPoint.getX();
		pointNP[1] = centerPoint.getY();
		pointNP[2] = centerPoint.getZ();

		double[] vertex1 = tri2.getVertex0();
		double[] vertex2 = tri2.getVertex1();
		double[] vertex3 = tri2.getVertex2();

//		double dist1 = PlaneMath.pointPlaneDist(vertex1, planeN, pointOnPlane);
//		double dist2 = PlaneMath.pointPlaneDist(vertex2, planeN, pointOnPlane);
//		double dist3 = PlaneMath.pointPlaneDist(vertex3, planeN, pointOnPlane);

		//manual implementation of Point to plane distance

		//PlaneN is defined by the equation: A * x + B * y + C * Z + D = 0
		double A = planeN[0];
		double B = planeN[1];
		double C = planeN[2];

		//(x, y, z) are the coordinates of a point on planeN. we will use the ceontroid of the triangle 1 which is pointNP
		double D = - (A * pointNP[0] + B * pointNP[1] + C * pointNP[2]);

		double t1 = -(A * vertex1[0] + B * vertex1[1] + C * vertex1[2] + D)/(A * A + B * B + C * C);
		double t2 = -(A * vertex2[0] + B * vertex2[1] + C * vertex2[2] + D)/(A * A + B * B + C * C);
		double t3 = -(A * vertex3[0] + B * vertex3[1] + C * vertex3[2] + D)/(A * A + B * B + C * C);

		//orthogonal projection of vertex1 of triangle 2 on the plane defined by triangle 1
		double[] vertex1o = new double[3];
		vertex1o[0] = A * t1 + vertex1[0];
		vertex1o[1] = B * t1 + vertex1[1];
		vertex1o[2] = B * t1 + vertex1[2];

		//orthogonal projection of vertex2 of triangle 2 on the plane defined by triangle 1
		double[] vertex2o = new double[3];
		vertex2o[0] = A * t2 + vertex2[0];
		vertex2o[1] = B * t2 + vertex2[1];
		vertex2o[2] = B * t2 + vertex2[2];

		//orthogonal projection of vertex3 of triangle 2 on the plane defined by triangle 1
		double[] vertex3o = new double[3];
		vertex3o[0] = A * t3 + vertex3[0];
		vertex3o[1] = B * t3 + vertex3[1];
		vertex3o[2] = B * t3 + vertex3[2];

		//computing the distanced from triangle 2 vertices to its orthogonal projections
//		double dist1 = UtilsMath.pointsDistD(vertex1, vertex1o);
//		double dist2 = UtilsMath.pointsDistD(vertex2, vertex2o);
//		double dist3 = UtilsMath.pointsDistD(vertex3, vertex3o);


//		//distance from vertex 1 of triangle 2 to the planeN defined by triangle1 - signed distances
		double dist1 = (A * vertex1[0] + B * vertex1[1] + C * vertex1[2] + D) / (Math.sqrt(A * A + B * B + C * C ));
		double dist2 = (A * vertex2[0] + B * vertex2[1] + C * vertex2[2] + D) / (Math.sqrt(A * A + B * B + C * C ));
		double dist3 = (A * vertex3[0] + B * vertex3[1] + C * vertex3[2] + D) / (Math.sqrt(A * A + B * B + C * C ));

		return Math.min(Math.min(Math.abs(dist1), Math.abs(dist2)), Math.abs(dist3));
	}

	/**
	 * get the sphere bounding box. Computing the radius of the sphere
	 * @param tri1 <Triangles> : the input triangle
	 * @return <double> : radius of the bounding sphere
	 */
	public static double triSphere (Triangles tri1) {
		//the radius of the bounding sphere of the triangle tri1
		double radius = 0;
		Vertex centroid;
		Vertex vertex1 = new Vertex(tri1.getVertex0()[0], tri1.getVertex0()[1], tri1.getVertex0()[2]);
		Vertex vertex2 = new Vertex(tri1.getVertex1()[0], tri1.getVertex1()[1], tri1.getVertex1()[2]);
		Vertex vertex3 = new Vertex(tri1.getVertex2()[0], tri1.getVertex2()[1], tri1.getVertex2()[2]);

		//get the center point of the triangle
		centroid = triCentroid(tri1);

		//get the distances from the center point to the triangle vertices
		double dc1 = UtilsMath.pointsDist(centroid, vertex1);
		double dc2 = UtilsMath.pointsDist(centroid, vertex2);
		double dc3 = UtilsMath.pointsDist(centroid, vertex3);

		//get the radius as the maximum distance
		radius = Math.max(Math.max(dc1, dc2), dc3);

		return radius;
	}
	/**
	 * construct the bounding box of a triangle
	 * @param tri <Triangles>
	 * @return <aabbTri> object containing the (min, max) group values for (x, y, z)
	 */
	public static aabbTri aabbTri (Triangles tri) {

		double[] vertex1 = tri.getVertex0();
		double[] vertex2 = tri.getVertex1();
		double[] vertex3 = tri.getVertex2();
		aabbTri aabb = new aabbTri(0, 0, 0, 0, 0, 0);

		aabb.xMin = Math.min(Math.min(vertex1[0], vertex2[0]), vertex3[0]);
		aabb.xMax = Math.max(Math.max(vertex1[0], vertex2[0]), vertex3[0]);
		aabb.yMin = Math.min(Math.min(vertex1[1], vertex2[1]), vertex3[1]);
		aabb.yMax = Math.max(Math.max(vertex1[1], vertex2[1]), vertex3[1]);
		aabb.zMin = Math.min(Math.min(vertex1[2], vertex2[2]), vertex3[2]);
		aabb.zMax = Math.max(Math.max(vertex1[2], vertex2[2]), vertex3[2]);

		return aabb;
	}


	//lazy loading;
	public static class aabbTri {
		private double xMin;
		private double xMax;
		private double yMin;
		private double yMax;
		private double zMin;
		private double zMax;

		public aabbTri(
				double xMin, double xMax,
				double yMin, double yMax,
				double zMin, double zMax) {
			this.xMin = xMin;
			this.xMax = xMax;
			this.yMin = yMin;
			this.yMax = yMax;
			this.zMin = zMin;
			this.zMax = zMax;

		}

		//getters and seters
		public double getxMin() {
			return xMin;
		}
		public void setxMin(double xMin) {
			this.xMin = xMin;
		}
		public double getxMax() {
			return xMax;
		}
		public void setxMax(double xMax) {
			this.xMax = xMax;
		}
		public double getyMin() {
			return yMin;
		}
		public void setyMin(double yMin) {
			this.yMin = yMin;
		}
		public double getyMax() {
			return yMax;
		}
		public void setyMax(double yMax) {
			this.yMax = yMax;
		}
		public double getzMin() {
			return zMin;
		}
		public void setzMin(double zMin) {
			this.zMin = zMin;
		}
		public double getzMax() {
			return zMax;
		}
		public void setzMax(double zMax) {
			this.zMax = zMax;
		}




	}

}

