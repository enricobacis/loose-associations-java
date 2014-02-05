package core.associations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import utils.DefaultHashMap;
import utils.functional.Generable;

/**
 * The Class BaseAssociations keeps all the associations and
 * index the groups in the fragment in order to speed up the
 * get of rows in groups. The indices are automatically created
 * upon put and changed accordingly to other map operations.
 * A set of full groups in every fragment in kept in order to
 * speed up the check of full groups.
 */
public abstract class BaseAssociations extends DefaultHashMap<Integer, List<Integer>> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2661845623251593843L;

	/**
	 * Instantiates a new base associations.
	 */
	public BaseAssociations() {
		super(ArrayList.class);
	}
	
	/* (non-Javadoc)
	 * @see utils.DefaultHashMap#get(java.lang.Object)
	 */
	@Override
    public List<Integer> get(Object key) {
		return Collections.unmodifiableList(super.get(key));
    }
	
	/**
	 * Modifiable version of get.
	 *
	 * @param key the key
	 * @return the association
	 */
	protected List<Integer> getModifiable(Object key) {
		return super.get(key);
	}
	
	/**
	 * Change group in a fragment for a row.
	 *
	 * @param rowId the row id
	 * @param fragmentId the fragment id
	 * @param newGroupId the new group id
	 * @throws IllegalArgumentException if the row is not associated yet
	 */
	public abstract void changeGroup(int rowId, int fragmentId, int newGroupId) throws IllegalArgumentException;

	/**
	 * Checks if is group full.
	 *
	 * @param fragment the fragment
	 * @param groupId the group id
	 * @return true, if is group full
	 */
	public abstract boolean isGroupFull(int fragment, int groupId);

	/**
	 * Gets the group (fragmentId, groupId).
	 *
	 * @param fragmentId the fragment id
	 * @param groupId the group id
	 * @return the group
	 */
	public abstract Set<Integer> getGroup(int fragmentId, int groupId);

	/**
	 * Gets the group size of (fragmentId, groupId).
	 *
	 * @param fragmentId the fragment id
	 * @param groupId the group id
	 * @return the group size
	 */
	public abstract int getGroupSize(int fragmentId, int groupId);

	/**
	 * Gets the groups in a fragment.
	 *
	 * @param fragmentId the fragment id
	 * @return the groups
	 */
	public abstract Map<Integer, Set<Integer>> getGroups(int fragmentId);

	/**
	 * Gets the average group size for groups in fragmentId.
	 *
	 * @param fragmentId the fragment id
	 * @return the average group size in fragment
	 */
	public abstract double getAverageGroupSizeInFragment(int fragmentId);

	/**
	 * Gets the average group size.
	 *
	 * @return the average group size
	 */
	public abstract double getAverageGroupSize();

	/**
	 * Gets the associations that are in (fragmentId, groupId).
	 *
	 * @param fragmentId the fragment id
	 * @param groupId the group id
	 * @return the associated
	 */
	public abstract Generable<List<Integer>> getAssociated(int fragmentId, int groupId);

	/**
	 * Check if an association from (fragment1, group1) to (fragment2, group2) already exists.
	 *
	 * @param fragment1 the fragment1
	 * @param group1 the group1
	 * @param fragment2 the fragment2
	 * @param group2 the group2
	 * @return true, if successful
	 */
	public abstract boolean exists(int fragment1, int group1, int fragment2, int group2);

}