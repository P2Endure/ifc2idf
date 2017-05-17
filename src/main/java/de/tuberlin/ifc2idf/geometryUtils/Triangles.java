package de.tuberlin.ifc2idf.geometryUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.bimserver.geometry.Matrix;

public class Triangles {
	private double[] vertex0;
	private double[] vertex1;
	private double[] vertex2;

	public Triangles(IntBuffer indices, FloatBuffer vertices, int i, double[] transformation, double[] locationProduct) {

		int index0 = indices.get(i) * 3;
		int index1 = indices.get(i + 1) * 3;
		int index2 = indices.get(i + 2) * 3;

		int index00 = index0+1;
		int index01 = index0+2;

		int index10 = index1+1;
		int index11 = index1+2;

		int index20 = index2+1;
		int index21 = index2+2;

		double[] tempVertex0 = Matrix.multiplyV(transformation, new double[]{vertices.get(index0), vertices.get(index00), vertices.get(index01)});
		double[] tempVertex1 = Matrix.multiplyV(transformation, new double[]{vertices.get(index1), vertices.get(index10), vertices.get(index11)});
		double[] tempVertex2 = Matrix.multiplyV(transformation, new double[]{vertices.get(index2), vertices.get(index20), vertices.get(index21)});
		/*
		 * after the transformation matrix is applied, the elements are basically moved to 0,0,0
		 * the operation bellow helps us to keep the elements on their position
		 */
		this.vertex0 = VectorMath.addition(tempVertex0, locationProduct);
		this.vertex1 = VectorMath.addition(tempVertex1, locationProduct);
		this.vertex2 = VectorMath.addition(tempVertex2, locationProduct);

	}

	public double[] getVertex0 (){
		return this.vertex0;
	}

	public double[] getVertex1 (){
		return this.vertex1;
	}

	public double[] getVertex2 (){
		return this.vertex2;
	}

	private double[] NEWCOMPUTE_INTERVALS(double VV0, double VV1, double VV2, double D0, double D1, double D2, double D0D1, double D0D2) {
		double A;
		double B;
		double C;
		double X0;
		double X1;


        if(D0D1 > 0.0f) {
             /* here we know that D0D2<=0.0 */
            /* that is D0, D1 are on the same side, D2 on the other or on the plane */
                A = VV2;
                B = (VV0 - VV2) * D2;
                C = (VV1 - VV2) * D2;
                X0 = D2 - D0;
                X1 = D2 - D1;

        } else if(D0D2 > 0.0f) {
                /* here we know that d0d1<=0.0 */
            A = VV1;
            B = (VV0 - VV1) * D1;
            C = (VV2 - VV1) * D1;
            X0 = D1 - D0;
            X1 = D1 - D2;

        } else if(D1 * D2 > 0.0f || D0 != 0.0f) {
                /* here we know that d0d1<=0.0 or that D0!=0.0 */
                A = VV0;
                B = (VV1 - VV0) * D0;
                C = (VV2 - VV0) * D0;
                X0 = D0 - D1;
                X1 = D0 - D2;

        } else if(D1 != 0.0f) {
                A = VV1;
                B = (VV0 - VV1) * D1;
                C = (VV2 - VV1) * D1;
                X0 = D1-D0;
                X1=D1-D2;

        } else if(D2 != 0.0f) {
                A = VV2;
                B = (VV0 - VV2) * D2;
                C = (VV1 - VV2) * D2;
                X0 = D2 - D0;
                X1 = D2 - D1;

        } else {
                /* triangles are coplanar */
            //TODO: here is something which is not according to Tomas Moller algorithm
        	///the return should be coplanar
        	return null;
        }
        //why do we need this?
        return new double[]{A, B, C, X0, X1};
	}

	private boolean POINT_IN_TRI(double V0[], double U0[], double U1[], double U2[], int i0, int i1) {
	  double a,b,c,d0,d1,d2;
	  /* is T1 completly inside T2? */
	  /* check if V0 is inside tri(U0,U1,U2) */
	  a = U1[i1] - U0[i1];
	  b = -(U1[i0] - U0[i0]);
	  c = -a * U0[i0] - b * U0[i1];
	  d0 = a * V0[i0] + b * V0[i1] + c;

	  a = U2[i1] - U1[i1];
	  b = -(U2[i0] - U1[i0]);
	  c = -a * U1[i0] - b * U1[i1];
	  d1 = a * V0[i0] + b * V0[i1] + c;

	  a = U0[i1] - U2[i1];
	  b = -(U0[i0] - U2[i0]);
	  c = -a * U2[i0] - b * U2[i1];
	  d2 = a * V0[i0] + b * V0[i1] + c;
	  if(d0 * d1 > 0.0f) {
	    if(d0 * d2 > 0.0f) return true;
	  }
	  return false;
	}

