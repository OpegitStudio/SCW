package daniillnull.scw;

import daniillnull.collada.Collada;
import daniillnull.collada.ColladaAsset;
import daniillnull.collada.ColladaIndex;
import daniillnull.collada.animation.ColladaAnimation;
import daniillnull.collada.animation.ColladaChannel;
import daniillnull.collada.animation.ColladaSampler;
import daniillnull.collada.camera.ColladaCamera;
import daniillnull.collada.camera.ColladaOptics;
import daniillnull.collada.controller.ColladaController;
import daniillnull.collada.controller.ColladaSkin;
import daniillnull.collada.effect.ColladaEffect;
import daniillnull.collada.geometry.ColladaGeometry;
import daniillnull.collada.geometry.ColladaMesh;
import daniillnull.collada.instance.ColladaInstance;
import daniillnull.collada.material.ColladaMaterial;
import daniillnull.collada.node.ColladaNode;
import daniillnull.collada.node.ColladaVisualScene;
import daniillnull.collada.scene.ColladaScene;
import daniillnull.scw.sections.*;
import daniillnull.scw.sections.subs.ScwNode;
import daniillnull.scw.util.ScwConfig;
import daniillnull.scw.util.ScwInputStream;
import daniillnull.scw.util.ScwOutputStream;
import daniillnull.scw.util.Suffix;
import daniillnull.util.Assert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.zip.CRC32;

public class ScwFile {
	private final List<ScwMaterial> materials;
	private final List<ScwCamera> cameras;
	private final List<ScwGeometry> geometries;
	private ScwScene scene;
	private ScwHeader header;

	public ScwFile() {
		materials = new ArrayList<>();
		cameras = new ArrayList<>();
		geometries = new ArrayList<>();
	}

	public void load(ScwInputStream is) throws IOException {
		materials.clear();
		cameras.clear();
		geometries.clear();
		scene = null;
		header = null;

		if (!is.readSectionName().equals("SC3D")) {
			throw new IOException("Invalid header");
		}

		while (is.hasAvailable()) {
			int sectionLength = is.readInt();
			String sectionType = is.readSectionName();
			byte[] sectionContent = new byte[sectionLength];
			is.readFully(sectionContent);
			int checksum = is.readInt();

			System.out.println("[INFO] Parsing section: " + sectionType + " (length: " + sectionLength + ", crc: 0x" + Integer.toHexString(checksum) + ")");

			ScwInputStream sectionIs = new ScwInputStream(sectionContent);
			sectionIs.updateConfig(is.getConfig());

			switch (sectionType) {
				case "HEAD":
					ScwHeader header = new ScwHeader();
					header.load(sectionIs);

					Assert.doAssert(this.header != null, "Try to set header when another one exists!");
					Assert.doAssert((header.version < 1 || header.version > 2), "Unsupported SC3D version");

					ScwConfig config = is.getConfig();
					config.version = header.version;
					config.frameRate = header.frameRate;

					this.header = header;
					break;
				case "MATE":
					ScwMaterial mate = new ScwMaterial();
					mate.load(sectionIs);

					System.out.println("[DEBUG] Material: " + mate);

					materials.add(mate);
					break;
				case "CAME":
					ScwCamera came = new ScwCamera();
					came.load(sectionIs);

					System.out.println("[DEBUG] Camera: " + came);

					cameras.add(came);
					break;
				case "GEOM":
					ScwGeometry geom = new ScwGeometry();
					geom.load(sectionIs);

					geometries.add(geom);
					break;
				case "NODE":
					ScwScene scene = new ScwScene();
					scene.load(sectionIs);

					Assert.doAssert(this.scene != null, "Try to set scene when another one exists!");

					this.scene = scene;
					break;
			}

			if (sectionIs.hasAvailable()) {
				System.out.println("[WARNING] sectionInputStream.available() = " + sectionIs.available());
			}
		}

		System.out.println("[INFO] SCW File info:");
		System.out.println("[INFO]  Version: " + header.version);
		System.out.println("[INFO]  Frame rate: " + header.frameRate);
		System.out.println("[INFO]  Unknown2: " + header.unknown2);
		System.out.println("[INFO]  Unknown3: " + header.animationFrameEnd);
		System.out.println("[INFO]  Materials filename: " + header.materialsFilename);
		System.out.println("[INFO]  UnknownB: " + header.unknownB);
	}

