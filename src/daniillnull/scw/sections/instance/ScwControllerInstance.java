package daniillnull.scw.sections.instance;

import daniillnull.collada.ColladaIndex;
import daniillnull.collada.controller.ColladaController;
import daniillnull.collada.geometry.ColladaGeometry;
import daniillnull.collada.instance.ColladaControllerInstance;
import daniillnull.collada.material.ColladaBindMaterial;
import daniillnull.collada.material.ColladaInstanceMaterial;
import daniillnull.scw.util.ScwInputStream;
import daniillnull.scw.util.ScwOutputStream;
import daniillnull.scw.util.Suffix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScwControllerInstance extends ScwInstance<ColladaControllerInstance> {
	public final List<ScwMaterialBinding> materialBindings;
	public String geometryName; // Notice: SCW doesn't have controllers, so CONT links to GEOM

	public ScwControllerInstance() {
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
		return "CONT";
	}

	@Override
	public void loadCollada(ColladaControllerInstance collada, ColladaIndex colladaIndex) {
		materialBindings.clear();

		if (collada.bindMaterial != null) {
			for (ColladaInstanceMaterial colladaInstanceMaterial : collada.bindMaterial.techniqueCommon.materialInstances) {
				ScwMaterialBinding materialBinding = new ScwMaterialBinding();
				materialBinding.loadColladaInstanceMaterial(colladaInstanceMaterial);
				materialBindings.add(materialBinding);
			}
		}

		// Search for related geometry and get its name
		ColladaController colladaController = colladaIndex.get(collada.id);
		ColladaGeometry colladaGeometry = colladaIndex.get(colladaController.getSource());
		geometryName = Suffix.remove(colladaGeometry.id, Suffix.GEOMETRY_MESH);
	}

	@Override
	public ColladaControllerInstance asCollada() {
		List<ColladaInstanceMaterial> materialInstances = new ArrayList<>();
		for (ScwMaterialBinding materialBinding : materialBindings) {
			materialInstances.add(materialBinding.asColladaInstanceMaterial());
		}
		ColladaBindMaterial bindMaterial = new ColladaBindMaterial(materialInstances);
		return new ColladaControllerInstance(Suffix.create(geometryName, Suffix.CONTROLLER_SKIN), bindMaterial);
	}
}
