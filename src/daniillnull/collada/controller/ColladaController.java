package daniillnull.collada.controller;

import daniillnull.collada.ColladaBaseObject;
import daniillnull.collada.ColladaIndex;
import daniillnull.collada.instance.ColladaControllerInstance;

import javax.xml.bind.annotation.XmlElement;

public class ColladaController extends ColladaBaseObject {
	@XmlElement
	public ColladaSkin skin;

	public ColladaController() {
	}

	public ColladaController(String id, ColladaSkin skin) {
		this.id = id;
		this.skin = skin;
	}

	public ColladaControllerInstance asControllerInstance() {
		return new ColladaControllerInstance(id, null);
	}

	public String getSource() {
		return skin.source;
	}

	@Override
	protected void buildIndex(ColladaIndex index) {
		super.buildIndex(index);
		skin.buildIndex(index);
	}
}
