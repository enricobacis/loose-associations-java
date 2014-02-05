package utils.stacks;

import java.util.EmptyStackException;
import java.util.Vector;

/**
 * The Class VectorStack implements Stack using a Vector.
 * This Stack is thread-safe because the push and pop operations
 * are synchronized so only one thread can execute them at one time.
 *
 * @param <T> the generic of elements in the stack
 */
public class VectorStack<T> implements Stack<T> {
	
	/** The internal stack. */
	private final Vector<T> stack;

	/**
	 * Instantiates a new vector stack. Its internal vector has size 10.
	 */
	public VectorStack() {
		this(10);
	}
	
	/**
	 * Instantiates a new vector stack with the specified initial capacity.
	 *
	 * @param initialCapacity the initial capacity
	 */
	public VectorStack(int initialCapacity) {
		stack = new Vector<T>(initialCapacity);
	}

	/* (non-Javadoc)
	 * @see stack.Stack#push(java.lang.Object)
	 */
	@Override
	public T push(T item) {
		stack.addElement(item);
		return item;
	}

	/* (non-Javadoc)
	 * @see stack.Stack#peek()
	 */
	@Override
	public T peek() throws EmptyStackException {
		int size = stack.size();
        if (size == 0)
            throw new EmptyStackException();
        return stack.elementAt(size - 1);
	}

	/* (non-Javadoc)
	 * @see stack.Stack#pop()
	 */
	@Override
	public synchronized T pop() throws EmptyStackException {
        int size = stack.size();
        T obj = this.peek();
        stack.removeElementAt(size - 1);
        return obj;
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
