package utils.tuples;

/**
 * The Class ComparablePair.
 *
 * @param <F> the generic type
 * @param <S> the generic type
 */
public class ComparablePair<F extends Comparable<? super F>, S extends Comparable<? super S>>
extends Pair<F, S> implements Comparable<ComparablePair<F, S>> {

	/**
	 * Instantiates a new comparable pair.
	 *
	 * @param first the first
	 * @param second the second
	 */
	public ComparablePair(F first, S second) {
		super(first, second);
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ComparablePair<F, S> other) {
		int compareFirst = first.compareTo(other.first);
		if (compareFirst != 0)
			return compareFirst;
		return second.compareTo(other.second);
	}

}
