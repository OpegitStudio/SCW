package daniillnull.util;

public class Assert {
	public static void doAssert(boolean condition, String string) {
		if (condition) {
			throw new RuntimeException(string);
		}
	}
}
