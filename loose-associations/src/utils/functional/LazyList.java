package utils.functional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The Class LazyList.
 *
 * @param <T> the generic type
 */
public class LazyList<T> implements Iterable<T> {
	
	/** The input. */
	private Iterator<T> input;
	
	/** The list. */
	private List<T> list;

	/**
	 * Instantiates a new lazy list.
	 *
	 * @param input the input
	 */
	public LazyList(Iterator<T> input) {
		this.input = input;
		this.list = new ArrayList<T>();
	}
	
	/**
	 * Instantiates a new lazy list.
	 *
	 * @param input the input
	 */
	public LazyList(Iterable<T> input) {
		this(input.iterator());
	}
	
	/**
	 * From.
	 *
	 * @param <T> the generic type
	 * @param iterator the iterator
	 * @return the lazy list
	 */
	public static <T> LazyList<T> from(Iterator<T> iterator) {
		return new LazyList<T>(iterator);
	}
	
	/**
	 * From.
	 *
	 * @param <T> the generic type
	 * @param iterable the iterable
	 * @return the lazy list
	 */
	public static <T> LazyList<T> from(Iterable<T> iterable) {
		return from(iterable.iterator());
	}
	
	/**
	 * Insert an element that will be extracted from the next call of next.
	 * We can only insert elements at the end of the most advanced iterator,
	 * so all the other iterators are in sync.
	 *
	 * @param element the element
	 */
	public void insert(T element) {
		list.add(element);
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			
			int currentIndex = 0;

			@Override
			public boolean hasNext() {
				return ((currentIndex < list.size()) || (input.hasNext()));
			}

			@Override
			public T next() {
				if (currentIndex == list.size())
					list.add(input.next());
				return list.get(currentIndex++);
			}

			@Override
			public void remove() { }
		};
	}

}
