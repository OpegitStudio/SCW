package daniillnull.collada.animation;

import daniillnull.collada.xml_adapters.IDRefAdapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class ColladaChannel {
	@XmlAttribute
	@XmlJavaTypeAdapter(IDRefAdapter.class)
	public String source;

	@XmlAttribute
	public String target;

	public ColladaChannel() {
	}

	public ColladaChannel(String source, String target) {
		this.source = source;
		this.target = target;
	}
}
