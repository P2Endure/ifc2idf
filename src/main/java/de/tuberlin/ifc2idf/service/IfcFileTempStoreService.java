package de.tuberlin.ifc2idf.service;

import org.springframework.web.multipart.MultipartFile;


public interface IfcFileTempStoreService {

	String getFileTempPath (MultipartFile ifcFile);

	void cleanUpTempPath ();

}
