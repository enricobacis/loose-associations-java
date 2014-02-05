/*
 * 
 */
package core;

import static utils.Functions.neighbors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Set;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.Timer;
import utils.functional.Function;
import utils.functional.Generable;
import utils.functional.LazyList;
import utils.functional.Range;
import utils.stacks.LinkedStack;
import utils.stacks.Stack;
import utils.tuples.Triplet;
import core.associations.Associations;
import core.associations.BaseAssociations;
import core.constraints.BaseConstraints;
import core.constraints.Constraints;
import core.fragments.BaseFragments;
import core.fragments.Fragment;
import core.fragments.Fragments;
import core.tables.BaseTable;
import core.tables.Row;

/**
 * The Class implements the LooseInterface component with a greedy algorithm that
 * incrementally finds a viable association for each row using the branch and bound
 * method with tree pruning. It tries to find a place looking in the empty groups
 * and checking for each selected group that the group heterogenity condition, the
 * association heterogenity and the deep heterogenity are not broken.
 * 
 * It tries to check for every possible collision in advance, becaue efficently pruning
 * the tree of solutions is a key idea in an NP problem.
 * 
 * When this first scan is completed, the algorithm starts a second scan to redistribute
 * the non-full groups over other full groups without breaking the heterogenity conditions
 * stated before.
 */
public class GreedyLoose extends Observable implements LooseSolver {
	
	/** The notify observers every n rows. */
	private final int notifyObserversEveryNRows = 1000;
	
	/** The notify observers every n operations. */
	private final int notifyObserversEveryNOperations = 10;
	
	/** The logger. */
	private final Logger logger;

	/** The source table. */
	private BaseTable table;
	
	/** The number of tuples in the table. */
	private int tuples;
	
	/** The instance of a BaseConstraints with current constraints. */
	private BaseConstraints constraints;
	
	/** The list of fragments (list of indices). */
	private BaseFragments fragments;
	
	/** The instance of a BaseAssociations to keep the associations. */
	private BaseAssociations associations;
	
	/** The set of dropped tuples. */
	private Set<Integer> dropped;
	
	/** The operation stack used in the second scan to linearize the operations. */
	private Stack<Operation> opstack;
	
	/** The elapsed time to make the association. */
	private String elapsedTime = "";

	/** Tells if the association has to stop. */
	private boolean isStopped;
	
	/**
	 * Instantiates a new problem from table, constraints and fragments.
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
	 * @param logger the logger
	 */
	public <C, F> GreedyLoose(BaseTable table, List<List<C>> constraints, List<List<F>> fragments, Logger logger) {
		// Fetch log4j configuration
		PropertyConfigurator.configure("log4j.properties");
		this.logger = logger;
		
		// initialize the problem
		initializeProblem(table, constraints, fragments);
	}
	
	/**
	 * Instantiates a new problem from table, constraints and fragments.
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
	public <C, F> GreedyLoose(BaseTable table, List<List<C>> constraints, List<List<F>> fragments) {
		this(table, constraints, fragments, LoggerFactory.getLogger(GreedyLoose.class));
	}
	
	/* (non-Javadoc)
	 * @see core.LooseInterface#initializeProblem(core.tables.BaseTable, java.util.List, java.util.List)
	 */
	@Override
	public <C, F> void initializeProblem(BaseTable table, List<List<C>> constraints, List<List<F>> fragments) {
		
		this.table = table;
		this.tuples = table.size();
		
		// convert each fragment to a list of indices
		ArrayList<List<Integer>> frags = new ArrayList<List<Integer>>();
		for (List<F> fragment: fragments)
			frags.add(table.toIndices(fragment));
		
		// creates the fragments object using the list of fragments
		this.fragments = new Fragments(frags);
		
		// convert each fragment to a list of indices
		List<List<Integer>> cons = new ArrayList<List<Integer>>();
		for (List<C> constraint: constraints)
			cons.add(table.toIndices(constraint));
		
		// creates the constraints object using the list of constraints
		this.constraints = new Constraints(cons, frags);
	}
	
	/**
	 * Gets the constraints.
	 *
	 * @return the constraints
	 */
	public BaseConstraints getConstraints() {
		return constraints;
	}
	
