package de.tuberlin.ifc2idf.service;

import java.util.List;

import org.bimserver.models.geometry.GeometryInfo;
import de.tuberlin.ifc2idf.geometryUtils.Triangles;

public interface IfcElementTriangles {

	public List<Triangles> getAllTriangles (GeometryInfo geometryInfo, double[] locationElement);

}
