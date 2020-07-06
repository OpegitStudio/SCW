package daniillnull.collada.controller;

import daniillnull.collada.misc.ColladaInput;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import java.util.ArrayList;
import java.util.List;

public class ColladaVertexWeights {
	@XmlElement(name = "input")
	public final List<ColladaInput> inputs;

	@XmlElement(name = "vcount")
	@XmlList
	public int[] vCount;

	@XmlElement
	@XmlList
	public int[] v;

	@XmlAttribute
	public int count;

	public ColladaVertexWeights() {
		inputs = new ArrayList<>();
	}

	public ColladaVertexWeights(int[] vCount, int[] v, List<ColladaInput> inputs) {
		this.vCount = vCount;
		this.v = v;
		this.inputs = inputs;
		count = vCount.length;
	}
}
