package daniillnull.collada.animation;

import daniillnull.collada.misc.ColladaInput;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import java.util.ArrayList;
import java.util.List;

public class ColladaSampler {
	@XmlElement(name = "input")
	public final List<ColladaInput> inputs;

	@XmlAttribute
	@XmlID
	public String id;

	public ColladaSampler() {
		inputs = new ArrayList<>();
	}

	public ColladaSampler(String id, List<ColladaInput> inputs) {
		this.id = id;
		this.inputs = inputs;
	}
}
