package daniillnull.scw.sections;

import daniillnull.scw.ScwSection;
import daniillnull.scw.util.ScwInputStream;
import daniillnull.scw.util.ScwOutputStream;

import java.io.IOException;

public class ScwHeader extends ScwSection {
	public int version, frameRate, unknown2, animationFrameEnd;
	public String materialsFilename;
	public boolean unknownB;

	@Override
	public void load(ScwInputStream is) throws IOException {
		version = is.readShort();
		frameRate = is.readShort();
		unknown2 = is.readShort();
		animationFrameEnd = is.readShort();
		materialsFilename = is.readUTF();
		if (is.hasAvailable()) {
			unknownB = is.readBoolean();
		}
	}

	@Override
	public void save(ScwOutputStream os) throws IOException {
		os.writeShort(version);
		os.writeShort(frameRate);
		os.writeShort(unknown2);
		os.writeShort(animationFrameEnd);
		os.writeUTF(materialsFilename);
		os.writeBoolean(unknownB);
	}

	@Override
	public String getSectionName() {
		return "HEAD";
	}
}
