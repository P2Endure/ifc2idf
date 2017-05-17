package de.tuberlin.ifc2idf.service;

import java.util.HashMap;

import org.bimserver.emf.IfcModelInterface;

public interface IfcProductsService {

	HashMap<Object, Integer> getAllElements(IfcModelInterface model);

}
