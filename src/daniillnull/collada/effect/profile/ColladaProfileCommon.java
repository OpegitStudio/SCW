package daniillnull.collada.effect.profile;

import daniillnull.collada.effect.technique.ColladaEffectTechnique;

import javax.xml.bind.annotation.XmlElement;

public class ColladaProfileCommon extends ColladaProfile {
	@XmlElement(name = "technique")
	public ColladaEffectTechnique technique;

	public ColladaProfileCommon() {
	}

	public ColladaProfileCommon(ColladaEffectTechnique technique) {
		this.technique = technique;
	}
}
