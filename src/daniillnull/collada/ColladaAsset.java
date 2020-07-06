package daniillnull.collada;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import java.util.Date;
import java.util.List;

public class ColladaAsset {
	@XmlElement(required = true)
	@XmlSchemaType(name = "dateTime")
	public Date created, modified;
	@XmlElement
	public Contributor contributor;

	public ColladaAsset() {
	}

	public ColladaAsset(Date created, Date modified, Contributor contributor) {
		this.created = created;
		this.modified = modified;
		this.contributor = contributor;
	}

	public static class Contributor {
		@XmlElement
		public String author;
		@XmlElement(name = "authoring_tool")
		public String authoringTool;
		@XmlElement
		public List<String> comments;

		public Contributor() {
		}

		public Contributor(String author, String authoringTool, List<String> comments) {
			this.author = author;
			this.authoringTool = authoringTool;
			this.comments = comments;
		}
	}
}
