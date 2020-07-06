package daniillnull.scw;

import daniillnull.scw.util.ScwInputStream;
import daniillnull.scw.util.ScwOutputStream;

import java.io.IOException;

public interface ScwSerializableEntity {
	void load(ScwInputStream is) throws IOException;

	default void save(ScwOutputStream os) throws IOException {
		throw new RuntimeException("not implemented");
	}
}
