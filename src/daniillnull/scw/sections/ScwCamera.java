package daniillnull.scw.sections;

import daniillnull.collada.camera.ColladaOptics;
import daniillnull.collada.camera.ColladaPerspective;
import daniillnull.scw.ScwSection;
import daniillnull.scw.util.ScwInputStream;
import daniillnull.scw.util.ScwOutputStream;

import java.io.IOException;

public class ScwCamera extends ScwSection {
	public String name;
	public float v1, v2, v3, zNear, zFar;

	public ScwCamera() {
	}

	public ScwCamera(String name) {
		this.name = name;
	}

	@Override
	public void load(ScwInputStream is) throws IOException {
		name = is.readUTF();
		v1 = is.readFloat();
		v2 = is.readFloat();
		v3 = is.readFloat();
		zNear = is.readFloat();
		zFar = is.readFloat();
	}

	@Override
	public void save(ScwOutputStream os) throws IOException {
		os.writeUTF(name);
		os.writeFloat(v1);
		os.writeFloat(v2);
		os.writeFloat(v3);
		os.writeFloat(zNear);
		os.writeFloat(zFar);
	}

	@Override
	public String getSectionName() {
		return "CAME";
	}

	@Override
	public String toString() {
		return "ScwCamera{" +
				"name='" + name + '\'' +
				", v1=" + v1 +
				", v2=" + v2 +
				", v3=" + v3 +
				", zNear=" + zNear +
				", zFar=" + zFar +
				'}';
	}

	public ColladaOptics asColladaOptics() {
		ColladaPerspective colladaPerspective = new ColladaPerspective();
		colladaPerspective.xFov = v2;
		colladaPerspective.aspectRatio = v3;
		colladaPerspective.zNear = zNear;
		colladaPerspective.zFar = zFar;
		return new ColladaOptics(colladaPerspective);
	}

	public void loadColladaOptics(ColladaOptics colladaOptics) {
		ColladaPerspective colladaPerspective = colladaOptics.techniqueCommon.perspective;
		v2 = colladaPerspective.xFov;
		v3 = colladaPerspective.aspectRatio;
		zNear = colladaPerspective.zNear;
		zFar = colladaPerspective.zFar;
	}
}
