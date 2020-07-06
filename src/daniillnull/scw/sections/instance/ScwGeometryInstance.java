package daniillnull.scw.sections.instance;

import daniillnull.collada.ColladaIndex;
import daniillnull.collada.instance.ColladaGeometryInstance;
import daniillnull.collada.material.ColladaBindMaterial;
import daniillnull.collada.material.ColladaInstanceMaterial;
import daniillnull.scw.util.ScwInputStream;
import daniillnull.scw.util.ScwOutputStream;
import daniillnull.scw.util.Suffix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScwGeometryInstance extends ScwInstance<ColladaGeometryInstance> {
	public final List<ScwMaterialBinding> materialBindings;
	public String geometryName;

	public ScwGeometryInstance() {
		materialBindings = new ArrayList<>();
	}

	@Override
	public void load(ScwInputStream is) throws IOException {
		geometryName = is.readUTF();
		int materialsCount = is.readShort();
		for (int i = 0; i < materialsCount; i++) {
			ScwMaterialBinding materialBinding = new ScwMaterialBinding();
			materialBinding.load(is);
			materialBindings.add(materialBinding);
		}
	}

	@Override
	public void save(ScwOutputStream os) throws IOException {
		os.writeUTF(geometryName);
		os.writeShort(materialBindings.size());
		for (ScwMaterialBinding materialBinding : materialBindings) {
			materialBinding.save(os);
		}
	}

	@Override
	public String getSectionName() {
		return "GEOM";
	}

	@Override
	public void loadCollada(ColladaGeometryInstance collada, ColladaIndex colladaIndex) {
		materialBindings.clear();

		if (collada.bindMaterial != null) {
			for (ColladaInstanceMaterial colladaInstanceMaterial : collada.bindMaterial.techniqueCommon.materialInstances) {
				ScwMaterialBinding materialBinding = new ScwMaterialBinding();
				materialBinding.loadColladaInstanceMaterial(colladaInstanceMaterial);
				materialBindings.add(materialBinding);
			}
		}

		geometryName = Suffix.remove(collada.id, Suffix.GEOMETRY_MESH);
	}

	@Override
	public ColladaGeometryInstance asCollada() {
		List<ColladaInstanceMaterial> materialInstances = new ArrayList<>();
		for (ScwMaterialBinding materialBinding : materialBindings) {
			materialInstances.add(materialBinding.asColladaInstanceMaterial());
		}
		ColladaBindMaterial bindMaterial = new ColladaBindMaterial(materialInstances);
		return new ColladaGeometryInstance(Suffix.create(geometryName, Suffix.GEOMETRY_MESH), bindMaterial);
	}
}
