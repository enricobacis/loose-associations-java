package core;

import java.util.List;
import java.util.Set;

import utils.tuples.Triplet;
import core.associations.BaseAssociations;
import core.fragments.BaseFragments;
import core.tables.BaseTable;

/**
 * Interface that components which want to be a Loose associator need to implement.
 * This is the minimal API offered to the component client. The component needs to
 * take a table, a list of constraints and a list of fragments and create a loose
 * association when associate is called.
 */
public interface LooseSolver {
	
	/**
	 * Initialize the problem from table, constraints and fragments.
	 * The list of constraints and fragments are generic because we can mix
	 * attribute names and indices, for example if the attributes in a table
	 * are ["a", "b", "c"], the constraint (or fragment) ["a", "b"] can be
	 * expressed as ["a", "b"] or [0, 1] or even [0, "b"] and ["a", 1]
	 * 
	 * Fragments and constraints are list of lists so for example [[0, 1], [1, 2], ...]
	 *
	 * @param <C> the generic type of constraints
	 * @param <F> the generic type of fragments
	 * @param table the table (source of data)
	 * @param constraints the list of constraints
	 * @param fragments the list of fragments
	 */
	public abstract <C, F> void initializeProblem(BaseTable table, List<List<C>> constraints, List<List<F>> fragments);

	/**
	 * Create the loose association using the group sizes k described in the kList
	 * for each fragment. a kList of [2, 3] imposes k=2 in the first fragment and
	 * k= 3 in the second fragment.
	 *
	 * @param kList the list of k (privacy factor or groups size) for each fragment
	 * @return a triplet containing the associations, the fragments and a set containing the index of dropped tuples
	 * @throws Exception if we encounter any exception, be sure to catch it somewhere.
	 */
	public abstract Triplet<BaseAssociations, BaseFragments, Set<Integer>> associate(List<Integer> kList) throws Exception;

}