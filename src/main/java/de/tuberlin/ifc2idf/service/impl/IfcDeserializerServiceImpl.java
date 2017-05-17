package de.tuberlin.ifc2idf.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;

import org.bimserver.emf.IfcModelInterface;
import org.bimserver.emf.OfflineGeometryGenerator;
import org.bimserver.emf.PackageMetaData;
import org.bimserver.emf.Schema;
import org.bimserver.ifc.BasicIfcModel;
import org.bimserver.ifc.step.deserializer.Ifc2x3tc1StepDeserializer;
import org.bimserver.ifc.step.serializer.Ifc2x3tc1StepSerializer;
import org.bimserver.models.ifc2x3tc1.Ifc2x3tc1Package;
import org.bimserver.plugins.deserializers.DeserializeException;
import org.bimserver.plugins.renderengine.RenderEngine;
import org.bimserver.plugins.renderengine.RenderEngineException;
import de.tuberlin.ifc2idf.service.IfcDeserializerService;
import org.ifcopenshell.IfcOpenShellEngine;
import org.springframework.stereotype.Service;

@Service
public class IfcDeserializerServiceImpl implements IfcDeserializerService{

	@Override
	public IfcModelInterface getIfcDeserialized (String filePath, boolean getGeometry) {

		Ifc2x3tc1StepDeserializer deserializer = new Ifc2x3tc1StepDeserializer(Schema.IFC2X3TC1);
		Ifc2x3tc1StepSerializer serializer = new Ifc2x3tc1StepSerializer(null);

		//reading the ifc file
		PackageMetaData packageMetaData = new PackageMetaData(Ifc2x3tc1Package.eINSTANCE, Schema.IFC2X3TC1, Paths.get("."));
		deserializer.init(packageMetaData);
		IfcModelInterface model = new BasicIfcModel(packageMetaData, null);
		//deserialize
		String tempPath = new File(filePath).getAbsolutePath();
		try {
			URI uri = new File(tempPath).toURI();
			model = deserializer.read(Paths.get(uri).toFile());
		}  catch (DeserializeException e) {
			e.printStackTrace();
		}

		if (getGeometry) {

			try {
				RenderEngine renderEngine = new IfcOpenShellEngine("src/main/resources/data/openshell/IfcGeomServer");
				renderEngine.init();
				OfflineGeometryGenerator offlineGeometryGenerator1 = new OfflineGeometryGenerator(model, serializer, renderEngine);
				offlineGeometryGenerator1.generateForAllElements();
			} catch (RenderEngineException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("Geometry was not generated becase was not requested");
		}


		return model;
	}

}
