package de.tuberlin.ifc2idf.service;

import java.util.List;

import org.bimserver.emf.IfcModelInterface;
import de.tuberlin.ifc2idf.geometryUtils.Triangles;

public interface IfcElementTriangles {

	public List<List<Triangles>> getIfcElements (IfcModelInterface ifcModel);

}
