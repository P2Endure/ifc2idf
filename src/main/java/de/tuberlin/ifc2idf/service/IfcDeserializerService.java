package de.tuberlin.ifc2idf.service;

import org.bimserver.emf.IfcModelInterface;

public interface IfcDeserializerService {

	IfcModelInterface getIfcDeserialized(String filePath, boolean getGeometry);

}
