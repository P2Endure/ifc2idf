package de.tuberlin.ifc2idf.utilsIFC;

import org.bimserver.models.ifc2x3tc1.IfcAxis2Placement;
import org.bimserver.models.ifc2x3tc1.IfcAxis2Placement3D;
import org.bimserver.models.ifc2x3tc1.IfcCartesianPoint;
import org.bimserver.models.ifc2x3tc1.IfcDirection;
import org.bimserver.models.ifc2x3tc1.IfcLocalPlacement;
import org.bimserver.models.ifc2x3tc1.IfcObjectPlacement;
import org.bimserver.models.ifc2x3tc1.IfcProduct;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;

public class UtilsIFC {


	public double[] getElementLocation (IfcProduct ifcProduct) {
		EList<Double> locationVector = new BasicEList<Double>();
		EList<Double> locationRefVector = new BasicEList<Double>();
		locationRefVector.add(0.0);
		locationRefVector.add(0.0);
		locationRefVector.add(0.0);

		double[] locVector = new double[3];

		locationVector = getRelativeLocation(ifcProduct);

		if (ifcProduct.eClass().getName() == "IfcWindow" ||
			ifcProduct.eClass().getName() == "IfcDoor" ||
			ifcProduct.eClass().getName().equalsIgnoreCase("IfcWindow") ||
			ifcProduct.eClass().getName().equalsIgnoreCase("IfcDoor")) {

			//get the opening related to a window or a wall
			IfcProduct ifcOpening = getRelatedElement(ifcProduct);
			EList<Double> locationOpening = getRelativeLocation(ifcOpening);

			//get the wall on which the opening is placed
			IfcProduct ifcWall = getRelatedElement(ifcOpening);
			EList<Double> locationWall = getRelativeLocation(ifcWall);

			//the direction is not working properly
//			EList<Double> wallDirection = getElementDirection(ifcWall);

			if (locationWall.get(0) < locationOpening.get(0)) {
				locationRefVector.set(0, -(locationWall.get(1) + locationOpening.get(1)));
				locationRefVector.set(1, locationWall.get(0) + locationOpening.get(0));
				locationRefVector.set(2, locationWall.get(2) + locationOpening.get(2));
			} else {
				locationRefVector.set(0, locationWall.get(0) + locationOpening.get(1));
				locationRefVector.set(1, locationWall.get(1) - locationOpening.get(0));
				locationRefVector.set(2, locationWall.get(2) + locationOpening.get(2));
			}

		}


		locVector[0] = locationVector.get(0) + locationRefVector.get(0);
		locVector[1] = locationVector.get(1) + locationRefVector.get(1);
		locVector[2] = locationVector.get(2) + locationRefVector.get(2);


		return locVector;
	}

	//get the location of an element
	public EList<Double> getRelativeLocation (IfcProduct ifcProduct) {
		IfcObjectPlacement productLocation = ifcProduct.getObjectPlacement();
		EList<Double> locationVector = new BasicEList<Double>();

		locationVector.add(0.0);
		locationVector.add(0.0);
		locationVector.add(0.0);

		if (productLocation != null && productLocation instanceof IfcLocalPlacement) {
			IfcLocalPlacement productPlacement = (IfcLocalPlacement)productLocation;
			IfcAxis2Placement relativePlacement = productPlacement.getRelativePlacement();

			if (relativePlacement != null ) {
				if (relativePlacement instanceof IfcAxis2Placement3D) {
					IfcAxis2Placement3D axis2Placement3D = (IfcAxis2Placement3D)relativePlacement;

					IfcCartesianPoint location = axis2Placement3D.getLocation();
					locationVector = location.getCoordinates();
				}
			}
		}

		return locationVector;
	}

	//get the direction of an element
	public EList<Double> getElementDirection (IfcProduct ifcProduct) {
		IfcObjectPlacement productLocation = ifcProduct.getObjectPlacement();
		EList<Double> directionVector = new BasicEList<Double>();

		if (productLocation != null && productLocation instanceof IfcLocalPlacement) {
			IfcLocalPlacement productPlacement = (IfcLocalPlacement)productLocation;
			IfcAxis2Placement relativePlacement = productPlacement.getRelativePlacement();

			if (relativePlacement != null ) {
				if (relativePlacement instanceof IfcAxis2Placement3D) {
					IfcAxis2Placement3D axis2Placement3D = (IfcAxis2Placement3D)relativePlacement;

					IfcDirection ifcDirection = axis2Placement3D.getAxis();

					if (ifcDirection != null) {
						directionVector = ifcDirection.getDirectionRatios();
					}
				}
			}
		}
		return directionVector;
	}

	//get the reference elements
	public IfcProduct getRelatedElement (IfcProduct ifcProduct) {
		IfcProduct ifcRefProduct = null;
		IfcObjectPlacement productLocation = ifcProduct.getObjectPlacement();

		if (productLocation != null && productLocation instanceof IfcLocalPlacement) {
			IfcLocalPlacement productPlacement = (IfcLocalPlacement)productLocation;
			IfcObjectPlacement relativePlacementTo = productPlacement.getPlacementRelTo();

			if (relativePlacementTo != null) {
				if (relativePlacementTo.getPlacesObject().size() != 0) {
					ifcRefProduct = relativePlacementTo.getPlacesObject().get(0);
				}
			}
		}
		return ifcRefProduct;
	}

}
