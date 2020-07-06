package daniillnull.collada.camera;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

public class ColladaOptics {
	@XmlElement(name = "technique_common", required = true)
	public TechniqueCommon techniqueCommon;

	public ColladaOptics() {
	}

	public ColladaOptics(ColladaPerspective perspective) {
		techniqueCommon = new TechniqueCommon(perspective);
	}

	@XmlType(name = "daniillnull_collada_camera_ColladaOptics_TechniqueCommon")
	public static class TechniqueCommon {
		@XmlElement(name = "perspective")
		public ColladaPerspective perspective;

		public TechniqueCommon() {
		}

		public TechniqueCommon(ColladaPerspective perspective) {
			this.perspective = perspective;
		}
	}
}
