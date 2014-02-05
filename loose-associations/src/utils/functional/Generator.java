package utils.functional;

import java.util.Iterator;

/**
 * The Class Generator implements Iterator but its elements are
 * computed only when requested and not stored.
 *
 * @param <I> the generic type
 * @param <O> the generic type
 */
public class Generator<I, O> implements Iterator<O> {
	
	/** The input. */
	private Iterator<I> input;
	
	/** The function. */
	private Function<I, O> function;
	
	/**
	 * Instantiates a new generator.
	 */
	protected Generator() { }
	
	/**
	 * Instantiates a new generator.
	 *
	 * @param input the input
	 * @param function the function
	 */
	public Generator(final Iterator<I> input, final Function<I, O> function) {
		this.input = input;
		this.function = function;
	}
	
	/**
	 * Creates a new Generator from an Iterator input and a function.
	 *
	 * @param <I> the generic type
	 * @param <O> the generic type
	 * @param input the input
	 * @param function the function
	 * @return the generator
	 */
	public static <I, O> Generator<I, O> from(final Iterator<I> input, final Function<I, O> function) {
		return new Generator<I, O>(input, function);
	}
	
	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return input.hasNext();
	}
	
	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	@Override
	public O next() {
		return function.apply(input.next());
	}
	
	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() { }
	
}
