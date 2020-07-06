package daniillnull.scwtool;

import daniillnull.collada.Collada;
import daniillnull.collada.geometry.ColladaGeometry;
import daniillnull.collada.geometry.ColladaMesh;
import daniillnull.collada.geometry.ColladaTriangles;
import daniillnull.collada.geometry.ColladaVertices;
import daniillnull.collada.misc.ColladaInput;
import daniillnull.collada.node.ColladaNode;
import daniillnull.collada.node.ColladaVisualScene;
import daniillnull.collada.scene.ColladaScene;
import daniillnull.collada.source.ColladaSource;
import daniillnull.collada.source.ColladaSourceAccessor;
import daniillnull.scw.ScwFile;
import daniillnull.scw.sections.ScwHeader;
import daniillnull.scw.sections.ScwScene;
import daniillnull.scw.util.ScwInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
	public static void main(String[] args) throws Exception {
		JAXBContext context = JAXBContext.newInstance(Collada.class);

		Unmarshaller unmarshaller = context.createUnmarshaller();
		Collada newCollada = (Collada) unmarshaller.unmarshal(new FileReader("fred31_attack.dae"));

		ScwFile another = new ScwFile();
		another.importCollada(newCollada, true, true, true, false);
		ScwHeader scwHeader = new ScwHeader();
		scwHeader.version = 2;
		scwHeader.frameRate = 30;
		scwHeader.animationFrameEnd = 40;
		scwHeader.materialsFilename = "";
		another.setHeader(scwHeader);
		ScwScene scwScene = another.getScene();
		scwScene.rotateRoot(0, -90, 0);
		scwScene.scaleRoot(2, 2, 2);
		Files.write(Paths.get("chara_attack.scw"), another.save());

		ScwFile scwFile = loadFile("bgr_bazaar.scw");
		Collada collada = scwFile.asCollada();

		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(collada, new FileWriter("out.dae"));
	}

	public static ScwFile loadFile(String filename) throws IOException {
		ScwFile scwFile = new ScwFile();
		ScwInputStream inputStream = new ScwInputStream(new FileInputStream(filename));
		scwFile.load(inputStream);
		return scwFile;
	}

	public static Collada createTestCollada() {
		ColladaVertices vertices = new ColladaVertices("id1-vertices");
		vertices.inputs.add(new ColladaInput("POSITION", "#id1-positions"));

		List<ColladaInput> trianglesInputs = List.of(new ColladaInput("VERTEX", "#id1-vertices", 0));
		ColladaTriangles triangles = new ColladaTriangles(new int[]{0, 1, 2}, 1, null, trianglesInputs);

		List<ColladaSource> geom1Sources = List.of(new ColladaSource("id1-positions", new float[]{0, 0, 0,
				0, 1, 0.5f,
				0, 0, 1}, 3, ColladaSourceAccessor.XYZ_FLOAT_PARAMS));

		ColladaMesh mesh = new ColladaMesh(vertices, List.of(triangles), geom1Sources);
		ColladaGeometry geom1 = new ColladaGeometry("id1", mesh);

		ColladaNode mainNode = new ColladaNode("mainnode", null);
		mainNode.instances.add(geom1.asInstance());

		ColladaVisualScene colladaVisualScene = new ColladaVisualScene("mainscene");
		colladaVisualScene.nodes.add(mainNode);

		ColladaScene colladaScene = new ColladaScene();
		colladaScene.sceneInstances.add(colladaVisualScene.asInstance());

		Collada collada = new Collada();
		collada.libraryGeometries.add(geom1);
		collada.libraryVisualScenes.add(colladaVisualScene);
		collada.scene = colladaScene;

		return collada;
	}
}
