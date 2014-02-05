package core.fragments;

import java.util.List;

/**
 * The Class BaseFragments represents a list of fragments.
 */
public abstract class BaseFragments implements Iterable<Fragment> {

	/**
	 * Gets the fragment at fragmentId.
	 *
	 * @param fragmentId the fragment id
	 * @return the fragment
	 */
	public abstract Fragment get(int fragmentId);

	/**
	 * Returns the number of fragments.
	 *
	 * @return the number of fragments
	 */
	public abstract int size();

	/**
	 * Sets the k list (the size of the kList must be the
	 * same of the number of fragments).
	 *
	 * @param kList the new k list
	 */
	public void setKList(List<Integer> kList) {
		// check kList size
		if (kList.size() != this.size())
			throw new IllegalArgumentException("kList size [" + kList.size() + "] differs from fragments size [" + this.size() + "]");

		// set k to each fragment
		for (int i = 0; i < this.size(); ++i)
			this.get(i).setK(kList.get(i));
	}

}
