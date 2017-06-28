package de.tuberlin.ifc2idf.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.bimserver.emf.IdEObject;
import org.bimserver.emf.IfcModelInterface;
import org.bimserver.models.ifc2x3tc1.IfcBuildingStorey;
import org.bimserver.models.ifc2x3tc1.IfcLocalPlacement;
import org.bimserver.models.ifc2x3tc1.IfcObjectDefinition;
import org.bimserver.models.ifc2x3tc1.IfcObjectPlacement;
import org.bimserver.models.ifc2x3tc1.IfcProduct;
import org.bimserver.models.ifc2x3tc1.IfcRelContainedInSpatialStructure;
import org.bimserver.models.ifc2x3tc1.IfcRelDecomposes;
import org.bimserver.models.ifc2x3tc1.IfcSpace;
import org.bimserver.models.ifc2x3tc1.IfcSpatialStructureElement;
import org.eclipse.emf.common.util.EList;
import org.springframework.stereotype.Service;

import com.google.common.collect.BiMap;

import de.tuberlin.ifc2idf.service.IfcProductsService;
import de.tuberlin.ifc2idf.utilsIFC.UtilsIFC;;

@Service
public class IfcProductsServiceImpl implements IfcProductsService {


	@Override
	public HashMap<Object, Integer> getAllElements(IfcModelInterface model) {
		
//		UtilsIFC utils = new UtilsIFC();
		
		List<Object> uniqueProducts = new ArrayList<Object>();
		HashMap<Object, Integer> getTypesandNo = new HashMap<Object, Integer>();

		List<IfcProduct> ifcProducts = model.getAllWithSubTypes(IfcProduct.class);
		List<IfcSpace> spaces = model.getAll(IfcSpace.class);
		
		
		if (!spaces.isEmpty()){
			System.out.println(spaces.size());
			for (IfcSpace ifcSpace : spaces){
				System.out.println("what a surprise: " + ifcSpace.getBoundedBy().size());
				System.out.println("what a surprise: " + ifcSpace.getBoundedBy().get(0).getName());
				List<IfcProduct> spaceElem = getSpaceElements(model, ifcSpace);
//				System.out.println(spaceElem.size());
//				if (spaceElem.size() != 0) {
//						System.out.println(spaceElem.get(0).getName());
//				}
			}
		} else {
			System.out.println("no spaces in this model");
		}


		if (!ifcProducts.isEmpty()){
			List<IfcProduct> ifcProductTemp = ifcProducts;
			Collections.sort(ifcProductTemp, new Comparator<IfcProduct> () {
				public int compare (IfcProduct ifcProd1, IfcProduct ifcProd2) {
					return ifcProd1.eClass().getName().compareTo(ifcProd2.eClass().getName());
				}
			});

			List<String> ifcCat = new ArrayList<String>();
			for (int i = 0; i < ifcProductTemp.size(); i++){
				IfcProduct ifcProdt = ifcProductTemp.get(i);
					ifcCat.add(ifcProdt.eClass().getName());
			}


			//get the unique categories
			uniqueProducts = ifcCat.stream().distinct().collect(Collectors.toList());


			for (int i = 0; i < uniqueProducts.size(); i++) {
				Object uniqueItem =  uniqueProducts.get(i);
				int catCount = 0;
				 	for (int j = 0; j<ifcCat.size(); j++) {
				 		Object normalItem = ifcCat.get(j);
				 		if (normalItem == uniqueItem && normalItem.toString() == uniqueItem.toString() && normalItem.toString().equals(uniqueItem.toString())) {
				 			catCount++;
				 		}
				 		getTypesandNo.put(uniqueItem, catCount);
				 	}
			}

//			System.out.println("Total number of elements in the model: "+ifcCat.size() + " ---elements per type: " + getTypesandNo);
//			System.out.println(uniqueProducts);
//			System.out.println(uniqueProducts.size());
		} else {
			System.out.println("Sorry! the ifc files was not read");
		}

		return getTypesandNo;
	}
	
	private static List<IfcProduct> getSpaceElements (IfcModelInterface model, IfcSpace ifcSpace) {
		
		List<IfcProduct> ifcElements = new ArrayList<IfcProduct>();
		BiMap<Long, ? extends IdEObject> objects = model.getObjects();
		IfcSpatialStructureElement ifcSpatialStructureElement = (IfcSpatialStructureElement) ifcSpace;
		
//		System.out.println("elevation of the Storey"+ ifcStoreyName.getName()+ " is " + ifcStoreyName.getElevation());
		
		
		
		for (IfcRelContainedInSpatialStructure ifcRelContainedInSpatialStructure : ifcSpatialStructureElement.getContainsElements()) { 
			objects.remove(ifcRelContainedInSpatialStructure.getOid());
			for (IfcProduct ifcProduct : ifcRelContainedInSpatialStructure.getRelatedElements()) {
				ifcElements.add(ifcProduct);
			}
		}

		for (IfcRelDecomposes ifcRelDecomposes : ifcSpace.getIsDecomposedBy())	{ 
			for (IfcObjectDefinition ifcObjectDefinition : ifcRelDecomposes.getRelatedObjects()) {
				ifcElements.add((IfcProduct) model.get(ifcObjectDefinition.getOid()));
			}
		}
		
		return ifcElements;
	}
}
