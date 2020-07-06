package daniillnull.collada.source;

import daniillnull.collada.source.array.AbstractArray;
import daniillnull.collada.source.array.FloatArray;
import daniillnull.collada.source.array.NameArray;

import javax.xml.bind.annotation.*;
import java.util.List;

public class ColladaSource {
	@XmlElements({@XmlElement(name = "float_array", type = FloatArray.class),
			@XmlElement(name = "Name_array", type = NameArray.class)})
	public AbstractArray array;

	@XmlElement(name = "technique_common", required = true)
	public TechniqueCommon techniqueCommon;

	@XmlAttribute
	@XmlID
	public String id;

	public ColladaSource() {
	}

	public ColladaSource(String id, float[] floats, int accessorStride, List<ColladaSourceAccessor.Param> accessorParams) {
		this.id = id;
		array = new FloatArray(floats, id + "-array");
		techniqueCommon = new TechniqueCommon(accessorParams, array.id, (floats.length / accessorStride), accessorStride);
	}

	public ColladaSource(String id, String[] names, int accessorStride, List<ColladaSourceAccessor.Param> accessorParams) {
		this.id = id;
		array = new NameArray(names, id + "-array");
		techniqueCommon = new TechniqueCommon(accessorParams, array.id, (names.length / accessorStride), accessorStride);
	}

	@XmlType(name = "daniillnull_collada_source_ColladaSource_TechniqueCommon")
	public static class TechniqueCommon {
		@XmlElement(required = true)
		public ColladaSourceAccessor accessor;

		public TechniqueCommon() {
		}

		public TechniqueCommon(List<ColladaSourceAccessor.Param> params, String source, int count, int stride) {
			this(new ColladaSourceAccessor(params, source, count, stride));
		}

		public TechniqueCommon(ColladaSourceAccessor accessor) {
			this.accessor = accessor;
		}
	}
}
