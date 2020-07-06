package daniillnull.collada.effect.technique;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ColladaEffectTechnique {
	@XmlAttribute
	public String sid;

	@XmlElement
	public ColladaPhong phong;

	public ColladaEffectTechnique() {
	}

	public ColladaEffectTechnique(String sid, ColladaPhong phong) {
		this.sid = sid;
		this.phong = phong;
	}
}
