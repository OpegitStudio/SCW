package daniillnull.collada.source;

import daniillnull.collada.xml_adapters.IDRefAdapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ColladaSourceAccessor {
	public static final List<Param> XYZ_FLOAT_PARAMS = List.of(
			new Param("X", "float"),
			new Param("Y", "float"),
			new Param("Z", "float"));
	public static final List<Param> ST_FLOAT_PARAMS = List.of(
			new Param("S", "float"),
			new Param("T", "float"));
	public static final List<Param> RGBA_FLOAT_PARAMS = List.of(
			new Param("R", "float"),
			new Param("G", "float"),
			new Param("B", "float"),
			new Param("A", "float"));
	public static final List<Param> JOINT_NAME_PARAMS = List.of(new Param("JOINT", "name"));
	public static final List<Param> INTERPOLATION_NAME_PARAMS = List.of(new Param("INTERPOLATION", "name"));
	public static final List<Param> WEIGHT_FLOAT_PARAMS = List.of(new Param("WEIGHT", "float"));
	public static final List<Param> TIME_FLOAT_PARAMS = List.of(new Param("TIME", "float"));
	public static final List<Param> TRANSFORM_FLOAT4x4_PARAMS = List.of(new Param("TRANSFORM", "float4x4"));

	@XmlElement(name = "param")
	public final List<Param> params;

	@XmlAttribute
	public int count;

	@XmlAttribute
	@XmlJavaTypeAdapter(value = IDRefAdapter.class)
	public String source;

	@XmlAttribute
	public int stride;

	public ColladaSourceAccessor() {
		params = new ArrayList<>();
	}

	public ColladaSourceAccessor(List<Param> params, String source, int count, int stride) {
		this.params = params;
		this.source = source;
		this.count = count;
		this.stride = stride;
	}

	public static class Param {
		@XmlAttribute
		public String name, type;

		public Param() {
		}

		public Param(String name, String type) {
			this.name = name;
			this.type = type;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Param param = (Param) o;
			return Objects.equals(name, param.name) &&
					Objects.equals(type, param.type);
		}

		@Override
		public int hashCode() {
			return Objects.hash(name, type);
		}
	}
}
