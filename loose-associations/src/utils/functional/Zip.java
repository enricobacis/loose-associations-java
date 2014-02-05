package utils.functional;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * The Class Zip joins more lists toghether in this way:
 * (1, 2, 3) zip (4, 5, 6) -> ((1, 4), (2, 5), (3, 6)) 
 *
 * @param <O> the generic type
 */
public class Zip<O> extends Generator<O, List<O>> {
	
	/** The iterators. */
	protected List<Iterator<O>> iterators = new LinkedList<Iterator<O>>();
	
	/**
	 * Instantiates a new zip.
	 *
	 * @param input the input
	 * @param iterables the iterables
	 */
	@SafeVarargs
	public Zip(Iterator<O> input, Iterable<O> ... iterables) {
		if (input == null) 
			iterators.add(input);
		for (Iterable<O> iterable: iterables)
			iterators.add(iterable.iterator());
	}
	
	/**
	 * Instantiates a new zip.
	 *
	 * @param iterables the iterables
	 */
	@SafeVarargs
	public Zip(Iterable<O> ... iterables) {
		this(null, iterables);
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
		Generator<O, List<O>> zipped = new Zip<O>(iterables);
		return Generable.from(zipped);
	}
	
	/**
	 * With.
	 *
	 * @param <O> the generic type
	 * @param input the input
	 * @param iterables the iterables
	 * @return the zip
	 */
	@SafeVarargs
	static <O> Zip<O> with(Iterator<O> input, Iterable<O> ... iterables) {
		return new Zip<O>(input, iterables);
	}
	
	/**
	 * With longest.
	 *
	 * @param <O> the generic type
	 * @param input the input
	 * @param iterables the iterables
	 * @return the zip
	 */
	@SafeVarargs
	static <O> Zip<O> withLongest(Iterator<O> input, Iterable<O> ... iterables) {
		return new ZipLongest<O>(input, iterables);
	}
	
	/* (non-Javadoc)
	 * @see functional.Generator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		for (Iterator<O> iterator: iterators)
			if (!iterator.hasNext())
				return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see functional.Generator#next()
	 */
	@Override
	public List<O> next() {
		List<O> list = new LinkedList<O>();
		for (Iterator<O> iterator: iterators)
			list.add(iterator.next());
		return list;
	}

}
