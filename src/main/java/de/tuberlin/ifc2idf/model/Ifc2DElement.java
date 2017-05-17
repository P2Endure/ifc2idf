package de.tuberlin.ifc2idf.model;

import java.util.List;

import de.tuberlin.ifc2idf.geometryUtils.Vertex;

public class Ifc2DElement {
	private String elementName;
	private String elementGUID;
	private List<Vertex> elementPoints;


	public Ifc2DElement (String elementName, String elementGuid, List<Vertex> elementPoints) {
		this.elementName = elementName;
		this.elementGUID = elementGuid;
		this.elementPoints = elementPoints;

	}

	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public String getElementGUID() {
		return elementGUID;
	}

	public void setElementGUID(String elementGUID) {
		this.elementGUID = elementGUID;
	}

	public List<Vertex> getElementPoints() {
		return elementPoints;
	}

	public void setElementPoints(List<Vertex> elementPoints) {
		this.elementPoints = elementPoints;
	}


}
