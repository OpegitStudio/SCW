package daniillnull.collada.instance;

import daniillnull.collada.xml_adapters.IDRefAdapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public abstract class ColladaInstance {
	@XmlID
	@XmlAttribute(name = "url")
	@XmlJavaTypeAdapter(IDRefAdapter.class)
	public String id;

	public ColladaInstance() {
	}

	public ColladaInstance(String id) {
		this.id = id;
	}
}
