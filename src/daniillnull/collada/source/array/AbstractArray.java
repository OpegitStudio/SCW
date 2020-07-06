package daniillnull.collada.source.array;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
public abstract class AbstractArray {
	@XmlAttribute
	public int count;

	@XmlAttribute
	@XmlID
	public String id;

	public float[] getFloats() {
		return null;
	}

	public String[] getNames() {
		return null;
	}
}
