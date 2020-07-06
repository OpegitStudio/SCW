package daniillnull.collada.node;

import daniillnull.collada.ColladaBaseObject;
import daniillnull.collada.ColladaIndex;
import daniillnull.collada.instance.ColladaCameraInstance;
import daniillnull.collada.instance.ColladaControllerInstance;
import daniillnull.collada.instance.ColladaGeometryInstance;
import daniillnull.collada.instance.ColladaInstance;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlType(propOrder = {"matrix", "rotate", "translate", "scale", "instances", "children"})
public class ColladaNode extends ColladaBaseObject {
	@XmlElement(name = "node")
	public final List<ColladaNode> children;
	@XmlElements({@XmlElement(name = "instance_geometry", type = ColladaGeometryInstance.class),
			@XmlElement(name = "instance_controller", type = ColladaControllerInstance.class),
			@XmlElement(name = "instance_camera", type = ColladaCameraInstance.class)})
	public final List<ColladaInstance> instances;
	@XmlElement
	public NamedFloatArray matrix, rotate, translate, scale;
	@XmlAttribute
	public String sid;

	@XmlAttribute
	public Type type;

	public ColladaNode() {
		children = new ArrayList<>();
		instances = new ArrayList<>();
	}

	public ColladaNode(String id, String sid) {
		this();
		this.id = id;
		this.sid = sid;
	}

	@Override
	protected void buildIndex(ColladaIndex index) {
		super.buildIndex(index);
		for (ColladaNode subnode : children) {
			subnode.buildIndex(index);
		}
	}

	public enum Type {
		JOINT, NODE
	}

	public static class NamedFloatArray {
		@XmlValue
		public float[] value;

		@XmlAttribute
		public String sid;

		public NamedFloatArray() {
		}

		public NamedFloatArray(float[] value, String sid) {
			this.value = value;
			this.sid = sid;
		}
	}
}
