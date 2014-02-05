package utils.functional;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * The Class ZipLongest does the same as zip but also
 * for non-equally-sized iterables. It returns lists
 * with null where the list is finished. So
 * (1, 2, 3) ziplongest (4, 5) -> ((1, 4), (2, 5), (3, null))
 *
 * @param <O> the generic type
 */
public class ZipLongest<O> extends Zip<O> {
	
	/**
	 * Instantiates a new zip longest.
	 *
	 * @param iterables the iterables
	 */
	@SafeVarargs
	public ZipLongest(Iterable<O> ... iterables) {
		super(iterables);
	}

	/**
	 * Instantiates a new zip longest.
	 *
	 * @param input the input
	 * @param iterables the iterables
	 */
	@SafeVarargs
	public ZipLongest(Iterator<O> input, Iterable<O> ... iterables) {
		super(input, iterables);
	}
	
	/**
	 * Of.
	 *
	 * @param <O> the generic type
	 * @param iterables the iterables
	 * @return the generable
	 */
	@SafeVarargs
	public static <O> Generable<List<O>> of(Iterable<O> ... iterables) {
		Generator<O, List<O>> zipped = new ZipLongest<O>(iterables);
		return Generable.from(zipped);
	}

	/* (non-Javadoc)
	 * @see functional.Zip#hasNext()
	 */
	@Override
	public boolean hasNext() {
		for (Iterator<O> iterator: iterators)
			if (iterator.hasNext())
				return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see functional.Zip#next()
	 */
	@Override
	public List<O> next() {
		List<O> list = new LinkedList<O>();
		for (Iterator<O> iterator: iterators)
			if (iterator.hasNext())
				list.add(iterator.next());
			else
				list.add(null);
		return list;
	}

}
