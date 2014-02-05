package core;

/**
 * The status a problem can be.
 */
public enum Status {
	
	/** The problem is not started yet. */
	IDLE,
	
	/** The problem is performing first scan. */
	FIRST_SCAN,
	
	/** The problem is performing second scan. */
	SECOND_SCAN,
	
	/** The problem is done. */
	DONE

}