	private int coplanar_tri_tri(double N[], double V0[], double V1[], double V2[], double U0[], double U1[], double U2[]) {
		double A[] = new double[3];
		int i0,i1;
		/* first project onto an axis-aligned plane, that maximizes the area */
		/* of the triangles, compute indices: i0,i1. */
		A[0]=Math.abs(N[0]);
		A[1]=Math.abs(N[1]);
		A[2]=Math.abs(N[2]);

		if(A[0] > A[1]) {
			if(A[0] > A[2])	{
				i0 = 1;      /* A[0] is greatest */
				i1 = 2;
			} else {
				i0 = 0;      /* A[2] is greatest */
				i1 = 1;
			}
		} else {   /* A[0]<=A[1] */
			if(A[2] > A[1]) {
				i0 = 0;      /* A[2] is greatest */
				i1 = 1;
			} else {
				i0 = 0;      /* A[1] is greatest */
				i1 = 2;
			}
		}

		/* test all edges of triangle 1 against the edges of triangle 2 */

		if (EDGE_AGAINST_TRI_EDGES(V0,V1,U0,U1,U2, i0, i1)) {
			return 1;
		}
		if (EDGE_AGAINST_TRI_EDGES(V1,V2,U0,U1,U2, i0, i1)) {
			return 1;
		}
		if (EDGE_AGAINST_TRI_EDGES(V2,V0,U0,U1,U2, i0, i1)) {
			return 1;
		}

		/* finally, test if tri1 is totally contained in tri2 or vice versa */
		if (POINT_IN_TRI(V0,U0,U1,U2, i0, i1)) {
			return 1;
		}
		if (POINT_IN_TRI(U0,V0,V1,V2, i0, i1)) {
			return 1;
		}

		return 0;
	}

	private boolean EDGE_EDGE_TEST(double V0[], double U0[], double U1[], int i0, int i1, double Ax, double Ay) {

	 double Bx = U0[i0] - U1[i0];
	 double By = U0[i1] - U1[i1];
	 double Cx = V0[i0] - U0[i0];
	 double Cy = V0[i1] - U0[i1];
	 double f = Ay * Bx - Ax * By;
	 double d = By * Cx - Bx * Cy;

	  if((f > 0 && d >= 0 && d <= f) || (f < 0 && d <= 0 && d >= f)) {
	   double e = Ax * Cy - Ay * Cx;

	   if(f > 0) {
	      if(e >= 0 && e <= f) return true;
	    } else {
	      if(e <= 0 && e >= f) return true;
	    }
	  }
	  return false;
	}

	 private boolean EDGE_AGAINST_TRI_EDGES(double[] V0, double[] V1, double[] U0, double[] U1, double[] U2, int i0, int i1) {

	    double Ax = V1[i0] - V0[i0];
	    double Ay = V1[i1] - V0[i1];
	    /* test edge U0,U1 against V0,V1 */
	    if (EDGE_EDGE_TEST(V0,U0,U1,i0, i1, Ax, Ay)) {
	    	return true;
	    }
	    /* test edge U1,U2 against V0,V1 */
	    if (EDGE_EDGE_TEST(V0,U1,U2, i0, i1, Ax, Ay)) {
	    	return true;
	    }
	    /* test edge U2,U1 against V0,V1 */
	    if (EDGE_EDGE_TEST(V0,U2,U0, i0, i1, Ax, Ay)) {
	    	return true;
	    }
	    return false;
	  }


