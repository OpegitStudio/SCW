package daniillnull.scw.sections.instance;

import daniillnull.collada.ColladaIndex;
import daniillnull.collada.instance.ColladaCameraInstance;
import daniillnull.scw.util.ScwInputStream;
import daniillnull.scw.util.ScwOutputStream;
import daniillnull.scw.util.Suffix;

import java.io.IOException;

public class ScwCameraInstance extends ScwInstance<ColladaCameraInstance> {
	public String cameraId, targetNodeId;

	@Override
	public void load(ScwInputStream is) throws IOException {
		cameraId = is.readUTF();
		targetNodeId = is.readUTF();
	}

	@Override
	public void save(ScwOutputStream os) throws IOException {
		os.writeUTF(cameraId);
		os.writeUTF(targetNodeId);
	}

	@Override
	public String getSectionName() {
		return "CAME";
	}

	@Override
	public void loadCollada(ColladaCameraInstance collada, ColladaIndex colladaIndex) {
		cameraId = Suffix.remove(collada.id, Suffix.CAMERA);
		targetNodeId = ""; // TODO: TargetNodeId
	}

	@Override
	public ColladaCameraInstance asCollada() {
		return new ColladaCameraInstance(Suffix.create(cameraId, Suffix.CAMERA));
	}
}
