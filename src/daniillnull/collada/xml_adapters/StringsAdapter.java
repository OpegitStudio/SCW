package daniillnull.collada.xml_adapters;

import daniillnull.collada.TypeUtils;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StringsAdapter extends XmlAdapter<String, String[]> {
	@Override
	public String[] unmarshal(String s) throws Exception {
		return TypeUtils.string2strings(s);
	}

	@Override
	public String marshal(String[] strings) throws Exception {
		return TypeUtils.strings2string(strings);
	}
}
