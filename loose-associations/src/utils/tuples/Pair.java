package utils.tuples;

import java.util.Arrays;
import java.util.Iterator;

/**
 * The Class Pair.
 *
 * @param <F> the generic type
 * @param <S> the generic type
 */
public class Pair<F, S> implements Iterable<Object> {
	
	/** The first. */
	protected final F first;
	
	/** The second. */
	protected final S second;

	/**
	 * Instantiates a new pair.
	 *
	 * @param first the first
	 * @param second the second
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public Pair(F first, S second) throws IllegalArgumentException {
		if (first == null || second == null)
			throw new IllegalArgumentException("Arguments can't be null");
		this.first = first;
		this.second = second;
	}
	
	/**
	 * Of.
	 *
	 * @param <F> the generic type
	 * @param <S> the generic type
	 * @param first the first
	 * @param second the second
	 * @return the pair
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public final static <F, S> Pair<F, S> of(F first, S second) throws IllegalArgumentException {
		return new Pair<F, S>(first, second);
	}
	
	/**
	 * Gets the first.
	 *
	 * @return the first
	 */
	public final F getFirst() {
		return first;
	}
	
	/**
	 * Gets the second.
	 *
	 * @return the second
	 */
	public final S getSecond() {
		return second;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Pair [first=" + first + ", second=" + second + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + first.hashCode();
		result = prime * result + second.hashCode();
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		
		if (other == null)
			return false;
		
		if (!(other instanceof Pair<?, ?>))
			return false;
		
		return (((Pair<?, ?>) other).first.equals(first)) &&
			   (((Pair<?, ?>) other).second.equals(second));	
	}

	@Override
	public Iterator<Object> iterator() {
		return Arrays.asList(first, second).iterator();
	}
	
}
