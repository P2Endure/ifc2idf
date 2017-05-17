package de.tuberlin.ifc2idf.model;

import de.tuberlin.ifc2idf.geometryUtils.Triangles;

public class IfcTriangles {

	private Triangles triangle;

	public IfcTriangles (Triangles triangle) {
		this.triangle = triangle;
	}

	public Triangles getTriangle() {
		return triangle;
	}

	public void setTriangle(Triangles triangle) {
		this.triangle = triangle;
	}
}
