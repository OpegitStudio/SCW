package daniillnull.collada.camera;

import javax.xml.bind.annotation.XmlElement;

public class ColladaPerspective {
	@XmlElement(name = "zfar", required = true)
	public float zFar;
	@XmlElement(name = "znear", required = true)
	public float zNear;
	@XmlElement(name = "aspect_ratio")
	public float aspectRatio;
	@XmlElement(name = "xfov")
	public float xFov;

	public ColladaPerspective() {
	}

	public ColladaPerspective(float zFar, float zNear, float aspectRatio, float xFov) {
		this.zFar = zFar;
		this.zNear = zNear;
		this.aspectRatio = aspectRatio;
		this.xFov = xFov;
	}
}
