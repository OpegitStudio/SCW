package daniillnull.collada.material;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

public class ColladaBindMaterial {
	@XmlElement(name = "technique_common", required = true)
	public TechniqueCommon techniqueCommon;

	public ColladaBindMaterial() {
	}

	public ColladaBindMaterial(List<ColladaInstanceMaterial> materialInstances) {
		techniqueCommon = new TechniqueCommon(materialInstances);
	}

	@XmlType(name = "daniillnull_collada_material_ColladaBindMaterial_TechniqueCommon")
	public static class TechniqueCommon {
		@XmlElement(name = "instance_material")
		public final List<ColladaInstanceMaterial> materialInstances;

		public TechniqueCommon() {
			materialInstances = new ArrayList<>();
		}

		public TechniqueCommon(List<ColladaInstanceMaterial> materialInstances) {
			this.materialInstances = materialInstances;
		}
	}
}
