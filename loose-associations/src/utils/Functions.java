package utils;

import java.util.List;
import java.util.ListIterator;

import utils.functional.Generable;
import utils.functional.Generator;

/**
 * Container class for static utility functions.
 */
public abstract class Functions {

	/**
	 * Computes the average of a list of numbers.
	 *
	 * @param <T> the generic type of numbers (T extends Numbers)
	 * @param list the list of numbers
	 * @return the average as double
	 */
	public static <T extends Number> double average(List<T> list) {
		double result = 0.0;
		if (list != null) {
			ListIterator<T> iter = list.listIterator();
			while (iter.hasNext()) {
				int n = iter.nextIndex() + 1;
				T value = iter.next();
				result += (1.0 / n) * (value.doubleValue() - result);
			}
		}
		return result;
	}
	
	/**
	 * Returns a generable of neighbour numbers from n in the range [min, max].
	 * The closest neighbours are returned first, starting from n-1.
	 * For example the neighbours of n=3 in the range [min=0, max=10] are:
	 * [2, 4, 1, 5, 0, 6, 7, 8, 9, 10]
	 * They are returned in a zig-zag order. It's safe to call this method even
	 * with big ranges because they are computed lazily and not stored as a list.
	 * If you want to store them for later access use a LazyList.
	 * 
	 * If n is not in the range it throws an IllegalArgumentException
	 *
	 * @param n the starting number (not returned by the function)
	 * @param min the minimum (included) number to return
	 * @param max the maximum (included) number to return
	 * @return the generable of neighbour numbers
	 * @throws IllegalArgumentException if n is not in the range [min, max]
	 */
	public static Generable<Integer> neighbors(final int n, final int min, final int max) throws IllegalArgumentException {
		
		if (n < min || n > max)
			throw new IllegalArgumentException("n must be min <= n <= max");
		
		Generator<Integer, Integer> generator = new Generator<Integer, Integer>() {
			
			// the next number when moving up
			int up = n + 1;
			
			// the next number when moving down
			int down = n - 1;
			
			// the first direction will be DOWN (switched before checking)
			// 0 for moving down, 1 for moving up
			int direction = 1;

			public boolean hasNext() { return hasUp() || hasDown(); }
			
			private boolean hasUp() { return up <= max; }
			
			private boolean hasDown() { return down >= min; }

			public Integer next() {
				// switch direction
				direction = (direction + 1) % 2;
				
				if (direction == 0)
					return hasDown() ? down-- : up++;
				else
					return hasUp() ? up++ : down--;
			}

			public void remove() { }
			
		};
		
		return Generable.from(generator);
	}

}
