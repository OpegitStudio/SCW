package daniillnull.collada.source.array;

import javax.xml.bind.annotation.XmlValue;

public class FloatArray extends AbstractArray {
	@XmlValue
	public float[] values;

	public FloatArray() {
	}

	public FloatArray(float[] values, String id) {
		this.values = values;
		this.id = id;
		count = values.length;
	}

	@Override
	public float[] getFloats() {
		return values;
	}
}
