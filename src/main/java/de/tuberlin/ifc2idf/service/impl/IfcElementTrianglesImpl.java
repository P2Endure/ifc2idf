package de.tuberlin.ifc2idf.service.impl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.bimserver.emf.IfcModelInterface;
import org.bimserver.models.geometry.GeometryData;
import org.bimserver.models.geometry.GeometryInfo;
import org.bimserver.models.ifc2x3tc1.IfcProduct;

import de.tuberlin.ifc2idf.utilsIFC.UtilsIFC;
import de.tuberlin.ifc2idf.geometryUtils.Triangles;
import de.tuberlin.ifc2idf.service.IfcElementTriangles;

import org.springframework.stereotype.Service;


@Service
public class IfcElementTrianglesImpl implements IfcElementTriangles {
	
	UtilsIFC utils = new UtilsIFC();
	
	@Override
	public List<List<Triangles>> getIfcElements (IfcModelInterface ifcModel) {
		List<List<Triangles>> ifcElementsGeometry = new ArrayList<List<Triangles>>();		
		
		if (ifcModel != null) {
			List<IfcProduct> ifcProducts = ifcModel.getAllWithSubTypes(IfcProduct.class);
			if (!ifcProducts.isEmpty()) {
				for (int i = 0; i < ifcProducts.size(); i++){
					IfcProduct ifcProduct = ifcProducts.get(i);
					
					System.out.println("connected to this: " + ifcProduct.getName());
					System.out.println("is this: " + ifcProduct.getObjectPlacement().getPlacesObject().get(0).getObjectType());
					double[] locationProduct = utils.getElementLocation(ifcProduct);
					GeometryInfo geometryInfo = ifcProduct.getGeometry();
					if (geometryInfo != null) {
						List<Triangles> elementTriangles = getAllTriangles(geometryInfo, locationProduct);
						ifcElementsGeometry.add(elementTriangles);
					}
				}
			}
		}
		return ifcElementsGeometry;
	}
	
	private List<Triangles> getAllTriangles (GeometryInfo geometryInfo, double[] locationElement) {

		List<Triangles> elementTriangles = new ArrayList<Triangles>();

		//get the geometry data attached to elements
		GeometryData dataElement = geometryInfo.getData();

		//get the indices
		IntBuffer indicesElement = getIntBuffer(dataElement.getIndices());

		//get the vertices
		FloatBuffer verticesElement = getFloatBuffer(dataElement.getVertices());


		//get the transformation matrix
		DoubleBuffer transformation = getDoubleBuffer(geometryInfo.getTransformation());
		double[] transformationArray = new double[16];
		for (int i=0; i<16; i++) {
			transformationArray[i] = transformation.get(i);
		}

		int indexTest = 0;
		for (int i=0; i<indicesElement.capacity(); i+=3) {
			//check in advance if the indices does not exceed the vertices capacity
			indexTest = indicesElement.get(i)*3;
			if (indexTest+2 < verticesElement.capacity()) {
				indexTest = indicesElement.get(i+1)*3;
				if (indexTest + 2 < verticesElement.capacity()) {
					indexTest = indicesElement.get(i+2)*3;
					if (indexTest + 2 < verticesElement.capacity()) {
						Triangles triangle = new Triangles(indicesElement, verticesElement, i, transformationArray, locationElement);
						elementTriangles.add(triangle);
					}
				}
			}
		}
		return elementTriangles;
	}

	private FloatBuffer getFloatBuffer(byte[] input) {
		ByteBuffer vertexBuffer = ByteBuffer.wrap(input);
		vertexBuffer.order(ByteOrder.LITTLE_ENDIAN);
		FloatBuffer verticesFloatBuffer = vertexBuffer.asFloatBuffer();
		verticesFloatBuffer.position(0);
		return verticesFloatBuffer;
	}

	private DoubleBuffer getDoubleBuffer(byte[] input) {
		ByteBuffer vertexBuffer = ByteBuffer.wrap(input);
		vertexBuffer.order(ByteOrder.LITTLE_ENDIAN);
		DoubleBuffer doubleBuffer = vertexBuffer.asDoubleBuffer();
		doubleBuffer.position(0);
		return doubleBuffer;
	}

	private IntBuffer getIntBuffer(byte[] input) {
		ByteBuffer indicesBuffer = ByteBuffer.wrap(input);
		indicesBuffer.order(ByteOrder.LITTLE_ENDIAN);
		IntBuffer indicesIntBuffer = indicesBuffer.asIntBuffer();
		return indicesIntBuffer;
	}



}
