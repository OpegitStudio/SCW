package daniillnull.collada.geometry;

import daniillnull.collada.ColladaBaseObject;
import daniillnull.collada.ColladaIndex;
import daniillnull.collada.instance.ColladaGeometryInstance;
import daniillnull.collada.instance.ColladaInstance;

import javax.xml.bind.annotation.XmlElement;

public class ColladaGeometry extends ColladaBaseObject {
	@XmlElement
	public ColladaMesh mesh;

	public ColladaGeometry() {
	}

	public ColladaGeometry(String id, ColladaMesh mesh) {
		this.id = id;
		this.mesh = mesh;
	}

	@Override
	public ColladaInstance asInstance() {
		return new ColladaGeometryInstance(id, null);
	}

	@Override
	protected void buildIndex(ColladaIndex index) {
		super.buildIndex(index);
		mesh.buildIndex(index);
	}
}