	public byte[] save() throws IOException {
		ByteArrayOutputStream bytesOs = new ByteArrayOutputStream();
		ScwOutputStream os = new ScwOutputStream(bytesOs);
		ScwConfig config = os.getConfig();
		CRC32 crc32calc = new CRC32();
		List<ScwSection> sections = getAllSections();

		config.version = header.version;
		config.frameRate = header.frameRate;

		os.writeSectionName("SC3D");

		for (ScwSection section : sections) {
			ByteArrayOutputStream sectionBytesOs = new ByteArrayOutputStream();
			ScwOutputStream sectionOs = new ScwOutputStream(sectionBytesOs);
			sectionOs.updateConfig(config);

			section.save(sectionOs);

			byte[] sectionData = sectionBytesOs.toByteArray();
			String sectionName = section.getSectionName();
			crc32calc.reset();
			crc32calc.update(sectionData);
			int checksum = (int) crc32calc.getValue();

			os.writeInt(sectionData.length);
			os.writeSectionName(sectionName);
			os.write(sectionData);
			os.writeInt(checksum);
		}

		// WEND
		os.writeInt(0);
		os.writeSectionName("WEND");
		os.writeInt(511983662);

		return bytesOs.toByteArray();
	}

	public List<ScwGeometry> getGeometries() {
		return geometries;
	}

	public ScwScene getScene() {
		return scene;
	}

	public ScwHeader getHeader() {
		return header;
	}

	public void setHeader(ScwHeader header) {
		this.header = header;
	}

	public List<ScwSection> getAllSections() {
		Assert.doAssert(header == null, "Header is null!");

		List<ScwSection> sections = new ArrayList<>();
		sections.add(header);
		sections.addAll(materials);
		sections.addAll(cameras);
		sections.addAll(geometries);
		if (scene != null) {
			sections.add(scene);
		}
		return sections;
	}

	public void importMaterials(ScwFile file) {
		materials.addAll(file.materials);
	}

	public Collada asCollada() {
		Collada collada = new Collada();

		Date date = Date.from(Instant.now());
		ColladaAsset.Contributor contributor = new ColladaAsset.Contributor("daniillnull", "Null's Ultimate SCW Tool", List.of());
		collada.asset = new ColladaAsset(date, date, contributor);

		exportCollada(collada, true, true, true, true);

		return collada;
	}

	public void exportCollada(Collada collada, boolean includeMaterials, boolean includeGeometries, boolean includeAnimations, boolean includeCameras) {
		if (scene != null) {
			ColladaVisualScene colladaVisualScene = scene.asCollada();
			ColladaScene colladaScene = new ColladaScene();
			colladaScene.sceneInstances.add(colladaVisualScene.asInstance());

			collada.libraryVisualScenes.add(colladaVisualScene);
			if (includeAnimations) {
				collada.libraryAnimations.addAll(scene.asColladaAnimations());
			}

			collada.scene = colladaScene;
		}

		if (includeMaterials) {
			for (ScwMaterial material : materials) {
				ColladaEffect colladaEffect = material.asColladaEffect();
				collada.libraryEffects.add(colladaEffect);
				collada.libraryMaterials.add(new ColladaMaterial(material.name, colladaEffect.asInstance()));
			}
		}

		if (includeGeometries) {
			for (ScwGeometry geometry : geometries) {
				String geomId = Suffix.create(geometry.name, Suffix.GEOMETRY_MESH);
				String contId = Suffix.create(geometry.name, Suffix.CONTROLLER_SKIN);

				ColladaMesh colladaMesh = geometry.asColladaMesh(geomId);
				ColladaGeometry colladaGeometry = new ColladaGeometry(geomId, colladaMesh);
				collada.libraryGeometries.add(colladaGeometry);

				ColladaSkin colladaSkin = geometry.asColladaSkin(contId, geomId);
				if (colladaSkin != null) {
					ColladaController colladaController = new ColladaController(contId, colladaSkin);
					collada.libraryControllers.add(colladaController);
				}
			}
		}

		if (includeCameras) {
			for (ScwCamera camera : cameras) {
				String cameId = Suffix.create(camera.name, Suffix.CAMERA);

				ColladaOptics colladaOptics = camera.asColladaOptics();
				ColladaCamera colladaCamera = new ColladaCamera(cameId, colladaOptics);
				collada.libraryCameras.add(colladaCamera);
			}
		}
	}

