package daniillnull.scw.sections.subs;

import daniillnull.collada.source.ColladaSource;
import daniillnull.collada.source.ColladaSourceAccessor;
import daniillnull.scw.ScwSerializableEntity;
import daniillnull.scw.util.ScwInputStream;
import daniillnull.scw.util.ScwOutputStream;
import daniillnull.util.Assert;

import java.io.IOException;
import java.util.List;

public class ScwVectors implements ScwSerializableEntity {
	public String type;
	public int indexesOffset, indexesSet, dimension, pointCount;
	public float[] data;

	@Override
	public void load(ScwInputStream is) throws IOException {
		type = is.readUTF();
		indexesOffset = is.readUnsignedByte();
		indexesSet = is.readUnsignedByte();
		dimension = is.readUnsignedByte();
		float scale = is.readFloat();
		pointCount = is.readInt();

		data = new float[pointCount * dimension];
		for (int i = 0; i < data.length; i++) {
			data[i] = is.readFloat16() * scale;
		}

		// TODO: Remove / remake this. In CR .scw "VERTEX" used instead of "POSITION"
		if (type.equals("VERTEX")) {
			type = "POSITION";
		}
	}

	@Override
	public void save(ScwOutputStream os) throws IOException {
		float scale = calculateScale();

		Assert.doAssert(pointCount * dimension != data.length, "Invalid data.length!");

		os.writeUTF(type);
		os.write(indexesOffset);
		os.write(indexesSet);
		os.write(dimension);
		os.writeFloat(scale);
		os.writeInt(pointCount);

		for (float value : data) {
			os.writeFloat16(value / scale);
		}
	}

	public float calculateScale() {
		float scale = 1.0f;
		for (float value : data) {
			scale = Math.max(Math.abs(value), scale);
		}
		return scale;
	}

	public String generateColladaName(String baseId) {
		if (indexesSet > 0) {
			return baseId + "-" + type.toLowerCase() + "-" + indexesSet;
		}
		return baseId + "-" + type.toLowerCase();
	}

	public ColladaSource asColladaSource(String baseId) {
		String id = generateColladaName(baseId);
		List<ColladaSourceAccessor.Param> params;
		switch (type) {
			case "POSITION":
			case "NORMAL":
				Assert.doAssert(dimension != 3, "Vectors with type POSITION or NORMAL dimension is not 3D!");
				params = ColladaSourceAccessor.XYZ_FLOAT_PARAMS;
				break;

			case "COLOR":
				Assert.doAssert(dimension != 4, "Vectors with type COLOR dimension is not 4D!");
				params = ColladaSourceAccessor.RGBA_FLOAT_PARAMS;
				break;

			case "TEXCOORD":
				Assert.doAssert(dimension != 2, "Vectors with type TEXCOORD dimension is not 2D!");
				params = ColladaSourceAccessor.ST_FLOAT_PARAMS;
				break;

			default:
				throw new RuntimeException("Unknown vectors type: " + type + ", dimension: " + dimension);
		}
		return new ColladaSource(id, data, dimension, params);
	}
}
