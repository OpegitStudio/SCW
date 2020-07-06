package daniillnull.collada.scene;

import daniillnull.collada.ColladaBaseObject;
import daniillnull.collada.instance.ColladaInstance;
import daniillnull.collada.instance.ColladaVisualSceneInstance;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import java.util.ArrayList;
import java.util.List;

public class ColladaScene extends ColladaBaseObject {
	@XmlElements({@XmlElement(name = "instance_visual_scene", type = ColladaVisualSceneInstance.class)})
	public final List<ColladaInstance> sceneInstances;

	public ColladaScene() {
		sceneInstances = new ArrayList<>();
	}
}
