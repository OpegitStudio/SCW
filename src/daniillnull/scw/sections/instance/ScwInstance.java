package daniillnull.scw.sections.instance;

import daniillnull.collada.ColladaIndex;
import daniillnull.collada.instance.ColladaInstance;
import daniillnull.scw.ScwSerializableEntity;

public abstract class ScwInstance<T extends ColladaInstance> implements ScwSerializableEntity {
	public void loadCollada(T collada, ColladaIndex colladaIndex) {
		throw new RuntimeException("not implemented");
	}

	public T asCollada() {
		throw new RuntimeException("not implemented");
	}

	public abstract String getSectionName();

	public static ScwInstance createByType(String sectionType) {
		switch (sectionType) {
			case "CONT":
				return new ScwControllerInstance();

			case "GEOM":
				return new ScwGeometryInstance();

			case "CAME":
				return new ScwCameraInstance();

			default:
				throw new RuntimeException("Unknown ScwInstance type: " + sectionType);
		}
	}
}
