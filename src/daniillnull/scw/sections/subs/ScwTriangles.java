package daniillnull.scw.sections.subs;

import daniillnull.collada.geometry.ColladaTriangles;
import daniillnull.collada.misc.ColladaInput;
import daniillnull.scw.ScwSerializableEntity;
import daniillnull.scw.util.ScwInputStream;
import daniillnull.scw.util.ScwOutputStream;
import daniillnull.util.Assert;

import java.io.IOException;
import java.util.List;

public class ScwTriangles implements ScwSerializableEntity {
	private static final int VERTEX_COUNT = 3;

	public String materialName;
	public int subsectionCount;
	public int triangleCount;
	public int[] indexes;

	public void load(ScwInputStream is) throws IOException {
		materialName = is.readUTF();
		triangleCount = is.readInt();
		subsectionCount = is.readByte();
		int indexSize = is.readByte();

		int indexesCount = (triangleCount * subsectionCount * VERTEX_COUNT);
		indexes = new int[indexesCount];
		for (int i = 0; i < indexesCount; i++) {
			indexes[i] = is.readCustomType(indexSize);
		}
	}

	@Override
	public void save(ScwOutputStream os) throws IOException {
		int indexSize = calculateIndexSize();

		os.writeUTF(materialName);
		os.writeInt(triangleCount);
		os.write(subsectionCount);
		os.write(indexSize);

		int indexesCount = (triangleCount * subsectionCount * VERTEX_COUNT);
		Assert.doAssert(indexesCount != indexes.length, "indexesCount != indexes.length");
		for (int i = 0; i < indexesCount; i++) {
			os.writeCustomType(indexes[i], indexSize);
		}
	}

	public int calculateIndexSize() {
		int max = 0;
		for (int index : indexes) {
			max = Math.max(max, index);
		}

		if (max > 0xffff) {
			return 4;
		} else if (max > 0xff) {
			return 2;
		} else {
			return 1;
		}
	}

	public ColladaTriangles asCollada(List<ColladaInput> inputs) {
		return new ColladaTriangles(indexes, triangleCount, materialName, inputs);
	}

	public void loadCollada(ColladaTriangles colladaTriangles) {
		indexes = colladaTriangles.indices;
		triangleCount = colladaTriangles.count;
		materialName = colladaTriangles.material;
		// TODO: change that. I don't know how to do that correctly.
		subsectionCount = colladaTriangles.inputs.get(colladaTriangles.inputs.size() - 1).offset + 1;
	}
}
