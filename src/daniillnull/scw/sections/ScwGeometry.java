package daniillnull.scw.sections;

import daniillnull.collada.ColladaIndex;
import daniillnull.collada.controller.ColladaSkin;
import daniillnull.collada.controller.ColladaVertexWeights;
import daniillnull.collada.geometry.ColladaMesh;
import daniillnull.collada.geometry.ColladaPrimitives;
import daniillnull.collada.geometry.ColladaTriangles;
import daniillnull.collada.geometry.ColladaVertices;
import daniillnull.collada.misc.ColladaInput;
import daniillnull.collada.source.ColladaSource;
import daniillnull.collada.source.ColladaSourceAccessor;
import daniillnull.scw.ScwSection;
import daniillnull.scw.sections.subs.ScwBoneWeights;
import daniillnull.scw.sections.subs.ScwJoint;
import daniillnull.scw.sections.subs.ScwTriangles;
import daniillnull.scw.sections.subs.ScwVectors;
import daniillnull.scw.util.ScwInputStream;
import daniillnull.scw.util.ScwOutputStream;
import daniillnull.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScwGeometry extends ScwSection {
	public final List<ScwTriangles> trianglesList;
	public final List<ScwJoint> joints;
	public final List<ScwBoneWeights> boneWeightsList;
	public final List<ScwVectors> vectorsList;
	public float[] bindMatrix;
	public String groupName, name;

	public ScwGeometry() {
		vectorsList = new ArrayList<>();
		trianglesList = new ArrayList<>();
		joints = new ArrayList<>();
		boneWeightsList = new ArrayList<>();
	}

	public ScwGeometry(String name, String groupName) {
		this();
		this.name = name;
		this.groupName = groupName;
	}

	@Override
	public void load(ScwInputStream is) throws IOException {
		trianglesList.clear();
		joints.clear();
		boneWeightsList.clear();
		vectorsList.clear();
		bindMatrix = null;

		name = is.readUTF();
		groupName = is.readUTF(); // IDK what is it

		if (is.getConfig().version <= 1) {
			is.skipBytes(16 * 4); // Unknown matrix for old versions
		}

		int vectorsListSize = is.readByte();
		for (int i = 0; i < vectorsListSize; i++) {
			ScwVectors vectors = new ScwVectors();
			vectors.load(is);

			vectorsList.add(vectors);
		}

		if (is.readBoolean()) {
			bindMatrix = new float[16];
			for (int i = 0; i < 16; i++) {
				bindMatrix[i] = is.readFloat();
			}
		}

		int boneCount = is.readByte();
		for (int i = 0; i < boneCount; i++) {
			ScwJoint scwBone = new ScwJoint();
			scwBone.load(is);

			joints.add(scwBone);
		}

		int boneWeightsCount = is.readInt(); // One Vertex = One BoneWeights
		for (int i = 0; i < boneWeightsCount; i++) {
			ScwBoneWeights boneWeights = new ScwBoneWeights();
			boneWeights.load(is);

			boneWeightsList.add(boneWeights);
		}

		byte triangleSetCount = is.readByte();
		for (int i = 0; i < triangleSetCount; i++) {
			ScwTriangles triangles = new ScwTriangles();
			triangles.load(is);

			trianglesList.add(triangles);
		}
	}

	@Override
	public void save(ScwOutputStream os) throws IOException {
		os.writeUTF(name);
		os.writeUTF(groupName);

		if (os.getConfig().version <= 1) {
			float[] unknownMatrix = {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1}; // Set it to identity matrix
			for (int i = 0; i < 16; i++) {
				os.writeFloat(unknownMatrix[i]);
			}
		}

		os.write(vectorsList.size());
		for (ScwVectors vectors : vectorsList) {
			vectors.save(os);
		}

		if (bindMatrix != null) {
			os.writeBoolean(true);
			for (int i = 0; i < 16; i++) {
				os.writeFloat(bindMatrix[i]);
			}
		} else {
			os.writeBoolean(false);
		}

		os.write(joints.size());
		for (ScwJoint joint : joints) {
			joint.save(os);
		}

		os.writeInt(boneWeightsList.size());
		for (ScwBoneWeights boneWeights : boneWeightsList) {
			boneWeights.save(os);
		}

		os.write(trianglesList.size());
		for (ScwTriangles triangles : trianglesList) {
			triangles.save(os);
		}
	}

	@Override
	public String getSectionName() {
		return "GEOM";
	}

	public ColladaMesh asColladaMesh(String idPrefix) {
		List<ColladaSource> sources = new ArrayList<>(3);

		for (ScwVectors vectors : vectorsList) {
			sources.add(vectors.asColladaSource(idPrefix));
		}

		ColladaVertices vertices = new ColladaVertices(idPrefix + "-vertices");
		vertices.inputs.add(new ColladaInput("POSITION", idPrefix + "-position"));

		List<ColladaPrimitives> colladaTrianglesList = new ArrayList<>();
		for (ScwTriangles triangles : trianglesList) {
			List<ColladaInput> trianglesInputs = new ArrayList<>(3);

			for (ScwVectors vectors : vectorsList) {
				if (vectors.type.equals("POSITION")) { // We should not put POSITION here, replace it to VERTEX
					trianglesInputs.add(new ColladaInput("VERTEX", vertices.id, vectors.indexesOffset));
				} else {
					trianglesInputs.add(new ColladaInput(vectors.type, vectors.generateColladaName(idPrefix), vectors.indexesOffset, vectors.indexesSet));
				}
			}

			ColladaTriangles colladaTriangles = triangles.asCollada(trianglesInputs);
			colladaTrianglesList.add(colladaTriangles);
		}

		return new ColladaMesh(vertices, colladaTrianglesList, sources);
	}

	public void loadColladaMesh(ColladaMesh colladaMesh, ColladaIndex colladaIndex) {
		trianglesList.clear();
		vectorsList.clear();

		if (!colladaMesh.primitivesList.isEmpty()) {
			if (colladaMesh.primitivesList.size() >= 2) {
				System.out.println("[WARNING] ScwGeometry.loadColladaMesh: mesh.primitivesList has more than one elements, their <input> will be ignored!");
			}
			ColladaTriangles colladaTriangles = (ColladaTriangles) colladaMesh.primitivesList.get(0);
			for (ColladaInput colladaInput : colladaTriangles.inputs) {
				if (colladaInput.semantic.equals("VERTEX")) {
					ColladaVertices vertices = colladaIndex.get(colladaInput.source);
					Assert.doAssert(vertices.inputs.size() >= 2, "There are more than one inputs in offset!");
					colladaInput = vertices.inputs.get(0); // Set input to POSITION
				}
				ColladaSource colladaSource = colladaIndex.get(colladaInput.source);

				ScwVectors vectors = new ScwVectors();
				vectors.type = colladaInput.semantic;
				vectors.indexesOffset = (colladaInput.offset != null ? colladaInput.offset : 0);
				vectors.indexesSet = (colladaInput.set != null ? colladaInput.set : 0);
				vectors.dimension = colladaSource.techniqueCommon.accessor.stride;
				vectors.pointCount = colladaSource.techniqueCommon.accessor.count;
				vectors.data = colladaSource.array.getFloats();

				vectorsList.add(vectors);
			}
		}

		for (ColladaPrimitives colladaPrimitives : colladaMesh.primitivesList) {
			ColladaTriangles colladaTriangles = (ColladaTriangles) colladaPrimitives;

			ScwTriangles triangles = new ScwTriangles();
			triangles.loadCollada(colladaTriangles);

			trianglesList.add(triangles);
		}
	}

	public ColladaSkin asColladaSkin(String idPrefix, String geomId) {
		int jointsCount = joints.size();

		if (jointsCount == 0) {
			return null;
		}

		String[] jointNames = new String[jointsCount];
		float[] jointBindMatrices = new float[jointsCount * 16];
		for (int i = 0; i < jointsCount; i++) {
			ScwJoint joint = joints.get(i);
			jointNames[i] = joint.name;
			System.arraycopy(joint.bindMatrix, 0, jointBindMatrices, (i * 16), 16);
		}

		int boneWeightsCount = boneWeightsList.size();
		int[] vCount = new int[boneWeightsCount];
		List<Float> weightsList = new ArrayList<>();
		List<Integer> vList = new ArrayList<>();
		for (int i = 0; i < boneWeightsCount; i++) {
			ScwBoneWeights boneWeights = boneWeightsList.get(i);
			int boneJointsCount = boneWeights.getUsedJointCount();
			vCount[i] = boneJointsCount;
			for (int j = 0; j < boneJointsCount; j++) {
				vList.add(boneWeights.joints[j]);
				int weightIndex = weightsList.size();
				weightsList.add(boneWeights.weights[j]);
				vList.add(weightIndex);
			}
		}
		float[] weights = new float[weightsList.size()];
		for (int i = 0; i < weightsList.size(); i++) {
			weights[i] = weightsList.get(i);
		}
		int[] v = new int[vList.size()];
		for (int i = 0; i < vList.size(); i++) {
			v[i] = vList.get(i);
		}

		ColladaSource jointsSource = new ColladaSource(idPrefix + "-joints", jointNames, 1, ColladaSourceAccessor.JOINT_NAME_PARAMS);
		ColladaSource bindPosesSource = new ColladaSource(idPrefix + "-bind-poses", jointBindMatrices, 16, ColladaSourceAccessor.TRANSFORM_FLOAT4x4_PARAMS);
		ColladaSource weightsSource = new ColladaSource(idPrefix + "-weights", weights, 1, ColladaSourceAccessor.WEIGHT_FLOAT_PARAMS);

		return new ColladaSkin(geomId, bindMatrix, vCount, v, jointsSource, bindPosesSource, weightsSource);
	}

	public void loadColladaSkin(ColladaSkin colladaSkin, Map<String, String> sid2id, ColladaIndex colladaIndex) {
		joints.clear();
		boneWeightsList.clear();

		bindMatrix = colladaSkin.bindShapeMatrix;

		ColladaInput jointInput = null, invBindMatrixInput = null;
		for (ColladaInput colladaInput : colladaSkin.jointsInputs) {
			if (colladaInput.semantic.equals("JOINT")) {
				jointInput = colladaInput;
			} else if (colladaInput.semantic.equals("INV_BIND_MATRIX")) {
				invBindMatrixInput = colladaInput;
			}
		}
		Assert.doAssert((jointInput == null || invBindMatrixInput == null), "JOINT or INV_BIND_MATRIX input not found");

		ColladaSource jointSource = colladaIndex.get(jointInput.source);
		ColladaSource invBindMatrixSource = colladaIndex.get(invBindMatrixInput.source);
		String[] jointNames = jointSource.array.getNames();
		float[] jointBindMatrices = invBindMatrixSource.array.getFloats();
		for (int i = 0; i < jointNames.length; i++) {
			String nodeId = sid2id.getOrDefault(jointNames[i], jointNames[i]);
			ScwJoint joint = new ScwJoint();
			joint.name = nodeId;
			System.arraycopy(jointBindMatrices, (i * 16), joint.bindMatrix, 0, 16);
			joints.add(joint);
		}

		ColladaVertexWeights colladaVertexWeights = colladaSkin.vertexWeights;
		int[] vCount = colladaVertexWeights.vCount;
		int[] v = colladaVertexWeights.v;
		float[] weights = null;
		int currentIndex = 0;

		for (ColladaInput colladaInput : colladaVertexWeights.inputs) {
			if (colladaInput.semantic.equals("JOINT")) {
				Assert.doAssert(colladaInput.offset != 0, "VertexWeights JOINT offset is not 0");
				Assert.doAssert(!colladaInput.source.equals(jointInput.source), "VertexWeights JOINT source is not same");
			} else if (colladaInput.semantic.equals("WEIGHT")) {
				Assert.doAssert(colladaInput.offset != 1, "VertexWeights WEIGHT offset is not 1");
				ColladaSource colladaSource = colladaIndex.get(colladaInput.source);
				weights = colladaSource.array.getFloats();
			} else {
				throw new RuntimeException("Bad input semantic in VertexWeights: " + colladaInput.semantic);
			}
		}

		for (int i = 0; i < vCount.length; i++) {
			ScwBoneWeights boneWeights = new ScwBoneWeights();

			int count = vCount[i];
			Assert.doAssert(count > 4, "Bad joint count for vertex in " + name);
			for (int j = 0; j < count; j++) {
				boneWeights.joints[j] = v[currentIndex];
				boneWeights.weights[j] = weights[v[currentIndex + 1]];
				currentIndex += 2;
			}

			boneWeightsList.add(boneWeights);
		}
	}
}
