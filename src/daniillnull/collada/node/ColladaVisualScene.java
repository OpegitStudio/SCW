package daniillnull.collada.node;

import daniillnull.collada.ColladaBaseObject;
import daniillnull.collada.ColladaIndex;
import daniillnull.collada.instance.ColladaInstance;
import daniillnull.collada.instance.ColladaVisualSceneInstance;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

public class ColladaVisualScene extends ColladaBaseObject {
	@XmlElement(name = "node")
	public final List<ColladaNode> nodes;

	public ColladaVisualScene() {
		nodes = new ArrayList<>();
	}

	public ColladaVisualScene(String id) {
		this();
		this.id = id;
	}

	@Override
	public ColladaInstance asInstance() {
		return new ColladaVisualSceneInstance(id);
	}

	@Override
	protected void buildIndex(ColladaIndex index) {
		super.buildIndex(index);
		for (ColladaNode node : nodes) {
			node.buildIndex(index);
		}
	}
}
