package daniillnull.scwtool;

import daniillnull.collada.Collada;
import daniillnull.scw.ScwFile;
import daniillnull.scw.sections.ScwHeader;
import daniillnull.scw.sections.ScwScene;
import daniillnull.scw.util.ScwInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConsoleMain {
	public static void main(String[] args) throws IOException, JAXBException {
		System.out.println("Ultimate SCW Tool - 1.0");
		System.out.println("-> Implemented by @daniillnull");
		System.out.println("-> github.com/opegitstudio/scw");
		System.out.println("-> dnull.xyz | nulls.team");
		System.out.println("-> (c) Null's Team, 2020");
		System.out.println();

		if (args.length >= 2 && args[0].equals("dae2scw")) {
			float[] scale = null, rotate = null, translate = null;
			boolean mate = true, geom = true, anim = true, came = false;
			String filename;

			int i = 1;
			int end = args.length - 2;
			while (i < end) {
				String arg = args[i];
				switch (arg) {
					case "-s":
						String[] splitS = args[i + 1].split(";");
						scale = new float[]{Float.parseFloat(splitS[0]), Float.parseFloat(splitS[1]), Float.parseFloat(splitS[2])};
						break;
					case "-r":
						String[] splitR = args[i + 1].split(";");
						rotate = new float[]{Float.parseFloat(splitR[0]), Float.parseFloat(splitR[1]), Float.parseFloat(splitR[2])};
						break;
					case "-t":
						String[] splitT = args[i + 1].split(";");
						translate = new float[]{Float.parseFloat(splitT[0]), Float.parseFloat(splitT[1]), Float.parseFloat(splitT[2])};
						break;
					case "-a":
						String value = args[i + 1];
						mate = (value.indexOf('M') != -1);
						geom = (value.indexOf('G') != -1);
						anim = (value.indexOf('A') != -1);
						came = (value.indexOf('C') != -1);
						break;
					default:
						printHelp();
						return;
				}
				i += 2;
			}
			filename = args[i];

			runDae2Scw(scale, rotate, translate, mate, geom, anim, came, filename);
		} else if (args.length >= 2 && args[0].equals("scw2dae")) {
			String filename = args[1];
			String animFilename = (args.length >= 3 ? args[2] : null);
			runScw2Dae(filename, animFilename);
		} else {
			printHelp();
		}
	}

	private static void printHelp() {
		System.out.println("Usage: executable.jar <command> <command arguments...>");
		System.out.println("Available commands:");
		System.out.println("-> dae2scw [-s X;Y;Z][-r X;Y;Z][-t X;T;Z][-a MGAC] <dae file>");
		System.out.println("  Convert input collada DAE files to SCW");
		System.out.println("  -s : set scaling for root nodes");
		System.out.println("  -r : set rotation for root nodes");
		System.out.println("  -t : set translation for root nodes");
		System.out.println("  -a : set exported objects (M - materials, G - geometries, A - animations, C - cameras; in one sequence like MGAC)");
		System.out.println("-> scw2dae <scw file> [anim scw file]:");
		System.out.println("  Convert input SCW files to collada DAE");
	}

	private static void runDae2Scw(float[] scale, float[] rotate, float[] translate, boolean mate, boolean geom, boolean anim, boolean came, String filename) throws JAXBException, IOException {
		String outFilename = filename.substring(0, filename.lastIndexOf('.')) + ".scw";

		JAXBContext context = JAXBContext.newInstance(Collada.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Collada collada = (Collada) unmarshaller.unmarshal(new FileReader(filename));

		ScwFile mainFile = new ScwFile();
		mainFile.importCollada(collada, mate, geom, anim, came);
		ScwHeader scwHeader = new ScwHeader();
		// TODO: More parameters
		scwHeader.version = 2;
		scwHeader.frameRate = 30;
		scwHeader.materialsFilename = "";
		mainFile.setHeader(scwHeader);
		ScwScene scwScene = mainFile.getScene();
		if (scale != null) {
			scwScene.scaleRoot(scale[0], scale[1], scale[2]);
		}
		if (rotate != null) {
			scwScene.rotateRoot(rotate[0], rotate[1], rotate[2]);
		}
		if (translate != null) {
			scwScene.translateRoot(translate[0], translate[1], translate[2]);
		}

		byte[] data = mainFile.save();
		Files.write(Paths.get(outFilename), data);

		System.out.println("Dae2Scw: Well done! Saved as " + outFilename);
	}

	private static void runScw2Dae(String filename, String animFilename) throws IOException, JAXBException {
		String outFilename = filename.substring(0, filename.lastIndexOf('.')) + ".dae";
		FileInputStream is = new FileInputStream(filename);

		ScwFile mainFile = new ScwFile();
		mainFile.load(new ScwInputStream(is));

		if (animFilename != null) {
			FileInputStream is2 = new FileInputStream(animFilename);
			ScwFile animFile = new ScwFile();
			animFile.load(new ScwInputStream(is2));

			mainFile.getScene().importFrames(animFile.getScene());
		}

		String materialsFilename = mainFile.getHeader().materialsFilename;
		if (materialsFilename != null && !materialsFilename.isEmpty()) {
			try {
				FileInputStream is3 = new FileInputStream(materialsFilename);
				ScwFile mateFile = new ScwFile();
				mateFile.load(new ScwInputStream(is3));

				mainFile.importMaterials(mateFile);
			} catch (FileNotFoundException ex) {
				System.out.println("[WARNING] Can't find related materials file: " + materialsFilename + ". Materials won't be processed.");
			}
		}

		Collada collada = mainFile.asCollada();

		JAXBContext context = JAXBContext.newInstance(Collada.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(collada, new FileWriter(outFilename));

		System.out.println("Scw2Dae: Well done! Saved as " + outFilename);
	}
}
