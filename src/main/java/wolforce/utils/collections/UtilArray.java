package wolforce.utils.collections;

public class UtilArray {

	@SafeVarargs
	public static <T> T[] array(T... items) {
		return items;
	}

	public static <T> T getRandomFromArray(T[] arr) {
		return arr[(int) (Math.random() * arr.length)];
	}

}
