package utils.stacks;

import java.util.EmptyStackException;

/**
 * The Interface Stack represents a last-in-first-out (LIFO) stack of objects.
 * The Stack interface Java should implements (without subclassing Vector).
 * The usual push and pop operations are provided, as well as a method to peek at
 * the top item on the stack, a method to test for whether the stack is empty.
 * 
 * When a stack is first created, it contains no items.
 *
 * @param <T> the generic of elements in the stack
 */
public interface Stack<T> {
	
	/**
	 * Pushes an item onto the top of this stack.
	 *
	 * @param item the item to be pushed onto this stack
	 * @return the item argument
	 */
	public abstract T push(T item);
	
	/**
	 * Looks at the object at the top of this stack without removing it from the stack.
	 *
	 * @return the object at the top of this stack (the last item of the Vector object)
	 * @throws EmptyStackException if the stack is empty
	 */
	public abstract T peek() throws EmptyStackException;
	
	/**
	 * Removes the object at the top of this stack and returns that object as the value of this function.
	 *
	 * @return the object at the top of this stack (the last item of the Vector object)
	 * @throws EmptyStackException if the stack is empty
	 */
	public abstract T pop() throws EmptyStackException;
	
	/**
	 * Tests if this stack has no items.
	 *
	 * @return true if and only if this stack has no components, that is, its size is zero; false otherwise.
	 */
	public abstract boolean isEmpty();
	
	/**
	 * Returns the number of items in the stack;.
	 *
	 * @return the number of items currently in the stack;
	 */
	public abstract int size();

}
