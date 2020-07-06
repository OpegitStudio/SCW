package daniillnull.collada.geometry;

import daniillnull.collada.misc.ColladaInput;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

public class ColladaVertices {
	@XmlElement(name = "input")
	public final List<ColladaInput> inputs;

	@XmlAttribute(name = "id")
	public String id;

	public ColladaVertices() {
		inputs = new ArrayList<>();
	}

	public ColladaVertices(String id) {
		this();
		this.id = id;
	}
}
