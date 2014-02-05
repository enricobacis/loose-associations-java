package core.fragments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * The Class Fragment represents a single fragment
 * in a schema fragmentation with its attributes.
 */
public class Fragment {

	/** The list of attribute indices in a fragment. */
	final private List<Integer> fragment;
	
	/** The id of the fragment. */
	final public int id;
	
	/** The first nonfull keeps track of the first nonfull group in the fragment. */
	private int firstNonfull = 0;
	
	/** The last usable is the last group to be checked by a greedy algorithm (it's a "no solutions after here"). */
	private int lastUsable = 0;
	
	/** The protection (privacy) factor k of the fragment. */
	private int k = 1;

	/**
	 * Instantiates a new fragment.
	 *
	 * @param id the id
	 * @param fragment the attribute indices in the fragment
	 */
	Fragment(int id, Collection<Integer> fragment) {
		this.id = id;
		this.fragment = Collections.unmodifiableList(new ArrayList<Integer>(fragment)); 
	}
	
	/**
	 * Gets the attributes indices.
	 *
	 * @return the attributes indices
	 */
	public List<Integer> getAttributes() {
		return fragment;
	}

	/**
	 * Sets protection (privacy) factor k of the fragment.
	 *
	 * @param k the new k
	 */
	public void setK(int k) {
		this.k = k;
		resetPointers();
	}
	
	/**
	 * Gets protection (privacy) factor k of the fragment.
	 *
	 * @return the k
	 */
	public int getK() {
		return k;
	}

	/**
	 * Reset pointers of first nonfull and lastusable to zero.
	 */
	public void resetPointers() {
		this.setFirstNonfull(0);
		this.setLastUsable(0);
	}

	/**
	 * Gets the first nonfull.
	 *
	 * @return the first nonfull
	 */
	public int getFirstNonfull() {
		return firstNonfull;
	}

	/**
	 * Sets the first nonfull.
	 *
	 * @param firstNonfull the new first nonfull
	 */
	public void setFirstNonfull(int firstNonfull) {
		this.firstNonfull = firstNonfull;
	}

	/**
	 * Gets the last usable.
	 *
	 * @return the last usable
	 */
	public int getLastUsable() {
		return lastUsable;
	}

	/**
	 * Sets the last usable.
	 *
	 * @param lastUsable the new last usable
	 */
	public void setLastUsable(int lastUsable) {
		this.lastUsable = lastUsable;
	}
	
	/**
	 * Sets the last usable if bigger than the current one.
	 *
	 * @param groupId the new last usable
	 */
	public void setLastUsableIfBigger(int groupId) {
		if (this.lastUsable < groupId)
			this.lastUsable = groupId;
	}

}
