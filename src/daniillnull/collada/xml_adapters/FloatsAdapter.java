package daniillnull.collada.xml_adapters;

import daniillnull.collada.TypeUtils;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class FloatsAdapter extends XmlAdapter<String, float[]> {
	@Override
	public float[] unmarshal(String s) throws Exception {
		return TypeUtils.string2floats(s);
	}

	@Override
	public String marshal(float[] floats) throws Exception {
		return TypeUtils.floats2string(floats);
	}
}
