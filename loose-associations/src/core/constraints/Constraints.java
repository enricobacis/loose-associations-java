package core.constraints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import core.tables.Row;

/**
 * The Class Constraints is an implementation of the BaseConstraints.
 */
public class Constraints extends BaseConstraints {
	
	/** The fragments. */
	private List<Set<Integer>> fragments = new ArrayList<Set<Integer>>();
	
	/** The fragment for. */
	private Map<Integer, Integer> fragmentFor = new HashMap<Integer, Integer>();
	
	/** The constraints. */
	private List<Set<Integer>> constraints = new ArrayList<Set<Integer>>();
	
	/** The intersect_cache for the memoization. */
	private Map<List<Integer>, Set<Integer>> intersect_cache = new HashMap<List<Integer>, Set<Integer>>();

	/**
	 * Instantiates a new constraints.
	 *
	 * @param constraints the constraints list
	 * @param fragments the fragments list
	 */
	public Constraints(List<List<Integer>> constraints, List<List<Integer>> fragments) {
		// Initialize fragments
		Set<Integer> allAttributes = new HashSet<Integer>();
		for (int i = 0; i < fragments.size(); ++i) {
			Collection<Integer> fragment = fragments.get(i);
			this.fragments.add(new HashSet<Integer>(fragment));
			allAttributes.addAll(fragment);
			for (int attribute: fragment)
				this.fragmentFor.put(attribute, i);
		}
		
		// initialize constraints
		for (Collection<Integer> constraint: constraints) {
			Set<Integer> constraintSet = new HashSet<Integer>(constraint);
			// drop not relevant constraints 
			if (allAttributes.containsAll(constraintSet))
				this.constraints.add(Collections.unmodifiableSet(constraintSet));
		}
		
		// make collections unmodifiable
		this.fragments = Collections.unmodifiableList(this.fragments);
		this.fragmentFor = Collections.unmodifiableMap(this.fragmentFor);
		this.constraints = Collections.unmodifiableList(this.constraints);
	}
	
	/* (non-Javadoc)
	 * @see core.constraints.BaseConstraints#_involvedFragmentsFor(int)
	 */
	protected Set<Integer> _involvedFragmentsFor(int constraintId) {
		Set<Integer> result = new HashSet<Integer>();
		for (int attribute: this.constraints.get(constraintId))
			result.add(this.fragmentFor.get(attribute));
		return result;
	}
	
	/* (non-Javadoc)
	 * @see core.constraints.BaseConstraints#_constraintsFor(int)
	 */
	protected Set<Integer> _constraintsFor(int fragmentId) {
		Set<Integer> result = new HashSet<Integer>();
		for (int constraintId = 0; constraintId < this.constraints.size(); ++constraintId)
			if (involvedFragmentsFor(constraintId).contains(fragmentId))
				result.add(constraintId);
		return result;
	}
	
	/**
	 * Gets the insersection of the attributes in fragmentId and
	 * constraintId using a memoization pattern.
	 *
	 * @param fragmentId the fragment id
	 * @param constraintId the constraint id
	 * @return the sets of attributes that are in the fragment and also in the constraint
	 */
	private Set<Integer> intersect(int fragmentId, int constraintId) {
		List<Integer> args = Arrays.asList(fragmentId, constraintId);
		Set<Integer> result = intersect_cache.get(args);
		if (result == null) {
			result = new HashSet<Integer>(fragments.get(fragmentId));
			result.retainAll(constraints.get(constraintId));
			result = Collections.unmodifiableSet(result);
			intersect_cache.put(args, result);
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see core.constraints.BaseConstraints#areRowsAlikeFor(int, int, int, int)
	 */
	@Override
	public boolean areRowsAlikeFor(Row row1, Row row2, int fragmentId, int constraintId) {
		for (int attribute: intersect(fragmentId, constraintId))
			if (!row1.data().get(attribute).equals(row2.data().get(attribute)))
				return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see core.constraints.BaseConstraints#areRowsAlike(int, int, int)
	 */
	@Override
	public boolean areRowsAlike(Row row1, Row row2, int fragmentId) {
		for (int constraintId: constraintsFor(fragmentId))
			if (areRowsAlikeFor(row1, row2, fragmentId, constraintId))
				return true;
		return false;
	}

}