	/**
	 * Returns the row data for the rows included int the group groupId in
	 * the fragment fragmentId.
	 *
	 * @param fragmentId the fragment id
	 * @param groupId the group id
	 * @return the rows in the group
	 */
	private List<Row> getGroupData(int fragmentId, int groupId) {
		List<Row> rows = new LinkedList<Row>();
		for (int rowId: associations.getGroup(fragmentId, groupId))
			rows.add(table.get(rowId));
		return rows;
	}
	
	/**
	 * Lazily returns the non full groups in a fragment (it starts looking from the
	 * firstNonfull and keeps on looking for non-full groups).
	 * It uses a range to lazily get the viable groups and then it filters the range
	 * using the isGroupFull method from the class BaseAssociations.
	 *
	 * @param fragmentId the fragment id for which we want to search nonfullgroups
	 * @return the generable of non full groups
	 */
	private Generable<Integer> getNonFullGroups(final int fragmentId) {
		// we create the range starting from the first nonnull group in the fragment to the last usable
		Fragment fragment = fragments.get(fragmentId);
		Range groups = Range.to(fragment.getFirstNonfull(), fragment.getLastUsable());
		// function do filter only nonfull groups in this fragment
		Function<Integer, Boolean> nonFull = new Function<Integer, Boolean>() {
			public Boolean apply(Integer groupId) {
				return !associations.isGroupFull(fragmentId, groupId);
			}
		};
		// we return a generable of nonfull groups in the fragment
		return groups.filter(nonFull);
	}
	
	/**
	 * Lazily checks if two groups in the same fragment contain alike tuples for a constraint.
	 * It uses the row data generated from the lazy getGroupData and produces two LazyLists.
	 * If the currentRow is non null it will be inserted in the LazyList of currentRows (to simulate
	 * the presence of the row in the currentGroup without actually adding it so preventing the computational
	 * cost of the addition which would trigger the addition to all the indices).
	 * 
	 * The algorithm stops when it finds the first alike tuple in the two groups.
	 *
	 * @param currentGroup the current group to check
	 * @param otherGroup the other group to check
	 * @param fragmentId the fragment id
	 * @param constraintId the constraint id
	 * @param currentRow the current row that we would like to add in the currentGroup
	 * @return true, if the two groups contain no alike tuples (including the currentRow in the currentGroup if not null)
	 */
	private boolean areGroupsAlike(int currentGroup, int otherGroup, int fragmentId, int constraintId, Row currentRow) {
		LazyList<Row> currentRows = LazyList.from(getGroupData(fragmentId, currentGroup));
		LazyList<Row> otherRows = LazyList.from(getGroupData(fragmentId, otherGroup));
		
		// we have to add the currentRow to currentGroup because it will go there, so we have to check with it in place
		if (currentRow != null)
			currentRows.insert(currentRow);
		
		// for any row in the currentRows
		for (Row row1: currentRows)
			// for any row in the otherRows
			for (Row row2: otherRows)
				// if the two rows are alike for a constraint in that fragment, the groups are alike
				if (constraints.areRowsAlikeFor(row1, row2, fragmentId, constraintId))
					return true;
		
		// if no pair of rows are alike, the two groups aren't alike
		return false;
	}
	
	/**
	 * Check that the three heterogenity condition are satisfied for the row 
	 * by the association if we set groupId in the association for the fragment fragmentId.
	 *
	 * @param row the row we are associating
	 * @param association the association we have computed till now (or the previous association in case we are redistributing)
	 * @param fragmentId the fragment we want to change in the association
	 * @param groupId the group we want to set in the association for the fragment
	 * @return true, if the association with groupId as fragmentId doesn't violate any heterogenity property
	 */
	private boolean checkHeterogenity(Row row, List<Integer> association, int fragmentId, int groupId) {
		// we rely on short circuit evaluation of booleans so we jump out of the function if any condition doesn't hold
		return (checkGroupHeterogenity(row, fragmentId, groupId) &&
				checkAssociationHeterogenity(association, fragmentId, groupId) &&
				checkDeepHeterogenity(row, association, fragmentId, groupId));
	}
	
