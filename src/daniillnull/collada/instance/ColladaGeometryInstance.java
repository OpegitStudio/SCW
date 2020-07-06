package daniillnull.collada.instance;

import daniillnull.collada.material.ColladaBindMaterial;

import javax.xml.bind.annotation.XmlElement;

public class ColladaGeometryInstance extends ColladaInstance {
	@XmlElement(name = "bind_material")
	public ColladaBindMaterial bindMaterial;

	public ColladaGeometryInstance() {
	}

	public ColladaGeometryInstance(String id, ColladaBindMaterial bindMaterial) {
		super(id);
		this.bindMaterial = bindMaterial;
	}
}
