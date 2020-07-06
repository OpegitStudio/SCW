package daniillnull.collada.material;

import daniillnull.collada.xml_adapters.IDRefAdapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class ColladaInstanceMaterial {
	@XmlAttribute
	public String symbol;

	@XmlAttribute
	@XmlJavaTypeAdapter(IDRefAdapter.class)
	public String target;

	public ColladaInstanceMaterial() {
	}

	public ColladaInstanceMaterial(String symbol, String target) {
		this.symbol = symbol;
		this.target = target;
	}
}