	/**
	 * Check if the group heterogenity holds if we insert the row in the group groupId in the
	 * fragment fragmentId. The idea is to check if there are other alike rows in the group
	 * selected for any of the constraints insisting on the fragment.
	 * If the row is alike to one other row for any constraint, it's not safe to put it in
	 * the group, hence false is returned.
	 * 
	 * Note that we don't need the association here since the it's a property of the values only
	 * (the row) and not a property of the graph.
	 *
	 * @param row the row we are associating
	 * @param fragmentId the fragment we want to change in the association
	 * @param groupId the group we want to set in the association for the fragment
	 * @return true, if the row can be put in the selected group for the selected fragment without violating the local group heterogenity
	 */
	private boolean checkGroupHeterogenity(Row row, int fragmentId, int groupId) {
		// for every other row in (fragmentId, groupId) [lazy]
		for (Row otherRow: getGroupData(fragmentId, groupId))
			// if the rows are alike for any constraint insisting on the fragmentId, the property is violated 
			if (constraints.areRowsAlike(row, otherRow, fragmentId)) {
				logger.trace("GROUP VIOLATED fragment {} group {}", fragmentId, groupId);
				return false;
			}
		
		// if no pair of rows are alike, group heterogenity holds
		return true;
	}
	
	/**
	 * Check if the association heterogenity holds for the growing association
	 * if we set groupId in the association for the fragment fragmentId.
	 * 
	 * The idea is that there can't be two associations sharing two groups for any two fragments.
	 * So, since we have already checked that the property holds for the association list computed till
	 * now, we just need to check that no other association exists with a link between
	 *   (fragmentId: groupId) <-> (otherFragment: otherGroup)
	 * where otherFragment and otherGroup are respectively the index and the value of every other node
	 * in the association list. Otherwise we can't create another association like that.
	 * 
	 * Note that we don't need to know the row data to enforce this property since it's a property
	 * based only on the graph.
	 * 
	 * If we think at graph theory this is the same as enforcing that we never have two links between
	 * two groups in any couple of fragments.
	 *
	 * @param association the association we have computed till now (or the previous association in case we are redistributing)
	 * @param fragmentId the fragment we want to change in the association
	 * @param groupId the group we want to set in the association for the fragment
	 * @return true, if the row can be put in the selected group for the selected fragment without violating the local association heterogenity
	 */
	private boolean checkAssociationHeterogenity(List<Integer> association, int fragmentId, int groupId) {
		// we use a listiterator so we can get the values and the index from the iterator 
		// without incurring in additional computational time if the list is a LinkedList
		ListIterator<Integer> iter = association.listIterator();
		while (iter.hasNext()) {
			int otherFragment = iter.nextIndex();
			int otherGroup = iter.next();
			// if we are redistributing, the association already has a group for fragmentId, but we want to change it to groupId
			// with this check we avoid to check an association that will be destroyed
			if (otherFragment != fragmentId)
				// if an association that links (fragmentId, groupId) <-> (otherFragment, otherGroup) exists we can't create another one
				if (associations.exists(fragmentId, groupId, otherFragment, otherGroup)) {
					logger.trace("ASSOCIATION VIOLATED fragment {} group {}", fragmentId, groupId);
					return false;
				}
		}
		
		// if no association has a link (fragmentId, groupId) <-> (otherFragment, otherGroup), association heterogenity holds
		return true;
	}
	
	/**
	 * Check if the deep heterogenity holds for the the association
	 * if we set groupId in the association for the fragment fragmentId.
	 * 
	 * To check this property in a greedy way we can split it in two different properties:
	 * 1) The association we are creating doesn't link any group that break a constraint
	 * 2) The row that we want to insert in (fragmentId, groupId) doesn't break any
	 *    already established association. In order to check this we have to check the
	 *    deep heterogenity condition of every group that is directly connected with
	 *    (fragmentId, groupId).
	 *
	 * @param row the row we are associating
	 * @param association the association we have computed till now (or the previous association in case we are redistributing)
	 * @param fragmentId the fragment we want to change in the association
	 * @param groupId the group we want to set in the association for the fragment
	 * @return true, if the row can be put in the selected group for the selected fragment without violating the local deep heterogenity
	 */
	private boolean checkDeepHeterogenity(Row row, List<Integer> association, int fragmentId, int groupId) {
		return checkDeepHeterogenityOfAssociation(row, association, fragmentId,	groupId) &&
			   checkDeepHeterogenityOfAssociatedGroups(row, fragmentId, groupId);
	}
	
