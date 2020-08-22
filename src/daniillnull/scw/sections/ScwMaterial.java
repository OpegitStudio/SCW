package daniillnull.scw.sections;

import daniillnull.collada.effect.ColladaEffect;
import daniillnull.collada.effect.profile.ColladaProfileCommon;
import daniillnull.collada.effect.technique.ColladaEffectTechnique;
import daniillnull.collada.effect.technique.ColladaPhong;
import daniillnull.scw.ScwSection;
import daniillnull.scw.util.ScwInputStream;
import daniillnull.scw.util.ScwOutputStream;

import java.io.IOException;

public class ScwMaterial extends ScwSection {
	public String name, shaderPath, unknownS3, unknownS4, opacityTexPath, unknownS6, diffuseLightmap, specularLightmap, diffuseTexPath, specularTexPath, colorizeTexPath, emissionTexPath;
	public int unknownB1, unknownB2;
	public int ambientColor, shaderDefineFlags, diffuseColor, specularColor, colorizeColor, emissionColor;
	public float unknownF1, unknownF2;

	public ScwMaterial() {
	}

	public ScwMaterial(String name) {
		this.name = name;
	}

	@Override
	public void load(ScwInputStream is) throws IOException {
		name = is.readUTF();
		shaderPath = is.readUTF();
		unknownB1 = is.readUnsignedByte();
		unknownB2 = is.readUnsignedByte();
		ambientColor = is.readInt();
		if (is.readBoolean()) {
			diffuseTexPath = is.readUTF();
		} else {
			diffuseColor = is.readInt();
		}
		if (is.readBoolean()) {
			specularTexPath = is.readUTF();
		} else {
			specularColor = is.readInt();
		}
		unknownS3 = is.readUTF();
		unknownS4 = is.readUTF();
		if (is.readBoolean()) {
			colorizeTexPath = is.readUTF();
		} else {
			colorizeColor = is.readInt();
		}
		if (is.readBoolean()) {
			emissionTexPath = is.readUTF();
		} else {
			emissionColor = is.readInt();
		}
		opacityTexPath = is.readUTF();
		unknownF1 = is.readFloat();
		unknownF2 = is.readFloat();
		diffuseLightmap = is.readUTF();
		specularLightmap = is.readUTF();
		if (is.getConfig().version >= 2) {
			unknownS6 = is.readUTF();
		}
		shaderDefineFlags = is.readInt();
	}

	@Override
	public void save(ScwOutputStream os) throws IOException {
		os.writeUTF(name);
		os.writeUTF(shaderPath);
		os.write(unknownB1);
		os.write(unknownB2);
		os.writeInt(ambientColor);
		if (diffuseTexPath != null) {
			os.writeBoolean(true);
			os.writeUTF(diffuseTexPath);
		} else {
			os.writeBoolean(false);
			os.writeInt(diffuseColor);
		}
		if (specularTexPath != null) {
			os.writeBoolean(true);
			os.writeUTF(specularTexPath);
		} else {
			os.writeBoolean(false);
			os.writeInt(specularColor);
		}
		os.writeUTF(unknownS3);
		os.writeUTF(unknownS4);
		if (colorizeTexPath != null) {
			os.writeBoolean(true);
			os.writeUTF(colorizeTexPath);
		} else {
			os.writeBoolean(false);
			os.writeInt(colorizeColor);
		}
		if (emissionTexPath != null) {
			os.writeBoolean(true);
			os.writeUTF(emissionTexPath);
		} else {
			os.writeBoolean(false);
			os.writeInt(emissionColor);
		}
		os.writeUTF(opacityTexPath);
		os.writeFloat(unknownF1);
		os.writeFloat(unknownF2);
		os.writeUTF(diffuseLightmap);
		os.writeUTF(specularLightmap);
		if (os.getConfig().version >= 2) {
			os.writeUTF(unknownS6);
		}
		os.writeInt(shaderDefineFlags);
	}

	@Override
	public String getSectionName() {
		return "MATE";
	}

	@Override
	public String toString() {
		return "ScwMaterial{" +
				"name='" + name + '\'' +
				",\nshaderPath='" + shaderPath + '\'' +
				",\nunknownS3='" + unknownS3 + '\'' +
				",\nunknownS4='" + unknownS4 + '\'' +
				",\nopacityTexPath='" + opacityTexPath + '\'' +
				",\ndiffuseLightmap='" + diffuseLightmap + '\'' +
				",\nspecularLightmap='" + specularLightmap + '\'' +
				",\ndiffuseTexPath='" + diffuseTexPath + '\'' +
				",\nspecularTexPath='" + specularTexPath + '\'' +
				",\ncolorizeTexPath='" + colorizeTexPath + '\'' +
				",\nemissionTexPath='" + emissionTexPath + '\'' +
				",\nunknownB1=" + unknownB1 +
				",\nunknownB2=" + unknownB2 +
				",\nambientColor=" + ambientColor +
				",\nshaderDefineFlags=" + shaderDefineFlags +
				",\ndiffuseColor=" + diffuseColor +
				",\nspecularColor=" + specularColor +
				",\ncolorizeColor=" + colorizeColor +
				",\nemissionColor=" + emissionColor +
				",\nunknownF1=" + unknownF1 +
				",\nunknownF2=" + unknownF2 +
				'}';
	}

	public ColladaEffect asColladaEffect() {
		ColladaPhong colladaPhong = new ColladaPhong();

		return new ColladaEffect(name + "-effect", new ColladaProfileCommon(new ColladaEffectTechnique("common", colladaPhong)));
	}

	public void loadColladaEffect(ColladaEffect colladaEffect) {
		// TODO: Implement this
		shaderPath = "shader/uber.vsh";
		unknownS3 = ".";
		unknownS4 = "";
		opacityTexPath = "";
		unknownS6 = "";
		diffuseLightmap = "sc3d/diffuse_lightmap.png";
		specularLightmap = "sc3d/specular_lightmap.png";
		unknownB1 = 4;
		ambientColor = 0xFF000000;

		// diffuseColor = 0xFF00FF00;
		// specularColor = 0xFF000000;
		diffuseTexPath = ".";
		specularTexPath = ".";

		colorizeColor = 0xFFFFFFFF;
		emissionColor = 0xFF000000;
		unknownF1 = 1.0f;
		shaderDefineFlags = 3014; // 1 is for AMBIENT, 2 is for DIFFUSE, 4 is for STENCIL, etc.
	}
}
