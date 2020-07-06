package daniillnull.collada;

import java.util.Collection;
import java.util.HashMap;

public class ColladaIndex {
	private final HashMap<String, Object> map;

	public ColladaIndex() {
		map = new HashMap<>();
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String id) {
		if (!map.containsKey(id)) {
			throw new RuntimeException("ColladaIndex.get: object \"" + id + "\" not found");
		}
		return (T) map.get(id);
	}

	public void register(String id, Object object) {
		if (map.containsKey(id)) {
			throw new RuntimeException("Object \"" + id + "\" is registered already");
		}
		map.put(id, object);
	}

	public Collection<Object> values() {
		return map.values();
	}
}
