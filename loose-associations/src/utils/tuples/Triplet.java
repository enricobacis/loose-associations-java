package utils.tuples;

import java.util.Arrays;
import java.util.Iterator;

/**
 * The Class Triplet.
 *
 * @param <F> the generic type
 * @param <S> the generic type
 * @param <T> the generic type
 */
public class Triplet<F, S, T> extends Pair<F, S> {

	/** The third. */
	protected final T third;
	
	/**
	 * Instantiates a new triplet.
	 *
	 * @param first the first
	 * @param second the second
	 * @param third the third
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public Triplet(F first, S second, T third) throws IllegalArgumentException {
		super(first, second);
		
		if (third == null)
			throw new IllegalArgumentException("Arguments can't be null");
		
		this.third = third;
	}
	
	/**
	 * Returns a new Triplet. 
	 *
	 * @param <F> the generic type
	 * @param <S> the generic type
	 * @param <T> the generic type
	 * @param first the first
	 * @param second the second
	 * @param third the third
	 * @return the triplet
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public final static <F, S, T> Triplet<F, S, T> of(F first, S second, T third) throws IllegalArgumentException {
		return new Triplet<F, S, T>(first, second, third);
	}
	
	/**
	 * Gets the third.
	 *
	 * @return the third
	 */
	public final T getThird() {
		return third;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Triplet [first=" + first + ", second=" + second + ", third=" + third + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + third.hashCode();
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
		
		if (!(other instanceof Triplet<?, ?, ?>))
			return false;
		
		return super.equals(other) &&
				(((Triplet<?, ?, ?>) other).third.equals(third));	
	}
	
	@Override
	public Iterator<Object> iterator() { 
		return Arrays.asList(first, second, third).iterator();
	}
	
}