	public void importCollada(Collada collada, boolean includeMaterials, boolean includeGeometries, boolean includeAnimations, boolean includeCameras) {
		ColladaIndex colladaIndex = collada.buildIndex();

		scene = null;

		if (collada.scene != null) {
			List<ColladaInstance> colladaVisualSceneInstances = collada.scene.sceneInstances;
			if (!colladaVisualSceneInstances.isEmpty()) {
				ColladaVisualScene colladaVisualScene = colladaIndex.get(colladaVisualSceneInstances.get(0).id);

				scene = new ScwScene();
				scene.loadCollada(colladaVisualScene, colladaIndex);
			}
		}

		if (includeMaterials) {
			materials.clear();
			for (ColladaMaterial colladaMaterial : collada.libraryMaterials) {
				ColladaEffect colladaEffect = colladaIndex.get(colladaMaterial.effectInstance.id);

				ScwMaterial material = new ScwMaterial(colladaMaterial.id);
				material.loadColladaEffect(colladaEffect);
				materials.add(material);
			}
		}

		if (includeGeometries) {
			// ScwJoint in geometries contains node ID, and these's no mapping for <skeleton> in SCW
			// We will search for all possible nodes and throw exception, if find same sid
			Map<String, String> sid2id = new HashMap<>();
			for (Object object : colladaIndex.values()) {
				if (object instanceof ColladaNode) {
					ColladaNode colladaNode = (ColladaNode) object;
					String sid = colladaNode.sid;
					if (sid != null && !sid.isEmpty()) {
						Assert.doAssert(sid2id.containsKey(sid), "There are multiple nodes with same 'sid' in collada document. Because of SCW restrictions that's not supported, please, fix it in input document.");
						sid2id.put(sid, colladaNode.id);
					}
				}
			}

			geometries.clear();

			Map<String, ColladaSkin> geomSkins = new HashMap<>();
			for (ColladaController colladaController : collada.libraryControllers) {
				ColladaSkin colladaSkin = colladaController.skin;
				Assert.doAssert(colladaSkin == null, "Controller \"" + colladaController.id + "\" is not skin");
				geomSkins.put(colladaSkin.source, colladaSkin);
			}
			for (ColladaGeometry colladaGeometry : collada.libraryGeometries) {
				String name = Suffix.remove(colladaGeometry.id, Suffix.GEOMETRY_MESH);
				ColladaSkin colladaSkin = geomSkins.get(colladaGeometry.id);

				ScwGeometry geometry = new ScwGeometry(name, name);
				if (colladaSkin != null) {
					geometry.loadColladaSkin(colladaSkin, sid2id, colladaIndex);
				}
				geometry.loadColladaMesh(colladaGeometry.mesh, colladaIndex);
				geometries.add(geometry);
			}
		}

		if (includeCameras) {
			cameras.clear();
			for (ColladaCamera colladaCamera : collada.libraryCameras) {
				String name = Suffix.remove(colladaCamera.id, Suffix.CAMERA);
				ScwCamera scwCamera = new ScwCamera(name);
				scwCamera.loadColladaOptics(colladaCamera.optics);
				cameras.add(scwCamera);
			}
		}

		if (scene != null && includeAnimations) {
			Map<String, ScwNode> nodeIndex = scene.buildIndex();

			for (ColladaAnimation colladaAnimation : collada.libraryAnimations) {
				for (ColladaChannel colladaChannel : colladaAnimation.getChannelsRecursively()) {
					ColladaSampler colladaSampler = colladaIndex.get(colladaChannel.source);
					String[] targetPath = colladaChannel.target.split("/");
					Assert.doAssert((targetPath.length == 2 && !targetPath[1].equals("transform")), "ColladaChannel target path is invalid");

					ScwNode node = nodeIndex.get(targetPath[0]);
					node.loadColladaAnimation(colladaSampler, colladaIndex);
				}
			}
		}
	}
}
