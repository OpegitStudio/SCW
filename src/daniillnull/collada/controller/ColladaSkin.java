package daniillnull.collada.controller;

import daniillnull.collada.ColladaIndex;
import daniillnull.collada.misc.ColladaInput;
import daniillnull.collada.source.ColladaSource;
import daniillnull.collada.xml_adapters.IDRefAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

@XmlType(propOrder = {"bindShapeMatrix", "sources", "jointsInputs", "vertexWeights"})
public class ColladaSkin {
	@XmlElement(name = "source")
	public final List<ColladaSource> sources;
	@XmlElementWrapper(name = "joints")
	@XmlElement(name = "input")
	public final List<ColladaInput> jointsInputs;
	@XmlElement(name = "bind_shape_matrix")
	@XmlList
	public float[] bindShapeMatrix;
	@XmlElement(name = "vertex_weights", required = true)
	public ColladaVertexWeights vertexWeights;

	@XmlAttribute
	@XmlJavaTypeAdapter(IDRefAdapter.class)
	public String source;

	public ColladaSkin() {
		sources = new ArrayList<>();
		jointsInputs = new ArrayList<>();
	}

	public ColladaSkin(String source, float[] bindShapeMatrix, int[] vCount, int[] v, ColladaSource jointsSource,
					   ColladaSource bindPosesSource, ColladaSource weightsSource) {
		this.source = source;
		this.bindShapeMatrix = bindShapeMatrix;

		sources = List.of(jointsSource, bindPosesSource, weightsSource);
		jointsInputs = List.of(new ColladaInput("JOINT", jointsSource.id),
				new ColladaInput("INV_BIND_MATRIX", bindPosesSource.id));

		List<ColladaInput> vertexWeightsInputs = List.of(new ColladaInput("JOINT", jointsSource.id, 0),
				new ColladaInput("WEIGHT", weightsSource.id, 1));

		vertexWeights = new ColladaVertexWeights(vCount, v, vertexWeightsInputs);
	}

	protected void buildIndex(ColladaIndex index) {
		for (ColladaSource source : sources) {
			index.register(source.id, source);
		}
	}
}
