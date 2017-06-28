package de.tuberlin.ifc2idf.controllers;

import java.util.ArrayList;
import java.util.List;

import org.bimserver.emf.IfcModelInterface;
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

import de.tuberlin.ifc2idf.geometryUtils.Triangles;
import de.tuberlin.ifc2idf.service.IfcDeserializerService;
import de.tuberlin.ifc2idf.service.IfcFileTempStoreService;
import de.tuberlin.ifc2idf.service.IfcElementTriangles;

@RestController
public class IfcExportCtrl {

	private IfcModelInterface ifcModel;

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

    @Autowired
    public void getTriangles(IfcElementTriangles ifcElementsTriangles) {
        this.ifcElementsTriangles = ifcElementsTriangles;
    }

    private IfcElementTriangles ifcElementsTriangles;

	@RequestMapping (
			value = "api/sendifc",
			method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<IfcModelInterface> postIfcModel (@RequestParam("ifcFile") MultipartFile ifcFile){

		String ifcTempPath = ifcTempStoreService.getFileTempPath(ifcFile);

		IfcModelInterface model = ifcDeserializerService.getIfcDeserialized(ifcTempPath, true);

		if (model == null) {
			return new ResponseEntity<IfcModelInterface>(HttpStatus.NO_CONTENT);
		}

		ifcModel = model;

		ifcTempStoreService.cleanUpTempPath();
		return new ResponseEntity<IfcModelInterface>(HttpStatus.OK);

	}

	//this should be the controller to get the geometry of the files
	@RequestMapping (
			value = "api/ifcElements",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<List<Triangles>>> getIfcModelElements () {

		List<List<Triangles>> types = new ArrayList<List<Triangles>>();

		if (ifcModel == null) {
			return new ResponseEntity<List<List<Triangles>>>(HttpStatus.NO_CONTENT);
		} else {
            types = ifcElementsTriangles.getIfcElements(ifcModel);
		}

            return new ResponseEntity<List<List<Triangles>>>(types, HttpStatus.OK);

        }

	//this should be the controller to export the idf file
	//not sure if the output will be JSON - or maybe will be
	@RequestMapping (
			value = "api/idfFile",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> getIdfFile() {

		List<String> types = new ArrayList<String>();

		if (ifcModel == null) {
			return new ResponseEntity<List<String>>(HttpStatus.NO_CONTENT);
		} else {
			//do something here...call the service to get the triangles
		}

		return new ResponseEntity<List<String>>(types, HttpStatus.OK);

	}

}
