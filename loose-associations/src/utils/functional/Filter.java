package utils.functional;

import java.util.Iterator;

/**
 * The Class Filter.
 *
 * @param <I> the generic type
 */
public class Filter<I> extends Generator<I, I> {
	
	/** The next. */
	private I next;
	
	/** The input. */
	private Iterator<I> input;
	
	/** The function. */
	private Function<I, Boolean> function;

	/**
	 * Instantiates a new filter.
	 *
	 * @param input the input
	 * @param function the function
	 */
	Filter(Iterator<I> input, Function<I, Boolean> function) {
		this.input = input;
		this.function = function;
	}
	
	/**
	 * With.
	 *
	 * @param <I> the generic type
	 * @param input the input
	 * @param function the function
	 * @return the filter
	 */
	static <I> Filter<I> with(Iterator<I> input, Function<I, Boolean> function) {
		return new Filter<I>(input, function);
	}
	
	/* (non-Javadoc)
	 * @see utils.functional.Generator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return ((next != null) || hasNextValid());
	}
	
	/**
	 * Checks for next valid.
	 *
	 * @return true, if successful
	 */
	private boolean hasNextValid() {
		while (input.hasNext()) {
			next = input.next();
			if (function.apply(next))
				return true;
		}
		next = null;
		return false;
	}
	
	/* (non-Javadoc)
	 * @see utils.functional.Generator#next()
	 */
	@Override
	public I next() {
		I current = next;
		next = null;
		return current;
	}
	
}
