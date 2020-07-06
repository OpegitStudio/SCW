package daniillnull.scw.sections.subs;

import daniillnull.scw.ScwSerializableEntity;
import daniillnull.scw.util.ScwInputStream;
import daniillnull.scw.util.ScwOutputStream;

import java.io.IOException;

public class ScwJoint implements ScwSerializableEntity {
	public final float[] bindMatrix;
	public String name;

	public ScwJoint() {
		bindMatrix = new float[16];
	}

	public void load(ScwInputStream is) throws IOException {
		name = is.readUTF();
		for (int i = 0; i < 16; i++) {
			bindMatrix[i] = is.readFloat();
		}
	}

	@Override
	public void save(ScwOutputStream os) throws IOException {
		os.writeUTF(name);
		for (int i = 0; i < 16; i++) {
			os.writeFloat(bindMatrix[i]);
		}
	}
}
