package daniillnull.collada;

public class TypeUtils {
	public static String ints2string(int[] ints) {
		StringBuilder sb = new StringBuilder();
		sb.append(ints[0]);
		for (int i = 1; i < ints.length; i++) {
			sb.append(' ');
			sb.append(ints[i]);
		}
		return sb.toString();
	}

	public static String strings2string(String[] strings) {
		StringBuilder sb = new StringBuilder();
		sb.append(strings[0]);
		for (int i = 1; i < strings.length; i++) {
			sb.append(' ');
			sb.append(strings[i]);
		}
		return sb.toString();
	}

	public static String floats2string(float[] floats) {
		StringBuilder sb = new StringBuilder();
		sb.append(floats[0]);
		for (int i = 1; i < floats.length; i++) {
			sb.append(' ');
			sb.append(floats[i]);
		}
		return sb.toString();
	}

	public static float[] string2floats(String str) {
		String[] sub = str.split(" +");
		float[] floats = new float[sub.length];
		for (int i = 0; i < sub.length; i++) {
			floats[i] = Float.parseFloat(sub[i]);
		}
		return floats;
	}

	public static String[] string2strings(String str) {
		return str.split(" +");
	}

	public static int[] string2ints(String str) {
		String[] sub = str.split(" +");
		int[] ints = new int[sub.length];
		for (int i = 0; i < sub.length; i++) {
			ints[i] = Integer.parseInt(sub[i]);
		}
		return ints;
	}
}
