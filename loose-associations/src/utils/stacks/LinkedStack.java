package utils.stacks;

import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * The Class LinkedStack implements a stack using LinkedList.
 * This is not thread-safe. Use VectorStack if you want a thread-safe
 * Stack class.
 *
 * @param <T> the generic of elements in the stack
 */
public class LinkedStack<T> implements Stack<T> {
	
	/** The internal stack. */
	private final LinkedList<T> stack;

	/**
	 * Instantiates a new linked stack.
	 */
	public LinkedStack() {
		stack = new LinkedList<T>();
	}

	/* (non-Javadoc)
	 * @see stack.Stack#push(java.lang.Object)
	 */
	@Override
	public T push(T item) {
		stack.addFirst(item);
		return item;
	}

	/* (non-Javadoc)
	 * @see stack.Stack#peek()
	 */
	@Override
	public T peek() throws EmptyStackException {
		try {
			return stack.getFirst();
		} catch (NoSuchElementException e) {
			throw new EmptyStackException();
		}
	}

	/* (non-Javadoc)
	 * @see stack.Stack#pop()
	 */
	@Override
	public T pop() throws EmptyStackException {
		try {
			return stack.removeFirst();
		} catch (NoSuchElementException e) {
			throw new EmptyStackException();
		}
	}

	/* (non-Javadoc)
	 * @see stack.Stack#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return stack.isEmpty();
	}

	/* (non-Javadoc)
	 * @see stack.Stack#size()
	 */
	@Override
	public int size() {
		return stack.size();
	}

}
