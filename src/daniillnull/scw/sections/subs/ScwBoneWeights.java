package daniillnull.scw.sections.subs;

import daniillnull.scw.ScwSerializableEntity;
import daniillnull.scw.util.ScwInputStream;
import daniillnull.scw.util.ScwOutputStream;

import java.io.IOException;

public class ScwBoneWeights implements ScwSerializableEntity {
	public final int[] joints;
	public final float[] weights;

	public ScwBoneWeights() {
		joints = new int[4];
		weights = new float[4];
	}

	@Override
	public void load(ScwInputStream is) throws IOException {
		joints[0] = is.readUnsignedByte();
		joints[1] = is.readUnsignedByte();
		joints[2] = is.readUnsignedByte();
		joints[3] = is.readUnsignedByte();
		weights[0] = is.readPositiveFloat16();
		weights[1] = is.readPositiveFloat16();
		weights[2] = is.readPositiveFloat16();
		weights[3] = is.readPositiveFloat16();
	}

	@Override
	public void save(ScwOutputStream os) throws IOException {
		os.write(joints[0]);
		os.write(joints[1]);
		os.write(joints[2]);
		os.write(joints[3]);
		os.writePositiveFloat16(weights[0]);
		os.writePositiveFloat16(weights[1]);
		os.writePositiveFloat16(weights[2]);
		os.writePositiveFloat16(weights[3]);
	}

	public int getUsedJointCount() {
		int jointsCount = 0;
		for (int i = 0; i < 4; i++) {
			if (weights[i] != 0) {
				jointsCount++;
			}
		}
		return jointsCount;
	}
}
