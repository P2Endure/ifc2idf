package de.tuberlin.ifc2idf.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
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
	private IfcModelInterface model1;

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


		ifcFilePath1 = "src/test/data/small_ifc/wall_Arch.ifc";

	    List<List<Triangles>> testList = new ArrayList<List<Triangles>>();

	    model1 = ifcDeserializerService.getIfcDeserialized(ifcFilePath1, false);
	    assertNotNull(model1);
	    testList = ifcElementsTriangles.getIfcElements(model1);
	    assertNotNull(testList);
	    System.out.println(testList);
		
		assertEquals(12, testList.size());
	}

}
