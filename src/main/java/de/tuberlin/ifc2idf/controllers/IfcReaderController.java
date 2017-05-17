package de.tuberlin.ifc2idf.controllers;



import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.bimserver.emf.IfcModelInterface;
import de.tuberlin.ifc2idf.service.IfcDeserializerService;
import de.tuberlin.ifc2idf.service.IfcFileTempStoreService;
import de.tuberlin.ifc2idf.service.IfcProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class IfcReaderController {


	private HashMap<Object, Integer> ifcCategories;

    @Autowired
    public void setProductsService(IfcProductsService ifcProductService) {
        this.ifcProductService = ifcProductService;
    }

	private IfcProductsService ifcProductService;

    @Autowired
    public void setIfcUploadService(IfcFileTempStoreService ifcTempStoreService) {
        this.ifcTempStoreService = ifcTempStoreService;
    }

	private IfcFileTempStoreService ifcTempStoreService;

	@Autowired
	public void getIfcDeserialized(IfcDeserializerService ifcDeserializerService) {
	        this.ifcDeserializerService = ifcDeserializerService;
	}

	private IfcDeserializerService  ifcDeserializerService;


	@RequestMapping (
			value = "api/sendfile",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<HashMap<Object, Integer>> postIfcModel (@RequestParam("ifcFile") MultipartFile ifcFile){

		String ifcTempPath = ifcTempStoreService.getFileTempPath(ifcFile);

		IfcModelInterface model = ifcDeserializerService.getIfcDeserialized(ifcTempPath, false);

		HashMap<Object, Integer> ifcProducts = ifcProductService.getAllElements(model);

		if (ifcProducts == null) {
			return new ResponseEntity<HashMap<Object, Integer>>(HttpStatus.NO_CONTENT);
		}

		ifcCategories = ifcProducts;

		ifcTempStoreService.cleanUpTempPath();
		return new ResponseEntity<HashMap<Object, Integer>>(ifcProducts, HttpStatus.OK);

	}

	@RequestMapping (
			value = "api/products",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<ProductsOut>> getIfcModelElements () {

		Collection<ProductsOut> types = new ArrayList<ProductsOut>();

		if (ifcCategories == null) {
			return new ResponseEntity<Collection<ProductsOut>>(HttpStatus.NO_CONTENT);
		}


		for (Object key : ifcCategories.keySet() ) {
			ProductsOut productsOut = new ProductsOut(null, null);
			productsOut.setIfcElementType(key.toString());
			productsOut.setElementsPerType(ifcCategories.get(key));
			types.add(productsOut);
		}

		return new ResponseEntity<Collection<ProductsOut>>(types, HttpStatus.OK);

	}

	private static class ProductsOut {
	    String ifcElementType;
	    int elementsPerType;


	   public ProductsOut (String ifcElementType, Integer elementsPerType) {

	   }

	    @SuppressWarnings("unused")
		public String getIfcElementType() {
	        return ifcElementType;
	    }

	    @SuppressWarnings("unused")
		public int getElementsPerType() {
	        return elementsPerType;
	    }


	    public void setIfcElementType(String ifcElementType) {
	        this.ifcElementType = ifcElementType;
	    }

	    public void setElementsPerType(int elementsPerType) {
	        this.elementsPerType = elementsPerType;
	    }
	}
}