	/**
	 * This is the first condition to enforce to be sure that deep heterogenity
	 * isn't broken by the association we are creating.
	 *
	 * @param row the row
	 * @param association the association
	 * @param fragmentId the fragment id
	 * @param groupId the group id
	 * @return true, if successful
	 */
	private boolean checkDeepHeterogenityOfAssociation(Row row, List<Integer> association, int fragmentId, int groupId) {
		// for every constraintId insisting on fragmentId
		for (int constraintId: constraints.constraintsFor(fragmentId)) {
			// for every fragment1 involved in the constraint
			for (int fragment1: constraints.involvedFragmentsFor(constraintId)) {
				// if fragment1 is the same as fragmentId, the group1 id groupId
				// if fragment1 is bigger than association we haven't decided a group yet so we pass -1
				//    an ever-empty group that acts like a check. If we can't associate the row to an empty group
				//    it means that the problem is in the already associated groups
				int group1 = (fragment1 == fragmentId) ? groupId : (fragment1 >= association.size() ? -1 : association.get(fragment1));
				// for every other association that has group1 in fragment1
				for (List<Integer> otherAssociation: associations.getAssociated(fragment1, group1)) {
					// that is not the same as the first one
					if (!otherAssociation.equals(association)) {
						// there must be at least a fragment2
						boolean found = false;
						// involved in the constraint
						for (int fragment2: constraints.involvedFragmentsFor(constraintId)) {
							// and different from fragment1
							if (fragment2 != fragment1) {
								// if fragment2 is the same as fragmentId, the group2 id groupId
								// if fragment2 is bigger than association we haven't decided a group yet so we pass -1
								//    an ever-empty group that acts like a check. If we can't associate the row to an empty group
								//    it means that the problem is in the already associated groups
								int group2 = (fragment2 == fragmentId) ? groupId : (fragment2 >= association.size() ? -1 : association.get(fragment2));
								// that breaks the constraint
								if (!areGroupsAlike(group2, otherAssociation.get(fragment2), fragment2, constraintId, row)) {
									found = true;
									break;
								}
							}
						}
						// if such a fragment2 doesn't exist, than deep association is violated
						if (!found) {
							logger.trace("DEEP ASSOCIATION VIOLATED fragment {} group {}", fragmentId, groupId);
							return false;
						}
					}
				}
			}
		}
		
		// if every condition is verified we can return true
		return true;
	}
	
	/**
	 * Check deep heterogenity of associated groups.
	 *
	 * @param row the row
	 * @param fragmentId the fragment id
	 * @param groupId the group id
	 * @return true, if successful
	 */
	private boolean checkDeepHeterogenityOfAssociatedGroups(Row row, int fragmentId, int groupId) {
		Set<Integer> constraintsFor = this.constraints.constraintsFor(fragmentId);
		
		// for every pre-existing link
		for(List<Integer> association1: associations.getAssociated(fragmentId, groupId)) {
			// for every fragment
			for (int fragment1 = 0; fragment1 < fragments.size(); ++fragment1) {
				// that is not the same we are associating
				if (fragment1 != fragmentId) {
					// I can already know which constraints can break,
					// only the constraints that are insisting on both fragmentId and fragment1
					HashSet<Integer> constraintsToCheck = new HashSet<Integer>(this.constraints.constraintsFor(fragment1));
					constraintsToCheck.retainAll(constraintsFor);
					
					// for every other association that includes association1[fragment1] as group for fragment1
					for (List<Integer> association2: associations.getAssociated(fragment1, association1.get(fragment1))) {
						// that is not the same as association1
						if (!association1.equals(association2)) {
							// for every breakable constraint
							for (int constraintId: constraintsToCheck) {
								// there must be at least a fragment2 
								boolean found = false;
								// involved in the constraint
								for (int fragment2: this.constraints.involvedFragmentsFor(constraintId)) {
									// with fragment2 different than fragment1
									if (fragment2 != fragment1) {
										// that breaks the association [not the constraint]
										if (!areGroupsAlike(association1.get(fragment2), association2.get(fragment2), fragment2, constraintId,
												// if fragment2 is the same as fragmentId I have to inform areGroupsAlike that I want to insert the row exactly there
												(fragment2 == fragmentId ? row : null))) {
											found = true;
											break;
										}
									}
								}
								// if such a fragment2 doesn't exist, than deep association is violated
								if (!found) {
									logger.trace("DEEP ASSOCIATED VIOLATED fragment {} group {}", fragmentId, groupId);
									return false;
								}
							}
						}
					}
				}
			}
		}
		
		// if every condition is verified we can return true
		return true;
	}
	
