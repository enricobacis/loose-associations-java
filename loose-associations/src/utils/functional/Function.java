package utils.functional;

/**
 * The Class Function.
 *
 * @param <I> the generic type
 * @param <O> the generic type
 */
public abstract class Function<I, O> {

	/**
	 * Apply the function to an input.
	 *
	 * @param arg0 the arg0
	 * @return the o
	 */
	public abstract O apply(I arg0);
	
}
