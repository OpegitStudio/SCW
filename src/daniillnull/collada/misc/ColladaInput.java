package daniillnull.collada.misc;

import daniillnull.collada.xml_adapters.IDRefAdapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class ColladaInput {
	@XmlAttribute
	public String semantic;

	@XmlAttribute
	@XmlJavaTypeAdapter(IDRefAdapter.class)
	public String source;

	@XmlAttribute
	public Integer offset;

	@XmlAttribute
	public Integer set;

	public ColladaInput() {
	}

	public ColladaInput(String semantic, String source, int offset, int set) {
		this(semantic, source);
		this.offset = offset;
		this.set = set;
	}

	public ColladaInput(String semantic, String source, int offset) {
		this(semantic, source);
		this.offset = offset;
	}

	public ColladaInput(String semantic, String source) {
		this.semantic = semantic;
		this.source = source;
	}
}