	/**
	 * Find an association for the row that doesn't break any
	 * property. If no association is found it returns an empty list.
	 *
	 * @param row the row to associate
	 * @return the list (can be empty if an association is not found)
	 */
	private List<Integer> findAssociation(Row row) {
		List<Integer> association = new ArrayList<Integer>(fragments.size());
		extendAssociation(row, association);
		return association;
	}
	
	/**
	 * Extend an association with another fragment checking that
	 * this new group in the fragment doesn't break any property.
	 *
	 * @param row the row to associate
	 * @param association the association
	 * @return true, if the association is ok
	 */
	private boolean extendAssociation(Row row, List<Integer> association) {
		if (association.size() == fragments.size())
			return true;
		
		int fragmentId = association.size();
		for (Integer groupId: getNonFullGroups(fragmentId)) {
			if (checkHeterogenity(row, association, fragmentId, groupId)) {
				association.add(groupId);
				if (extendAssociation(row, association))
					return true;
				else
					association.remove(association.size() - 1);
			}
		}
		return false;
	}
	
	/**
	 * Returns the neighbors groups that are full.
	 *
	 * @param fragmentId the fragment id
	 * @param groupId the group id
	 * @return the generable
	 */
	private Generable<Integer> fullNeighborsGroups(final int fragmentId, int groupId) {
		Generable<Integer> newGroupIds = neighbors(groupId, 0, fragments.get(fragmentId).getLastUsable());
		Function<Integer, Boolean> full = new Function<Integer, Boolean>() {
			public Boolean apply(Integer newGroupId) {
				return associations.isGroupFull(fragmentId, newGroupId);
			}
		};
		return newGroupIds.filter(full);
	}
	
	/**
	 * Redistribute a group and check that everything is still ok.
	 *
	 * @param fragmentId the fragment id
	 * @param groupId the group id
	 */
	private void redistributeGroup(int fragmentId, int groupId) {
		logger.debug("redistributing group {} in fragment {}", groupId, fragmentId);
		for (Row row: getGroupData(fragmentId, groupId)) {
			final List<Integer> association = Collections.unmodifiableList(associations.get(row.id()));
			if (!association.isEmpty()) {
				boolean found = false;
				for (int newGroupId: fullNeighborsGroups(fragmentId, groupId)) {
					if (checkHeterogenity(row, association, fragmentId, newGroupId)) {
						logger.debug("fragment {} row {} : group {} -> group {}", fragmentId, row.id(), groupId, newGroupId);
						associations.changeGroup(row.id(), fragmentId, newGroupId);
						found = true;
						break;
					}
				}
				if (!found) {
					logger.debug("fragment {} group {} : row {} not reallocable", fragmentId, row.id(), groupId);
					opstack.push(Operation.ofDeletion(row.id()));
				}
			}
		}
	}
	
	/**
	 * Delete a row and check that everything is still ok.
	 *
	 * @param rowId the row id
	 */
	private void deleteRow(int rowId) {
		dropped.add(rowId);
		List<Integer> association = associations.get(rowId);
		associations.remove(rowId);
		
		ListIterator<Integer> iter = association.listIterator();
		while (iter.hasNext()) {
			int fragmentId = iter.nextIndex();
			int groupId = iter.next();
			
			// add nonFull groups to operation queue for redistributing
			if (!associations.isGroupFull(fragmentId, groupId))
				opstack.push(Operation.ofRedistribution(fragmentId, groupId));
		}
		logger.info("row {} deleted", rowId);
	}
	
	/**
	 * Update first nonfull.
	 *
	 * @param association the association
	 */
	private void updateFirstNonfull(final List<Integer> association) {
		ListIterator<Integer> iter = association.listIterator();
		while (iter.hasNext()) {
			int fragmentId = iter.nextIndex();
			Fragment fragment = fragments.get(fragmentId);
			int groupId = iter.next();
			if (groupId == fragment.getFirstNonfull()) {
				int newFirstNonfull = groupId;
				while (associations.isGroupFull(fragmentId, newFirstNonfull))
					++newFirstNonfull;
				fragment.setFirstNonfull(newFirstNonfull);
			}
		}
	}
	
