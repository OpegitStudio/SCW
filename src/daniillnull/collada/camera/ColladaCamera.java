package daniillnull.collada.camera;

import daniillnull.collada.ColladaBaseObject;

import javax.xml.bind.annotation.XmlElement;

public class ColladaCamera extends ColladaBaseObject {
	@XmlElement(name = "optics")
	public ColladaOptics optics;

	public ColladaCamera() {
	}

	public ColladaCamera(String id, ColladaOptics optics) {
		this.id = id;
		this.optics = optics;
	}
}
