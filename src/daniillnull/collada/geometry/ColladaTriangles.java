package daniillnull.collada.geometry;

import daniillnull.collada.misc.ColladaInput;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import java.util.ArrayList;
import java.util.List;

public class ColladaTriangles extends ColladaPrimitives {
	@XmlElement(name = "input")
	public final List<ColladaInput> inputs;

	@XmlElement(name = "p")
	@XmlList
	public int[] indices;

	@XmlAttribute
	public int count;

	@XmlAttribute
	public String material;

	public ColladaTriangles() {
		inputs = new ArrayList<>();
	}

	public ColladaTriangles(int[] indices, int count, String material, List<ColladaInput> inputs) {
		this.indices = indices;
		this.count = count;
		this.material = material;
		this.inputs = inputs;
	}
}