	/**
	 * Update last usable.
	 *
	 * @param association the association
	 */
	private void updateLastUsable(final List<Integer> association) {
		ListIterator<Integer> iter = association.listIterator();
		while (iter.hasNext()) {
			int fragmentId = iter.nextIndex();
			int groupId = iter.next();
			fragments.get(fragmentId).setLastUsableIfBigger(groupId + 1);
		}
	}
	
	/**
	 * Update pointers to nonfull and lastusable.
	 *
	 * @param association the association
	 */
	private void updatePointers(final List<Integer> association) {
		updateFirstNonfull(association);
		updateLastUsable(association);
	}
	
	/* (non-Javadoc)
	 * @see core.LooseInterface#associate(java.util.List, int)
	 */
	@Override
	public Triplet<BaseAssociations, BaseFragments, Set<Integer>> associate(final List<Integer> kList) throws Exception {
		
		Timer timer = new Timer();
		isStopped = false;
		
		fragments.setKList(kList);
		
		this.associations = new Associations(kList);
		this.dropped = new HashSet<Integer>();
		
		logger.info("{} starting first scan", timer);
		for (Row row: table) {
			
			// quit operation if isStopped
			if (isStopped)
				return null;
			
			int rowId = row.id();
			if ((rowId % notifyObserversEveryNRows == 0)) {
				logger.info("{} associating row {}", timer, rowId);
				setChanged();
				notifyObservers(Triplet.of(Status.FIRST_SCAN, rowId, dropped.size()));
			}
			
			List<Integer> association = findAssociation(row);
			if (association.isEmpty()) {
				// not associable
				dropped.add(rowId);
				logger.info("row {} dropped at first scan", rowId);
			} else {
				// associated
				associations.put(rowId, association);
				updatePointers(association);
				logger.trace("row {} associated: {}", rowId, association);
			}
		}

		logger.info("{} first scan done, starting second scan", timer);
		
		opstack = new LinkedStack<Operation>();
		
		// add nonfull groups to redistribute queue
		for (int fragmentId = 0; fragmentId < fragments.size(); ++fragmentId)
			for (int groupId: getNonFullGroups(fragmentId))
				if (associations.getGroupSize(fragmentId, groupId) != 0)
					opstack.push(Operation.ofRedistribution(fragmentId, groupId));
		
		int operations = 0;
		Operation operation;
		
		while (!opstack.isEmpty()) {
			
			// quit operation if isStopped
			if (isStopped)
				return null;
			
			if ((operations++) % notifyObserversEveryNOperations == 0) {
				logger.info("{} opstack size: {}", timer, opstack.size());
				setChanged();
				notifyObservers(Triplet.of(Status.SECOND_SCAN, opstack.size(), dropped.size()));
			}
			
			operation = opstack.pop();
			int[] args = operation.getArgs();
			
			switch (operation.getAction()) {
			case REDISTRIBUTE:
				int fragmentId = args[0];
				int groupId = args[1];
				redistributeGroup(fragmentId, groupId);
				break;
				
			case DELETE:
				int rowId = args[0];
				deleteRow(rowId);
				break;
				
			default:
				throw new Exception("Unkown operation " + operation.getAction().name());
			}
		}

		setChanged();
		notifyObservers(Triplet.of(Status.DONE, (int) timer.get(), dropped.size()));
		logger.info("{} second scan done after {} stack operations", timer, operations);
		this.elapsedTime = timer.toString();
		
		return Triplet.of(this.associations, this.fragments, this.dropped);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (associations == null)
			return "== Loose not associated yet ==";
		
		StringBuilder sb = new StringBuilder(200);
		sb.append("== Loose association completed " + elapsedTime + " ==\n\n");
		sb.append("Average group sizes:\n");
		for (Fragment fragment: fragments)
			sb.append("Fragment " + fragment.id + ": requested " + fragment.getK() +
					" -> got " + associations.getAverageGroupSizeInFragment(fragment.id) + "\n");
		
		sb.append("\n" + dropped.size() + " (" + 100.0 * dropped.size() / tuples + "%) lines dropped: " + dropped);
		return sb.toString();
	}

	public void stop() {
		isStopped  = true;
	}

}
