package de.tuberlin.ifc2idf.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;

import org.bimserver.emf.IfcModelInterface;
import de.tuberlin.ifc2idf.AbstractUnitTest;
import de.tuberlin.ifc2idf.geometryUtils.Triangles;
import de.tuberlin.ifc2idf.service.impl.IfcDeserializerServiceImpl;
import de.tuberlin.ifc2idf.service.impl.IfcElementTrianglesImpl;
import de.tuberlin.ifc2idf.service.impl.IfcProductsServiceImpl;
import de.tuberlin.ifc2idf.utilsIFC.UtilsIFC;
import org.junit.Before;
import org.junit.Test;


public class IfcUnitTests extends AbstractUnitTest {


	private IfcDeserializerServiceImpl ifcDeserializerService;
	private IfcProductsServiceImpl ifcProductsService;
	private IfcElementTrianglesImpl ifcElementsTriangles;
	private UtilsIFC ifcUtils;

	private String ifcFilePath1 = new String();
	private String ifcFilePath2 = new String();
	private String ifcFileName1 = new String();
	private String ifcFileName2 = new String();
	private IfcModelInterface model1;
	private IfcModelInterface model2;

	@Before
	public void setupServices () {
		ifcDeserializerService = new IfcDeserializerServiceImpl();
		ifcProductsService = new IfcProductsServiceImpl();
		ifcElementsTriangles = new IfcElementTrianglesImpl();
		ifcUtils = new UtilsIFC();

	}


	@Test
	public void testServicesCreation () {
		assertNotNull(ifcDeserializerService);
		assertNotNull(ifcProductsService);
		assertNotNull(ifcElementsTriangles);
		assertNotNull(ifcUtils);
	}

	@Test
	public void testProductsService () throws Exception{


		ifcFilePath1 = "src/test/data/ifc/Gemaal_Weteringschans_Bouwkundig.ifc";

	    HashMap<Object, Integer> testList = new HashMap<Object, Integer>();

	    model1 = ifcDeserializerService.getIfcDeserialized(ifcFilePath1, false);

	    testList = ifcProductsService.getAllElements(model1);

	    HashMap<Object, Integer> testList2 = new HashMap<Object, Integer>();

	    testList2.put("IfcFlowSegment", 6);
	    testList2.put("IfcWall", 9);
	    testList2.put("IfcSite", 1);
	    testList2.put("IfcWindow", 36);
	    testList2.put("IfcCurtainWall", 23);
	    testList2.put("IfcRoof", 2);
	    testList2.put("IfcWallStandardCase", 155);
	    testList2.put("IfcBuildingElementProxy", 3);
	    testList2.put("IfcOpeningElement", 47);
	    testList2.put("IfcMember", 65);
	    testList2.put("IfcDoor", 12);
	    testList2.put("IfcSlab", 43);

		assertNotNull(ifcProductsService);
		assertNotNull(testList);
		assertEquals(12, testList.size());
		assertEquals(testList2, testList);
	}

}
