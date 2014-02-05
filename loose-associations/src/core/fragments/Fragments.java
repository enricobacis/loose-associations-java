package core.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * The Class Fragments implements BaseFragments using a List.
 */
public class Fragments extends BaseFragments {
	
	/** The fragments. */
	private List<Fragment> fragments;

	/**
	 * Instantiates a new fragments.
	 *
	 * @param fragments the fragments list
	 */
	public Fragments(List<List<Integer>> fragments) {
		this.fragments = new ArrayList<Fragment>(fragments.size());
		for (int id = 0; id < fragments.size(); ++id)
			this.fragments.add(new Fragment(id, fragments.get(id)));
		this.fragments = Collections.unmodifiableList(this.fragments);
	}
	
	/* (non-Javadoc)
	 * @see core.fragments.BaseFragments#get(int)
	 */
	@Override
	public Fragment get(int fragmentId) {
		return fragments.get(fragmentId);
	}
	
	/* (non-Javadoc)
	 * @see core.fragments.BaseFragments#size()
	 */
	@Override
	public int size() {
		return fragments.size();
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Fragment> iterator() {
		return this.fragments.iterator();
	}

}
