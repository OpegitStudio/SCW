package daniillnull.collada.effect;

import daniillnull.collada.ColladaBaseObject;
import daniillnull.collada.effect.profile.ColladaProfile;
import daniillnull.collada.effect.profile.ColladaProfileCommon;
import daniillnull.collada.instance.ColladaEffectInstance;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class ColladaEffect extends ColladaBaseObject {
	@XmlElements({@XmlElement(name = "profile_COMMON", type = ColladaProfileCommon.class)})
	public ColladaProfile profile;

	public ColladaEffect() {
	}

	public ColladaEffect(String id, ColladaProfile profile) {
		this.id = id;
		this.profile = profile;
	}

	@Override
	public ColladaEffectInstance asInstance() {
		return new ColladaEffectInstance(id);
	}
}
