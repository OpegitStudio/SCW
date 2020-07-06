package daniillnull.collada.geometry;

import daniillnull.collada.ColladaIndex;
import daniillnull.collada.source.ColladaSource;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlType(propOrder = {"sources", "vertices", "primitivesList"})
public class ColladaMesh {
	@XmlElement(name = "source")
	public final List<ColladaSource> sources;
	@XmlElements({@XmlElement(name = "triangles", type = ColladaTriangles.class)})
	public final List<ColladaPrimitives> primitivesList;
	@XmlElement
	public ColladaVertices vertices;

	public ColladaMesh() {
		sources = new ArrayList<>();
		primitivesList = new ArrayList<>();
	}

	public ColladaMesh(ColladaVertices vertices, List<ColladaPrimitives> primitivesList, List<ColladaSource> sources) {
		this.primitivesList = primitivesList;
		this.vertices = vertices;
		this.sources = sources;
	}

	protected void buildIndex(ColladaIndex index) {
		index.register(vertices.id, vertices);
		for (ColladaSource source : sources) {
			index.register(source.id, source);
		}
	}
}
