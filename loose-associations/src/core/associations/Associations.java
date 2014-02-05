package core.associations;

import static utils.Functions.average;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import utils.DefaultHashMap;
import utils.functional.Function;
import utils.functional.Generable;

/**
 * The Class Associations.
 */
public class Associations extends BaseAssociations {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3351306272288523938L;

	/** The k list. */
	private List<Integer> kList;
	
	/** The fragments. */
	private int fragments;

	/** The indices. */
	private ArrayList<DefaultHashMap<Integer, Set<Integer>>> indices;

	/** The ids of full groups in a fragment. */
	private ArrayList<TIntSet> fulls;

	/**
	 * Instantiates a new associations.
	 *
	 * @param kList the k list
	 */
	public Associations(List<Integer> kList) {
		this.kList = kList;
		this.fragments = kList.size();
		this.indices = new ArrayList<DefaultHashMap<Integer, Set<Integer>>>();
		this.fulls = new ArrayList<TIntSet>();
		
		for (int f = 0; f < fragments; ++f) {
			this.indices.add(new DefaultHashMap<Integer, Set<Integer>>(HashSet.class));
			this.fulls.add(new TIntHashSet());
		}
	}
	
	/**
	 *  Adds a rowId to a group in a fragment.
	 * This method affects only the indices and not the association, so it has
	 * to be used carefully while adding the association or manually changing the group.
	 *
	 * @param rowId the row to be added.
	 * @param fragmentId the destination fragment.
	 * @param groupId the destination group.
	 */
	private void addToGroup(int rowId, int fragmentId, int groupId) {
		Set<Integer> group = this.indices.get(fragmentId).get(groupId);
		group.add(rowId);
		if (group.size() == this.kList.get(fragmentId))
			this.fulls.get(fragmentId).add(groupId);
	}
	
	/* (non-Javadoc)
	 * @see java.util.HashMap#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public List<Integer> put(Integer rowId, List<Integer> association) {
		if ((association != null) && (!association.isEmpty()) && (association.size() != this.fragments))
			throw new IllegalArgumentException("Wrong value length for " + association + ". Expected length: " + this.fragments);
		
		if (association == null)
			throw new IllegalArgumentException("null association received");
		
		// Remove previous value
		this.remove(rowId);
		
		ListIterator<Integer> li = association.listIterator();
		while(li.hasNext()) {
			int fragmentId = li.nextIndex();
			int groupId = li.next();
			addToGroup(rowId, fragmentId, groupId);
		}
		return super.put(rowId, association);
	}
	
	/**
	 * Removes a rowId from a group in a fragment.
	 * This method affects only the indices and not the association, so it has
	 * to be used carefully while removing the association or manually changing the group.
	 *
	 * @param rowId the row to be removed.
	 * @param fragmentId the source fragment.
	 * @param groupId the source group.
	 */
	private void removeFromGroup(Object rowId, int fragmentId, int groupId) {
		Set<Integer> group = this.indices.get(fragmentId).get(groupId);
		if (group.size() == this.kList.get(fragmentId))
			this.fulls.get(fragmentId).remove(groupId);
		group.remove(rowId);
	}
	
	/* (non-Javadoc)
	 * @see java.util.HashMap#remove(java.lang.Object)
	 */
	@Override
	public List<Integer> remove(Object rowId) {
		if (super.containsKey(rowId)) {
			ListIterator<Integer> association = super.get(rowId).listIterator();
			while (association.hasNext()) {
				int fragmentId = association.nextIndex();
				int groupId = association.next();
				removeFromGroup(rowId, fragmentId, groupId);
			}
		}
		return super.remove(rowId);
	}
	
	/* (non-Javadoc)
	 * @see core.associations.BaseAssociations#changeGroup(int, int, int)
	 */
	@Override
	public void changeGroup(int rowId, int fragmentId, int newGroupId) throws IllegalArgumentException {
		if (!super.containsKey(rowId))
			throw new IllegalArgumentException("Can't change group for row " + rowId + " because an association isn't set yet");
		List<Integer> association = super.getModifiable(rowId);
		int oldGroupId = association.get(fragmentId);
		removeFromGroup(rowId, fragmentId, oldGroupId);
		addToGroup(rowId, fragmentId, newGroupId);
		association.set(fragmentId, newGroupId);
	}
	
	/* (non-Javadoc)
	 * @see core.associations.AssociationsInterface#isGroupFull(int, int)
	 */
	@Override
	public boolean isGroupFull(int fragmentId, int groupId) {
		return this.fulls.get(fragmentId).contains(groupId);
	}

	/* (non-Javadoc)
	 * @see core.associations.AssociationsInterface#getGroup(int, int)
	 */
	@Override
	public Set<Integer> getGroup(int fragmentId, int groupId) {
		return Collections.unmodifiableSet(this.indices.get(fragmentId).get(groupId));
	}

	/* (non-Javadoc)
	 * @see core.associations.AssociationsInterface#getGroupSize(int, int)
	 */
	@Override
	public int getGroupSize(int fragmentId, int groupId) {
		return this.indices.get(fragmentId).get(groupId).size();
	}

	/* (non-Javadoc)
	 * @see core.associations.AssociationsInterface#getGroups(int)
	 */
	@Override
	public Map<Integer, Set<Integer>> getGroups(int fragmentId) {
		return Collections.unmodifiableMap(this.indices.get(fragmentId));
	}

	/* (non-Javadoc)
	 * @see core.associations.AssociationsInterface#getAverageGroupSizeInFragment(int)
	 */
	@Override
	public double getAverageGroupSizeInFragment(int fragmentId) {
		List<Integer> lengths = new LinkedList<Integer>();
		for (Set<Integer> group: this.indices.get(fragmentId).values())
			if (group != null && !group.isEmpty())
				lengths.add(group.size());
		return average(lengths);
	}

	/* (non-Javadoc)
	 * @see core.associations.AssociationsInterface#getAverageGroupSize()
	 */
	@Override
	public double getAverageGroupSize() {
		List<Double> fragmentAverages = new LinkedList<Double>();
		for (int fragment = 0; fragment < this.fragments; ++fragment)
			fragmentAverages.add(getAverageGroupSizeInFragment(fragment));
		return average(fragmentAverages);
	}

	/* (non-Javadoc)
	 * @see core.associations.AssociationsInterface#getAssociated(int, int)
	 */
	@Override
	public Generable<List<Integer>> getAssociated(final int fragmentId, final int groupId) {
		Function<Integer, List<Integer>> rowIdToAssociation = new Function<Integer, List<Integer>>() {
			public List<Integer> apply(Integer rowId) {
				return Associations.this.get(rowId);
			}
		};
		
		return Generable.from(this.indices.get(fragmentId).get(groupId), rowIdToAssociation);
	}

	/* (non-Javadoc)
	 * @see core.associations.AssociationsInterface#exists(int, int, int, int)
	 */
	@Override
	public boolean exists(int fragment1, int group1, int fragment2, int group2) {
		for (List<Integer> associated: getAssociated(fragment2, group2))
			if (associated.get(fragment1) == group1)
				return true;
		return false;
	}
	
}