	public boolean intersects(Triangles triangle2, double epsilon, double tolerance) {
		double[] N1 = new double[3];
		double[] N2 = new double[3];
		double d1, d2, du0, du1, du2, dv0, dv1, dv2;
		double[] D = new double[3];
		double [] isect1 = new double[2];
		double [] isect2 = new double[2];
		short index;
		double vp0, vp1, vp2;
		double up0, up1, up2;
		double bb, cc, max;


		/* compute plane equation of triangle(V0,V1,V2) */
		N1 = TrianglesMath.getTriPlNormal(vertex0, vertex1, vertex2);
		d1= -VectorMath.dot(N1, vertex0);
		/* plane equation 1: N1.X+d1=0 */


		/* put U0,U1,U2 into plane equation 1 to compute signed distances to the plane*/
		du0 = VectorMath.dot(N1, triangle2.vertex0) + d1;
		du1 = VectorMath.dot(N1, triangle2.vertex1) + d1;
		du2 = VectorMath.dot(N1, triangle2.vertex2) + d1;


		//this part eliminate those clashes which for which the signed distance is smaller than the tolerance set.

		if(Math.abs(du0) < tolerance) {
			du0 = 0.0;
		}
		if(Math.abs(du1) < tolerance) {
			du1 = 0.0;
		}
		if(Math.abs(du2) < tolerance) {
			du2 = 0.0;
		}


		//TODO: get the distance which gives clashes.
		//TODO: to find a way to identify it.
		//use the else
		//check if all the points lie on the same side of the plane
		if(du0 * du1 > 0.0f && du0 * du2 > 0.0f) { 	 /* same sign on all of them + not equal 0 ? */
			return false;                  		  		     /* no intersection occurs */
		}


		/* compute plane of triangle (U0,U1,U2) */
		N2 = TrianglesMath.getTriPlNormal(triangle2.vertex0, triangle2.vertex1, triangle2.vertex2);
		d2 = -VectorMath.dot(N2, triangle2.vertex0);
		/* plane equation 2: N2.X+d2=0 */

		/* put V0,V1,V2 into plane equation 2 */
		dv0 = VectorMath.dot(N2, vertex0) + d2;
		dv1 = VectorMath.dot(N2, vertex1) + d2;
		dv2 = VectorMath.dot(N2, vertex2) + d2;
		//this part eliminate those clashes which for which the signed distance is smaller than the tolerance set.

		if(Math.abs(dv0) < tolerance) {
			dv0 = 0.0;
		}
		if(Math.abs(dv1) < tolerance) {
			dv1 = 0.0;
		}
		if(Math.abs(dv2) < tolerance) {
			dv2 = 0.0;
		}

		//TODO: get the distance which gives clashes.
		//TODO: to find a way to identify it.
		//use the else
		//check if all the points lie on the same side of the plane
		if(dv0 * dv1 > 0.0f && dv0 * dv2 > 0.0f) { /* same sign on all of them + not equal 0 ? */
			return false;                   	   /* no intersection occurs */
		}

		//starting to compute the overlapping of the intersection lines
		/* compute direction of intersection line */
		D = VectorMath.crossProduct(N1, N2);

		/* compute and index to the largest component of D */
		max = Math.abs(D[0]);
		index = 0;
		bb = Math.abs(D[1]);
		cc = Math.abs(D[2]);
		if(bb > max) {
			max = bb;
			index = 1;
		}
		if(cc > max) {
			max = cc;
			index = 2;
		};

		/* this is the simplified projection onto L*/
		vp0 = vertex0[index];
		vp1 = vertex1[index];
		vp2 = vertex2[index];

		up0 = triangle2.vertex0[index];
		up1 = triangle2.vertex1[index];
		up2 = triangle2.vertex2[index];

		/* compute interval for triangle 1 */
		double[] nci1 = NEWCOMPUTE_INTERVALS(vp0, vp1, vp2, dv0, dv1, dv2, dv0 * dv1, dv0 * dv2);
		if (nci1 == null) {
           return coplanar_tri_tri(N1, vertex0, vertex1, vertex2, triangle2.vertex0, triangle2.vertex1, triangle2.vertex2) == 1;
		}
		double a = nci1[0];
		double b = nci1[1];
		double c = nci1[2];
		double x0 = nci1[3];
		double x1 = nci1[4];

		/* compute interval for triangle 2 */
		double[] nci2 = NEWCOMPUTE_INTERVALS(up0, up1, up2, du0, du1, du2, du0 * du1, du0 * du2);
		if (nci2 == null) {
            return coplanar_tri_tri(N1, vertex0, vertex1, vertex2, triangle2.vertex0, triangle2.vertex1, triangle2.vertex2) == 1;
		}
		double d = nci2[0];
		double e = nci2[1];
		double f = nci2[2];
		double y0 = nci2[3];
		double y1 = nci2[4];

		double xx,yy,xxyy,tmp;
		xx = x0 * x1;
		yy = y0 * y1;
		xxyy = xx * yy;

		tmp = a * xxyy;
		isect1[0] = tmp + b * x1 * yy;
		isect1[1] = tmp + c * x0 * yy;

		tmp = d * xxyy;
		isect2[0] = tmp + e * xx * y1;
		isect2[1] = tmp + f * xx * y0;

		if (isect1[0] > isect1[1]) {
			double x = isect1[0];
			isect1[0] = isect1[1];
			isect1[1] = x;
		}
		if (isect2[0] > isect2[1]) {
			double x = isect2[0];
			isect2[0] = isect2[1];
			isect2[1] = x;
		}

		if(isect1[1] < isect2[0] || isect2[1] < isect1[0]) {
			return false;
		}
		return true;
	}

}
