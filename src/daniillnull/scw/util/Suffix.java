package daniillnull.scw.util;

public class Suffix {
	public static final String MATERIAL = "material";
	public static final String CAMERA = "camera";
	public static final String ANIMATION = "anim";
	public static final String GEOMETRY_MESH = "mesh";
	public static final String CONTROLLER_SKIN = "skin";

	public static String create(String base, String suffix) {
		String pd = "-" + suffix;
		if (base.endsWith(pd)) {
			return base;
		}
		return base + pd;
	}

	public static String remove(String base, String suffix) {
		String pd = "-" + suffix;
		if (base.endsWith(suffix)) {
			return base.substring(0, base.length() - pd.length());
		}
		return base;
	}
}
