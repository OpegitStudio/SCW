package daniillnull.collada.xml_adapters;

import daniillnull.collada.TypeUtils;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class IntsAdapter extends XmlAdapter<String, int[]> {
	@Override
	public int[] unmarshal(String s) throws Exception {
		return TypeUtils.string2ints(s);
	}

	@Override
	public String marshal(int[] ints) throws Exception {
		return TypeUtils.ints2string(ints);
	}
}
