package de.tuberlin.ifc2idf.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import de.tuberlin.ifc2idf.service.IfcFileTempStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class IfcFileTempStoreServiceImpl implements IfcFileTempStoreService {

	@Autowired
	private Environment env;

	@Override
	public String getFileTempPath (MultipartFile ifcFile) {

	      // Get the filename and build the local file path
	      String fileName = ifcFile.getOriginalFilename();
	      String directory = env.getProperty("de.tuberlin.ifc2idf.paths.uploadedFiles");
	      String fileTempPath = Paths.get(directory, fileName).toString();

	      System.out.println(directory);

	      try {
	      // Save the file locally
	      BufferedOutputStream stream =
	          new BufferedOutputStream(new FileOutputStream(new File(fileTempPath)));
	      stream.write(ifcFile.getBytes());
	      stream.close();
	    }
	    catch (Exception e) {
	      System.out.println(e.getMessage());
	      return null;
	    }

	    System.out.println(fileTempPath);


		return fileTempPath;

	} //end getFileTempPath

	@Override
	public void cleanUpTempPath () {

	    String directory = env.getProperty("de.tuberlin.ifc2idf.paths.uploadedFiles").toString();

	    try {
		    File folder = new File (directory);
			File keep = new File (directory, ".keep");

			for (File file : folder.listFiles()) {
					file.delete();
					System.out.println(file.getName() + " is deleted");
			}//end for

			keep.createNewFile();

	    } catch (IOException e) {
		      e.printStackTrace();
		}

		System.out.println("directory is cleaned");
	} //end cleanUpTempPath

}
