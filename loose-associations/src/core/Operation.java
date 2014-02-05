package core;

import java.util.Arrays;

/**
 * The Class Operation represent an operation in the stack.
 */
public final class Operation {
	
	/**
	 * The action to perform.
	 */
	public static enum Action {
		
		/** delete a row. */
		DELETE,
		
		/** redistribute a group. */
		REDISTRIBUTE
	}
	
	/** The action. */
	private final Action action;
	
	/** The arguments of the operation. */
	private final int[] args;
	
	/**
	 * Instantiates a new operation.
	 *
	 * @param action the action
	 * @param args the arguments of the operation
	 */
	public Operation(Action action, int ... args) {
		this.action = action;
		this.args = args;
	}
	
	/**
	 * Creates a new operation.
	 *
	 * @param action the action
	 * @param args the arguments of the operation
	 * @return the operation
	 */
	public static Operation of(Action action, int ... args) {
		return new Operation(action, args);
	}
	
	/**
	 * Creates a new operation of redistribution.
	 *
	 * @param args the arguments of the operation
	 * @return the operation
	 */
	public static Operation ofRedistribution(int ... args) {
		return new Operation(Action.REDISTRIBUTE, args);
	}
	
	/**
	 * Creates a new operation of deletion.
	 *
	 * @param args the arguments of the operation
	 * @return the operation
	 */
	public static Operation ofDeletion(int ... args) {
		return new Operation(Action.DELETE, args);
	}
	
	/**
	 * Gets the action.
	 *
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}
	
	/**
	 * Gets the arguments of the operation.
	 *
	 * @return the arguments of the operation
	 */
	public int[] getArgs() {
		return args;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Operation [action=" + action + ", args="
				+ Arrays.toString(args) + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + action.hashCode();
		result = prime * result + Arrays.hashCode(args);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Operation))
			return false;
		Operation other = (Operation) obj;
		if (action != other.action)
			return false;
		if (!Arrays.equals(args, other.args))
			return false;
		return true;
	}
	
	
	
	
	
}
