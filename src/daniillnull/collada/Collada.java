package daniillnull.collada;

import daniillnull.collada.animation.ColladaAnimation;
import daniillnull.collada.camera.ColladaCamera;
import daniillnull.collada.controller.ColladaController;
import daniillnull.collada.effect.ColladaEffect;
import daniillnull.collada.geometry.ColladaGeometry;
import daniillnull.collada.material.ColladaMaterial;
import daniillnull.collada.node.ColladaVisualScene;
import daniillnull.collada.scene.ColladaScene;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@XmlRootElement(name = "COLLADA")
@XmlType(propOrder = {"asset", "libraryEffects", "libraryMaterials", "libraryControllers", "libraryCameras", "libraryGeometries", "libraryAnimations", "libraryVisualScenes", "scene"})
public class Collada {
	@XmlElementWrapper(name = "library_controllers")
	@XmlElement(name = "controller")
	public final List<ColladaController> libraryControllers;
	@XmlElementWrapper(name = "library_cameras")
	@XmlElement(name = "camera")
	public final List<ColladaCamera> libraryCameras;
	@XmlElementWrapper(name = "library_geometries")
	@XmlElement(name = "geometry")
	public final List<ColladaGeometry> libraryGeometries;
	@XmlElementWrapper(name = "library_visual_scenes")
	@XmlElement(name = "visual_scene")
	public final List<ColladaVisualScene> libraryVisualScenes;
	@XmlElementWrapper(name = "library_animations")
	@XmlElement(name = "animation")
	public final List<ColladaAnimation> libraryAnimations;
	@XmlElementWrapper(name = "library_effects")
	@XmlElement(name = "effect")
	public final List<ColladaEffect> libraryEffects;
	@XmlElementWrapper(name = "library_materials")
	@XmlElement(name = "material")
	public final List<ColladaMaterial> libraryMaterials;
	@XmlElement(name = "asset", required = true)
	public ColladaAsset asset;
	@XmlElement(name = "scene", required = true)
	public ColladaScene scene;
	@XmlAttribute(required = true)
	public String version;

	public Collada() {
		version = "1.4.1";

		libraryControllers = new ArrayList<>();
		libraryCameras = new ArrayList<>();
		libraryGeometries = new ArrayList<>();
		libraryVisualScenes = new ArrayList<>();
		libraryAnimations = new ArrayList<>();
		libraryEffects = new ArrayList<>();
		libraryMaterials = new ArrayList<>();
	}

	public ColladaIndex buildIndex() {
		ColladaIndex index = new ColladaIndex();
		Consumer<ColladaBaseObject> indexer = (obj) -> obj.buildIndex(index);
		libraryControllers.forEach(indexer);
		libraryCameras.forEach(indexer);
		libraryGeometries.forEach(indexer);
		libraryVisualScenes.forEach(indexer);
		libraryAnimations.forEach(indexer);
		libraryEffects.forEach(indexer);
		libraryMaterials.forEach(indexer);
		return index;
	}
}
