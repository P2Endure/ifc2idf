package de.tuberlin.ifc2idf.geometryUtils;

public class Vector {
	public final int X = 0;
    public final int Y = 1;
    public final int Z = 2;


    public final double val[];

    public Vector() {
        val = new double[3];
     }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(X);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(Y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(Z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
}
