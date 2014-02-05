package core.constraints;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import core.tables.Row;

/**
 * The Class BaseConstraints keeps track of constraints
 * and fragments in order to check if two rows are
 * considered alike in a fragment (either for a single
 * constraint or for all the ones insisting on the fragment).
 */
public abstract class BaseConstraints {
	
	/** The involved fragments cache for memoization. */
	private Map<Integer, Set<Integer>> involvedFragmentsFor_cache = new HashMap<Integer, Set<Integer>>();
	
	/** The constraints cache for memoization. */
	private Map<Integer, Set<Integer>> constraintsFor_cache = new HashMap<Integer, Set<Integer>>();
	
	/**
	 * Internal function called by memoizer and returns the
	 * involved fragments for a constraint.
	 *
	 * @param constraintId the constraint id
	 * @return the sets of fragments involved in a constraint
	 */
	protected abstract Set<Integer> _involvedFragmentsFor(int constraintId);
	
	/**
	 * Internal function called by the memoizer and returns the
	 * constraints insisting on a fragment.
	 *
	 * @param fragmentId the fragment id
	 * @return the sets of constraints insisting on a fragment
	 */
	protected abstract Set<Integer> _constraintsFor(int fragmentId);
	
	/**
	 * Check if two rows are alike wrt to a fragment for a specific constraint.
	 *
	 * @param row1 the row1
	 * @param row2 the row2
	 * @param fragmentId the fragment id
	 * @param constraintId the constraint id
	 * @return true, if the rows are alike
	 */
	public abstract boolean areRowsAlikeFor(Row row1, Row row2, int fragmentId, int constraintId);

	/**
	 * Check if two rows are alike wrt to a fragment for at least
	 * one constraint.
	 *
	 * @param row1 the row1
	 * @param row2 the row2
	 * @param fragmentId the fragment id
	 * @return true, if the rows are alike
	 */
	public abstract boolean areRowsAlike(Row row1, Row row2, int fragmentId);
	
	/**
	 * Returns the involved fragments for a constraint using a memoization
	 * pattern (dynamic programming) using a cache.
	 *
	 * @param constraintId the constraint id
	 * @return the sets of fragments involved for a constraints
	 */
	public Set<Integer> involvedFragmentsFor(int constraintId) {
		Set<Integer> result = involvedFragmentsFor_cache.get(constraintId);
		if (result == null) {
			result = _involvedFragmentsFor(constraintId);
			result = Collections.unmodifiableSet(result);
			involvedFragmentsFor_cache.put(constraintId, result);
		}
		return result;
	}
	
	/**
	 * Returns the constraints for a fragment using a memoization pattern
	 * (dynamic programming) using a cache.
	 *
	 * @param fragmentId the fragment id
	 * @return the sets of constraints insisting on a fragment
	 */
	public Set<Integer> constraintsFor(int fragmentId) {
		Set<Integer> result = constraintsFor_cache.get(fragmentId);
		if (result == null) {
			result = _constraintsFor(fragmentId);
			result = Collections.unmodifiableSet(result);
			constraintsFor_cache.put(fragmentId, result);
		}
		return result;
	}
	
}
