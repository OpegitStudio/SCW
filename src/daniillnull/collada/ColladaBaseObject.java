package daniillnull.collada;

import daniillnull.collada.instance.ColladaInstance;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;

public abstract class ColladaBaseObject {
	@XmlID
	@XmlAttribute(name = "id")
	public String id;

	public ColladaInstance asInstance() {
		return null;
	}

	protected void buildIndex(ColladaIndex index) {
		index.register(id, this);
	}
}
