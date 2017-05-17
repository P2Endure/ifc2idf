package de.tuberlin.ifc2idf.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.bimserver.emf.IfcModelInterface;
import org.bimserver.models.ifc2x3tc1.IfcProduct;
import de.tuberlin.ifc2idf.service.IfcProductsService;
import org.springframework.stereotype.Service;

@Service
public class IfcProductsServiceImpl implements IfcProductsService {


	@Override
	public HashMap<Object, Integer> getAllElements(IfcModelInterface model) {

		List<Object> uniqueProducts = new ArrayList<Object>();
		HashMap<Object, Integer> getTypesandNo = new HashMap<Object, Integer>();

		List<IfcProduct> ifcProducts = model.getAllWithSubTypes(IfcProduct.class);

		if (!ifcProducts.isEmpty()){
			List<IfcProduct> ifcProductTemp = ifcProducts;
			Collections.sort(ifcProductTemp, new Comparator<IfcProduct> () {
				public int compare (IfcProduct ifcProd1, IfcProduct ifcProd2) {
					return ifcProd1.eClass().getName().compareTo(ifcProd2.eClass().getName());
				}
			});

			List<String> ifcCat = new ArrayList<String>();
			for (int i = 0; i <ifcProductTemp.size(); i++){
				IfcProduct ifcProdt = ifcProductTemp.get(i);
				if (ifcProdt.eClass().getName() == "IfcBuilding" ||
					ifcProdt.eClass().getName() == "IfcBuildingStorey" ||
					ifcProdt.eClass().getName().equalsIgnoreCase("IfcBuildingStorey") ||
					ifcProdt.eClass().getName().equalsIgnoreCase("IfcBuilding") ) {
					System.out.println(ifcProdt.eClass().getName() + " was not included in the list of categories");

				} else {
					ifcCat.add(ifcProdt.eClass().getName());
				}
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

			System.out.println("Total number of elements in the model: "+ifcCat.size() + " ---elements per type: " + getTypesandNo);
			System.out.println(uniqueProducts);
			System.out.println(uniqueProducts.size());
		} else {
			System.out.println("Sorry! the ifc files was not read");
		}

		return getTypesandNo;
	}
}
