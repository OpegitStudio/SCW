package daniillnull.collada.source.array;

import javax.xml.bind.annotation.XmlValue;

public class NameArray extends AbstractArray {
	@XmlValue
	public String[] names;

	public NameArray() {
	}

	public NameArray(String[] names, String id) {
		this.names = names;
		this.id = id;
		count = names.length;
	}

	@Override
	public String[] getNames() {
		return names;
	}
}
