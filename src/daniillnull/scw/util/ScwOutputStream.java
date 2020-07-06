package daniillnull.scw.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ScwOutputStream extends DataOutputStream {
	private final ScwConfig config;

	public ScwOutputStream(OutputStream out) {
		super(out);
		config = new ScwConfig();
	}

	public void writeSectionName(String sectionName) throws IOException {
		byte[] data = sectionName.getBytes();
		if (data.length != 4) {
			throw new IllegalArgumentException("sectionName should have length = 4");
		}
		write(data);
	}

	public void writeCustomType(int v, int size) throws IOException {
		for (int i = (size - 1); i >= 0; i--) {
			write(v >>> (i * 8));
		}
	}

	public void writeFloat16(float v) throws IOException {
		writeShort((short) (v * 32512));
	}

	public void writePositiveFloat16(float v) throws IOException {
		writeShort((short) (v * 0xFFFF));
	}

	public ScwConfig getConfig() {
		return config;
	}

	public void updateConfig(ScwConfig source) {
		config.version = source.version;
		config.frameRate = source.frameRate;
	}
}
