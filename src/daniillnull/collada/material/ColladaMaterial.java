package daniillnull.collada.material;

import daniillnull.collada.ColladaBaseObject;
import daniillnull.collada.instance.ColladaEffectInstance;

import javax.xml.bind.annotation.XmlElement;

public class ColladaMaterial extends ColladaBaseObject {
	@XmlElement(name = "instance_effect")
	public ColladaEffectInstance effectInstance;

	public ColladaMaterial() {
	}

	public ColladaMaterial(String id, ColladaEffectInstance effectInstance) {
		this.id = id;
		this.effectInstance = effectInstance;
	}
}
