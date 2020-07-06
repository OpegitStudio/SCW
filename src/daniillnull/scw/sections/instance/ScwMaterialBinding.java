package daniillnull.scw.sections.instance;

import daniillnull.collada.material.ColladaInstanceMaterial;
import daniillnull.scw.ScwSerializableEntity;
import daniillnull.scw.util.ScwInputStream;
import daniillnull.scw.util.ScwOutputStream;

import java.io.IOException;

public class ScwMaterialBinding implements ScwSerializableEntity {
	public String symbol, target;

	public ScwMaterialBinding() {
	}

	public ScwMaterialBinding(String symbol, String target) {
		this.symbol = symbol;
		this.target = target;
	}

	@Override
	public void load(ScwInputStream is) throws IOException {
		symbol = is.readUTF();
		target = is.readUTF();
	}

	@Override
	public void save(ScwOutputStream os) throws IOException {
		os.writeUTF(symbol);
		os.writeUTF(target);
	}

	public ColladaInstanceMaterial asColladaInstanceMaterial() {
		return new ColladaInstanceMaterial(symbol, target);
	}

	public void loadColladaInstanceMaterial(ColladaInstanceMaterial colladaInstanceMaterial) {
		symbol = colladaInstanceMaterial.symbol;
		target = colladaInstanceMaterial.target;
	}
}
