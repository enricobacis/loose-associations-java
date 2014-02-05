package utils.stacks;

import java.util.ArrayDeque;
import java.util.EmptyStackException;
import java.util.NoSuchElementException;

/**
 * The Class ArrayStack implements a stack using an ArrayDeque.
 * This is the suggested creation of a Stack in the Oracle Java
 * Documentation, but this class hides the methods that would
 * allow a user to use the class in a non-lifo way.
 * 
 * This is not thread-safe. Use VectorStack if you want a thread-safe
 * Stack class.
 *
 * @param <T> the generic of elements in the stack
 */
public class ArrayStack<T> implements Stack<T> {
	
	/** The internal stack. */
	private ArrayDeque<T> stack; 

	/**
	 * Instantiates a new array stack with initial capacity of 10.
	 */
	public ArrayStack() {
		this(10);
	}
	
	/**
	 * Instantiates a new array stack with the specified initial capacity.
	 *
	 * @param initialCapacity the initial capacity
	 */
	public ArrayStack(int initialCapacity) {
		stack = new ArrayDeque<T>(initialCapacity);
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
			throw new  EmptyStackException();
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
			throw new  EmptyStackException();
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
