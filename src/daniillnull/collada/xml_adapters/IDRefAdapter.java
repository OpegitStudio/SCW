package daniillnull.collada.xml_adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class IDRefAdapter extends XmlAdapter<String, String> {
	@Override
	public String unmarshal(String s) throws Exception {
		if (s.charAt(0) != '#') {
			throw new RuntimeException("Invalid IDRef");
		}
		return s.substring(1);
	}

	@Override
	public String marshal(String s) throws Exception {
		return "#" + s;
	}
}
