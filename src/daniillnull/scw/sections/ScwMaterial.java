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
	public String name, shaderPath, unknownS3, unknownS4, unknownS5, diffuseLightmap, specularLightmap, diffuseTexPath, unkTexPath, unknownPS3, unknownPS4;
	public int unknownB1, unknownB2;
	public int unknownI1, unknownI2, unknownI3, diffuseColor, unkColor, unknownPI3, unknownPI4;
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
		unknownI1 = is.readInt();
		if (is.readBoolean()) {
			diffuseTexPath = is.readUTF();
		} else {
			diffuseColor = is.readInt();
		}
		if (is.readBoolean()) {
			unkTexPath = is.readUTF();
		} else {
			unkColor = is.readInt();
		}
		unknownS3 = is.readUTF();
		unknownS4 = is.readUTF();
		if (is.readBoolean()) {
			unknownPS3 = is.readUTF();
		} else {
			unknownPI3 = is.readInt();
		}
		if (is.readBoolean()) {
			unknownPS4 = is.readUTF();
		} else {
			unknownPI4 = is.readInt();
		}
		unknownS5 = is.readUTF();
		unknownF1 = is.readFloat();
		unknownF2 = is.readFloat();
		diffuseLightmap = is.readUTF();
		specularLightmap = is.readUTF();
		unknownI2 = is.readInt();
		unknownI3 = is.readUnsignedShort();
	}

	@Override
	public void save(ScwOutputStream os) throws IOException {
		os.writeUTF(name);
		os.writeUTF(shaderPath);
		os.write(unknownB1);
		os.write(unknownB2);
		os.writeInt(unknownI1);
		if (diffuseTexPath != null) {
			os.writeBoolean(true);
			os.writeUTF(diffuseTexPath);
		} else {
			os.writeBoolean(false);
			os.writeInt(diffuseColor);
		}
		if (unkTexPath != null) {
			os.writeBoolean(true);
			os.writeUTF(unkTexPath);
		} else {
			os.writeBoolean(false);
			os.writeInt(unkColor);
		}
		os.writeUTF(unknownS3);
		os.writeUTF(unknownS4);
		if (unknownPS3 != null) {
			os.writeBoolean(true);
			os.writeUTF(unknownPS3);
		} else {
			os.writeBoolean(false);
			os.writeInt(unknownPI3);
		}
		if (unknownPS4 != null) {
			os.writeBoolean(true);
			os.writeUTF(unknownPS4);
		} else {
			os.writeBoolean(false);
			os.writeInt(unknownPI4);
		}
		os.writeUTF(unknownS5);
		os.writeFloat(unknownF1);
		os.writeFloat(unknownF2);
		os.writeUTF(diffuseLightmap);
		os.writeUTF(specularLightmap);
		os.writeInt(unknownI2);
		os.writeShort(unknownI3);
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
				",\nunknownS5='" + unknownS5 + '\'' +
				",\ndiffuseLightmap='" + diffuseLightmap + '\'' +
				",\nspecularLightmap='" + specularLightmap + '\'' +
				",\ndiffuseTexPath='" + diffuseTexPath + '\'' +
				",\nunkTexPath='" + unkTexPath + '\'' +
				",\nunknownPS3='" + unknownPS3 + '\'' +
				",\nunknownPS4='" + unknownPS4 + '\'' +
				",\nunknownB1=" + unknownB1 +
				",\nunknownB2=" + unknownB2 +
				",\nunknownI1=" + unknownI1 +
				",\nunknownI2=" + unknownI2 +
				",\nunknownI3=" + unknownI3 +
				",\ndiffuseColor=" + diffuseColor +
				",\nunkColor=" + unkColor +
				",\nunknownPI3=" + unknownPI3 +
				",\nunknownPI4=" + unknownPI4 +
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
		unknownS5 = "";
		diffuseLightmap = "sc3d/diffuse_lightmap.png";
		specularLightmap = "sc3d/specular_lightmap.png";
		unknownB1 = 4;
		unknownI1 = 0xFF000000;

		// diffuseColor = 0xFF00FF00;
		// unkColor = 0xFF000000;
		diffuseTexPath = ".";
		unkTexPath = ".";

		unknownPI3 = 0xFFFFFFFF;
		unknownPI4 = 0xFF000000;
		unknownF1 = 1.0f;
		unknownI3 = 3014;
	}
}
